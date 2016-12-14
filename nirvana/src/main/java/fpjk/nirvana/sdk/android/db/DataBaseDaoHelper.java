package fpjk.nirvana.sdk.android.db;


import android.accounts.Account;
import android.app.Activity;
import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;

import fpjk.nirvana.sdk.android.jsbridge.WJBridgeUtils;
import fpjk.nirvana.sdk.android.logger.L;

@SuppressWarnings("unchecked")
public class DataBaseDaoHelper {
    private static final String TAG = "DataBaseDaoHelper";

    public static DataBaseDaoHelper newInstance(@NonNull Activity context) {
        return new DataBaseDaoHelper(WJBridgeUtils.checkNoNull(context, "Context not NULL!"));
    }

    private DataBaseDaoHelper(Activity context) {
    }

    public synchronized void createIfNotExists(Dao daoInstance, Object object) {
        try {
            daoInstance.createIfNotExists(object);
        } catch (SQLException e) {
            L.e(TAG, e);
        }
    }

    public synchronized boolean queryContactExists(Dao daoInstance, Long uid, String mobile) {
        try {
            QueryBuilder<Account, String> queryBuilder = daoInstance.queryBuilder();
            return queryBuilder.where().eq("uid", uid).and().eq("phoneNum", mobile).countOf() > 0;
        } catch (Exception e) {
            L.e(TAG, e);
        }
        return false;
    }

    public synchronized boolean queryRecordExists(Dao daoInstance, Long uid, String mobile, Long date) {
        try {
            QueryBuilder<Account, String> queryBuilder = daoInstance.queryBuilder();
            return queryBuilder.where()
                    .eq("uid", uid)
                    .and()
                    .eq("phoneNum", mobile)
                    .and()
                    .eq("date", date)
                    .countOf() > 0;
        } catch (Exception e) {
            L.e(TAG, e);
        }
        return false;
    }

}
