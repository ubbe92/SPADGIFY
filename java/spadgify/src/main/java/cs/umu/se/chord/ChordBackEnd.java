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

    private Node gRPCFindSuccessor(int id, Node nodePrime) {
        System.out.println("gRPCFindSuccessor()");
        initChannelAndStub(nodePrime.getMyIp(), nodePrime.getMyPort());
        Chord.FindSuccessorRequest request = Chord.FindSuccessorRequest.newBuilder().setId(id).build();
        Chord.FindSuccessorReply reply = blockingStub.findSuccessor(request);
        Chord.ChordNode chordNode = reply.getChordNode();
        shutdownChannel(channel);

        Node successor = chordUtil.createNodeFromGRPCChordNode(chordNode);
        return successor;
    }

    private Node gRPCClosestPrecedingFinger(int id, Node nodePrime) {
        System.out.println("gRPC ClosestPrecedingFinger()");
        initChannelAndStub(nodePrime.getMyIp(), nodePrime.getMyPort());
        System.out.println("nodePrime: " + nodePrime);
        System.out.println("Before request CPF");
        Chord.ClosestPrecedingFingerRequest request = Chord.ClosestPrecedingFingerRequest.newBuilder().setId(id).build();
        System.out.println("Before reply CPF");
        Chord.ClosestPrecedingFingerReply reply = blockingStub.closestPrecedingFinger(request);
        System.out.println("Received reply in CPF");
        Chord.ChordNode chordNode = reply.getChordNode();
        shutdownChannel(channel);

        // Closest preceding finger
        Node cpf = chordUtil.createNodeFromGRPCChordNode(chordNode);
        return cpf;
    }

    // initialize finger table of local node;
    // n' is an arbitrary node already in the network
    public synchronized void initFingerTable(Node nodePrime) {
        System.out.println("initFingerTable()");
        FingerTableEntry[] table = node.getFingerTable().getTable();
        int start = table[0].getStart();

        System.out.println("Start: "+ start);
        Node successor = gRPCFindSuccessor(start, nodePrime);
        System.out.println("Our successor is: " + successor);

        table[0].setNode(successor);
        node.setPredecessor(successor.getPredecessor());
        successor.setPredecessor(node);

        int m = node.getM();
        for (int i = 0; i < m - 1; i++) {
            int fingerStart = table[i+1].getStart();
            Node fingerNode = table[i].getNode();

            if (isFingerStartInIntervalIFT(fingerStart, node, fingerNode)) {
                System.out.println("In interval");
                table[i+1].setNode(fingerNode);
            } else {
                System.out.println("Not in interval");
                Node n = gRPCFindSuccessor(fingerStart, nodePrime);
                table[i+1].setNode(n);
            }
        }
    }

    public synchronized void updateOthers() {
        System.out.println("updateOthers()");
        int m = node.getM();

        for (int i = 0; i < m; i++) {
            int id = (node.getMyIdentifier() - ((int) Math.pow(2,i)));
            Node p = findPredecessor(id);

            // Kör p.grpcUpdatefingertable

        }
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

//        if (isNodeAlone(nodePrime))
//            return nodePrime;


        // TODO: Implement this!! gRPCClosestPrecedingFinger
        // closestPrecedingFinger needs to be a gRPC function that can also call other nodes
        while (!isIdInIntervalFP(id, nodePrime, nodePrime.getSuccessor())) {
//            nodePrime = closestPrecedingFinger(id);
            nodePrime = gRPCClosestPrecedingFinger(id, nodePrime);
        }

        return nodePrime;
    }


    // return closest finger preceding id
    public synchronized Node closestPrecedingFinger(int id) {
        System.out.println("Closest preceding finger()");

        int m = node.getM();
        FingerTableEntry[] table = node.getFingerTable().getTable();

        for (int i = m - 1; i >= 0; i--) {
            Node fingerNode = table[i].getNode();

            if (isFingerNodeInIntervalCPF(fingerNode, node, id))
                return fingerNode;
        }

        return node;
    }

    private boolean isIdInIntervalFP(int nodeIdentifier, Node nodePrime, Node successor) {

        int leftBound = nodePrime.getMyIdentifier();
        int rightBound = successor.getMyIdentifier();
        int m = node.getM();

//        System.out.println("id: " + nodeIdentifier + " left: " + leftBound + " right: " + rightBound);

        // If full circle interval e.g. (1, 1]
        if (leftBound == rightBound)
            return true;

        if (leftBound <= rightBound) {
//            System.out.println("isIdInIntervalFP() Left lessOrEq than right - node identifier: "
//                    + nodeIdentifier + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return nodeIdentifier > leftBound && nodeIdentifier <= rightBound;
        }
        else {
//            System.out.println("isIdInIntervalFP() Left greater than right - node identifier: "
//                    + nodeIdentifier + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return (nodeIdentifier > leftBound && nodeIdentifier <= m) || (nodeIdentifier > 0 && nodeIdentifier <= rightBound);
        }
    }

    private boolean isFingerNodeInIntervalCPF(Node fingerNode, Node n, int id) {

        int leftBound = n.getMyIdentifier();
        int rightBound = id;
        int fingerNodeIdentifier = fingerNode.getMyIdentifier();
        int m = node.getM();

        // If full circle e.g. (1, 1)
        if (leftBound == rightBound && fingerNodeIdentifier != leftBound) {
            return true;
        }

        if (leftBound <= rightBound) {
//            System.out.println("isFingerNodeInIntervalCPF() Left lessOrEq than right - node identifier: "
//                    + fingerNodeIdentifier + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return fingerNodeIdentifier > leftBound && fingerNodeIdentifier < rightBound;
        }
        else {
//            System.out.println("isFingerNodeInIntervalCPF() Left greater than right - node identifier: "
//                    + fingerNodeIdentifier + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return (fingerNodeIdentifier > leftBound && fingerNodeIdentifier < m)
                    || (fingerNodeIdentifier > 0 && fingerNodeIdentifier < rightBound);
        }
    }

    private synchronized boolean isFingerStartInIntervalIFT(int fingerStartIPlusOne, Node node, Node fingerNodeI) {
        int leftBound = node.getMyIdentifier();
        int rightBound = fingerNodeI.getMyIdentifier();
        int m = node.getM();

        int maxNodes = (int) Math.pow(2, m);

        // If full circle e.g. [1, 1)
        if (leftBound == rightBound)
            return true;

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
