package cs.umu.se.client;

import com.google.protobuf.ByteString;
import cs.umu.se.chord.Hash;
import cs.umu.se.chord.Node;
import cs.umu.se.interfaces.Storage;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;
import cs.umu.se.util.MediaUtil;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import proto.Chord;
import proto.FileGrpc;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public class ClientBackend implements Storage {

    private String ip;
    private int port;
    private String saveFolderPath;
    private MediaUtil mediaUtil;
    private final int chunkSize = 2048;
    private int m;

    private Logger logger = LogManager.getLogger();

    public ClientBackend(String ip, int port, String saveFolderPath, int m) {
        this.ip = ip;
        this.port = port;
        this.saveFolderPath = saveFolderPath;
        this.mediaUtil = new MediaUtil(m);
        this.m = m;
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
//                System.out.println("Response from server: " + value.getMessage());
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
                        logger.info("onNext() value has no media info");
                        return;
                    }

                    chordMediaInfo = value.getMediaInfo();
                    System.out.println("Artist: " + chordMediaInfo.getArtist());
                    logger.info("Artist: {}", chordMediaInfo.getArtist());
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
                    future.complete("File could not be retrieved");
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
                    logger.info("File download complete!");
                    future.complete("File downloaded successfully");  // Complete the future when the server is done
                } catch (Exception e) {
                    future.complete("File could not be retrieved: " + e.getMessage());  // Complete the future when the server is done
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
    public Song[] retrieve(int nodeIdentifier) {
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

    /**
     * Lists all songs stored in the cluster.
     *
     * @param firstNodeIdentifierString an identifier string on the format: 'ip:port'. This is used to determine which
     *                                  node received this request first. When a client calls a node this should be set
     *                                  to 'firstNodeIp:firstNodePort'
     *
     * @return an array of media info objects or null if there are none.
     */
    @Override
    public MediaInfo[] listAllSongs(String firstNodeIdentifierString) {
        MediaInfo[] mediaInfos = null;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
        FileGrpc.FileBlockingStub stub = FileGrpc.newBlockingStub(channel);

        int firstNodeIdentifier = Hash.getNodeIdentifierFromString(firstNodeIdentifierString, m);
        String[] ipPort = firstNodeIdentifierString.split(":");
        String firstNodeIp = ipPort[0];
        int firstNodePort = Integer.parseInt(ipPort[1]);

        Chord.ListAllSongsRequest request = Chord.ListAllSongsRequest.newBuilder()
                .setIp(firstNodeIp)
                .setPort(firstNodePort)
                .setIdentifier(firstNodeIdentifier)
                .build();
        Chord.ListAllSongsReply reply = stub.listAllSongs(request);

        List<Chord.MediaInfo> mediaInfosList = reply.getMediaInfosList();
        mediaInfos = mediaUtil.convertGRPCChordMediaInfosToMediaInfos(mediaInfosList);

        channel.shutdown();

        return mediaInfos;
    }

    @Override
    public MediaInfo[] listNodeSongs() {
        MediaInfo[] mediaInfos = null;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
        FileGrpc.FileBlockingStub stub = FileGrpc.newBlockingStub(channel);

        Chord.ListNodeSongsRequest request = Chord.ListNodeSongsRequest.newBuilder().build();
        Chord.ListNodeSongsReply reply = stub.listNodeSongs(request);

        List<Chord.MediaInfo> mediaInfoList = reply.getMediaInfosList();
        mediaInfos = mediaUtil.convertGRPCChordMediaInfosToMediaInfos(mediaInfoList);

        channel.shutdown();

        return mediaInfos;
    }


    @Override
    public void requestTransfer(Node node) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
        FileGrpc.FileBlockingStub stub = FileGrpc.newBlockingStub(channel);

        String destIp = node.getMyIp();
        int destPort = node.getMyPort();
        int identifier = node.getMyIdentifier();

        Chord.RequestTransferRequest request = Chord.RequestTransferRequest.newBuilder()
                .setIp(destIp)
                .setPort(destPort)
                .setIdentifier(identifier)
                .build();
        Chord.RequestTransferReply reply = stub.requestTransfer(request);

        String message = reply.getMessage();
        boolean success = reply.getSuccess();

        message = "Transfer: " + success + ". Message: " + message;
        logger.info(message);

        channel.shutdown();
    }

    @Override
    public void transfer(Song song) {
        String filePath = song.getFilePath();
        CompletableFuture<String> future = new CompletableFuture<>(); // or this can be used to wait until complete

        ManagedChannel channel = ManagedChannelBuilder.forAddress(ip, port).usePlaintext().build();
        FileGrpc.FileStub stub = FileGrpc.newStub(channel);

        StreamObserver<Chord.FileChunk> requestObserver = stub.transfer(new StreamObserver<Chord.UploadStatus>() {

            @Override
            public void onNext(Chord.UploadStatus value) {
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                future.completeExceptionally(t);  // Complete future with exception in case of error

            }

            @Override
            public void onCompleted() {
                System.out.println("File upload completed.");
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

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        channel.shutdown();
    }
}
