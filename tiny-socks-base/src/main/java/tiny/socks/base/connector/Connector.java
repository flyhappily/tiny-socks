package tiny.socks.base.connector;

import io.netty.channel.ChannelFuture;

/**
 * @author: pf_xu
 * @date: 2021/1/3 0:10
 * @Email:pfxuchn@gmail.com
 */
public interface Connector {

    ChannelFuture connect(String host, int port);

    void shutdown();

}
