package fpjk.nirvana.sdk.wjbridge.data;

import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;

import fpjk.nirvana.sdk.wjbridge.presenter.WJBridgeWebView;

/**
 * Summary: Created by FelixChen
 *
 * Created 2017-02-22 15:00
 *
 * Mail:lovejiuwei@gmail.com
 *
 * QQ:74104
 */
public class CookieMgr {
    private static CookieMgr mgr = new CookieMgr();

    public static CookieMgr get() {
        return mgr;
    }

    private CookieMgr() {

    }

    public String getCookie(String url) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        return cookieManager.getCookie(url);
    }

    /**
     * 将cookie同步到WebView
     *
     * @param url    WebView要加载的url
     * @param cookie 要同步的cookie
     * @return true 同步cookie成功，false同步cookie失败
     */
    public void insertCookie(WJBridgeWebView wjBridgeWebView, String url, String cookie) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, cookie);
        wjBridgeWebView.loadUrl(url);
    }

    public void remoeAllCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                }
            });
        } else {
            cookieManager.removeAllCookie();
        }
    }
}
