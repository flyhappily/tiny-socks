package tiny.socks.client;

import tiny.socks.base.connector.exception.ConnectorException;

/**
 * @author: pf_xu
 * @date: 2021/1/4 14:28
 * @Email:pfxuchn@gmail.com
 */
public class ClientTest {

    public static void main(String[] args) throws ConnectorException {
        LocalConnector localConnector = new LocalConnector();
        localConnector.doConnect("127.0.0.1",7979);
    }

}
