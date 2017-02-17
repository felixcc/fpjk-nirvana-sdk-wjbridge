package fpjk.nirvana.sdk.wjbridge.data;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;

import java.util.List;

import fpjk.nirvana.sdk.wjbridge.business.entity.RecordEntity;
import fpjk.nirvana.sdk.wjbridge.business.entity.RecordList;
import fpjk.nirvana.sdk.wjbridge.db.DataBaseDaoHelper;
import fpjk.nirvana.sdk.wjbridge.db.dao.RecordDao;
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
 * Created by FelixChen
 * Created 2016-12-13 22:08
 * Mail:lovejiuwei@gmail.com
 * QQ:74104
 */
public class RecordMgr extends PhoneStatus {
    private Activity mContext;

    private Dao mRecordDao;

    private RxPermissions mRxPermissions = null;

    private CompositeDisposable mCompositeDisposable;

    public static RecordMgr newInstance(@NonNull Activity context) {
        return new RecordMgr(WJBridgeUtils.checkNoNull(context, "Context not NULL!"));
    }

    private RecordMgr(Activity context) {
        mContext = context;
        mRecordDao = new RecordDao(context).create();
        //permissions
        mRxPermissions = new RxPermissions(context);
        mRxPermissions.setLogging(true);
        mCompositeDisposable = new CompositeDisposable();
    }

    public void obtainRecords(final long uid, final WJCallbacks wjCallbacks) {
        mRxPermissions.requestEach(Manifest.permission.READ_CALL_LOG).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
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
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                L.e("call", throwable);
                buildReturnMsg(wjCallbacks, FpjkEnum.ErrorCode.USER_REJECT_CALL_RECORD.getValue());
            }
        });
    }

    private void submitRecords(final Long uid, final WJCallbacks wjCallbacks) {
        mCompositeDisposable.add(Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> subscriber) throws Exception {
                try {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                        subscriber.onError(new Throwable("请开启获取通讯录权限"));
                        return;
                    }
                    Cursor cursor = mContext.getContentResolver().query(
                            CallLog.Calls.CONTENT_URI,
                            new String[]{CallLog.Calls.DURATION,
                                    CallLog.Calls.CACHED_NAME,
                                    CallLog.Calls.TYPE,
                                    CallLog.Calls.DATE,
                                    CallLog.Calls.NUMBER},
                            null,
                            null,
                            CallLog.Calls.DEFAULT_SORT_ORDER);
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
                            int phoneIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                            int nameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
                            int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);
                            int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
                            int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);

                            String phoneNumber = cursor.getString(phoneIndex);
                            String name = cursor.getString(nameIndex);
                            int type = cursor.getInt(typeIndex);
                            Long date = cursor.getLong(dateIndex);
                            long duration = cursor.getLong(durationIndex);

                            if (TextUtils.isEmpty(phoneNumber)) {
                                return null;
                            }

                            recordList.setPhoneNum(phoneNumber);
                            recordList.setName(name);
                            recordList.setType(type);
                            recordList.setDuration(duration);
                            recordList.setDate(date);
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
                        return !DataBaseDaoHelper.get(mContext).queryRecordExists(mRecordDao, uid, recordList.getPhoneNum(), recordList.getDate());
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
                        //destory
                        mCompositeDisposable.clear();
                    }
                }));
    }
}
