package cs.umu.se.grpc;

import cs.umu.se.chord.ChordBackEnd;
import cs.umu.se.chord.Node;
import cs.umu.se.util.ChordUtil;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.Logger;
import proto.Chord;
import proto.NodeGrpc;

/**
 * NodeImpl is a concrete implementation of the NodeGrpc.NodeImplBase class, representing
 * a node in a Chord distributed hash table. This class provides implementations for several
 * RPC methods used in the Chord protocol, enabling node-to-node communication for operations
 * such as finding a successor, notifying about existence, checking node liveness, and setting predecessors/successors.
 */
public class NodeImpl extends NodeGrpc.NodeImplBase {
    private final int remotePort;
    private final String remoteIp;
    private final int port;
    private final long m;
    private final int mode;
    private final Node node;
    private final long maxNodes;
    private final int exitCode;
    private final ChordUtil chordUtil = new ChordUtil();
    private final ChordBackEnd chordBackEnd;
    private final String ip;
    private final Logger logger;

    public NodeImpl(Node node, String remoteIp, int remotePort, int mode, int exitCode, int delay, Logger logger, ChordBackEnd chordBackEnd) {
        this.logger = logger;
        this.logger.info("Node service is up for node: {}", node);

        this.remoteIp = remoteIp;
        this.port = node.getMyPort();
        this.remotePort = remotePort;
        this.m = node.getM();
        this.mode = mode;
        this.maxNodes = (long) Math.pow(2, m);
        this.exitCode = exitCode;
        this.ip = node.getMyIp();

        this.node = node;
        this.chordBackEnd = chordBackEnd;
        this.chordBackEnd.setDelay(delay); // Set the delay of stabilize (default to 1000 ms)

        this.logger.info("NodeImpl m: {}, maxNodes: {}", m, maxNodes);
    }

    /**
     * Processes a request to find the successor of a given node ID in the Chord network.
     *
     * @param req request containing the node ID for which the successor is to be found.
     * @param resp used to send the response containing the successor node information.
     */
    @Override
    public void findSuccessor(Chord.FindSuccessorRequest req, StreamObserver<Chord.FindSuccessorReply> resp) {
//        System.out.println("SERVER GOT findSuccessor REQUEST!");
        int id = (int) req.getId();

        Node nodePrime = chordBackEnd.findSuccessor(id);

        Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNode(nodePrime);
        Chord.FindSuccessorReply reply = Chord.FindSuccessorReply.newBuilder().setChordNode(chordNode).build();

        node.displayCurrentTable();

        resp.onNext(reply);
        resp.onCompleted();
    }

    /**
     * Handles a notify request in the Chord protocol, allowing a node to inform another node about its existence.
     * This method is typically used to maintain and update the finger table and predecessor information of nodes.
     *
     * @param req The `NotifyRequest` request containing information about the notifying node.
     * @param resp The `StreamObserver<NotifyReply>` used to send the response after processing the notification.
     */
    @Override
    public void notify(Chord.NotifyRequest req, StreamObserver<Chord.NotifyReply> resp) {
//        System.out.println("SERVER GOT notify REQUEST!");

        Chord.ChordNode chordNode = req.getChordNode();
        Node nodePrime = chordUtil.createNodeFromGRPCChordNode(chordNode);
        chordBackEnd.notify(nodePrime);
        Chord.NotifyReply reply = Chord.NotifyReply.newBuilder().build();

        node.displayCurrentTable();

        resp.onNext(reply);
        resp.onCompleted();
    }

    /**
     * Handles an incoming ping request to check if the node is alive and responds with the same status.
     *
     * @param req the request containing the ping data, including the node's alive status.
     * @param resp the response observer used to send back the reply containing the node's alive status.
     */
    @Override
    public void pingNode(Chord.PingNodeRequest req, StreamObserver<Chord.PingNodeReply> resp) {
//        System.out.println("SERVER GOT pingNode REQUEST!");

        boolean isAlive = req.getIsAlive();
        Chord.PingNodeReply reply = Chord.PingNodeReply.newBuilder().setIsAlive(isAlive).build();

        node.displayCurrentTable();

        resp.onNext(reply);
        resp.onCompleted();
    }

    /**
     * Retrieves the predecessor node in the Chord protocol and returns it in the response.
     *
     * @param req The request object of type `Chord.GetPredecessorRequest`.
     * @param resp The response observer used to send back the reply containing the predecessor node information.
     */
    @Override
    public void getPredecessor(Chord.GetPredecessorRequest req, StreamObserver<Chord.GetPredecessorReply> resp) {
//        System.out.println("SERVER GOT getPredecessor REQUEST!");

        Node predecessor = node;
        Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNode(predecessor);
        Chord.GetPredecessorReply reply = Chord.GetPredecessorReply.newBuilder().setChordNode(chordNode).build();

        resp.onNext(reply);
        resp.onCompleted();
    }

    /**
     * Sets the successor node for the predecessor in the Chord protocol.
     *
     * @param req the request containing the Chord node information to set as the successor.
     * @param resp the response observer used to send back an acknowledgement.
     */
    @Override
    public void setPredecessorsSuccessor(Chord.SetPredecessorsSuccessorRequest req, StreamObserver<Chord.SetPredecessorsSuccessorReply> resp) {
//        System.out.println("SERVER GOT setPredecessorsSuccessor REQUEST!");

        Chord.ChordNode chordNode = req.getChordNode();
        Node newSuccessor = chordUtil.createNodeFromGRPCChordNode(chordNode);
        node.setSuccessor(newSuccessor);
        Chord.SetPredecessorsSuccessorReply reply = Chord.SetPredecessorsSuccessorReply.newBuilder().build();


        node.displayCurrentTable();

        resp.onNext(reply);
        resp.onCompleted();
    }

    /**
     * Handles the request to set the predecessor of the successor node in the Chord protocol.
     *
     * @param req the request containing the Chord node information to set as the predecessor.
     * @param resp the response observer used to send back an acknowledgement reply.
     */
    @Override
    public void setSuccessorsPredecessor(Chord.SetSuccessorsPredecessorRequest req, StreamObserver<Chord.SetSuccessorsPredecessorReply> resp) {
//        System.out.println("SERVER GOT setSuccessorsPredecessor REQUEST!");

        Chord.ChordNode chordNode = req.getChordNode();
        Node newPredecessor = chordUtil.createNodeFromGRPCChordNode(chordNode);
        node.setPredecessor(newPredecessor);
        Chord.SetSuccessorsPredecessorReply reply = Chord.SetSuccessorsPredecessorReply.newBuilder().build();

        node.displayCurrentTable();

        resp.onNext(reply);
        resp.onCompleted();
    }

    public ChordBackEnd getChordBackEnd() {
        return chordBackEnd;
    }

    public Node getNode() {
        return node;
    }
}
