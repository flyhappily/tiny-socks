package tiny.socks.base.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: pf_xu
 * @date: 2021/1/4 15:59
 * @Email:pfxuchn@gmail.com
 */
public class ObjectEncoder extends MessageToByteEncoder<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ObjectEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if(msg instanceof ByteBuf){
            out.writeBytes((ByteBuf)msg);
        }
        else if(msg instanceof byte[]){
            out.writeBytes((byte[]) msg);
        }
        else{
            logger.info("msg类型不匹配，type=[{}]",msg.getClass().getName());
        }
    }
}
