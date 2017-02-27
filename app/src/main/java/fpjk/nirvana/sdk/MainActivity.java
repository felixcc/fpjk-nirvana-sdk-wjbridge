package fpjk.nirvana.sdk;

import android.app.Activity;
import android.os.Bundle;

import fpjk.nirvana.sdk.wjbridge.business.FpjkView;
import fpjk.nirvana.sdk.wjbridge.business.FpjkWJSDKMgr;
import fpjk.nirvana.sdk.wjbridge.business.IReceiveLogoutAction;
import fpjk.nirvana.sdk.wjbridge.business.vo.FpjkTheme;
import fpjk.nirvana.sdk.wjbridge.logger.L;

public class MainActivity extends Activity {

    FpjkView mFpjkView;

    String url = "http://10.10.232.242:9527/";//首页
    //    String url = "http://10.10.191.154:9000/";//测试
    String cookie = "session=Fe26.2**1f37371601fa6b359b162bac2a33c9716c3687b76813182d68c1f26ee6fd17bd*nIrCqGQf02fWc8CPOefnpw*Z7JJ1og7tGX32tHBMjV1WpVsvbkpNnFMDe8NSVqECqw**bab0966f76d0a6d653f0aa90ab13306c4b81845f6b49b671bc617ddf2d1e9140*COwRICQcZL93DHcoqpcxAco32t6qXz-Mw17vhAq85e4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFpjkView = (FpjkView) findViewById(R.id.fpjkView);

        FpjkWJSDKMgr.get()
                .setActivity(this)
                .setFpjkView(mFpjkView)
                .setFpjkTheme(new FpjkTheme()
//                        .setTitleBarBackBtnResId(R.mipmap.ic_launcher)
//                        .setTitleBarBackgroundColorResId(R.color.colorAccent)
                )
                .setShownBackButton(true)
                .execute();

        FpjkWJSDKMgr.get().loadUrl(url);
//        FpjkWJSDKMgr.get().insertCookieAndLoaded(url, cookie);

        FpjkWJSDKMgr.get().logout(new IReceiveLogoutAction() {
            @Override
            public void onReceive() {
                L.i("Logout=====================================");
            }
        });
    }

}
