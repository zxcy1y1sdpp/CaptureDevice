package app.jietuqi.cn.ui.wechatscreenshot.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import app.jietuqi.cn.database.IOpenHelper;
import app.jietuqi.cn.database.MyOpenHelper;
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity;
import app.jietuqi.cn.util.EventBusUtil;

/**
 * Created by 刘远博 on 2017/2/15.
 */
public class WechatScreenShotHelper extends MyOpenHelper implements IOpenHelper {
    /**
     * 数据库的名称
     */
    public final static String TABLE_NAME = "wechat_screen_shot_single";

    public WechatScreenShotHelper(Context context) {
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
                    "voiceLength integer, alreadyRead text, voiceToText text, position integer, isComMsg text, " +
                    "lastTime integer, resourceName text, timeType text");
            builder.append(")");
            db.execSQL(builder.toString());
        }
    }
    public int save(WechatScreenShotEntity wechatScreenShotEntity){
        wechatScreenShotEntity.tag = 0;
        createSingleTalkTable();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
        values.put("wechatUserId", wechatScreenShotEntity.wechatUserId);
        values.put("avatarInt", wechatScreenShotEntity.avatarInt);
        values.put("avatarStr", wechatScreenShotEntity.avatarStr);
        values.put("resourceName", wechatScreenShotEntity.resourceName);
        values.put("timeType", wechatScreenShotEntity.timeType);
        values.put("msgType", wechatScreenShotEntity.msgType);
        if (wechatScreenShotEntity.msgType == 0){
            values.put("msg", wechatScreenShotEntity.msg);
        }else if (wechatScreenShotEntity.msgType == 1){
            values.put("img", wechatScreenShotEntity.img);
            values.put("filePath", wechatScreenShotEntity.filePath);
            values.put("msg", "图片");
        }else if (wechatScreenShotEntity.msgType == 2){
            values.put("time", wechatScreenShotEntity.time);
        }else if (wechatScreenShotEntity.msgType == 3){
            values.put("receive", wechatScreenShotEntity.receive);
            values.put("money", wechatScreenShotEntity.money);
            values.put("msg", wechatScreenShotEntity.msg);
        }else if (wechatScreenShotEntity.msgType == 4){
            values.put("receive", wechatScreenShotEntity.receive);
            values.put("money", wechatScreenShotEntity.money);
            values.put("receiveTransferId", wechatScreenShotEntity.receiveTransferId);
        }else if (wechatScreenShotEntity.msgType == 5){
            values.put("msg", wechatScreenShotEntity.msg);
            values.put("transferOutTime", wechatScreenShotEntity.transferOutTime);
            values.put("transferReceiveTime", wechatScreenShotEntity.transferReceiveTime);
            values.put("receive", wechatScreenShotEntity.receive);
            values.put("money", wechatScreenShotEntity.money);
        }else if (wechatScreenShotEntity.msgType == 6){
            values.put("receive", wechatScreenShotEntity.receive);
            values.put("money", wechatScreenShotEntity.money);
            values.put("receiveTransferId", wechatScreenShotEntity.receiveTransferId);
        }else if (wechatScreenShotEntity.msgType == 7){
            if (wechatScreenShotEntity.voiceLength <= 0){
                values.put("voiceLength", 1);
            }else {
                values.put("voiceLength", wechatScreenShotEntity.voiceLength);
            }

            values.put("msg", wechatScreenShotEntity.msg);
            values.put("alreadyRead", wechatScreenShotEntity.alreadyRead);
            values.put("voiceToText", wechatScreenShotEntity.voiceToText);

        }else if (wechatScreenShotEntity.msgType == 8){
            values.put("msg", wechatScreenShotEntity.msg);
        }
        values.put("isComMsg", wechatScreenShotEntity.isComMsg);
        values.put("lastTime",wechatScreenShotEntity.lastTime);
        int position = allCaseNum(TABLE_NAME);
        values.put("position", position);
        wechatScreenShotEntity.position = position;
        long l = db.insert(TABLE_NAME,null,values);//插入第一条数据
        wechatScreenShotEntity.id = (int) l;
        Log.e("insert " , l+"");
//        if (wechatScreenShotEntity.msgType != 2){
        EventBusUtil.post(wechatScreenShotEntity);
//        }
        return (int) l;
    }
    /**
     * 查询微信聊天与某人的单聊消息
     * @return
     */
    public ArrayList<WechatScreenShotEntity> queryAll(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WechatScreenShotEntity> list = new ArrayList<>();
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            WechatScreenShotEntity entity = new WechatScreenShotEntity();
            int id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id
            entity.id = id;
            entity.wechatUserId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            entity.avatarInt = cursor.getInt(cursor.getColumnIndex("avatarInt"));
            entity.avatarStr = cursor.getString(cursor.getColumnIndex("avatarStr"));
            entity.resourceName = cursor.getString(cursor.getColumnIndex("resourceName"));
            entity.timeType = cursor.getString(cursor.getColumnIndex("timeType"));
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
    public int update(WechatScreenShotEntity wechatScreenShotEntity) {
        wechatScreenShotEntity.tag = 1;
        int msgType = wechatScreenShotEntity.msgType;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("wechatUserId" , wechatScreenShotEntity.wechatUserId);
        cv.put("avatarInt" , wechatScreenShotEntity.avatarInt);
        cv.put("avatarStr" , wechatScreenShotEntity.avatarStr);
        cv.put("resourceName" , wechatScreenShotEntity.resourceName);
        cv.put("timeType" , wechatScreenShotEntity.timeType);
        cv.put("msgType", wechatScreenShotEntity.msgType);
        cv.put("isComMsg", wechatScreenShotEntity.isComMsg);
        cv.put("lastTime", wechatScreenShotEntity.lastTime);
        cv.put("position", wechatScreenShotEntity.position);
        if (msgType == 0){
            cv.put("msg" , wechatScreenShotEntity.msg);
        }else if (msgType == 1){
            cv.put("img" , wechatScreenShotEntity.img);
            cv.put("filePath" , wechatScreenShotEntity.filePath);
        }else if (msgType == 2){
            cv.put("time" , wechatScreenShotEntity.time);
        }else if (msgType == 3){
            cv.put("money" , wechatScreenShotEntity.money);
            cv.put("receive" , wechatScreenShotEntity.receive);
            cv.put("msg" , wechatScreenShotEntity.msg);
        }else if (msgType == 4){
            cv.put("receive" , wechatScreenShotEntity.receive);
        }else if (msgType == 5){
            cv.put("transferOutTime" , wechatScreenShotEntity.transferOutTime);
            cv.put("transferReceiveTime" , wechatScreenShotEntity.transferReceiveTime);
            cv.put("receive" , wechatScreenShotEntity.receive);
            cv.put("money" , wechatScreenShotEntity.money);
            cv.put("msg" , wechatScreenShotEntity.msg);
        }else if (msgType == 6){
            cv.put("transferOutTime" , wechatScreenShotEntity.transferOutTime);
            cv.put("transferReceiveTime" , wechatScreenShotEntity.transferReceiveTime);
            cv.put("receive" , wechatScreenShotEntity.receive);
            cv.put("money" , wechatScreenShotEntity.money);
        }else if (msgType == 7){
            if (wechatScreenShotEntity.voiceLength <= 0){
                cv.put("voiceLength" , 1);
            }else {
                cv.put("voiceLength" , wechatScreenShotEntity.voiceLength);
            }
            cv.put("msg" , wechatScreenShotEntity.msg);
            cv.put("voiceToText" , wechatScreenShotEntity.voiceToText);
            cv.put("alreadyRead" , wechatScreenShotEntity.alreadyRead);
        }else if (msgType == 8){
            cv.put("msg" , wechatScreenShotEntity.msg);
        }
        int result = 0;
        if (db.isOpen()) {
            try {
                String[] where = new String[] {String.valueOf(wechatScreenShotEntity.id)};
                result = db.update(TABLE_NAME, cv, "id=?", where);
                Log.e("update ", "result : " + result);
            }catch (Exception e){
                Log.e("db", "Exception : " + e.getMessage());
            }
        }
        EventBusUtil.post(wechatScreenShotEntity);
//        db.close();
        return  result;
    }
//
    /**
     * 随机查询出一个用户信息
     * @return
     */
    public WechatScreenShotEntity query(int receiveTransferId){
        SQLiteDatabase db = getReadableDatabase();
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        WechatScreenShotEntity entity = null;
        Cursor cursor = db.query(TABLE_NAME, null, "receiveTransferId = ?", new String[]{String.valueOf(receiveTransferId)}, null, null, null);
        while (cursor.moveToNext()) {
            entity = new WechatScreenShotEntity();
            entity.id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id;
            entity.wechatUserId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            entity.avatarInt = cursor.getInt(cursor.getColumnIndex("avatarInt"));
            entity.avatarStr = cursor.getString(cursor.getColumnIndex("avatarStr"));
            entity.resourceName = cursor.getString(cursor.getColumnIndex("resourceName"));
            entity.timeType = cursor.getString(cursor.getColumnIndex("timeType"));
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
            try {
                entity.voiceToText = cursor.getString(cursor.getColumnIndex("voiceToText"));
            }catch (NullPointerException e){
                Log.e("微信截图", "读取语音转文字的时候为null");
            }
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
    public int delete(WechatScreenShotEntity entity){
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