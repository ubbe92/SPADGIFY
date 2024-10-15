package cs.umu.se.client;

import cs.umu.se.chord.Node;
import cs.umu.se.interfaces.Storage;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;

public class MediaBackend implements Storage {

    public MediaBackend(String socketIp, int socketPort, int restPort, int m) {

    }

    @Override
    public void store(Song song) {

    }

    @Override
    public void store(Song[] song) {

    }

    @Override
    public Song retrieve(String identifierString) {
        return null;
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
    public void delete(String identifierString) {

    }

    @Override
    public void delete(String[] identifierString) {

    }

    @Override
    public MediaInfo[] listAllSongs(String identifierString) {
        return new MediaInfo[0];
    }

    @Override
    public MediaInfo[] listNodeSongs() {
        return new MediaInfo[0];
    }

    @Override
    public void requestTransfer(Node node) {

    }

    @Override
    public void transfer(Song song) {

    }
}
