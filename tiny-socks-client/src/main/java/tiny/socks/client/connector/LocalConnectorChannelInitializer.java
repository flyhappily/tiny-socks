package tiny.socks.client.connector;

import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.IdleStateHandler;
import tiny.socks.base.*;
import tiny.socks.client.connector.handler.LocalIdleStateHandler;
import tiny.socks.client.connector.handler.verify.LocalConnectorVerifyDecoder;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/8 9:29
 * @Email:pfxuchn@gmail.com
 */
public class LocalConnectorChannelInitializer extends BaseChannelInitializer {


    public LocalConnectorChannelInitializer(Module module) {
        super(module);
    }

    @Override
    protected void loadChannelHandlers(List<ChannelHandler> channelHandlers) {
        channelHandlers.add(new DataPacketEncoder());
        channelHandlers.add(new IdleStateHandler(0,0,5));
        channelHandlers.add(new LocalIdleStateHandler((LocalConnector)module));
        channelHandlers.add(new UnpackDecoder());
        channelHandlers.add(new DataPacketDecoder());
        channelHandlers.add(new LocalConnectorVerifyDecoder());
    }
}
