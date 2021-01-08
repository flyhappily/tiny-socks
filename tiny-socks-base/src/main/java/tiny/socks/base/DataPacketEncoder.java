package tiny.socks.base;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import tiny.socks.base.model.DataPacket;

/**
 * @author: pf_xu
 * @date: 2021/1/7 15:33
 * @Email:pfxuchn@gmail.com
 */
public class DataPacketEncoder extends MessageToByteEncoder<DataPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, DataPacket msg, ByteBuf out) throws Exception {
        out.writeShort(msg.getDataContent().readableBytes()+1);
        out.writeByte(msg.getDataType());
        out.writeBytes(msg.getDataContent());
    }
}
