package cs.umu.se.util;

import cs.umu.se.chord.Node;
import proto.Chord;

import java.io.IOException;
import java.net.*;

/**
 * Utility class providing various helper methods for handling Chord protocol operations.
 */
public class ChordUtil {

    public ChordUtil() {}

    /**
     * Retrieves the local IP address of the machine by creating a UDP connection to a remote server.
     *
     * @return A string representing the local IP address of the machine. If the IP address cannot be determined, an empty string is returned.
     */
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

    /**
     * Scans the given port range for an available port.
     *
     * @param startRange The starting port number of the range to scan.
     * @param stopRange The ending port number of the range to scan.
     * @return The first available port in the specified range, or -1 if no ports are available.
     */
    public synchronized int getAvailablePortGRPC(int startRange, int stopRange) {
        for (int port = startRange; port < stopRange; port++) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                if (!(serverSocket.getLocalPort() == port)) continue;

                return port;
            } catch (IOException e) {
                System.out.println("port in use..." + e.getMessage());
            }
        }

        return -1;
    }

    public synchronized int getAvailablePortSocket(int startRange, int stopRange) {
        String ip = getLocalIp();
        Socket portCheck;
        for (int port = startRange; port < stopRange; port++) {
            try {
                System.out.println("Looking for " + port);
                portCheck = new Socket(ip, port);
                System.out.println("There is a server running on port " + port);
            } catch (UnknownHostException e) {
                System.out.println("Unknown host exception: " + e);
                break;
            } catch (IOException e) {
                return port; // Could not establish connection == available port
            }
        }
        return -1;
    }

    /**
     * Creates a new Node instance from a given GRPC ChordNode.
     *
     * @param node The GRPC ChordNode to be converted to a Node instance.
     * @return A new Node instance created from the given GRPC ChordNode.
     */
    public synchronized Node createNodeFromGRPCChordNode(Chord.ChordNode node) {
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

    /**
     * Creates a GRPC ChordNode from a given Node instance.
     *
     * @param node The Node instance to be converted to a GRPC ChordNode.
     * @return A GRPC ChordNode representation of the given Node instance.
     */
    public synchronized Chord.ChordNode createGRPCChordNodeFromNode(Node node) {
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
            succ = createGRPCNodeFromNode(successor);
            build.setSuccessor(succ);
        }

        if (predecessor != null) {
            pred = createGRPCNodeFromNode(predecessor);
            build.setPredecessor(pred);
        }

        return build.build();
    }


    private synchronized Chord.ChordNode createGRPCNodeFromNode(Node node) {
        return Chord.ChordNode.newBuilder()
                .setIp(node.getMyIp())
                .setPort(node.getMyPort())
                .setIdentifier(node.getMyIdentifier())
                .setM(node.getM())
                .build();
    }
}
