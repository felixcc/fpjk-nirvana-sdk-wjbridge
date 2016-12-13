package fpjk.nirvana.sdk.android.business.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Summary:json里面的data层
 * Created by Felix
 * Date: 01/12/2016
 * Time: 18:59
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class DataTransferEntity implements Parcelable {
    @JSONField(name = "url")
    private String url = "";

    @JSONField(name = "title")
    private String title = "";

    @JSONField(name = "finishUrl")
    private String finishUrl = "";

    @JSONField(name = "uid")
    private Long uid = 0L;

    public String getUrl() {
        return url;
    }

    public DataTransferEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public DataTransferEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getFinishUrl() {
        return finishUrl;
    }

    public DataTransferEntity setFinishUrl(String finishUrl) {
        this.finishUrl = finishUrl;
        return this;
    }

    public Long getUid() {
        return uid;
    }

    public DataTransferEntity setUid(Long uid) {
        this.uid = uid;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.finishUrl);
        dest.writeLong(this.uid);
    }

    public DataTransferEntity() {
    }

    protected DataTransferEntity(Parcel in) {
        this.url = in.readString();
        this.title = in.readString();
        this.finishUrl = in.readString();
        this.uid = in.readLong();
    }

    public static final Creator<DataTransferEntity> CREATOR = new Creator<DataTransferEntity>() {
        @Override
        public DataTransferEntity createFromParcel(Parcel source) {
            return new DataTransferEntity(source);
        }

        @Override
        public DataTransferEntity[] newArray(int size) {
            return new DataTransferEntity[size];
        }
    };

    @Override
    public String toString() {
        return "DataTransferEntity{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", finishUrl='" + finishUrl + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
