package fpjk.nirvana.sdk.android.data;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

import fpjk.nirvana.sdk.android.business.ContactList;
import fpjk.nirvana.sdk.android.business.ContactListEntity;
import fpjk.nirvana.sdk.android.business.ErrorCodeEntity;
import fpjk.nirvana.sdk.android.db.DataBaseDaoHelper;
import fpjk.nirvana.sdk.android.db.dao.ContactDao;
import fpjk.nirvana.sdk.android.db.model.DBContactsEntity;
import fpjk.nirvana.sdk.android.jsbridge.WJBridgeUtils;
import fpjk.nirvana.sdk.android.jsbridge.WJCallbacks;
import fpjk.nirvana.sdk.android.logger.L;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
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
public class ContactMgr {
    private Activity mContext;

    private Subscription mSubscription;

    private Dao mContactDao;

    private RxPermissions mRxPermissions = null;

    public static ContactMgr newInstance(@NonNull Activity context) {
        return new ContactMgr(WJBridgeUtils.checkNoNull(context, "Context not NULL!"));
    }

    private ContactMgr(Activity context) {
        mContext = context;
        mContactDao = new ContactDao(mContext).create();
        //permissions
        mRxPermissions = new RxPermissions(context);
        mRxPermissions.setLogging(true);
    }

    public void obtainContacts(final Long uid, final WJCallbacks wjCallbacks) {
        final ErrorCodeEntity errorCodeEntity = new ErrorCodeEntity();
        mRxPermissions.requestEach(Manifest.permission.READ_CONTACTS)
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
                            errorCodeEntity.setErrorCode(FpjkEnum.ErrorCode.USER_DENIED_ACCESS.getValue());
                            String callBack = GsonMgr.get().toJSONString(errorCodeEntity);
                            wjCallbacks.onCallback(callBack);
                        } else {
                            // Denied permission with ask never again
                            // Need to go to the settings
                            L.i("Need to go to the settings");
                            errorCodeEntity.setErrorCode(FpjkEnum.ErrorCode.USER_DENIED_ACCESS.getValue());
                            String callBack = GsonMgr.get().toJSONString(errorCodeEntity);
                            wjCallbacks.onCallback(callBack);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        L.e("call", throwable);
                        errorCodeEntity.setErrorCode(FpjkEnum.ErrorCode.USER_DENIED_ACCESS.getValue());
                        String callBack = GsonMgr.get().toJSONString(errorCodeEntity);
                        wjCallbacks.onCallback(callBack);
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        L.i("completed -^^-");
                    }
                });
    }

    private void submitContacts(final Long uid, final WJCallbacks wjCallbacks) {
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
        }).map(new Func1<Cursor, ContactList>() {
            @Override
            public ContactList call(Cursor cursor) {
                ContactList contactBean = new ContactList();
                try {
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    contactBean.setFullName(escapeSql(name.trim()));
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
                    List<String> nums = new ArrayList<>();
                    while (phoneCursor.moveToNext() && !phoneCursor.isClosed()) {
                        String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        nums.add(escapeSql(phone.trim()));
                    }
                    contactBean.setPhoneNumList(nums);
                    phoneCursor.close();
                } catch (Exception e) {
                    L.e("submitContacts[%s]", e);
                }
                return contactBean;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Func1<ContactList, Boolean>() {
                    @Override
                    public Boolean call(ContactList contactList) {
                        List<String> phones = contactList.getPhoneNumList();
                        for (int i = 0; i < phones.size(); i++) {
                            String phone = phones.get(i);
                            boolean result = DataBaseDaoHelper.newInstance(mContext).query(mContactDao, uid, phone);
                            if (result) {
                                phones.remove(i);
                                --i;
                            }
                        }
                        return !phones.isEmpty();
                    }
                }).toList().subscribe(new Action1<List<ContactList>>() {
                    @Override
                    public void call(List<ContactList> contactLists) {
                        ContactListEntity contactListEntity = new ContactListEntity();
                        contactListEntity.setContactList(contactLists);
                        String returnJSString = GsonMgr.get().toJSONString(contactListEntity);
                        wjCallbacks.onCallback(returnJSString);
                        //insert DB
                        for (ContactList value : contactLists) {
                            DBContactsEntity dbContactsEntity = new DBContactsEntity();
                            dbContactsEntity.setUid(uid);
                            dbContactsEntity.setFullName(value.getFullName());
                            if (value.getPhoneNumList().size() > 0) {
                                for (String phoneNum : value.getPhoneNumList()) {
                                    dbContactsEntity.setPhoneNum(phoneNum);
                                    DataBaseDaoHelper.newInstance(mContext).createIfNotExists(mContactDao, dbContactsEntity);
                                }
                            }
                        }
                        //recycle
                        destory();
                    }
                });
    }

    private boolean isHistoryExits(long uid, String phoneNum) {
        try {
            return DataBaseDaoHelper.newInstance(mContext).query(mContactDao, uid, phoneNum);
        } catch (Exception e) {
            L.e("getHistoryContacts[%s]", e);
        }
        return false;
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
