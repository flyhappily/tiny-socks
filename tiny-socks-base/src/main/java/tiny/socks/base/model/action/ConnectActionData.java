package tiny.socks.base.model.action;

/**
 * @author: pf_xu
 * @date: 2021/1/9 11:22
 * @Email:pfxuchn@gmail.com
 */
public class ConnectActionData {

    /**
     * 来源端口
     */
    private short localPort;


    /**
     * 目标域名
     */
    private String domain;

    /**
     * ipV4
     */
    private String ipV4;

    /**
     * ipV6
     */
    private String ipV6;

    public void setLocalPort(short localPort) {
        this.localPort = localPort;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setIpV4(String ipV4) {
        this.ipV4 = ipV4;
    }

    public void setIpV6(String ipV6) {
        this.ipV6 = ipV6;
    }

    public short getLocalPort() {
        return localPort;
    }

    public String getDomain() {
        return domain;
    }

    public String getIpV4() {
        return ipV4;
    }

    public String getIpV6() {
        return ipV6;
    }
}
