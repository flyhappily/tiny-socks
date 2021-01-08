package tiny.socks.server.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tiny.socks.server.server.receiver.RemoteReceiver;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: pf_xu
 * @date: 2021/1/8 10:03
 * @Email:pfxuchn@gmail.com
 */
public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final RemoteReceiver remoteReceiver;

    private ConcurrentHashMap<ChannelId, ChannelFuture> remoteChannelFutures;

    public Server() {
        remoteReceiver = new RemoteReceiver(this);
    }

    public void registerChannelFuture(ChannelId channelId, ChannelFuture channelFuture){
        remoteChannelFutures.put(channelId,channelFuture);
    }

    public void removeChannelFuture(ChannelId channelId){
        remoteChannelFutures.remove(channelId);
    }

    public ChannelFuture getChannelFuture(ChannelId channelId){
        return remoteChannelFutures.get(channelId);
    }

    public void start(){
        remoteReceiver.start();
    }


}
