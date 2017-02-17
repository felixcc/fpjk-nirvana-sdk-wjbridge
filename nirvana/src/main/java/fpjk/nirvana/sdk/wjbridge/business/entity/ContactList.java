package fpjk.nirvana.sdk.wjbridge.business.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Summary:
 * Created by Felix
 * Date: 12/12/2016ContactList
 * Time: 17:18
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class ContactList {
    @JSONField(name = "fullName")
    private String fullName;

    @JSONField(name = "phoneNumList")
    private List<String> phoneNumList = null;

    /**
     * @return The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName The fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return The phoneNumList
     */
    public List<String> getPhoneNumList() {
        return phoneNumList;
    }

    /**
     * @param phoneNumList The phoneNumList
     */
    public void setPhoneNumList(List<String> phoneNumList) {
        this.phoneNumList = phoneNumList;
    }
}
