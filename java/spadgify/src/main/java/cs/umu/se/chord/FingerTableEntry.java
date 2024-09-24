package cs.umu.se.chord;

public class FingerTableEntry {
    private int start;
    private int[] interval;
    private Node node;

    public FingerTableEntry(int start, int[] interval, Node node) {
        this.start = start;
        this.interval = interval;
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getStart() {
        return start;
    }

    public int[] getInterval() {
        return interval;
    }
}
