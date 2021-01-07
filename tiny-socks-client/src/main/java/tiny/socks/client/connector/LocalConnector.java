package tiny.socks.client.connector;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.DataPacketDecoder;
import tiny.socks.base.DataPacketEncoder;
import tiny.socks.base.UnpackDecoder;
import tiny.socks.base.connector.AbstractConnector;
import tiny.socks.base.ObjectEncoder;
import tiny.socks.base.model.DataPacket;
import tiny.socks.base.model.RemoteAddress;
import tiny.socks.client.connector.handler.LocalIdleStateHandler;
import tiny.socks.client.connector.handler.verify.LocalConnectorVerifyDecoder;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: pf_xu
 * @date: 2021/1/3 0:24
 * @Email:pfxuchn@gmail.com
 */
public class LocalConnector extends AbstractConnector {

    private static final Logger logger = LoggerFactory.getLogger(LocalConnector.class);

    private ChannelFuture channelFuture;

    private final RemoteAddress remoteAddress;

    public LocalConnector(String host,int port){
        remoteAddress = new RemoteAddress(host,port);
    }


    @Override
    protected void addChannelHandlers(List<ChannelHandler> channelHandlers) {
//        channelHandlers.add(new IdleStateHandler(0,0,5));
//        channelHandlers.add(new LocalIdleStateHandler(this));
        channelHandlers.add(new DataPacketEncoder());
        channelHandlers.add(new UnpackDecoder());
        channelHandlers.add(new DataPacketDecoder());
        channelHandlers.add(new LocalConnectorVerifyDecoder());
    }

    public void doConnect(){
        this.doConnect(remoteAddress.getHost(),remoteAddress.getPort());
    }

    public void doConnect(String host, int port){
        if(channelFuture!=null
                &&channelFuture.channel() != null
                && channelFuture.channel().isActive()) {
            return;
        }
        this.channelFuture = this.connect(host,port).addListener(
                new ConnectResultListener(this,remoteAddress));
    }

    public void doWhenConnected(){
        byte [] bytes = new byte[3];
        bytes[0] = 0x01;
        bytes[1] = 0x01;
        bytes[2] = 0x02;
        this.write((byte) 0x02,bytes);
        LocalConnectorVerifyDecoder localConnectorVerifyDecoder = (LocalConnectorVerifyDecoder)channelFuture.channel().pipeline().get(LocalConnectorVerifyDecoder.NAME);
        localConnectorVerifyDecoder.setVerifyStatus(LocalConnectorVerifyDecoder.WAIT_VERIFY_METHOD_STATUS);
    }

    public void doWhenConnectFailed(){
        group.schedule(new Runnable() {
            @Override
            public void run() {
                logger.info("重连连接...");
                doConnect(remoteAddress.getHost(),remoteAddress.getPort());
            }
        },5, TimeUnit.SECONDS);

    }

    public void write (byte dataType,byte[] bytes){
        DataPacket dataPacket = new DataPacket(dataType,Unpooled.copiedBuffer(bytes));
        this.channelFuture.channel().writeAndFlush(dataPacket);
    }

    public void write (byte[] bytes){
       this.write((byte) 0x04,bytes);
    }

    public void start() {
        doConnect();
    }

    public boolean isTunnelActive(){
        if(channelFuture!=null && channelFuture.channel() != null && channelFuture.channel().isActive()){
            LocalConnectorVerifyDecoder localConnectorVerifyDecoder = (LocalConnectorVerifyDecoder)channelFuture.channel().pipeline().get(LocalConnectorVerifyDecoder.NAME);
            if(localConnectorVerifyDecoder.getVerifyStatus()==LocalConnectorVerifyDecoder.VERIFIED_STATUS){
                return true;
            }
        }
        return false;
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
                logger.info("failed to connect to [{}:{}] ",remoteAddress.getHost(),remoteAddress.getPort());
                localConnector.doWhenConnectFailed();
            }
        }


    }

}
