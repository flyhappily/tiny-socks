package tiny.socks.client;

import io.netty.channel.ChannelHandler;
import tiny.socks.base.connector.AbstractConnector;
import tiny.socks.client.handler.verify.ClientVerifyDecoder;
import tiny.socks.base.encoder.ObjectEncoder;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/3 0:24
 * @Email:pfxuchn@gmail.com
 */
public class LocalConnector extends AbstractConnector {

    private ClientVerifyDecoder clientVerifyDecoder;

    protected LocalConnector(String host, int port) {
        super(host, port);
    }

    @Override
    protected void addChannelHandlers(List<ChannelHandler> channelHandlers) {
        channelHandlers.add(new ObjectEncoder());
        this.clientVerifyDecoder = new ClientVerifyDecoder();
        channelHandlers.add(this.clientVerifyDecoder);
    }

    @Override
    protected void initVerifyMessageSend() {
        byte [] bytes = new byte[3];
        bytes[0] = 0x01;
        bytes[1] = 0x01;
        bytes[2] = 0x02;
        this.write(bytes);
        this.clientVerifyDecoder.setVerifyStatus(ClientVerifyDecoder.WAIT_VERIFY_METHOD_STATUS);
    }

}
