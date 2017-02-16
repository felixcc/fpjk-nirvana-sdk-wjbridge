package fpjk.nirvana.sdk.wjbridge.business.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Summary:
 * Created by Felix
 * Date: 13/12/2016
 * Time: 20:01
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class RecordEntity {
    @JSONField(name = "recordList")
    private List<RecordList> recordList = null;

    /**
     * @return The recordList
     */
    public List<RecordList> getRecordList() {
        return recordList;
    }

    /**
     * @param recordList The recordList
     */
    public void setRecordList(List<RecordList> recordList) {
        this.recordList = recordList;
    }

}
