package tiny.socks.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.server.handler.verify.ServerVerifyDecoder;
import tiny.socks.base.encoder.ObjectEncoder;
/**
 * @author: pf_xu
 * @date: 2021/1/2 23:19
 * @Email:pfxuchn@gmail.com
 */
public class Server{

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    protected final EventLoopGroup parentGroup;

    protected final EventLoopGroup childGroup;

    protected final ServerBootstrap serverBootstrap;

    protected final int port;

    protected Server() {
        this.parentGroup = new NioEventLoopGroup();
        this.childGroup = new NioEventLoopGroup();
        this.serverBootstrap = new ServerBootstrap();
        this.port = 7979;
    }

    public void run() {
        try {
            // 1. 绑定两个线程组分别用来处理客户端通道的accept和读写时间
            this.serverBootstrap.group(this.parentGroup, this.childGroup)
                    // 2. 绑定服务端通道NioServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    // 3. 给读写事件的线程通道绑定handler去真正处理读写
                    // ChannelInitializer初始化通道SocketChannel
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ObjectEncoder());
                            socketChannel.pipeline().addLast(new ServerVerifyDecoder());
                        }
                    });
            // 4. 监听端口（服务器host和port端口），同步返回
            ChannelFuture future = serverBootstrap.bind(this.port).sync();
            logger.info("[http server] has booted successfully and  opened in port:{}", this.port);
            // 当通道关闭时继续向后执行，这是一个阻塞方法
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("serverBoot failed e:[{}] e.message = [{}]", e, e.getMessage());
        } finally {
            this.childGroup.shutdownGracefully();
            this.parentGroup.shutdownGracefully();
        }
    }


}
