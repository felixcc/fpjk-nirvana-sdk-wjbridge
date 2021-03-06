package fpjk.nirvana.sdk.wjbridge.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import fpjk.nirvana.sdk.wjbridge.db.DatabaseHelper;
import fpjk.nirvana.sdk.wjbridge.db.model.DBContactsEntity;
import fpjk.nirvana.sdk.wjbridge.logger.L;

public class ContactDao implements IDataBaseDao {

    private Context mContext;

    public ContactDao(Context context) {
        mContext = context;
    }

    @Override
    public Dao create() {
        try {
            return DatabaseHelper.getHelper(mContext).getDao(DBContactsEntity.class);
        } catch (SQLException e) {
            L.e("ContactDao", e);
        }
        return null;
    }
}
