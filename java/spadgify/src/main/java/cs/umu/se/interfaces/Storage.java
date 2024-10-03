package cs.umu.se.interfaces;

import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;

import java.io.IOException;

public interface Storage {

    void store(Song song);

    void store(Song[] song);

    Song retrieve(String identifierString);

    Song[] retrieve(String[] identifierString);

    void delete(String identifierString);

    void delete(String[] identifierString);
}
