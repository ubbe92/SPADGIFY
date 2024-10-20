package cs.umu.se.storage;

import cs.umu.se.chord.Node;
import cs.umu.se.interfaces.Storage;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;
import cs.umu.se.util.MediaUtil;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The StorageBackend class provides an implementation of the Storage interface.
 * It is responsible for storing, retrieving, deleting, and transferring songs
 * from a storage backend, using a combination of in-memory storage and file
 * storage.
 */
public class StorageBackend implements Storage {

    private final MediaUtil mediaUtil;
    private final HashMap<String, Song> songHashMap = new HashMap<>(); // Key format: song + "-" + artist + "-" + album
    private final Node node;
    private final Logger logger;

    public StorageBackend(Node node, int m, Logger logger) {
        this.node = node;
        this.logger = logger;
        mediaUtil = new MediaUtil(m);
    }

    /**
     * Stores the given song to the local storage, ensuring the data is written to a file and the song entry
     * is saved in the internal hash map for future retrieval.
     *
     * @param song the song object containing the metadata and data to be stored.
     *             The method writes the song data to the file path specified in the song object.
     */
    @Override
    public void store(Song song) {
        try {
            synchronized (this) {
                mediaUtil.writeToFile(song.getData(), song.getFilePath());
                songHashMap.put(song.getIdentifierString(), song);
                song.setData(null); // we don't need to hold this in memory, retrieve method will add the data back
            }
        } catch (IOException e) {
            logger.error("Could not save song : \"{}\" to disc. {}", song, Arrays.toString(e.getStackTrace()));
        }
        logger.info("Storing: \"{}\" at: \"{}\"", song, song.getFilePath());
    }

    /**
     * Retrieves a Song object corresponding to the given identifier string.
     * If the song is found in the internal storage but its data is not loaded into memory,
     * the song's data will be read from the file system and set to the Song object.
     *
     * @param identifierString the unique identifier string of the song to be retrieved.
     * @return the Song object corresponding to the identifier string, with data loaded from the file system if necessary. Returns null if the song is not found.
     */
    @Override
    public Song retrieve(String identifierString) {
        Song song = null;
        try {
            synchronized (this) {
                song = songHashMap.get(identifierString);
                if (song == null)
                    return song;

                String filePath = song.getFilePath();
                byte[] data = mediaUtil.readFromFile(filePath);
                song.setData(data); // loaded data from disc back into memory

//                displayFilePaths();
            }
        } catch (Exception e) {
            logger.error("retrieve() Could not retrieve song: {} from disc. {}", identifierString, Arrays.toString(e.getStackTrace()));
        }

        logger.info("Retrieved song: \"{}\"", song);
        return song;
    }

    /**
     * Retrieves an array of Song objects within a specified node identifier interval.
     * The method ensures that the song data is read from the file and set to each Song object.
     *
     * @param nodeIdentifier the identifier of the node up to which songs should be retrieved.
     * @return an array of Song objects whose hashes fall within the interval.
     */
    @Override
    public Song[] retrieve(int nodeIdentifier) {
        synchronized (this) {
            ArrayList<Song> songArrayList = new ArrayList<>();

            for (Song s: songHashMap.values()) {
                int hash = s.getMediaInfo().getHash();
                boolean isInInterval = mediaUtil.isNumberInIntervalExclusiveInclusive(hash, node.getMyIdentifier(), nodeIdentifier);

                if (isInInterval) {
                    String filePath = s.getFilePath();
                    try {
                        byte[] data = mediaUtil.readFromFile(filePath);
                        s.setData(data);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    songArrayList.add(s);
                }
            }

            Song[] songs = new Song[songArrayList.size()];
            return songArrayList.toArray(songs);
        }
    }

    /**
     * Deletes a song from the local storage based on the given identifier string.
     * This method removes the song from the internal hash map and deletes the associated file from the disk.
     *
     * @param identifierString the unique identifier string of the song to be deleted.
     * @throws IllegalArgumentException if the song cannot be deleted.
     */
    @Override
    public void delete(String identifierString) throws IllegalArgumentException {
        synchronized (this) {
            Song song = songHashMap.get(identifierString);

            // delete file on disc
            mediaUtil.deleteFile(song.getFilePath());

            songHashMap.remove(identifierString);

            logger.info("Deleted song: \"{}\"", song);
        }
    }

    @Override
    public MediaInfo[] listAllSongs(String identifierString) {
        return getMediaInfos();
    }

    @Override
    public MediaInfo[] listNodeSongs() {
        return getMediaInfos();
    }

    @Override
    public void requestTransfer(Node node) {

    }

    @Override
    public void transfer(Song song) {

    }

    /**
     * Retrieves an array of MediaInfo objects representing the metadata of all songs stored in the internal hash map.
     *
     * @return an array of MediaInfo objects.
     */
    private MediaInfo[] getMediaInfos() {
        synchronized (this) {
            int size = songHashMap.size();
            MediaInfo[] mediaInfos = new MediaInfo[size];

            int i = 0;
            for (Song s: songHashMap.values()) {
                mediaInfos[i] = s.getMediaInfo();
                i++;
            }

            return mediaInfos;
        }
    }

    private void displayFilePaths() {
        for (Song s : songHashMap.values()) {
            System.out.println("path: " + s.getFilePath() + " for file: " + s);
        }
    }
}
