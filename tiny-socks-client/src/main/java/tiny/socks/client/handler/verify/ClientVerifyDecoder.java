package tiny.socks.client.handler.verify;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/3 12:37
 * @Email:pfxuchn@gmail.com
 */
public class ClientVerifyDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(ClientVerifyDecoder.class);

    public static final int WAIT_VERIFY_STATUS = 0;

    /**
     * 等待服务器通知支持的验证方法
     */
    public static final int WAIT_VERIFY_METHOD_STATUS = 1;

    public static final int VERIFYING_STATUS = 2;

    public static final int VERIFIED_STATUS = 3;

    private int verifyStatus;

    public ClientVerifyDecoder(){
        this.verifyStatus = WAIT_VERIFY_STATUS;
    }

    public void setVerifyStatus(int verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (verifyStatus){
            case WAIT_VERIFY_STATUS:
                logger.error("初始状态不对，还没有给服务器发送信息，就已经收到服务器的数据了");
                logger.error("错误数据为：{}",in.readByte());
                break;
            case WAIT_VERIFY_METHOD_STATUS:
                if(in.isReadable(2)){
                    byte version = in.readByte();
                    byte methodCount = in.readByte();
                    byte [] methods = new byte[methodCount];
                    in.readBytes(methods);
                    logger.debug("版本号：{}，验证方法数量：{}",version,methodCount);
                }
                return;
            default:
                break;
        }
        ctx.close();
    }
}
