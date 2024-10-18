package cs.umu.se.rest;

import cs.umu.se.chord.Node;
import cs.umu.se.client.ClientBackend;
import cs.umu.se.types.MediaInfo;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.logging.log4j.Logger;
public class RESTMediaInfo extends ServerResource {
    private Node node;
    private ClientBackend clientBackend;
    private Logger logger;

    @Override
    public void doInit() {
        RESTServer restServer = (RESTServer) getApplication();
        this.node = restServer.getNode();
        this.logger = restServer.getCustomLogger();
        clientBackend = new ClientBackend(node.getMyIp(), node.getMyPort(), "", node.getM());
    }

    @Get
    public MediaInfo[] retrieveMediaInfos() {
        MediaInfo[] mediaInfos = null;
        try {
            String identifierString = node.getMyIp() + ":" + node.getMyPort();
            mediaInfos = clientBackend.listAllSongs(identifierString);
            logger.info("REST list all songs called.");

        } catch (Exception e) {
            logger.error("Could not retrieve all media infos from REST server: {}", e.getMessage());
        }

        return mediaInfos;
    }
}
