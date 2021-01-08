package tiny.socks.server.server;

import io.netty.channel.ChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.*;
import tiny.socks.base.receiver.AbstractReceiver;
import tiny.socks.server.server.handler.verify.RemoteServerVerifyDecoder;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/2 23:19
 * @Email:pfxuchn@gmail.com
 */
public class RemoteReceiver extends AbstractReceiver {

    private static final Logger logger = LoggerFactory.getLogger(RemoteReceiver.class);

    private final Server server;

    public RemoteReceiver(Server server) {
        this.server = server;
    }

    @Override
    protected BaseChannelInitializer getBaseChannelInitializer() {
        return new BaseChannelInitializer() {
            @Override
            protected void loadChannelHandlers(List<ChannelHandler> channelHandlers) {
                channelHandlers.add(new DataPacketEncoder());
                channelHandlers.add(new UnpackDecoder());
                channelHandlers.add(new DataPacketDecoder());
                channelHandlers.add(new RemoteServerVerifyDecoder());
                channelHandlers.add(new IdleStatePacketHandler());

            }
        };
    }

}
