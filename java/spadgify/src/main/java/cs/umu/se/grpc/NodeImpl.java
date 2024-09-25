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

        int id = (int) req.getId();

//        Node nodePrime = node; // change to find_predecessor(id)
        Node nodePrime = chordBackEnd.findPredecessor(id); // change to find_predecessor(id)

        Node nodePrimeSuccessor = nodePrime.getSuccessor();
        Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNode(nodePrimeSuccessor);
        Chord.FindSuccessorReply reply = Chord.FindSuccessorReply.newBuilder().setChordNode(chordNode).build();
        resp.onNext(reply);
        resp.onCompleted();
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
