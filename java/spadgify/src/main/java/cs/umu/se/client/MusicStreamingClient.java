package cs.umu.se.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class MusicStreamingClient extends WebSocketClient {
    private final CountDownLatch latch = new CountDownLatch(1);
    private byte[] data;

    public MusicStreamingClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Client onOpen");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Client received string message");
        byte[] bytes = message.getBytes();
        latch.countDown();
    }

    // Handle binary messages
    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("Client received binary message");
        byte[] bytes = new byte[message.remaining()];
        message.get(bytes);  // Read the bytes from ByteBuffer

        if (bytes.length > 0)
            this.data = bytes;
        else
            this.data = null;

        // Handle or process the binary data (e.g., save or play audio)
        latch.countDown();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Client onClose");

    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Client onError");
    }

    public void awaitResponse() throws InterruptedException {
        System.out.println("Client awaiting response...");
        latch.await();
    }

    public byte[] getData() {
        return data;
    }
}
