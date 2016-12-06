package fpjk.nirvana.sdk.android.business;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Summary:
 * Created by Felix
 * Date: 02/12/2016
 * Time: 15:43
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class CookieList {

    @JSONField(name = "path")
    private String path;

    @JSONField(name = "value")
    private String value;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "domain")
    private String domain;

    public String getPath() {
        return path;
    }

    public CookieList setPath(String path) {
        this.path = path;
        return this;
    }

    public String getValue() {
        return value;
    }

    public CookieList setValue(String value) {
        this.value = value;
        return this;
    }

    public String getName() {
        return name;
    }

    public CookieList setName(String name) {
        this.name = name;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public CookieList setDomain(String domain) {
        this.domain = domain;
        return this;
    }

    @Override
    public String toString() {
        return "CookieList{" +
                "path='" + path + '\'' +
                ", value='" + value + '\'' +
                ", name='" + name + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }
}
