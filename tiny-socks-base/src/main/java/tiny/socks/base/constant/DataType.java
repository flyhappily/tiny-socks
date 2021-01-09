package tiny.socks.base.constant;

/**
 * @author: pf_xu
 * @date: 2021/1/9 10:52
 * @Email:pfxuchn@gmail.com
 */
public class DataType {

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
