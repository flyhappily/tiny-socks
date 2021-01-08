package tiny.socks.base.receiver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.BaseChannelInitializer;
import tiny.socks.base.LifeCycle;

/**
 * @author: pf_xu
 * @date: 2021/1/6 18:47
 * @Email:pfxuchn@gmail.com
 */
public abstract class AbstractReceiver implements Receiver, LifeCycle {

    private static final Logger logger = LoggerFactory.getLogger(AbstractReceiver.class);

    protected final EventLoopGroup parentGroup;

    protected final EventLoopGroup childGroup;

    protected int port;

    protected AbstractReceiver() {
        this.parentGroup = new NioEventLoopGroup();
        this.childGroup = new NioEventLoopGroup();
        this.port = 7979;
    }

    public AbstractReceiver port(int port){
        this.port = port;
        return this;
    }

    protected abstract BaseChannelInitializer getBaseChannelInitializer();

    public final void start(){
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(parentGroup,childGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(getBaseChannelInitializer());
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
