package cs.umu.se;

import cs.umu.se.grpc.GRPCServer;
import cs.umu.se.util.GetOP;
import picocli.CommandLine;

import java.util.Arrays;

public class Chord {
    public static void main(String[] args) {


        // Examples on how to run this chord node
        // Start new DHT:       java -jar spadgify-1.0-SNAPSHOT.jar 1 8185 3 (id 1)
        // Join existing DHT:   java -jar spadgify-1.0-SNAPSHOT.jar -p 8185 -i 192.168.38.126 0 8187 3 (id 3)
        // Join existing DHT:   java -jar spadgify-1.0-SNAPSHOT.jar -p 8185 -i 192.168.38.126 0 8193 3 (id 0)
        GetOP getOp = new GetOP();
        CommandLine commandLine = new CommandLine(getOp);
        int exitCode = commandLine.execute(args);

        if (exitCode != 0) {
            System.out.println("Exit code: " + exitCode + " reason: " + getOp.translateExitCode(exitCode));
//            commandLine.usage(System.out);
            System.exit(exitCode);
        }
        System.out.println("args: " + Arrays.toString(args));

        int port = getOp.getPort();
        String remoteIp = getOp.getRemoteIp();
        int remotePort = getOp.getRemotePort();
        int m = getOp.getM();
        int mode = getOp.getMode();

        GRPCServer server = new GRPCServer();
        server.startServer(port, remoteIp, remotePort, m, mode, exitCode);
    }
}