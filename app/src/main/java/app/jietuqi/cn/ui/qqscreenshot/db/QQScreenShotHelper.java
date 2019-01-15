package app.jietuqi.cn.ui.qqscreenshot.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import app.jietuqi.cn.database.IOpenHelper;
import app.jietuqi.cn.database.MyOpenHelper;
import app.jietuqi.cn.ui.qqscreenshot.entity.QQScreenShotEntity;
import app.jietuqi.cn.util.EventBusUtil;

/**
 * Created by 刘远博 on 2017/2/15.
 */
public class QQScreenShotHelper extends MyOpenHelper implements IOpenHelper {
    /**
     * 数据库的名称
     */
    public final static String TABLE_NAME = "qq_screen_shot_single";

    public QQScreenShotHelper(Context context) {
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
            builder.append("wechatUserId text, avatarInt integer, avatarStr text, msgType integer" +
                    ", msg text, img integer, filePath text, time integer, transferOutTime integer, " +
                    "transferReceiveTime integer, receiveTransferId integer, receive text, money text, " +
                    "voiceLength integer, alreadyRead text, voiceToText text, position integer, isComMsg text, lastTime integer");
            builder.append(")");
            db.execSQL(builder.toString());
//            db.close();
        }
    }
    public int save(QQScreenShotEntity entity){
        entity.tag = 0;
        createSingleTalkTable();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
        values.put("wechatUserId", entity.wechatUserId);
        values.put("avatarInt", entity.avatarInt);
        values.put("avatarStr", entity.avatarStr);
        values.put("msgType", entity.msgType);
        if (entity.msgType == 0){
            values.put("msg", entity.msg);
        }else if (entity.msgType == 1){
            values.put("img", entity.img);
            values.put("filePath", entity.filePath);
            values.put("msg", "图片");
        }else if (entity.msgType == 2){
            values.put("time", entity.time);
        }else if (entity.msgType == 3){
            values.put("receive", entity.receive);
            values.put("money", entity.money);
            values.put("msg", entity.msg);
        }else if (entity.msgType == 4){
            values.put("receive", entity.receive);
            values.put("money", entity.money);
            values.put("receiveTransferId", entity.receiveTransferId);
        }else if (entity.msgType == 5){
            values.put("msg", entity.msg);
            values.put("transferOutTime", entity.transferOutTime);
            values.put("transferReceiveTime", entity.transferReceiveTime);
            values.put("receive", entity.receive);
            values.put("money", entity.money);
        }else if (entity.msgType == 6){
            values.put("receive", entity.receive);
            values.put("money", entity.money);
            values.put("receiveTransferId", entity.receiveTransferId);
        }else if (entity.msgType == 7){
            if (entity.voiceLength <= 0){
                values.put("voiceLength", 1);
            }else {
                values.put("voiceLength", entity.voiceLength);
            }

            values.put("msg", entity.msg);
            values.put("alreadyRead", entity.alreadyRead);
            values.put("voiceToText", entity.voiceToText);

        }else if (entity.msgType == 8){
            values.put("msg", entity.msg);
        }
        values.put("isComMsg", entity.isComMsg);
        values.put("lastTime",entity.lastTime);
        int position = allCaseNum(TABLE_NAME);
        values.put("position", position);
        entity.id = allCaseNum(TABLE_NAME) + 1;
        entity.position = position;
        long l = db.insert(TABLE_NAME,null,values);//插入第一条数据
        Log.e("insert " , l+"");
        if (entity.msgType != 2){
            EventBusUtil.post(entity);
        }

        return (int) l;
    }
    /**
     * 查询微信聊天与某人的单聊消息
     * @return
     */
    public ArrayList<QQScreenShotEntity> queryAll(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<QQScreenShotEntity> list = new ArrayList<>();
//如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            QQScreenShotEntity entity = new QQScreenShotEntity();
            int id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id
            entity.id = id;
            entity.wechatUserId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            entity.avatarInt = cursor.getInt(cursor.getColumnIndex("avatarInt"));
            entity.avatarStr = cursor.getString(cursor.getColumnIndex("avatarStr"));
            entity.msgType = cursor.getInt(cursor.getColumnIndex("msgType"));
            entity.isComMsg = "1".equals(cursor.getString(cursor.getColumnIndex("isComMsg")));
            entity.lastTime = cursor.getLong(cursor.getColumnIndex("lastTime"));
            entity.position = cursor.getInt(cursor.getColumnIndex("position"));
            if (entity.msgType == 0){
                entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
            }else if (entity.msgType == 1){
                entity.img = cursor.getInt(cursor.getColumnIndex("img"));
                entity.filePath = cursor.getString(cursor.getColumnIndex("filePath"));
            }else if (entity.msgType == 2){
                entity.time = cursor.getLong(cursor.getColumnIndex("time"));
            }else if (entity.msgType == 3){
                entity.receive = "1".equals(cursor.getString(cursor.getColumnIndex("receive")));
                entity.money = cursor.getString(cursor.getColumnIndex("money"));
                entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财";
                }
            }else if (entity.msgType == 4){
                entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
                entity.money = cursor.getString(cursor.getColumnIndex("money"));
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财";
                }
                entity.receive = "1".equals(cursor.getString(cursor.getColumnIndex("receive")));
                entity.receiveTransferId = cursor.getInt(cursor.getColumnIndex("receiveTransferId"));
            }else if (entity.msgType == 5){
                entity.transferOutTime = cursor.getLong(cursor.getColumnIndex("transferOutTime"));
                entity.transferReceiveTime = cursor.getLong(cursor.getColumnIndex("transferReceiveTime"));
                entity.receive = "1".equals(cursor.getString(cursor.getColumnIndex("receive")));
                entity.money = cursor.getString(cursor.getColumnIndex("money"));
                entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
            }else if (entity.msgType == 6){
                entity.transferOutTime = cursor.getLong(cursor.getColumnIndex("transferOutTime"));
                entity.transferReceiveTime = cursor.getLong(cursor.getColumnIndex("transferReceiveTime"));
                entity.receive = "1".equals(cursor.getString(cursor.getColumnIndex("receive")));
                entity.money = cursor.getString(cursor.getColumnIndex("money"));
                entity.receiveTransferId = cursor.getInt(cursor.getColumnIndex("receiveTransferId"));
            }else if (entity.msgType == 7){
                int voiceLength = cursor.getInt(cursor.getColumnIndex("voiceLength"));
                if (voiceLength <= 0){
                    entity.voiceLength = 1;
                }else {
                    entity.voiceLength = voiceLength;
                }

                entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
                entity.voiceToText = cursor.getString(cursor.getColumnIndex("voiceToText"));
                entity.alreadyRead = "1".equals(cursor.getString(cursor.getColumnIndex("alreadyRead")));
            }else if (entity.msgType == 8){
                entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
            }
            list.add(entity);
        }
        //关闭游标
        cursor.close();
        return list;
    }
    public int update(QQScreenShotEntity entity) {
        entity.tag = 1;
        int msgType = entity.msgType;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("wechatUserId" , entity.wechatUserId);
        cv.put("avatarInt" , entity.avatarInt);
        cv.put("avatarStr" , entity.avatarStr);
        cv.put("msgType", entity.msgType);
        cv.put("isComMsg", entity.isComMsg);
        cv.put("lastTime", entity.lastTime);
        cv.put("position", entity.position);
        if (msgType == 0){
            cv.put("msg" , entity.msg);
        }else if (msgType == 1){
            cv.put("img" , entity.img);
            cv.put("filePath" , entity.filePath);
        }else if (msgType == 2){
            cv.put("time" , entity.time);
        }else if (msgType == 3){
            cv.put("money" , entity.money);
            cv.put("receive" , entity.receive);
            cv.put("msg" , entity.msg);
        }else if (msgType == 4){
            cv.put("receive" , entity.receive);
        }else if (msgType == 5){
            cv.put("transferOutTime" , entity.transferOutTime);
            cv.put("transferReceiveTime" , entity.transferReceiveTime);
            cv.put("receive" , entity.receive);
            cv.put("money" , entity.money);
            cv.put("msg" , entity.msg);
        }else if (msgType == 6){
            cv.put("transferOutTime" , entity.transferOutTime);
            cv.put("transferReceiveTime" , entity.transferReceiveTime);
            cv.put("receive" , entity.receive);
            cv.put("money" , entity.money);
        }else if (msgType == 7){
            if (entity.voiceLength <= 0){
                cv.put("voiceLength" , 1);
            }else {
                cv.put("voiceLength" , entity.voiceLength);
            }
            cv.put("msg" , entity.msg);
            cv.put("voiceToText" , entity.voiceToText);
            cv.put("alreadyRead" , entity.alreadyRead);
        }else if (msgType == 8){
            cv.put("msg" , entity.msg);
        }
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
        if (msgType != 2){
            EventBusUtil.post(entity);
        }
//        db.close();
        return  result;
    }
//
    /**
     * 随机查询出一个用户信息
     * @return
     */
    public QQScreenShotEntity query(int receiveTransferId){
        SQLiteDatabase db = getReadableDatabase();
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        QQScreenShotEntity entity = null;
        Cursor cursor = db.query(TABLE_NAME, null, "receiveTransferId = ?", new String[]{String.valueOf(receiveTransferId)}, null, null, null);
        while (cursor.moveToNext()) {
            entity = new QQScreenShotEntity();
            entity.id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id;
            entity.wechatUserId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            entity.avatarInt = cursor.getInt(cursor.getColumnIndex("avatarInt"));
            entity.avatarStr = cursor.getString(cursor.getColumnIndex("avatarStr"));
            entity.msgType = cursor.getInt(cursor.getColumnIndex("msgType"));
            entity.isComMsg = "1".equals(cursor.getString(cursor.getColumnIndex("isComMsg")));
            entity.lastTime = cursor.getLong(cursor.getColumnIndex("lastTime"));
            entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
            entity.filePath = cursor.getString(cursor.getColumnIndex("filePath"));
            entity.img = cursor.getInt(cursor.getColumnIndex("img"));
            entity.time = cursor.getLong(cursor.getColumnIndex("time"));
            entity.receive = "1".equals(cursor.getString(cursor.getColumnIndex("receive")));
            entity.money = cursor.getString(cursor.getColumnIndex("money"));
            entity.position = cursor.getInt(cursor.getColumnIndex("position"));

            entity.transferOutTime = cursor.getLong(cursor.getColumnIndex("transferOutTime"));
            entity.transferReceiveTime = cursor.getLong(cursor.getColumnIndex("transferReceiveTime"));
            entity.receiveTransferId = cursor.getInt(cursor.getColumnIndex("receiveTransferId"));
            entity.voiceLength = cursor.getInt(cursor.getColumnIndex("voiceLength"));
            if (entity.voiceLength <= 0){
                entity.voiceLength = 1;
            }
            entity.alreadyRead = "1".equals(cursor.getString(cursor.getColumnIndex("alreadyRead")));
            entity.voiceToText = cursor.getString(cursor.getColumnIndex("voiceToText"));
            if (entity.msgType == 3){
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财";
                }
            }else if (entity.msgType == 4){
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财";
                }
            }
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
    public int delete(QQScreenShotEntity entity){
        SQLiteDatabase db = getWritableDatabase();
        String[] where = new String[] {String.valueOf(entity.id)};
        int result = db.delete(TABLE_NAME,"id=?", where);
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