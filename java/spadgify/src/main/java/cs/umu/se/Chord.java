package cs.umu.se;

import cs.umu.se.grpc.GRPCServer;
import cs.umu.se.util.ChordGetOP;
import picocli.CommandLine;

public class Chord {

    public static void main(String[] args) {

        // Examples on how to run this chord node
        // Start new DHT:       java -jar spadgify-1.0-SNAPSHOT.jar -c 10 -d 1000 1 8185 3 (id 1) (sinthu: id 4)
        // Join existing DHT:   java -jar spadgify-1.0-SNAPSHOT.jar -c 10 -d 1000 -p 8185 -i 192.168.38.126 0 8187 3 (id 3) (sinthu: id 6)
        // Join existing DHT:   java -jar spadgify-1.0-SNAPSHOT.jar -c 10 -d 1000 -p 8185 -i 192.168.38.126 0 8193 3 (id 0) (sinthu: id 5)
        // Join existing DHT:   java -jar spadgify-1.0-SNAPSHOT.jar -c 10 -d 1000 -p 8185 -i 192.168.38.126 0 8188 3 (id 6)
        // Join existing DHT:   java -jar spadgify-1.0-SNAPSHOT.jar -c 10 -d 1000 -p 8185 -i 192.168.38.126 0 8206 3 (id 4)

        // music player ip: 192.168.38.126:8080
        // ethereal vistas-Mikael Jäcksson-In the bodega
        ChordGetOP chordGetOp = new ChordGetOP();
        CommandLine commandLine = new CommandLine(chordGetOp);
        int exitCode = commandLine.execute(args);

        if (chordGetOp.isHelp())
            System.exit(0);

        if (exitCode != 0) {
            System.out.println("Exit code: " + exitCode + " reason: " + chordGetOp.translateExitCode(exitCode));
//            commandLine.usage(System.out);
            System.exit(exitCode);
        }

        // System.out.println("args: " + Arrays.toString(args));

        int port = chordGetOp.getPort();
        String remoteIp = chordGetOp.getRemoteIp();
        int remotePort = chordGetOp.getRemotePort();
        int m = chordGetOp.getM();
        int mode = chordGetOp.getMode();
        int delay = chordGetOp.getDelay();
        int cacheSize = chordGetOp.getCache();

        // System.out.println("Delay: " + delay);

        GRPCServer server = new GRPCServer();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");

            // Stop the music streaming server
            server.stopMusicServer();

            // Stop the rest server
            server.stopRESTServer();

            server.stopWorkerThread();
            while (server.isStabilizerWorkerAlive()); // Wait a while for the worker thread to stop

            System.out.println("Notifying successor and predecessor and transferring keys");
            server.leaveChordNetwork();

            System.out.println("Stopping worker thread and server thread!");
            server.stopServer();

        }));

        server.startServer(port, remoteIp, remotePort, m, mode, exitCode, delay, cacheSize);
    }
}