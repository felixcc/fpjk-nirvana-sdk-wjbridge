package fpjk.nirvana.sdk.android.business.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Summary:
 * Created by Felix
 * Date: 14/12/2016
 * Time: 17:10
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class InstalledAppInfoEntity implements Parcelable {
    private String appId = "";
    private String name = "";
    private String version = "";

    public String getAppId() {
        return appId;
    }

    public InstalledAppInfoEntity setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getName() {
        return name;
    }

    public InstalledAppInfoEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public InstalledAppInfoEntity setVersion(String version) {
        this.version = version;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appId);
        dest.writeString(this.name);
        dest.writeString(this.version);
    }

    public InstalledAppInfoEntity() {
    }

    protected InstalledAppInfoEntity(Parcel in) {
        this.appId = in.readString();
        this.name = in.readString();
        this.version = in.readString();
    }

    public static final Creator<InstalledAppInfoEntity> CREATOR = new Creator<InstalledAppInfoEntity>() {
        @Override
        public InstalledAppInfoEntity createFromParcel(Parcel source) {
            return new InstalledAppInfoEntity(source);
        }

        @Override
        public InstalledAppInfoEntity[] newArray(int size) {
            return new InstalledAppInfoEntity[size];
        }
    };

    @Override
    public String toString() {
        return "InstalledAppInfoEntity{" +
                "appId='" + appId + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
