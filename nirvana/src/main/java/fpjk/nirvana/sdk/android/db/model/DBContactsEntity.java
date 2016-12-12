package fpjk.nirvana.sdk.android.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "fpjk_local_mobile")
public class DBContactsEntity implements Parcelable {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "uid")
    private long uid;

    @DatabaseField(columnName = "fullName")
    private String fullName;

    @DatabaseField(columnName = "phoneNum") //ContactListEntity
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.uid);
        dest.writeString(this.fullName);
        dest.writeString(this.phoneNum);
    }

    public DBContactsEntity() {
    }

    protected DBContactsEntity(Parcel in) {
        this.id = in.readLong();
        this.uid = in.readLong();
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
                ", uid=" + uid +
                ", fullName='" + fullName + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }
}
