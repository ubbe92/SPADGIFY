package cs.umu.se.chord;

import cs.umu.se.client.ClientBackend;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;
import cs.umu.se.util.ChordUtil;
import cs.umu.se.workers.StabilizerWorker;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.logging.log4j.Logger;
import proto.Chord;
import proto.NodeGrpc;
import java.util.Arrays;

/**
 * Backend to handle the Chord protocol
 */
public class ChordBackEnd {

    private final Node node;
    private ManagedChannel channel;
    private NodeGrpc.NodeBlockingStub blockingStub;
    private ChordUtil chordUtil = new ChordUtil();
    private int next = 0;
    private int delay = 1000;
    private StabilizerWorker worker;
    private final Logger logger;

    public ChordBackEnd(Node node, Logger logger) {
        this.node = node;
        this.logger = logger;
    }

    private void initChannelAndStub(String ip, int port) {
        try {
            this.channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
            this.blockingStub = NodeGrpc.newBlockingStub(channel);
        } catch (Exception e) {
            logger.error("initChannelAndStub() failed! {}", Arrays.toString(e.getStackTrace()));
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


    // TESTING PEDIA SOLUTION - WORKS!

    /**
     * Create a new Chord network
     */
    public void create() {
        node.setPredecessor(null);
        node.setSuccessor(node);

        this.worker = new StabilizerWorker(this, node.getM(), delay);
        Thread thread = new Thread(worker);
        thread.start();

        node.displayCurrentTable();
    }

    /**
     * Join existing Chord network
     * @param nodePrime already existing node in network
     */
    public void join(Node nodePrime) {
        node.setPredecessor(null);
        node.setSuccessor(gRPCFindSuccessor(nodePrime, nodePrime.getMyIdentifier()));

        FingerTableEntry[] table = node.getFingerTable().getTable();
        Node successor = node.getSuccessor();
        for (int i = 1; i < node.getM(); i++)
            table[i].setNode(successor);

        this.worker = new StabilizerWorker(this, node.getM(), delay);
        Thread thread = new Thread(worker);
        thread.start();

        // Give the node some time to stabilize before requesting songs from successor
        try {
            int m = node.getM();
            int gracePeriod = delay * m;
            Thread.sleep(gracePeriod);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        successor = node.getSuccessor();
        // list all node songs between my predecessor and me
        String succIp = successor.getMyIp();
        int succPort = successor.getMyPort();
        int m = node.getM();

        ClientBackend clientBackend = new ClientBackend(succIp, succPort, "", m);
        clientBackend.requestTransfer(node);
    }

    /**
     * Leave the Chord network
     */
    public void leave() {
        Node successor = node.getSuccessor();
        Node predecessor = node.getPredecessor();

        if (successor != null && !successor.equals(node)) {
            // Notify successor to update its predecessor
            gRPCSetSuccessorsPredecessorInNode(successor, predecessor);
        }

        if (predecessor != null && !successor.equals(node)) {
            // Notify predecessor to update its successor
            gRPCSetPredecessorsSuccessorInNode(predecessor, successor);
        }

        // Start transfer keys to successor
        String successorIp = successor.getMyIp();
        int successorPort = successor.getMyPort();
        int m = node.getM();

        // Get all our songs
        ClientBackend clientBackend = new ClientBackend(node.getMyIp(), node.getMyPort(), "", m);
        MediaInfo[] mediaInfos = clientBackend.listNodeSongs();
        Song[] songs = new Song[mediaInfos.length];

        // Cleanup locally saved files/data
        int i = 0;
        boolean isAlone = (successor.equals(node) && predecessor.equals(node));
        for (MediaInfo mediaInfo : mediaInfos) {
            String identifierString = mediaInfo.getIdentifierString();

            if (!isAlone)
                songs[i] = clientBackend.retrieve(identifierString);

            clientBackend.delete(identifierString);
            i++;
        }

        // We are alone in the ring
        if (isAlone)
            return;

        // Transfer all songs to successor
        clientBackend = new ClientBackend(successorIp, successorPort, "", m);
        for (Song s : songs)
            clientBackend.store(s);
    }

    /**
     * Set the successor's predecessor
     * @param successor the successor
     * @param predecessor the predecessor
     */
    private void gRPCSetSuccessorsPredecessorInNode(Node successor, Node predecessor) {
        try {
            initChannelAndStub(successor.getMyIp(), successor.getMyPort());
            String ip = predecessor.getMyIp();
            int port = predecessor.getMyPort();
            int m = predecessor.getM();

            Node n = new Node(ip, port, m);

            n.setSuccessor(successor);

            Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNode(n);

            Chord.SetSuccessorsPredecessorRequest request = Chord.SetSuccessorsPredecessorRequest
                    .newBuilder()
                    .setChordNode(chordNode)
                    .build();

            Chord.SetSuccessorsPredecessorReply reply = blockingStub.setSuccessorsPredecessor(request);

            shutdownChannel(channel);
        } catch (Exception e) {
            logger.error("gRPCSetSuccessorsPredecessorInNode() crashed {}", Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Set the predecessor's successor
     * @param predecessor the predecessor
     * @param successor the successor
     */
    private void gRPCSetPredecessorsSuccessorInNode(Node predecessor, Node successor) {
        try {
            initChannelAndStub(predecessor.getMyIp(), predecessor.getMyPort());
            String ip = successor.getMyIp();
            int port = successor.getMyPort();
            int m = successor.getM();

            Node n = new Node(ip, port, m);

            n.setPredecessor(predecessor);

            Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNode(n);

            Chord.SetPredecessorsSuccessorRequest request = Chord.SetPredecessorsSuccessorRequest
                    .newBuilder()
                    .setChordNode(chordNode)
                    .build();

            Chord.SetPredecessorsSuccessorReply reply = blockingStub.setPredecessorsSuccessor(request);

            shutdownChannel(channel);
        } catch (Exception e) {
            logger.error("gRPCSetPredecessorsSuccessorInNode() crashed {}", Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Stabilizes the network by setting correct predecessor and successor for a node
     */
    public void stabilize() {
        Node successor = node.getSuccessor();
        Node x;

        if (successor.equals(node))
            x = node.getPredecessor();
        else
            x = gRPCGetPredecessor(successor);

        if (x != null) {
            if (isNodeInIntervalExclusive(x, node, successor.getMyIdentifier())) {
                node.setSuccessor(x);
            }
        }

        if (successor.equals(node)) {
            notify(node);
        } else {
            gRPCNotify(successor, node);
        }

    }

    private Node gRPCGetPredecessor(Node n) {
        try {
            initChannelAndStub(n.getMyIp(), n.getMyPort());

            Chord.GetPredecessorRequest request = Chord.GetPredecessorRequest.newBuilder().build();
            Chord.GetPredecessorReply reply = blockingStub.getPredecessor(request);
            Node successor = chordUtil.createNodeFromGRPCChordNode(reply.getChordNode());

            shutdownChannel(channel);
            return successor.getPredecessor();
        } catch (Exception e) {
            logger.error("gRPCGetPredecessor() crashed {}", Arrays.toString(e.getStackTrace()));
        }
        return node; // should not happen lmao
    }

    /**
     * Notify this node by setting its predecessor
     * @param nodePrime the node
     */
    public synchronized void notify(Node nodePrime) {
        Node predecessor = node.getPredecessor();
        if (predecessor == null || isNodeInIntervalExclusive(nodePrime, predecessor, node.getMyIdentifier()))
            node.setPredecessor(nodePrime);
    }

    /**
     * Notify another node remotely via gRPC
     * @param successor the node to notify
     * @param n this node
     */
    public void gRPCNotify(Node successor, Node n) {
        try {
            initChannelAndStub(successor.getMyIp(), successor.getMyPort());
            Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNode(n);
            Chord.NotifyRequest request = Chord.NotifyRequest.newBuilder().setChordNode(chordNode).build();
            Chord.NotifyReply reply = blockingStub.notify(request);
            shutdownChannel(channel);
        } catch (Exception e) {
            logger.error("gRPCNotify() crashed {}", Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Fixes the finger table by setting correct node for each entry
     */
    public void fixFingers() {
        int m = node.getM();
        int maxNodes = (int) Math.pow(2, m);
        FingerTableEntry[] table = node.getFingerTable().getTable();
        next++;

        if (next > m - 1)
            next = 0;

        int n = node.getMyIdentifier();
        int id = n + (int) Math.pow(2, next);

        id = id % maxNodes;

        Node successor = findSuccessor(id);

        table[next].setNode(successor);
    }

    /**
     * Checks if the predecessor is still alive
     */
    public synchronized void checkPredecessor() {
        Node predecessor = node.getPredecessor();

        if (predecessor == null)
            return;

        boolean alive = isNodeAlive(predecessor);

        // if predecessor has failed
        if (!alive)
            node.setPredecessor(null);
    }

    /**
     * Check if successor is still alive
     */
    public synchronized void checkSuccessor() {
        Node successor = node.getSuccessor();

        if (successor == null)
            return;

        boolean alive = isNodeAlive(successor);

        // if successor has failed
        if (!alive) {
            FingerTableEntry[] table = node.getFingerTable().getTable();

            // try to find a new successor
            for (int i = 1; i < node.getM(); i++) {
                Node fingerNode = table[i].getNode();
                if (fingerNode.equals(node))
                    continue;

                alive = isNodeAlive(fingerNode);
                if (alive) {
                    node.setSuccessor(fingerNode);
                    return;
                }
            }
            node.setSuccessor(node); // worst case, we are alone
        }
    }

    /**
     * Check if a node is alive by sending a ping request
     * @param node the node to check
     * @return true if the node is alive otherwise false
     */
    private synchronized boolean isNodeAlive(Node node) {
        boolean alive = true;

        try {
            alive = gRPCPingNode(node);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("isNodeAlive crashed" + Arrays.toString(e.getStackTrace()));
            alive = false;
        }

        return alive;
    }

    /**
     * Find the successor of a node
     * @param id the node's identifier
     * @return the nodes successor
     */
    public synchronized Node findSuccessor(int id) {
        if (isNumberInIntervalExclusiveInclusive(id, node, node.getSuccessor())) {
            return node.getSuccessor();
        } else {
            Node n0 = closestPrecedingNode(id);
            return gRPCFindSuccessor(n0, id);
        }
    }

    /**
     * Ping a node by sending empty gRPC
     * @param node the node to ping
     * @return true if the node could be pinged, i.e. it's alive, otherwise false
     */
    public boolean gRPCPingNode(Node node) {
        try {
            initChannelAndStub(node.getMyIp(), node.getMyPort());

            Chord.PingNodeRequest request = Chord.PingNodeRequest.newBuilder().setIsAlive(true).build();
            Chord.PingNodeReply reply = blockingStub.pingNode(request);

            shutdownChannel(channel);

            return reply.getIsAlive();
        } catch (Exception e) {
            logger.error("gRPCPingNode() {}", Arrays.toString(e.getStackTrace()));
        }

        return false;
    }

    /**
     * Find a node's successor via gRPC
     * @param nodePrime the node to contact
     * @param id the node id of whom to find successor to
     * @return the successor or this node if crash
     */
    private Node gRPCFindSuccessor(Node nodePrime, int id) {
        try {
            initChannelAndStub(nodePrime.getMyIp(), nodePrime.getMyPort());

            Chord.FindSuccessorRequest request = Chord.FindSuccessorRequest.newBuilder().setId(id).build();
            Chord.FindSuccessorReply reply = blockingStub.findSuccessor(request);
            Chord.ChordNode chordNode = reply.getChordNode();

            shutdownChannel(channel);

            Node successor = chordUtil.createNodeFromGRPCChordNode(chordNode);
            return successor;
        } catch (Exception e) {
            logger.error("gRPCFindSuccessor() crashed {}", Arrays.toString(e.getStackTrace()));
        }

        return node;
    }

    /**
     * Find the closest preceding node to a node
     * @param id the node id
     * @return the closest preceding node
     */
    public Node closestPrecedingNode(int id) {
        node.displayCurrentTable();

        int m = node.getM();
        FingerTableEntry[] table = node.getFingerTable().getTable();
        for (int i = m - 1; i >= 0; i--) {
            Node fingerNode = table[i].getNode();
            if (isNodeInIntervalExclusive(fingerNode, node, id)) {
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
