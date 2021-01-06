package tiny.socks.base.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.BaseChannelInitializer;
import tiny.socks.base.LifeCycle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/6 18:47
 * @Email:pfxuchn@gmail.com
 */
public abstract class AbstractServer implements Server, LifeCycle {

    private static final Logger logger = LoggerFactory.getLogger(AbstractServer.class);

    protected final EventLoopGroup parentGroup;

    protected final EventLoopGroup childGroup;

    protected int port;

    protected AbstractServer() {
        this.parentGroup = new NioEventLoopGroup();
        this.childGroup = new NioEventLoopGroup();
        this.port = 7979;
    }

    public AbstractServer port(int port){
        this.port = port;
        return this;
    }

    protected abstract void addChannelHandlers(List<ChannelHandler> channelHandlers);

    private List<ChannelHandler> loadChannelHandlers(){
        List<ChannelHandler> channelHandlers = new ArrayList<>();
        this.addChannelHandlers(channelHandlers);
        if (channelHandlers.isEmpty()) {
            throw new IllegalArgumentException("channelHandlers should be configured correctly");
        }
        return channelHandlers;
    }

    @Override
    public final void start(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentGroup,childGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new BaseChannelInitializer(loadChannelHandlers()));
        try {
            ChannelFuture future = serverBootstrap.bind(port).sync();
            logger.info("server is listening in port {}",port);
            future.channel().closeFuture().sync();
        }catch (Exception e){
            //do nothing
        }finally {
            this.childGroup.shutdownGracefully();
            this.parentGroup.shutdownGracefully();
        }
    }

    @Override
    public final void shutdown(){
        childGroup.shutdownGracefully();
        parentGroup.shutdownGracefully();
    }
}
