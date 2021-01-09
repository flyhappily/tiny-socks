package tiny.socks.server.server.receiver;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.BaseChannelInitializer;
import tiny.socks.base.model.action.ConnectActionData;
import tiny.socks.base.receiver.AbstractReceiver;
import tiny.socks.server.server.Server;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: pf_xu
 * @date: 2021/1/2 23:19
 * @Email:pfxuchn@gmail.com
 */
public class RemoteReceiver extends AbstractReceiver {

    private static final Logger logger = LoggerFactory.getLogger(RemoteReceiver.class);

    private final Server server;

    private final ConcurrentHashMap<ChannelId, Channel> tunnels = new ConcurrentHashMap<>();

    public RemoteReceiver(Server server) {
        this.server = server;
    }

    @Override
    protected BaseChannelInitializer getBaseChannelInitializer() {
        return new RemoteReceiverChannelInitializer(this);
    }

    public void connectRemote(Channel tunnelChannel, ConnectActionData connectActionData){

    }

}
