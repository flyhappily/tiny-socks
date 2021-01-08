package tiny.socks.base;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.model.DataPacket;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/8 10:28
 * @Email:pfxuchn@gmail.com
 */
public class IdleStatePacketHandler extends MessageToMessageDecoder<DataPacket> {

    private static final Logger logger = LoggerFactory.getLogger(IdleStatePacketHandler.class);
    
    @Override
    protected void decode(ChannelHandlerContext ctx, DataPacket msg, List<Object> out) throws Exception {
        if(msg.getDataType()==DataPacket.DataType.IDLE) {
            logger.debug("收到心跳包，不处理，释放资源。。。。。。。。");
            msg.getDataContent().release();
        }
        else {
            out.add(msg);
        }
    }
}
