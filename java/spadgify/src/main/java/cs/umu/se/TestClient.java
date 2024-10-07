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
//        String ip = "192.168.1.50";
        int port = 8185;
        String inputFilePath = "./../../testMedia/input-music/freeDemoSong.mp3";
        String outputFolderPath = "./../../testMedia/output-music/";
        ClientBackend backend = new ClientBackend(ip, port, outputFolderPath, m);

        byte[] bytes = mediaUtil.readFromFile(inputFilePath);
//        MediaInfo mediaInfo = new MediaInfo("Anton Dacklin Gaied", "Mot graven vi går!",
//                "Datas album", 110, "Chill music", 3535934, m); // id 3

        MediaInfo mediaInfo = new MediaInfo("DJ Dick", "Mot graven vi går!",
                "Datas album", 110, "Chill music", 3535934, m); // id 4

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
}
