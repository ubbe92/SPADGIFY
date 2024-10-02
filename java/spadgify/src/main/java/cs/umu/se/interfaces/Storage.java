package cs.umu.se.interfaces;

import cs.umu.se.types.Song;

public interface Storage {

    public void store(Song song);

//    public void store();
    public void retrieve(Song song);

//    public void retrieve();

    public void delete(Song song);

//    public void delete();


}
