package tiny.socks.server;

import tiny.socks.server.server.Server;

/**
 * @author: pf_xu
 * @date: 2021/1/4 15:38
 * @Email:pfxuchn@gmail.com
 */
public class ServerTest {

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
