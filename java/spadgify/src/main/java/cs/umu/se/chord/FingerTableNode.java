package cs.umu.se.chord;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public class FingerTableNode {

    private String ip;
    private int port;
    private int nodeIdentifier;
    private BigInteger hash;

    public FingerTableNode(String ip, int port, int m) {
        this.ip = ip;
        this.port = port;
        String identifierString = ip + ":" + port;
        try {
            hash = Hash.getHashBigInteger(Hash.getDigest(identifierString), 1);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        this.nodeIdentifier = Hash.getNodeIdentifierFromString(identifierString, m);
    }

    @Override
    public String toString() {
        return String.valueOf(nodeIdentifier);
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getNodeIdentifier() {
        return nodeIdentifier;
    }
}
