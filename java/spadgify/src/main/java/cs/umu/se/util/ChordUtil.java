package cs.umu.se.util;

import cs.umu.se.chord.FingerTableNode;
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

    public synchronized Node createNodeFromGRPCNode(Chord.ChordNode node) {

        return null;
    }

    public synchronized Chord.ChordNode createGRPCNodeFromNode(Node node) {
        String ip = node.getMyIp();
        int port = node.getMyPort();
        int identifier = node.getMyIdentifier();
        int m = node.getM();

        Chord.ChordNode succ = createGRPCSuccessorNodeFromNode(node.getSuccessor());
        Chord.ChordNode pred = createGRPCPredecessorNodeFromNode(node.getPredecessor());

        if (succ == null || pred == null) {
            return Chord.ChordNode.newBuilder()
                    .setIp(ip)
                    .setPort(port)
                    .setIdentifier(identifier)
                    .setM(m)
                    .build();
        }

        return Chord.ChordNode.newBuilder()
                .setIp(ip)
                .setPort(port)
                .setIdentifier(identifier)
                .setM(m)
                .setSuccessor(succ)
                .setPredecessor(pred)
                .build();
    }

    public synchronized Node createSuccessorNodeFromGRPCNode(Chord.ChordNode successor) {

        return null;
    }

    public synchronized Node createPredecessorNodeFromGRPCNode(Chord.ChordNode predecessor) {
        return null;
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
