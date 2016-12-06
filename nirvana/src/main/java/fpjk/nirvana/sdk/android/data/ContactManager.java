package fpjk.nirvana.sdk.android.data;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

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
            L.e(e);
        }
        return null;
    }

    public void submitContacts(final ICallBack iCallBack) {
        mHistoryContacts = getHistoryContacts();
        mSubscription = Observable.create(new Observable.OnSubscribe<Cursor>() {
            @Override
            public void call(Subscriber<? super Cursor> subscriber) {
                ContentResolver contentResolver = mContext.getContentResolver();
                Cursor cursor = contentResolver.query(ContactsContract.RawContacts.CONTENT_URI, null, null, null, null);
                if (null == cursor) {
                    subscriber.onError(new Throwable());
                    return;
                }
                while (cursor.moveToNext() && !cursor.isClosed()) {
                    subscriber.onNext(cursor);
                }
                cursor.close();
                subscriber.onCompleted();
            }
        }).map(new Func1<Cursor, DBContactsEntity>() {
            @Override
            public DBContactsEntity call(Cursor cursor) {
                DBContactsEntity contactBean = new DBContactsEntity();
                try {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    contactBean.setFullName(escapeSql(name));
                    //获取联系人号码
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor phoneCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                            null,
                            null);
                    if (null == phoneCursor) {
                        return null;
                    }
                    StringBuilder phoneNumber = new StringBuilder();
                    while (phoneCursor.moveToNext() && !phoneCursor.isClosed()) {
                        String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (phoneCursor.isLast()) {
                            phoneNumber.append(null != phone ? phone : "");
                        } else {
                            phoneNumber.append(null != phone ? phone + "," : "");
                        }
                        contactBean.setPhoneNum(escapeSql(phoneNumber.toString()));
                    }
                    phoneCursor.close();
                } catch (Exception e) {
                    L.e(e);
                }
                return contactBean;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<DBContactsEntity, Boolean>() {
                    @Override
                    public Boolean call(DBContactsEntity dbContactsEntity) {
                        for (DBContactsEntity inside : mHistoryContacts) {
                            if (inside.getPhoneNum().equals(dbContactsEntity.getPhoneNum()) && inside.getFullName().equals(dbContactsEntity.getFullName())) {
                                return false;
                            }
                        }
                        return true;
                    }
                })
                .toList()
                .subscribe(new Action1<List<DBContactsEntity>>() {
                    @Override
                    public void call(List<DBContactsEntity> dbContactsEntities) {
                        L.d(dbContactsEntities.toString());
                        if (!dbContactsEntities.isEmpty()) {
                            //入库
                            List<Object> insertDB = new ArrayList<>();
                            insertDB.addAll(dbContactsEntities);
                            DataBaseDaoHelper.getInstance().insertToTableAll(iDataBaseDao.create(), insertDB);
                            if (null != iCallBack) {
                                iCallBack.onCompleted(dbContactsEntities);
                            }
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
