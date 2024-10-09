package cs.umu.se.chord;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class used to hash a value
 */
public class Hash {

    /**
     * Creates a SHA-1 digest given a string
     * @param s the string to digest
     * @return the digest as a byte array
     * @throws NoSuchAlgorithmException if algorithm doesnt exist
     */
    public synchronized static byte[] getDigest(String s) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(s.getBytes(StandardCharsets.UTF_8));
        return md.digest();
    }

    /**
     * Converts the digest to a hex string
     * @param digest the digest to convert
     * @return the converted digest in hex string format
     */
    public synchronized static String getHashHexString(byte[] digest) {
        StringBuilder hexString = new StringBuilder();

        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    /**
     * Creates a Big Integer given a digest and signum
     * @param digest the digest
     * @param signum the signum
     * @return a Big Integer representation of the hash
     */
    public synchronized static BigInteger getHashBigInteger(byte[] digest, int signum) {
        return new BigInteger(signum, digest);
    }

    /**
     * Converts a hash as Big Integer to a hash as int
     * @param hashBigInteger hash as Big Integer
     * @param maxNrNodes max number of nodes in Chord network i.e. 2^m
     * @return node identifier
     */
    private synchronized static int getNodeIdentifier(BigInteger hashBigInteger, int maxNrNodes) {
        BigInteger mod = BigInteger.valueOf(maxNrNodes);
        BigInteger bigIntIdentifier = hashBigInteger.mod(mod);
        return bigIntIdentifier.intValue();
    }

    /**
     * Converts a string (ip:port) to an identifier
     * @param s the string
     * @param m m-bit identifier
     * @return an identifier
     */
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
