package cs.umu.se;

import cs.umu.se.types.Song;
import cs.umu.se.util.MediaUtil;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class MusicStreamingWebSocketServer extends WebSocketServer {
    public static byte[] mp3Data;
    public static Song song;
    public static String clientMessage;

    public MusicStreamingWebSocketServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection: " + conn.getRemoteSocketAddress());
        conn.send(mp3Data);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Message from client: " + message);
        clientMessage = message;
        String[] info = clientMessage.split("-");
        System.out.println("Info: " + Arrays.toString(info));
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("Server started!");
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, InterruptedException {
        int m = 3;
        MediaUtil mediaUtil = new MediaUtil(m);
        String inputDirectoryPath = "./../../testMedia/input-music";
        File[] files = mediaUtil.getAllFilesInDirectory(inputDirectoryPath);
        Song[] songs = mediaUtil.getSongsFromFiles(files);

        song = songs[0];
        mp3Data = song.getData();
        System.out.println("Song: " + song);


        InetSocketAddress address = new InetSocketAddress("192.168.38.126", 8080);
        WebSocketServer server = new MusicStreamingWebSocketServer(address);
        server.start();
        System.out.println("WebSocket server started on port: " + address.getPort());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("Stoping server!");
                server.stop();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}

