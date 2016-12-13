package fpjk.nirvana.sdk.android.business.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Summary:与JS交互时传输数据的模板。
 * Created by Felix
 * Date: 01/12/2016
 * Time: 16:48
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class ProcessBusinessEntity {
    @JSONField(name = "opt")
    private String opt = "";

    @JSONField(name = "data")
    private DataTransferEntity data = null;

    public String getOpt() {
        return opt;
    }

    public ProcessBusinessEntity setOpt(String opt) {
        this.opt = opt;
        return this;
    }

    public DataTransferEntity getData() {
        return data;
    }

    public ProcessBusinessEntity setData(DataTransferEntity data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "ProcessBusinessEntity{" +
                "opt='" + opt + '\'' +
                ", data=" + data +
                '}';
    }
}
