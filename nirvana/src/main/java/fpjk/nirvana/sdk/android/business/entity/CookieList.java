package fpjk.nirvana.sdk.android.business.entity;

import java.io.Serializable;

/**
 * Summary:
 * Created by Felix
 * Date: 02/12/2016
 * Time: 15:43
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class CookieList implements Serializable {

    private String path;

    private String value;

    private String name;

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
