package tiny.socks.base.utils;

/**
 * @author: pf_xu
 * @date: 2021/1/4 22:27
 * @Email:pfxuchn@gmail.com
 */
public class NumberUtil {

    public static byte[] shortToBytes(short a){
        byte[] bytes = new byte[2];
        bytes[0]=(byte)(a>>8&0x00ff);
        bytes[1]=(byte)(a&0x00ff);
        return bytes;
    }


    public static short bytesToShort(byte [] bytes){
        if(bytes==null||bytes.length!=2){
            throw new IllegalArgumentException("the length of byte array should be two");
        }
        return (short) (bytes[0]<<8|bytes[1]&0x00ff);
    }

}
