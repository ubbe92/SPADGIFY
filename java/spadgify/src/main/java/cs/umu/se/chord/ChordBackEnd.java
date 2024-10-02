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
    private int delay = 1000;
    private StabilizerWorker worker;

    public ChordBackEnd(Node node) {
        this.node = node;
    }

    private void initChannelAndStub(String ip, int port) {
        try {
            this.channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
            this.blockingStub = NodeGrpc.newBlockingStub(channel);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("initChannelAndStub() failed!");
        }
    }

    private void shutdownChannel(ManagedChannel channel) {
        channel.shutdown();
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

    private boolean isNumberInIntervalInclusiveExclusive(int fingerStartIPlusOne, Node node, Node fingerNodeI) {
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


    // TESTING WIKIPEDIA SOLUTION
    public void createWIKI() {
        node.setPredecessor(null);
        node.setSuccessor(node);

        this.worker = new StabilizerWorker(this, node.getM(), delay);
        Thread thread = new Thread(worker);
        thread.start();

        node.displayCurrentTable();
    }

    public void joinWIKI(Node nodePrime) {
        node.setPredecessor(null);
        node.setSuccessor(gRPCFindSuccessorWIKI(nodePrime, nodePrime.getMyIdentifier()));

        FingerTableEntry[] table = node.getFingerTable().getTable();
        Node successor = node.getSuccessor();
        for (int i = 1; i < node.getM(); i++)
            table[i].setNode(successor);

        this.worker = new StabilizerWorker(this, node.getM(), delay);
        Thread thread = new Thread(worker);
        thread.start();
    }

    public void leaveWIKI() {
        System.out.println("leaveWIKI()");
        Node successor = node.getSuccessor();
        Node predecessor = node.getPredecessor();

        if (successor != null && !successor.equals(node)) {
            // Notify successor to update its predecessor
            gRPCSetSuccessorsPredecessorInNodeWIKI(successor, predecessor);
        }

        if (predecessor != null && !successor.equals(node)) {
            // Notify predecessor to update its successor
            gRPCSetPredecessorsSuccessorInNodeWIKI(predecessor, successor);
        }

        // Transfer keys to correct node here
    }

    private void gRPCSetSuccessorsPredecessorInNodeWIKI(Node successor, Node predecessor) {
        System.out.println("gRPCSetSuccessorsPredecessorInNodeWIKI()");

        try {
            initChannelAndStub(successor.getMyIp(), successor.getMyPort());
            String ip = predecessor.getMyIp();
            int port = predecessor.getMyPort();
            int m = predecessor.getM();

            Node n = new Node(ip, port, m);

            n.setSuccessor(successor);

            Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNodeWIKI(n);

            System.out.println("succ: " + n.getSuccessor());
            System.out.println("n: " + n);


            Chord.SetSuccessorsPredecessorRequestWIKI request = Chord.SetSuccessorsPredecessorRequestWIKI
                    .newBuilder()
                    .setChordNode(chordNode)
                    .build();

            Chord.SetSuccessorsPredecessorReplyWIKI reply = blockingStub.setSuccessorsPredecessorWIKI(request);

            shutdownChannel(channel);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("gRPCSetSuccessorsPredecessorInNodeWIKI() crashed");
        }
    }

    private void gRPCSetPredecessorsSuccessorInNodeWIKI(Node predecessor, Node successor) {
        System.out.println("gRPCSetPredecessorsSuccessorInNodeWIKI()");

        try {
            initChannelAndStub(predecessor.getMyIp(), predecessor.getMyPort());
            String ip = successor.getMyIp();
            int port = successor.getMyPort();
            int m = successor.getM();

            Node n = new Node(ip, port, m);

            n.setPredecessor(predecessor);

            Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNodeWIKI(n);

            System.out.println("pred: " + n.getPredecessor());
            System.out.println("n: " + n);


            Chord.SetPredecessorsSuccessorRequestWIKI request = Chord.SetPredecessorsSuccessorRequestWIKI
                    .newBuilder()
                    .setChordNode(chordNode)
                    .build();

            Chord.SetPredecessorsSuccessorReplyWIKI reply = blockingStub.setPredecessorsSuccessorWIKI(request);

            shutdownChannel(channel);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("gRPCSetPredecessorsSuccessorInNodeWIKI() crashed");
        }
    }


    public void stabilizeWIKI() {
        System.out.println("stabilizeWIKI()");
//        Node x = node.getSuccessor().getPredecessor(); // GRPC CALL TO SUCCESSOR TO GET PREDECESSOR FFS
        Node successor = node.getSuccessor();
        Node x;

        if (successor.equals(node))
            x = node.getPredecessor();
        else
            x = gRPCGetPredecessorWIKI(successor);

        if (x != null) {
           if (isNodeInIntervalExclusive(x, node, successor.getMyIdentifier())) {
               node.setSuccessor(x);
           }
        }

        if (successor.equals(node)) {
            notifyWIKI(node);
        } else {
            gRPCNotifyWIKI(successor, node);
        }

    }

    private Node gRPCGetPredecessorWIKI(Node n) {
        System.out.println("gRPCGetPredecessorWIKI()");
        try {
            initChannelAndStub(n.getMyIp(), n.getMyPort());

            Chord.GetPredecessorRequestWIKI request = Chord.GetPredecessorRequestWIKI.newBuilder().build();
            Chord.GetPredecessorReplyWIKI reply = blockingStub.getPredecessorWIKI(request);
            Node successor = chordUtil.createNodeFromGRPCChordNodeWIKI(reply.getChordNode());

            shutdownChannel(channel);
            return successor.getPredecessor();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("gRPCGetPredecessorWIKI() crashed");
        }
        return node; // should not happen lmao
    }

    public synchronized void notifyWIKI(Node nodePrime) {
        System.out.println("notifyWIKI()");
        Node predecessor = node.getPredecessor();
        if (predecessor == null || isNodeInIntervalExclusive(nodePrime, predecessor, node.getMyIdentifier()))
            node.setPredecessor(nodePrime);
    }

    public void gRPCNotifyWIKI(Node successor, Node n) {
        System.out.println("gRPCNotifyWIKI()");
        try {
            initChannelAndStub(successor.getMyIp(), successor.getMyPort());
            Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNodeWIKI(n);
            Chord.NotifyRequestWIKI request = Chord.NotifyRequestWIKI.newBuilder().setChordNode(chordNode).build();
            Chord.NotifyReplyWIKI reply = blockingStub.notifyWIKI(request);
            shutdownChannel(channel);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("gRPCNotifyWIKI() crashed");
        }


    }

    public void fixFingersWIKI() {
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

    public synchronized void checkSuccessorWIKI() {
        System.out.println("checkSuccessorWIKI()");
        Node successor = node.getSuccessor();

        if (successor == null)
            return;

        boolean alive = isNodeAliveWIKI(successor);

        if (!alive) { // if successor has failed
            FingerTableEntry[] table = node.getFingerTable().getTable();

            for (int i = 1; i < node.getM(); i++) { // try to find a new successor
                Node fingerNode = table[i].getNode();
                if (fingerNode.equals(node))
                    continue;

                alive = isNodeAliveWIKI(fingerNode);
                if (alive) {
                    node.setSuccessor(fingerNode);
                    return;
                }
            }
            node.setSuccessor(node); // worst case, we are alone
        }
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

    public boolean gRPCPingNodeWIKI(Node node) {
        System.out.println("gRPCPingNodeWIKI()");

        try {
            initChannelAndStub(node.getMyIp(), node.getMyPort());

            Chord.PingNodeRequestWIKI request = Chord.PingNodeRequestWIKI.newBuilder().setIsAlive(true).build();
            Chord.PingNodeReplyWIKI reply = blockingStub.pingNodeWIKI(request);

            shutdownChannel(channel);

            return reply.getIsAlive();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("gRPCPingNodeWIKI() crashed");
        }

        return false; // should not happen lmao
    }

    private Node gRPCFindSuccessorWIKI(Node nodePrime, int id) {
        System.out.println("gRPCFindSuccessorWIKI(): " + nodePrime + " id: " + id);

        try {
            initChannelAndStub(nodePrime.getMyIp(), nodePrime.getMyPort());

            Chord.FindSuccessorRequestWIKI request = Chord.FindSuccessorRequestWIKI.newBuilder().setId(id).build();
            Chord.FindSuccessorReplyWIKI reply = blockingStub.findSuccessorWIKI(request);
            Chord.ChordNode chordNode = reply.getChordNode();

            shutdownChannel(channel);

            Node successor = chordUtil.createNodeFromGRPCChordNodeWIKI(chordNode);
            return successor;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("gRPCFindSuccessorWIKI() crashed");
        }

        return node; // should not happen lmao
    }

    public Node closestPrecedingNodeWIKI(int id) {
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

    public synchronized StabilizerWorker getWorkerThread() {
        return this.worker;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
