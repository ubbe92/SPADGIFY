package cs.umu.se.util;

import cs.umu.se.chord.FingerTableEntry;
import cs.umu.se.chord.Node;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;
import org.tritonus.share.sampled.file.TAudioFileFormat;
import proto.Chord;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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


    public MediaInfo[] convertGRPCChordMediaInfosToMediaInfos(List<Chord.MediaInfo> mediaInfosList) {
        int size = mediaInfosList.size();
        MediaInfo[] mediaInfos = new MediaInfo[size];

        for (int i = 0; i < size; i++) {
            mediaInfos[i] = convertGRPCChordMediaInfoToMediaInfo(mediaInfosList.get(i));
        }

        return mediaInfos;
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

    public List<Chord.MediaInfo> convertMediaInfosToGRPCChordMediaInfos(MediaInfo[] mediaInfos) {
        Chord.MediaInfo[] chordMediaInfos = new Chord.MediaInfo[mediaInfos.length];

        int i = 0;
        for (MediaInfo m : mediaInfos) {
            chordMediaInfos[i] = convertMediaInfoToGRPCChordMediaInfo(m);
            i++;
        }

        return Arrays.asList(chordMediaInfos);
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

    public int getDurationOfMp3(File file) throws UnsupportedAudioFileException, IOException {
        int duration = -1;

        AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
        if (fileFormat instanceof TAudioFileFormat) {
            Map<?, ?> properties = fileFormat.properties();
            String key = "duration";
            Long microseconds = (Long) properties.get(key);
            int mili = (int) (microseconds / 1000);
            duration = (mili / 1000); // get the duration in seconds
        }

        return duration;
    }

    public String[] parseFileName(String fileName) {
        String[] fileInfo = fileName.split("-");
        String[] albumInfo = fileInfo[2].split("\\."); // remove the .mp3 ending of the file
        fileInfo[2] = albumInfo[0];
        return fileInfo;
    }

    public File[] getAllFilesInDirectory(String path) {
        File dir = new File(path);
        return dir.listFiles();
    }

    public Song getSongFromFile(File file) throws IOException, UnsupportedAudioFileException {
        String filePath = file.getPath(); // Used to denote where the file is stored

        byte[] bytes = readFromFile(filePath);
        String[] fileInfo = parseFileName(file.getName());

        String title = fileInfo[0];
        String artist = fileInfo[1];
        String album = fileInfo[2];
        int duration = getDurationOfMp3(file);
        String genre = "UNKNOWN";
        long size = bytes.length;
        int m = this.m;

        MediaInfo mediaInfo = new MediaInfo(artist, title, album, duration, genre, size, m);

        return new Song(mediaInfo, filePath, bytes);
    }

    public Song[] getSongsFromFiles(File[] files) throws UnsupportedAudioFileException, IOException {
        int size = files.length;
        Song[] songs = new Song[size];

        if (files != null)
            for (int i = 0; i < size; i++)
                songs[i] = getSongFromFile(files[i]);
        else
            songs = null;

        return songs;
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

    public void deleteFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not delete file: '" + filePath  + "' reason: " + e.getMessage());
        }
    }


    public boolean isNumberInIntervalInclusiveExclusive(int hash, int leftBound, int rightBound) {
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

    public boolean isNumberInIntervalExclusiveInclusive(int hash, int leftBound, int rightBound) {
        int maxNodes = (int) Math.pow(2, m);

        // If full circle interval e.g. (1, 1]
        if (leftBound == rightBound)
            return true;

        // exc/inc e.g. (x, y]
        if (leftBound < rightBound) {
            // Simple range, no wrapping
            return hash > leftBound && hash <= rightBound;
        } else {
            // Wrapped range
            return (hash > leftBound && hash < maxNodes) ||
                    (hash >= 0 && hash <= rightBound);
        }
    }

    public MediaInfo[] mergeMediaUtilsArrays(MediaInfo[] mediaInfos1, MediaInfo[] mediaInfos2) {
        int size1 = 0;
        int size2 = 0;

        if (mediaInfos1 != null)
            size1 = mediaInfos1.length;

        if (mediaInfos2 != null)
            size2 = mediaInfos2.length;

        int totalSize = size1 + size2;
        MediaInfo[] mediaInfos = new MediaInfo[totalSize];

        if (mediaInfos1 != null)
            System.arraycopy(mediaInfos1, 0, mediaInfos, 0, size1);

        if (mediaInfos2 != null)
            System.arraycopy(mediaInfos2, 0, mediaInfos, size1, size2);

        return mediaInfos;
    }
}
