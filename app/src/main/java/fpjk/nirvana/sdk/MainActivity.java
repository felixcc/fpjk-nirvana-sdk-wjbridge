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
    String url = "http://10.10.191.154:9000/";//测试

//    String url = "http://10.10.232.242:9527?userToken=aeca9150-fcb5-11e6-aca4-c12eb512c5f9";//测试
//    String cookie = "session=Fe26.2**8b3ede3f4d100381543a7f5205e9db8c7ac9347369f04e8be54c33cddf882780*YSN3oh4-uzS6rqn42gDHbw*Jm9OjuWleUSryozYcjTRxtNuy5D7PLcgVyi69P7wndI**453eb1d434271b404ef8ccb21927081cb3f073c66dbdb126cbc058d000e36ed0*MzvViEoimNq6nsXoBLQ3lSPtGF3OWpzb-2BWpLeCUgE";

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

        FpjkWJSDKMgr.get().loadUrl(url);
//        FpjkWJSDKMgr.get().insertCookieAndLoaded(url, cookie);

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
