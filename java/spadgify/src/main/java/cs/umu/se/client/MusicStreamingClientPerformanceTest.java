package cs.umu.se.client;

import cs.umu.se.types.MediaInfo;
import cs.umu.se.util.MediaUtil;
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
import java.util.Arrays;
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

    public MusicStreamingClientPerformanceTest(String socketIp, int socketPort, int restPort, int m, int nrThreads) throws URISyntaxException, IOException, ParseException {
        this.mediaUtil = new MediaUtil(m);

        this.socketEndpoint = "ws://" + socketIp + ":" + socketPort;
        this.uri = new URI(socketEndpoint);
        this.restEndpoint = String.format("http://%s:%s/API/mediaInfo", socketIp, restPort);

        Response response = mediaUtil.makeRestletRequestWithoutBody(restEndpoint, Method.GET, restClient);
        Representation representation = response.getEntity();
        JSONArray jsonArray = mediaUtil.convertRepresentationToJsonArray(representation);
        this.mediaInfos = mediaUtil.convertJSONArrayToMediaInfos(jsonArray);

        this.executor = Executors.newFixedThreadPool(nrThreads);
    }

    public void makeBoxPlotIncClientsNoCaching() {

        // define what data should be fetched, dummy or real? We have real data inside the mediaInfos array in this class

        // create all the threads (we need a class for this) that shall try to stream data through the music
        // streaming client (which is also an own thread)

        // for each thread call executor.execute(worker)

        // executor.shutdown()

        // while (!executor.isTerminated()) {   } or:
        // executor.awaitTermination(3000, TimeUnit.MILLISECONDS); ?


    }

    public void makeBoxPlotIncClientsWithCache() {

    }


}
