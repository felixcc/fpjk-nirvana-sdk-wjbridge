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
import fpjk.nirvana.sdk.wjbridge.business.entity.OpenUrlResponse;
import fpjk.nirvana.sdk.wjbridge.business.entity.ProcessBusinessEntity;
import fpjk.nirvana.sdk.wjbridge.business.vo.OpenUrlVo;
import fpjk.nirvana.sdk.wjbridge.data.ContactMgr;
import fpjk.nirvana.sdk.wjbridge.data.DeviceMgr;
import fpjk.nirvana.sdk.wjbridge.data.FpjkEnum;
import fpjk.nirvana.sdk.wjbridge.data.GsonMgr;
import fpjk.nirvana.sdk.wjbridge.data.LocationMgr;
import fpjk.nirvana.sdk.wjbridge.data.RecordMgr;
import fpjk.nirvana.sdk.wjbridge.data.RxBus;
import fpjk.nirvana.sdk.wjbridge.data.SmsMgr;
import fpjk.nirvana.sdk.wjbridge.data.event.EventLocation;
import fpjk.nirvana.sdk.wjbridge.data.event.EventPageFinished;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJBridgeHandler;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJWebLoader;
import fpjk.nirvana.sdk.wjbridge.logger.L;
import fpjk.nirvana.sdk.wjbridge.logger.Logger;
import fpjk.nirvana.sdk.wjbridge.presenter.WJBridgeWebView;
import io.reactivex.functions.Consumer;

/**
 * Summary:与H5之间交互的业务层
 * Created by Felix
 * Date: 01/12/2016
 * Time: 15:32
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class FpjkBusiness {
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

    public void execute() {
        processMessages();
        processRxBusEvent();
        processPageEvent();
    }

    private void processMessages() {
        if (mWebLoader.get() != null && mWebLoader.get() instanceof WJBridgeWebView) {
            final WJBridgeWebView wjBridgeWebView = (WJBridgeWebView) mWebLoader.get();
            wjBridgeWebView.registerHandler(cN, new WJBridgeHandler() {
                @Override
                public void handler(String data, WJCallbacks callbacks) {
                    dispatchMessages(data, callbacks);
                }
            });
        }

        mOpenUrlVo = new OpenUrlVo();
        mSmsMgr = SmsMgr.newInstance(mContext);
        mDeviceMgr = DeviceMgr.newInstance(mContext);
        mRecordMgr = RecordMgr.newInstance(mContext);
        mContactMgr = ContactMgr.newInstance(mContext);
        mLocationMgr = LocationMgr.newInstance(mContext);
        Logger.init("Fpjk");
    }

    private void processRxBusEvent() {
        RxBus.get().asFlowable().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof EventLocation) {
                    String mLocationInfo = ((EventLocation) o).getLocationInfo();
                    WJCallbacks wjCallbacks = ((EventLocation) o).getWjCallbacks();
                    wjCallbacks.onCallback(mLocationInfo);
                }
                L.d("toObserverable[%s]", o);
            }
        });
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
                mContactMgr.obtainContacts(uid, wjCallbacks);
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
                String callBackJson = GsonMgr.get().toJSONString(cookieEntity);
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
                String json = GsonMgr.get().toJSONString(deviceInfoEntity);
                wjCallbacks.onCallback(json);
            } else if (FpjkEnum.Business.GET_LOCATION.getValue().equals(entity.getOpt())) {
                mLocationMgr.start(wjCallbacks);
            } else if (FpjkEnum.Business.GET_SMS_RECORDS.getValue().equals(entity.getOpt())) {
                DataTransferEntity dataTransferEntity = entity.getData();
                long uid = dataTransferEntity.getUid();
                mSmsMgr.obtainSms(uid, wjCallbacks);
            } else if (FpjkEnum.Business.GET_CALL_RECORDS.getValue().equals(entity.getOpt())) {
                DataTransferEntity dataTransferEntity = entity.getData();
                long uid = dataTransferEntity.getUid();
                mRecordMgr.obtainRecords(uid, wjCallbacks);
            } else if (FpjkEnum.Business.REFRESH_NAVIGATION.getValue().equals(entity.getOpt())) {
                OpenUrlResponse openUrlResponse = new OpenUrlResponse();
                openUrlResponse.setSuccess(1);
                String callBack = GsonMgr.get().toJSONString(openUrlResponse);
                wjCallbacks.onCallback(callBack);
                processCanGoBack();
            }
        } catch (Exception e) {
            L.e("JavaScript invoke Native is Error ^ JSON->[%S] Error->[%s]", jsonData, e);
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

    private void processStrokes(final DataTransferEntity dataTransferEntity, final WJCallbacks wjCallbacks) {
        //如果进入到 OpenUrl 界面则自动记录 Title 以及是否展示状态
        mOpenUrlVo.setTitle(mFpjkView.getTitle());
        mOpenUrlVo.setShownBackButton(mFpjkView.isShownBackButton());

        mFpjkView.showBackButton();
        mFpjkView.showStrokesTab();
        mFpjkView.setTitle(dataTransferEntity.getTitle());
        mFpjkView.loadStrokesUrl(dataTransferEntity.getUrl());

        RxBus.get().asFlowable().subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                if (o instanceof EventPageFinished) {
                    String matchingUrl = ((EventPageFinished) o).getCurrentUrl();
                    //如果淘宝登录成功会关闭当前页面，返回上一个页面状态。
                    if (matchingUrl.startsWith(dataTransferEntity.getFinishUrl())) {
                        processWhenStrokesGoBackInitialState(wjCallbacks, FpjkEnum.OpenUrlStatus.AUTO_SHUTDOWN.getValue());
                        L.d("匹配到了指定URL，即将爆炸[%s]", o);
                    }
                }
            }
        });

        mFpjkView.onBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mFpjkView.isDisplayDefatultView()) {
                    processWhenStrokesGoBackInitialState(wjCallbacks, FpjkEnum.OpenUrlStatus.USER_SHUTDOWN.getValue());
                }
            }
        });
    }

    /**
     * 跳转到 openurl 处理返回的状态
     */
    private void processWhenStrokesGoBackInitialState(WJCallbacks wjCallbacks, Integer value) {
        //call JS
        OpenUrlResponse openUrlResponse = new OpenUrlResponse();
        openUrlResponse.setSuccess(value);
        String callBack = GsonMgr.get().toJSONString(openUrlResponse);
        wjCallbacks.onCallback(callBack);
        //review page
        mFpjkView.showDefaultTab();
        mFpjkView.setTitle(mOpenUrlVo.getTitle());
        processCanGoBack();
    }

    /**
     * 处理 SDK 页面事件
     */
    private void processPageEvent() {
        //title
        mFpjkView.setTitle("涅槃");
        processCanGoBack();

        mFpjkView.onBack(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processCanGoBack();
            }
        });
    }

    private void processCanGoBack() {
        if (mFpjkView.isDisplayDefatultView()) {
            if (mFpjkView.canGoBack()) {
                mFpjkView.goBack();
            } else {
                //default btn
                if (mFpjkView.isShownBackButton()) {
                    mFpjkView.showBackButton();
                } else {
                    mFpjkView.hideBackButton();
                }
            }
        }
    }

}
