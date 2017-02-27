package fpjk.nirvana.sdk.wjbridge.business;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import fpjk.nirvana.sdk.wjbridge.R;
import fpjk.nirvana.sdk.wjbridge.Titlebar;
import fpjk.nirvana.sdk.wjbridge.WebViewEmptyLayout;
import fpjk.nirvana.sdk.wjbridge.WebViewScaleProgressBar;
import fpjk.nirvana.sdk.wjbridge.business.vo.FpjkTheme;
import fpjk.nirvana.sdk.wjbridge.presenter.WJBridgeWebView;


/**
 * Summary: Created by Felix Date: 13/12/2016 Time: 16:32 QQ:74104 EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class FpjkView extends RelativeLayout {

    private ViewFlipper mViewFlipper;

    private WJBridgeWebView mDefaultWJBridgeWebView;

    private WJBridgeWebView mStrokesWJBridgeWebView;

    private WebViewScaleProgressBar mWebViewScaleProgressBar;

    private WebViewEmptyLayout mWebViewEmptyLayout;

    private View mTitlebarContent;

    private Titlebar mTitlebar;

    //theme
    private FpjkTheme mFpjkTheme;

    //只针对第一次加载 SDK 是否显示回退按钮。
    private boolean isLoadedSDKShownBackButton = true;

    //如果打开新页面，则记录当前回退按钮展示状态。
    private boolean misPrePageBackButtonDisplayState = false;

    public FpjkView(Context context) {
        this(context, null);
    }

    public FpjkView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FpjkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        buildConfigs(context);
    }

    private void buildConfigs(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.fpjk_layout, this);

        mTitlebarContent = v.findViewById(R.id.titlebarContent);

        mViewFlipper = (ViewFlipper) v.findViewById(R.id.viewFlipper);
        mDefaultWJBridgeWebView = (WJBridgeWebView) v.findViewById(R.id.defaultWJBridgeWebView);
        mStrokesWJBridgeWebView = (WJBridgeWebView) v.findViewById(R.id.strokesWJBridgeWebView);

        mWebViewEmptyLayout = (WebViewEmptyLayout) v.findViewById(R.id.webViewEmptyLayout);
        mWebViewScaleProgressBar = (WebViewScaleProgressBar) v.findViewById(R.id.webViewScaleProgressBar);

        mTitlebar = (Titlebar) v.findViewById(R.id.titleBar);
    }

    /*******************
     * WebViewScaleProgressBar start *
     *******************/
    public WebViewScaleProgressBar getWebViewScaleProgressBar() {
        return mWebViewScaleProgressBar;
    }
    /*******************
     * WebViewScaleProgressBar end *
     *******************/


    /*******************
     * WebViewEmptyLayout start *
     *******************/
    public WebViewEmptyLayout getWebViewEmptyLayout() {
        return mWebViewEmptyLayout;
    }
    /*******************
     * WebViewEmptyLayout end *
     *******************/


    /*******************
     * WJBridgeWebView start *
     *******************/
    public WJBridgeWebView getDefaultWJBridgeWebView() {
        return mDefaultWJBridgeWebView;
    }

    public void loadDefaultUrl(String url) {
        mDefaultWJBridgeWebView.loadUrl(url);
    }

    public boolean isDisplayDefatultView() {
        return mViewFlipper.getDisplayedChild() == 0;
    }

    public void showDefaultTab() {
        mViewFlipper.setDisplayedChild(0);
    }

    public void showStrokesTab() {
        mViewFlipper.setDisplayedChild(1);
    }

    public void loadStrokesUrl(String url) {
        mStrokesWJBridgeWebView.loadUrl(url);
    }

    public void strokesClear() {
        mStrokesWJBridgeWebView.clearHistory();
        mStrokesWJBridgeWebView.clearFormData();
        mStrokesWJBridgeWebView.clearCache(true);
    }

    public boolean canGoBack() {
        return getDefaultWJBridgeWebView().canGoBack();
    }

    public void goBack() {
        getDefaultWJBridgeWebView().goBack();
    }

    public boolean canGoForward() {
        return getDefaultWJBridgeWebView().canGoForward();
    }

    public void goForward() {
        getDefaultWJBridgeWebView().goForward();
    }

    /*******************
     * WJBridgeWebView end *
     *******************/


    /*******************
     * titlebar start *
     *******************/
    public void onBack(View.OnClickListener o) {
        mTitlebar.onBack(o);
    }

    public void setTitle(String title) {
        mTitlebar.setTitle(title);
    }

    public String getTitle() {
        return mTitlebar.getTitle();
    }

    public void showBackButton() {
        mTitlebar.showBackButton();
        misPrePageBackButtonDisplayState = true;
    }

    public void hideBackButton() {
        mTitlebar.hideBackButton();
        misPrePageBackButtonDisplayState = false;
    }

    public void setTitlebarBackgroupdColor(@ColorRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTitlebarContent.setBackgroundColor(getResources().getColor(resId, null));
        } else {
            mTitlebarContent.setBackgroundColor(getResources().getColor(resId));
        }
    }

    public void setBackBtnResId(@DrawableRes int resId) {
        mTitlebar.setBackBtnResId(resId);
    }

    public boolean isPrePageBackButtonDisplayState() {
        return misPrePageBackButtonDisplayState;
    }
    /*******************
     * titlebar end *
     *******************/


    /*******************
     * isLoadedSDKShownBackButton start *
     *******************/
    public boolean isLoadedSDKShownBackButton() {
        return isLoadedSDKShownBackButton;
    }

    public FpjkView setLoadedSDKShownBackButton(boolean loadedSDKShownBackButton) {
        isLoadedSDKShownBackButton = loadedSDKShownBackButton;
        return this;
    }
    /*******************
     * isLoadedSDKShownBackButton end *
     *******************/


    /*******************
     * finish start *
     *******************/
    public void clear() {
        mDefaultWJBridgeWebView.removeAllViews();
        mStrokesWJBridgeWebView.removeAllViews();
    }
    /*******************
     * finish end *
     *******************/
}
