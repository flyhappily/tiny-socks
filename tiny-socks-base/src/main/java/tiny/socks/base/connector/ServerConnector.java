package tiny.socks.base.connector;

import io.netty.channel.ChannelHandler;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/3 0:29
 * @Email:pfxuchn@gmail.com
 */
public class ServerConnector extends AbstractConnector{

    protected ServerConnector(String host, int port) {
        super(host, port);
    }

    @Override
    protected void addChannelHandlers(List<ChannelHandler> channelHandlers) {

    }

    @Override
    protected void initVerifyMessageSend() {

    }

}
