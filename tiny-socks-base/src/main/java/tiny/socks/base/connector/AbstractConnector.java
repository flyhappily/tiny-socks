package tiny.socks.base.connector;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
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

    private EventLoopGroup group;

    private final String host;

    private final int port;

    protected List<ChannelHandler> channelHandlers = new ArrayList<>();

    protected ChannelFuture channelFuture;

    protected AbstractConnector(String host, int port) {
        this.host = host;
        this.port = port;
        this.addChannelHandlers(channelHandlers);
    }

    protected abstract void addChannelHandlers(List<ChannelHandler> channelHandlers);


    public final void connect(){
        if (channelHandlers.isEmpty()) {
            throw new IllegalArgumentException();
        }
        // 首先，netty通过ServerBootstrap启动服务端
        Bootstrap client = new Bootstrap();

        //第1步 定义线程组，处理读写和链接事件，没有了accept事件
        this.group = new NioEventLoopGroup();
        try {
            client.group(group);

            //第2步 绑定客户端通道
            client.channel(NioSocketChannel.class);

            //第3步 给NIoSocketChannel初始化handler， 处理读写事件
            ConnectorChannelInitializer connectorChannelInitializer = new ConnectorChannelInitializer(this.channelHandlers);
            client.handler(connectorChannelInitializer);

            //连接服务器
            this.channelFuture = client.connect(this.host, this.port).sync();
            this.initVerifyMessageSend();
            this.channelFuture.channel().closeFuture().sync();
            this.shutdown();
        } catch (Exception e) {
            group.shutdownGracefully();
            logger.error("create connector failed, [host:port]=[{}:{}]",this.host,this.port);
            Thread.currentThread().interrupt();
        }

    }

    protected abstract void initVerifyMessageSend();

    @Override
    public final void write(byte[] bytes){
        this.channelFuture.channel().writeAndFlush(bytes);
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
