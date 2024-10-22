package cs.umu.se.workers;

import cs.umu.se.client.MusicStreamingClient;
import cs.umu.se.types.Song;

import java.net.URI;

public class WebSocketTaskWorker implements Runnable {
    private final boolean getAll;
    private MusicStreamingClient musicStreamingClient;
    private String getThisSong;
    private long time = 0;

    public WebSocketTaskWorker(Song song, URI uri, boolean getAll) {
        this.musicStreamingClient = new MusicStreamingClient(uri);
        this.getThisSong = song.getIdentifierString();
        this.getAll = getAll;
    }


    @Override
    public void run() {
        try {
            musicStreamingClient.addHeader("getAll", String.valueOf(getAll));
            long t1 = System.currentTimeMillis();
            musicStreamingClient.connectBlocking();
            musicStreamingClient.send(getThisSong);
            musicStreamingClient.awaitResponse();
            musicStreamingClient.closeBlocking();
            long t2 = System.currentTimeMillis();
            time = t2 - t1;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method gets the time it took the thread to execute its run method.
     * @return the time.
     */
    public long getTime() {
        return time;
    }
}
