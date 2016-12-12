package fpjk.nirvana.sdk.android.business;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.CookieManager;

import org.apache.commons.lang.StringUtils;

import java.lang.ref.WeakReference;

import fpjk.nirvana.sdk.android.OpenUrlActivity;
import fpjk.nirvana.sdk.android.data.ContactMgr;
import fpjk.nirvana.sdk.android.data.DeviceMgr;
import fpjk.nirvana.sdk.android.data.FpjkEnum;
import fpjk.nirvana.sdk.android.data.GsonMgr;
import fpjk.nirvana.sdk.android.data.LocationMgr;
import fpjk.nirvana.sdk.android.data.RxBus;
import fpjk.nirvana.sdk.android.data.event.EventLocation;
import fpjk.nirvana.sdk.android.data.event.EventPageFinished;
import fpjk.nirvana.sdk.android.jsbridge.WJBridgeHandler;
import fpjk.nirvana.sdk.android.jsbridge.WJBridgeUtils;
import fpjk.nirvana.sdk.android.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.android.jsbridge.WJWebLoader;
import fpjk.nirvana.sdk.android.logger.L;
import fpjk.nirvana.sdk.android.logger.Logger;
import fpjk.nirvana.sdk.android.presenter.WJBridgeWebView;
import rx.functions.Action1;

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

    private Activity mContext;
    private DeviceMgr mDeviceMgr;
    private ContactMgr mContactMgr;
    private LocationMgr mLocationMgr;
    private int mWJWebLoaderWidth = 0;
    private int mWJWebLoaderHeight = 0;

    public static FpjkBusiness newInstance(WJWebLoader webLoader) {
        return new FpjkBusiness(WJBridgeUtils.checkNoNull(webLoader, "WJWebLoader not NULL!"));
    }

    private FpjkBusiness(@NonNull WJWebLoader webLoader) {
        mWebLoader = new WeakReference<>(webLoader);
        Logger.init("Fpjk");
    }

    public void process() {
        processMessages();
        processCookieEvent();
    }

    private void processCookieEvent() {
        RxBus.get().toObserverable().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o instanceof EventLocation) {
                    String mLocationInfo = ((EventLocation) o).getLocationInfo();
                    WJCallbacks wjCallbacks = ((EventLocation) o).getWjCallbacks();
                    wjCallbacks.onCallback(mLocationInfo);
                }
                if (o instanceof EventPageFinished) {
                }
                L.d("toObserverable[%s]", o);
            }
        });
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

            wjBridgeWebView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    mWJWebLoaderWidth = wjBridgeWebView.getWidth();
                    mWJWebLoaderHeight = wjBridgeWebView.getHeight();
                    L.d("addOnLayoutChangeListener.width[%s],height[%s]", mWJWebLoaderWidth, mWJWebLoaderHeight);
                }
            });

            mContext = (Activity) wjBridgeWebView.getContext();
            mDeviceMgr = DeviceMgr.newInstance(mContext);
            mContactMgr = ContactMgr.newInstance(mContext);
            mLocationMgr = LocationMgr.newInstance(mContext);
        }
    }

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
                Intent intent = new Intent(mContext, OpenUrlActivity.class);
                intent.putExtra(OpenUrlActivity.EXTRA_KEY, dataTransferEntity);
                mContext.startActivity(intent);
            } else if (FpjkEnum.Business.GET_COOKIE.getValue().equals(entity.getOpt())) {
                if (StringUtils.isEmpty(entity.getData().getUrl())) {
                    return;
                }
                DataTransferEntity dataTransferEntity = entity.getData();
                String cookie = CookieManager.getInstance().getCookie(dataTransferEntity.getUrl());
                CookieEntity cookieEntity = mDeviceMgr.formatCookie(cookie);
                wjCallbacks.onCallback(GsonMgr.get().toJSONString(cookieEntity));
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
                String json = GsonMgr.get().toJSONString(deviceInfoEntity);
                wjCallbacks.onCallback(json);
            } else if (FpjkEnum.Business.GET_LOCATION.getValue().equals(entity.getOpt())) {
                mLocationMgr.start(wjCallbacks);
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
                }
            });
        }
    }
}
