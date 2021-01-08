package tiny.socks.client.connector;

import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.IdleStateHandler;
import tiny.socks.base.BaseChannelInitializer;
import tiny.socks.base.DataPacketDecoder;
import tiny.socks.base.DataPacketEncoder;
import tiny.socks.base.UnpackDecoder;
import tiny.socks.client.connector.handler.LocalIdleStateHandler;
import tiny.socks.client.connector.handler.verify.LocalConnectorVerifyDecoder;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/8 9:29
 * @Email:pfxuchn@gmail.com
 */
public class LocalConnectorChannelInitializer extends BaseChannelInitializer {

    private final LocalConnector localConnector;

    public LocalConnectorChannelInitializer(LocalConnector localConnector) {
        this.localConnector = localConnector;
    }

    @Override
    protected void loadChannelHandlers(List<ChannelHandler> channelHandlers) {
        channelHandlers.add(new IdleStateHandler(0,0,5));
        channelHandlers.add(new LocalIdleStateHandler(localConnector));
        channelHandlers.add(new DataPacketEncoder());
        channelHandlers.add(new UnpackDecoder());
        channelHandlers.add(new DataPacketDecoder());
        channelHandlers.add(new LocalConnectorVerifyDecoder());
    }
}
