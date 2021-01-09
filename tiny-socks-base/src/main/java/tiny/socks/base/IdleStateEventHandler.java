package tiny.socks.base;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.base.model.DataPacket;

/**
 * @author: pf_xu
 * @date: 2021/1/5 10:52
 * @Email:pfxuchn@gmail.com
 */
public class IdleStateEventHandler extends ChannelDuplexHandler {

    public static final String NAME = "idleStateEventHandler";

    private static final Logger logger = LoggerFactory.getLogger(IdleStateEventHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state().equals(IdleState.ALL_IDLE)){
                logger.debug("ALL_IDLE 事件，发心跳包......");
                this.ping(ctx);
            }

        }else {
            super.userEventTriggered(ctx, evt);
        }




    }

    private void ping(ChannelHandlerContext channelHandlerContext){
        ByteBuf byteBuf = Unpooled.buffer(1);
        byteBuf.writeByte(0x01);
        channelHandlerContext.writeAndFlush(new DataPacket((byte) 0x01,byteBuf));
    }
}
