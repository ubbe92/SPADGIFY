package cs.umu.se.music;

import cs.umu.se.client.ClientBackend;
import cs.umu.se.types.Song;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class MusicStreamingServer extends WebSocketServer {
    private int m;
    private String ip = "";
    private int chordServerPort = -1;
    private int musicServerPort = -1;
    public byte[] mp3Data = new byte[0];
    private ClientBackend clientBackend;
    private Logger logger;


    public MusicStreamingServer(InetSocketAddress address) {
        super(address);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String msg = "New connection: " + conn.getRemoteSocketAddress();
        logger.info(msg);
        System.out.println("Websocket thread name: " + Thread.currentThread().getName());
//        conn.send(mp3Data);
//        mp3Data = new byte[0];
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        String msg = "Music server closed connection: " + conn.getRemoteSocketAddress();
        logger.info(msg);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        String msg = "Message from client: " + message;
        logger.info(msg);
        Song song = clientBackend.retrieve(message);
        logger.info("Song retrieved: {}", song);

        if (song != null) {
            mp3Data = song.getData();
            conn.send(mp3Data);
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        this.ip = this.getAddress().getHostName();
        this.musicServerPort = this.getPort();
        String message = "Music server started at: " + ip + ":" + musicServerPort;
        logger.info(message);
//        System.out.println(message);
        clientBackend = new ClientBackend(ip, chordServerPort, "", m);
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setChordServerPort(int chordServerPort) {
        this.chordServerPort = chordServerPort;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}