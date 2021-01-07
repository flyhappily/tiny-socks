package tiny.socks.base.model;

import io.netty.buffer.ByteBuf;

/**
 * +--------+----------------+
 *   | data type | Actual Content |
 *   | 1 byte | bytes |
 * +--------+----------------+
 * @author: pf_xu
 * @date: 2021/1/3 15:52
 * @Email:pfxuchn@gmail.com
 **/

public class DataPacket {

    private final byte dataType;

    private final ByteBuf body;

    public DataPacket(byte dataType, ByteBuf body) {
        this.dataType = dataType;
        this.body = body;
    }

    public byte getDataType() {
        return dataType;
    }

    public ByteBuf getBody() {
        return body;
    }

    public static class DataType{

        private DataType(){

        }

        /**
         * 验证中
         */
        public static final byte VERIFYING = 0x02;
    }

}
