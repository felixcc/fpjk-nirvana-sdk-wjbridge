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

    String url = "http://10.10.232.242:9527/";//首页
//    String url = "http://10.10.191.154:9000/";//测试

//    String url = "http://10.10.232.242:9527?userToken=010fc460-fd8f-11e6-82fd-599da5512996";//测试
//    String cookie = "session=Fe26.2**89c4558be0f32550999806d45bd387803d8ffbbef2c5d4a5041f80f58e435504*r8SayjVV8oGKSkJnPuhB7g*IM6B6mNbD7HQeNivszGxX7QFnIXBdKLUp20eytelW4E**3c66c8ef971178fb187c8a1916c39c86ed09f1900226ff6b068a58fac2be9d04*2suIRw-vZRQpvSjgCFp0awqEUBMlUg769S_RlFbC_9E";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFpjkView = (FpjkView) findViewById(R.id.fpjkView);

        FpjkWJSDKMgr.get()
                .setUs("iqianzhan")
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

