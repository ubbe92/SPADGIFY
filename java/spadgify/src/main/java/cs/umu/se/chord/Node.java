package cs.umu.se.chord;

import cs.umu.se.util.ChordUtil;

public class Node {
    private FingerTable fingerTable;
    private String myIp;
    private int myPort;
    private int m;
    private int myIdentifier;
    private ChordUtil chordUtil = new ChordUtil();
    private Node successor;
    private Node predecessor;

    public Node(String ip, int port, int m) {
        this.myPort = port;
        this.m = m;
        this.myIp = ip;
        String identifierString = myIp + ":" + port;
        this.myIdentifier = Hash.getNodeIdentifierFromString(identifierString, m);

        // Init finger table for this node
        fingerTable = new FingerTable(this);

        // We need to update these somehow at a later date
        successor = getSuccessor();
        predecessor = successor;

        System.out.println("Created node with identifier string: " + identifierString);
        displayCurrentTable();
    }

    public void displayCurrentTable() {
        fingerTable.displayCurrentTable();
    }

    public Node getSuccessor() {
        FingerTableEntry[] table = fingerTable.getTable();
        return table[0].getNode();
    }

    public void setSuccessor(Node successor) {
        FingerTableEntry[] table = fingerTable.getTable();
        table[0].setNode(successor);
    }

    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
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

    @Override
    public boolean equals(Object o) {

        if (o == this)
            return true;

        if (!(o instanceof Node))
            return false;

        Node n = (Node) o;
        return (this.getMyPort() == n.getMyPort()) && (this.getMyIp().equals(n.getMyIp()));
    }
}
