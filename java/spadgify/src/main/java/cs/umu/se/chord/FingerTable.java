package cs.umu.se.chord;

/**
 * Class to handle finger table for a node
 */
public class FingerTable {
    private final int m;
    private int myIdentifier;
    private final FingerTableEntry[] table;
    private final Node thisNode;

    public FingerTable(Node thisNode) {
        this.m = thisNode.getM();
        this.myIdentifier = thisNode.getMyIdentifier();
        this.thisNode = thisNode;
        this.table = new FingerTableEntry[m];

        // Calculate the start and intervals and put this node as the successor node for each entry
        fillTableWithEntries(myIdentifier, m);
    }

    public FingerTableEntry[] getTable() {
        return table;
    }

    /**
     * Calculate the start value for the interval associated with an entry in the finger table
     * @param thisNodeIdentifier the identifier for a node
     * @param i the index in the finger table
     * @return the value for the interval
     */
    public int calculateStartForIndexI(int thisNodeIdentifier, int i) {
        return (int) ((thisNodeIdentifier + Math.pow(2, i)) % Math.pow(2, m));
    }

    /**
     * Calculate the start and end values for an interval associated with an entry in the finger table
     * @param thisNodeIdentifier the identifier for a node
     * @param i the index in the finger table
     * @return the interval
     */
    public int[] calculateInterval(int thisNodeIdentifier, int i) {
        int[] interval = new int[2];
        interval[0] = calculateStartForIndexI(thisNodeIdentifier, i);
        interval[1] = calculateStartForIndexI(thisNodeIdentifier, i + 1);
        return interval;
    }

    /**
     * Fills the finger table with entries
     * @param myIdentifier this nodes identifier
     * @param m m-bit identifier
     */
    private void fillTableWithEntries(int myIdentifier, int m) {
        for (int i = 0; i < m; i++) {
            int start = calculateStartForIndexI(myIdentifier, i);
            int[] interval = calculateInterval(myIdentifier, i);

            // Fill table with entries that does not at the moment have any nodes.
            table[i] = new FingerTableEntry(start, interval, null);
        }
    }

    /**
     * Print the current table to the terminal
     */
    public synchronized void displayCurrentTable() {
        System.out.println("IP: " + thisNode.getMyIp() +
                ", PORT: " + thisNode.getMyPort() +
                ", identifier: " + thisNode.getMyIdentifier());

        System.out.println("Start\tInterval\tSuccessor");
        for (FingerTableEntry e : table) {
            int[] interval = e.getInterval();

            if (e.getNode() != null)
                System.out.println(e.getStart() + "\t[" + interval[0] + ", " + interval[1] + ")\t\t" + e.getNode().getMyIdentifier());
            else
                System.out.println(e.getStart() + "\t[" + interval[0] + ", " + interval[1] + ")\t\t" + "null");

        }
    }

}
