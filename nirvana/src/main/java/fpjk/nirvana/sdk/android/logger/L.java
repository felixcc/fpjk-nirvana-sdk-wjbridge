package fpjk.nirvana.sdk.android.logger;

/**
 * Created with Android Studio.
 * User: Felix
 * Date: 10/19/15
 * Time: 2:17 PM
 * QQ:74104
 * Email:lovejiuwei@gmail.com
 */
public class L {
    private static boolean isDebug = true;

    private static final String TAG = "FELIX";

    public static void json(String json) {
        if (isDebug) {
            Logger.json(TAG, json);
        }
    }

    public static void i(String msg, Object... args) {
        if (isDebug) {
            Logger.i(TAG, String.format(msg, args));
        }
    }

    public static void e(String msg, Object... args) {
        if (isDebug) {
            Logger.e(TAG, String.format(msg, args));
        }
    }

    public static void e(String msg, Throwable error) {
        if (isDebug) {
            Logger.e(TAG, msg, error);
        }
    }
}
