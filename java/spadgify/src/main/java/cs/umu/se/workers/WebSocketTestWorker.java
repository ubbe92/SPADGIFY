package cs.umu.se.workers;

import cs.umu.se.client.MusicStreamingClient;
import cs.umu.se.types.Song;

import java.net.URI;

public class WebSocketTestWorker implements Runnable {
    private Song song;
    private URI uri;
    private MusicStreamingClient musicStreamingClient;
    private String getThisSong;

    public WebSocketTestWorker(Song song, URI uri) {
        this.song = song;
        this.uri = uri;
        this.musicStreamingClient = new MusicStreamingClient(uri);
        this.getThisSong = song.getIdentifierString();
    }


    @Override
    public void run() {
//        System.out.println("Thread " + Thread.currentThread().getName() + " connecting to web socket");
        try {
            musicStreamingClient.connectBlocking();
            musicStreamingClient.send(getThisSong);
            musicStreamingClient.awaitResponse();
            musicStreamingClient.closeBlocking();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("Thread " + Thread.currentThread().getName() + " closed connection to web socket and got: " +
//                musicStreamingClient.getData().length + " bytes");
    }
}
