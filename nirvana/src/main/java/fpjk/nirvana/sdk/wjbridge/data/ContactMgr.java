package fpjk.nirvana.sdk.wjbridge.data;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;
import java.util.List;

import fpjk.nirvana.sdk.wjbridge.business.entity.ContactList;
import fpjk.nirvana.sdk.wjbridge.business.entity.ContactListEntity;
import fpjk.nirvana.sdk.wjbridge.db.DataBaseDaoHelper;
import fpjk.nirvana.sdk.wjbridge.db.dao.ContactDao;
import fpjk.nirvana.sdk.wjbridge.db.model.DBContactsEntity;
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
 * Date: 28/11/2016
 * Time: 16:04
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */
public class ContactMgr extends IReturnJSJson {
    private Activity mContext;

    private Dao mContactDao;

    private RxPermissions mRxPermissions = null;

    private CompositeDisposable mCompositeDisposable;

    private String mImei;

    public static ContactMgr newInstance(@NonNull Activity context, DeviceMgr deviceMgr) {
        return new ContactMgr(WJBridgeUtils.checkNoNull(context, "Context not NULL!"), deviceMgr);
    }

    private ContactMgr(Activity context, DeviceMgr deviceMgr) {
        mContext = context;
        mContactDao = new ContactDao(mContext).create();
        //permissions
        mRxPermissions = new RxPermissions(context);
        mRxPermissions.setLogging(true);
        mCompositeDisposable = new CompositeDisposable();
        mImei = deviceMgr.getIMEI();
    }

    public void obtainContacts(final String imei, final Long uid, final WJCallbacks wjCallbacks) {
        mRxPermissions.requestEach(Manifest.permission.READ_CONTACTS).subscribe(new Consumer<Permission>() {
            @Override
            public void accept(Permission permission) throws Exception {
                L.i("Permission result " + permission);
                if (permission.granted) {
                    L.i("granted");
                    submitContacts(imei, uid, wjCallbacks);
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // Denied permission without ask never again
                    L.i("shouldShowRequestPermissionRationale");
                    buildErrorJSJson(FpjkEnum.ErrorCode.USER_DENIED_ACCESS.getValue(), wjCallbacks);
                } else {
                    // Denied permission with ask never again
                    // Need to go to the settings
                    L.i("Need to go to the settings");
                    buildErrorJSJson(FpjkEnum.ErrorCode.USER_DENIED_ACCESS.getValue(), wjCallbacks);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                L.e("call", throwable);
                buildErrorJSJson(FpjkEnum.ErrorCode.USER_DENIED_ACCESS.getValue(), wjCallbacks);
            }
        });
    }

    private void submitContacts(final String imei, final Long uid, final WJCallbacks wjCallbacks) {
        mCompositeDisposable.add(Observable.create(new ObservableOnSubscribe<Cursor>() {
            @Override
            public void subscribe(ObservableEmitter<Cursor> subscriber) throws Exception {
                try {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                        subscriber.onError(new Throwable("请开启获取联系人权限"));
                        return;
                    }
                    Uri uri = ContactsContract.Contacts.CONTENT_URI;
                    ContentResolver contentResolver = mContext.getContentResolver();
                    Cursor cursor = contentResolver.query(uri, null, null, null, null);
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
                .map(new Function<Cursor, ContactList>() {
                    @Override
                    public ContactList apply(Cursor cursor) throws Exception {
                        ContactList contactBean = new ContactList();
                        try {
                            //获取联系人的ID
                            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                            //获取联系人的姓名
                            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            contactBean.setFullName(escapeSql(name.trim()));
                            //查询电话类型的数据操作
                            Cursor phoneCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                                    null, null);
                            //获取联系人号码
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
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<ContactList>() {
                    @Override
                    public boolean test(ContactList contactList) throws Exception {
                        List<String> phones = contactList.getPhoneNumList();
                        for (int i = 0; i < phones.size(); i++) {
                            String phone = phones.get(i);
                            boolean result = DataBaseDaoHelper.get(mContext).queryContactExists(mContactDao, uid, phone);
                            if (result) {
                                phones.remove(i);
                                --i;
                            }
                        }
                        return !phones.isEmpty();
                    }
                })
                .toList()
                .subscribe(new Consumer<List<ContactList>>() {
                    @Override
                    public void accept(List<ContactList> contactLists) throws Exception {
                        ContactListEntity contactListEntity = new ContactListEntity();
                        contactListEntity.setContactList(contactLists);
                        String returnJSString = buildReturnCorrectJSJson(contactListEntity);
                        wjCallbacks.onCallback(returnJSString);
                        //insert DB
                        for (ContactList value : contactLists) {
                            DBContactsEntity dbContactsEntity = new DBContactsEntity();
                            dbContactsEntity.setUid(uid);
                            dbContactsEntity.setFullName(value.getFullName());
                            if (value.getPhoneNumList().size() > 0) {
                                for (String phoneNum : value.getPhoneNumList()) {
                                    dbContactsEntity.setPhoneNum(phoneNum);
                                    DataBaseDaoHelper.get(mContext).createIfNotExists(mContactDao, dbContactsEntity);
                                }
                            }
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
