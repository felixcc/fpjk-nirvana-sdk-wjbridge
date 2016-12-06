package fpjk.nirvana.sdk.android.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

import java.util.List;

import fpjk.nirvana.sdk.android.business.FpjkBusiness;
import fpjk.nirvana.sdk.android.jsbridge.WJBridgeHandler;
import fpjk.nirvana.sdk.android.jsbridge.WJBridgeProvider;
import fpjk.nirvana.sdk.android.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.android.jsbridge.WJMessage;
import fpjk.nirvana.sdk.android.jsbridge.WJWebLoader;
import fpjk.nirvana.sdk.android.jsbridge.WebViewJavascriptBridge;

/**
 * author: EwenQin
 * since : 2016/10/24 下午4:14.
 */
public class WJBridgeWebView extends WebView implements WebViewJavascriptBridge, WJWebLoader {

    private static final String TAG = "WJBridgeWebView";

    private WJBridgeProvider mProvider;

    public WJBridgeWebView(Context context) {
        super(context);
        this._init();
    }

    public WJBridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this._init();
    }

    public WJBridgeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void _init() {
        this.mProvider = WJBridgeProvider.newInstance(this);
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        this.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        this.setWebViewClient(onCreateWebViewClient(mProvider));

        FpjkBusiness.newInstance(this).process();
    }

    protected WJBridgeWebViewClient onCreateWebViewClient(WJBridgeProvider provider) {
        return new WJBridgeWebViewClient(provider);
    }

    @Override
    public void send(String data) {
        this.mProvider.send(data);
    }

    @Override
    public void send(String data, WJCallbacks callbacks) {
        this.mProvider.send(data, callbacks);
    }

    @Override
    public void setStartupMessages(List<WJMessage> messages) {
        this.mProvider.setStartupMessages(messages);
    }

    @Override
    public void loadUrl(String jsUrl, WJCallbacks callbacks) {
        this.mProvider.loadUrl(jsUrl, callbacks);
    }

    @Override
    public void callHandler(String handlerName, String data, WJCallbacks callbacks) {
        this.mProvider.callHandler(handlerName, data, callbacks);
    }

    @Override
    public void registerHandler(String handlerName, WJBridgeHandler handler) {
        this.mProvider.registerHandler(handlerName, handler);
    }

    @Override
    public void setDefaultHandler(WJBridgeHandler handler) {
        this.mProvider.setDefaultHandler(handler);
    }

    @Override
    public void destroy() {
        this.mProvider.destroy();
        super.destroy();
    }
}
