package fpjk.nirvana.sdk.wjbridge.business.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Summary:
 * Created by Felix
 * Date: 13/12/2016
 * Time: 20:00
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class RecordList implements Parcelable {
    @JSONField(name = "phoneNum")
    private String phoneNum;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "content")
    private String content;

    @JSONField(name = "date")
    private Long date;

    @JSONField(name = "duration")
    private long duration;

    @JSONField(name = "type")
    private Integer type;

    public String getPhoneNum() {
        return phoneNum;
    }

    public RecordList setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        return this;
    }

    public String getName() {
        return name;
    }

    public RecordList setName(String name) {
        this.name = name;
        return this;
    }

    public String getContent() {
        return content;
    }

    public RecordList setContent(String content) {
        this.content = content;
        return this;
    }

    public Long getDate() {
        return date;
    }

    public RecordList setDate(Long date) {
        this.date = date;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public RecordList setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public Integer getType() {
        return type;
    }

    public RecordList setType(Integer type) {
        this.type = type;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.phoneNum);
        dest.writeString(this.name);
        dest.writeString(this.content);
        dest.writeValue(this.date);
        dest.writeLong(this.duration);
        dest.writeValue(this.type);
    }

    public RecordList() {
    }

    protected RecordList(Parcel in) {
        this.phoneNum = in.readString();
        this.name = in.readString();
        this.content = in.readString();
        this.date = (Long) in.readValue(Long.class.getClassLoader());
        this.duration = in.readLong();
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<RecordList> CREATOR = new Creator<RecordList>() {
        @Override
        public RecordList createFromParcel(Parcel source) {
            return new RecordList(source);
        }

        @Override
        public RecordList[] newArray(int size) {
            return new RecordList[size];
        }
    };

    @Override
    public String toString() {
        return "RecordList{" +
                "phoneNum='" + phoneNum + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", type=" + type +
                '}';
    }
}
