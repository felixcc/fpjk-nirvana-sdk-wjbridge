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

    private L() {

    }

    public static void json(String json) {
        if (isDebug) {
            Logger.json(json);
        }
    }

    public static void d(String msg, Object... args) {
        if (isDebug) {
            Logger.d(String.format(msg, args));
        }
    }

    public static void i(String msg, Object... args) {
        if (isDebug) {
            Logger.i(String.format(msg, args));
        }
    }

    public static void e(String msg, Object... args) {
        if (isDebug) {
            Logger.e(String.format(msg, args));
        }
    }

    public static void e(String msg, Throwable error) {
        if (isDebug) {
            Logger.e(msg, error);
        }
    }
}
