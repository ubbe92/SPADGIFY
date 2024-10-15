package cs.umu.se.util;

import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * The `GetOP` class is responsible for parsing command line arguments and initiating the operation
 * of a node in a Chord network. It can either create a new network or join an existing one.
 *
 * Command line parameters:
 * - mode: 0 to join an existing network, 1 to create a new network.
 * - port: The port number for this node.
 * - m: The identifier size for the network.
 *
 * Command line options:
 * - -p, --port: The port of a node in the network.
 * - -i, --ip: The IP address of a node in the network.
 * - -d, --delay: The delay in milliseconds for the stabilization process.
 * - -h, --help: Show the help message and exit.
 * - -c, --cache: Sets the desired size of the cache for storing songs.
 */
@CommandLine.Command(name = "chord", mixinStandardHelpOptions = true, version = "Ubbes Chord DHT 1.0",
        description = "Starts a node and lets it either join or create a DHT.")
public class ChordGetOP implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", paramLabel = "mode", description = "0 = join existing DTH. 1 = create new DHT.")
    private int mode = -1;

    @CommandLine.Parameters(index = "1", paramLabel = "port", description = "Which port should this node use.")
    private int port = -1;

    @CommandLine.Parameters(index = "2", paramLabel = "m", description = "The identifier length m.")
    private int m = -1;

    @CommandLine.Option(names = {"-p", "--port"}, description = "The port of a node in the DTH.")
    private int remotePort = -1;

    @CommandLine.Option(names = {"-i", "--ip"}, description = "The ip of a node in the DTH.")
    private String remoteIp = "";

    @CommandLine.Option(names = {"-d", "--delay"}, description = "How often shall the node perform stabilize (ms).")
    private int delay = 1000;

    @CommandLine.Option(names = {"-h", "--help"}, description = "Show this help message and exit!")
    private boolean help;

    @CommandLine.Option(names = {"-c", "--cache"}, description = "Sets the desired size of the cache used to store songs.")
    private int cache = 100;

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

    public int getDelay() {
        return delay;
    }

    public boolean isHelp() {
        return help;
    }

    public int getCache() {
        return cache;
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
