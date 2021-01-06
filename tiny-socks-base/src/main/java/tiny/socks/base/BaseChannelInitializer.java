package tiny.socks.base;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author: pf_xu
 * @date: 2021/1/6 19:45
 * @Email:pfxuchn@gmail.com
 */
public class BaseChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    public static final String FIELD_NAME = "NAME";

    private static final Logger logger = LoggerFactory.getLogger(BaseChannelInitializer.class);

    private final List<ChannelHandler> channelHandlers;

    public BaseChannelInitializer(List<ChannelHandler> channelHandlers){
        this.channelHandlers = channelHandlers;
    }

    @Override
    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
        for (ChannelHandler channelHandler:this.channelHandlers){
            String name = null;
            try {
                Field field = channelHandler.getClass().getDeclaredField(FIELD_NAME);
                name = (String)field.get(channelHandler);
                if(name==null){
                    throw new NullPointerException("name is null");
                }
                nioSocketChannel.pipeline().addLast(name,channelHandler);
                logger.debug("使用name属性注册handler,name={},handler={}",name,channelHandler.getClass().getName());
            }catch (Exception e){
                nioSocketChannel.pipeline().addLast(channelHandler);
                logger.debug("获取handler name属性异常，{}",e.toString());
                logger.debug("使用默认handler注册方法，handler {}",channelHandler.getClass().getName());
            }
        }
    }
}
