package tiny.socks.server.server.connector.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/8 15:09
 * @Email:pfxuchn@gmail.com
 */
public class ByteIn extends ByteToMessageDecoder {

    private static final int MAX_LENGTH = 1024;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.isReadable()){
            if(in.readableBytes()>MAX_LENGTH){
                ByteBuf byteBuf = Unpooled.buffer(MAX_LENGTH);
                byteBuf.writeBytes(in);
                out.add(byteBuf);
            }
            else {
                ByteBuf byteBuf = Unpooled.buffer(in.readableBytes());
                byteBuf.writeBytes(in);
                out.add(byteBuf);
            }
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
