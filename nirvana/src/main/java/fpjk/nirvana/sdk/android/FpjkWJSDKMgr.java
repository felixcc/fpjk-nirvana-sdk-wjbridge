package fpjk.nirvana.sdk.android;

import android.app.Activity;

import fpjk.nirvana.sdk.android.business.FpjkBusiness;
import fpjk.nirvana.sdk.android.business.FpjkView;
import fpjk.nirvana.sdk.android.jsbridge.WJBridgeUtils;

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
    private Activity mActivity;
    private FpjkView mFpjkView;

    private static FpjkWJSDKMgr mFpjkWJSDKMgr = new FpjkWJSDKMgr();

    public static FpjkWJSDKMgr get() {
        return mFpjkWJSDKMgr;
    }

    private FpjkWJSDKMgr() {
    }

    public void buildConfiguration(Activity context, FpjkView fpjkView) {
        mActivity = context;
        mFpjkView = fpjkView;
        WJBridgeUtils.checkNoNull(context, "Activity not NULL!");
        FpjkBusiness.newInstance(context, fpjkView.getDefaultWJBridgeWebView()).registerSwitcher(fpjkView).execute();
    }

    public void loadUrl(String url) {
        WJBridgeUtils.checkNoNull(mFpjkView, "FpjkView not NULL!");
        mFpjkView.loadDefaultUrl(url);
    }

    public boolean canGoBack() {
        WJBridgeUtils.checkNoNull(mFpjkView, "FpjkView not NULL!");
        return mFpjkView.getDefaultWJBridgeWebView().canGoBack();
    }

    public void goBack() {
        WJBridgeUtils.checkNoNull(mFpjkView, "FpjkView not NULL!");
        mFpjkView.getDefaultWJBridgeWebView().goBack();
    }

    public boolean canGoForward() {
        WJBridgeUtils.checkNoNull(mFpjkView, "FpjkView not NULL!");
        return mFpjkView.getDefaultWJBridgeWebView().canGoForward();
    }

    public void goForward() {
        WJBridgeUtils.checkNoNull(mFpjkView, "FpjkView not NULL!");
        mFpjkView.getDefaultWJBridgeWebView().goForward();
    }

    public void reload() {
        WJBridgeUtils.checkNoNull(mFpjkView, "FpjkView not NULL!");
        mFpjkView.getDefaultWJBridgeWebView().reload();
    }
}
