package fpjk.nirvana.sdk.android.db;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import fpjk.nirvana.sdk.android.logger.L;

@SuppressWarnings("unchecked")
public class DataBaseDaoHelper {
    private static final String TAG = "DataBaseDaoHelper";

    private static DataBaseDaoHelper helper;

    public static synchronized DataBaseDaoHelper getInstance() {
        if (helper == null) {
            helper = new DataBaseDaoHelper();
        }
        return helper;
    }
    /***********************************************插入***********************************************/
    /**
     * 向数据库中数据表名为clazz.getSimpleName()表插入数据
     */

    public synchronized void insertToTable(Dao daoInstance, Object object) {
        try {
            daoInstance.create(object);
        } catch (SQLException e) {
            L.e(TAG, e);
        }
    }

    public synchronized void insertToTableAll(final Dao daoInstance, final List<Object> lists) {
        try {
            /**
             * 需要一次性批量操作很多数据时候，需要使用事务
             */
            daoInstance.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    for (Object liveItemBean : lists) {
                        int code = daoInstance.create(liveItemBean);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            L.e(TAG, e);
        }
    }

    /**
     * 向数据库中插入或者替换有相同索引列的数据 其实就是 ID列， 只能够是int、long等之类的，不能使String等的引用数据类型
     * 调用这个方法也必须要求一个ID属性
     */
    public synchronized Dao.CreateOrUpdateStatus insertOrUpdateToTable(Dao daoInstance, Object object) {
        try {
            return daoInstance.createOrUpdate(object);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***********************************************查询***********************************************/
    /**
     * 查询数据库中数据表名为clazz.getSimpleName()的所有数据，最后返回一个集合
     */
    public synchronized List<?> queryAll(Dao<?, ?> daoInstance) throws SQLException {
        return daoInstance.queryForAll();
    }

    /**
     * 通过queryBuilder获取一个专注于各种查询的实例，可以进行排序order by，分组group by，还有模糊查询like以及获取查询的数量limit等
     * 而通过where则是进行更加细致的条件查询
     * 而下面的则是 进行多条件查询 并且都是且的关系  前者是列名的数组，后者是对应列值的数组
     */
    public synchronized List<?> queryByWhere(Dao<?, ?> daoInstance, String[] columnName, Object... columnValue) throws SQLException {
        Where<?, ?> where = daoInstance.queryBuilder().where();
        where.eq(columnName[0], columnValue[0]);
        for (int i = 1; i < columnName.length; i++) {
            where.and().eq(columnName[i], columnValue[i]);
        }
        return where.query();
    }

    /**
     * 查询某一个特定的元素
     */
    public synchronized Object queryCertain(Dao<?, ?> daoInstance, String[] columnName, Object... columnValue) throws SQLException {
        Where<?, ?> where = daoInstance.queryBuilder().where();
        where.eq(columnName[0], columnValue[0]);
        for (int i = 1; i < columnName.length; i++) {
            where.and().eq(columnName[i], columnValue[i]);
        }
        if (where.query().size() == 1) {
            return where.query().get(0);
        }
        return null;
    }

    /**
     * 查询数据库中数据表名为clazz.getSimpleName()的表中是否包含与object属性值完全相同的数据
     * 但是如果 某些属性值是默认值得话，那么是不会拿来进行比对的的，例如 (null, false, 0, 0.0, etc.) 如果有字段是这些默认值的话，那么是不会进行考虑的
     */
    public synchronized boolean isContainsData(Dao daoInstance, Object object) {
        try {
            if (((List<Object>) daoInstance.queryForMatching(object)).size() > 0) {
                return true;
            }
        } catch (SQLException e) {
            L.e(TAG, e);
        }
        return false;
    }
    /***********************************************删除***********************************************/
    /**
     * 删除数据库中数据表名为clazz.getSimpleName()的表中数据，并且根据给出的条件的
     */
    public synchronized void deleteData(Dao<?, ?> daoInstance, String[] columnName, Object[] columnValue) {
        DeleteBuilder<?, ?> deleteBuilder = daoInstance.deleteBuilder();//调用一次就会生成一个删除builder的实例，所以只能够用一次
        Where<?, ?> where = deleteBuilder.where();
        try {
            where.eq(columnName[0], columnValue[0]);
            for (int i = 1; i < columnName.length; i++) {
                where.and().eq(columnName[i], columnValue[i]);
            }
            deleteBuilder.delete();
        } catch (SQLException e) {
            L.e(TAG, e);
        }
    }

    /**
     * 删除全部数据，但是表结构还在,新添加的id也不会从1开始自增
     */
    public synchronized void deleteAll(Dao<?, ?> daoInstance) {
        try {
            daoInstance.deleteBuilder().delete();
        } catch (SQLException e) {
            L.e(TAG, e);
        }
    }

    /***********************************************更新***********************************************/
    /**
     * 通过条件修改 某条数据某些字段的值
     * 也可以延展成 先根据那些不需要修改的字段信息，然后查询出该条数据的ID，然后将这个ID设置到新数据对象的对应ID属性，然后调用update（object）方法直接修改也可以的
     * 第一个数组是列表名的数组，第二是数组名的对应的值得数组，而需要修改的则是两个数组的最后一个元素
     */
    public void updateByWhere(Dao<?, ?> daoInstance, String[] columnName, Object... columnValue) {
        UpdateBuilder<?, ?> updateBuilder = daoInstance.updateBuilder();
        Where<?, ?> where = updateBuilder.where();
        try {
            where.eq(columnName[0], columnValue[0]);
            for (int i = 1; i < columnName.length - 1; i++) {
                where.and().eq(columnName[i], columnValue[i]);
            }
            updateBuilder.updateColumnValue(columnName[columnName.length - 1], columnValue[columnName.length - 1]);
            updateBuilder.update();
        } catch (SQLException e) {
            L.e(TAG, e);
        }
    }

    public synchronized void updateByWhere(Dao<?, ?> daoInstance, String[] whereArgs, Object[] whereValue, String[] columnName, Object[] columnValue) {
        UpdateBuilder<?, ?> updateBuilder = daoInstance.updateBuilder();
        Where<?, ?> where = updateBuilder.where();
        try {

            for (int i = 0; i < columnName.length; i++) {
                where.eq(whereArgs[i], whereValue[i]);
                if (i < columnName.length - 1) {
                    where.and();
                }
            }
            updateBuilder.updateColumnValue(columnName[columnName.length - 1], columnValue[columnName.length - 1]);
            updateBuilder.update();
        } catch (SQLException e) {
            L.e(TAG, e);
        }
    }

    public synchronized int update(Dao daoInstance, Object object) {
        try {
            return daoInstance.update(object);
        } catch (SQLException e) {
            L.e(TAG, e);
        }
        return 0;
    }

    public synchronized void delete(Dao daoInstance, Object object) {
        try {
            daoInstance.delete(object);
        } catch (SQLException e) {
            L.e(TAG, e);
        }
    }

    public synchronized void delete(final Dao daoInstance, final List<? extends Object> list) {
        try {
            daoInstance.callBatchTasks(new Callable<Void>() {

                @Override
                public Void call() throws Exception {
                    for (Object item : list) {
                        daoInstance.delete(item);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            L.e(TAG, e);
        }
    }

    public synchronized long getCount(Dao<?, ?> daoInstance) {
        try {
            QueryBuilder<?, ?> builder = daoInstance.queryBuilder();
            builder.setCountOf(true);
            long count = builder.countOf();
            return count;
        } catch (SQLException e) {
            L.e(TAG, e);
        }
        return 0;
    }
}
