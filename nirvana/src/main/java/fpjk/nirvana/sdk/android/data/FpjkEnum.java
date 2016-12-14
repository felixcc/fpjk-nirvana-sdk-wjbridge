package fpjk.nirvana.sdk.android.data;

/**
 * Summary:
 * Created by Felix
 * Date: 01/12/2016
 * Time: 16:42
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class FpjkEnum {
    public enum Business {
        GET_DEVICE_INFO("getDeviceInfo"),
        OPEN_URL("openUrl"),
        GET_COOKIE("getCookie"),
        GET_LOCATION("getLocation"),
        GET_CALL_RECORDS("getCallRecords"),
        GET_SMS_RECORDS("getSMSRecords"),
        GET_CONTACTS("getContacts");

        private final String value;

        // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
        Business(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ErrorCode {
        USER_DENIED_ACCESS(10001),//用户拒绝通讯录权限
        USER_DENIED_LOCATION(10002),//用户拒绝定位权限
        USER_MOBILE_LOCATION_SERVICES_OFF(10003),//用户手机定位服务关闭
        USER_REJECT_CALL_RECORD(10004),//用户拒绝通话记录权限
        USERS_REFUSE_SMS_PERMISSIONS(10005);//用户拒绝短信权限

        private final int value;

        // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
        ErrorCode(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum OpenUrlStatus {
        USER_SHUTDOWN(0),//手动关闭
        AUTO_SHUTDOWN(1);//检测到指定URL关闭

        private final int value;

        // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
        OpenUrlStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum Record {
        INCOMING_TYPE(0),//呼入
        OUTGOING_TYPE(1),//呼出
        MISSED_TYPE(2);//未接

        private final int value;

        // 构造器默认也只能是private, 从而保证构造函数只能在内部使用
        Record(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
