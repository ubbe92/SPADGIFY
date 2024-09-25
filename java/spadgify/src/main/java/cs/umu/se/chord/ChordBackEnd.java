package cs.umu.se.chord;

import cs.umu.se.util.ChordUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.Chord;
import proto.NodeGrpc;

public class ChordBackEnd {

    private Node node;
    private ManagedChannel channel;
    private NodeGrpc.NodeBlockingStub blockingStub;
    private ChordUtil chordUtil = new ChordUtil();

    public ChordBackEnd(Node node) {
        this.node = node;
    }

    // node (node) joins the network;
    // nodePrime (n') is an arbitrary node in the network
    public synchronized void join(Node nodePrime) {
        System.out.println("join()");

        // Make grpc call to node, "ping it"

        if (nodePrime != null) { // Temp condition, should be a boolean like "nodePrimeIsAlive"
            System.out.println("node " + node + " is joining node: " + nodePrime);
            initFingerTable(nodePrime);
            updateOthers();

            // move keys in (predecessor; n] from successor

        } else { // node is the only node in the network
            System.out.println("node is the only node in the network");
            FingerTableEntry[] table = node.getFingerTable().getTable();
            for (int i = 0; i < node.getM(); i++) {
                table[i].setNode(node);
            }
            node.setPredecessor(node);
        }

        node.displayCurrentTable();
    }

    private void initChannelAndStub(String ip, int port) {
        this.channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
        this.blockingStub = NodeGrpc.newBlockingStub(channel);
    }

    private void shutdownChannel(ManagedChannel channel) {
        channel.shutdown();
    }

    private Node gRPCFindSuccessor(int start) {
        Chord.FindSuccessorRequest request = Chord.FindSuccessorRequest.newBuilder().setId(start).build();
        Chord.FindSuccessorReply reply = blockingStub.findSuccessor(request);
        Chord.ChordNode chordNode = reply.getChordNode();

        Node successor = chordUtil.createNodeFromGRPCChordNode(chordNode);
        return successor;
    }

    // initialize finger table of local node;
    // n' is an arbitrary node already in the network
    public synchronized void initFingerTable(Node nodePrime) {
        System.out.println("initFingerTable()");
        FingerTableEntry[] table = node.getFingerTable().getTable();
        int start = table[0].getStart();

        initChannelAndStub(nodePrime.getMyIp(), nodePrime.getMyPort());
        Node successor = gRPCFindSuccessor(start);
        System.out.println("Our successor is: " + successor);

        table[0].setNode(successor);
        node.setPredecessor(successor.getPredecessor());
        successor.setPredecessor(node);

        int m = node.getM();
        for (int i = 0; i < m - 1; i++) {
            int fingerStart = table[i+1].getStart();
            Node fingerNode = table[i].getNode();

            if (isStartInIntervalNodeToFingerNodeI(fingerStart, node, fingerNode)) {
                table[i+1].setNode(fingerNode);
            } else {
                Node n = gRPCFindSuccessor(fingerStart);
                table[i+1].setNode(n);
            }
        }
    }

    public synchronized void updateOthers() {
        System.out.println("updateOthers()");
    }

    public synchronized void updateFingerTable() {
        System.out.println("updateFingerTable()");
    }


    // ask node n to find id’s successor
    public synchronized Node findSuccessor(int id) {
        System.out.println("Find successor()");
        Node nodePrime = findPredecessor(id);
        return nodePrime.getSuccessor();
    }

    private boolean isNodeAlone(Node node) {
         return node.getSuccessor().getMyIdentifier() == node.getMyIdentifier()
                 && node.getPredecessor().getMyIdentifier() == node.getMyIdentifier();
    }

    // ask node n to find id’s predecessor
    public synchronized Node findPredecessor(int id) {
        System.out.println("Find predecessor()");
        Node nodePrime = node;

        if (isNodeAlone(nodePrime))
            return nodePrime;

        // closestPrecedingFinger needs to be a gRPC function that can also call other nodes
//        while (!isIdInIntervalNodePrimeToNodePrimeSuccessor(id, nodePrime, nodePrime.getSuccessor())) {
//            nodePrime = closestPrecedingFinger(id);
//        }

        return nodePrime;
    }


    // return closest finger preceding id
    public synchronized Node closestPrecedingFinger(int id) {
        System.out.println("Closest preceding finger()");

        int m = node.getM();
        FingerTableEntry[] table = node.getFingerTable().getTable();

        for (int i = m - 1; i >= 0; i--) {
            Node fingerNode = table[i].getNode();

            if (isFingerNodeIdentifierInIntervalNodeToId(fingerNode, node, id))
                return fingerNode;
        }

        return node;
    }

    public boolean isIdInIntervalNodePrimeToNodePrimeSuccessor(int nodeIdentifier, Node nodePrime, Node successor) {

        int leftBound = nodePrime.getMyIdentifier();
        int rightBound = successor.getMyIdentifier();
        int m = node.getM();

        System.out.println("id: " + nodeIdentifier + " left: " + leftBound + " right: " + rightBound);


        if (leftBound <= rightBound) {
            System.out.println("isIdInIntervalNodePrimeToNodePrimeSuccessor() Left lessOrEq than right - node identifier: "
                    + nodeIdentifier + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return nodeIdentifier > leftBound && nodeIdentifier <= rightBound;
        }
        else {
            System.out.println("isIdInIntervalNodePrimeToNodePrimeSuccessor() Left greater than right - node identifier: "
                    + nodeIdentifier + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return (nodeIdentifier > leftBound && nodeIdentifier <= m) || (nodeIdentifier > 0 && nodeIdentifier <= rightBound);
        }
    }

    public boolean isFingerNodeIdentifierInIntervalNodeToId(Node fingerNode, Node n, int id) {

        int leftBound = n.getMyIdentifier();
        int rightBound = id;
        int fingerNodeIdentifier = fingerNode.getMyIdentifier();
        int m = node.getM();

        if (leftBound <= rightBound) {
            System.out.println("isFingerNodeIdentifierInIntervalNodeToId() Left lessOrEq than right - node identifier: "
                    + fingerNodeIdentifier + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return fingerNodeIdentifier > leftBound && fingerNodeIdentifier < rightBound;
        }
        else {
            System.out.println("isFingerNodeIdentifierInIntervalNodeToId() Left greater than right - node identifier: "
                    + fingerNodeIdentifier + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return (fingerNodeIdentifier > leftBound && fingerNodeIdentifier < m)
                    || (fingerNodeIdentifier > 0 && fingerNodeIdentifier < rightBound);
        }
    }

    public synchronized boolean isStartInIntervalNodeToFingerNodeI(int fingerStartIPlusOne, Node node, Node fingerNodeI) {
        int leftBound = node.getMyIdentifier();
        int rightBound = fingerNodeI.getMyIdentifier();
        int m = node.getM();

        int maxNodes = (int) Math.pow(2, m);

        if (leftBound <= rightBound) {
            // Simple range, no wrapping
            return fingerStartIPlusOne >= leftBound && fingerStartIPlusOne < rightBound;
        } else {
            // Wrapped range
            return (fingerStartIPlusOne >= leftBound && fingerStartIPlusOne < maxNodes) ||
                    (fingerStartIPlusOne >= 0 && fingerStartIPlusOne < rightBound);
        }
    }
}
