package cs.umu.se;

import cs.umu.se.client.*;
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

    // example on commandline arguments for this program: -l -p 192.168.38.126 8185 3 192.168.38.126 8080 8000
    public static void main(String[] args) throws Exception {
        System.out.println("Test client running...");
        ClientGetOP clientGetOP = new ClientGetOP();
        CommandLine commandLine = new CommandLine(clientGetOP);
        commandLine.execute(args);

        boolean isVersion = commandLine.isVersionHelpRequested();
        boolean isUsage = commandLine.isUsageHelpRequested();
        if (isVersion || isUsage)
            System.exit(0);

        String nodeIp = clientGetOP.getNodeIp();
        int nodePort = clientGetOP.getNodePort();
        String saveFolderPath = "";
        int m = clientGetOP.getM();
        boolean isLogic = clientGetOP.isLogic();
        boolean isPerformance = clientGetOP.isPerformance();
        String socketIp = clientGetOP.getSocketIp();
        int socketPort = clientGetOP.getSocketPort();
        int restPort = clientGetOP.getRestPort();

        MediaUtil mediaUtil = new MediaUtil(m);

        System.out.println("node ip: '" + nodeIp + "'");
        System.out.println("node port: '" + nodePort + "'");
        System.out.println("socket ip: '" + socketIp + "'");
        System.out.println("socket port: '" + socketPort + "'");
        System.out.println("rest port: '" + restPort + "'");

        // test gRPC backend
        Storage storage = new ClientBackend(nodeIp, nodePort, saveFolderPath, m);

        // logic tests
        if (isLogic) {
            ClientLogicTest test = new ClientLogicTest(storage, m, nodeIp, nodePort);
            // check logic
            test.testListNodeSong();
            test.testListAllSongs();
            test.testStoreAndDelete();
            test.testStoreDuplicate();
        }

        // performance tests
        if (isPerformance) {
            ClientPerformanceTest test = new ClientPerformanceTest(storage, m, nodeIp, nodePort);

            // do tests

            // plot results
        }

        // test web socket backend here (need to be implement methods and create a thread pool)!!!!!!
        MediaBackend mediaBackend = new MediaBackend(socketIp, socketPort, restPort, m);

        // logic tests
        if (isLogic) {
            ClientMediaLogicTest test = new ClientMediaLogicTest(mediaBackend);

            // check logic

        }

        // performance tests
        if (isPerformance) {
            ClientMediaPerformanceTest test = new ClientMediaPerformanceTest(mediaBackend);

            // do tests

            // plot results
        }








//        String inputFilePath = "./../../testMedia/input-music/freeDemoSong.mp3";
//        String outputFolderPath = "./../../testMedia/output-music/";
//        ClientBackend backend = new ClientBackend(nodeIp, nodePort, outputFolderPath, m);
//
//
//        // Open all files in the input directory, parse them and create song objects
//        String inputDirectoryPath = "./../../testMedia/input-music";
//        File[] files = mediaUtil.getAllFilesInDirectory(inputDirectoryPath);
//        Song[] songs = mediaUtil.getSongsFromFiles(files);
//
//        for (Song song : songs)
//            System.out.println("hash: " + song.getMediaInfo().getHash() +
//                    "\tduration: " + song.getMediaInfo().getDuration() +
//                    "\tsize: " + song.getMediaInfo().getSize() +
//                    "\tSong: " + song);
//
//        for (Song song : songs) {
//            backend.store(song);
//        }
//
//        Thread.sleep(500);
//
//        String firstNodeIdentifierString = nodeIp + ":" + nodePort;
//        MediaInfo[] mediaInfos = backend.listAllSongs(firstNodeIdentifierString);
//
//        for (MediaInfo mediaInfo : mediaInfos)
//            System.out.println("Media info: " + mediaInfo);








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
