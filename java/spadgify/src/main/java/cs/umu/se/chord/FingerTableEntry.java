package cs.umu.se.chord;

/**
 * Class to handle finger table entries
 */
public class FingerTableEntry {
    private final int start;
    private final int[] interval;
    private Node node;

    public FingerTableEntry(int start, int[] interval, Node node) {
        this.start = start;
        this.interval = interval;
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public synchronized void setNode(Node node) {
        this.node = node;
    }

    public int getStart() {
        return start;
    }

    public int[] getInterval() {
        return interval;
    }
}
