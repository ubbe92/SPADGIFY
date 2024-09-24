package cs.umu.se.grpc;

import cs.umu.se.chord.ChordBackEnd;
import cs.umu.se.chord.Node;
import cs.umu.se.util.ChordUtil;
import io.grpc.stub.StreamObserver;
import proto.Chord;
import proto.NodeGrpc;

public class NodeImpl extends NodeGrpc.NodeImplBase {
    private int remotePort;
    private String remoteIp;
    private int port;
    private long m;
    private int mode;
    private Node node;
    private long maxNodes;
    private int exitCode;
    private ChordUtil chordUtil = new ChordUtil();
    private ChordBackEnd chordBackEnd;
    private String ip;

    public NodeImpl(int port, String remoteIp, int remotePort, int m, int mode, int exitCode) {
        this.remoteIp = remoteIp;
        this.port = port;
        this.remotePort = remotePort;
        this.m = m;
        this.mode = mode;
        this.maxNodes = (long) Math.pow(2, m);
        this.exitCode = exitCode;
        this.ip = chordUtil.getLocalIp();

        this.node = new Node(ip, port, m);
        this.chordBackEnd = new ChordBackEnd(node);

        System.out.println("NodeImpl m: " + m + ", maxNodes: " + maxNodes);
    }

    @Override
    public void findSuccessor(Chord.FindSuccessorRequest req, StreamObserver<Chord.FindSuccessorReply> resp) {
        System.out.println("SERVER GOT REQUEST!");

        // Parse request
//        FingerTableNode fingerTableNode = chordUtil.createFingerTableNode(req.getChordNode());
//        FingerTableNode fingerTableNodePrime = node.findSuccessor(fingerTableNode);

//        Node id = chordUtil.createNode();
//        Node nodePrime = chordBackEnd.findSuccessor();

//        // Make reply
//        Chord.ChordNode chordNode = chordUtil.createGRPCChordNode(fingerTableNodePrime);
//        Chord.FindSuccessorReply reply = Chord.FindSuccessorReply.newBuilder().setChordNode(chordNode).build();
//        resp.onNext(reply);
//        resp.onCompleted();



        int port = 8187;
        String ip = chordUtil.getLocalIp();
        int m = 3;
        Node nodeIds = new Node(ip, port, m);
        Node nodePrime = chordBackEnd.findSuccessor(nodeIds.getMyIdentifier());

        System.out.println("Node prime: " + nodePrime.getMyIdentifier());
    }

    @Override
    public void findPredecessor(Chord.FindPredecessorRequest req, StreamObserver<Chord.FindPredecessorReply> resp) {

    }

    @Override
    public void closestPrecedingFinger(Chord.ClosestPrecedingFingerRequest req, StreamObserver<Chord.ClosestPrecedingFingerReply> resp) {

    }

    public ChordBackEnd getChordBackEnd() {
        return chordBackEnd;
    }

}
