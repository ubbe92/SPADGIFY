package cs.umu.se.util;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "client", mixinStandardHelpOptions = true, version = "Test client 1.0",
        description = "Performs logic and performance test on the chord cluster.")
public class ClientGetOP implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", paramLabel = "node ip", description = "The ip to a node.")
    private String nodeIp = "";

    @CommandLine.Parameters(index = "1", paramLabel = "node port", description = "The port to a node.")
    private int nodePort = -1;

    @CommandLine.Parameters(index = "2", paramLabel = "m", description = "The identifier length m.")
    private int m = -1;

    @CommandLine.Parameters(index = "3", paramLabel = "socket ip", description = "The ip to a web socket.")
    private String socketIp = "";

    @CommandLine.Parameters(index = "4", paramLabel = "socket port", description = "The port to a web socket.")
    private int socketPort = -1;

    @CommandLine.Parameters(index = "5", paramLabel = "rest port", description = "The port to a rest server.")
    private int restPort = -1;


    @CommandLine.Option(names = {"-l", "--logic"}, description = "Perform logic tests.")
    private boolean logic = false;

    @CommandLine.Option(names = {"-p", "--performance"}, description = "Perform performance tests.")
    private boolean performance = false;

    @CommandLine.Option(names = {"-u", "--upload"}, description = "Upload real music files from the given path to the node.")
    private String pathToMusic = "";



    @Override
    public Integer call() throws Exception {
        return 0;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public int getNodePort() {
        return nodePort;
    }

    public int getM() {
        return m;
    }

    public boolean isLogic() {
        return logic;
    }

    public boolean isPerformance() {
        return performance;
    }

    public String getSocketIp() {
        return socketIp;
    }

    public int getSocketPort() {
        return socketPort;
    }

    public int getRestPort() {
        return restPort;
    }

    public String getPathToMusic() {
        return pathToMusic;
    }

    public boolean isUpload() {
        return !pathToMusic.equals("");
    }
}
