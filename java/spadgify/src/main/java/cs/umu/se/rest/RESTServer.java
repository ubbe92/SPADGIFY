package cs.umu.se.rest;


import cs.umu.se.chord.Node;
import org.apache.logging.log4j.Logger;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.engine.application.CorsFilter;
import org.restlet.routing.Router;

import java.util.Arrays;
import java.util.HashSet;

public class RESTServer extends Application {

    private Component component;
    private Node node;
    private int port;
    private Logger logger;


    public RESTServer() {}

    public void startServer(Node node, int port, Logger logger) {
        this.node = node;
        this.port = port;
        this.logger = logger;

        component = new Component();
        component.getServers().add(Protocol.HTTP, port);
        Application application = this;
        String contextRoot = "/API";
        component.getDefaultHost().attach(contextRoot, application);

        try {
            component.start();
        } catch (Exception e) {
            logger.error("Could not start restlet server: {}", e.getMessage());
        }
        System.out.println("RESTServer started on port: " + port);
    }

    public void stopServer() {
        try {
            component.stop();
        } catch (Exception e) {
            logger.error("Could not stop restlet server: {}", e.getMessage());
        }
    }

    public Node getNode() {
        return node;
    }

    public Logger getCustomLogger() {
        return logger;
    }

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/mediaInfo", RESTMediaInfo.class);
//        return router;

        // Set up CORS
        CorsFilter corsFilter = new CorsFilter(getContext(), router);
        corsFilter.setAllowedOrigins(new HashSet<>(Arrays.asList("*"))); // Allows all origins
        corsFilter.setAllowedCredentials(true);
        corsFilter.setAllowedHeaders(new HashSet<>(Arrays.asList("Content-Type", "Authorization")));
        corsFilter.setDefaultAllowedMethods(new HashSet<>(Arrays.asList(Method.GET, Method.POST, Method.OPTIONS, Method.PUT, Method.DELETE)));

        return corsFilter;
    }


}
