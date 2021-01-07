package tiny.socks.server.server;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.server.AbstractServer;
import tiny.socks.server.server.handler.DataPacketHandler;
import tiny.socks.server.server.handler.IdleStateDoorHandler;
import tiny.socks.server.server.handler.verify.RemoteServerVerifyDecoder;
import tiny.socks.base.ObjectEncoder;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: pf_xu
 * @date: 2021/1/2 23:19
 * @Email:pfxuchn@gmail.com
 */
public class RemoteServer  extends AbstractServer {

    private static final Logger logger = LoggerFactory.getLogger(RemoteServer.class);

    @Override
    protected void addChannelHandlers(List<ChannelHandler> channelHandlers) {
        channelHandlers.add(new IdleStateHandler(5,5,0, TimeUnit.SECONDS));
        channelHandlers.add(new IdleStateDoorHandler());
        channelHandlers.add(new ObjectEncoder());
        channelHandlers.add(new RemoteServerVerifyDecoder());
//        channelHandlers.add(new LengthFieldBasedFrameDecoder(Short.MAX_VALUE,0,2));
//        channelHandlers.add(new DataPacketHandler());

    }

}
