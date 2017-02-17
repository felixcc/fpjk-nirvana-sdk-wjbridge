package fpjk.nirvana.sdk.wjbridge.business.entity;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Summary:
 * Created by Felix
 * Date: 12/12/2016
 * Time: 17:19
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class ContactListEntity {
    @JSONField(name = "contactList")
    private List<ContactList> contactList = null;

    /**
     * @return The contactList
     */
    public List<ContactList> getContactList() {
        return contactList;
    }

    /**
     * @param contactList The contactList
     */
    public void setContactList(List<ContactList> contactList) {
        this.contactList = contactList;
    }

}
