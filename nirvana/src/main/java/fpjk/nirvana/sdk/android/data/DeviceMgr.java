package fpjk.nirvana.sdk.android.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import fpjk.nirvana.sdk.android.business.entity.CookieEntity;
import fpjk.nirvana.sdk.android.business.entity.CookieList;
import fpjk.nirvana.sdk.android.jsbridge.WJBridgeUtils;
import fpjk.nirvana.sdk.android.logger.Logger;

/**
 * Summary:
 * Created by Felix
 * Date: 26/11/2016
 * Time: 15:11
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class DeviceMgr {

    private Context mContext = null;

    public static DeviceMgr newInstance(@NonNull Context context) {
        return new DeviceMgr(WJBridgeUtils.checkNoNull(context, "Context not NULL!"));
    }

    private DeviceMgr(Context context) {
        mContext = context;
    }

    private String channelName = "";//渠道名称

    public String getChannelName() {
        return channelName;
    }

    public DeviceMgr setChannelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    /**
     * 获取DeviceID
     */
    private String mDeviceId;

    public String getIMEI() {
        if (!TextUtils.isEmpty(mDeviceId)) {
            return mDeviceId;
        } else {
            mDeviceId = DeviceUuidFactory.getDeviceId(mContext);
        }
        return mDeviceId;
    }

    /**
     * 获取当前版本名称
     *
     * @return 版本名称
     */
    public String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            return packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e);
        }
        return "";
    }

    public String getSyVersion() {
        return Build.VERSION.RELEASE + "";
    }

    /**
     * 获取版本号
     */
    public int getVersionCode() {
        PackageManager packageManager = mContext.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("", e.getMessage() + ";;;");
        }
        int version = packInfo.versionCode;
        return version;
    }

    private static class DeviceUuidFactory {
        private static final String PREFS_DEVICE_ID = "key_device_id";

        private static String imei;

        /**
         * Returns a unique UUID for the current android device. As with all UUIDs, this unique ID
         * is "very highly likely" to be unique across all Android devices. Much more so than
         * ANDROID_ID is.
         * <p/>
         * The UUID is generated by using ANDROID_ID as the base key if appropriate, falling back on
         * TelephonyManager.getDeviceID() if ANDROID_ID is known to be incorrect, and finally
         * falling back on a random UUID that's persisted to SharedPreferences if getDeviceID() does
         * not return a usable value.
         * <p/>
         * In some rare circumstances, this ID may change. In particular, if the device is factory
         * reset a new device ID may be generated. In addition, if a user upgrades their phone from
         * certain buggy implementations of Android 2.2 to a newer, non-buggy version of Android,
         * the device ID may change. Or, if a user uninstalls your app on a device that has neither
         * a proper Android ID nor a Device ID, this ID may change on reinstallation.
         * <p/>
         * Note that if the code falls back on using TelephonyManager.getDeviceId(), the resulting
         * ID will NOT change after a factory reset. Something to be aware of.
         * <p/>
         * Works around a bug in Android 2.2 for many devices when using ANDROID_ID directly.
         *
         * @return a UUID that may be used to uniquely identify your device for most purposes.
         * @see <http://code.google.com/p/android/issues/detail?id=10603/>
         */
        private static synchronized String getDeviceId(Context context) {
            if (imei == null) {
                final SharedPreferences prefs = context.getSharedPreferences("QianZhanFile", 0);
                String deviceId = null;
                try {
                    deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                } catch (Throwable e) {

                }
                if (deviceId == null || isFull(deviceId, '0') || isFull(deviceId, '*')) {
                    final String savedDeviceId = prefs.getString(PREFS_DEVICE_ID, null);
                    if (savedDeviceId == null) {
                        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        if (!"9774d56d682e549c".equals(androidId)) {
                            deviceId = androidId;
                        } else {
                            deviceId = UUID.randomUUID().toString();
                        }
                        prefs.edit().putString(PREFS_DEVICE_ID, deviceId).commit();
                    } else {
                        deviceId = savedDeviceId;
                    }
                }
                imei = deviceId;
            }
            return imei;
        }

        private static boolean isFull(String source, char target) {
            for (char ch : source.toCharArray()) {
                if (target != ch) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 格式化cookie
     */
    public CookieEntity formatCookie(String cookie) {
        CookieEntity cookieEntity = new CookieEntity();
        List<CookieList> cookieList = new ArrayList<>();
        try {
            cookie = cookie.replaceAll(" ", "");
            String[] arr = cookie.split(";");
            for (String item : arr) {
                int index = item.indexOf("=");
                String[] itemArr = new String[2];
                itemArr[0] = item.substring(0, index);
                itemArr[1] = item.substring(index + 1, item.length());
                if (itemArr.length > 1) {
                    CookieList value = new CookieList();
                    value.setName(itemArr[0]);
                    value.setValue(itemArr[1]);
                    cookieList.add(value);
                }
            }
            cookieEntity.setCookieList(cookieList);
            return cookieEntity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public final int isEmulator() {
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (imei != null && imei.equals("000000000000000")) {
                return 1;
            }
        } catch (Exception e) {
            Logger.e(e);
        }

        if ((Build.PRODUCT.equals("sdk")) || (Build.PRODUCT.equals("google_sdk"))
                || (Build.PRODUCT.equals("sdk_x86")) || (Build.PRODUCT.equals("vbox86p"))) {
            return 1;
        }
        if ((Build.MANUFACTURER.equals("unknown")) || (Build.MANUFACTURER.equals("Genymotion"))) {
            return 1;
        }
        if ((Build.BRAND.equals("generic")) || (Build.BRAND.equals("generic_x86"))) {
            return 1;
        }
        if ((Build.DEVICE.equals("generic")) || (Build.DEVICE.equals("generic_x86")) || (Build.DEVICE.equals("vbox86p"))) {
            return 1;
        }
        if ((Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"))
                || (Build.MODEL.equals("Android SDK built for x86"))) {
            return 1;
        }
        if ((Build.HARDWARE.equals("goldfish")) || (Build.HARDWARE.equals("vbox86"))) {
            return 1;
        }
        if ((Build.FINGERPRINT.contains("generic/sdk/generic"))
                || (Build.FINGERPRINT.contains("generic_x86/sdk_x86/generic_x86"))
                || (Build.FINGERPRINT.contains("generic/google_sdk/generic"))
                || (Build.FINGERPRINT.contains("generic/vbox86p/vbox86p"))) {
            return 1;
        }

        return 0;
    }
}
