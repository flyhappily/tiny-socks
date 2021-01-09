package tiny.socks.client;

import tiny.socks.client.connector.LocalConnector;

/**
 * @author: pf_xu
 * @date: 2021/1/7 22:13
 * @Email:pfxuchn@gmail.com
 */
public class ClientTest2 {

    public static void main(String[] args) {
        LocalConnector localConnector = new LocalConnector("127.0.0.1",7979);
        localConnector.start();
    }

}
