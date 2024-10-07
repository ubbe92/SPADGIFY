package cs.umu.se.interfaces;

public interface Server {

    void startServer(int port, String remoteIp, int remotePort, int m, int mode, int exitCode, int delay, int cacheSize);

    void stopServer();
}
