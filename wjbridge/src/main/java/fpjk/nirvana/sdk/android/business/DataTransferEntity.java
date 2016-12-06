package fpjk.nirvana.sdk.android.business;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Summary:json里面的data层
 * Created by Felix
 * Date: 01/12/2016
 * Time: 18:59
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class DataTransferEntity {
    @JSONField(name = "url")
    private String url = "";

    public String getUrl() {
        return url;
    }

    public DataTransferEntity setUrl(String url) {
        this.url = url;
        return this;
    }
}
