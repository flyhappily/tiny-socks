package tiny.socks.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import tiny.socks.base.connector.exception.ConnectorException;
import tiny.socks.base.model.DataPacket;
import tiny.socks.client.connector.LocalConnector;

import java.nio.charset.StandardCharsets;

/**
 * @author: pf_xu
 * @date: 2021/1/4 14:28
 * @Email:pfxuchn@gmail.com
 */
public class ClientTest {

    public static void main(String[] args) throws ConnectorException {
        LocalConnector localConnector = new LocalConnector("127.0.0.1",7979);
        localConnector.start();
        while (true){
            if(localConnector.isTunnelActive()){
                byte [] bytes = {0x01};
                localConnector.write((byte) 0x01,bytes);
                break;
            }
        }



    }

}
