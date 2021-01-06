package tiny.socks.base.connector;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.LifeCycle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/2 23:44
 * @Email:pfxuchn@gmail.com
 */
public abstract class AbstractConnector implements Connector, LifeCycle {

    protected final EventLoopGroup group;

    protected AbstractConnector() {
        this.group = new NioEventLoopGroup();
    }

    public final ChannelFuture connect(String host, int port){
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ConnectorChannelInitializer(this.loadHandlers()));
        return bootstrap.connect(host, port);
    }

    @Override
    public void shutdown(){
        this.group.shutdownGracefully();
    }

    private  List<ChannelHandler> loadHandlers(){
        List<ChannelHandler> channelHandlers = new ArrayList<>();
        this.addChannelHandlers(channelHandlers);
        if (channelHandlers.isEmpty()) {
            throw new IllegalArgumentException("channelHandlers should be configured correctly");
        }
        return channelHandlers;
    }

    protected abstract void addChannelHandlers(List<ChannelHandler> channelHandlers);

    private static class ConnectorChannelInitializer extends ChannelInitializer<NioSocketChannel> {

        private final List<ChannelHandler> channelHandlers;

        public ConnectorChannelInitializer(List<ChannelHandler> channelHandlers){
            this.channelHandlers = channelHandlers;
        }

        @Override
        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
            for (ChannelHandler channelHandler:this.channelHandlers){
                nioSocketChannel.pipeline().addLast(channelHandler);
            }
        }
    }
}
