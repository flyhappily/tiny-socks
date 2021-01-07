package tiny.socks.base;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import tiny.socks.base.model.DataPacket;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/7 14:44
 * @Email:pfxuchn@gmail.com
 */
public class DataPacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        DataPacket dataPacket = new DataPacket(msg.readByte(),msg);
        msg.retain();
        out.add(dataPacket);
    }
}
