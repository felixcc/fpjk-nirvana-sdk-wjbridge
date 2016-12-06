package fpjk.nirvana.sdk.android.jsbridge;

import java.util.List;

public interface WebViewJavascriptBridge {

    void send(String data);

    void send(String data, WJCallbacks callbacks);

    void setStartupMessages(List<WJMessage> messages);

    void loadUrl(String jsUrl, WJCallbacks callbacks);

    void callHandler(String handlerName, String data, WJCallbacks callbacks);

    void registerHandler(String handlerName, WJBridgeHandler handler);

    void setDefaultHandler(WJBridgeHandler handler);
}
