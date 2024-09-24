package cs.umu.se.util;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "chord", mixinStandardHelpOptions = true, version = "Ubbes Chord DHT 1.0",
        description = "Starts a node and lets it either join or create a DHT.")
public class GetOP implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", paramLabel = "mode", description = "0 = join existing DTH. 1 = create new DHT")
    private int mode = -1;

    @CommandLine.Parameters(index = "1", paramLabel = "port", description = "Which port should this node use.")
    private int port = -1;

    @CommandLine.Parameters(index = "2", paramLabel = "m", description = "The identifier length m")
    private int m = -1;

    @CommandLine.Option(names = {"-p", "--port"}, description = "The port of a node in the DTH.")
    private int remotePort = -1;

    @CommandLine.Option(names = {"-i", "--ip"}, description = "The ip of a node in the DTH.")
    private String remoteIp = "";

    @Override
    public Integer call() throws Exception {
        int ret = 0;

        if (mode == 0) {

            if (remotePort == -1)
                ret = -1;

            if (remoteIp.equals(""))
                ret = -1;

        } else if (mode == 1) {
        } else {
            ret = -2;
        }

        return ret;
    }

    public int getMode() {
        return mode;
    }

    public int getPort() {
        return port;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public int getM() {
        return m;
    }

    public String translateExitCode(int exitCode) {
        String ret = "";
        switch (exitCode) {
            case -2:
                ret = "Unknown mode: " + mode;
                break;
            case -1:
                ret = "Missing remote node IP and/or Port when trying to use mode: " + mode;
                break;
            case 0:
                ret = "All is well";
                break;
            default:
                ret = "Unknown exit code!";
                break;
        }
        return ret;
    }
}
