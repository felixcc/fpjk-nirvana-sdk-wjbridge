package fpjk.nirvana.sdk;

import android.app.Activity;
import android.os.Bundle;

import fpjk.nirvana.sdk.wjbridge.business.FpjkView;
import fpjk.nirvana.sdk.wjbridge.business.FpjkWJSDKMgr;
import fpjk.nirvana.sdk.wjbridge.business.IReceivedStrategy;
import fpjk.nirvana.sdk.wjbridge.business.vo.FpjkTheme;
import fpjk.nirvana.sdk.wjbridge.logger.L;

public class MainActivity extends Activity {

    FpjkView mFpjkView;

//    String url = "http://10.10.232.242:9527/";//首页
//    String url = "http://10.10.191.154:9000/";//测试

    String url = "http://10.10.232.242:9527?userToken=be4bb890-fd85-11e6-ad66-193321d112c8";//测试
    String cookie = "session=Fe26.2**01941b107c9d556dd64120e0870b5e235b009b18901d0504e4c782931cda9ad9*sqtoYs7O3Q7NIY3XHkhDrg*boDPQHOSEY66Qg1-XbR5HqBI4P2YHNns11-cV2y6IME**bf7ded618bd682a7b941291355248033f844af9eb55fe6e6a720148d6366efc6*b_GBbEaeMuRKWU6r3tQQ-Vza4eXZ6eW5RgG_1fQE_zU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFpjkView = (FpjkView) findViewById(R.id.fpjkView);

        FpjkWJSDKMgr.get()
                .setActivity(this)
                .setFpjkView(mFpjkView)
                .setFpjkTheme(new FpjkTheme()
                        .setTitleBarBackBtnResId(R.mipmap.ic_launcher)
                        .setTitleBarBackgroundColorResId(R.color.colorPrimary)
                        .setTitleBarContentColor(R.color.white)
                )
                .setShownBackButton(false)
                .execute();

//        FpjkWJSDKMgr.get().loadUrl(url);
        FpjkWJSDKMgr.get().insertCookieAndLoaded(url, cookie);

        FpjkWJSDKMgr.get().onReceivedStrategy(new IReceivedStrategy() {
            @Override
            public void onReceivedLogout() {
                L.i("==================onReceivedLogout=====================");
            }

            @Override
            public void onReceivedOnPageError() {
                L.i("==================onReceivedOnPageError===================");
            }
        });
    }
}

