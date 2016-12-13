package fpjk.nirvana.sdk.android.business;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Summary:
 * Created by Felix
 * Date: 07/12/2016
 * Time: 11:30
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class OpenUrlResponse {

    @JSONField(name = "success")
    private Integer success;

    /**
     * @return The success
     */
    public Integer getSuccess() {
        return success;
    }

    /**
     * @param success The success
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "OpenUrlResponse{" +
                "success=" + success +
                '}';
    }
}