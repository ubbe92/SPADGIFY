package cs.umu.se.client;

import cs.umu.se.interfaces.Storage;

public class ClientPerformanceTest {
    private Storage storage;

    public ClientPerformanceTest(Storage storage, int m, String nodeIp, int nodePort) {
        this.storage = storage;
    }
}
