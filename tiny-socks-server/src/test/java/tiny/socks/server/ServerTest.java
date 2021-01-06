package tiny.socks.server;

import tiny.socks.server.server.RemoteServer;

/**
 * @author: pf_xu
 * @date: 2021/1/4 15:38
 * @Email:pfxuchn@gmail.com
 */
public class ServerTest {

    public static void main(String[] args) {
        RemoteServer remoteServer = new RemoteServer();
        remoteServer.start();
    }
}
