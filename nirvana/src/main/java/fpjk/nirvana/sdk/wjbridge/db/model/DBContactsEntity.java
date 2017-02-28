package fpjk.nirvana.sdk.wjbridge.db.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "fpjk_local_contacts")
public class DBContactsEntity {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(index = true)
    private long uid;

    @DatabaseField
    private String fullName;

    @DatabaseField
    private String phoneNum;

    public long getId() {
        return id;
    }

    public DBContactsEntity setId(long id) {
        this.id = id;
        return this;
    }

    public long getUid() {
        return uid;
    }

    public DBContactsEntity setUid(long uid) {
        this.uid = uid;
        return this;
    }

    public String getFullName() {
        return fullName;
    }

    public DBContactsEntity setFullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public DBContactsEntity setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    @Override
    public String toString() {
        return "DBContactsEntity{" +
                "id=" + id +
                ", uid=" + uid +
                ", fullName='" + fullName + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
