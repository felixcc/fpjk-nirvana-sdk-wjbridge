package fpjk.nirvana.sdk.wjbridge.business;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.webkit.CookieManager;

import org.apache.commons.lang.StringUtils;

import java.lang.ref.WeakReference;

import fpjk.nirvana.sdk.wjbridge.business.entity.CookieEntity;
import fpjk.nirvana.sdk.wjbridge.business.entity.DataTransferEntity;
import fpjk.nirvana.sdk.wjbridge.business.entity.DeviceInfoEntity;
import fpjk.nirvana.sdk.wjbridge.business.entity.ProcessBusinessEntity;
import fpjk.nirvana.sdk.wjbridge.data.ContactMgr;
import fpjk.nirvana.sdk.wjbridge.data.DeviceMgr;
import fpjk.nirvana.sdk.wjbridge.data.FpjkEnum;
import fpjk.nirvana.sdk.wjbridge.data.GsonMgr;
import fpjk.nirvana.sdk.wjbridge.data.LocationMgr;
import fpjk.nirvana.sdk.wjbridge.data.RecordMgr;
import fpjk.nirvana.sdk.wjbridge.data.RxBus;
import fpjk.nirvana.sdk.wjbridge.data.SmsMgr;
import fpjk.nirvana.sdk.wjbridge.data.event.EventLocation;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJBridgeHandler;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJBridgeUtils;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJWebLoader;
import fpjk.nirvana.sdk.wjbridge.logger.L;
import fpjk.nirvana.sdk.wjbridge.logger.Logger;
import fpjk.nirvana.sdk.wjbridge.presenter.WJBridgeWebView;
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
    private RecordMgr mRecordMgr;
    private SmsMgr mSmsMgr;
    private ITabViewSwitcher mITabViewSwitcher;

    public interface ITabViewSwitcher {
        void showOpenUrlTab(DataTransferEntity dataTransferEntity, WJCallbacks wjCallbacks);
    }

    public static FpjkBusiness newInstance(Activity activity, WJWebLoader webLoader) {
        return new FpjkBusiness(activity, WJBridgeUtils.checkNoNull(webLoader, "WJWebLoader not NULL!"));
    }

    private FpjkBusiness(@NonNull Activity activity, @NonNull WJWebLoader webLoader) {
        mWebLoader = new WeakReference<>(webLoader);
        mContext = activity;
    }

    public FpjkBusiness registerSwitcher(ITabViewSwitcher ITabViewSwitcher) {
        mITabViewSwitcher = ITabViewSwitcher;
        return this;
    }

    public void execute() {
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
        }

        mSmsMgr = SmsMgr.newInstance(mContext);
        mDeviceMgr = DeviceMgr.newInstance(mContext);
        mRecordMgr = RecordMgr.newInstance(mContext);
        mContactMgr = ContactMgr.newInstance(mContext);
        mLocationMgr = LocationMgr.newInstance(mContext);
        Logger.init("Fpjk");
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
                if (null != mITabViewSwitcher) {
                    mITabViewSwitcher.showOpenUrlTab(dataTransferEntity, wjCallbacks);
                }
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
}
