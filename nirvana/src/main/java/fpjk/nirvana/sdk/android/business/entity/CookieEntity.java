package fpjk.nirvana.sdk.android.business.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.List;

/**
 * Summary:
 * Created by Felix
 * Date: 02/12/2016
 * Time: 11:12
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class CookieEntity {

    @JSONField(name = "cookieList")
    private List<CookieList> cookieList = new ArrayList<CookieList>();

    /**
     * @return The cookieList
     */
    public List<CookieList> getCookieList() {
        return cookieList;
    }

    /**
     * @param cookieList The cookieList
     */
    public void setCookieList(List<CookieList> cookieList) {
        this.cookieList = cookieList;
    }
}
