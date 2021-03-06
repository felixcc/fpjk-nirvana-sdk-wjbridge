package fpjk.nirvana.sdk.wjbridge.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import fpjk.nirvana.sdk.wjbridge.db.DatabaseHelper;
import fpjk.nirvana.sdk.wjbridge.db.model.DBRecordEntity;
import fpjk.nirvana.sdk.wjbridge.logger.L;

/**
 * Summary:
 * Created by Felix
 * Date: 14/12/2016
 * Time: 11:46
 * QQ:74104
 * EMAIL:lovejiuwei@gmail.com
 * Version 1.0
 */

public class SmsDao implements IDataBaseDao {

    private Context mContext;

    public SmsDao(Context context) {
        mContext = context;
    }

    @Override
    public Dao create() {
        try {
            return DatabaseHelper.getHelper(mContext).getDao(DBRecordEntity.class);
        } catch (SQLException e) {
            L.e("ContactDao", e);
        }
        return null;
    }
}

