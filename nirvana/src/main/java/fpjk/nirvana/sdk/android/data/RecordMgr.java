package fpjk.nirvana.sdk.android.data;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import fpjk.nirvana.sdk.android.jsbridge.WJBridgeUtils;
import fpjk.nirvana.sdk.android.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.android.logger.L;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Summary:
 * Created by FelixChen
 * Created 2016-12-13 22:08
 * Mail:lovejiuwei@gmail.com
 * QQ:74104
 */

public class RecordMgr {
    private Activity mContext;

    private Subscription mSubscription;

    private Dao mContactDao;

    private RxPermissions mRxPermissions = null;

    public static RecordMgr newInstance(@NonNull Activity context) {
        return new RecordMgr(WJBridgeUtils.checkNoNull(context, "Context not NULL!"));
    }

    private RecordMgr(Activity context) {
        mContext = context;
        //permissions
        mRxPermissions = new RxPermissions(context);
        mRxPermissions.setLogging(true);
    }

    public void obtainRecords(final Long uid, final WJCallbacks wjCallbacks) {
        mRxPermissions.requestEach(Manifest.permission.READ_CALL_LOG)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        L.i("Permission result " + permission);
                        if (permission.granted) {
                            L.i("granted");
                            submitContacts(uid, wjCallbacks);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                            L.i("shouldShowRequestPermissionRationale");
                            buildReturnMsg(wjCallbacks, FpjkEnum.ErrorCode.USER_DENIED_ACCESS.getValue());
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            L.i("Need to go to the settings");
                            buildReturnMsg(wjCallbacks, FpjkEnum.ErrorCode.USER_DENIED_ACCESS.getValue());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        L.e("call", throwable);
                        buildReturnMsg(wjCallbacks, FpjkEnum.ErrorCode.USER_DENIED_ACCESS.getValue());
                    }
                });
    }
}
