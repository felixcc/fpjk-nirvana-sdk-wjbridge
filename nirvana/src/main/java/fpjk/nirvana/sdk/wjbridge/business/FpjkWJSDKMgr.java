package fpjk.nirvana.sdk.wjbridge.business;

import android.app.Activity;

import fpjk.nirvana.sdk.wjbridge.data.CookieMgr;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJBridgeUtils;

/**
 * Summary:暴露给第三方的管理者 Created by Felix Date: 13/12/2016 Time: 16:03 QQ:74104
 * EMAIL:lovejiuwei@gmail.com Version 1.0
 */
public class FpjkWJSDKMgr {
    private FpjkView mFpjkView;

    private Activity mActivity;

    private boolean mShownBackButton = true;

    private static FpjkWJSDKMgr mFpjkWJSDKMgr = new FpjkWJSDKMgr();

    public static FpjkWJSDKMgr get() {
        return mFpjkWJSDKMgr;
    }

    private FpjkWJSDKMgr() {
    }

    public FpjkWJSDKMgr setActivity(Activity mActivity) {
        WJBridgeUtils.checkNoNull(mActivity, "Activity not NULL!");
        this.mActivity = mActivity;
        return this;
    }

    public FpjkWJSDKMgr setFpjkView(FpjkView mFpjkView) {
        WJBridgeUtils.checkNoNull(mFpjkView, "fpjkView not NULL!");
        this.mFpjkView = mFpjkView;
        return this;
    }

    public FpjkWJSDKMgr setShownBackButton(boolean mShownBackButton) {
        this.mShownBackButton = mShownBackButton;
        return this;
    }

    public void execute() {
        mFpjkView.setLoadedSDKShownBackButton(mShownBackButton);
        FpjkBusiness.get().buildConfiguration(mActivity, mFpjkView).execute();
    }

    public void logout(IReceiveLogoutAction iReceiveLogoutAction) {
        FpjkBusiness.get().registerLogoutAction(iReceiveLogoutAction);
    }

    public void loadUrl(String url) {
        WJBridgeUtils.checkNoNull(mFpjkView, "FpjkView not NULL!");
        mFpjkView.loadDefaultUrl(url);
    }

    public void insertCookieAndLoaded(String url, String cookie) {
        WJBridgeUtils.checkNoNull(mFpjkView, "FpjkView not NULL!");
        CookieMgr.get().insertCookieAndLoaded(mFpjkView.getDefaultWJBridgeWebView(), url, cookie);
    }

    public void goBack() {
        WJBridgeUtils.checkNoNull(mFpjkView, "FpjkView not NULL!");
        mFpjkView.goBack();
    }

    public void goForward() {
        WJBridgeUtils.checkNoNull(mFpjkView, "FpjkView not NULL!");
        mFpjkView.goForward();
    }

    public void reload() {
        WJBridgeUtils.checkNoNull(mFpjkView, "FpjkView not NULL!");
        mFpjkView.getDefaultWJBridgeWebView().reload();
    }

    public void sendMessages(String msg) {
        WJBridgeUtils.checkNoNull(mFpjkView, "FpjkView not NULL!");
//        FpjkBusiness.newInstance(context, mFpjkView.getDefaultWJBridgeWebView()).sendMessages(msg);
    }

}
