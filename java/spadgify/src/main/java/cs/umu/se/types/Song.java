package cs.umu.se.types;

/**
 * The Song class represents a song with associated media information, file path, and data.
 * It provides methods to retrieve and update these attributes, as well as to make deep copies of Song objects.
 */
public class Song {

    private final MediaInfo mediaInfo;
    private String filePath = "";
    private byte[] data;

    public Song(MediaInfo mediaInfo, String filePath, byte[] data) {
        this.mediaInfo = mediaInfo;
        this.filePath = filePath;
        this.data = data;
    }

    /**
     * To make a deep copy of a song object
     * @param that the song object to make a deep copy off
     */
    public Song(Song that) {
        this(that.getMediaInfo(), that.getFilePath(), that.getData());
    }

    public MediaInfo getMediaInfo() {
        return mediaInfo;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getIdentifierString() {
        return mediaInfo.getIdentifierString();
    }

    @Override
    public String toString() {
        return mediaInfo.getIdentifierString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return song.getIdentifierString().equals(this.getIdentifierString());
    }
}
