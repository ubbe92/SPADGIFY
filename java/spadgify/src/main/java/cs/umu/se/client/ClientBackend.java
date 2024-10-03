package cs.umu.se.client;

import com.google.protobuf.ByteString;
import cs.umu.se.interfaces.Storage;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;
import cs.umu.se.util.MediaUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import proto.Chord;
import proto.FileGrpc;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class ClientBackend implements Storage {

    private String ip;
    private int port;
    private String saveFolderPath;
    private MediaUtil mediaUtil;
    private final int chunkSize = 2048;


    public ClientBackend(String ip, int port, String saveFolderPath, int m) {
        this.ip = ip;
        this.port = port;
        this.saveFolderPath = saveFolderPath;
        this.mediaUtil = new MediaUtil(m);
    }



    @Override
    public void store(Song song) {
        String filePath = song.getFilePath();
        //        CountDownLatch latch = new CountDownLatch(1); // makes client wait until transfer complete
        CompletableFuture<String> future = new CompletableFuture<>(); // or this can be used to wait until complete

        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
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

        int offset = 0;
        byte[] data = song.getData();
        Chord.MediaInfo chordMediaInfo = mediaUtil.convertMediaInfoToGRPCChordMediaInfo(song.getMediaInfo());
        while (offset < data.length) {
            int remaining = data.length - offset;
            int currentChunkSize = Math.min(chunkSize, remaining);

            ByteString chunk = ByteString.copyFrom(data, offset, currentChunkSize);
            Chord.FileChunk request = Chord.FileChunk.newBuilder()
                    .setContent(chunk)
                    .setMediaInfo(chordMediaInfo)
                    .build();
            requestObserver.onNext(request);

            offset += currentChunkSize;
        }
        requestObserver.onCompleted();


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

//        latch.await();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        channel.shutdown();
    }

    @Override
    public void store(Song[] song) {

    }

    @Override
    public Song retrieve(String identifierString) {
        CompletableFuture<String> future = new CompletableFuture<>(); // or this can be used to wait until complete
        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
        FileGrpc.FileStub stub = FileGrpc.newStub(channel);

        Chord.DownloadRequest request = Chord.DownloadRequest.newBuilder().setIdentifierString(identifierString).build();

        AtomicReference<Song> atomicSong = new AtomicReference<>();

        StreamObserver<Chord.FileChunk> streamObserver = new StreamObserver<Chord.FileChunk>() {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Chord.MediaInfo chordMediaInfo;
            int i = 0;
            Song song = null;
            String savePath = saveFolderPath + identifierString + ".mp3";


            @Override
            public void onNext(Chord.FileChunk value) {
                if (i == 0) {
                    if (!value.hasMediaInfo()) {
                        chordMediaInfo = null;
                        System.out.println("onNext() value has no media info");
                        return;
                    }

                    chordMediaInfo = value.getMediaInfo();
                    System.out.println("Artist: " + chordMediaInfo.getArtist());
                    i++;
                }

                try {
                    byteArrayOutputStream.write(value.getContent().toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                future.completeExceptionally(t);  // Complete future with exception in case of error
            }

            @Override
            public void onCompleted() {
                if (chordMediaInfo == null) {
                    atomicSong.set(null);
                    return;
                }

                try {
                    byteArrayOutputStream.close();

                    MediaInfo mediaInfo = mediaUtil.convertGRPCChordMediaInfoToMediaInfo(chordMediaInfo);
                    byte[] data = byteArrayOutputStream.toByteArray();
                    song = new Song(mediaInfo, "", data);

//                    mediaUtil.writeToFile(data, savePath);

                    song.setFilePath(savePath);
                    atomicSong.set(song);

                    System.out.println("File download complete!");
                    future.complete("File downloaded successfully");  // Complete the future when the server is done
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        };

        stub.download(request, streamObserver);

        channel.shutdown();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }

        return atomicSong.get();
    }

    @Override
    public Song[] retrieve(String[] identifierString) {
        return new Song[0];
    }

    @Override
    public void delete(String identifierString) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
        FileGrpc.FileBlockingStub stub = FileGrpc.newBlockingStub(channel);

        Chord.DeleteRequest request = Chord.DeleteRequest.newBuilder().setIdentifierString(identifierString).build();
        Chord.DeleteStatus resp = stub.delete(request);

        channel.shutdown();

        if (!resp.getSuccess())
            throw new IllegalArgumentException(resp.getMessage());
    }

    @Override
    public void delete(String[] identifierString) {

    }
}
