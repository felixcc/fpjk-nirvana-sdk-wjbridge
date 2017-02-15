package fpjk.nirvana.sdk.android.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.util.List;

import fpjk.nirvana.sdk.android.jsbridge.WJBridgeHandler;
import fpjk.nirvana.sdk.android.jsbridge.WJBridgeProvider;
import fpjk.nirvana.sdk.android.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.android.jsbridge.WJMessage;
import fpjk.nirvana.sdk.android.jsbridge.WJWebLoader;
import fpjk.nirvana.sdk.android.jsbridge.WebViewJavascriptBridge;

public class WJBridgeWebView extends WebView implements WebViewJavascriptBridge, WJWebLoader {

    private WJBridgeProvider mProvider;

    public WJBridgeWebView(Context context) {
        super(context);
        init();
    }

    public WJBridgeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WJBridgeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        mProvider = WJBridgeProvider.newInstance(this);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        getSettings().setDefaultTextEncodingName("UTF-8");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        setWebViewClient(onCreateWebViewClient(mProvider));
        setWebChromeClient(onCreateWJBridgeChromeClient(mProvider));
    }

    protected WJBridgeWebViewClient onCreateWebViewClient(WJBridgeProvider provider) {
        return new WJBridgeWebViewClient(provider);
    }

    private WJBridgeChromeClient onCreateWJBridgeChromeClient(WJBridgeProvider provider) {
        return new WJBridgeChromeClient(provider);
    }

    @Override
    public void send(String data) {
        mProvider.send(data);
    }

    @Override
    public void send(String data, WJCallbacks callbacks) {
        mProvider.send(data, callbacks);
    }

    @Override
    public void setStartupMessages(List<WJMessage> messages) {
        mProvider.setStartupMessages(messages);
    }

    @Override
    public void loadUrl(String jsUrl, WJCallbacks callbacks) {
        mProvider.loadUrl(jsUrl, callbacks);
    }

    @Override
    public void callHandler(String handlerName, String data, WJCallbacks callbacks) {
        mProvider.callHandler(handlerName, data, callbacks);
    }

    @Override
    public void registerHandler(String handlerName, WJBridgeHandler handler) {
        mProvider.registerHandler(handlerName, handler);
    }

    @Override
    public void setDefaultHandler(WJBridgeHandler handler) {
        mProvider.setDefaultHandler(handler);
    }

    @Override
    public void destroy() {
        mProvider.destroy();
        super.destroy();
    }
}
