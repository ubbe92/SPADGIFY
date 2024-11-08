package cs.umu.se.music;

import cs.umu.se.chord.Node;
import cs.umu.se.client.ClientBackend;
import cs.umu.se.types.Song;
import org.apache.logging.log4j.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicStreamingServer extends WebSocketServer {
    private int m;
    private String ip = "";
    private int chordServerPort = -1;
    private int musicServerPort = -1;
    public byte[] mp3Data = new byte[0];
    private ClientBackend clientBackend;
    private Logger logger;
    private Node node;
    private final int maxPendingConnections = 320;
    private boolean getAll = true;
    private final double fivePercent = 0.05;

    public MusicStreamingServer(InetSocketAddress address, Node node, Logger logger) {
        super(address);
        setMaxPendingConnections(maxPendingConnections);

        this.node = node;
        this.m = node.getM();
        this.chordServerPort = node.getMyPort();
        this.logger = logger;
        logger.info("Music streaming sever has maximum {} connections", maxPendingConnections);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String msg = "New connection: " + conn.getRemoteSocketAddress();
        logger.info(msg);

        synchronized (this) {
            if (handshake.hasFieldValue("getAll"))
                getAll = Boolean.parseBoolean(handshake.getFieldValue("getAll"));
        }
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
            if (getAll)
                conn.send(mp3Data);
            else {
                int fivePercentLength = (int) (mp3Data.length * fivePercent);
                byte[] mp3DataFivePercent = new byte[fivePercentLength];
                System.arraycopy(mp3Data, 0, mp3DataFivePercent, 0, fivePercentLength);
                conn.send(mp3DataFivePercent);
            }
        } else
            conn.send(new byte[0]); // to indicate that a song does not exist

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
        clientBackend = new ClientBackend(ip, chordServerPort, "", m);
    }
}
