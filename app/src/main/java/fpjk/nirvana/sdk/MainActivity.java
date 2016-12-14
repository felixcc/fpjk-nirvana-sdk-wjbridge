package fpjk.nirvana.sdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import fpjk.nirvana.sdk.android.FpjkWJSDKMgr;
import fpjk.nirvana.sdk.android.business.FpjkView;

public class MainActivity extends Activity implements View.OnClickListener {

    FpjkView mFpjkView;

    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.goBack).setOnClickListener(this);
        findViewById(R.id.forward).setOnClickListener(this);
        findViewById(R.id.reload).setOnClickListener(this);

        mEditText = (EditText) findViewById(R.id.address);
        mFpjkView = (FpjkView) findViewById(R.id.fpjkView);

        FpjkWJSDKMgr.get().buildConfiguration(this, mFpjkView);

        final String url = mEditText.getText().toString();
        FpjkWJSDKMgr.get().loadUrl(url);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.goBack:
                if (FpjkWJSDKMgr.get().canGoBack()) {
                    FpjkWJSDKMgr.get().goBack();
                }
                break;
            case R.id.forward:
                if (FpjkWJSDKMgr.get().canGoForward()) {
                    FpjkWJSDKMgr.get().goForward();
                }
                break;
            case R.id.reload:
//                final String url = mEditText.getText().toString();
//                mWJBridgeWebView.loadUrl(url);
                FpjkWJSDKMgr.get().reload();
                break;
            default:
                break;
        }
    }
}
