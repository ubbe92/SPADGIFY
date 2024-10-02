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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class TestClient {

    public static <InputStream> void main(String[] args) throws InterruptedException, ExecutionException {
        System.out.println("Test client running...");

        String ip = "192.168.38.126";
        int port = 8187;
        String pathToFile = "/Users/antondacklin/Downloads/testMedia/input-music/freeDemoSong.mp3";

//        CountDownLatch latch = new CountDownLatch(1); // makes client wait until transfer complete
        CompletableFuture<String> future = new CompletableFuture<>(); // or this can be used to wait until complete


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
//                latch.countDown();
                future.completeExceptionally(t);  // Complete future with exception in case of error

            }

            @Override
            public void onCompleted() {
                System.out.println("File upload completed.");
//                latch.countDown();
                future.complete("File uploaded successfully");  // Complete the future when the server is done

            }
        });

        Chord.MediaInfo mediaInfo = Chord.MediaInfo.newBuilder()
                .setArtist("Anton Dacklin Gaied")
                .setSong("Mot graven vi går!")
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

//        latch.await();
        future.get();
        System.out.println("Done");
    }
}
