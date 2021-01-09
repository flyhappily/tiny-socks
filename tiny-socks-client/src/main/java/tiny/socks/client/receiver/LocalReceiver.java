package tiny.socks.client.receiver;

import io.netty.channel.ChannelHandler;
import tiny.socks.base.BaseChannelInitializer;
import tiny.socks.base.receiver.AbstractReceiver;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/9 12:03
 * @Email:pfxuchn@gmail.com
 */
public class LocalReceiver extends AbstractReceiver {


    @Override
    protected BaseChannelInitializer getBaseChannelInitializer() {
        return new BaseChannelInitializer(this){

            private LocalReceiver localReceiver;

            @Override
            protected void loadChannelHandlers(List<ChannelHandler> channelHandlers) {

            }
        };
    }
}
