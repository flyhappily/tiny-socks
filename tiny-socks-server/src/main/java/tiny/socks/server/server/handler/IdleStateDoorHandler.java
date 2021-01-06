package tiny.socks.server.server.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: pf_xu
 * @date: 2021/1/5 10:52
 * @Email:pfxuchn@gmail.com
 */
public class IdleStateDoorHandler extends ChannelDuplexHandler {

    public static final String NAME = "idleStateDoorHandler";

    private static final Logger logger = LoggerFactory.getLogger(IdleStateDoorHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            logger.debug("捕获验证超时时间");
            //ctx.close();
            super.userEventTriggered(ctx, evt);
        }




    }
}
