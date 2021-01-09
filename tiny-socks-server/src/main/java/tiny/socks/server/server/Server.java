package tiny.socks.server.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.server.server.connector.RemoteConnector;
import tiny.socks.server.server.receiver.RemoteReceiver;

/**
 * @author: pf_xu
 * @date: 2021/1/8 10:03
 * @Email:pfxuchn@gmail.com
 */
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final RemoteReceiver remoteReceiver;

    private final RemoteConnector remoteConnector;

    public Server() {
        remoteReceiver = (RemoteReceiver) new RemoteReceiver(this).port(7979);
        remoteConnector = new RemoteConnector(this);
    }


    public void start(){
        remoteReceiver.start();
    }


}
