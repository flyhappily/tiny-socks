package tiny.socks.client.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.LifeCycle;

/**
 * @author: pf_xu
 * @date: 2021/1/6 17:51
 * @Email:pfxuchn@gmail.com
 */
public class LocalServer implements LifeCycle {

    private static final Logger logger = LoggerFactory.getLogger(LocalServer.class);

    protected  final EventLoopGroup parentGroup;

    protected  final EventLoopGroup childGroup;

    protected  final ServerBootstrap serverBootstrap;

    protected  int port = 9999;

    protected LocalServer() {
        this.parentGroup = new NioEventLoopGroup();
        this.childGroup = new NioEventLoopGroup();
        this.serverBootstrap = new ServerBootstrap();
    }

    public LocalServer port(int port){
        this.port = port;
        return this;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {
        childGroup.shutdownGracefully();
        parentGroup.shutdownGracefully();
    }
}
