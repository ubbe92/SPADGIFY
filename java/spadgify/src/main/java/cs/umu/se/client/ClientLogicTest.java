package cs.umu.se.client;

import cs.umu.se.chord.Hash;
import cs.umu.se.interfaces.Storage;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;
import cs.umu.se.util.MediaUtil;

import java.util.Random;

public class ClientLogicTest {
    private MediaUtil mediaUtil;
    private String nodeIp;
    private int nodePort;
    private Storage storage;
    private int m;

    public ClientLogicTest(Storage storage, int m, String nodeIp, int nodePort) {
        this.storage = storage;
        this.m = m;
        this.nodeIp = nodeIp;
        this.nodePort = nodePort;
        this.mediaUtil = new MediaUtil(m);
    }

    public void testListAllSongs() {
        long size = 10810096;
        Song song = mediaUtil.createDummySong(size);
        String identifierString = nodeIp + ":" + nodePort;

        int lengthBeforeStore = storage.listAllSongs(identifierString).length;
        storage.store(song);
        int lengthAfterStore = storage.listAllSongs(identifierString).length;

        if (lengthAfterStore != (lengthBeforeStore + 1))
            throw new IllegalStateException("testListAllSongs(): lengthAfterStore != (lengthBeforeStore + 1)");

        int lengthBeforeDelete = lengthAfterStore;
        String songIdentifierString = song.getIdentifierString();
        storage.delete(songIdentifierString);
        int lengthAfterDelete = storage.listAllSongs(identifierString).length;

        if (lengthAfterDelete != (lengthBeforeDelete - 1))
            throw new IllegalStateException("testListNodeSong(): lengthAfterDelete != (lengthBeforeDelete - 1)");

        System.out.println("testListAllSongs() done!");

    }
    public void testStoreAndDelete() {
        long size = 10810096;
        Song song = mediaUtil.createDummySong(size);
        Song returnSong = storage.retrieve(song.getIdentifierString());

        if (returnSong != null)
            throw new IllegalStateException("testStoreAndDelete(): returnSong != null before storing");

        storage.store(song);
        returnSong = storage.retrieve(song.getIdentifierString());

        if (returnSong == null)
            throw new IllegalStateException("testStoreAndDelete(): returnSong == null after storing");

        storage.delete(song.getIdentifierString());
        returnSong = storage.retrieve(song.getIdentifierString());

        if (returnSong != null)
            throw new IllegalStateException("testStoreAndDelete(): returnSong != null after deleting");

        System.out.println("testStoreAndDelete() done!");

    }

    public void testStoreDuplicate() {
        long size = 10810096;
        Song song1 = mediaUtil.createDummySong(size);
        Song song2 = mediaUtil.createDummySong(size);
        String identifierString = nodeIp + ":" + nodePort;

        int lengthBeforeStore = storage.listAllSongs(identifierString).length;
        if (lengthBeforeStore != 0)
            throw new IllegalStateException("testStoreDuplicate():  lengthBeforeStore != 0");

        storage.store(song1);
        storage.store(song1);

        int lengthAfterStore = storage.listAllSongs(identifierString).length;
        if (lengthAfterStore != 1)
            throw new IllegalStateException("testStoreDuplicate():  lengthAfterStore != 1");

        storage.store(song2);
        lengthAfterStore = storage.listAllSongs(identifierString).length;
        if (lengthAfterStore != 2)
            throw new IllegalStateException("testStoreDuplicate():  lengthAfterStore != 2");

        storage.delete(song1.getIdentifierString());
        storage.delete(song2.getIdentifierString());

        int lengthAfterDelete = storage.listAllSongs(identifierString).length;
        if (lengthAfterDelete != 0)
            throw new IllegalStateException("testStoreDuplicate():  lengthAfterDelete != 0");

        System.out.println("testStoreDuplicate() done!");
    }
}
