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

    public MusicStreamingClientPerformanceTest(String socketIp, int socketPort, int restPort, int m, int nrThreads, ClientBackend gRPCBackend) throws URISyntaxException, IOException, ParseException {
        this.mediaUtil = new MediaUtil(m);

        this.socketEndpoint = "ws://" + socketIp + ":" + socketPort;
        this.uri = new URI(socketEndpoint);
        this.restEndpoint = String.format("http://%s:%s/API/mediaInfo", socketIp, restPort);
        this.gRPCBackend = gRPCBackend;

        Response response = mediaUtil.makeRestletRequestWithoutBody(restEndpoint, Method.GET, restClient);
        Representation representation = response.getEntity();
        JSONArray jsonArray = mediaUtil.convertRepresentationToJsonArray(representation);
        this.mediaInfos = mediaUtil.convertJSONArrayToMediaInfos(jsonArray);

        this.executor = Executors.newFixedThreadPool(nrThreads);
    }

    public void makeBoxPlotIncClientsNoCaching(String title, int nrBoxes, int nrClients, int iterations, long songSize) {

        // define what data should be fetched, dummy or real? We have real data inside the mediaInfos array in this class

        // create all the threads (we need a class for this) that shall try to stream data through the music
        // streaming client (which is also an own thread)

        // for each thread call executor.execute(worker)

        // executor.shutdown()

        // while (!executor.isTerminated()) {   } or:
        // executor.awaitTermination(3000, TimeUnit.MILLISECONDS); ?

        BoxPlotXChart boxPlot = new BoxPlotXChart();
        boxPlot.createPlot(title, MusicStreamingClient.class.getName(), "Time (ms)");

        int j = 1;
        for (int i = 0; i < nrBoxes; i++) {
            // create one unique song for each client
            Song[] songs = mediaUtil.createDummySongs(nrClients * j, songSize);

            // Send all songs to the cluster
            for (Song song : songs)
                gRPCBackend.store(song);

            // Create worker threads
            WebSocketTestWorker[] testWorkers = new WebSocketTestWorker[nrClients * j];
            for (int k = 0; k < nrClients * j; k++)
                testWorkers[k] = new WebSocketTestWorker(songs[k], uri);

            // Send the workers to the thread pool
            for (WebSocketTestWorker testWorker : testWorkers)
                executor.execute(testWorker);

            // Shutdown the thread pool
            executor.shutdown();

            // Wait for the thread pool to shut down
            try {
                executor.awaitTermination(30000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Remove the songs from the cluster
            for (Song song : songs) {
                String identifierString = song.getIdentifierString();
                gRPCBackend.delete(identifierString);
            }

//            boxPlot.addToPlot(nrClients * j     + " clients", testSongStreamingNTimes(songs, iterations));

            j = j * 2;
        }

//        boxPlot.showPlot();
    }

    public void makeBoxPlotIncClientsWithCache() {

    }

}
