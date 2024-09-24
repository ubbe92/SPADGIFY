package cs.umu.se.grpc;

import cs.umu.se.chord.ChordBackEnd;
import cs.umu.se.chord.FingerTableNode;
import cs.umu.se.chord.Node;
import cs.umu.se.interfaces.Server;
import io.grpc.ServerBuilder;
import org.checkerframework.checker.units.qual.N;

import java.io.IOException;

public class GRPCServer implements Server {
    private io.grpc.Server server;

    public GRPCServer() {}

    @Override
    public void startServer(int port, String remoteIp, int remotePort, int m, int mode, int exitCode) {

        NodeImpl nodeImpl = new NodeImpl(port, remoteIp, remotePort, m, mode, exitCode);
        this.server = ServerBuilder
                .forPort(port)
                .addService(nodeImpl)
                .build();
        try {
            server.start();
            System.out.println("gRPCServer started on port: " + port);

            ChordBackEnd chordBackEnd = nodeImpl.getChordBackEnd();
            switch (mode) {
                case 0: // join existing DTH.
                    System.out.println("Joining existing DTH!");
                    Node id = new Node(remoteIp, remotePort, m);
                    chordBackEnd.join(id);
                    break;
                case 1: // create new DHT
                    System.out.println("Creating new DHT!");
                    chordBackEnd.join(null);

                    break;
                default: // Unknown mode
                    System.out.println("Unknown mode!");
                    System.exit(exitCode);
                    break;
            }

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

    public boolean isShutdown() {
        return server.isShutdown();
    }
}
