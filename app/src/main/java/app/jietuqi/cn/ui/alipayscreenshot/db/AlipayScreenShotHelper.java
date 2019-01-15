package app.jietuqi.cn.ui.alipayscreenshot.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import app.jietuqi.cn.database.IOpenHelper;
import app.jietuqi.cn.database.MyOpenHelper;
import app.jietuqi.cn.ui.alipayscreenshot.entity.AlipayScreenShotEntity;
import app.jietuqi.cn.util.EventBusUtil;

/**
 * Created by 刘远博 on 2017/2/15.
 */
public class AlipayScreenShotHelper extends MyOpenHelper implements IOpenHelper {
    /**
     * 数据库的名称
     */
    public final static String TABLE_NAME = "alipay_screen_shot_single";

    public AlipayScreenShotHelper(Context context) {
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
    public int save(AlipayScreenShotEntity alipayScreenShotEntity){
        alipayScreenShotEntity.tag = 0;
        createSingleTalkTable();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
        values.put("wechatUserId", alipayScreenShotEntity.wechatUserId);
        values.put("avatarInt", alipayScreenShotEntity.avatarInt);
        values.put("avatarStr", alipayScreenShotEntity.avatarStr);
        values.put("msgType", alipayScreenShotEntity.msgType);
        if (alipayScreenShotEntity.msgType == 0){
            values.put("msg", alipayScreenShotEntity.msg);
        }else if (alipayScreenShotEntity.msgType == 1){
            values.put("img", alipayScreenShotEntity.img);
            values.put("filePath", alipayScreenShotEntity.filePath);
            values.put("msg", "图片");
        }else if (alipayScreenShotEntity.msgType == 2){
            values.put("time", alipayScreenShotEntity.time);
        }else if (alipayScreenShotEntity.msgType == 3){
            values.put("receive", alipayScreenShotEntity.receive);
            values.put("money", alipayScreenShotEntity.money);
            values.put("msg", alipayScreenShotEntity.msg);
        }else if (alipayScreenShotEntity.msgType == 4){
            values.put("receive", alipayScreenShotEntity.receive);
            values.put("money", alipayScreenShotEntity.money);
            values.put("receiveTransferId", alipayScreenShotEntity.receiveTransferId);
        }else if (alipayScreenShotEntity.msgType == 5){
            values.put("msg", alipayScreenShotEntity.msg);
            values.put("transferOutTime", alipayScreenShotEntity.transferOutTime);
            values.put("transferReceiveTime", alipayScreenShotEntity.transferReceiveTime);
            values.put("receive", alipayScreenShotEntity.receive);
            values.put("money", alipayScreenShotEntity.money);
        }else if (alipayScreenShotEntity.msgType == 6){
            values.put("receive", alipayScreenShotEntity.receive);
            values.put("money", alipayScreenShotEntity.money);
            values.put("receiveTransferId", alipayScreenShotEntity.receiveTransferId);
        }else if (alipayScreenShotEntity.msgType == 7){
            if (alipayScreenShotEntity.voiceLength <= 0){
                values.put("voiceLength", 1);
            }else {
                values.put("voiceLength", alipayScreenShotEntity.voiceLength);
            }

            values.put("msg", alipayScreenShotEntity.msg);
            values.put("alreadyRead", alipayScreenShotEntity.alreadyRead);
            values.put("voiceToText", alipayScreenShotEntity.voiceToText);

        }else if (alipayScreenShotEntity.msgType == 8){
            values.put("msg", alipayScreenShotEntity.msg);
        }
        values.put("isComMsg", alipayScreenShotEntity.isComMsg);
        values.put("lastTime",alipayScreenShotEntity.lastTime);
        int position = allCaseNum(TABLE_NAME);
        values.put("position", position);
        alipayScreenShotEntity.id = allCaseNum(TABLE_NAME) + 1;
        alipayScreenShotEntity.position = position;
        long l = db.insert(TABLE_NAME,null,values);//插入第一条数据
        Log.e("insert " , l+"");
        if (alipayScreenShotEntity.msgType != 2){
            EventBusUtil.post(alipayScreenShotEntity);
        }
        return (int) l;
    }
    /**
     * 查询微信聊天与某人的单聊消息
     * @return
     */
    public ArrayList<AlipayScreenShotEntity> queryAll(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<AlipayScreenShotEntity> list = new ArrayList<>();
//如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            AlipayScreenShotEntity entity = new AlipayScreenShotEntity();
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
                    entity.msg = "恭喜发财，大吉大利";
                }
            }else if (entity.msgType == 4){
                entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
                entity.money = cursor.getString(cursor.getColumnIndex("money"));
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财，大吉大利";
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
    public int update(AlipayScreenShotEntity alipayScreenShotEntity) {
        alipayScreenShotEntity.tag = 1;
        int msgType = alipayScreenShotEntity.msgType;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("wechatUserId" , alipayScreenShotEntity.wechatUserId);
        cv.put("avatarInt" , alipayScreenShotEntity.avatarInt);
        cv.put("avatarStr" , alipayScreenShotEntity.avatarStr);
        cv.put("msgType", alipayScreenShotEntity.msgType);
        cv.put("isComMsg", alipayScreenShotEntity.isComMsg);
        cv.put("lastTime", alipayScreenShotEntity.lastTime);
        cv.put("position", alipayScreenShotEntity.position);
        if (msgType == 0){
            cv.put("msg" , alipayScreenShotEntity.msg);
        }else if (msgType == 1){
            cv.put("img" , alipayScreenShotEntity.img);
            cv.put("filePath" , alipayScreenShotEntity.filePath);
        }else if (msgType == 2){
            cv.put("time" , alipayScreenShotEntity.time);
        }else if (msgType == 3){
            cv.put("money" , alipayScreenShotEntity.money);
            cv.put("receive" , alipayScreenShotEntity.receive);
            cv.put("msg" , alipayScreenShotEntity.msg);
        }else if (msgType == 4){
            cv.put("receive" , alipayScreenShotEntity.receive);
        }else if (msgType == 5){
            cv.put("transferOutTime" , alipayScreenShotEntity.transferOutTime);
            cv.put("transferReceiveTime" , alipayScreenShotEntity.transferReceiveTime);
            cv.put("receive" , alipayScreenShotEntity.receive);
            cv.put("money" , alipayScreenShotEntity.money);
            cv.put("msg" , alipayScreenShotEntity.msg);
        }else if (msgType == 6){
            cv.put("transferOutTime" , alipayScreenShotEntity.transferOutTime);
            cv.put("transferReceiveTime" , alipayScreenShotEntity.transferReceiveTime);
            cv.put("receive" , alipayScreenShotEntity.receive);
            cv.put("money" , alipayScreenShotEntity.money);
        }else if (msgType == 7){
            if (alipayScreenShotEntity.voiceLength <= 0){
                cv.put("voiceLength" , 1);
            }else {
                cv.put("voiceLength" , alipayScreenShotEntity.voiceLength);
            }
            cv.put("msg" , alipayScreenShotEntity.msg);
            cv.put("voiceToText" , alipayScreenShotEntity.voiceToText);
            cv.put("alreadyRead" , alipayScreenShotEntity.alreadyRead);
        }else if (msgType == 8){
            cv.put("msg" , alipayScreenShotEntity.msg);
        }
        int result = 0;
        if (db.isOpen()) {
            try {
                String[] where = new String[] {String.valueOf(alipayScreenShotEntity.id)};
                result = db.update(TABLE_NAME, cv, "id=?", where);
                Log.e("update ", "result : " + result);
            }catch (Exception e){
                Log.e("db", "Exception : " + e.getMessage());
            }
        }
        if (msgType != 2){
            EventBusUtil.post(alipayScreenShotEntity);
        }
//        db.close();
        return  result;
    }
//
    /**
     * 随机查询出一个用户信息
     * @return
     */
    public AlipayScreenShotEntity query(int receiveTransferId){
        SQLiteDatabase db = getReadableDatabase();
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        AlipayScreenShotEntity entity = null;
        Cursor cursor = db.query(TABLE_NAME, null, "receiveTransferId = ?", new String[]{String.valueOf(receiveTransferId)}, null, null, null);
        while (cursor.moveToNext()) {
            entity = new AlipayScreenShotEntity();
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
                    entity.msg = "恭喜发财，大吉大利";
                }
            }else if (entity.msgType == 4){
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财，大吉大利";
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
    public int delete(AlipayScreenShotEntity entity){
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