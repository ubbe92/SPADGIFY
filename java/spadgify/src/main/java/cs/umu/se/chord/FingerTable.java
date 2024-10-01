package cs.umu.se.chord;

public class FingerTable {
    private int m;
    private int myIdentifier;
    private FingerTableEntry[] table;
    private Node thisNode;

    public FingerTable(Node thisNode) {
        this.m = thisNode.getM();
        this.myIdentifier = thisNode.getMyIdentifier();
        this.thisNode = thisNode;
        this.table = new FingerTableEntry[m];

        // Calculate the start and intervals and put this node as the successor node for each entry
        fillTableWithEntries(myIdentifier, m, null);
    }

    public FingerTableEntry[] getTable() {
        return table;
    }

    public int calculateStartForIndexI(int thisNodeIdentifier, int i) {
        return (int) ((thisNodeIdentifier + Math.pow(2, i)) % Math.pow(2, m));
    }

    public int[] calculateInterval(int thisNodeIdentifier, int i) {
        int[] interval = new int[2];
        interval[0] = calculateStartForIndexI(thisNodeIdentifier, i);
        interval[1] = calculateStartForIndexI(thisNodeIdentifier, i + 1);
        return interval;
    }

    private void fillTableWithEntries(int myIdentifier, int m, Node node) {
        for (int i = 0; i < m; i++) {
            int start = calculateStartForIndexI(myIdentifier, i);
            int[] interval = calculateInterval(myIdentifier, i);

            // Fill table with entries that does not at the moment have any nodes.
            table[i] = new FingerTableEntry(start, interval, node);
        }
    }

    public void displayCurrentTable() {
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
