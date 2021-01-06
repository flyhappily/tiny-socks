package tiny.socks.client.connector.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.client.connector.LocalConnector;
import tiny.socks.client.connector.handler.verify.LocalConnectorVerifyDecoder;

/**
 * @author: pf_xu
 * @date: 2021/1/6 11:54
 * @Email:pfxuchn@gmail.com
 */
public class LocalIdleStateHandler extends ChannelDuplexHandler {

    public static final String NAME = "localIdleStateHandler";

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
            logger.debug("localConnector,捕获通道超时事件");
            LocalConnectorVerifyDecoder localConnectorVerifyDecoder = ctx.pipeline().get(LocalConnectorVerifyDecoder.class);
            if(localConnectorVerifyDecoder.getVerifyStatus()!=LocalConnectorVerifyDecoder.VERIFIED_STATUS){
                logger.error("验证异常，关闭通道");
                ctx.close();
            }
        }
        else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("捕获到异常,{}",cause.getMessage());
    }
}
