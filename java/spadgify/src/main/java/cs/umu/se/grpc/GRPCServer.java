package cs.umu.se.grpc;

import cs.umu.se.chord.ChordBackEnd;
import cs.umu.se.chord.Node;
import cs.umu.se.interfaces.Server;
import cs.umu.se.music.MusicStreamingServer;
import cs.umu.se.rest.RESTServer;
import cs.umu.se.storage.StorageBackend;
import cs.umu.se.util.ChordUtil;
import cs.umu.se.workers.StabilizerWorker;
import io.grpc.ServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The GRPCServer class implements the Server interface and is responsible
 * for initializing and managing a gRPC server instance that supports a
 * distributed hash table (DHT) through the Chord protocol.
 */
public class GRPCServer implements Server {
    private io.grpc.Server server;
    private StabilizerWorker worker;
    private ChordBackEnd chordBackEnd;
    private StorageBackend storageBackend;
    private final ChordUtil chordUtil = new ChordUtil();
    private final Logger logger  = LogManager.getLogger();
    private MusicStreamingServer musicStreamingServer;
    private RESTServer restServer;

    public GRPCServer() {}

    /**
     * Starts the gRPC server for the node.
     *
     * @param port The local port to start the server on.
     * @param remoteIp The remote IP address to connect to for existing DHT, if applicable.
     * @param remotePort The remote port to connect to for existing DHT, if applicable.
     * @param m The number of bits for the node identifier.
     * @param mode The operational mode of the server (0 for joining existing DHT, 1 for creating new DHT).
     * @param exitCode The exit code to use in case of a critical error.
     * @param delay The delay in milliseconds between each stabilize.
     * @param cacheSize The cache size for the storage backend.
     */
    @Override
    public void startServer(int port, String remoteIp, int remotePort, int m, int mode, int exitCode, int delay, int cacheSize) {
        createDirectories();

        String ip = chordUtil.getLocalIp();
        Node node = new Node(ip, port, m);

        storageBackend = new StorageBackend(node, m, this.logger);
        chordBackEnd = new ChordBackEnd(node, this.logger);

        NodeImpl nodeImpl = new NodeImpl(node, remoteIp, remotePort, mode, exitCode, delay, logger, chordBackEnd);
        FileImpl fileImpl = new FileImpl(node, cacheSize, logger, storageBackend);

        this.server = ServerBuilder
                .forPort(port)
                .addService(nodeImpl)
                .addService(fileImpl)
                .build();
        try {
            server.start();
            logger.info("gRPCServer started on port: {}",  port);

            switch (mode) {
                case 0: // join existing DTH.
                    logger.info("Joining existing DHT!");
                    Node nodePrime = new Node(remoteIp, remotePort, m);

                    chordBackEnd.join(nodePrime);
                    break;
                case 1: // create new DHT
                    logger.info("Creating new DHT!");

                    chordBackEnd.create();
                    break;
                default: // Unknown mode
                    logger.error("Unknown mode!: {}", mode);
                    System.exit(exitCode);
                    break;
            }

            worker = chordBackEnd.getWorkerThread();

            // Start music server
            int musicServerPort = chordUtil.getAvailablePortSocket(8080, 8160);
            String logMessage = "Chosen music socket port: " + musicServerPort;
            logger.info(logMessage);
            InetSocketAddress address = new InetSocketAddress(ip, musicServerPort);
            musicStreamingServer = new MusicStreamingServer(address, node, logger);
            musicStreamingServer.start();

            // Start rest server
            int restServerPort = chordUtil.getAvailablePortSocket(8000, 8079);
            logMessage = "Chosen rest server port: " + restServerPort;
            logger.info(logMessage);
            restServer = new RESTServer();
            restServer.startServer(node, restServerPort, logger);

            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            logger.error("gRPCServer could not start on port: {} - {}", port,  e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void stopServer() {
        this.server.shutdown();
    }

    public void stopWorkerThread() {
        this.worker.stopStabilize();
    }

    public void leaveChordNetwork() {
        chordBackEnd.leave();
    }

    public boolean isStabilizerWorkerAlive() {
        return this.worker.isAlive();
    }

    public boolean isShutdown() {
        return server.isShutdown();
    }

    /**
     * Creates the necessary directories for the gRPC server to store media and log files.
     */
    private void createDirectories() {
        try {
            Files.createDirectory(Paths.get("./media-spadgify"));
            Files.createDirectory(Paths.get("./logs-spadgify"));

        } catch (FileAlreadyExistsException e) {
            logger.info("createDirectories() directories already exists!");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("createDirectories() could not create directories!");
            System.exit(1);
        }
    }

    public StorageBackend getStorageBackend() {
        return this.storageBackend;
    }

    public void stopMusicServer() {
        try {
            musicStreamingServer.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopRESTServer() {
        restServer.stopServer();
    }
}
