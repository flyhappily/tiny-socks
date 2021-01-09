package tiny.socks.server.server.receiver;

import io.netty.channel.ChannelHandler;
import tiny.socks.base.*;
import tiny.socks.server.server.receiver.handler.RemoteReceiverDataPacketHandler;
import tiny.socks.server.server.receiver.handler.verify.RemoteServerVerifyDecoder;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/9 11:09
 * @Email:pfxuchn@gmail.com
 */
public class RemoteReceiverChannelInitializer extends BaseChannelInitializer {


    public RemoteReceiverChannelInitializer(Module module) {
        super(module);
    }

    @Override
    protected void loadChannelHandlers(List<ChannelHandler> channelHandlers) {
        channelHandlers.add(new DataPacketEncoder());
        channelHandlers.add(new UnpackDecoder());
        channelHandlers.add(new DataPacketDecoder());
        channelHandlers.add(new RemoteServerVerifyDecoder());
        channelHandlers.add(new IdleStatePacketHandler());
        channelHandlers.add(new RemoteReceiverDataPacketHandler((RemoteReceiver)module));
    }
}
