package tiny.socks.base.connector;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.BaseChannelInitializer;
import tiny.socks.base.LifeCycle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/2 23:44
 * @Email:pfxuchn@gmail.com
 */
public abstract class AbstractConnector implements Connector, LifeCycle {

    private static final Logger logger = LoggerFactory.getLogger(AbstractConnector.class);

    protected final EventLoopGroup group;

    protected AbstractConnector() {
        this.group = new NioEventLoopGroup();
    }

    public final ChannelFuture connect(String host, int port){
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(this.getBaseChannelInitializer());
        return bootstrap.connect(host, port);
    }

    protected abstract BaseChannelInitializer getBaseChannelInitializer();

    @Override
    public void shutdown(){
        this.group.shutdownGracefully();
    }

}
