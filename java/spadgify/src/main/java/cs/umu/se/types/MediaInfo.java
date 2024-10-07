package cs.umu.se.types;

import cs.umu.se.chord.Hash;

import java.util.Objects;

public class MediaInfo {
    private final String artist;
    private final String song;
    private final String album;
    private final int duration;
    private final String genre;
    private final long size;
    private final int hash;
    private final String identifierString;
    private int m;

    public MediaInfo(String artist, String song, String album, int duration, String genre, long size, int m) {
        this.artist = artist;
        this.song = song;
        this.album = album;
        this.duration = duration;
        this.genre = genre;
        this.size = size;
        this.m = m;
        identifierString = song + "-" + artist + "-" + album;
        this.hash = Hash.getNodeIdentifierFromString(identifierString, m);
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

    @Override
    public String toString() {
        return "MediaInfo{" +
                "artist='" + artist + '\'' +
                ", song='" + song + '\'' +
                ", album='" + album + '\'' +
                ", duration=" + duration +
                ", genre='" + genre + '\'' +
                ", size=" + size +
                ", hash=" + hash +
                ", identifierString='" + identifierString + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaInfo mediaInfo = (MediaInfo) o;
        return hash == mediaInfo.hash && m == mediaInfo.m && identifierString.equals(mediaInfo.getIdentifierString());
    }
}
