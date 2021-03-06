package fpjk.nirvana.sdk.wjbridge.db;


import android.accounts.Account;
import android.app.Activity;
import android.support.annotation.NonNull;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.util.List;
import java.util.concurrent.Callable;

import fpjk.nirvana.sdk.wjbridge.jsbridge.WJBridgeUtils;
import fpjk.nirvana.sdk.wjbridge.logger.L;

@SuppressWarnings("unchecked")
public class DataBaseDaoHelper {
    private static final String TAG = "DataBaseDaoHelper";

    public static DataBaseDaoHelper get(@NonNull Activity context) {
        return new DataBaseDaoHelper(WJBridgeUtils.checkNoNull(context, "Context not NULL!"));
    }

    private DataBaseDaoHelper(Activity context) {
    }

    /**
     * 批量插入 不能使用循环一个一个的插入，因为这样会一直打开数据库、插入数据、 关闭数据库
     */
    public synchronized void addBatchTask(final Dao daoInstance, final List<Object> values) {
        try {
            daoInstance.callBatchTasks(new Callable() {
                @Override
                public Object call() throws Exception {
                    for (Object value : values) {
                        daoInstance.createIfNotExists(value);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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

    public synchronized boolean querySmsExists(Dao daoInstance, Long uid, String mobile, Long date) {
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
