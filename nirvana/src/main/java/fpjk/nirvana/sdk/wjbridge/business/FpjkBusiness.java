package fpjk.nirvana.sdk.wjbridge.business;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.webkit.CookieManager;

import org.apache.commons.lang.StringUtils;

import java.lang.ref.WeakReference;

import fpjk.nirvana.sdk.wjbridge.business.entity.CookieEntity;
import fpjk.nirvana.sdk.wjbridge.business.entity.DataTransferEntity;
import fpjk.nirvana.sdk.wjbridge.business.entity.DeviceInfoEntity;
import fpjk.nirvana.sdk.wjbridge.business.entity.LocationEntity;
import fpjk.nirvana.sdk.wjbridge.business.entity.ProcessBusinessEntity;
import fpjk.nirvana.sdk.wjbridge.business.entity.SuccessResponse;
import fpjk.nirvana.sdk.wjbridge.business.vo.OpenUrlVo;
import fpjk.nirvana.sdk.wjbridge.data.ContactMgr;
import fpjk.nirvana.sdk.wjbridge.data.CookieMgr;
import fpjk.nirvana.sdk.wjbridge.data.DeviceMgr;
import fpjk.nirvana.sdk.wjbridge.data.FpjkEnum;
import fpjk.nirvana.sdk.wjbridge.data.GsonMgr;
import fpjk.nirvana.sdk.wjbridge.data.IReturnJSJson;
import fpjk.nirvana.sdk.wjbridge.data.LocationMgr;
import fpjk.nirvana.sdk.wjbridge.data.RecordMgr;
import fpjk.nirvana.sdk.wjbridge.data.RxBus;
import fpjk.nirvana.sdk.wjbridge.data.SmsMgr;
import fpjk.nirvana.sdk.wjbridge.data.event.EventLocation;
import fpjk.nirvana.sdk.wjbridge.data.event.EventOnProgressChanged;
import fpjk.nirvana.sdk.wjbridge.data.event.EventPageReceivedError;
import fpjk.nirvana.sdk.wjbridge.data.event.EventPageReceivedFinished;
import fpjk.nirvana.sdk.wjbridge.data.event.EventPageReceivedStarted;
import fpjk.nirvana.sdk.wjbridge.data.event.EventPageReceivedTitle;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJBridgeHandler;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJWebLoader;
import fpjk.nirvana.sdk.wjbridge.logger.L;
import fpjk.nirvana.sdk.wjbridge.logger.Logger;
import fpjk.nirvana.sdk.wjbridge.presenter.WJBridgeWebView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Summary:与H5之间交互的业务层 Created by Felix Date: 01/12/2016 Time: 15:32 QQ:74104
 * EMAIL:lovejiuwei@gmail.com Version 1.0
 */
public class FpjkBusiness extends IReturnJSJson {
    private WeakReference<WJWebLoader> mWebLoader;
    private final String cN = "fpjkBridgeCallNative";
    private final String cJ = "fpjkBridgeCallJavaScript";

    private OpenUrlVo mOpenUrlVo;
    private FpjkView mFpjkView;
    private Activity mContext;
    private DeviceMgr mDeviceMgr;
    private ContactMgr mContactMgr;
    private LocationMgr mLocationMgr;
    private RecordMgr mRecordMgr;
    private SmsMgr mSmsMgr;

    private String mFailingUrl;

    //strokes start
    private boolean mSwitchTheStrokesShownTitle = false;
    private WJCallbacks mStrokesWjCallbacks;
    //strokes end
    private IReceiveLogoutAction mIReceiveLogoutAction;

    //rxjava
    private CompositeDisposable mStrokesCompositeDisposable;//openurl 注册了一个，单独清理
    private CompositeDisposable mCompositeDisposable;//全局

    private static FpjkBusiness mFpjkBusiness = new FpjkBusiness();

    public static FpjkBusiness get() {
        return mFpjkBusiness;
    }

    private FpjkBusiness() {
    }

    public FpjkBusiness buildConfiguration(Activity activity, FpjkView fpjkView) {
        WJWebLoader webLoader = fpjkView.getDefaultWJBridgeWebView();
        mWebLoader = new WeakReference<>(webLoader);
        mContext = activity;
        mFpjkView = fpjkView;
        return this;
    }

    public FpjkBusiness registerLogoutAction(IReceiveLogoutAction mIReceiveLogoutAction) {
        this.mIReceiveLogoutAction = mIReceiveLogoutAction;
        return this;
    }

    public void execute() {
        processConfiguration();
        processRxBusEvent();
        processPageEvent();
    }

    private void processConfiguration() {
        if (mWebLoader.get() != null && mWebLoader.get() instanceof WJBridgeWebView) {
            final WJBridgeWebView wjBridgeWebView = (WJBridgeWebView) mWebLoader.get();
            wjBridgeWebView.registerHandler(cN, new WJBridgeHandler() {
                @Override
                public void handler(String data, WJCallbacks callbacks) {
                    dispatchMessages(data, callbacks);
                }
            });
        }

        mStrokesCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable = new CompositeDisposable();

        mOpenUrlVo = new OpenUrlVo();
        mDeviceMgr = DeviceMgr.newInstance(mContext);

        mSmsMgr = SmsMgr.newInstance(mContext, mDeviceMgr);
        mRecordMgr = RecordMgr.newInstance(mContext, mDeviceMgr);
        mContactMgr = ContactMgr.newInstance(mContext, mDeviceMgr);
        mLocationMgr = LocationMgr.newInstance(mContext, mDeviceMgr);

        Logger.init("Fpjk");
    }

    private void processRxBusEvent() {
        mCompositeDisposable.add(RxBus.get().asFlowable().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                //location
                if (o instanceof EventLocation) {
                    LocationEntity locationEntity = ((EventLocation) o).getLocationEntity();
                    WJCallbacks wjCallbacks = ((EventLocation) o).getWjCallbacks();
                    String callBackJson = buildReturnCorrectJSJson(locationEntity);
                    wjCallbacks.onCallback(callBackJson);
                }
                //webview
                if (o instanceof EventPageReceivedStarted) {
                    L.d("EventPageReceivedStarted");
                }
                if (o instanceof EventPageReceivedFinished) {
                    L.d("EventPageReceivedFinished");
                }
                if (o instanceof EventPageReceivedTitle) {
                    //get document title 晚于与JS交互
                    if (mSwitchTheStrokesShownTitle) {
                        mSwitchTheStrokesShownTitle = false;
                        return;
                    }
                    String title = ((EventPageReceivedTitle) o).getTitle();
                    if (title.contains("找不到") ||
                            title.contains("不到") ||
                            title.contains("找") ||
                            title.contains("error") ||
                            title.contains("denied") ||
                            title.contains("webview")) {
                        mFpjkView.setTitle("钱站");
                    } else {
                        mFpjkView.setTitle(title);
                    }
                    L.d("EventPageReceivedTitle", title);
                }
                if (o instanceof EventPageReceivedError) {
                    L.d("EventPageReceivedError");
                    EventPageReceivedError error = (EventPageReceivedError) o;
                    boolean mPageReceivedError = error.isPageReceivedError();
                    mFailingUrl = error.getFailingUrl();
                    try {
                        if (!mPageReceivedError) {
                            mFpjkView.getWebViewEmptyLayout().dismiss();
                        } else {
                            mFpjkView.getWebViewEmptyLayout().display();
                        }
                    } catch (Throwable e) {
                        L.e("", e);
                    }
                }
                if (o instanceof EventOnProgressChanged) {
                    int newProgress = ((EventOnProgressChanged) o).getNewProgress();
                    L.d("EventOnProgressChanged[%s]", newProgress);
                    if (newProgress == 100) {
                        mFpjkView.getWebViewScaleProgressBar().setProgress(newProgress);
                        mFpjkView.getWebViewScaleProgressBar().playFinishAnim();
                    } else {
                        if (View.INVISIBLE == mFpjkView.getWebViewScaleProgressBar().getVisibility()) {
                            mFpjkView.getWebViewScaleProgressBar().setVisibility(View.VISIBLE);
                            mFpjkView.getWebViewScaleProgressBar().setProgress(newProgress);
                        } else {
                            mFpjkView.getWebViewScaleProgressBar().setProgressSmooth(newProgress, true);
                        }
                    }
                }
                L.d("toObserverable[%s]", o);
            }
        }));
    }

    /**
     * 处理和 JS 交互的逻辑
     */
    private void dispatchMessages(String jsonData, final WJCallbacks wjCallbacks) {
        if (StringUtils.isEmpty(jsonData)) {
            return;
        }
        L.json(jsonData);
        try {
            ProcessBusinessEntity entity = GsonMgr.get().json2Object(jsonData, ProcessBusinessEntity.class);
            if (FpjkEnum.Business.GET_CONTACTS.getValue().equals(entity.getOpt())) {
                DataTransferEntity dataTransferEntity = entity.getData();
                long uid = dataTransferEntity.getUid();
                mContactMgr.obtainContacts(mDeviceMgr.getIMEI(), uid, wjCallbacks);
            } else if (FpjkEnum.Business.OPEN_URL.getValue().equals(entity.getOpt())) {
                DataTransferEntity dataTransferEntity = entity.getData();
                processStrokes(dataTransferEntity, wjCallbacks);
            } else if (FpjkEnum.Business.GET_COOKIE.getValue().equals(entity.getOpt())) {
                if (StringUtils.isEmpty(entity.getData().getUrl())) {
                    return;
                }
                DataTransferEntity dataTransferEntity = entity.getData();

                CookieManager manager = CookieManager.getInstance();
                String cookie = manager.getCookie(dataTransferEntity.getUrl());

                CookieEntity cookieEntity = mDeviceMgr.formatCookie(cookie);
                String callBackJson = buildReturnCorrectJSJson(cookieEntity);
                wjCallbacks.onCallback(callBackJson);
            } else if (FpjkEnum.Business.GET_DEVICE_INFO.getValue().equals(entity.getOpt())) {
                DeviceInfoEntity deviceInfoEntity = new DeviceInfoEntity();
                deviceInfoEntity.setDeviceInfo(new DeviceInfoEntity.DeviceInfo()
                        .setOs("android")
                        .setSysVersion(mDeviceMgr.getSyVersion())
                        .setUs("us")
                        .setDeviceState(mDeviceMgr.isEmulator() + "")
                        .setVersion(mDeviceMgr.getVersionName())
                        .setVersionCode(mDeviceMgr.getVersionCode() + "")
                        .setDeviceModel(Build.MODEL)
                        .setPid(mDeviceMgr.getIMEI()));
                deviceInfoEntity.setAppList(mDeviceMgr.getIntalledAppList());
                String callBackJson = buildReturnCorrectJSJson(deviceInfoEntity);
                wjCallbacks.onCallback(callBackJson);
            } else if (FpjkEnum.Business.GET_LOCATION.getValue().equals(entity.getOpt())) {
                mLocationMgr.start(wjCallbacks);
            } else if (FpjkEnum.Business.GET_SMS_RECORDS.getValue().equals(entity.getOpt())) {
                DataTransferEntity dataTransferEntity = entity.getData();
                long uid = dataTransferEntity.getUid();
                mSmsMgr.obtainSms(mDeviceMgr.getIMEI(), uid, wjCallbacks);
            } else if (FpjkEnum.Business.GET_CALL_RECORDS.getValue().equals(entity.getOpt())) {
                DataTransferEntity dataTransferEntity = entity.getData();
                long uid = dataTransferEntity.getUid();
                mRecordMgr.obtainRecords(mDeviceMgr.getIMEI(), uid, wjCallbacks);
            } else if (FpjkEnum.Business.REFRESH_NAVIGATION.getValue().equals(entity.getOpt())) {
                SuccessResponse successResponse = new SuccessResponse();
                successResponse.setSuccess(1);
                String callBackJson = buildReturnCorrectJSJson(successResponse);
                wjCallbacks.onCallback(callBackJson);
                processRefreshNavigation();
            } else if (FpjkEnum.Business.LOGOUT.getValue().equals(entity.getOpt())) {
                SuccessResponse successResponse = new SuccessResponse();
                successResponse.setSuccess(1);
                String callBackJson = buildReturnCorrectJSJson(successResponse);
                wjCallbacks.onCallback(callBackJson);
                if (null != mIReceiveLogoutAction) {
                    mIReceiveLogoutAction.onReceive();
                }
            }
        } catch (Exception e) {
            buildJSCallNativeParseError(wjCallbacks);
            L.e("JavaScript invoke Native is Error ^ JSON->[%S] Error->[%s]", jsonData, e);
        }
    }

    private void processStrokes(final DataTransferEntity dataTransferEntity, final WJCallbacks wjCallbacks) {
        //切换到OpenUrl模式
        mSwitchTheStrokesShownTitle = true;
        //切换到OpenUrl 模式的点击判定
        mStrokesWjCallbacks = wjCallbacks;
        //如果进入到 OpenUrl 界面则自动记录 Title 以及是否展示状态
        mOpenUrlVo.setTitle(mFpjkView.getTitle());
        mOpenUrlVo.setShownBackButton(mFpjkView.isPrePageBackButtonDisplayState());
        //titlebar
        mFpjkView.showBackButton();
        mFpjkView.showStrokesTab();
        mFpjkView.setTitle(dataTransferEntity.getTitle());
        mFpjkView.loadStrokesUrl(dataTransferEntity.getUrl());

        mStrokesCompositeDisposable.add(RxBus.get().asFlowable().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof EventPageReceivedFinished) {
                    String matchingUrl = ((EventPageReceivedFinished) o).getCurrentUrl();
                    //如果淘宝登录成功会关闭当前页面，返回上一个页面状态。
                    if (matchingUrl.startsWith(dataTransferEntity.getFinishUrl())) {
                        processWhenStrokesGoBackInitialState(wjCallbacks, FpjkEnum.OpenUrlStatus.AUTO_SHUTDOWN.getValue());
                        L.d("匹配到了指定URL，即将爆炸[%s]", o);
                    }
                }
            }
        }));
    }

    /**
     * 跳转到 openurl 处理返回的状态
     */
    private void processWhenStrokesGoBackInitialState(WJCallbacks wjCallbacks, Integer value) {
        //call JS
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setSuccess(value);
        String callBack = buildReturnCorrectJSJson(successResponse);
        wjCallbacks.onCallback(callBack);
        //review page
        mFpjkView.showDefaultTab();
        mFpjkView.setTitle(mOpenUrlVo.getTitle());
        //clear rxjava heap
        mStrokesCompositeDisposable.clear();
        //clear obj
        mStrokesWjCallbacks = null;
        if (mOpenUrlVo.isShownBackButton()) {
            mFpjkView.showBackButton();
        } else {
            mFpjkView.hideBackButton();
        }
    }

    /**
     * 处理 SDK 页面事件
     */
    private void processPageEvent() {
        //title
        processRefreshNavigation();

        //titlebar backbtn onclick
        mFpjkView.onBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCanGoBack();
                processRefreshNavigation();
            }
        });

        //empty
        mFpjkView.getWebViewEmptyLayout().setOnRefreshClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFpjkView.getWebViewEmptyLayout().dismiss();
                if (StringUtils.isNotEmpty(mFailingUrl)) {
                    mFpjkView.getDefaultWJBridgeWebView().loadUrl(mFailingUrl);
                    mFpjkView.getWebViewScaleProgressBar().setProgress(0);
                }
            }
        });
    }

    private void processRefreshNavigation() {
        if (mFpjkView.canGoBack()) {
            mFpjkView.showBackButton();
        } else {
            //default btn
            if (mFpjkView.isLoadedSDKShownBackButton()) {
                mFpjkView.showBackButton();
            } else {
                mFpjkView.hideBackButton();
            }
        }
    }

    private void processCanGoBack() {
        //如果切换到了 openurl 模式，则只关闭当前页面，反之正常逻辑。
        if (null != mStrokesWjCallbacks && !mFpjkView.isDisplayDefatultView()) {
            processWhenStrokesGoBackInitialState(mStrokesWjCallbacks, FpjkEnum.OpenUrlStatus.USER_SHUTDOWN.getValue());
            return;
        }
        //正常逻辑
        if (mFpjkView.canGoBack()) {
            mFpjkView.goBack();
        }
    }

    public void sendMessages(String sendData) {
        if (mWebLoader.get() != null && mWebLoader.get() instanceof WJBridgeWebView) {
            WJBridgeWebView wjBridgeWebView = (WJBridgeWebView) mWebLoader.get();
            wjBridgeWebView.callHandler(cJ, sendData, new WJCallbacks() {
                @Override
                public void onCallback(String data) {
                    L.d(data);
                }
            });
        }
    }

    public void clear() {
        mCompositeDisposable.clear();
        mFpjkView.clear();
        CookieMgr.get().remoeAllCookies();
    }

    @Override
    public String imei() {
        return mDeviceMgr.getIMEI();
    }
}
