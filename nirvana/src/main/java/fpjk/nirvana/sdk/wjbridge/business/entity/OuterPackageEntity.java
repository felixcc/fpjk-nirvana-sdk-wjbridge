package fpjk.nirvana.sdk.wjbridge.business.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Summary:
 * Created by FelixChen
 * Created 2017-02-21 12:00
 * Mail:lovejiuwei@gmail.com
 * QQ:74104
 */
public class OuterPackageEntity {
    @JSONField(name = "common")
    private CommonEntity common;

    @JSONField(name = "body")
    private Object body = new Object();

    public CommonEntity getCommon() {
        return common;
    }

    public OuterPackageEntity setCommon(CommonEntity common) {
        this.common = common;
        return this;
    }

    public Object getBody() {
        return body;
    }

    public OuterPackageEntity setBody(Object body) {
        this.body = body;
        return this;
    }

    @Override
    public String toString() {
        return "OuterPackageEntity{" +
                "common=" + common +
                ", body=" + body +
                '}';
    }
}
