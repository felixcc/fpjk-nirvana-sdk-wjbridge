package fpjk.nirvana.sdk.wjbridge.presenter;

import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import fpjk.nirvana.sdk.wjbridge.jsbridge.WJBridgeProvider;

/**
 * Summary:
 * Created by FelixChen
 * Created 2017-02-14 14:18
 * Mail:lovejiuwei@gmail.com
 * QQ:74104
 */

public class WJBridgeChromeClient extends WebChromeClient {
    private WJBridgeProvider mProvider;

    public WJBridgeChromeClient(WJBridgeProvider provider) {
        mProvider = provider;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
    }


    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }
}
