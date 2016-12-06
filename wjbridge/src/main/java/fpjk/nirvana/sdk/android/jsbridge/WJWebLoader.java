package fpjk.nirvana.sdk.android.jsbridge;

import android.content.Context;

import java.util.Map;

public interface WJWebLoader {

    Context getContext();

    void loadUrl(String url);

    void loadUrl(String url, Map<String, String> headers);
}
