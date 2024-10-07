package cs.umu.se.grpc;

import cs.umu.se.chord.ChordBackEnd;
import cs.umu.se.chord.Node;
import cs.umu.se.interfaces.Server;
import cs.umu.se.util.ChordUtil;
import cs.umu.se.workers.StabilizerWorker;
import io.grpc.ServerBuilder;
import org.checkerframework.checker.units.qual.N;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GRPCServer implements Server {
    private io.grpc.Server server;
    private StabilizerWorker worker;
    private ChordBackEnd chordBackEnd;
    private ChordUtil chordUtil = new ChordUtil();


    public GRPCServer() {}

    @Override
    public void startServer(int port, String remoteIp, int remotePort, int m, int mode, int exitCode, int delay) {
        createDirectories();

        String ip = chordUtil.getLocalIp();
        Node node = new Node(ip, port, m);

        NodeImpl nodeImpl = new NodeImpl(node, remoteIp, remotePort, mode, exitCode, delay);
        FileImpl fileImpl = new FileImpl(node);

        this.server = ServerBuilder
                .forPort(port)
                .addService(nodeImpl)
                .addService(fileImpl)
                .build();
        try {
            server.start();
            System.out.println("gRPCServer started on port: " + port);

            chordBackEnd = nodeImpl.getChordBackEnd();
            switch (mode) {
                case 0: // join existing DTH.
                    System.out.println("Joining existing DTH!");
                    Node nodePrime = new Node(remoteIp, remotePort, m);

                    // TESTING WIKI SOLUTION
                    chordBackEnd.joinWIKI(nodePrime);
                    break;
                case 1: // create new DHT
                    System.out.println("Creating new DHT!");

                    // TESTING WIKI SOLUTION
                    chordBackEnd.createWIKI();
                    break;
                default: // Unknown mode
                    System.out.println("Unknown mode!");
                    System.exit(exitCode);
                    break;
            }

            worker = chordBackEnd.getWorkerThread();

            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            System.out.println("gRPCServer could not start on port: " + port + " - " + e.getMessage());
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
        chordBackEnd.leaveWIKI();
    }

    public boolean isStabilizerWorkerAlive() {
        return this.worker.isAlive();
    }

    public boolean isShutdown() {
        return server.isShutdown();
    }

    private void createDirectories() {
        try {
            Files.createDirectory(Paths.get("./media-spadgify"));
            Files.createDirectory(Paths.get("./logs-spadgify"));

        } catch (FileAlreadyExistsException e) {
            System.out.println("createDirectories() directories already exists!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("createDirectories() could not create directories!");
            System.exit(1);
        }
    }
}
