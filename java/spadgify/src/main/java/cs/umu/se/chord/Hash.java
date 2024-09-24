package cs.umu.se.chord;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {

//    public Hash() {}

    public synchronized static byte[] getDigest(String s) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(s.getBytes(StandardCharsets.UTF_8));
        return md.digest();
    }


    public synchronized static String getHashHexString(byte[] digest) {
        StringBuilder hexString = new StringBuilder();

        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public synchronized static BigInteger getHashBigInteger(byte[] digest, int signum) {
        return new BigInteger(signum, digest);
    }

    private synchronized static int getNodeIdentifier(BigInteger hashBigInteger, int maxNrNodes) {
        BigInteger mod = BigInteger.valueOf(maxNrNodes);
        BigInteger bigIntIdentifier = hashBigInteger.mod(mod);
        return bigIntIdentifier.intValue();
    }


    public synchronized static int getNodeIdentifierFromString(String s, int m) {
        int res = -1;
        try {
            byte[] digest = Hash.getDigest(s);
            BigInteger bigInteger = Hash.getHashBigInteger(digest, 1);
            int maxNodes = (int) Math.pow(2, m);
            res = Hash.getNodeIdentifier(bigInteger, maxNodes);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Could not create hash: " + e.getMessage());
            System.exit(1);
        }

        return res;
    }

}
