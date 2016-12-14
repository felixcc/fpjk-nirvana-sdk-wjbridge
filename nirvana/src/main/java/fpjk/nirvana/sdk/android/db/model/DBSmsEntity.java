package fpjk.nirvana.sdk.android.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "fpjk_local_sms")
public class DBSmsEntity implements Parcelable {
    @DatabaseField(generatedId = true)
    private long id;

    @DatabaseField(columnName = "uid", index = true)
    private long uid;

    @DatabaseField(columnName = "phoneNum")
    private String phoneNum;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "date")
    private Long date;

    @DatabaseField(columnName = "content")
    private String content;

    @DatabaseField(columnName = "type")
    private Integer type;

    public long getId() {
        return id;
    }

    public DBSmsEntity setId(long id) {
        this.id = id;
        return this;
    }

    public long getUid() {
        return uid;
    }

    public DBSmsEntity setUid(long uid) {
        this.uid = uid;
        return this;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public DBSmsEntity setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    public String getName() {
        return name;
    }

    public DBSmsEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Long getDate() {
        return date;
    }

    public DBSmsEntity setDate(Long date) {
        this.date = date;
        return this;
    }

    public String getContent() {
        return content;
    }

    public DBSmsEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public DBSmsEntity setType(Integer type) {
        this.type = type;
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
        dest.writeString(this.phoneNum);
        dest.writeString(this.name);
        dest.writeValue(this.date);
        dest.writeString(this.content);
        dest.writeValue(this.type);
    }

    public DBSmsEntity() {
    }

    protected DBSmsEntity(Parcel in) {
        this.id = in.readLong();
        this.uid = in.readLong();
        this.phoneNum = in.readString();
        this.name = in.readString();
        this.date = (Long) in.readValue(Long.class.getClassLoader());
        this.content = in.readString();
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<DBSmsEntity> CREATOR = new Creator<DBSmsEntity>() {
        @Override
        public DBSmsEntity createFromParcel(Parcel source) {
            return new DBSmsEntity(source);
        }

        @Override
        public DBSmsEntity[] newArray(int size) {
            return new DBSmsEntity[size];
        }
    };

    @Override
    public String toString() {
        return "DBSmsEntity{" +
                "id=" + id +
                ", uid=" + uid +
                ", phoneNum='" + phoneNum + '\'' +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", content='" + content + '\'' +
                ", type=" + type +
                '}';
    }
}
