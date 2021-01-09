package tiny.socks.server.server.connector.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * 将上游来的数据加密，并根据请求来源封装成数据包
 * @author: pf_xu
 * @date: 2021/1/8 15:49
 * @Email:pfxuchn@gmail.com
 */
public class HandlerByte extends MessageToMessageDecoder<ByteBuf> {


    private ChannelHandlerContext channelHandlerContext;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

    }
}
