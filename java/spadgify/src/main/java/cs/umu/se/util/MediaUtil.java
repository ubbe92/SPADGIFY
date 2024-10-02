package cs.umu.se.util;

import cs.umu.se.chord.FingerTableEntry;
import cs.umu.se.chord.Hash;
import cs.umu.se.chord.Node;
import cs.umu.se.types.MediaInfo;
import proto.Chord;

import java.io.*;

public class MediaUtil {
    private int m;

    public MediaUtil(int m) {
        this.m = m;
    }

    public MediaInfo convertGRPCChordMediaInfoToMediaInfo(Chord.MediaInfo chordMediaInfo) {
        String artist = chordMediaInfo.getArtist();
        String song = chordMediaInfo.getSong();
        String album = chordMediaInfo.getAlbum();
        int duration = chordMediaInfo.getDuration();
        String genre = chordMediaInfo.getGenre();
        long size = chordMediaInfo.getSize();
        return new MediaInfo(artist, song, album, duration, genre, size, m);
    }

    public Chord.MediaInfo convertMediaInfoToGRPCChordMediaInfo(MediaInfo mediaInfo) {
        return Chord.MediaInfo.newBuilder()
                .setArtist(mediaInfo.getArtist())
                .setSong(mediaInfo.getSong())
                .setAlbum(mediaInfo.getAlbum())
                .setDuration(mediaInfo.getDuration())
                .setGenre(mediaInfo.getGenre())
                .setSize(mediaInfo.getSize())
                .build();
    }

    public Node getResponsibleNodeForHash(Node node, int hash) {
        Node destinationNode = node;
        FingerTableEntry[] table = node.getFingerTable().getTable();

        for (int i = 0; i < m; i++) {
            int[] interval = table[i].getInterval();
            int leftInclusive = interval[0];
            int rightExclusive = interval[1];

            if (isNumberInIntervalInclusiveExclusive(hash, leftInclusive, rightExclusive)) {
                destinationNode = table[i].getNode();
                break;
            }
        }

        return destinationNode;
    }

    public void writeToFile(byte[] fileData, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(fileData);
        }
    }

    public byte[] readFromFile(String filePath) throws IOException {
        File file = new File(filePath);

        // Using a FileInputStream to read the file
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];  // Buffer to read chunks of the file
            int bytesRead;

            // Reading file in chunks of 1024 bytes
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            // Return the file content as a byte array
            return baos.toByteArray();
        }
    }


    private boolean isNumberInIntervalInclusiveExclusive(int hash, int leftBound, int rightBound) {
        int maxNodes = (int) Math.pow(2, m);

        // If full circle e.g. [1, 1)
        if (leftBound == rightBound)
            return true;

        // inc/exc e.g. [x, y)
        if (leftBound <= rightBound) {
            // Simple range, no wrapping
            return hash >= leftBound && hash < rightBound;
        } else {
            // Wrapped range
            return (hash >= leftBound && hash < maxNodes) ||
                    (hash >= 0 && hash < rightBound);
        }
    }
}
