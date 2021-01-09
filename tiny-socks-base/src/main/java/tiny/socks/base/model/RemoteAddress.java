package tiny.socks.base.model;

/**
 * @author: pf_xu
 * @date: 2021/1/6 16:25
 * @Email:pfxuchn@gmail.com
 */
public class RemoteAddress {

    private String host;

    private int port;

    public RemoteAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
