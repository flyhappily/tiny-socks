package tiny.socks.server.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/7 10:21
 * @Email:pfxuchn@gmail.com
 */
public class DataPacketHandler extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(DataPacketHandler.class);


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        logger.info("数据包长度为:{}",in.readableBytes());
    }
}
