package tiny.socks.client.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.client.LocalConnector;

/**
 * @author: pf_xu
 * @date: 2021/1/6 11:54
 * @Email:pfxuchn@gmail.com
 */
public class LocalIdleStateHandler extends ChannelDuplexHandler {

    private final LocalConnector localConnector;

    private static final Logger logger = LoggerFactory.getLogger(LocalIdleStateHandler.class);

    public LocalIdleStateHandler(LocalConnector localConnector) {
        this.localConnector = localConnector;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("连接断开了,重新连接...");
        localConnector.doConnect();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            logger.debug("localConnector,捕获验证超时事件");
            //ctx.close();
            super.userEventTriggered(ctx, evt);
        }

    }
}
