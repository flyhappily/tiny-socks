package tiny.socks.server.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.BaseChannelInitializer;
import tiny.socks.base.connector.AbstractConnector;

/**
 * 远程服务器上的连接器，用于连接远程服务
 * @author: pf_xu
 * @date: 2021/1/8 9:54
 * @Email:pfxuchn@gmail.com
 */
public class RemoteConnector extends AbstractConnector {

    private final Server server;

    private static final Logger logger = LoggerFactory.getLogger(RemoteConnector.class);

    public RemoteConnector(Server server) {
        this.server = server;
    }

    @Override
    protected BaseChannelInitializer getBaseChannelInitializer() {
        return null;
    }
}
