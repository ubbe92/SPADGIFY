package cs.umu.se.types;

public class Song {

    private MediaInfo mediaInfo;
    private String filePath = "";
    private byte[] data;

    public Song(MediaInfo mediaInfo, String filePath, byte[] data) {
        this.mediaInfo = mediaInfo;
        this.filePath = filePath;
        this.data = data;
    }

    public MediaInfo getMediaInfo() {
        return mediaInfo;
    }

    public String getFilePath() {
        return filePath;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
