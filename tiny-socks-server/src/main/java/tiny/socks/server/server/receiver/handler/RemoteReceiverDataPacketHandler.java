package tiny.socks.server.server.receiver.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.constant.DataAction;
import tiny.socks.base.constant.DataType;
import tiny.socks.base.model.DataPacket;
import tiny.socks.base.model.action.ConnectActionData;
import tiny.socks.base.receiver.exception.ReceiverHandleException;
import tiny.socks.server.server.receiver.RemoteReceiver;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/8 16:22
 * @Email:pfxuchn@gmail.com
 */
public class RemoteReceiverDataPacketHandler extends MessageToMessageDecoder<DataPacket> {

    private static final Logger logger = LoggerFactory.getLogger(RemoteReceiverDataPacketHandler.class);

    private final RemoteReceiver remoteReceiver;

    public RemoteReceiverDataPacketHandler(RemoteReceiver remoteReceiver) {
        this.remoteReceiver = remoteReceiver;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DataPacket msg, List<Object> out) throws Exception {
        if(msg.getDataType()== DataType.COMMON){
            //读取所有信息
            ByteBuf in = msg.getDataContent();
            byte cmd = in.readByte();
            switch (cmd){
                case DataAction.CMD_RELAY:
                    in.release();
                    break;
                //连接指令
                case DataAction.CMD_CONNECT:
                    try {
                        this.remoteReceiver.connectRemote(ctx.channel(),parseConnectActionData(in));
                    }
                    catch (ReceiverHandleException e){
                        logger.error("解析connect指令错误,e:{}",e.getMessage());
                    }
                    finally {
                        in.release();
                    }
                    break;
                default:
                    in.release();
                    fail(ctx);
                    break;
            }

        }
        else{
            out.add(msg);
        }
    }

    private void fail(ChannelHandlerContext context){
        context.close();
    }

    private ConnectActionData parseConnectActionData(ByteBuf in) throws ReceiverHandleException {
        ConnectActionData connectActionData = new ConnectActionData();
        if(in.readableBytes()<2){
            throw new ReceiverHandleException("can not read LOCAL PORT");
        }
        int localPort = in.readShort();
        if(!in.isReadable()){
            throw new ReceiverHandleException("can not read TARGET TYPE");
        }
        byte targetType = in.readByte();
        switch (targetType){
            case 0x01:
                break;
            case 0x02:
                break;
            case 0x03:
                break;
            default:
               throw new ReceiverHandleException("unknown targetType");
        }


        return connectActionData;
    }

}
