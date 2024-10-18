package cs.umu.se;

import cs.umu.se.client.*;
import cs.umu.se.interfaces.Storage;
import cs.umu.se.types.Song;
import cs.umu.se.util.ClientGetOP;
import cs.umu.se.util.MediaUtil;
import picocli.CommandLine;

import java.io.File;
import java.net.URI;

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
        int exitCode = commandLine.execute(args);

        if (exitCode != 0) {
            System.out.println("GetOP exit code: " + exitCode);
            System.exit(exitCode);
        }

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
        boolean isUpload = clientGetOP.isUpload();
        String pathToMusic = clientGetOP.getPathToMusic();
        String socketIp = clientGetOP.getSocketIp();
        int socketPort = clientGetOP.getSocketPort();
        int restPort = clientGetOP.getRestPort();

        MediaUtil mediaUtil = new MediaUtil(m);

        System.out.println("node ip: '" + nodeIp + "'");
        System.out.println("node port: '" + nodePort + "'");
        System.out.println("socket ip: '" + socketIp + "'");
        System.out.println("socket port: '" + socketPort + "'");
        System.out.println("rest port: '" + restPort + "'");


        // Upload music to the node
        // ------------------------------------------------
        if (isUpload) {
            System.out.println("Is upload: " + isUpload + ", path: " + pathToMusic);
            String outputFolderPath = "./../../testMedia/output-music/"; // if we want to save retrieved songs to disc later
            ClientBackend backend = new ClientBackend(nodeIp, nodePort, outputFolderPath, m);

            // Open all files in the input directory, parse them and create song objects
            // e.g. pathToMusic = "./../../testMedia/input-music"
            File[] files = mediaUtil.getAllFilesInDirectory(pathToMusic);
            Song[] songs = mediaUtil.getSongsFromFiles(files);

            for (Song song : songs)
                System.out.println("hash: " + song.getMediaInfo().getHash() +
                        "\tduration: " + song.getMediaInfo().getDuration() +
                        "\tsize: " + song.getMediaInfo().getSize() +
                        "\tSong: " + song);

            for (Song song : songs) {
                backend.store(song);
            }


            // Only for testing, remove later
            MusicStreamingClientLogicTest test = new MusicStreamingClientLogicTest(socketIp, socketPort, restPort, m);

            // check logic
            test.testRESTListAllSongs();
            test.testStreamingData();


            // Testing how to perform performance test below, remove later
            String socketEndpoint = "ws://" + socketIp + ":" + socketPort;
            URI uri = new URI(socketEndpoint);
            int nrClients = 4;
            MusicStreamingClient[] musicStreamingClients = new MusicStreamingClient[nrClients];

            for (int i = 0; i < nrClients; i++) {
                musicStreamingClients[i] = new MusicStreamingClient(uri);
            }

            for (MusicStreamingClient musicStreamingClient : musicStreamingClients)
                musicStreamingClient.connectBlocking();


            String getThisSong = "ethereal vistas-Mikael Jäcksson-In the bodega";
            for (MusicStreamingClient musicStreamingClient : musicStreamingClients)
                musicStreamingClient.send(getThisSong);

            Thread.sleep(3000);
            for (MusicStreamingClient musicStreamingClient : musicStreamingClients)
                musicStreamingClient.closeBlocking();

            int nrThreads = 5;
            MusicStreamingClientPerformanceTest t = new MusicStreamingClientPerformanceTest(socketIp, socketPort, restPort, m, nrThreads);
        }


        // test gRPC backend
        // ------------------------------------------------
        Storage storage = new ClientBackend(nodeIp, nodePort, saveFolderPath, m);

        // logic tests
        if (isLogic) {
            ClientLogicTest test = new ClientLogicTest(storage, m, nodeIp, nodePort);

            // check logic
            test.testListNodeSong();
            test.testListAllSongs();
            test.testStoreAndDelete();
            test.testStoreDuplicate();
            System.out.println("Logic tests done!");
        }

        // performance tests
        if (isPerformance) {
            ClientPerformanceTest test = new ClientPerformanceTest(storage, m, nodeIp, nodePort);

            // do tests
            int iterations = 10;
            int nrSongs = 5;
            int nrBoxes = 2;
            long songSize = 10810096;

            // Test increasing amount of messages - seq without cache
            String title = "Non caching retrieve of songs with fixed payload size of 10 Mb repeated " + iterations + " times.";
            test.makeBoxPlotSeqIncSongsNoCaching(title, nrBoxes, nrSongs, iterations, songSize);

            // Test increasing amount of messages - seq with cache
            title = "Caching retrieve of songs with fixed payload size of 10 Mb repeated " + iterations + " times.";
            test.makeBoxPlotSeqIncSonsWithCache(title, nrBoxes, nrSongs, iterations, songSize);

            System.out.println("Performance tests done!");
        }

        // test web socket backend here (need to be implement methods and create a thread pool)!!!!!!
        // ------------------------------------------------
        // logic tests
        if (isLogic) {
            MusicStreamingClientLogicTest test = new MusicStreamingClientLogicTest(socketIp, socketPort, restPort, m);

            // check logic
            test.testRESTListAllSongs();
            test.testStreamingData();

            System.out.println("Web socket logic tests done!");
        }

        // performance tests
        if (isPerformance) {
            int nrThreads = 5;
            MusicStreamingClientPerformanceTest test = new MusicStreamingClientPerformanceTest(socketIp, socketPort, restPort, m, nrThreads);

            // do tests

            // plot results

            System.out.println("Web socket performance tests done!");
        }








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

        System.out.println("Test client done!");
    }
}
