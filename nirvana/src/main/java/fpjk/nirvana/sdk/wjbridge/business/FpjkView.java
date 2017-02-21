package fpjk.nirvana.sdk.wjbridge.business;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import fpjk.nirvana.sdk.wjbridge.R;
import fpjk.nirvana.sdk.wjbridge.presenter.WJBridgeWebView;


/**
 * Summary: Created by Felix Date: 13/12/2016 Time: 16:32 QQ:74104 EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class FpjkView extends RelativeLayout {

    private ViewFlipper mViewFlipper;

    private WJBridgeWebView mDefaultWJBridgeWebView;

    private WJBridgeWebView mStrokesWJBridgeWebView;

    private ImageView mIvTitleBarBack;

    private TextView mIvTitleBarTitle;

    private boolean isShownBackButton = true;

    private Context mContext;

    public FpjkView(Context context) {
        super(context);
        build(context);
    }

    public FpjkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        build(context);
    }

    public FpjkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        build(context);
    }

    private void build(Context context) {
        View v = LayoutInflater.from(context).inflate(R.layout.fpjk_layout, null);
        mContext = context;

        mViewFlipper = (ViewFlipper) v.findViewById(R.id.viewFlipper);
        mDefaultWJBridgeWebView = (WJBridgeWebView) v.findViewById(R.id.defaultWJBridgeWebView);
        mStrokesWJBridgeWebView = (WJBridgeWebView) v.findViewById(R.id.strokesWJBridgeWebView);

        mIvTitleBarBack = (ImageView) v.findViewById(R.id.ivTitleBarBack);
        mIvTitleBarTitle = (TextView) v.findViewById(R.id.ivTitleBarTitle);

        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(-1, -1);
        addView(v, rl);
    }

    public void debugEnabled(View.OnLongClickListener o) {
        mIvTitleBarTitle.setOnLongClickListener(o);
    }

    public WJBridgeWebView getDefaultWJBridgeWebView() {
        return mDefaultWJBridgeWebView;
    }

    public void loadDefaultUrl(String url) {
        mDefaultWJBridgeWebView.loadUrl(url);
    }

    /**
     * 将cookie同步到WebView
     *
     * @param url    WebView要加载的url
     * @param cookie 要同步的cookie
     * @return true 同步cookie成功，false同步cookie失败
     */
    public void synchronizedCookie(String url, String cookie) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {

                }
            });
        } else {
            cookieManager.removeAllCookie();
        }

        cookieManager.setCookie(url, cookie);
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

    public void onBack(View.OnClickListener o) {
        mIvTitleBarBack.setOnClickListener(o);
    }

    public void setTitle(String title) {
        mIvTitleBarTitle.setText(title);
    }

    public String getTitle() {
        return mIvTitleBarTitle.getText().toString();
    }

    public boolean isShownBackButton() {
        return isShownBackButton;
    }

    public FpjkView setShownBackButton(boolean shownBackButton) {
        isShownBackButton = shownBackButton;
        return this;
    }

    public void showBackButton() {
        mIvTitleBarBack.setVisibility(View.VISIBLE);
    }

    public void hideBackButton() {
        mIvTitleBarBack.setVisibility(View.GONE);
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
}
