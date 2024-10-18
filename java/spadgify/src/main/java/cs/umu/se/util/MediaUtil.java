package cs.umu.se.util;

import cs.umu.se.chord.FingerTableEntry;
import cs.umu.se.chord.Node;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
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
import java.util.Random;

/**
 * MediaUtil is a utility class for various media-related operations including converting media information
 * between different formats, extracting metadata from files, and performing hash calculations for a distributed
 * chord network.
 */
public class MediaUtil {
    private final int m;

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

    /**
     * Determines the responsible node for the given hash value.
     *
     * @param node the starting node for the searching process
     * @param hash the hash value for which the responsible node is to be found
     * @return the node responsible for handling the given hash
     */
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

    /**
     * Retrieves the duration of an MP3 file in seconds.
     *
     * @param file the MP3 file for which the duration is to be retrieved
     * @return the duration of the MP3 file in seconds, or -1 if the duration could not be determined
     * @throws UnsupportedAudioFileException if the specified file is not a valid audio file format
     * @throws IOException if an I/O error occurs while reading the file
     */
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

    /**
     * Retrieves a Song object from a specified file.
     *
     * @param file the File object representing the audio file to be processed
     * @return a Song object containing the media information and data derived from the specified file
     * @throws IOException if an I/O error occurs while reading the file
     * @throws UnsupportedAudioFileException if the specified file is not a valid audio file format
     */
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

    /**
     * Merges two arrays of MediaInfo objects into a single array.
     *
     * @param mediaInfos1 the first array of MediaInfo objects to be merged
     * @param mediaInfos2 the second array of MediaInfo objects to be merged
     * @return a new array of MediaInfo objects containing all elements from both input arrays
     */
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

    public String createRandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public Song createDummySong(long size) {
        byte[] data = new byte[(int) size];
        String artist = createRandomString(15);
        String song = createRandomString(15);
        String album = createRandomString(15);
        MediaInfo mediaInfo = new MediaInfo(artist, song, album, 120, "", size, m);
        return new Song(mediaInfo, "", data);
    }

    public JSONArray convertRepresentationToJsonArray(Representation entity) throws IOException, ParseException {
        String s = entity.getText();
        JSONParser parser = new JSONParser();
        return (JSONArray) parser.parse(s);
    }

    public MediaInfo[] convertJSONArrayToMediaInfos(JSONArray jsonArray) {
        MediaInfo[] mediaInfos = new MediaInfo[jsonArray.size()];

        int i = 0;
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            mediaInfos[i] = convertJSONObjectToMediaInfo(jsonObject);
            i++;
        }

        return mediaInfos;
    }

    public MediaInfo convertJSONObjectToMediaInfo(JSONObject json) {
        String song = (String) json.get("song");
        long duration = (long) json.get("duration");
        long size = (long) json.get("size");
        String artist = (String) json.get("artist");
        String album = (String) json.get("album");
        String genre = (String) json.get("genre");

        return new MediaInfo(artist, song, album, (int) duration, genre, size, m);
    }

    public Response makeRestletRequestWithoutBody(String url, Method method, Client restClient) {
        Request request = new Request(method, url, null);
        return restClient.handle(request);
    }

    /**
     * This method creates an array of dummy songs. In order to make the cache testable, every other song is replaced
     * with one of the Math.floor(Math.sqrt(nrSongs)) popular songs. Hence, some songs will be more popular and thereby
     * be in the cache
     * @param nrSongs the number songs requested
     * @param songSize size of each song
     * @return an array of songs containing duplicates in order to test the cache
     */
    public Song[] createCacheableDummySongs(int nrSongs, long songSize) {
        Song[] songs = new Song[nrSongs];
        for (int k = 0; k < nrSongs; k++) {
            songs[k] = createDummySong(songSize);
        }

        int nrPopularSongs = (int) Math.floor(Math.sqrt(songs.length));
        Song[] popularSongs = new Song[nrPopularSongs];
        for (int k = 0; k < nrPopularSongs; k++)
            popularSongs[k] = createDummySong(songSize);

        for (int k = 0; k < songs.length; k++) {
            if (k % 2 == 0) {
                Random rand = new Random();
                int index = rand.nextInt(nrPopularSongs);
                songs[k] = popularSongs[index];
            }
        }

        return songs;
    }

    /**
     * This method creates an array of dummy songs of length nrSongs. Each song will be unique, hence the cache inside
     * the nodes will not be used since each song in this array is unique.
     * @param nrSongs the number songs requested
     * @param songSize size of each song
     * @return an array of unique songs
     */
    public Song[] createDummySongs(int nrSongs, long songSize) {
        // create dummy data
        Song[] songs = new Song[nrSongs];
        for (int k = 0; k < nrSongs; k++) {
            songs[k] = createDummySong(songSize);
        }

        return songs;
    }
}
