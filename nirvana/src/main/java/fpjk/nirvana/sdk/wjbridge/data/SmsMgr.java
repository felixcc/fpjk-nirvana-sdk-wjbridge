package fpjk.nirvana.sdk.wjbridge.data;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;

import java.util.List;

import fpjk.nirvana.sdk.wjbridge.business.entity.RecordEntity;
import fpjk.nirvana.sdk.wjbridge.business.entity.RecordList;
import fpjk.nirvana.sdk.wjbridge.db.DataBaseDaoHelper;
import fpjk.nirvana.sdk.wjbridge.db.dao.SmsDao;
import fpjk.nirvana.sdk.wjbridge.db.model.DBRecordEntity;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJBridgeUtils;
import fpjk.nirvana.sdk.wjbridge.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.wjbridge.logger.L;
import fpjk.nirvana.sdk.wjbridge.permission.Permission;
import fpjk.nirvana.sdk.wjbridge.permission.RxPermissions;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Summary:
 * Created by Felix
 * Date: 14/12/2016
 * Time: 16:55
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class SmsMgr extends PhoneStatus {
    private Activity mContext;

    private Subscription mSubscription;

    private Dao mSmsDao;

    private RxPermissions mRxPermissions = null;

    public static SmsMgr newInstance(@NonNull Activity context) {
        return new SmsMgr(WJBridgeUtils.checkNoNull(context, "Context not NULL!"));
    }

    private SmsMgr(Activity context) {
        mContext = context;
        mSmsDao = new SmsDao(context).create();
        //permissions
        mRxPermissions = new RxPermissions(context);
        mRxPermissions.setLogging(true);
    }

    public void obtainSms(final long uid, final WJCallbacks wjCallbacks) {
        mRxPermissions.requestEach(Manifest.permission.READ_SMS)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        L.i("Permission result " + permission);
                        if (permission.granted) {
                            L.i("granted");
                            submitSms(uid, wjCallbacks);
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

    private void submitSms(final Long uid, final WJCallbacks wjCallbacks) {
        mSubscription = Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                try {
                    ContentResolver contentResolver = mContext.getContentResolver();
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                        subscriber.onError(new Throwable("请开启获取短信权限"));
                        return;
                    }
                    String[] projection = new String[]{"_id", "address", "person",
                            "body", "date", "type"};
                    Cursor cursor = contentResolver.query(Uri.parse("content://sms/"), projection, null, null, "date desc");
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
                try {
                    RecordList recordList = new RecordList();
                    int index_Address = cursor.getColumnIndex("address");
                    int index_Person = cursor.getColumnIndex("person");
                    int index_Body = cursor.getColumnIndex("body");
                    int index_Date = cursor.getColumnIndex("date");
                    int index_Type = cursor.getColumnIndex("type");

                    String strAddress = cursor.getString(index_Address);
                    int intPerson = cursor.getInt(index_Person);
                    String strbody = cursor.getString(index_Body);
                    long longDate = cursor.getLong(index_Date);
                    int intType = cursor.getInt(index_Type);

                    if (TextUtils.isEmpty(strAddress)) {
                        return null;
                    }
                    recordList.setPhoneNum(strAddress);
                    recordList.setName(intPerson + "");
                    recordList.setType(intType);
                    recordList.setContent(strbody);
                    recordList.setDate(longDate);
                    return recordList;
                } catch (Exception e) {
                    L.e("", e);
                }
                return null;
            }
        })
                .filter(new Func1<RecordList, Boolean>() {
                    @Override
                    public Boolean call(RecordList recordList) {
                        return !DataBaseDaoHelper.get(mContext).querySmsExists(mSmsDao, uid, recordList.getPhoneNum(), recordList.getDate());
                    }
                })
                .subscribeOn(Schedulers.newThread())
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
                            dbRecordEntity.setPhoneNum(value.getPhoneNum());
                            dbRecordEntity.setName(value.getName());
                            dbRecordEntity.setDate(value.getDate());
                            dbRecordEntity.setContent(value.getContent());
                            dbRecordEntity.setType(value.getType());
                            DataBaseDaoHelper.get(mContext).createIfNotExists(mSmsDao, dbRecordEntity);
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
