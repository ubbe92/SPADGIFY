package cs.umu.se;

import com.google.protobuf.ByteString;
import cs.umu.se.client.ClientBackend;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;
import cs.umu.se.util.MediaUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.checkerframework.checker.units.qual.C;
import proto.Chord;
import proto.FileGrpc;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class TestClient {
    private static int m = 3;
    private static int chunkSize = 2048;
    private static MediaUtil mediaUtil = new MediaUtil(m);

    public static void main(String[] args) throws Exception {
        System.out.println("Test client running...");

        String ip = "192.168.38.126";
        int port = 8185;
        String inputFilePath = "./../../testMedia/input-music/freeDemoSong.mp3";
        String outputFolderPath = "./../../testMedia/output-music/";
        ClientBackend backend = new ClientBackend(ip, port, outputFolderPath, m);

        byte[] bytes = mediaUtil.readFromFile(inputFilePath);
        MediaInfo mediaInfo = new MediaInfo("Anton Dacklin Gaied", "Mot graven vi går!",
                "Datas album", 110, "Chill music", 3535934, m);

        Song song = new Song(mediaInfo, "", bytes);

        System.out.println("Hash of mediaInfo: " + mediaInfo.getHash());

        backend.store(song);
        System.out.println("Sent song: " + song);
        Thread.sleep(2000);

        Song songRet = backend.retrieve(song.getIdentifierString());
        if (songRet != null)
            mediaUtil.writeToFile(songRet.getData(), songRet.getFilePath());

        System.out.println("Got song: " + songRet);
        Thread.sleep(2000);

        if (songRet != null)
            backend.delete(songRet.getIdentifierString());

        System.out.println("Done");
    }

//    private static void store(String ip, int port, String filePath) throws ExecutionException, InterruptedException {
////        CountDownLatch latch = new CountDownLatch(1); // makes client wait until transfer complete
//        CompletableFuture<String> future = new CompletableFuture<>(); // or this can be used to wait until complete
//
//        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
//        FileGrpc.FileStub stub = FileGrpc.newStub(channel);
//
//        StreamObserver<Chord.FileChunk> requestObserver = stub.upload(new StreamObserver<Chord.UploadStatus>() {
//            @Override
//            public void onNext(Chord.UploadStatus value) {
//                System.out.println("Response from server: " + value.getMessage());
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                t.printStackTrace();
////                latch.countDown();
//                future.completeExceptionally(t);  // Complete future with exception in case of error
//
//            }
//
//            @Override
//            public void onCompleted() {
//                System.out.println("File upload completed.");
////                latch.countDown();
//                future.complete("File uploaded successfully");  // Complete the future when the server is done
//            }
//        });
//
//        Chord.MediaInfo mediaInfo = Chord.MediaInfo.newBuilder()
//                .setArtist("Anton Dacklin Gaied")
//                .setSong("Mot graven vi går!")
//                .setAlbum("Datas album")
//                .setDuration(110)
//                .setGenre("Chill musik!")
//                .setSize(3535934)
//                .build();
//
//        try (FileInputStream inputStream = new FileInputStream(filePath)) {
//            byte[] buffer = new byte[chunkSize];
//            int bytesRead;
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                Chord.FileChunk chunk = Chord.FileChunk.newBuilder()
//                        .setContent(ByteString.copyFrom(buffer, 0, bytesRead))
//                        .setMediaInfo(mediaInfo)
//                        .build();
//                requestObserver.onNext(chunk);
//            }
//            requestObserver.onCompleted();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        latch.await();
//        future.get();
//        channel.shutdown();
//    }

//    private static Song retrieve(String ip, int port, Chord.MediaInfo mediaInfo) throws ExecutionException, InterruptedException {
//        CompletableFuture<String> future = new CompletableFuture<>(); // or this can be used to wait until complete
//        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
//        FileGrpc.FileStub stub = FileGrpc.newStub(channel);
//
//        Chord.DownloadRequest request = Chord.DownloadRequest.newBuilder().setMediaInfo(mediaInfo).build();
//
//        AtomicReference<Song> atomicSong = new AtomicReference<>();
//
//        StreamObserver<Chord.FileChunk> streamObserver = new StreamObserver<Chord.FileChunk>() {
////            FileOutputStream fos;
////
////            {
////                try {
////                    fos = new FileOutputStream("/Users/antondacklin/Downloads/testMedia/retrieveFile.mp3");
////                } catch (FileNotFoundException e) {
////                    throw new RuntimeException(e);
////                }
////            }
//
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            Chord.MediaInfo chordMediaInfo;
//            int i = 0;
//            Song song = null;
//            String savePath = "/Users/antondacklin/Downloads/testMedia/retrieveFile.mp3";
//
//            @Override
//            public void onNext(Chord.FileChunk value) {
////                try {
////                    fos.write(value.getContent().toByteArray());
////                } catch (IOException e) {
////                    throw new RuntimeException(e);
////                }
//
//                if (i == 0) {
//                    chordMediaInfo = value.getMediaInfo();
//                    System.out.println("Artist: " + chordMediaInfo.getArtist());
//                    i++;
//                }
//
//                try {
//                    byteArrayOutputStream.write(value.getContent().toByteArray());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                t.printStackTrace();
//                future.completeExceptionally(t);  // Complete future with exception in case of error
//            }
//
//            @Override
//            public void onCompleted() {
//                try {
////                    fos.close();
//
//                    byteArrayOutputStream.close();
//
//                    MediaInfo mediaInfo = mediaUtil.convertGRPCChordMediaInfoToMediaInfo(chordMediaInfo);
//                    byte[] data = byteArrayOutputStream.toByteArray();
//                    song = new Song(mediaInfo, "", data);
//                    mediaUtil.writeToFile(data, savePath);
//                    atomicSong.set(song);
//
//                    System.out.println("File download complete!");
//                    future.complete("File uploaded successfully");  // Complete the future when the server is done
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//        };
//
//        stub.download(request, streamObserver);
//
//        channel.shutdown();
//        future.get();
//
//        return atomicSong.get();
//        return null;
//    }
}
