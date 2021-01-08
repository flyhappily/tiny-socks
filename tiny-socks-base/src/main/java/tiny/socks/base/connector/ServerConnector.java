package tiny.socks.base.connector;

import io.netty.channel.ChannelHandler;
import tiny.socks.base.BaseChannelInitializer;

import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/3 0:29
 * @Email:pfxuchn@gmail.com
 */
public class ServerConnector extends AbstractConnector{


    public void start() {

    }


    @Override
    protected BaseChannelInitializer getBaseChannelInitializer() {
        return null;
    }
}
