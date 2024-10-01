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
    public void findSuccessorWIKI(Chord.FindSuccessorRequestWIKI req, StreamObserver<Chord.FindSuccessorReplyWIKI> resp) {
        System.out.println("SERVER GOT findSuccessorWIKI REQUEST!");

        int id = (int) req.getId();

        Node nodePrime = chordBackEnd.findSuccessorWIKI(id);
        System.out.println("NODE PRIME: " + nodePrime);

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
        System.out.println("Request from: " + nodePrime);
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

    @Override
    public void getPredecessorWIKI(Chord.GetPredecessorRequestWIKI req, StreamObserver<Chord.GetPredecessorReplyWIKI> resp) {
        System.out.println("SERVER GOT getPredecessorWIKI REQUEST!");
        System.out.println("Thread in server: " + Thread.currentThread().getName());

        Node predecessor = node;
        Chord.ChordNode chordNode = chordUtil.createGRPCChordNodeFromNodeWIKI(predecessor);
        Chord.GetPredecessorReplyWIKI reply = Chord.GetPredecessorReplyWIKI.newBuilder().setChordNode(chordNode).build();

        resp.onNext(reply);
        resp.onCompleted();
    }

    @Override
    public void setPredecessorsSuccessorWIKI(Chord.SetPredecessorsSuccessorRequestWIKI req, StreamObserver<Chord.SetPredecessorsSuccessorReplyWIKI> resp) {
        System.out.println("SERVER GOT setPredecessorsSuccessorWIKI REQUEST!");

        Chord.ChordNode chordNode = req.getChordNode();
        Node newSuccessor = chordUtil.createNodeFromGRPCChordNodeWIKI(chordNode);
        node.setSuccessor(newSuccessor);
        Chord.SetPredecessorsSuccessorReplyWIKI reply = Chord.SetPredecessorsSuccessorReplyWIKI.newBuilder().build();


        node.displayCurrentTable();

        resp.onNext(reply);
        resp.onCompleted();
    }

    @Override
    public void setSuccessorsPredecessorWIKI(Chord.SetSuccessorsPredecessorRequestWIKI req, StreamObserver<Chord.SetSuccessorsPredecessorReplyWIKI> resp) {
        System.out.println("SERVER GOT setSuccessorsPredecessorWIKI REQUEST!");

        Chord.ChordNode chordNode = req.getChordNode();
        Node newPredecessor = chordUtil.createNodeFromGRPCChordNodeWIKI(chordNode);
        node.setPredecessor(newPredecessor);
        Chord.SetSuccessorsPredecessorReplyWIKI reply = Chord.SetSuccessorsPredecessorReplyWIKI.newBuilder().build();

        node.displayCurrentTable();

        resp.onNext(reply);
        resp.onCompleted();
    }

    public ChordBackEnd getChordBackEnd() {
        return chordBackEnd;
    }

}
