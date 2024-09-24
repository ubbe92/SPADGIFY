package cs.umu.se.interfaces;

public interface Server {

    void startServer(int port, String remoteIp, int remotePort, int m, int mode, int exitCode);

    void stopServer();
}
