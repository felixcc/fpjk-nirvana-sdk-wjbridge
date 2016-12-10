package fpjk.nirvana.sdk.android.data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import fpjk.nirvana.sdk.android.business.AddressInfo;
import fpjk.nirvana.sdk.android.business.CoordinateInfo;
import fpjk.nirvana.sdk.android.business.ErrorCodeEntity;
import fpjk.nirvana.sdk.android.business.LocationEntity;
import fpjk.nirvana.sdk.android.data.event.EventLocation;
import fpjk.nirvana.sdk.android.jsbridge.WJBridgeUtils;
import fpjk.nirvana.sdk.android.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.android.logger.L;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Summary:
 * Created by Felix
 * Date: 26/11/2016
 * Time: 14:48
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class LocationMgr {
    private AMapLocationClient locationClient = null;
    private WJCallbacks wjCallbacks;
    private RxPermissions mRxPermissions = null;
    private Activity mActivity;

    public static LocationMgr newInstance(@NonNull Activity context) {
        return new LocationMgr(WJBridgeUtils.checkNoNull(context, "Context not NULL!"));
    }

    private LocationMgr(Activity context) {
        //初始化client
        locationClient = new AMapLocationClient(context.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        //设置定位监听
        locationClient.setLocationListener(locationListener);
        //permissions
        mRxPermissions = new RxPermissions(context);
        mRxPermissions.setLogging(true);
        mActivity = context;
    }

    private void startLocation() {
        locationClient.startLocation();
    }

    public void stopLocation() {
        locationClient.stopLocation();
    }

    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(1000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        return mOption;
    }

    /**
     * 定位监听
     */
    private AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            String result = "";
            if (null != loc) {
                //解析定位结果
                result = getLocationStr(loc);
            } else {
                result = "定位失败";
            }
            L.i("onLocationChanged: " + result);
            RxBus.newInstance().send(new EventLocation().setWjCallbacks(wjCallbacks).setLocationInfo(result));
        }
    };

    /**
     * 根据定位结果返回定位信息的字符串
     */
    private synchronized String getLocationStr(AMapLocation location) {
        if (null == location) {
            return null;
        }
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
            LocationEntity locationEntity = new LocationEntity();
            locationEntity.setMapType("amap");

            CoordinateInfo coordinateInfo = new CoordinateInfo();
            coordinateInfo.setLatitude(location.getLatitude());
            coordinateInfo.setLongitude(location.getLongitude());
            locationEntity.setCoordinateInfo(coordinateInfo);

            AddressInfo addressInfo = new AddressInfo();
            addressInfo.setDistrict(location.getDistrict());
            addressInfo.setProvince(location.getProvince());
            addressInfo.setCountry(location.getCountry());
            addressInfo.setCity(location.getCity());
            addressInfo.setAddress(location.getAddress());
            locationEntity.setAddressInfo(addressInfo);

            return GsonMgr.get().toJSONString(locationEntity);
        }
        return location.getErrorInfo();
    }

    public void start(final WJCallbacks wjCallbacks) {
        this.wjCallbacks = wjCallbacks;
        final ErrorCodeEntity errorCodeEntity = new ErrorCodeEntity();
        if (!isLocationOpen(mActivity)) {
            L.i("定位未打开[%s]", false);
            errorCodeEntity.setErrorCode(FpjkEnum.ErrorCode.USER_MOBILE_LOCATION_SERVICES_OFF.getValue());
            String callBack = GsonMgr.get().toJSONString(errorCodeEntity);
            wjCallbacks.onCallback(callBack);
            return;
        }
        mRxPermissions.requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        L.i("Permission result " + permission);
                        if (permission.granted) {
                            L.i("granted");
                            startLocation();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                            L.i("shouldShowRequestPermissionRationale");
                            errorCodeEntity.setErrorCode(FpjkEnum.ErrorCode.USER_DENIED_LOCATION.getValue());
                            String callBack = GsonMgr.get().toJSONString(errorCodeEntity);
                            wjCallbacks.onCallback(callBack);
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            L.i("Need to go to the settings");
                            errorCodeEntity.setErrorCode(FpjkEnum.ErrorCode.USER_MOBILE_LOCATION_SERVICES_OFF.getValue());
                            String callBack = GsonMgr.get().toJSONString(errorCodeEntity);
                            wjCallbacks.onCallback(callBack);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        stopLocation();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        stopLocation();
                    }
                });
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @return true 表示开启
     */
    private boolean isLocationOpen(final Activity context) {
        LocationManager locationMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
}
