package cs.umu.se.interfaces;

import cs.umu.se.chord.Node;

public interface Server {

    void startServer(int port, String remoteIp, int remotePort, int m, int mode, int exitCode);

    void stopServer();
}
