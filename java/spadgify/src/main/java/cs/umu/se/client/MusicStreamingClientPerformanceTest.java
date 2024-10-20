package cs.umu.se.client;

import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;
import cs.umu.se.util.BoxPlotXChart;
import cs.umu.se.util.MediaUtil;
import cs.umu.se.workers.WebSocketTestWorker;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.restlet.Client;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MusicStreamingClientPerformanceTest {
    private ExecutorService executor;
    private int nrThreads;
    private MediaUtil mediaUtil;
    private Client restClient = new Client(Protocol.HTTP);
    private String socketEndpoint;
    private String restEndpoint;
    private URI uri;
    private MediaInfo[] mediaInfos;
    private ClientBackend gRPCBackend;

    public MusicStreamingClientPerformanceTest(String socketIp, int socketPort, int restPort, int m, int nrThreads, ClientBackend gRPCBackend) throws URISyntaxException, IOException, ParseException {
        this.mediaUtil = new MediaUtil(m);
        this.socketEndpoint = "ws://" + socketIp + ":" + socketPort;
        this.uri = new URI(socketEndpoint);
        this.restEndpoint = String.format("http://%s:%s/API/mediaInfo", socketIp, restPort);
        this.nrThreads = nrThreads;
        this.gRPCBackend = gRPCBackend;

        Response response = mediaUtil.makeRestletRequestWithoutBody(restEndpoint, Method.GET, restClient);
        Representation representation = response.getEntity();
        JSONArray jsonArray = mediaUtil.convertRepresentationToJsonArray(representation);
        this.mediaInfos = mediaUtil.convertJSONArrayToMediaInfos(jsonArray);
    }

    public void makeBoxPlotIncClientsNoCachingTotalTime(String title, int nrBoxes, int nrClients, int iterations, long songSize) {
        BoxPlotXChart boxPlot = new BoxPlotXChart();
        boxPlot.createPlot(title, MusicStreamingClient.class.getName(), "Time (ms)");

        int factor = 1;
        for (int i = 0; i < nrBoxes - 1; i++)
            factor = factor * 2;

        // create one unique song for each client
        Song[] songs = mediaUtil.createDummySongs(nrClients * factor, songSize);

        // store songs in the cluster
        for (Song s : songs)
            gRPCBackend.store(s);

        int j = 1;
        for (int i = 0; i < nrBoxes; i++) {
            boxPlot.addToPlot(nrClients * j     + " clients", testSongWebSocketStreamingNTimesTotalTime(songs, iterations, nrClients * j));
            j = j * 2;
        }

        // remove songs from the cluster
        for (Song s : songs)
            gRPCBackend.delete(s.getIdentifierString());

        boxPlot.showPlot();
    }

    public void makeBoxPlotIncClientsNoCachingPerClientTime(String title, int nrBoxes, int nrClients, int iterations, long songSize) {
        BoxPlotXChart boxPlot = new BoxPlotXChart();
        boxPlot.createPlot(title, MusicStreamingClient.class.getName(), "Time (ms)");

        int factor = 1;
        for (int i = 0; i < nrBoxes - 1; i++)
            factor = factor * 2;

        // create one unique song for each client
        Song[] songs = mediaUtil.createDummySongs(nrClients * factor, songSize);

        // store songs in the cluster
        for (Song s : songs)
            gRPCBackend.store(s);

        int j = 1;
        for (int i = 0; i < nrBoxes; i++) {
            boxPlot.addToPlot(nrClients * j     + " clients", testSongWebSocketStreamingNTimesPerClientTime(songs, iterations, nrClients * j));
            j = j * 2;
        }

        // remove songs from the cluster
        for (Song s : songs)
            gRPCBackend.delete(s.getIdentifierString());

        boxPlot.showPlot();
    }

    public void makeBoxPlotIncClientsWithCachingTotalTime() {

    }

    private List<Long> testSongWebSocketStreamingNTimesTotalTime(Song[] songs, int iterations, int nrClients) {
        List<Long> results = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {

            // Create worker threads
            WebSocketTestWorker[] testWorkers = new WebSocketTestWorker[nrClients];
            for (int k = 0; k < nrClients; k++)
                testWorkers[k] = new WebSocketTestWorker(songs[k], uri);

            results.add(testSongWebSocketStreamingTotalTime(songs, testWorkers));
            System.out.println("testSongWebSocketStreamingNTimesTotalTime() iteration: " + i + " done nrClients: " + nrClients);
        }
        return results;
    }

    private long testSongWebSocketStreamingTotalTime(Song[] songs, WebSocketTestWorker[] testWorkers) {
        // Create a new thread pool
        this.executor = Executors.newFixedThreadPool(nrThreads);

        // perform test
        long t1 = System.currentTimeMillis();
        executeWorkers(testWorkers);
        long t2 = System.currentTimeMillis();

        return t2 - t1;
    }

    private List<Long> testSongWebSocketStreamingNTimesPerClientTime(Song[] songs, int iterations, int nrClients) {
        List<Long> results = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {

            // Create worker threads
            WebSocketTestWorker[] testWorkers = new WebSocketTestWorker[nrClients];
            for (int k = 0; k < nrClients; k++)
                testWorkers[k] = new WebSocketTestWorker(songs[k], uri);

            // Create a new thread pool
            this.executor = Executors.newFixedThreadPool(nrThreads);

            executeWorkers(testWorkers);

            // save all client times for his iteration
            for (WebSocketTestWorker testWorker : testWorkers)
                results.add(testWorker.getTime());

            System.out.println("testSongWebSocketStreamingNTimesPerClientTime() iteration: " + i + " done nrClients: " + nrClients);
        }

        return results;
    }

    private void executeWorkers(WebSocketTestWorker[] testWorkers) {
        // Send the workers to the thread pool
        for (WebSocketTestWorker testWorker : testWorkers)
            executor.execute(testWorker);

        // Shutdown the thread pool
        executor.shutdown();

        // Wait for the thread pool to shut down
        try {
            executor.awaitTermination(60000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
