package fpjk.nirvana.sdk.android.data;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.commons.lang.StringEscapeUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fpjk.nirvana.sdk.android.db.DataBaseDaoHelper;
import fpjk.nirvana.sdk.android.db.dao.ContactDao;
import fpjk.nirvana.sdk.android.db.dao.IDataBaseDao;
import fpjk.nirvana.sdk.android.db.model.DBContactsEntity;
import fpjk.nirvana.sdk.android.jsbridge.WJBridgeUtils;
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
 * Created by Felix
 * Date: 28/11/2016
 * Time: 16:04
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class ContactManager {
    private static final int RECORD_STATE = 1;
    private static final int CONTACT_STATE = 2;
    private static final int SMS_STATE = 3;

    private Context mContext;

    private Subscription mSubscription;

    private List<DBContactsEntity> mHistoryContacts;

    private IDataBaseDao iDataBaseDao;

    public interface ICallBack {
        void onCompleted(List<DBContactsEntity> contactsEntities);
    }

    public static ContactManager newInstance(@NonNull Context context) {
        return new ContactManager(WJBridgeUtils.checkNoNull(context, "Context not NULL!"));
    }

    private ContactManager(Context context) {
        mContext = context;
        iDataBaseDao = new ContactDao(mContext);
    }

    private List<DBContactsEntity> getHistoryContacts() {
        try {
            return (List<DBContactsEntity>) DataBaseDaoHelper.getInstance().queryAll(iDataBaseDao.create());
        } catch (SQLException e) {
            L.e("[%s]", e);
        }
        return null;
    }

    /**
     * @param type <br /> 1 为通话记录 <br /> 2 为联系人 <br /> 3 为短信
     * @return 指定类型的游标
     */
    private Cursor getCursor(int type) {
        ContentResolver resolver = mContext.getContentResolver();
        try {
            Uri uri = null;
            switch (type) {
                case RECORD_STATE:
                    uri = CallLog.Calls.CONTENT_URI;
                    break;
                case CONTACT_STATE:
                    uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                    break;
                case SMS_STATE:
                    uri = Uri.parse("content://sms/");
                    break;
            }
            if (uri != null) {
                Cursor cursor = resolver.query(uri, null, null, null, null);
                if (cursor != null && cursor.getCount() != 0) {
                    return cursor;
                }
            }
        } catch (Exception e) {
            L.e("[%s]", e);
        }
        return null;
    }

    private Observable<Cursor> makeContactObservable() {
        return Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    try {
                        Cursor cursor = getCursor(CONTACT_STATE);
                        if (null == cursor) {
                            return;
                        }
                        while (cursor.moveToNext() && !cursor.isClosed()) {
                            subscriber.onNext(cursor);
                        }
                        cursor.close();
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(e);
                        L.e("[%s]", e);
                    }
                }
            }
        });
    }

    /**
     * 提交联系人信息，增量哦。
     */
    public void submitContacts(final ICallBack iCallBack) {
        mHistoryContacts = getHistoryContacts();

        mSubscription = makeContactObservable()
                .subscribeOn(Schedulers.io())
                .map(new Func1<Cursor, DBContactsEntity>() {
                    @Override
                    public DBContactsEntity call(Cursor cursor) {
                        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String name = cursor.getString(nameIndex);
                        String phoneNumber = cursor.getString(phoneIndex);
                        DBContactsEntity newOne = new DBContactsEntity().setPhoneNum(escapeSql(phoneNumber)).setFullName(escapeSql(name));
                        try {
                            if (!mHistoryContacts.isEmpty()) {
                                for (DBContactsEntity inside : mHistoryContacts) {
                                    if (!inside.getPhoneNum().equals(newOne.getPhoneNum()) && !inside.getFullName().equals(newOne.getFullName())) {
                                        return newOne;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            L.e("[%s]", e);
                        }
                        L.e("[%s]", "没有找到匹配的通讯录。");
                        cursor.close();
                        return newOne;
                    }
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<DBContactsEntity>>() {
                    @Override
                    public void call(List<DBContactsEntity> contactsEntities) {
                        int size = contactsEntities.size();
                        if (size == 0) {
                            return;
                        }
                        Log.d("[%s]", "call: " + contactsEntities);
                        //入库
                        List<Object> insertDB = new ArrayList<>();
                        insertDB.addAll(contactsEntities);
                        DataBaseDaoHelper.getInstance().insertToTableAll(iDataBaseDao.create(), insertDB);
                        //回调
                        if (null != iCallBack) {
                            iCallBack.onCompleted(contactsEntities);
                        }
                        destory();
                    }
                });
    }

    private void destory() {
        if (null != mSubscription && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    /**
     * 对存入数据库的字段进行特殊字符处理
     *
     * @param sqlcolumn 需要存入数据的字符串
     * @return 格式化的字符串
     */
    private String escapeSql(String sqlcolumn) {
        return StringEscapeUtils.escapeSql(sqlcolumn);
    }
}
