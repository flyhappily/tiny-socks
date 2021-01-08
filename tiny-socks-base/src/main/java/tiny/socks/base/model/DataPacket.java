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

    private final ByteBuf dataContent;

    public DataPacket(byte dataType, ByteBuf dataContent) {
        this.dataType = dataType;
        this.dataContent = dataContent;
    }

    public byte getDataType() {
        return dataType;
    }

    public ByteBuf getDataContent() {
        return dataContent;
    }

    public static class DataType{

        private DataType(){

        }

        /**
         * 心跳数据
         */
        public static final byte IDLE = 0x01;

        /**
         * 验证数据
         */
        public static final byte VERIFYING = 0x02;

        /**
         * 通用数据
         */
        public static final byte COMMON = 0x03;


    }

}
