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
        Song song = null;
        try {
            synchronized (this) {
                song = songHashMap.get(identifierString);
                if (song == null)
                    return song;

                String filePath = song.getFilePath();
                byte[] data = mediaUtil.readFromFile(filePath);
                song.setData(data); // loaded data from disc back into memory
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("retrieve() Could not retrieve song: " + identifierString + " from disc");
        }

        System.out.println("Retrieved song: '" + song + "'");
        return song;
    }

    @Override
    public Song[] retrieve(String[] identifierString) {
        return new Song[0];
    }

    @Override
    public Song[] retrieve(int nodeIdentifier) {
        return new Song[0];
    }

    @Override
    public  void delete(String identifierString) throws IllegalArgumentException {
        synchronized (this) {
            Song song = songHashMap.get(identifierString);

            // delete file on disc
            mediaUtil.deleteFile(song.getFilePath());

            songHashMap.remove(identifierString);
        }
    }

    @Override
    public void delete(String[] identifierString) {

    }

    @Override
    public MediaInfo[] listAllSongs(String identifierString) {
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

    @Override
    public MediaInfo[] listNodeSongs() {
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
}
