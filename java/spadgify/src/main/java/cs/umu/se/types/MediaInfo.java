package cs.umu.se.types;

import cs.umu.se.chord.Hash;

public class MediaInfo {
    private final String artist;
    private final String song;
    private final String album;
    private final int duration;
    private final String genre;
    private final long size;
    private final int hash;
    private final String identifierString;

    public MediaInfo(String artist, String song, String album, int duration, String genre, long size, int hash) {
        this.artist = artist;
        this.song = song;
        this.album = album;
        this.duration = duration;
        this.genre = genre;
        this.size = size;
        this.hash = hash;
        identifierString = song + ":" + artist + ":" + album;
    }

    public String getArtist() {
        return artist;
    }

    public String getSong() {
        return song;
    }

    public String getAlbum() {
        return album;
    }

    public int getDuration() {
        return duration;
    }

    public String getGenre() {
        return genre;
    }

    public long getSize() {
        return size;
    }

    public int getHash() {
        return hash;
    }

    public String getIdentifierString() {
        return identifierString;
    }
}
