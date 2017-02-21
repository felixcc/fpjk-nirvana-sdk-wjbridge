package fpjk.nirvana.sdk;

import android.app.Activity;
import android.os.Bundle;

import fpjk.nirvana.sdk.wjbridge.business.FpjkView;
import fpjk.nirvana.sdk.wjbridge.business.FpjkWJSDKMgr;

public class MainActivity extends Activity {

    FpjkView mFpjkView;

    //    final String url = "http://10.10.232.242:9527/";//首页
    final String url = "http://10.10.191.154:9000/";//测试

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFpjkView = (FpjkView) findViewById(R.id.fpjkView);

        FpjkWJSDKMgr.get()
                .setActivity(this)
                .setFpjkView(mFpjkView)
                .setShownBackButton(false)
                .execute();

        FpjkWJSDKMgr.get().loadUrl(url);
    }
}
