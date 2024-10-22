package cs.umu.se.client;

import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;
import cs.umu.se.util.BoxPlotXChart;
import cs.umu.se.util.MediaUtil;
import cs.umu.se.workers.WebSocketTaskWorker;
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
    private MediaUtil mediaUtil;
    private Client restClient = new Client(Protocol.HTTP);
    private String socketEndpoint;
    private String restEndpoint;
    private URI uri;
    private MediaInfo[] mediaInfos;
    private ClientBackend gRPCBackend;

    public MusicStreamingClientPerformanceTest(String socketIp, int socketPort, int restPort, int m,  ClientBackend gRPCBackend) throws URISyntaxException, IOException, ParseException {
        this.mediaUtil = new MediaUtil(m);
        this.socketEndpoint = "ws://" + socketIp + ":" + socketPort;
        this.uri = new URI(socketEndpoint);
        this.restEndpoint = String.format("http://%s:%s/API/mediaInfo", socketIp, restPort);
        this.gRPCBackend = gRPCBackend;

        Response response = mediaUtil.makeRestletRequestWithoutBody(restEndpoint, Method.GET, restClient);
        Representation representation = response.getEntity();
        JSONArray jsonArray = mediaUtil.convertRepresentationToJsonArray(representation);
        this.mediaInfos = mediaUtil.convertJSONArrayToMediaInfos(jsonArray);
    }

    public void makeBoxPlotTotalTime(String title, int nrBoxes, int nrTasks, int iterations, Song[] songs) {
        BoxPlotXChart boxPlot = new BoxPlotXChart();
        boxPlot.createPlot(title, MusicStreamingClient.class.getName(), "Time (ms)");

        // store songs in the cluster
        for (Song s : songs)
            gRPCBackend.store(s);

        for (int i = 0; i < nrBoxes; i++) {
            int nrThreads = (int) Math.pow(2, i);
            boxPlot.addToPlot(nrThreads + " client(s)", testSongWebSocketStreamingNTimesTotalTime(songs, iterations, nrTasks, nrThreads));
        }

        // remove songs from the cluster
        for (Song s : songs)
            gRPCBackend.delete(s.getIdentifierString());

        boxPlot.showPlot();
    }

    public void makeBoxPlotPerClientTime(String title, int nrBoxes, int nrTasks, int iterations, boolean getAll, Song[] songs) {
        BoxPlotXChart boxPlot = new BoxPlotXChart();
        boxPlot.createPlot(title, MusicStreamingClient.class.getName(), "Time (ms)");

        // store songs in the cluster
        for (Song s : songs)
            gRPCBackend.store(s);

        for (int i = 0; i < nrBoxes; i++) {
            int nrThreads = (int) Math.pow(2, i);
            boxPlot.addToPlot(nrThreads + " client(s)", testSongStreamingITimesPerClientTime(songs, iterations, nrTasks, getAll, nrThreads));
        }

        // remove songs from the cluster
        for (Song s : songs)
            gRPCBackend.delete(s.getIdentifierString());

        boxPlot.showPlot();
    }

    private List<Long> testSongWebSocketStreamingNTimesTotalTime(Song[] songs, int iterations, int nrTasks, int nrThreads) {
        boolean getAll = true;

        List<Long> results = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {

            // Create worker threads
            WebSocketTaskWorker[] taskWorkers = new WebSocketTaskWorker[nrTasks];
            for (int k = 0; k < nrTasks; k++)
                taskWorkers[k] = new WebSocketTaskWorker(songs[k], uri, getAll);

            long res = testSongWebSocketStreamingTotalTime(taskWorkers, nrThreads);
            if (res != -1)
                results.add(res);

            System.out.println("testSongWebSocketStreamingNTimesTotalTime() iteration: " + i + " done, nrTasks: " + nrTasks + ", nrThreads: " + nrThreads);
        }
        return results;
    }

    private long testSongWebSocketStreamingTotalTime(WebSocketTaskWorker[] taskWorkers, int nrThreads) {
        // Create a new thread pool
        this.executor = Executors.newFixedThreadPool(nrThreads);

        // perform test
        long t1 = System.currentTimeMillis();
        boolean res = executeWorkers(taskWorkers);
        long t2 = System.currentTimeMillis();

        if (res)
            return t2 - t1;
        else
            return -1;
    }

    private List<Long> testSongStreamingITimesPerClientTime(Song[] songs, int iterations, int nrTasks, boolean getAll, int nrThreads) {
        List<Long> results = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {

            // Create worker threads
            WebSocketTaskWorker[] testWorkers = new WebSocketTaskWorker[nrTasks];
            for (int k = 0; k < nrTasks; k++)
                testWorkers[k] = new WebSocketTaskWorker(songs[k], uri, getAll);

            // Create a new thread pool
            this.executor = Executors.newFixedThreadPool(nrThreads);

            boolean res = executeWorkers(testWorkers);

            // save all client times for his iteration
            for (WebSocketTaskWorker testWorker : testWorkers)
                results.add(testWorker.getTime());

            System.out.println("testSongStreamingITimesPerClientTime() iteration: " + i + " done, nrTasks: " + nrTasks + ", nrThreads: " + nrThreads);
        }

        return results;
    }

    private boolean executeWorkers(WebSocketTaskWorker[] taskWorkers) {
        // Send the workers to the thread pool
        for (WebSocketTaskWorker taskWorker : taskWorkers)
            executor.execute(taskWorker);

        // Shutdown the thread pool
        executor.shutdown();

        // Wait for the thread pool to shut down
        try {
            long timeout = 120000;
            return executor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
