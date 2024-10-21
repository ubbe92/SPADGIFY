package cs.umu.se.client;

import cs.umu.se.types.MediaInfo;
import cs.umu.se.util.MediaUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.restlet.Client;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class MusicStreamingClientLogicTest {
    private Client restClient = new Client(Protocol.HTTP);
    private MusicStreamingClient musicStreamingClient;
    private String socketIp;
    private int socketPort;
    private int restPort;
    private int m;
    private String socketEndpoint;
    private String restEndpoint;


    public MusicStreamingClientLogicTest(String socketIp, int socketPort, int restPort, int m) throws URISyntaxException {
        this.socketIp = socketIp;
        this.socketPort = socketPort;
        this.restPort = restPort;
        this.m = m;

        this.socketEndpoint = "ws://" + socketIp + ":" + socketPort;
        URI uri = new URI(socketEndpoint);
        this.musicStreamingClient = new MusicStreamingClient(uri);

        this.restEndpoint = String.format("http://%s:%s/API/mediaInfo", socketIp, restPort);
    }

    public void testRESTListAllSongs() {
        MediaUtil mediaUtil = new MediaUtil(m);
        Response response = mediaUtil.makeRestletRequestWithoutBody(restEndpoint, Method.GET, restClient);
        Representation representation = response.getEntity();

        try {
            JSONArray jsonArray = mediaUtil.convertRepresentationToJsonArray(representation);

            if (jsonArray.isEmpty())
                throw new IllegalStateException("testRESTListAllSongs(): response from server was empty. Are there songs in the cluster?");

            JSONObject jsonObj = (JSONObject) jsonArray.get(0);

            if(!jsonObj.containsKey("song"))
                throw new IllegalStateException("testRESTListAllSongs(): response from server did not contain song");

            if(!jsonObj.containsKey("duration"))
                throw new IllegalStateException("testRESTListAllSongs(): response from server did not contain duration");

            if(!jsonObj.containsKey("size"))
                throw new IllegalStateException("testRESTListAllSongs(): response from server did not contain size");

            if(!jsonObj.containsKey("artist"))
                throw new IllegalStateException("testRESTListAllSongs(): response from server did not contain artist");

            if(!jsonObj.containsKey("album"))
                throw new IllegalStateException("testRESTListAllSongs(): response from server did not contain album");

            if(!jsonObj.containsKey("genre"))
                throw new IllegalStateException("testRESTListAllSongs(): response from server did not contain genre");

            if(!jsonObj.containsKey("hash"))
                throw new IllegalStateException("testRESTListAllSongs(): response from server did not contain hash");

            if(!jsonObj.containsKey("identifierString"))
                throw new IllegalStateException("testRESTListAllSongs(): response from server did not contain identifierString");

        } catch (IOException | ParseException e) {
            throw new IllegalStateException("testRESTListAllSongs(): could not convert REST response to JSON array");
        }

        System.out.println("testRESTListAllSongs() done!");
    }

    public void testStreamingData() {
        String getThisSong = "ethereal vistas-Mikael Jäcksson-In the bodega";
        try {
            musicStreamingClient.addHeader("getAll", "true");
            musicStreamingClient.connectBlocking();

            if (musicStreamingClient.isOpen()) {
                musicStreamingClient.send(getThisSong);
                musicStreamingClient.awaitResponse();
            }

            if (musicStreamingClient.getData() == null)
                throw new IllegalStateException("testStreamingData(): could not fetch song data.");

            musicStreamingClient.closeBlocking();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("testStreamingData() done!");
    }
}
