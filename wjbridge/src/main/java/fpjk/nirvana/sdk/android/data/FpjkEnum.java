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
        GET_COOKIE("getCookie"),
        GET_LOCATION("getLocation"),
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
}
