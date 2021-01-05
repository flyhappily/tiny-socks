package tiny.socks.server.handler.verify;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.utils.NumberUtil;

import java.nio.charset.StandardCharsets;
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
        byte version;
        byte methodCount;
        switch (verifyStatus){
            case WAIT_VERIFY_STATUS:
                if(!in.isReadable(2)){
                    return;
                }
                version = in.readByte();
                methodCount = in.readByte();
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
                logger.info("给客户端响应支持的验证方法,version={},resp={}",version,resp);
                this.verifyStatus = VERIFYING_STATUS;
                break;
            case VERIFYING_STATUS:
                if(!in.isReadable(4)){
                    return;
                }
                version = in.readByte();
                byte method = in.readByte();
                byte [] lengthBytes = new byte[2];
                short length = NumberUtil.bytesToShort(lengthBytes);
                if(in.readableBytes()<length){
                    in.clear();
                    return;
                }
                byte[] dataBytes = new byte[length];
                in.readBytes(length);
                String authInfo = new String(dataBytes, StandardCharsets.US_ASCII);
                logger.info("读取到客户端发送的验证信息，version={},method={},authInfo={}"
                        ,version,method,authInfo);
                //发送验证结果
                byte [] result = new byte[2];
                result[0] = 0x01;
                result[1] = 0x01;
                logger.info("给客户端发送验证结果，result={}",result);
                ctx.writeAndFlush(result);
                this.verifyStatus = VERIFIED_STATUS;
                return;
            case VERIFIED_STATUS:
                out.add(in);
                break;
            default:
                break;
        }
    }
}
