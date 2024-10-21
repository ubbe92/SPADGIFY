package cs.umu.se.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class MusicStreamingClient extends WebSocketClient {
    private CountDownLatch latch = new CountDownLatch(1);
    private byte[] data;

    public MusicStreamingClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
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
        byte[] bytes = new byte[message.remaining()];
        message.get(bytes);  // Read the bytes from ByteBuffer
        // Handle or process the binary data (e.g., save or play audio)
        synchronized (this) {
            if (bytes.length > 0)
                this.data = bytes;
            else
                this.data = null;
        }

        latch.countDown();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Client onError: " + ex.getMessage());
    }

    public void awaitResponse() throws InterruptedException {
        latch.await();
        latch = new CountDownLatch(1);
    }

    public byte[] getData() {
        synchronized (this) {
            return data;
        }
    }
}
