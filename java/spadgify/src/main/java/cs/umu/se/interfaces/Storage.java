package cs.umu.se.interfaces;

import cs.umu.se.chord.Node;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;

public interface Storage {

    void store(Song song);

    Song retrieve(String identifierString);

    Song[] retrieve(int nodeIdentifier);

    void delete(String identifierString);

    MediaInfo[] listAllSongs(String identifierString);

    MediaInfo[] listNodeSongs();

    void requestTransfer(Node node);

    void transfer(Song song);
}
