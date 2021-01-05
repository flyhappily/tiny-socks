package tiny.socks.base.connector;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/2 23:44
 * @Email:pfxuchn@gmail.com
 */
public abstract class AbstractConnector implements Connector {

    private static final Logger logger = LoggerFactory.getLogger(AbstractConnector.class);

    private final Bootstrap bootstrap;

    private final EventLoopGroup group;

    protected List<ChannelHandler> channelHandlers = new ArrayList<>();

    protected AbstractConnector() {
        this.addChannelHandlers(channelHandlers);
        if (channelHandlers.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.bootstrap = new Bootstrap();
        this.group = new NioEventLoopGroup();
        this.bootstrap.group(group);
        this.bootstrap.channel(NioSocketChannel.class);
        ConnectorChannelInitializer connectorChannelInitializer = new ConnectorChannelInitializer(this.channelHandlers);
        this.bootstrap.handler(connectorChannelInitializer);
    }

    protected abstract void addChannelHandlers(List<ChannelHandler> channelHandlers);

    public final ChannelFuture connect(String host, int port){
        try {
            return this.bootstrap.connect(host, port).sync();
        } catch (Exception e) {
            logger.error("create connector failed, [host:port]=[{}:{}]",host,port);
            return null;
        }
    }

    @Override
    public void shutdown(){
        this.group.shutdownGracefully();
    }

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
