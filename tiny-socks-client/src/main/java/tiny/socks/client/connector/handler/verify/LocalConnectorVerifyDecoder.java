package tiny.socks.client.connector.handler.verify;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.utils.NumberUtil;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/3 12:37
 * @Email:pfxuchn@gmail.com
 */
public class LocalConnectorVerifyDecoder extends ByteToMessageDecoder {

    public static final String NAME = "localConnectorVerifyDecoder";

    private static final Logger logger = LoggerFactory.getLogger(LocalConnectorVerifyDecoder.class);

    public static final int WAIT_VERIFY_STATUS = 0;

    /**
     * 等待服务器通知支持的验证方法
     */
    public static final int WAIT_VERIFY_METHOD_STATUS = 1;

    public static final int VERIFYING_STATUS = 2;

    public static final int VERIFIED_STATUS = 3;

    private int verifyStatus;

    private final String userName = "xu";

    private final String password = "123";


    public LocalConnectorVerifyDecoder(){
        this.verifyStatus = WAIT_VERIFY_STATUS;
    }

    public void setVerifyStatus(int verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public int getVerifyStatus() {
        return verifyStatus;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        switch (verifyStatus){
            case WAIT_VERIFY_STATUS:
                logger.error("初始状态不对，还没有给服务器发送信息，就已经收到服务器的数据了");
                logger.error("错误数据为：{}",in.readByte());
                break;
            case WAIT_VERIFY_METHOD_STATUS:
                if(in.readableBytes()<3){
                    return;
                }
                byte version = in.readByte();
                byte methodCount = in.readByte();
                if(in.readableBytes()<methodCount){
                    in.clear();
                    return;
                }
                byte [] methods = new byte[methodCount];
                in.readBytes(methods);
                logger.debug("服务端响应验证方法，版本号：{}，验证方法数量：{}，methods:{}",version,methodCount,methods);
                //TODO 选择合适方法
                //用户名密码验证方式
                if(checkUserAndPasswordMethod(methods)){
                    byte [] authInfo = (userName +"\n"+ password).getBytes(StandardCharsets.US_ASCII.name());
                    ByteBuf byteBuf = Unpooled.buffer(4+authInfo.length);
                    byteBuf.writeByte(0x01);
                    byteBuf.writeByte(0x01);
                    if(authInfo.length<=Short.MAX_VALUE){
                        byteBuf.writeBytes(NumberUtil.shortToBytes((short) authInfo.length));
                        byteBuf.writeBytes(authInfo);
                        ctx.writeAndFlush(byteBuf);
                        this.verifyStatus = VERIFYING_STATUS;
                        return;
                    }
                }
                break;
            case VERIFYING_STATUS:
                if(in.readableBytes()<2){
                    return;
                }
                version = in.readByte();
                byte result = in.readByte();
                logger.debug("服务器验证结果为：version={},result={}",version,result);
                if(result==0x01){
                    verifyStatus = VERIFIED_STATUS;
                    logger.info("verified, begin to transmit data remoteAddress:{},localAddress:{}"
                            ,ctx.channel().remoteAddress(),ctx.channel().localAddress());
                    return;
                }
                break;
            case VERIFIED_STATUS:
                out.add(in);
                break;
            default:
                break;
        }
        in.skipBytes(in.readableBytes());
        logger.error("验证过程出现异常，关闭通道");
        ctx.close();
    }

    private boolean checkUserAndPasswordMethod(byte [] methods){
        for(byte method : methods){
            if(method==0x01){
                return true;
            }
        }
        return false;
    }


}
