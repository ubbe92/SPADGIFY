package cs.umu.se;

import cs.umu.se.client.ClientBackend;
import cs.umu.se.client.ClientLogicTest;
import cs.umu.se.client.ClientPerformanceTest;
import cs.umu.se.interfaces.Storage;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;
import cs.umu.se.util.ClientGetOP;
import cs.umu.se.util.MediaUtil;
import picocli.CommandLine;

import java.io.*;

/**
 * The TestClient class is responsible for initializing and running a test client
 * for media management. It interacts with a backend system to store, list, and
 * retrieve songs.
 */
public class TestClient {

    // example on commandline arguments for this program: -l -p 192.168.38.126 8185 3
    public static void main(String[] args) throws Exception {
        System.out.println("Test client running...");
        ClientGetOP clientGetOP = new ClientGetOP();
        CommandLine commandLine = new CommandLine(clientGetOP);
        commandLine.execute(args);

        boolean isVersion = commandLine.isVersionHelpRequested();
        boolean isUsage = commandLine.isUsageHelpRequested();
        if (isVersion || isUsage)
            System.exit(0);

        String ip = clientGetOP.getIp();
        int port = clientGetOP.getPort();
        String saveFolderPath = "";
        int m = clientGetOP.getM();
        boolean isLogic = clientGetOP.isLogic();
        boolean isPerformance = clientGetOP.isPerformance();
        MediaUtil mediaUtil = new MediaUtil(m);

        System.out.println("ip: '" + ip + "'");
        System.out.println("port: '" + port + "'");

        Storage storage = new ClientBackend(ip, port, saveFolderPath, m);

        // logic tests
        if (isLogic) {
            ClientLogicTest test = new ClientLogicTest(storage);
        }

        // performance tests
        if (isPerformance) {
            ClientPerformanceTest test = new ClientPerformanceTest(storage);
        }
        
        String inputFilePath = "./../../testMedia/input-music/freeDemoSong.mp3";
        String outputFolderPath = "./../../testMedia/output-music/";
        ClientBackend backend = new ClientBackend(ip, port, outputFolderPath, m);


        // Open all files in the input directory, parse them and create song objects
        String inputDirectoryPath = "./../../testMedia/input-music";
        File[] files = mediaUtil.getAllFilesInDirectory(inputDirectoryPath);
        Song[] songs = mediaUtil.getSongsFromFiles(files);

        for (Song song : songs)
            System.out.println("hash: " + song.getMediaInfo().getHash() +
                    "\tduration: " + song.getMediaInfo().getDuration() +
                    "\tsize: " + song.getMediaInfo().getSize() +
                    "\tSong: " + song);

        for (Song song : songs) {
            backend.store(song);
        }

        Thread.sleep(500);

        String firstNodeIdentifierString = ip + ":" + port;
        MediaInfo[] mediaInfos = backend.listAllSongs(firstNodeIdentifierString);

        for (MediaInfo mediaInfo : mediaInfos)
            System.out.println("Media info: " + mediaInfo);

//        byte[] bytes = mediaUtil.readFromFile(inputFilePath);
////        MediaInfo mediaInfo = new MediaInfo("Anton Dacklin Gaied", "Mot graven vi går!",
////                "Datas album", 110, "Chill music", 3535934, m); // id 3
//
//        MediaInfo mediaInfo = new MediaInfo("DJ Dick", "Mot graven vi går!",
//                "Datas album", 110, "Chill music", 3535934, m); // id 4
//
//        Song song = new Song(mediaInfo, "", bytes);
//
//        System.out.println("Hash of mediaInfo: " + mediaInfo.getHash());
//
//
//        backend.store(song);
//        System.out.println("Sent song: " + song);
//        Thread.sleep(2000);
//
//        Song songRet = backend.retrieve(song.getIdentifierString());
//        if (songRet != null)
//            mediaUtil.writeToFile(songRet.getData(), songRet.getFilePath());
//
//        System.out.println("Got song: " + songRet);
//        Thread.sleep(2000);
//
//        if (songRet != null)
//            backend.delete(songRet.getIdentifierString());

        System.out.println("Done");
    }
}
