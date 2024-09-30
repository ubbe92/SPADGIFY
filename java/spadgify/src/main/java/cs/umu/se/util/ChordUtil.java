package cs.umu.se.util;

import cs.umu.se.chord.Node;
import proto.Chord;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ChordUtil {

    public ChordUtil() {}

    public synchronized String getLocalIp() {
        String ip = "";
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (UnknownHostException | SocketException e) {
            System.out.println("Could not get local ip: " + e.getMessage());
        }
        return ip;
    }

    public synchronized Node createNodeFromGRPCChordNode(Chord.ChordNode node) {
        Chord.ChordNode succ = node.getSuccessor();
        Chord.ChordNode pred = node.getPredecessor();

        String ip = node.getIp();
        int port = (int) node.getPort();
        int m = (int) node.getM();

        Node newNode = new Node(ip, port, m);

        ip = pred.getIp();
        port = (int) pred.getPort();
        m = (int) pred.getM();

        Node predNode = new Node(ip, port, m);
        newNode.setPredecessor(predNode);

        ip = succ.getIp();
        port = (int) succ.getPort();
        m = (int) succ.getM();

        Node succNode = new Node(ip, port, m);
        newNode.setSuccessor(succNode);

        return newNode;
    }

    public synchronized Node createNodeFromGRPCChordNodeWIKI(Chord.ChordNode node) {
        String ip = node.getIp();
        int port = (int) node.getPort();
        int m = (int) node.getM();
        Node newNode = new Node(ip, port, m);

        if (node.hasPredecessor()) {
            Chord.ChordNode pred = node.getPredecessor();
            ip = pred.getIp();
            port = (int) pred.getPort();
            m = (int) pred.getM();

            Node predNode = new Node(ip, port, m);
            newNode.setPredecessor(predNode);
        } else
            newNode.setPredecessor(null);

        if (node.hasSuccessor()) {
            Chord.ChordNode succ = node.getSuccessor();
            ip = succ.getIp();
            port = (int) succ.getPort();
            m = (int) succ.getM();

            Node succNode = new Node(ip, port, m);
            newNode.setSuccessor(succNode);
        } else
            newNode.setSuccessor(null);

        return newNode;
    }

    public synchronized Chord.ChordNode createGRPCChordNodeFromNode(Node node) {
        String ip = node.getMyIp();
        int port = node.getMyPort();
        int identifier = node.getMyIdentifier();
        int m = node.getM();

        Chord.ChordNode succ = createGRPCSuccessorNodeFromNode(node.getSuccessor());
        Chord.ChordNode pred = createGRPCPredecessorNodeFromNode(node.getPredecessor());

        return Chord.ChordNode.newBuilder()
                .setIp(ip)
                .setPort(port)
                .setIdentifier(identifier)
                .setM(m)
                .setSuccessor(succ)
                .setPredecessor(pred)
                .build();
    }

    public synchronized Chord.ChordNode createGRPCChordNodeFromNodeWIKI(Node node) {
        String ip = node.getMyIp();
        int port = node.getMyPort();
        int identifier = node.getMyIdentifier();
        int m = node.getM();

        Node successor = node.getSuccessor();
        Node predecessor = node.getPredecessor();
        Chord.ChordNode succ = null;
        Chord.ChordNode pred = null;

        Chord.ChordNode.Builder build = Chord.ChordNode.newBuilder()
            .setIp(ip)
            .setPort(port)
            .setIdentifier(identifier)
            .setM(m);

        if (successor != null) {
            succ = createGRPCNodeFromNodeWIKI(successor);
            build.setSuccessor(succ);
        }

        if (predecessor != null) {
            pred = createGRPCPredecessorNodeFromNode(predecessor);
            build.setPredecessor(pred);
        }

        return build.build();
    }


    public synchronized Node createSuccessorNodeFromGRPCNode(Chord.ChordNode successor) {

        return null;
    }

    public synchronized Node createPredecessorNodeFromGRPCNode(Chord.ChordNode predecessor) {
        return null;
    }

    private synchronized Chord.ChordNode createGRPCNodeFromNodeWIKI(Node node) {
        return Chord.ChordNode.newBuilder()
                .setIp(node.getMyIp())
                .setPort(node.getMyPort())
                .setIdentifier(node.getMyIdentifier())
                .setM(node.getM())
                .build();
    }

    public synchronized Chord.ChordNode createGRPCSuccessorNodeFromNode(Node successor) {
        if (successor == null)
            return null;

        return Chord.ChordNode.newBuilder()
                .setIp(successor.getMyIp())
                .setPort(successor.getMyPort())
                .setIdentifier(successor.getMyIdentifier())
                .setM(successor.getM())
                .build();
    }

    public synchronized Chord.ChordNode createGRPCPredecessorNodeFromNode(Node predecessor) {
        if (predecessor == null)
            return null;

        return Chord.ChordNode.newBuilder()
                .setIp(predecessor.getMyIp())
                .setPort(predecessor.getMyPort())
                .setIdentifier(predecessor.getMyIdentifier())
                .setM(predecessor.getM())
                .build();
    }
}
