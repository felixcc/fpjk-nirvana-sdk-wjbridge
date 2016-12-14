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
    private String appName = "";
    private String packageName = "";
    private String versionName = "";
    private int versionCode = 0;

    public String getAppName() {
        return appName;
    }

    public InstalledAppInfoEntity setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public String getPackageName() {
        return packageName;
    }

    public InstalledAppInfoEntity setPackageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public String getVersionName() {
        return versionName;
    }

    public InstalledAppInfoEntity setVersionName(String versionName) {
        this.versionName = versionName;
        return this;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public InstalledAppInfoEntity setVersionCode(int versionCode) {
        this.versionCode = versionCode;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appName);
        dest.writeString(this.packageName);
        dest.writeString(this.versionName);
        dest.writeInt(this.versionCode);
    }

    public InstalledAppInfoEntity() {
    }

    protected InstalledAppInfoEntity(Parcel in) {
        this.appName = in.readString();
        this.packageName = in.readString();
        this.versionName = in.readString();
        this.versionCode = in.readInt();
    }

    public static final Parcelable.Creator<InstalledAppInfoEntity> CREATOR = new Parcelable.Creator<InstalledAppInfoEntity>() {
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
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                '}';
    }
}
