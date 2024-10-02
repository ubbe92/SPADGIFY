package cs.umu.se.storage;

import cs.umu.se.interfaces.Storage;
import cs.umu.se.types.MediaInfo;
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
    public synchronized void store(Song song) {

        try {
            mediaUtil.saveToFile(song.getData(), song.getFilePath());
            String key = song.getIdentifierString();
            songHashMap.put(key, song);

            song.setData(null); // we don't need to hold this in memory, retrieve method will add the data back
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("store() Could not save song: '" + song + "' to disc.");
        }
    }

    @Override
    public synchronized Song retrieve(String identifierString) {
        Song song;
        try {
            song = songHashMap.get(identifierString);
            String filePath = song.getFilePath();
            byte[] data = mediaUtil.readFromFile(filePath);
            song.setData(data); // loaded data from disc back into memory
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("retrive() Could not retrieve song: " + identifierString + " from disc");
            song = null;
        }

        return song;
    }

    @Override
    public synchronized void delete(String identifierString) {

    }

}
