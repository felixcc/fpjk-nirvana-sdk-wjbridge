package fpjk.nirvana.sdk.wjbridge.presenter;

import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import fpjk.nirvana.sdk.wjbridge.data.RxBus;
import fpjk.nirvana.sdk.wjbridge.data.event.EventOnProgressChanged;
import fpjk.nirvana.sdk.wjbridge.data.event.EventPageReceivedTitle;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJBridgeProvider;

/**
 * Summary: Created by FelixChen Created 2017-02-14 14:18 Mail:lovejiuwei@gmail.com QQ:74104
 */

public class WJBridgeChromeClient extends WebChromeClient {
    private WJBridgeProvider mProvider;

    public WJBridgeChromeClient(WJBridgeProvider provider) {
        mProvider = provider;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        RxBus.get().send(new EventOnProgressChanged().setNewProgress(newProgress));
    }

    @Override
    public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        RxBus.get().send(new EventPageReceivedTitle().setTitle(title));
    }
}
