package fpjk.nirvana.sdk.android.business;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Summary:
 * Created by Felix
 * Date: 01/12/2016
 * Time: 19:04
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class DeviceInfoEntity {
    @JSONField(name = "deviceInfo")
    private DeviceInfo deviceInfo;

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public DeviceInfoEntity setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
        return this;
    }

    @Override
    public String toString() {
        return "DeviceInfoEntity{" +
                "deviceInfo=" + deviceInfo +
                '}';
    }

    public static class DeviceInfo {
        @JSONField(name = "os")
        private String os;

        @JSONField(name = "sysVersion")
        private String sysVersion;

        @JSONField(name = "pid")
        private String pid;

        @JSONField(name = "cellPhoneType")
        private String cellPhoneType;

        @JSONField(name = "version")
        private String version;

        @JSONField(name = "versionName")
        private String versionName;

        @JSONField(name = "us")
        private String us;

        @JSONField(name = "deviceState")
        private String deviceState;

        public String getOs() {
            return os;
        }

        public DeviceInfo setOs(String os) {
            this.os = os;
            return this;
        }

        public String getSysVersion() {
            return sysVersion;
        }

        public DeviceInfo setSysVersion(String sysVersion) {
            this.sysVersion = sysVersion;
            return this;
        }

        public String getPid() {
            return pid;
        }

        public DeviceInfo setPid(String pid) {
            this.pid = pid;
            return this;
        }

        public String getCellPhoneType() {
            return cellPhoneType;
        }

        public DeviceInfo setCellPhoneType(String cellPhoneType) {
            this.cellPhoneType = cellPhoneType;
            return this;
        }

        public String getVersion() {
            return version;
        }

        public DeviceInfo setVersion(String version) {
            this.version = version;
            return this;
        }

        public String getVersionName() {
            return versionName;
        }

        public DeviceInfo setVersionName(String versionName) {
            this.versionName = versionName;
            return this;
        }

        public String getUs() {
            return us;
        }

        public DeviceInfo setUs(String us) {
            this.us = us;
            return this;
        }

        public String getDeviceState() {
            return deviceState;
        }

        public DeviceInfo setDeviceState(String deviceState) {
            this.deviceState = deviceState;
            return this;
        }

        @Override
        public String toString() {
            return "DeviceInfo{" +
                    "os='" + os + '\'' +
                    ", sysVersion='" + sysVersion + '\'' +
                    ", pid='" + pid + '\'' +
                    ", cellPhoneType='" + cellPhoneType + '\'' +
                    ", version='" + version + '\'' +
                    ", versionName='" + versionName + '\'' +
                    ", us='" + us + '\'' +
                    ", deviceState='" + deviceState + '\'' +
                    '}';
        }
    }
}
