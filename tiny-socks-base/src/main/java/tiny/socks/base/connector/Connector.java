package tiny.socks.base.connector;

/**
 * @author: pf_xu
 * @date: 2021/1/3 0:10
 * @Email:pfxuchn@gmail.com
 */
public interface Connector {

    void connect();

    void write(byte[] bytes);

    void shutdown();

}
