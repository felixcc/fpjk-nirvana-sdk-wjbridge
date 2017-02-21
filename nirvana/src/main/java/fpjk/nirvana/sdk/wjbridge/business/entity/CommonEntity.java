package fpjk.nirvana.sdk.wjbridge.business.entity;

/**
 * Summary:
 * Created by FelixChen
 * Created 2017-02-21 14:35
 * Mail:lovejiuwei@gmail.com
 * QQ:74104
 */

public class CommonEntity {
    private String pid = "";
    private String version = "";
    private String os = "android";

    public String getPid() {
        return pid;
    }

    public CommonEntity setPid(String pid) {
        this.pid = pid;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public CommonEntity setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getOs() {
        return os;
    }

    public CommonEntity setOs(String os) {
        this.os = os;
        return this;
    }

    @Override
    public String toString() {
        return "CommonEntity{" +
                "pid='" + pid + '\'' +
                ", version='" + version + '\'' +
                ", os='" + os + '\'' +
                '}';
    }
}
