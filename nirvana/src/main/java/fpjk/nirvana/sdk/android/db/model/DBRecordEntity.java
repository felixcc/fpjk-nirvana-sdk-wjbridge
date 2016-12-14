package fpjk.nirvana.sdk.android.db.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "fpjk_local_call_log")
public class DBRecordEntity implements Parcelable {
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

    @DatabaseField(columnName = "duration")
    private long duration;

    @DatabaseField(columnName = "type")
    private Integer type;

    public long getId() {
        return id;
    }

    public DBRecordEntity setId(long id) {
        this.id = id;
        return this;
    }

    public long getUid() {
        return uid;
    }

    public DBRecordEntity setUid(long uid) {
        this.uid = uid;
        return this;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public DBRecordEntity setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    public String getName() {
        return name;
    }

    public DBRecordEntity setName(String name) {
        this.name = name;
        return this;
    }

    public Long getDate() {
        return date;
    }

    public DBRecordEntity setDate(Long date) {
        this.date = date;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public DBRecordEntity setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public DBRecordEntity setType(Integer type) {
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
        dest.writeLong(this.date);
        dest.writeLong(this.duration);
        dest.writeValue(this.type);
    }

    public DBRecordEntity() {
    }

    protected DBRecordEntity(Parcel in) {
        this.id = in.readLong();
        this.uid = in.readLong();
        this.phoneNum = in.readString();
        this.name = in.readString();
        this.date = in.readLong();
        this.duration = in.readLong();
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<DBRecordEntity> CREATOR = new Creator<DBRecordEntity>() {
        @Override
        public DBRecordEntity createFromParcel(Parcel source) {
            return new DBRecordEntity(source);
        }

        @Override
        public DBRecordEntity[] newArray(int size) {
            return new DBRecordEntity[size];
        }
    };

    @Override
    public String toString() {
        return "DBRecordEntity{" +
                "id=" + id +
                ", uid=" + uid +
                ", phoneNum='" + phoneNum + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", duration=" + duration +
                ", type=" + type +
                '}';
    }
}
