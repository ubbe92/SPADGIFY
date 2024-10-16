package cs.umu.se.client;

import cs.umu.se.chord.Hash;
import cs.umu.se.interfaces.Storage;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;

import java.util.Random;

public class ClientLogicTest {
    private String nodeIp;
    private int nodePort;
    private Storage storage;
    private int m;

    public ClientLogicTest(Storage storage, int m, String nodeIp, int nodePort) {
        this.storage = storage;
        this.m = m;
        this.nodeIp = nodeIp;
        this.nodePort = nodePort;
    }

    private String createRandomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = length;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    private Song createDummySong() {
        long size = 10810096;
        byte[] data = new byte[(int) size];
        String artist = createRandomString(15);
        String song = createRandomString(15);
        String album = createRandomString(15);
        MediaInfo mediaInfo = new MediaInfo(artist, song, album, 120, "", size, m);
        System.out.println("hash: " + mediaInfo.getHash());
        return new Song(mediaInfo, "", data);
    }

    public void testListNodeSong() {
        Song song = createDummySong();

        int lengthBeforeStore = storage.listNodeSongs().length;
        storage.store(song);
        int lengthAfterStore = storage.listNodeSongs().length;

        if (lengthAfterStore != (lengthBeforeStore + 1))
            throw new IllegalStateException("testListNodeSong(): lengthAfterStore != (lengthBeforeStore + 1)");


        int lengthBeforeDelete = lengthAfterStore;
        String identifierString = song.getIdentifierString();
        storage.delete(identifierString);

        int lengthAfterDelete = storage.listNodeSongs().length;

        if (lengthAfterDelete != (lengthBeforeDelete - 1))
            throw new IllegalStateException("testListNodeSong(): lengthAfterDelete != (lengthBeforeDelete - 1)");

        System.out.println("testListNodeSong() done!");
    }

    public void testListAllSongs() {
        Song song = createDummySong();
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
        Song song = createDummySong();
        Song returnSong = storage.retrieve(song.getIdentifierString());

        if (returnSong != null)
            throw new IllegalStateException("testStoreAndDelete(): returnSong != null before storing");

        storage.store(song);
        returnSong = storage.retrieve(song.getIdentifierString());

        if (returnSong == null)
            throw new IllegalStateException("testStoreAndDelete(): returnSong == null after storing");

        storage.delete(song.getIdentifierString());
        returnSong = storage.retrieve(song.getIdentifierString());
        System.out.println("Ret song: " + returnSong);

        if (returnSong != null)
            throw new IllegalStateException("testStoreAndDelete(): returnSong != null after deleting");

        System.out.println("testStoreAndDelete() done!");

    }

}
