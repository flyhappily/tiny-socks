package tiny.socks.server.server.receiver.handler.verify;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.constant.DataType;
import tiny.socks.base.model.DataPacket;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 *
 * 验证连接合法性
 * @author: pf_xu
 * @date: 2021/1/3 12:21
 * @Email:pfxuchn@gmail.com
 */
public class RemoteServerVerifyDecoder extends MessageToMessageDecoder<DataPacket> {

    public static final String NAME = "remoteServerVerifyDecoder";

    private static final Logger logger = LoggerFactory.getLogger(RemoteServerVerifyDecoder.class);

    private static final int WAIT_VERIFY_STATUS = 0;

    private static final int VERIFYING_STATUS = 1;

    private static final int VERIFIED_STATUS = 2;

    private int verifyStatus;

    public RemoteServerVerifyDecoder(){
        this.verifyStatus = WAIT_VERIFY_STATUS;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DataPacket msg, List<Object> out) throws Exception {

        if(this.verifyStatus!=VERIFIED_STATUS && msg.getDataType()!=DataType.VERIFYING){
            this.fail(ctx,"未验证通过，就接收到数据包");
            return;
        }

        if(msg.getDataType() == DataType.VERIFYING){

            ByteBuf in = msg.getDataContent();

            switch (verifyStatus) {
                case WAIT_VERIFY_STATUS:
                    this.doWhenWaitVerifyStatus(ctx,in);
                    break;
                case VERIFYING_STATUS:
                    this.doWhenVerifyingStatus(ctx,in);
                    break;
                case VERIFIED_STATUS:
                    logger.info("验证通过，仍然收到验证数据包，丢弃之");
                    this.releaseByteBuf(in);
                    break;
                default:
                    this.fail(ctx,"未匹配到合适的验证状态");
                    break;
            }

        }else {
            logger.debug("RemoteServerVerifyDecoder交由下游处理,dataType:{}",msg.getDataType());
            out.add(msg);
        }
    }

    private void doWhenWaitVerifyStatus(ChannelHandlerContext ctx, ByteBuf in){
        byte version = in.readByte();
        byte methodCount = in.readByte();
        if (in.readableBytes() < methodCount) {
            this.fail(ctx,"验证方法数量不对");
            return;
        }
        byte[] methods = new byte[methodCount];
        in.readBytes(methods);
        ByteBuf resp = Unpooled.buffer(3);
        resp.writeByte(0x01);
        resp.writeByte(0x01);
        resp.writeByte(0x01);
        ctx.writeAndFlush(new DataPacket(DataType.VERIFYING,resp));
        logger.debug("给客户端响应支持的验证方法,version={},resp={}", version, resp);
        this.verifyStatus = VERIFYING_STATUS;
        this.releaseByteBuf(in);
    }

    private void doWhenVerifyingStatus(ChannelHandlerContext ctx, ByteBuf in){
        byte version = in.readByte();
        byte method = in.readByte();
        int length = in.readShort();
        if (in.readableBytes() < length) {
            this.fail(ctx,"验证数据有误");
            return;
        }
        byte[] dataBytes = new byte[length];
        in.readBytes(dataBytes);
        String authInfo = new String(dataBytes, StandardCharsets.US_ASCII);
        logger.debug("读取到客户端发送的验证信息，version={},method={},authInfo={}"
                , version, method, authInfo);
        //发送验证结果
        ByteBuf result = Unpooled.buffer(2);
        result.writeByte(0x01);
        //校验成功
        result.writeByte(0x01);
        logger.debug("给客户端发送验证结果，result={}", result);
        ctx.writeAndFlush(new DataPacket(DataType.VERIFYING,result));
        this.verifyStatus = VERIFIED_STATUS;
        logger.info("verified, begin to transmit data remoteAddress:{},localAddress:{}"
                , ctx.channel().remoteAddress(), ctx.channel().localAddress());
        this.releaseByteBuf(in);
    }



    private void fail(ChannelHandlerContext channelHandlerContext, String message){
        logger.error("{}，关闭channel",message);
        channelHandlerContext.close();
    }

    private void releaseByteBuf(ByteBuf byteBuf){
        ReferenceCountUtil.release(byteBuf);
    }
//    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        byte version;
//        byte methodCount;
//        switch (verifyStatus){
//            case WAIT_VERIFY_STATUS:
//                if(!in.isReadable(2)){
//                    return;
//                }
//                version = in.readByte();
//                methodCount = in.readByte();
//                if(in.readableBytes()<methodCount){
//                    in.clear();
//                    return;
//                }
//                byte[] methods = new byte[methodCount];
//                in.readBytes(methods);
//                byte[] resp = new byte[3];
//                resp[0] = 0x01;
//                resp[1] = 0x01;
//                resp[2] = 0x01;
//                ctx.writeAndFlush(resp);
//                logger.debug("给客户端响应支持的验证方法,version={},resp={}",version,resp);
//                this.verifyStatus = VERIFYING_STATUS;
//                break;
//            case VERIFYING_STATUS:
//                if(!in.isReadable(4)){
//                    return;
//                }
//                version = in.readByte();
//                byte method = in.readByte();
////                byte [] lengthBytes = new byte[2];
////                in.readBytes(lengthBytes);
////                short length = NumberUtil.bytesToShort(lengthBytes);
//                int length = in.readShort();
//                if(in.readableBytes()<length){
//                    in.clear();
//                    return;
//                }
//                byte[] dataBytes = new byte[length];
//                in.readBytes(dataBytes);
//                String authInfo = new String(dataBytes, StandardCharsets.US_ASCII);
//                logger.debug("读取到客户端发送的验证信息，version={},method={},authInfo={}"
//                        ,version,method,authInfo);
//                //发送验证结果
//                byte [] result = new byte[2];
//                result[0] = 0x01;
//                //校验成功
//
//                result[1] = 0x01;
//                logger.debug("给客户端发送验证结果，result={}",result);
//                ctx.writeAndFlush(result);
//                this.verifyStatus = VERIFIED_STATUS;
//                logger.info("verified, begin to transmit data remoteAddress:{},localAddress:{}"
//                        ,ctx.channel().remoteAddress(),ctx.channel().localAddress());
//                return;
//            case VERIFIED_STATUS:
//                out.add(in);
//                break;
//            default:
//                logger.error("验证失败，关闭channel");
//                ctx.close();
//                break;
//        }
//    }
}
