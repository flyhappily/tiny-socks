package tiny.socks.server.handler.verify;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 * 验证连接合法性
 * @author: pf_xu
 * @date: 2021/1/3 12:21
 * @Email:pfxuchn@gmail.com
 */
public class ServerVerifyDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(ServerVerifyDecoder.class);

    private static final int WAIT_VERIFY_STATUS = 0;

    private static final int VERIFYING_STATUS = 1;

    private static final int VERIFIED_STATUS = 2;

    private int verifyStatus;

    public ServerVerifyDecoder(){
        this.verifyStatus = WAIT_VERIFY_STATUS;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (verifyStatus){
            case WAIT_VERIFY_STATUS:
                if(!in.isReadable(2)){
                    return;
                }
                byte version = in.readByte();
                byte methodCount = in.readByte();
                if(in.readableBytes()<methodCount){
                    in.clear();
                    return;
                }
                byte[] methods = new byte[methodCount];
                in.readBytes(methods);
                byte[] resp = new byte[3];
                resp[0] = 0x01;
                resp[1] = 0x01;
                resp[2] = 0x01;
                //addListener(ChannelFutureListener.CLOSE)
                ctx.writeAndFlush(resp);
                logger.info("给客户端响应支持的验证方法");
                this.verifyStatus = VERIFYING_STATUS;
                break;
            case VERIFYING_STATUS:

            case VERIFIED_STATUS:

            default:

                break;
        }
    }
}
