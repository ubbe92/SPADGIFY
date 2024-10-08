package cs.umu.se.interfaces;

import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;

public interface Storage {

    void store(Song song);

    void store(Song[] song);

    Song retrieve(String identifierString);

    Song[] retrieve(String[] identifierString);

    Song[] retrieve(int nodeIdentifier);

    void delete(String identifierString);

    void delete(String[] identifierString);

    MediaInfo[] listAllSongs(String identifierString);

    MediaInfo[] listSongsFromNode();

    MediaInfo[] listSongsInIntervalFromNode(int nodeIdentifier);

    Song retrieveFromNode(String identifierString);

    void deleteFromNode(String identifierString);
}
