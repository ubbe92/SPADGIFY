package cs.umu.se.chord;

/**
 * Class to represent a Node object
 */
public class Node {
    private final FingerTable fingerTable;
    private final String myIp;
    private final int myPort;
    private final int m;
    private final int myIdentifier;
    private Node predecessor;

    public Node(String ip, int port, int m) {
        this.myPort = port;
        this.m = m;
        this.myIp = ip;
        String identifierString = myIp + ":" + port;
        this.myIdentifier = Hash.getNodeIdentifierFromString(identifierString, m);

        // Init finger table for this node
        fingerTable = new FingerTable(this);
        predecessor = null;
    }

    /**
     * Display the current finger table and print the nodes successor and predecessor
     */
    public synchronized void displayCurrentTable() {
        fingerTable.displayCurrentTable();
        System.out.println("My successor: " + getSuccessor());
        System.out.println("My predecessor: " + getPredecessor());
    }

    public Node getSuccessor() {
        FingerTableEntry[] table = fingerTable.getTable();
        return table[0].getNode();
    }

    public synchronized void setSuccessor(Node successor) {
        FingerTableEntry[] table = fingerTable.getTable();
        table[0].setNode(successor);
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public synchronized void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }

    public FingerTable getFingerTable() {
        return fingerTable;
    }

    public String getMyIp() {
        return myIp;
    }

    public int getMyPort() {
        return myPort;
    }

    public int getM() {
        return m;
    }

    public int getMyIdentifier() {
        return myIdentifier;
    }

    @Override
    public String toString() {
        return myIp + ":" + myPort + " identifier: " + myIdentifier;
    }

    /**
     * Check equality by comparing port and ip
     * @param o the object to check
     * @return true if object is equal to node else false
     */
//    @Override
//    public boolean equals(Object o) {
//
//        if (o == this)
//            return true;
//
//        if (!(o instanceof Node))
//            return false;
//
//        Node n = (Node) o;
//        return (this.getMyPort() == n.getMyPort()) && (this.getMyIp().equals(n.getMyIp()));
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return myPort == node.myPort && myIp.equals(node.myIp);
    }
}
