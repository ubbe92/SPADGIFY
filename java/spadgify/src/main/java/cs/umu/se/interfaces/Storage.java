package cs.umu.se.interfaces;

import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;

import java.io.IOException;

public interface Storage {

    public void store(Song song);

    public Song retrieve(String identifierString);

    public void delete(String identifierString);
}
