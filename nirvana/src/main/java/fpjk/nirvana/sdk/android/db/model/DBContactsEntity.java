package fpjk.nirvana.sdk.android.db.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 联系人数据模型
 */
@DatabaseTable(tableName = "t_Mobile")
public class DBContactsEntity implements Parcelable {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "fullName")
    private String fullName;

    @DatabaseField(columnName = "phoneNum")
    private String phoneNum;

    public int getId() {
        return id;
    }

    public DBContactsEntity setId(int id) {
        this.id = id;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.fullName);
        dest.writeString(this.phoneNum);
    }

    public DBContactsEntity() {
    }

    protected DBContactsEntity(Parcel in) {
        this.id = in.readInt();
        this.fullName = in.readString();
        this.phoneNum = in.readString();
    }

    public static final Creator<DBContactsEntity> CREATOR = new Creator<DBContactsEntity>() {
        @Override
        public DBContactsEntity createFromParcel(Parcel source) {
            return new DBContactsEntity(source);
        }

        @Override
        public DBContactsEntity[] newArray(int size) {
            return new DBContactsEntity[size];
        }
    };

    @Override
    public String toString() {
        return "DBContactsEntity{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
