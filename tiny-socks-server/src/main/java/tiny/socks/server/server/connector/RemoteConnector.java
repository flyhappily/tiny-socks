package tiny.socks.server.server.connector;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.BaseChannelInitializer;
import tiny.socks.base.connector.AbstractConnector;
import tiny.socks.server.server.Server;
import tiny.socks.server.server.connector.handler.ByteIn;
import tiny.socks.server.server.connector.handler.HandlerByte;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 远程服务器上的连接器，用于连接远程服务
 * @author: pf_xu
 * @date: 2021/1/8 9:54
 * @Email:pfxuchn@gmail.com
 */
public class RemoteConnector extends AbstractConnector {

    private static final Logger logger = LoggerFactory.getLogger(RemoteConnector.class);

    private final Server server;

    private  final ConcurrentHashMap<String, Channel> channels = new ConcurrentHashMap<>();

    private Channel TunnelChannel;

    public RemoteConnector(Server server) {
        this.server = server;
    }

    @Override
    protected BaseChannelInitializer getBaseChannelInitializer() {
        return new BaseChannelInitializer(this) {
            @Override
            protected void loadChannelHandlers(List<ChannelHandler> channelHandlers) {
                channelHandlers.add(new ByteIn());
                channelHandlers.add(new HandlerByte());
            }
        };
    }
}
