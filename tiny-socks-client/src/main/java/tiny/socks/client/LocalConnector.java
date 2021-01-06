package tiny.socks.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.connector.AbstractConnector;
import tiny.socks.base.encoder.ObjectEncoder;
import tiny.socks.base.model.RemoteAddress;
import tiny.socks.client.handler.LocalIdleStateHandler;
import tiny.socks.client.handler.verify.ClientVerifyDecoder;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: pf_xu
 * @date: 2021/1/3 0:24
 * @Email:pfxuchn@gmail.com
 */
public class LocalConnector extends AbstractConnector {

    private static final Logger logger = LoggerFactory.getLogger(LocalConnector.class);

    private ClientVerifyDecoder clientVerifyDecoder;

    private ChannelFuture channelFuture;

    private RemoteAddress remoteAddress;

    @Override
    protected void addChannelHandlers(List<ChannelHandler> channelHandlers) {
        channelHandlers.add(new IdleStateHandler(0,0,5));
        channelHandlers.add(new LocalIdleStateHandler(this));
        channelHandlers.add(new ObjectEncoder());
        this.clientVerifyDecoder = new ClientVerifyDecoder();
        channelHandlers.add(this.clientVerifyDecoder);
    }


    public void doConnect(){
        if(remoteAddress!=null){
            this.doConnect(remoteAddress.getHost(),remoteAddress.getPort());
        }else {
            logger.error("remote address is null");
        }
    }

    protected void doConnect(String host, int port){
        if(remoteAddress==null){
            remoteAddress = new RemoteAddress(host,port);
        }
        if(channelFuture!=null
                &&channelFuture.channel() != null
                && channelFuture.channel().isActive()) {
            return;
        }
        this.channelFuture = this.connect(host,port).addListener(
                new ConnectResultListener(this,remoteAddress));
    }

    public void doWhenConnected(){
        this.start();
    }

    public void doWhenConnectFailed(RemoteAddress remoteAddress){
        group.schedule(new Runnable() {
            @Override
            public void run() {
                logger.info("重连连接...");
                doConnect(remoteAddress.getHost(),remoteAddress.getPort());
            }
        },10, TimeUnit.SECONDS);

    }

    protected void write (byte[] bytes){
        this.channelFuture.channel().writeAndFlush(bytes);
    }

    private void start() {
        byte [] bytes = new byte[3];
        bytes[0] = 0x01;
        bytes[1] = 0x01;
        bytes[2] = 0x02;
        this.write(bytes);
        this.clientVerifyDecoder.setVerifyStatus(ClientVerifyDecoder.WAIT_VERIFY_METHOD_STATUS);
    }

    private static class ConnectResultListener implements ChannelFutureListener{

        private final LocalConnector localConnector;

        private final RemoteAddress remoteAddress;

        public ConnectResultListener(LocalConnector localConnector, RemoteAddress remoteAddress) {
            this.localConnector = localConnector;
            this.remoteAddress = remoteAddress;
        }

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if(future.isSuccess()){
                logger.info("connect to {} successfully",future.channel().remoteAddress());
                localConnector.doWhenConnected();
            }else {
                logger.info("connect to [{}:{}] failed",remoteAddress.getHost(),remoteAddress.getPort());
                localConnector.doWhenConnectFailed(remoteAddress);
            }
        }


    }

}
