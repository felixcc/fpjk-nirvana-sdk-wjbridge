package fpjk.nirvana.sdk.wjbridge.data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import fpjk.nirvana.sdk.wjbridge.business.entity.AddressInfo;
import fpjk.nirvana.sdk.wjbridge.business.entity.CoordinateInfo;
import fpjk.nirvana.sdk.wjbridge.business.entity.LocationEntity;
import fpjk.nirvana.sdk.wjbridge.data.event.EventLocation;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJBridgeUtils;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.wjbridge.logger.L;
import fpjk.nirvana.sdk.wjbridge.permission.Permission;
import fpjk.nirvana.sdk.wjbridge.permission.RxPermissions;
import io.reactivex.functions.Consumer;

/**
 * Summary: Created by Felix Date: 26/11/2016 Time: 14:48 QQ:74104 EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class LocationMgr extends IReturnJSJson {
    private AMapLocationClient locationClient = null;
    private WJCallbacks wjCallbacks;
    private RxPermissions mRxPermissions = null;
    private Activity mActivity;
    private String mImei;
    private boolean mIsNeedGeo;

    public static LocationMgr newInstance(@NonNull Activity context, DeviceMgr deviceMgr) {
        return new LocationMgr(WJBridgeUtils.checkNoNull(context, "Context not NULL!"), deviceMgr);
    }

    private LocationMgr(Activity context, DeviceMgr deviceMgr) {
        //初始化client
        locationClient = new AMapLocationClient(context.getApplicationContext());
        //设置定位监听
        locationClient.setLocationListener(locationListener);
        //permissions
        mRxPermissions = new RxPermissions(context);
        mRxPermissions.setLogging(true);
        mActivity = context;
        mImei = deviceMgr.getIMEI();
    }

    private void startLocation() {
        AMapLocationClientOption locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
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
        mOption.setNeedAddress(mIsNeedGeo);//可选，设置是否返回逆地理地址信息。默认是true
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
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (null != aMapLocation) {
                //解析定位结果
                LocationEntity locationEntity = processLocationCallBack(aMapLocation);
                RxBus.get().send(new EventLocation().setWjCallbacks(wjCallbacks).setLocationEntity(locationEntity));
            }
            L.i("onLocationChanged: " + aMapLocation);
        }
    };

    /**
     * 根据定位结果返回定位信息的字符串
     */
    private synchronized LocationEntity processLocationCallBack(AMapLocation location) {
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setMapType("amap");
        if (null == location) {
            locationEntity.setErrorCode(FpjkEnum.ErrorCode.USER_MOBILE_LOCATION_SERVICES_OFF.getValue());
            return locationEntity;
        }
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
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
        } else {
            locationEntity.setErrorCode(location.getErrorCode());
        }
        return locationEntity;
    }

    public void start(final WJCallbacks wjCallbacks, boolean isNeedGeo) {
        this.wjCallbacks = wjCallbacks;
        this.mIsNeedGeo = isNeedGeo;
        if (!isLocationOpen(mActivity)) {
            L.i("定位未打开[%s]", false);
            buildErrorJSJson(FpjkEnum.ErrorCode.USER_MOBILE_LOCATION_SERVICES_OFF.getValue(), wjCallbacks);
            return;
        }

        mRxPermissions.requestEach(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                L.i("Permission result " + permission);
                if (permission.granted) {
                    L.i("granted");
                    startLocation();
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // Denied permission without ask never again
                    L.i("shouldShowRequestPermissionRationale");
                    buildErrorJSJson(FpjkEnum.ErrorCode.USER_DENIED_LOCATION.getValue(), wjCallbacks);
                } else {
                    // Denied permission with ask never again
                    // Need to go to the settings
                    L.i("Need to go to the settings");
                    buildErrorJSJson(FpjkEnum.ErrorCode.USER_MOBILE_LOCATION_SERVICES_OFF.getValue(), wjCallbacks);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                buildErrorJSJson(FpjkEnum.ErrorCode.USER_MOBILE_LOCATION_SERVICES_OFF.getValue(), wjCallbacks);
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

    @Override
    public String imei() {
        return mImei;
    }
}
