package cs.umu.se.storage;

import cs.umu.se.interfaces.Storage;
import cs.umu.se.types.Song;
import cs.umu.se.util.MediaUtil;

import java.io.IOException;
import java.util.HashMap;

public class StorageBackend implements Storage {

    private MediaUtil mediaUtil;
    private HashMap<String, Song> songHashMap = new HashMap<>(); // Key format: song + "-" + artist + "-" + album

    public StorageBackend(int m) {
        mediaUtil = new MediaUtil(m);
    }

    @Override
    public void store(Song song) {
        try {
            synchronized (this) {
                mediaUtil.writeToFile(song.getData(), song.getFilePath());
                songHashMap.put(song.getIdentifierString(), song);
                song.setData(null); // we don't need to hold this in memory, retrieve method will add the data back
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not save song: '" + song + "' to disc: " + e.getMessage());
        }

        System.out.println("Storing: '" + song + "' at: " + song.getFilePath());
    }

    @Override
    public void store(Song[] song) {

    }

    @Override
    public Song retrieve(String identifierString) {
        Song song;
        try {
            synchronized (this) {
                song = songHashMap.get(identifierString);
                String filePath = song.getFilePath();
                byte[] data = mediaUtil.readFromFile(filePath);
                song.setData(data); // loaded data from disc back into memory
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("retrieve() Could not retrieve song: " + identifierString + " from disc");
            song = null;
        }

        System.out.println("Retrieved song: '" + song + "'");
        return song;
    }

    @Override
    public Song[] retrieve(String[] identifierString) {
        return new Song[0];
    }

    @Override
    public synchronized void delete(String identifierString) throws IllegalArgumentException {
        Song song = songHashMap.get(identifierString);

        // delete file on disc
        mediaUtil.deleteFile(song.getFilePath());

        songHashMap.remove(identifierString);
    }

    @Override
    public void delete(String[] identifierString) {

    }

}
