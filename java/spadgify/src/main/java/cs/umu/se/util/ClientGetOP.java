package cs.umu.se.util;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "client", mixinStandardHelpOptions = true, version = "Test client 1.0",
        description = "Performs logic and performance test on the chord cluster.")
public class ClientGetOP implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", paramLabel = "ip", description = "The ip to a node.")
    private String ip = "";

    @CommandLine.Parameters(index = "1", paramLabel = "port", description = "The port to a node.")
    private int port = -1;

    @CommandLine.Parameters(index = "2", paramLabel = "m", description = "The identifier length m.")
    private int m = -1;

    @CommandLine.Option(names = {"-l", "--logic"}, description = "Perform logic tests.")
    private boolean logic = false;

    @CommandLine.Option(names = {"-p", "--performance"}, description = "Perform performance tests.")
    private boolean performance = false;


    @Override
    public Integer call() throws Exception {
        return 0;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
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
}
