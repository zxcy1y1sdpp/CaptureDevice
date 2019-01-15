package app.jietuqi.cn.wechat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import app.jietuqi.cn.database.IOpenHelper;
import app.jietuqi.cn.database.MyOpenHelper;
import app.jietuqi.cn.util.EventBusUtil;
import app.jietuqi.cn.wechat.entity.WechatCreateBillsEntity;

/**
 * Created by 刘远博 on 2017/2/15.
 */
public class WechatCreateBillsHelper extends MyOpenHelper implements IOpenHelper {
    /**
     * 数据库的名称
     */
    public final static String TABLE_NAME = "wechatBills";

    public WechatCreateBillsHelper(Context context) {
        super(context);
    }
    /**
     * 单聊表名的规则 -- wechat_single + 对象id
     */
    public void createSingleTalkTable() {
        if (!isTableExists(TABLE_NAME)){
            SQLiteDatabase db = getWritableDatabase();
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ");
            builder.append(TABLE_NAME);
            builder.append(" (");
            builder.append("id Integer PRIMARY KEY AUTOINCREMENT,");
            builder.append("type text, iconString text, iconInt integer, title text, timestamp long, time text, money text, incomeAndExpenses integer, " +
                    "hasRefund text, position integer");
            builder.append(")");
            db.execSQL(builder.toString());
        }
    }
    public int save(WechatCreateBillsEntity entity){
        entity.tag = 0;
        createSingleTalkTable();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
        values.put("type", entity.type);
        values.put("iconString", entity.iconString);
        values.put("iconInt", entity.iconInt);
        values.put("title", entity.title);
        values.put("timestamp", entity.timestamp);
        values.put("time", entity.time);
        values.put("money", entity.money);
        values.put("incomeAndExpenses", entity.incomeAndExpenses);
        values.put("hasRefund", entity.hasRefund);
        int position = allCaseNum(TABLE_NAME);
        values.put("position", position);
        entity.id = allCaseNum(TABLE_NAME) + 1;
        entity.position = position;
        long l = db.insert(TABLE_NAME,null,values);//插入第一条数据
        Log.e("insert " , l+"");
//            EventBusUtil.post(entity);
        return (int) l;
    }
    /**
     * 查询微信聊天与某人的单聊消息
     * @return
     */
    public ArrayList<WechatCreateBillsEntity> queryAll(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WechatCreateBillsEntity> list = new ArrayList<>();
//如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            WechatCreateBillsEntity entity = new WechatCreateBillsEntity();
            entity.id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id
            entity.type = cursor.getString(cursor.getColumnIndex("type"));
            entity.iconString = cursor.getString(cursor.getColumnIndex("iconString"));
            entity.iconInt = cursor.getInt(cursor.getColumnIndex("iconInt"));
            entity.title = cursor.getString(cursor.getColumnIndex("title"));

            entity.hasRefund = "1".equals(cursor.getString(cursor.getColumnIndex("hasRefund")));
            entity.time = cursor.getString(cursor.getColumnIndex("time"));
            entity.money = cursor.getString(cursor.getColumnIndex("money"));
            entity.timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
            entity.position = cursor.getInt(cursor.getColumnIndex("position"));
            entity.incomeAndExpenses = cursor.getInt(cursor.getColumnIndex("incomeAndExpenses"));
            list.add(entity);
        }
        //关闭游标
        cursor.close();
        return list;
    }
    public int update(WechatCreateBillsEntity entity) {
        entity.tag = 1;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("type" , entity.type);
        cv.put("iconString" , entity.iconString);
        cv.put("iconInt" , entity.iconInt);
        cv.put("title" , entity.title);
        cv.put("hasRefund", entity.hasRefund);
        cv.put("time", entity.time);
        cv.put("money", entity.money);
        cv.put("timestamp", entity.timestamp);
        cv.put("position", entity.position);
        cv.put("incomeAndExpenses", entity.incomeAndExpenses);
        int result = 0;
        if (db.isOpen()) {
            try {
                String[] where = new String[] {String.valueOf(entity.id)};
                result = db.update(TABLE_NAME, cv, "id=?", where);
                Log.e("update ", "result : " + result);
            }catch (Exception e){
                Log.e("db", "Exception : " + e.getMessage());
            }
        }
            EventBusUtil.post(entity);
//        db.close();
        return  result;
    }
//
    /**
     * 随机查询出一个用户信息
     * @return
     */
    public WechatCreateBillsEntity query(int receiveTransferId){
        SQLiteDatabase db = getReadableDatabase();
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        WechatCreateBillsEntity entity = null;
        Cursor cursor = db.query(TABLE_NAME, null, "receiveTransferId = ?", new String[]{String.valueOf(receiveTransferId)}, null, null, null);
        while (cursor.moveToNext()) {
            entity = new WechatCreateBillsEntity();
            entity.id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id;
            entity.type = cursor.getString(cursor.getColumnIndex("type"));
            entity.iconString = cursor.getString(cursor.getColumnIndex("iconString"));
            entity.iconInt = cursor.getInt(cursor.getColumnIndex("iconInt"));
            entity.title = cursor.getString(cursor.getColumnIndex("title"));
            entity.hasRefund = "1".equals(cursor.getString(cursor.getColumnIndex("hasRefund")));

            entity.money = cursor.getString(cursor.getColumnIndex("money"));
            entity.time = cursor.getString(cursor.getColumnIndex("time"));
            entity.timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
            entity.position = cursor.getInt(cursor.getColumnIndex("position"));
            entity.incomeAndExpenses = cursor.getInt(cursor.getColumnIndex("incomeAndExpenses"));
        }
        //关闭游标
        cursor.close();
        return entity;
    }
    /**
     * 删除指定id的数据
     * @param entity
     * @return
     */
    public int delete(WechatCreateBillsEntity entity){
        entity.tag = 2;
        SQLiteDatabase db = getWritableDatabase();
        String[] where = new String[] {String.valueOf(entity.id)};
        int result = db.delete(TABLE_NAME,"id=?", where);
        EventBusUtil.post(entity);
        return result;
    }

    public int deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        if (isTableExists(TABLE_NAME)){
            return db.delete(TABLE_NAME,null,null);//返回删除的数量
        }else {
            return -1;
        }
    }
}