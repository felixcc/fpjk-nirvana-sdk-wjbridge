package fpjk.nirvana.sdk.android.data;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import fpjk.nirvana.sdk.android.business.AddressInfo;
import fpjk.nirvana.sdk.android.business.CoordinateInfo;
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

public class LocationManager {
    private AMapLocationClient locationClient = null;
    private WJCallbacks wjCallbacks;
    private RxPermissions mRxPermissions = null;

    public static LocationManager newInstance(@NonNull Activity context) {
        return new LocationManager(WJBridgeUtils.checkNoNull(context, "Context not NULL!"));
    }

    private LocationManager(Activity context) {
        //初始化client
        locationClient = new AMapLocationClient(context.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        //设置定位监听
        locationClient.setLocationListener(locationListener);
        //permissions
        mRxPermissions = new RxPermissions(context);
        mRxPermissions.setLogging(true);
    }

    private void start() {
        if (locationClient.isStarted()) {
            locationClient.stopLocation();
        }
        locationClient.startLocation();
    }

    public void stop() {
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
                Log.d("AMapLocationListener", "onLocationChanged: " + result);
            } else {
                result = "定位失败";
                Log.d("AMapLocationListener", "onLocationChanged: " + result);
            }
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

            return GsonManager.newInstance().toJSONString(locationEntity);
        }
        return location.getErrorInfo();
    }

    private void start(final Activity context, WJCallbacks wjCallbacks) {
        this.wjCallbacks = wjCallbacks;
        mRxPermissions.requestEach(Manifest.permission.READ_CONTACTS)
                .subscribe(new Action1<Permission>() {
                               @Override
                               public void call(Permission permission) {
                                   L.i("Permission result " + permission);
                                   if (permission.granted) {
                                       start();
                                       Toast.makeText(context,
                                               "permission.granted",
                                               Toast.LENGTH_SHORT).show();
                                   } else if (permission.shouldShowRequestPermissionRationale) {
                                       stop();
                                       // Denied permission without ask never again
                                       Toast.makeText(context,
                                               "Denied permission without ask never again",
                                               Toast.LENGTH_SHORT).show();
                                   } else {
                                       stop();
                                       // Denied permission with ask never again
                                       // Need to go to the settings
                                       Toast.makeText(context,
                                               "Permission denied, can't enable the camera",
                                               Toast.LENGTH_SHORT).show();
                                   }
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable t) {
                                stop();
                                L.e("onError", t);
                            }
                        },
                        new Action0() {
                            @Override
                            public void call() {
                                stop();
                                L.i("OnComplete");
                            }
                        });
    }
}
