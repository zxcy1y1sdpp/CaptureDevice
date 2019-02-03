package app.jietuqi.cn.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static app.jietuqi.cn.constant.DatabaseConfig.DATABASE_NAME;
import static app.jietuqi.cn.constant.DatabaseConfig.DATABASE_VERSION;

/**
 * Created by 刘远博 on 2017/2/15.
 */
public class MyOpenHelper extends SQLiteOpenHelper implements IOpenHelper {
    /**
     * 数据库版本号
     */
    public MyOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * 初始化数据库，一般我们都在这里写语句，现在我们自己封装了方法，就不需要在这里写
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("MyOpenHelper", "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("onUpgrade", "MyOpenHelper --- 》   oldversion:  " + oldVersion + "    newVersion: " + newVersion);
    }

    /**
     * 判断表是否存在
     * @param tableName
     * @return
     */
    public boolean isTableExists(String tableName) {
        boolean flag = false;
        SQLiteDatabase db = getReadableDatabase();//获取一个可读的数据库对象
        Cursor cursor = null;
        try {
            cursor = db.query("sqlite_master", null, "type = 'table' and name = ?", new String[]{tableName.replaceAll("\\.", "_")}, null, null, null);
            while (cursor.moveToNext()) {
                if (cursor.getCount() > 0) {
                    flag = true;
                }
            }
        } catch (Exception e) {
            Log.e("cursor", "isExistTabValus  error");
        } finally {
            if (cursor != null) {
                cursor.close();
            }
//            db.close();//关闭数据库
        }
        return flag;
    }
    public int allCaseNum(String tableName){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT count(*) from " + tableName;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return (int) count;
    }
}