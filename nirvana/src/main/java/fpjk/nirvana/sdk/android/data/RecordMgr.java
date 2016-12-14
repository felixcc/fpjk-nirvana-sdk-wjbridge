package fpjk.nirvana.sdk.android.data;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import fpjk.nirvana.sdk.android.business.entity.RecordEntity;
import fpjk.nirvana.sdk.android.business.entity.RecordList;
import fpjk.nirvana.sdk.android.db.DataBaseDaoHelper;
import fpjk.nirvana.sdk.android.db.dao.RecordDao;
import fpjk.nirvana.sdk.android.db.model.DBRecordEntity;
import fpjk.nirvana.sdk.android.jsbridge.WJBridgeUtils;
import fpjk.nirvana.sdk.android.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.android.logger.L;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Summary:
 * Created by FelixChen
 * Created 2016-12-13 22:08
 * Mail:lovejiuwei@gmail.com
 * QQ:74104
 */

public class RecordMgr extends PhoneStatus {
    private Activity mContext;

    private Subscription mSubscription;

    private Dao mRecordDao;

    private RxPermissions mRxPermissions = null;

    public static RecordMgr newInstance(@NonNull Activity context) {
        return new RecordMgr(WJBridgeUtils.checkNoNull(context, "Context not NULL!"));
    }

    private RecordMgr(Activity context) {
        mContext = context;
        mRecordDao = new RecordDao(context).create();
        //permissions
        mRxPermissions = new RxPermissions(context);
        mRxPermissions.setLogging(true);
    }

    public void obtainRecords(final long uid, final WJCallbacks wjCallbacks) {
        mRxPermissions.requestEach(Manifest.permission.READ_CALL_LOG)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        L.i("Permission result " + permission);
                        if (permission.granted) {
                            L.i("granted");
                            submitRecords(uid, wjCallbacks);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // Denied permission without ask never again
                            L.i("shouldShowRequestPermissionRationale");
                            buildReturnMsg(wjCallbacks, FpjkEnum.ErrorCode.USER_REJECT_CALL_RECORD.getValue());
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            L.i("Need to go to the settings");
                            buildReturnMsg(wjCallbacks, FpjkEnum.ErrorCode.USER_REJECT_CALL_RECORD.getValue());
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        L.e("call", throwable);
                        buildReturnMsg(wjCallbacks, FpjkEnum.ErrorCode.USER_REJECT_CALL_RECORD.getValue());
                    }
                });
    }

    private void submitRecords(final Long uid, final WJCallbacks wjCallbacks) {
        mSubscription = Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                try {
                    ContentResolver contentResolver = mContext.getContentResolver();
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                        subscriber.onError(new Throwable("请开启获取通讯录权限"));
                        return;
                    }
                    Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null);
                    if (null == cursor) {
                        subscriber.onError(new Throwable("CONTENT_URI == Null"));
                        return;
                    }
                    while (cursor.moveToNext() && !cursor.isClosed()) {
                        subscriber.onNext(cursor);
                    }
                    cursor.close();
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                    L.e("submitContacts", e);
                }
            }
        }).map(new Func1<Cursor, RecordList>() {
            @Override
            public RecordList call(Cursor cursor) {
                RecordList recordList = new RecordList();
                int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                int nameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
                int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
                int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
                int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);

                while (cursor.moveToNext() && !cursor.isClosed()) {
                    String name = cursor.getString(nameIndex);
                    String phoneNumber = cursor.getString(numberIndex);
                    int type = cursor.getInt(typeIndex);
                    Long date = cursor.getLong(dateIndex);
                    long duration = cursor.getLong(durationIndex);
                    if (TextUtils.isEmpty(phoneNumber)) {
                        continue;
                    }
                    recordList.setPhoneNum(phoneNumber);
                    recordList.setName(name);
                    recordList.setType(type);
                    recordList.setDuration(duration);
                    recordList.setDate(date);
                }
                return recordList;
            }
        })
                .filter(new Func1<RecordList, Boolean>() {
                    @Override
                    public Boolean call(RecordList recordList) {
                        return !DataBaseDaoHelper.get(mContext).queryRecordExists(mRecordDao, uid, recordList.getPhoneNum(), recordList.getDate());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Subscriber<List<RecordList>>() {
                    @Override
                    public void onCompleted() {
                        L.d("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        L.e("onError", e);
                    }

                    @Override
                    public void onNext(List<RecordList> recordLists) {
                        RecordEntity recordEntity = new RecordEntity();
                        recordEntity.setRecordList(recordLists);
                        String returnJSString = GsonMgr.get().toJSONString(recordEntity);
                        wjCallbacks.onCallback(returnJSString);
                        //insert DB
                        for (RecordList value : recordLists) {
                            DBRecordEntity dbRecordEntity = new DBRecordEntity();
                            dbRecordEntity.setUid(uid);
                            dbRecordEntity.setType(value.getType());
                            dbRecordEntity.setPhoneNum(value.getPhoneNum());
                            dbRecordEntity.setName(value.getName());
                            dbRecordEntity.setDuration(value.getDuration());
                            dbRecordEntity.setDate(value.getDate());
                            DataBaseDaoHelper.get(mContext).createIfNotExists(mRecordDao, dbRecordEntity);
                        }
                        //
                        destory();
                    }
                });
    }

    private void destory() {
        if (null != mSubscription && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
