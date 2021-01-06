package tiny.socks.base.model;

import io.netty.buffer.ByteBuf;

/**
 * @author: pf_xu
 * @date: 2021/1/3 16:05
 * @Email:pfxuchn@gmail.com
 */
public interface Data {

    void encodeAsByteBuf(ByteBuf byteBuf);
}
