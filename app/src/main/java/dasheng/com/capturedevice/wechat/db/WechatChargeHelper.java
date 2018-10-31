package dasheng.com.capturedevice.wechat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import dasheng.com.capturedevice.database.MyOpenHelper;
import dasheng.com.capturedevice.wechat.entity.WechatChargeDetailEntity;

/**
 * 作者： liuyuanbo on 2018/10/30 14:45.
 * 时间： 2018/10/30 14:45
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class WechatChargeHelper extends MyOpenHelper {
    public WechatChargeHelper(Context context) {
        super(context);
    }
    /**
     * 创建微信好友关系表
     */
    public void createTable() {
        if (!isTableExists("wechatCharge")){
            SQLiteDatabase db = getWritableDatabase();
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ");
            builder.append("wechatCharge");
            builder.append(" (");
            builder.append("id Integer PRIMARY KEY AUTOINCREMENT,");
            builder.append("money text, type text, name text, time text");
            builder.append(")");
            db.execSQL(builder.toString());
        }
    }
    public int save(WechatChargeDetailEntity entity){
        String tableName = "wechatCharge";
        createTable();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
        values.put("money", entity.money);
        values.put("type", entity.type);
        values.put("name", entity.name);
        values.put("time", entity.time);
        long l = db.insert(tableName,null,values);//插入第一条数据
        Log.e("insert wechatCharge", l+"");
        return (int) l;
    }
    /**
     * 具体收入或者支出的名称
     */
    public String name;
    /**
     * 金额
     */
    public String money;
    /**
     * 收入或者支出的时间
     */
    public String time;
    /**
     * 查询微信聊天列表页面的数据集合
     * @return
     */
    public ArrayList<WechatChargeDetailEntity> query(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WechatChargeDetailEntity> list = new ArrayList<>();
        String tableName = "wechatCharge";
//如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(tableName)) {
            return null;
        }
        WechatChargeDetailEntity entity;
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String money = cursor.getString(cursor.getColumnIndex("money"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            entity = new WechatChargeDetailEntity(id, type, name, money, time);
            list.add(entity);
        }
        //关闭游标
        cursor.close();
        return list;
    }

    /**
     * 更新零钱明细数据
     * @param entity
     * @return
     */
    public int update(WechatChargeDetailEntity entity) {
        String tableName = "wechatCharge";
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("type" , entity.type);
        cv.put("name" , entity.name);
        cv.put("money", entity.money);
        cv.put("time", entity.time);
        int result = 0;
        if (db.isOpen()) {
            try {
                String[] where = new String[] {String.valueOf(entity.id)};
                result = db.update(tableName, cv, "id=?", where);
                Log.e("update ", "result : " + result);
            }catch (Exception e){
                Log.e("db", "Exception : " + e.getMessage());
            }
        }
//        db.close();
        return  result;
    }
}
