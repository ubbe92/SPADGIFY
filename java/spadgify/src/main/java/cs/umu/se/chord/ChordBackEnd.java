package cs.umu.se.chord;

public class ChordBackEnd {

    private Node node;

    public ChordBackEnd(Node node) {
        this.node = node;
    }

    // node (node) joins the network;
    // nodePrime (n') is an arbitrary node in the network
    public synchronized void join(Node nodePrime) {
        System.out.println("join()");

        // Make grpc call to node

        if (nodePrime != null) { // Temp condition
            System.out.println("node is joining the network: " + node.getMyIp() + ":" + node.getMyPort());
            initFingerTable(nodePrime);
            updateOthers();

            // move keys in (predecessor; n] from successor

        } else { // node is the only node in the network
            System.out.println("node is the only node in the network");
            FingerTableEntry[] table = node.getFingerTable().getTable();
            for (int i = 0; i < node.getM(); i++) {
                table[i].setNode(node);
            }
            node.setPredecessor(node);
        }

        node.displayCurrentTable();
    }

    // initialize finger table of local node;
    // n' is an arbitrary node already in the network
    public synchronized void initFingerTable(Node nodePrime) {
//        System.out.println("initFingerTable()");
//        FingerTableEntry[] table = node.getFingerTable().getTable();
//        int start = table[0].getStart();
//
//        Node node = gRPCFindSuccessor(nodePrime, start);
//        table[0].setNode(node);
//
//        Node pred = node.getSuccessor().getPredecessor();
//        node.setPredecessor(pred);
//        Node succ = node.getSuccessor();
//        succ.setPredecessor(node);
//
//        int m = node.getM();
//        for (int i = 0; i < m - 1; i++) {
//            int fingerStart = table[i+1].getStart();
//            Node fingerNode = table[i].getNode();
//
//            if (isStartInIntervalNodeToFingerNodeI(fingerStart, node, fingerNode)) {
//                table[i+1].setNode(fingerNode);
//            } else {
//                Node n = gRPCFindSuccessor(nodePrime, fingerStart);
//                table[i+1].setNode(n);
//            }
//        }
    }

    public synchronized void updateOthers() {
        System.out.println("updateOthers()");
    }

    public synchronized void updateFingerTable() {
        System.out.println("updateFingerTable()");
    }


    // ask node n to find id’s successor
    public synchronized Node findSuccessor(int id) {
        System.out.println("Find successor()");
        Node nodePrime = findPredecessor(id);
        return nodePrime.getSuccessor();
    }


    // ask node n to find id’s predecessor
    public synchronized Node findPredecessor(int id) {
        System.out.println("Find predecessor()");
        Node nodePrime = node;

        while (!isIdInIntervalNodePrimeToNodePrimeSuccessor(id, nodePrime, nodePrime.getSuccessor())) {
            nodePrime = closestPrecedingFinger(id);
        }

        return nodePrime;
    }


    // return closest finger preceding id
    public synchronized Node closestPrecedingFinger(int id) {
        System.out.println("Closest preceding finger()");

        int m = node.getM();
        FingerTableEntry[] table = node.getFingerTable().getTable();

        for (int i = m - 1; i >= 0; i--) {
            Node fingerNode = table[i].getNode();

            if (isFingerNodeIdentifierInIntervalNodeToId(fingerNode, node, id))
                return fingerNode;
        }

        return node;
    }

    public boolean isIdInIntervalNodePrimeToNodePrimeSuccessor(int nodeIdentifier, Node nodePrime, Node successor) {

        int leftBound = nodePrime.getMyIdentifier();
        int rightBound = successor.getMyIdentifier();
        int m = node.getM();

        if (leftBound <= rightBound) {
            System.out.println("isIdInIntervalNodePrimeToNodePrimeSuccessor() Left lessOrEq than right - node identifier: "
                    + nodeIdentifier + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return nodeIdentifier > leftBound && nodeIdentifier <= rightBound;
        }
        else {
            System.out.println("isIdInIntervalNodePrimeToNodePrimeSuccessor() Left greater than right - node identifier: "
                    + nodeIdentifier + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return (nodeIdentifier > leftBound && nodeIdentifier <= m) || (nodeIdentifier > 0 && nodeIdentifier <= rightBound);
        }
    }

    public boolean isFingerNodeIdentifierInIntervalNodeToId(Node fingerNode, Node n, int id) {

        int leftBound = n.getMyIdentifier();
        int rightBound = id;
        int fingerNodeIdentifier = fingerNode.getMyIdentifier();
        int m = node.getM();

        if (leftBound <= rightBound) {
            System.out.println("isFingerNodeIdentifierInIntervalNodeToId() Left lessOrEq than right - node identifier: "
                    + fingerNodeIdentifier + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return fingerNodeIdentifier > leftBound && fingerNodeIdentifier < rightBound;
        }
        else {
            System.out.println("isFingerNodeIdentifierInIntervalNodeToId() Left greater than right - node identifier: "
                    + fingerNodeIdentifier + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return (fingerNodeIdentifier > leftBound && fingerNodeIdentifier < m)
                    || (fingerNodeIdentifier > 0 && fingerNodeIdentifier < rightBound);
        }
    }

    public synchronized boolean isStartInIntervalNodeToFingerNodeI(int fingerStartIPlusOne, Node node, Node fingerNodeI) {
        int leftBound = node.getMyIdentifier();
        int rightBound = fingerNodeI.getMyIdentifier();
        int m = node.getM();

        if (leftBound < rightBound) {
            System.out.println("isStartInIntervalNodeToFingerNodeI() Left lessOrEq than right - node identifier: "
                    + fingerStartIPlusOne + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return fingerStartIPlusOne >= leftBound && fingerStartIPlusOne < rightBound;
        }
        else {
            System.out.println("isStartInIntervalNodeToFingerNodeI() Left greater than right - node identifier: "
                    + fingerStartIPlusOne + ", left bound: " + leftBound + ", right bound: " + rightBound);

            return (fingerStartIPlusOne >= leftBound && fingerStartIPlusOne < m)
                    || (fingerStartIPlusOne >= 0 && fingerStartIPlusOne < rightBound);
        }
    }
}
