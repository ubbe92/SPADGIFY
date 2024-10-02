package cs.umu.se;

import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.checkerframework.checker.units.qual.C;
import proto.Chord;
import proto.FileGrpc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class TestClient {

    public static <InputStream> void main(String[] args) throws InterruptedException {
        System.out.println("Test client running...");

        String ip = "192.168.38.126";
        int port = 8185;
        String pathToFile = "/Users/antondacklin/Downloads/testMedia/input-music/freeDemoSong.mp3";

        CountDownLatch latch = new CountDownLatch(1);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port)
                .usePlaintext()
                .build();

        FileGrpc.FileStub stub = FileGrpc.newStub(channel);

        StreamObserver<Chord.FileChunk> requestObserver = stub.upload(new StreamObserver<Chord.UploadStatus>() {
            @Override
            public void onNext(Chord.UploadStatus value) {
                System.out.println("Response from server: " + value.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("File upload completed.");
                latch.countDown();
            }
        });
        Chord.MediaInfo mediaInfo = Chord.MediaInfo.newBuilder()
                .setArtist("Sinthu")
                .setSong("Lalala land")
                .setAlbum("Datas album")
                .setDuration(110)
                .setGenre("Chill musik!")
                .setSize(3535934)
                .build();
        try (FileInputStream inputStream = new FileInputStream(pathToFile)) {
            byte[] buffer = new byte[2048];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {

                Chord.FileChunk chunk = Chord.FileChunk.newBuilder()
                        .setContent(ByteString.copyFrom(buffer, 0, bytesRead))
                        .setMediaInfo(mediaInfo)
                        .build();
                requestObserver.onNext(chunk);
            }

            requestObserver.onCompleted();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Thread.sleep(3000);
        latch.await();
        System.out.println("Done");
    }
}
