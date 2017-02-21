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

import org.reactivestreams.Subscription;

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
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Summary:
 * Created by Felix
 * Date: 14/12/2016
 * Time: 16:55
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class SmsMgr extends IReturnJSJson {
    private Activity mContext;

    private Subscription mSubscription;

    private Dao mSmsDao;

    private RxPermissions mRxPermissions = null;

    private CompositeDisposable mCompositeDisposable;

    private String mImei;

    public static SmsMgr newInstance(@NonNull Activity context, DeviceMgr deviceMgr) {
        return new SmsMgr(WJBridgeUtils.checkNoNull(context, "Context not NULL!"), deviceMgr);
    }

    private SmsMgr(Activity context, DeviceMgr deviceMgr) {
        mContext = context;
        mSmsDao = new SmsDao(context).create();
        //permissions
        mRxPermissions = new RxPermissions(context);
        mRxPermissions.setLogging(true);
        mCompositeDisposable = new CompositeDisposable();
        mImei = deviceMgr.getIMEI();
    }

    public void obtainSms(final String imei, final long uid, final WJCallbacks wjCallbacks) {
        mRxPermissions.requestEach(Manifest.permission.READ_SMS).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                L.i("Permission result " + permission);
                if (permission.granted) {
                    L.i("granted");
                    submitSms(imei, uid, wjCallbacks);
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // Denied permission without ask never again
                    L.i("shouldShowRequestPermissionRationale");
                    buildErrorJSJson(FpjkEnum.ErrorCode.USER_REJECT_CALL_RECORD.getValue(), wjCallbacks);
                } else {
                    // Denied permission with ask never again
                    // Need to go to the settings
                    L.i("Need to go to the settings");
                    buildErrorJSJson(FpjkEnum.ErrorCode.USER_REJECT_CALL_RECORD.getValue(), wjCallbacks);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                L.e("call", throwable);
                buildErrorJSJson(FpjkEnum.ErrorCode.USER_REJECT_CALL_RECORD.getValue(), wjCallbacks);
            }
        });
    }

    private void submitSms(final String imei, final Long uid, final WJCallbacks wjCallbacks) {
        mCompositeDisposable.add(Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> subscriber) throws Exception {
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
                    subscriber.onComplete();
                } catch (Exception e) {
                    subscriber.onError(e);
                    L.e("submitContacts", e);
                }
            }
        })
                .map(new Function<Cursor, RecordList>() {
                    @Override
                    public RecordList apply(Cursor cursor) throws Exception {
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
                .filter(new Predicate<RecordList>() {
                    @Override
                    public boolean test(RecordList recordList) throws Exception {
                        return !DataBaseDaoHelper.get(mContext).querySmsExists(mSmsDao, uid, recordList.getPhoneNum(), recordList.getDate());
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(new Consumer<List<RecordList>>() {
                    @Override
                    public void accept(List<RecordList> recordLists) throws Exception {
                        RecordEntity recordEntity = new RecordEntity();
                        recordEntity.setRecordList(recordLists);
                        String returnJSString = buildReturnCorrectJSJson(recordEntity);
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
                        //destory
                        mCompositeDisposable.clear();
                    }
                }));
    }

    @Override
    public String imei() {
        return mImei;
    }
}
