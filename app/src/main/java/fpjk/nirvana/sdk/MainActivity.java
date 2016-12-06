package fpjk.nirvana.sdk;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import fpjk.nirvana.sdk.android.presenter.WJBridgeWebView;

public class MainActivity extends Activity implements View.OnClickListener {

    WJBridgeWebView mWJBridgeWebView;

    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.goBack).setOnClickListener(this);
        findViewById(R.id.forward).setOnClickListener(this);
        findViewById(R.id.reload).setOnClickListener(this);

        mEditText = (EditText) findViewById(R.id.address);
        mWJBridgeWebView = (WJBridgeWebView) findViewById(R.id.wJBridgeX5WebView);

        final String url = mEditText.getText().toString();
        mWJBridgeWebView.loadUrl(url);

//        String url = "file:///android_asset/index.html";
//        mWJBridgeWebView.registerObtainCookie(new IObtainCookie() {
//            @Override
//            public void obtain(String cookie) {
//                L.d("wjBridgeWebView.registerObtainCookie->[%s]", cookie);
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.goBack:
                if (mWJBridgeWebView.canGoBack()) {
                    mWJBridgeWebView.goBack();
                }
                break;
            case R.id.forward:
                if (mWJBridgeWebView.canGoForward()) {
                    mWJBridgeWebView.goForward();
                }
                break;
            case R.id.reload:
//                final String url = mEditText.getText().toString();
//                mWJBridgeWebView.loadUrl(url);
                mWJBridgeWebView.reload();
                break;
            default:
                break;
        }
    }
}
