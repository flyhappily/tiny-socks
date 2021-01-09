package tiny.socks.base;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 分割字节流
 * @author: pf_xu
 * @date: 2021/1/7 13:36
 * @Email:pfxuchn@gmail.com
 */
public class UnpackDecoder extends LengthFieldBasedFrameDecoder {

    public UnpackDecoder() {
        super(2+Short.MAX_VALUE, 0, 2,0,2);
    }
}
