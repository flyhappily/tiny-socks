package tiny.socks.server.server.receiver;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import tiny.socks.base.model.DataPacket;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/8 16:22
 * @Email:pfxuchn@gmail.com
 */
public class DataPacketHandler extends MessageToMessageDecoder<DataPacket> {

    @Override
    protected void decode(ChannelHandlerContext ctx, DataPacket msg, List<Object> out) throws Exception {
        if(msg.getDataType()==DataPacket.DataType.COMMON){

        }
        else{
            out.add(msg);
        }
    }
}
