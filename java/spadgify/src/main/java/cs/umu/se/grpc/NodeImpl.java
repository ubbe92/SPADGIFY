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
        System.out.println("SERVER GOT findSuccessor REQUEST!");

        int id = (int) req.getId();

        Node nodePrime = chordBackEnd.findPredecessor(id); // change to find_predecessor(id)

        Node nodePrimeSuccessor = nodePrime.getSuccessor();
        Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNode(nodePrimeSuccessor);
        Chord.FindSuccessorReply reply = Chord.FindSuccessorReply.newBuilder().setChordNode(chordNode).build();

        System.out.println("after receiving findSuccessor gRPC call");
        node.displayCurrentTable();

        resp.onNext(reply);
        resp.onCompleted();
    }

    @Override
    public void findPredecessor(Chord.FindPredecessorRequest req, StreamObserver<Chord.FindPredecessorReply> resp) {

    }

    @Override
    public void closestPrecedingFinger(Chord.ClosestPrecedingFingerRequest req, StreamObserver<Chord.ClosestPrecedingFingerReply> resp) {
        System.out.println("SERVER GOT closestPrecedingFinger REQUEST!");
        int id = (int) req.getId();

        Node nodePrime = chordBackEnd.closestPrecedingFinger(id);
        Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNode(nodePrime);
        Chord.ClosestPrecedingFingerReply reply = Chord.ClosestPrecedingFingerReply.newBuilder().setChordNode(chordNode).build();

        System.out.println("after receiving closestPrecedingFinger gRPC call");
        node.displayCurrentTable();

        resp.onNext(reply);
        resp.onCompleted();

    }

    @Override
    public void updateFingerTable(Chord.UpdateFingerTableRequest req, StreamObserver<Chord.UpdateFingerTableReply> resp) {
        System.out.println("SERVER GOT updateFingerTable REQUEST");

        int i = (int) req.getI();
        Chord.ChordNode chordNode = req.getChordNode();
        Node s = chordUtil.createNodeFromGRPCChordNode(chordNode);

        System.out.println("i: " + i + " s: " + s);
        chordBackEnd.updateFingerTable(s, i); // this crashes the program

//        updateFingerTable()
//        s: 1 in interval: [1 - 1)
//        updateFingerTable() Updating my own fingertable with: 192.168.38.126:8185 identifier: 1 at i: 0
//        updateFingerTable()
//        s: 1 in interval: [1 - 1)
//        updateFingerTable() Updating my own fingertable with: 192.168.38.126:8185 identifier: 1 at i: 0
//        Exception in thread "grpc-default-executor-0" java.lang.StackOverflowError
//        at cs.umu.se.chord.ChordBackEnd.updateFingerTable(ChordBackEnd.java:148)
//        at cs.umu.se.chord.ChordBackEnd.updateFingerTable(ChordBackEnd.java:163)

        Chord.UpdateFingerTableReply reply = Chord.UpdateFingerTableReply.newBuilder().build();

        System.out.println("after receiving updateFingerTable gRPC call");
        node.displayCurrentTable();

        resp.onNext(reply);
        resp.onCompleted();

    }

    @Override
    public void findSuccessorWIKI(Chord.FindSuccessorRequestWIKI req, StreamObserver<Chord.FindSuccessorReplyWIKI> resp) {
        System.out.println("SERVER GOT findSuccessorWIKI REQUEST!");

        int id = (int) req.getId();

        Node nodePrime = chordBackEnd.findSuccessorWIKI(id);
        Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNodeWIKI(nodePrime);
        Chord.FindSuccessorReplyWIKI reply = Chord.FindSuccessorReplyWIKI.newBuilder().setChordNode(chordNode).build();

        System.out.println("after receiving findSuccessorWIKI gRPC call");
        node.displayCurrentTable();

        resp.onNext(reply);
        resp.onCompleted();
    }

    @Override
    public void notifyWIKI(Chord.NotifyRequestWIKI req, StreamObserver<Chord.NotifyReplyWIKI> resp) {
        System.out.println("SERVER GOT notifyWIKI REQUEST!");

        Chord.ChordNode chordNode = req.getChordNode();
        Node nodePrime = chordUtil.createNodeFromGRPCChordNodeWIKI(chordNode);
        chordBackEnd.notifyWIKI(nodePrime);
        Chord.NotifyReplyWIKI reply = Chord.NotifyReplyWIKI.newBuilder().build();

        System.out.println("after receiving notifyWIKI gRPC call");
        node.displayCurrentTable();

        resp.onNext(reply);
        resp.onCompleted();
    }

    @Override
    public void pingNodeWIKI(Chord.PingNodeRequestWIKI req, StreamObserver<Chord.PingNodeReplyWIKI> resp) {
        System.out.println("SERVER GOT pingNodeWIKI REQUEST!");

        boolean isAlive = req.getIsAlive();
        Chord.PingNodeReplyWIKI reply = Chord.PingNodeReplyWIKI.newBuilder().setIsAlive(isAlive).build();

        System.out.println("after receiving pingNodeWIKI gRPC call");
        node.displayCurrentTable();

        resp.onNext(reply);
        resp.onCompleted();
    }

    public ChordBackEnd getChordBackEnd() {
        return chordBackEnd;
    }

}
