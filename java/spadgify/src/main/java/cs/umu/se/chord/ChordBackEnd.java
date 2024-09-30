package cs.umu.se.chord;

import cs.umu.se.util.ChordUtil;
import cs.umu.se.workers.StabilizerWorker;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import proto.Chord;
import proto.NodeGrpc;

public class ChordBackEnd {

    private Node node;
    private ManagedChannel channel;
    private NodeGrpc.NodeBlockingStub blockingStub;
    private ChordUtil chordUtil = new ChordUtil();
    private int next = 0;

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

            node.displayCurrentTable();

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

        System.out.println("Finished join!");
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

            if (isNumberInIntervalInclusiveExclusive(fingerStart, node, fingerNode)) {
                System.out.println("fingerStart: " + fingerStart + " in interval: [" + node.getMyIdentifier() + " - " + fingerNode.getMyIdentifier() + ")");
                table[i+1].setNode(fingerNode);
            } else {
                System.out.println("fingerStart: " + fingerStart + " not in interval: [" + node.getMyIdentifier() + " - " + fingerNode.getMyIdentifier() + ")");
                Node n = gRPCFindSuccessor(fingerStart, nodePrime);
                table[i+1].setNode(n);
            }
        }
    }

    public synchronized void updateOthers() {
        System.out.println("updateOthers()");
        int m = node.getM();
        int maxNodes = (int) Math.pow(2, m);

        for (int i = 0; i < m; i++) {
            int id = (node.getMyIdentifier() + 1) - ((int) Math.pow(2, i));

            // wrap around the circle if below zero e.g. -1 == 7 if m == 3
            if (id < 0)
                id += maxNodes;

            id = id % maxNodes;

            Node p = findPredecessor(id);

            if (p.equals(node)) {
                System.out.println("updateOthers() p == node");
                updateFingerTable(node, i);

            // else if p == another node call gRPCUpdateFingerTable
            } else {
                System.out.println("updateOthers() p != node");
                gRPCUpdateFingerTable(node, i, p);
            }
        }
    }

    public synchronized void updateFingerTable(Node s, int i) {
        System.out.println("updateFingerTable()");

        FingerTableEntry[] table = node.getFingerTable().getTable();

        Node fingerNodeI = table[i].getNode();

        if (isNumberInIntervalInclusiveExclusive(s.getMyIdentifier(), node, fingerNodeI)) {
            System.out.println("I: " + i + " s: " + s + " in interval: [" + node.getMyIdentifier() + " - " + fingerNodeI.getMyIdentifier() + ")");

            table[i].setNode(s);
            Node p = node.getPredecessor();


            // if p == this node call local updateFingerTable
            if (p.equals(node)) {

                System.out.println("updateFingerTable() Updating my own fingertable with: " + node + " at i: " + i);

                // infinite recursion??? REMOVE THIS?????!!!!
//                updateFingerTable(node, i);

            // else if p == another node call gRPCUpdateFingerTable
            } else {
                System.out.println("updateFingerTable() Telling: " + p + " to update fingertable with: " + node + " at i: " + i);
                gRPCUpdateFingerTable(s, i, p);
            }
        }
    }

    public synchronized void gRPCUpdateFingerTable(Node s, int i, Node p) {
        System.out.println("gRPCUpdateFingerTable calling node: " + p + " to insert node: " + s + " at index: " + i);
        Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNode(s);

        initChannelAndStub(p.getMyIp(), p.getMyPort());
        Chord.UpdateFingerTableRequest request = Chord.UpdateFingerTableRequest.newBuilder()
                .setI(i)
                .setChordNode(chordNode)
                .build();
        Chord.UpdateFingerTableReply reply = blockingStub.updateFingerTable(request);
        shutdownChannel(channel);
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

        while (!isNumberInIntervalExclusiveInclusive(id, nodePrime, nodePrime.getSuccessor())) {
            // Possible collisions with small m - but we don't care B^)
            System.out.println(id + " is not in the exc/inc interval: " + nodePrime.getMyIdentifier() + " - " + nodePrime.getSuccessor().getMyIdentifier());

            if (nodePrime.equals(node)) {
                System.out.println("Calling my self: " + nodePrime + " with id: " + id);
                nodePrime = closestPrecedingFinger(id);
                System.out.println("nodePrime: " + nodePrime);
            } else {
                System.out.println("Calling someone else: " + nodePrime + " with id: " + id);
                nodePrime = gRPCClosestPrecedingFinger(id, nodePrime);
                System.out.println("nodePrime: " + nodePrime);
            }
        }

        return nodePrime;
    }


    // return closest finger preceding id
    public synchronized Node closestPrecedingFinger(int id) {
        System.out.println("Closest preceding finger() with id: " + id);

        int m = node.getM();
        FingerTableEntry[] table = node.getFingerTable().getTable();

//        node.displayCurrentTable();

        for (int i = m - 1; i >= 0; i--) {
            Node fingerNode = table[i].getNode();

            if (isNodeInIntervalExclusive(fingerNode, node, id)) {
                System.out.println("FingerNode[" + i + "]: " + fingerNode + " in interval: (" + node.getMyIdentifier() + " - " + id + ")");
                return fingerNode;
            }

            System.out.println("FingerNode[" + i + "]: " + fingerNode + " not in interval: (" + node.getMyIdentifier() + " - " + id + ")");

        }
        System.out.println("finger nodes not in interval returning self node: " + node );
        return node;
    }

    private boolean isNumberInIntervalExclusiveInclusive(int nodeIdentifier, Node nodePrime, Node successor) {
        int leftBound = nodePrime.getMyIdentifier();
        int rightBound = successor.getMyIdentifier();
        int m = node.getM();
        int maxNodes = (int) Math.pow(2, m);

        // If full circle interval e.g. (1, 1]
        if (leftBound == rightBound)
            return true;

        // exc/inc e.g. (x, y]
        if (leftBound < rightBound) {
            // Simple range, no wrapping
            return nodeIdentifier > leftBound && nodeIdentifier <= rightBound;
        } else {
            // Wrapped range
            return (nodeIdentifier > leftBound && nodeIdentifier < maxNodes) ||
                    (nodeIdentifier >= 0 && nodeIdentifier <= rightBound);
        }
    }

    private boolean isNodeInIntervalExclusive(Node fingerNode, Node n, int id) {

        int leftBound = n.getMyIdentifier();
        int rightBound = id;
        int fingerNodeIdentifier = fingerNode.getMyIdentifier();
        int m = node.getM();
        int maxNodes = (int) Math.pow(2, m);

        System.out.println("fingerNode: " + fingerNodeIdentifier + " in: (" + leftBound + ", " + rightBound + ")");

        // If full circle e.g. (1, 1)
        if (leftBound == rightBound && fingerNodeIdentifier != leftBound) {
            return true;
        }

        // exc/exc e.g. (x, y)
        if (leftBound < rightBound) {
            // Simple range, no wrapping
            return fingerNodeIdentifier > leftBound && fingerNodeIdentifier < rightBound;
        } else {
            // Wrapped range
            return (fingerNodeIdentifier > leftBound && fingerNodeIdentifier < maxNodes) ||
                    (fingerNodeIdentifier >= 0 && fingerNodeIdentifier < rightBound);
        }

    }

    private synchronized boolean isNumberInIntervalInclusiveExclusive(int fingerStartIPlusOne, Node node, Node fingerNodeI) {
        int leftBound = node.getMyIdentifier();
        int rightBound = fingerNodeI.getMyIdentifier();
        int m = node.getM();

        int maxNodes = (int) Math.pow(2, m);

        // If full circle e.g. [1, 1)
        if (leftBound == rightBound)
            return true;

        // inc/exc e.g. [x, y)
        if (leftBound <= rightBound) {
            // Simple range, no wrapping
            return fingerStartIPlusOne >= leftBound && fingerStartIPlusOne < rightBound;
        } else {
            // Wrapped range
            return (fingerStartIPlusOne >= leftBound && fingerStartIPlusOne < maxNodes) ||
                    (fingerStartIPlusOne >= 0 && fingerStartIPlusOne < rightBound);
        }
    }



    // TESTING WIKIPEDIA
    public synchronized void createWIKI() {
        node.setPredecessor(null);
        node.setSuccessor(node);

        StabilizerWorker worker = new StabilizerWorker(this, node.getM());
        Thread thread = new Thread(worker);
        thread.start();

        node.displayCurrentTable();
    }

    public synchronized void joinWIKI(Node nodePrime) {
        node.setPredecessor(null);
        node.setSuccessor(gRPCFindSuccessorWIKI(nodePrime, nodePrime.getMyIdentifier()));

        FingerTableEntry[] table = node.getFingerTable().getTable();
        Node successor = node.getSuccessor();
        for (int i = 1; i < node.getM(); i++)
            table[i].setNode(successor);

        StabilizerWorker worker = new StabilizerWorker(this, node.getM());
        Thread thread = new Thread(worker);
        thread.start();
    }

    public synchronized void stabilizeWIKI() {
        System.out.println("stabilizeWIKI()");
//        Node x = node.getSuccessor().getPredecessor(); // GRPC CALL TO SUCCESSOR TO GET PREDECESSOR FFS

        Node x = gRPCGetPredecessorWIKI(node.getSuccessor());

        System.out.println("X: " + x);

        if (x != null) {
           if (isNodeInIntervalExclusive(x, node, node.getSuccessor().getMyIdentifier())) {
               node.setSuccessor(x);
           }
        }

        Node successor = node.getSuccessor();


        if (successor.equals(node)) {
            notifyWIKI(node);
        } else {
            gRPCNotifyWIKI(successor, node);
        }

    }

    private synchronized Node gRPCGetPredecessorWIKI(Node n) {
        System.out.println("gRPCGetPredecessorWIKI()");

        initChannelAndStub(n.getMyIp(), n.getMyPort());
        Chord.GetPredecessorRequestWIKI request = Chord.GetPredecessorRequestWIKI.newBuilder().build();
        Chord.GetPredecessorReplyWIKI reply = blockingStub.getPredecessorWIKI(request);
        Node successor = chordUtil.createNodeFromGRPCChordNodeWIKI(reply.getChordNode());

        shutdownChannel(channel);

        return successor.getPredecessor();
    }

    public synchronized void notifyWIKI(Node nodePrime) {
        System.out.println("notifyWIKI()");
        Node predecessor = node.getPredecessor();
        if (predecessor == null || isNodeInIntervalExclusive(nodePrime, predecessor, node.getMyIdentifier()))
            node.setPredecessor(nodePrime);
    }

    public synchronized void gRPCNotifyWIKI(Node successor, Node n) {
        System.out.println("gRPCNotifyWIKI()");
        initChannelAndStub(successor.getMyIp(), successor.getMyPort());

        Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNodeWIKI(n);
        Chord.NotifyRequestWIKI request = Chord.NotifyRequestWIKI.newBuilder().setChordNode(chordNode).build();
        Chord.NotifyReplyWIKI reply = blockingStub.notifyWIKI(request);

        shutdownChannel(channel);

    }

    public synchronized void fixFingersWIKI() {
        System.out.println("fixFingersWIKI()");
        int m = node.getM();
        int maxNodes = (int) Math.pow(2, m);
        FingerTableEntry[] table = node.getFingerTable().getTable();
        next++;

        if (next > m - 1)
            next = 0;

        int n = node.getMyIdentifier();
        int id = n + (int) Math.pow(2, next);

        id = id % maxNodes;

        System.out.println("Next = " + next + " id: " + id);

        Node successor = findSuccessorWIKI(id);

        table[next].setNode(successor);
    }

    public synchronized void checkPredecessorWIKI() {
        System.out.println("checkPredecessorWIKI()");
        Node predecessor = node.getPredecessor();

        if (predecessor == null)
            return;

        boolean alive = isNodeAliveWIKI(predecessor);

        if (!alive) // if predecessor has failed
            node.setPredecessor(null);
    }

    private synchronized boolean isNodeAliveWIKI(Node node) {
        System.out.println("isNodeAliveWIKI()");
        boolean alive = true;
        try {
            alive = gRPCPingNodeWIKI(node);
        } catch (Exception e) {
            e.printStackTrace();
            alive = false;
        }

        return alive;
    }

    public synchronized Node findSuccessorWIKI(int id) {
        System.out.println("findSuccessorWIKI()");
        if (isNumberInIntervalExclusiveInclusive(id, node, node.getSuccessor())) {
            return node.getSuccessor();
        } else {
            Node n0 = closestPrecedingNodeWIKI(id);
            return gRPCFindSuccessorWIKI(n0, id);
        }

    }

    public synchronized boolean gRPCPingNodeWIKI(Node node) {
        System.out.println("gRPCPingNodeWIKI()");
        initChannelAndStub(node.getMyIp(), node.getMyPort());

        Chord.PingNodeRequestWIKI request = Chord.PingNodeRequestWIKI.newBuilder().setIsAlive(true).build();
        Chord.PingNodeReplyWIKI reply = blockingStub.pingNodeWIKI(request);

        shutdownChannel(channel);

        return reply.getIsAlive();
    }

    private synchronized Node gRPCFindSuccessorWIKI(Node nodePrime, int id) {
        System.out.println("gRPCFindSuccessorWIKI(): " + nodePrime + " id: " + id);

        initChannelAndStub(nodePrime.getMyIp(), nodePrime.getMyPort());

        Chord.FindSuccessorRequestWIKI request = Chord.FindSuccessorRequestWIKI.newBuilder().setId(id).build();
        Chord.FindSuccessorReplyWIKI reply = blockingStub.findSuccessorWIKI(request);
        Chord.ChordNode chordNode = reply.getChordNode();

        shutdownChannel(channel);

        Node successor = chordUtil.createNodeFromGRPCChordNodeWIKI(chordNode);
        return successor;
    }

    public synchronized Node closestPrecedingNodeWIKI(int id) {
        System.out.println("closestPrecedingNode()");
        node.displayCurrentTable();

        int m = node.getM();
        FingerTableEntry[] table = node.getFingerTable().getTable();
        for (int i = m - 1; i >= 0; i--) {
            Node fingerNode = table[i].getNode();
            if (isNodeInIntervalExclusive(fingerNode, node, id)) {
                System.out.println("Returning node: " + fingerNode);
                return fingerNode;
            }
        }
        return node;
    }
}
