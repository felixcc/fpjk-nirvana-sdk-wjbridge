package fpjk.nirvana.sdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import fpjk.nirvana.sdk.wjbridge.business.FpjkWJSDKMgr;
import fpjk.nirvana.sdk.wjbridge.business.FpjkView;

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

        FpjkWJSDKMgr.get()
                .setActivity(this)
                .setFpjkView(mFpjkView)
                .setShownBackButton(true)
                .execute();

        final String url = mEditText.getText().toString();
        FpjkWJSDKMgr.get().loadUrl(url);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.goBack:
                FpjkWJSDKMgr.get().goBack();
                break;
            case R.id.forward:
                FpjkWJSDKMgr.get().goForward();
                break;
            case R.id.reload:
                final String url = mEditText.getText().toString();
                Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
                FpjkWJSDKMgr.get().loadUrl(url);
                break;
            default:
                break;
        }
    }
}
