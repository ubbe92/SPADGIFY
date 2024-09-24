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
}
