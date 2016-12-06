package fpjk.nirvana.sdk.android.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import fpjk.nirvana.sdk.android.db.model.DBContactsEntity;
import fpjk.nirvana.sdk.android.logger.L;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static DatabaseHelper instance;

    public static final String DB_NAME = "fpjk_bridge.db";

    private Map<String, Dao> daos = new HashMap<>();

    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        /*File file = context.getDatabasePath(DB_NAME);
        //判断数据库文件存在与否
		if(file.exists()){
			//根据数据库路径打开已经存在的数据库 可以进 行读写
			SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getDatabasePath(DB_NAME).getPath(), null,
					SQLiteDatabase.OPEN_READWRITE);
			connectionSource = new AndroidConnectionSource(db);
		}  */
        super(context, DB_NAME, null, VERSION);
    }

    /**
     * 进行数据表的创建，目前是创建了 短信、联系人、通话记录的数据表
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, DBContactsEntity.class);
        } catch (SQLException e) {
            L.e(TAG, e);
        }
    }

    //进行数据库的升级
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            sqLiteDatabase.execSQL("DROP TABLE t_Mobile");
            sqLiteDatabase.execSQL("DROP TABLE t_Cookie");

            TableUtils.createTableIfNotExists(connectionSource, DBContactsEntity.class);
        } catch (SQLException e) {
            L.e(TAG, e);
        }
    }

    /**
     * 单例获取该Helper
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context.getApplicationContext());
            }
        }
        return instance;
    }

    /**
     * 在这里获取一个操作一张表的Dao类，这个Dao类就是操作数据库中对应数据表的工具类，可以进行增删改查 ，这里传入的参数就是一个类
     */
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();
        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }
}
