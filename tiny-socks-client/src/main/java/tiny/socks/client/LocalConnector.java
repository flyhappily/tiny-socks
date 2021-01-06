package tiny.socks.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.IdleStateHandler;
import tiny.socks.base.connector.AbstractConnector;
import tiny.socks.base.connector.exception.ConnectorException;
import tiny.socks.base.encoder.ObjectEncoder;
import tiny.socks.client.handler.LocalIdleStateHandler;
import tiny.socks.client.handler.verify.ClientVerifyDecoder;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/3 0:24
 * @Email:pfxuchn@gmail.com
 */
public class LocalConnector extends AbstractConnector {

    private ClientVerifyDecoder clientVerifyDecoder;

    private ChannelFuture channelFuture;

    @Override
    protected void addChannelHandlers(List<ChannelHandler> channelHandlers) {
        channelHandlers.add(new IdleStateHandler(8,6,0));
        channelHandlers.add(new LocalIdleStateHandler());
        channelHandlers.add(new ObjectEncoder());
        this.clientVerifyDecoder = new ClientVerifyDecoder();
        channelHandlers.add(this.clientVerifyDecoder);
    }

    public void doConnect(String host, int port) throws ConnectorException {
        ChannelFuture future = this.connect(host,port);
        if(future == null){
            throw new ConnectorException("connect host:port ["+host+":"+port+"] failed");
        }
        this.channelFuture = future;
    }

    protected void write (byte[] bytes){
        this.channelFuture.channel().writeAndFlush(bytes);
    }

    protected void start() {
        byte [] bytes = new byte[3];
        bytes[0] = 0x01;
        bytes[1] = 0x01;
        bytes[2] = 0x02;
        this.write(bytes);
        this.clientVerifyDecoder.setVerifyStatus(ClientVerifyDecoder.WAIT_VERIFY_METHOD_STATUS);
    }

}
