package fpjk.nirvana.sdk.android;

import android.app.Activity;

import fpjk.nirvana.sdk.android.business.FpjkBusiness;
import fpjk.nirvana.sdk.android.business.FpjkView;

/**
 * Summary:暴露给第三方的管理者
 * Created by Felix
 * Date: 13/12/2016
 * Time: 16:03
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class FpjkWJSDKMgr {
    public static void initWebView(FpjkView fpjkView, Activity activity, String url) {
        FpjkBusiness.newInstance(activity, fpjkView.getDefaultWJBridgeWebView()).registerSwitcher(fpjkView).execute();
        fpjkView.loadDefaultUrl(url);
    }
}
