package fpjk.nirvana.sdk.android.business;

import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.CookieManager;

import org.apache.commons.lang.StringUtils;

import java.lang.ref.WeakReference;
import java.util.List;

import fpjk.nirvana.sdk.android.data.ContactManager;
import fpjk.nirvana.sdk.android.data.DeviceManager;
import fpjk.nirvana.sdk.android.data.FpjkEnum;
import fpjk.nirvana.sdk.android.data.GsonManager;
import fpjk.nirvana.sdk.android.data.LocationManager;
import fpjk.nirvana.sdk.android.data.RxBus;
import fpjk.nirvana.sdk.android.data.event.EventLocation;
import fpjk.nirvana.sdk.android.db.model.DBContactsEntity;
import fpjk.nirvana.sdk.android.jsbridge.WJBridgeHandler;
import fpjk.nirvana.sdk.android.jsbridge.WJBridgeUtils;
import fpjk.nirvana.sdk.android.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.android.jsbridge.WJWebLoader;
import fpjk.nirvana.sdk.android.logger.L;
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

    private Context mContext;
    private DeviceManager mDeviceManager;
    private ContactManager mContactManager;
    private LocationManager mLocationManager;

    public static FpjkBusiness newInstance(WJWebLoader webLoader) {
        return new FpjkBusiness(WJBridgeUtils.checkNoNull(webLoader, "WJWebLoader not NULL!"));
    }

    private FpjkBusiness(@NonNull WJWebLoader webLoader) {
        mWebLoader = new WeakReference<>(webLoader);
    }

    public void process() {
        processMessages();
        processCookieEvent();
    }

    private void processCookieEvent() {
        RxBus.newInstance().toObserverable().subscribe(new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (o instanceof EventLocation) {
                    mLocationManager.stop();

                    String mLocationInfo = ((EventLocation) o).getLocationInfo();
                    WJCallbacks wjCallbacks = ((EventLocation) o).getWjCallbacks();
                    wjCallbacks.onCallback(mLocationInfo);
                    L.d("EventLocation->[%s]", mLocationInfo);
                }
            }
        });
    }

    private void processMessages() {
        if (mWebLoader.get() != null && mWebLoader.get() instanceof WJBridgeWebView) {
            final WJBridgeWebView wjBridgeWebView = (WJBridgeWebView) mWebLoader.get();
            wjBridgeWebView.registerHandler(cN, new WJBridgeHandler() {
                @Override
                public void handler(String data, WJCallbacks callbacks) {
                    L.d("registerHandler->[%s]", data);
                    dispatchMessages(data, callbacks);
                }
            });

            mContext = wjBridgeWebView.getContext();
            mDeviceManager = DeviceManager.newInstance(mContext);
            mContactManager = ContactManager.newInstance(mContext);
            mLocationManager = LocationManager.newInstance(mContext);
        }
    }

    private void dispatchMessages(String jsonData, final WJCallbacks wjCallbacks) {
        if (StringUtils.isEmpty(jsonData)) {
            return;
        }
        try {
            ProcessBusinessEntity entity = GsonManager.newInstance().json2Object(jsonData, ProcessBusinessEntity.class);
            if (FpjkEnum.Business.GET_CONTACTS.getValue().equals(entity.getOpt())) {
                mContactManager.submitContacts(new ContactManager.ICallBack() {
                    @Override
                    public void onCompleted(List<DBContactsEntity> contactsEntities) {
                        String contactJson = GsonManager.newInstance().toJSONString(contactsEntities);
                        L.d("ContactManager.submitContacts->[%s]", contactJson);
                        wjCallbacks.onCallback(contactJson);
                    }
                });
            } else if (FpjkEnum.Business.GET_COOKIE.getValue().equals(entity.getOpt())) {
                if (StringUtils.isEmpty(entity.getData().getUrl())) {
                    return;
                }
                DataTransferEntity dataTransferEntity = entity.getData();
                String cookie = CookieManager.getInstance().getCookie(dataTransferEntity.getUrl());
                wjCallbacks.onCallback(cookie);
            } else if (FpjkEnum.Business.GET_DEVICE_INFO.getValue().equals(entity.getOpt())) {
                DeviceInfoEntity deviceInfoEntity = new DeviceInfoEntity();
                deviceInfoEntity.setDeviceInfo(new DeviceInfoEntity.DeviceInfo()
                        .setOs("android")
                        .setSysVersion(mDeviceManager.getSysVersion())
                        .setUs("us")
                        .setDeviceState(mDeviceManager.isEmulator() + "")
                        .setVersion(mDeviceManager.getVersionCode() + "")
                        .setVersionName(mDeviceManager.getVersionName())
                        .setPid(mDeviceManager.getIMEI()));
                String json = GsonManager.newInstance().toJSONString(deviceInfoEntity);
                wjCallbacks.onCallback(json);
            } else if (FpjkEnum.Business.GET_LOCATION.getValue().equals(entity.getOpt())) {
                mLocationManager.buildCallback(wjCallbacks);
                mLocationManager.start();
            }
            L.d("dispatchMessages->[%s]", entity);
        } catch (Exception e) {
            L.e("JavaScript invoke Native is Error:[%s]", jsonData, e);
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
