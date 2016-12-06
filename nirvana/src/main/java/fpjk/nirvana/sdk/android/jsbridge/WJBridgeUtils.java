package fpjk.nirvana.sdk.android.jsbridge;

import android.content.Context;
import android.os.SystemClock;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import fpjk.nirvana.sdk.android.logger.L;

public final class WJBridgeUtils {
    private static final String TAG = "WJBridgeUtils";

    private static final String ENCODING = "UTF-8";

    static final String WJ_PROTOCOL_SCHEME = "wvjbscheme://";
    static final String WJ_BRIDGE_LOADED = WJ_PROTOCOL_SCHEME + "__BRIDGE_LOADED__";
    static final String WJ_RETURN_DATA = WJ_PROTOCOL_SCHEME + "return/";
    static final String WJ_FETCH_QUEUE_MESSAGE = WJ_RETURN_DATA + "_fetchQueue/";
    static final String WJ_EMPTY_STR = "";
    static final String WJ_SPLIT_MARK = "/";

    static final String CALLBACK_ID_FORMAT = "native_cb_%s_%s";
    static final String JS_DISPATCH_MESSAGE = "javascript:WebViewJavascriptBridge._handleMessageFromNative('%s');";
    static final String JS_FETCH_QUEUE = "javascript:WebViewJavascriptBridge._fetchQueue();";

    private WJBridgeUtils() {
        throw new AssertionError("no instance!");
    }

    public static <T> T checkNoNull(T t, String msg) {
        if (t == null) {
            throw new NullPointerException(msg);
        }
        return t;
    }

    public static String formatCallbackId(long uniqueId) {
        return String.format(CALLBACK_ID_FORMAT, uniqueId, SystemClock.currentThreadTimeMillis());
    }

    public static String formatJsDispatchMessage(String messageJson) {
        return String.format(JS_DISPATCH_MESSAGE, messageJson);
    }

    public static String parseFuncName(String jsUrl) {
        return jsUrl.replace("javascript:WebViewJavascriptBridge.", "").replaceAll("\\(.*\\);", "");
    }

    public static String getFuncNameFromUrl(String url) {
        String temp = url.replace(WJ_RETURN_DATA, WJ_EMPTY_STR);
        String[] functionAndData = temp.split(WJ_SPLIT_MARK);
        if (functionAndData.length >= 1) {
            return functionAndData[0];
        }
        return null;
    }

    public static String getReturnDataFromUrl(String url) {
        if (url.startsWith(WJ_FETCH_QUEUE_MESSAGE)) {
            return url.replace(WJ_FETCH_QUEUE_MESSAGE, WJ_EMPTY_STR);
        }

        String temp = url.replace(WJ_RETURN_DATA, WJ_EMPTY_STR);
        String[] functionAndData = temp.split(WJ_SPLIT_MARK);

        if (functionAndData.length >= 2) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < functionAndData.length; i++) {
                sb.append(functionAndData[i]);
            }
            return sb.toString();
        }
        return null;
    }

    public static String decodeUrl(String url) {
        try {
            return URLDecoder.decode(url, ENCODING);
        } catch (Exception e) {
            L.e(TAG, e);
            return url;
        }
    }

    public static void webViewLoadJs(WJWebLoader view, String url) {
        String js = "var newscript = document.createElement(\"script\");";
        js += "newscript.src=\"" + url + "\";";
        js += "document.scripts[0].parentNode.insertBefore(newscript,document.scripts[0]);";
        view.loadUrl("javascript:" + js);
    }

    public static void webViewLoadLocalJs(WJWebLoader loader, String path) {
        String jsContent = assetFile2Str(loader.getContext(), path);
        loader.loadUrl("javascript:" + jsContent);
    }

    private static String assetFile2Str(Context context, String urlStr) {
        InputStream is = null;
        try {
            is = context.getAssets().open(urlStr);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sb = new StringBuilder();
            do {
                line = bufferedReader.readLine();
                if (line != null && !line.matches("^\\s*\\/\\/.*")) {
                    sb.append(line);
                }
            } while (line != null);
            WJBridgeUtils.close(bufferedReader);
            return sb.toString();
        } catch (Exception e) {
            L.e("assetFile2Str.[%s]", new Object[]{urlStr}, e);
        } finally {
            WJBridgeUtils.close(is);
        }
        return null;
    }

    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                if (closeable != null) {
                    closeable.close();
                }
            } catch (Exception e) {
                L.e("close.[%s]", new Object[]{closeable}, e);
            }
        }
    }
}
