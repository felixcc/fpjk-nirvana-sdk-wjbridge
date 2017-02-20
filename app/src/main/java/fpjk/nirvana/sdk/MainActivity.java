package fpjk.nirvana.sdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import fpjk.nirvana.sdk.wjbridge.business.FpjkView;
import fpjk.nirvana.sdk.wjbridge.business.FpjkWJSDKMgr;

public class MainActivity extends Activity implements View.OnClickListener {

    FpjkView mFpjkView;

    final String url = "http://10.10.232.242:9527/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.goBack).setOnClickListener(this);
        findViewById(R.id.forward).setOnClickListener(this);
        findViewById(R.id.reload).setOnClickListener(this);

        mFpjkView = (FpjkView) findViewById(R.id.fpjkView);

        FpjkWJSDKMgr.get()
                .setActivity(this)
                .setFpjkView(mFpjkView)
                .setShownBackButton(false)
                .execute();

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
                Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
                FpjkWJSDKMgr.get().loadUrl(url);
                break;
            default:
                break;
        }
    }
}
