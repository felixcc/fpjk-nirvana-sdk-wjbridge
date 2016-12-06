package fpjk.nirvana.sdk.android.logger;

import android.util.Log;

import fpjk.nirvana.sdk.wjbridge.BuildConfig;


/**
 * Created with Android Studio.
 * User: Felix
 * Date: 10/19/15
 * Time: 2:17 PM
 * QQ:74104
 * Email:lovejiuwei@gmail.com
 */
public class L {
    private static boolean isDebug = BuildConfig.DEBUG;

    private static final String TAG = "FELIX";

    public static void i(String tag, String msg) {
        Logger.i(tag, msg);
    }

    public static void i(String msg) {
        Logger.i(TAG, msg);
    }

    public static void i(String tag, String msg, Object... args) {
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        i(tag, msg);
    }

    public static void i(String msg, Object... args) {
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        i(TAG, msg);
    }

    public static void httpInfo(String message) {
        if (isDebug)
            Log.d("httpInfo", message);
    }

    public static void d(String message) {
        if (isDebug)
            Logger.d(TAG, message);
    }

    public static void d(String message, int methodCount) {
        if (isDebug)
            Logger.d(TAG, message, methodCount);
    }

    public static void d(String tag, String message, int methodCount) {
        if (isDebug)
            Logger.d(tag, message, methodCount);
    }

    public static void d(String msg, Object... args) {
        if (isDebug) {
            if (args.length > 0) {
                msg = String.format(msg, args);
            }
            Logger.d(TAG, msg);
        }
    }

    public static void json(String message) {
        if (isDebug)
            Logger.json(message);
    }

    public static void w(String tag, String msg) {
        if (isDebug)
            Logger.w(tag, msg);
    }

    public static void w(String msg) {
        if (isDebug)
            Logger.w(TAG, msg);
    }

    public static void w(String tag, String msg, Object... args) {
        if (isDebug) {
            if (args.length > 0) {
                msg = String.format(msg, args);
            }
            w(tag, msg);
        }
    }

    public static void w(String msg, Object... args) {
        if (isDebug) {
            if (args.length > 0) {
                msg = String.format(msg, args);
            }
            w(TAG, msg);
        }
    }

    public static void e(String msg) {
        Logger.e(TAG, msg);
    }

    public static void e(String msg, Object... args) {
        if (args.length > 0) {
            msg = String.format(msg, args);
        }
        Logger.e(TAG, msg);
    }

    public static void e(String msg, Throwable throwable) {
        Logger.e(TAG, msg, throwable);
    }

    public static void e(Throwable throwable) {
        Logger.e(TAG, throwable);
    }
}
