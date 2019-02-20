package app.jietuqi.cn.wechat.simulator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import app.jietuqi.cn.ui.entity.WechatUserEntity;
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity;

import static app.jietuqi.cn.constant.DatabaseConfig.DATABASE_NAME;
import static app.jietuqi.cn.constant.DatabaseConfig.DATABASE_VERSION;

/**
 * 作者： liuyuanbo on 2019/1/9 10:25.
 * 时间： 2019/1/9 10:25
 * 邮箱： 972383753@qq.com
 * 用途： 微信模拟器首页列表的数据操作
 */
public class WechatSimulatorListHelper extends SQLiteOpenHelper {
    /**
     * 数据库的名称
     */
    public static String TABLE_NAME = "wechatSimulatorList";

    public WechatSimulatorListHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        create();
        onUpgrade(db, db.getVersion(), DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    /**
     * 单聊表名的规则 -- wechat_single + 对象id
     */
    public void create() {
        if (!isTableExists(TABLE_NAME)){
            SQLiteDatabase db = getWritableDatabase();
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ");
            builder.append(TABLE_NAME);
            builder.append(" (");
            builder.append("id Integer PRIMARY KEY AUTOINCREMENT,");
            builder.append("wechatUserId text, avatarInt integer, wechatUserNickName text, avatarStr text, msgType integer, msg text, time integer, receive text, money text, " +
                    " alreadyRead text, position integer, isComMsg text, top text, showPoint text, unReadNum, lastTime integer, chatBg text, resourceName text, timeType text," +
                    "groupHeader BLOB, chatType text, groupRoles text, groupName text, groupTableName text, recentRoles text, groupRoleCount integer, groupShowNickName text," +
                    "groupRedPacketInfo BLOB");
            builder.append(")");
            db.execSQL(builder.toString());
        }
    }
    public int save(WechatUserEntity entity){
        create();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
        values.put("wechatUserId", entity.wechatUserId);
        values.put("avatarInt", entity.avatarInt);
        values.put("avatarStr", entity.wechatUserAvatar);
        values.put("resourceName", entity.resourceName);
        values.put("timeType", entity.timeType);
        values.put("wechatUserNickName", entity.wechatUserNickName);
        values.put("msgType", entity.msgType);
        values.put("receive", entity.receive);
        values.put("money", entity.money);
        values.put("msg", entity.msg);
        values.put("unReadNum", entity.unReadNum);
        values.put("time", entity.time);
        values.put("alreadyRead", entity.alreadyRead);
        values.put("isComMsg", entity.isComMsg);
        values.put("top", entity.top);
        values.put("showPoint", entity.showPoint);
        values.put("lastTime",entity.lastTime);
        values.put("chatBg",entity.chatBg);
        values.put("chatType",entity.chatType);

        /** ----------------- 群聊相关 ----------------- */
        values.put("groupName",entity.groupName);
        values.put("groupShowNickName",entity.groupShowNickName);
        values.put("groupRoleCount",entity.groupRoleCount);
        values.put("groupTableName", entity.groupTableName);
        if (null != entity.groupHeader){
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            entity.groupHeader.compress(Bitmap.CompressFormat.PNG, 100, os);
            values.put("groupHeader", os.toByteArray());
        }
        if (null != entity.groupRoles){
            Gson gson = new Gson();
            String inputString= gson.toJson(entity.groupRoles);
            values.put("groupRoles", inputString);
        }
        if (null != entity.groupRedPacketInfo){
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
                objectOutputStream.writeObject(entity.groupRedPacketInfo);
                objectOutputStream.flush();
                byte data[] = arrayOutputStream.toByteArray();
                values.put("groupRedPacketInfo", data);
                objectOutputStream.close();
                arrayOutputStream.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        int position = allCaseNum(TABLE_NAME);
        values.put("position", position);
        entity.position = position;
        long l = db.insert(TABLE_NAME,null,values);//插入第一条数据
        entity.id = (int) l;
        Log.e("insert " , l+"");
        return (int) l;
    }
    /**
     * 查询微信聊天与某人的单聊消息
     * @return
     */
    public ArrayList<WechatUserEntity> queryAll(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WechatUserEntity> list = new ArrayList<>();
//如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "top desc, lastTime desc");
        while (cursor.moveToNext()) {
            WechatUserEntity entity = new WechatUserEntity();
            entity.id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id;
            entity.wechatUserId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            entity.avatarInt = cursor.getInt(cursor.getColumnIndex("avatarInt"));
            entity.wechatUserAvatar = cursor.getString(cursor.getColumnIndex("avatarStr"));
            entity.resourceName = cursor.getString(cursor.getColumnIndex("resourceName"));
            entity.timeType = cursor.getString(cursor.getColumnIndex("timeType"));
            entity.wechatUserNickName = cursor.getString(cursor.getColumnIndex("wechatUserNickName"));
            entity.msgType = cursor.getString(cursor.getColumnIndex("msgType"));
            entity.isComMsg = "1".equals(cursor.getString(cursor.getColumnIndex("isComMsg")));
            entity.top = "1".equals(cursor.getString(cursor.getColumnIndex("top")));
            entity.showPoint = "1".equals(cursor.getString(cursor.getColumnIndex("showPoint")));
            entity.lastTime = cursor.getLong(cursor.getColumnIndex("lastTime"));
            entity.position = cursor.getInt(cursor.getColumnIndex("position"));
            entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
            entity.unReadNum = cursor.getString(cursor.getColumnIndex("unReadNum"));
            entity.time = cursor.getLong(cursor.getColumnIndex("time"));
            entity.receive = "1".equals(cursor.getString(cursor.getColumnIndex("receive")));
            entity.money = cursor.getString(cursor.getColumnIndex("money"));
            entity.alreadyRead = "1".equals(cursor.getString(cursor.getColumnIndex("alreadyRead")));
            entity.chatBg = cursor.getString(cursor.getColumnIndex("chatBg"));

            /** ----------------- 群聊相关 ----------------- */
            byte[] in = cursor.getBlob(cursor.getColumnIndex("groupHeader"));
            if (null != in){
                entity.groupShowNickName = "1".equals(cursor.getString(cursor.getColumnIndex("groupShowNickName")));
                entity.groupTableName = cursor.getString(cursor.getColumnIndex("groupTableName"));
                entity.groupRoleCount = cursor.getInt(cursor.getColumnIndex("groupRoleCount"));
                entity.groupHeader = BitmapFactory.decodeByteArray(in,0,in.length);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<WechatUserEntity>>() {}.getType();
                String role = cursor.getString(cursor.getColumnIndex("groupRoles"));
                ArrayList<WechatUserEntity> roles = gson.fromJson(role, type);
                entity.groupRoles = roles;
                entity.groupName = cursor.getString(cursor.getColumnIndex("groupName"));
            }
            byte data[] = cursor.getBlob(cursor.getColumnIndex("groupRedPacketInfo"));
            if (null != data){
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    entity.groupRedPacketInfo = (WechatScreenShotEntity) inputStream.readObject();
                    inputStream.close();
                    arrayInputStream.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            entity.chatType = cursor.getInt(cursor.getColumnIndex("chatType"));

            if ("3".equals(entity.msgType)){
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财，大吉大利";
                }
            }else if ("4".equals(entity.msgType)){
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财，大吉大利";
                }
            }
            list.add(entity);
        }
        //关闭游标
        cursor.close();
        return list;
    }

    public int update(WechatUserEntity entity) {
        create();
        SQLiteDatabase db = getWritableDatabase();
        int amount;
        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(sql, null);
        amount = c.getCount();
        if (amount == 0){
            return save(entity);
        }
        ContentValues cv = new ContentValues();
        cv.put("wechatUserId" , entity.wechatUserId);
        cv.put("avatarInt" , entity.avatarInt);
        cv.put("avatarStr" , entity.wechatUserAvatar);
        cv.put("resourceName" , entity.resourceName);
        cv.put("timeType" , entity.timeType);
        cv.put("wechatUserNickName" , entity.wechatUserNickName);
        cv.put("msgType", entity.msgType);
        cv.put("isComMsg", entity.isComMsg);
        cv.put("top", entity.top);
        cv.put("showPoint", entity.showPoint);
        cv.put("lastTime", entity.lastTime);
        cv.put("position", entity.position);
        cv.put("msg" , entity.msg);
        cv.put("unReadNum" , entity.unReadNum);
        cv.put("time" , entity.time);
        cv.put("money" , entity.money);
        cv.put("receive" , entity.receive);
        cv.put("alreadyRead" , entity.alreadyRead);
        cv.put("chatBg" , entity.chatBg);
        cv.put("chatType" , entity.chatType);

        /** ----------------- 群聊相关 ----------------- */
        cv.put("groupShowNickName",entity.groupShowNickName);
        cv.put("groupName",entity.groupName);
        cv.put("groupRoleCount",entity.groupRoleCount);
        cv.put("groupTableName",entity.groupTableName);
        if (null != entity.groupHeader){
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            entity.groupHeader.compress(Bitmap.CompressFormat.PNG, 100, os);
            cv.put("groupHeader", os.toByteArray());
        }
        if (null != entity.groupRoles){
            Gson gson = new Gson();
            String inputString = gson.toJson(entity.groupRoles);
            cv.put("groupRoles", inputString);
        }
        if (null != entity.groupRedPacketInfo){
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
                objectOutputStream.writeObject(entity.groupRedPacketInfo);
                objectOutputStream.flush();
                byte data[] = arrayOutputStream.toByteArray();
                cv.put("groupRedPacketInfo", data);
                objectOutputStream.close();
                arrayOutputStream.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
//        db.close();
        return  result;
    }
//
    /**
     * 查询出一个用户信息
     * @return
     */
    public WechatUserEntity query(int receiveTransferId){
        SQLiteDatabase db = getReadableDatabase();
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        WechatUserEntity entity = null;
        Cursor cursor = db.query(TABLE_NAME, null, "receiveTransferId = ?", new String[]{String.valueOf(receiveTransferId)}, null, null, null);
        while (cursor.moveToNext()) {
            entity = new WechatUserEntity();
            entity.id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id;
            entity.wechatUserId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            entity.avatarInt = cursor.getInt(cursor.getColumnIndex("avatarInt"));
            entity.wechatUserAvatar = cursor.getString(cursor.getColumnIndex("avatarStr"));
            entity.resourceName = cursor.getString(cursor.getColumnIndex("resourceName"));
            entity.timeType = cursor.getString(cursor.getColumnIndex("timeType"));
            entity.wechatUserNickName = cursor.getString(cursor.getColumnIndex("wechatUserNickName"));
            entity.msgType = cursor.getString(cursor.getColumnIndex("msgType"));
            entity.isComMsg = "1".equals(cursor.getString(cursor.getColumnIndex("isComMsg")));
            entity.top = "1".equals(cursor.getString(cursor.getColumnIndex("top")));
            entity.showPoint = "1".equals(cursor.getString(cursor.getColumnIndex("showPoint")));
            entity.lastTime = cursor.getLong(cursor.getColumnIndex("lastTime"));
            entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
            entity.unReadNum = cursor.getString(cursor.getColumnIndex("unReadNum"));
            entity.time = cursor.getLong(cursor.getColumnIndex("time"));
            entity.receive = "1".equals(cursor.getString(cursor.getColumnIndex("receive")));
            entity.money = cursor.getString(cursor.getColumnIndex("money"));
            entity.position = cursor.getInt(cursor.getColumnIndex("position"));
            entity.chatBg = cursor.getString(cursor.getColumnIndex("chatBg"));
            entity.alreadyRead = "1".equals(cursor.getString(cursor.getColumnIndex("alreadyRead")));
            /** ----------------- 群聊相关 ----------------- */
            byte[] in=cursor.getBlob(cursor.getColumnIndex("groupHeader"));
            if (null != in){
                entity.groupName = cursor.getString(cursor.getColumnIndex("groupName"));
                entity.groupShowNickName = "1".equals(cursor.getString(cursor.getColumnIndex("groupShowNickName")));
                entity.groupRoleCount = cursor.getInt(cursor.getColumnIndex("groupRoleCount"));
                entity.groupTableName = cursor.getString(cursor.getColumnIndex("groupTableName"));
                entity.groupHeader = BitmapFactory.decodeByteArray(in,0,in.length);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<WechatUserEntity>>() {}.getType();
                ArrayList<WechatUserEntity> roles = gson.fromJson(cursor.getString(cursor.getColumnIndex("groupRoles")), type);
                entity.groupRoles = roles;
            }
            byte data[] = cursor.getBlob(cursor.getColumnIndex("groupRedPacketInfo"));
            if (null != data){
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    entity.groupRedPacketInfo = (WechatScreenShotEntity) inputStream.readObject();
                    inputStream.close();
                    arrayInputStream.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            entity.chatType = cursor.getInt(cursor.getColumnIndex("chatType"));
            if ("3".equals(entity.msgType)){
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财，大吉大利";
                }
            }else if ("4".equals(entity.msgType)){
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
     * 根据id查询指定用户数据在聊天列表中的主键值
     * @return
     */
    public int queryByWechatUserId(String wechatUserId){
        SQLiteDatabase db = getReadableDatabase();
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return -1;
        }
        WechatUserEntity entity = null;
        Cursor cursor = db.query(TABLE_NAME, null, "wechatUserId = ?", new String[]{wechatUserId}, null, null, null);
        while (cursor.moveToNext()) {
            entity = new WechatUserEntity();
            entity.id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id;
            entity.wechatUserId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            entity.avatarInt = cursor.getInt(cursor.getColumnIndex("avatarInt"));
            entity.wechatUserAvatar = cursor.getString(cursor.getColumnIndex("avatarStr"));
            entity.resourceName = cursor.getString(cursor.getColumnIndex("resourceName"));
            entity.timeType = cursor.getString(cursor.getColumnIndex("timeType"));
            entity.wechatUserNickName = cursor.getString(cursor.getColumnIndex("wechatUserNickName"));
            entity.msgType = cursor.getString(cursor.getColumnIndex("msgType"));
            entity.isComMsg = "1".equals(cursor.getString(cursor.getColumnIndex("isComMsg")));
            entity.top = "1".equals(cursor.getString(cursor.getColumnIndex("top")));
            entity.showPoint = "1".equals(cursor.getString(cursor.getColumnIndex("showPoint")));
            entity.lastTime = cursor.getLong(cursor.getColumnIndex("lastTime"));
            entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
            entity.unReadNum = cursor.getString(cursor.getColumnIndex("unReadNum"));
            entity.time = cursor.getLong(cursor.getColumnIndex("time"));
            entity.receive = "1".equals(cursor.getString(cursor.getColumnIndex("receive")));
            entity.money = cursor.getString(cursor.getColumnIndex("money"));
            entity.position = cursor.getInt(cursor.getColumnIndex("position"));
            entity.chatBg = cursor.getString(cursor.getColumnIndex("chatBg"));
            entity.alreadyRead = "1".equals(cursor.getString(cursor.getColumnIndex("alreadyRead")));
            entity.chatType = cursor.getInt(cursor.getColumnIndex("chatType"));
            /** ----------------- 群聊相关 ----------------- */
            byte[] in=cursor.getBlob(cursor.getColumnIndex("groupHeader"));
            if (null != in){
                entity.groupShowNickName = "1".equals(cursor.getString(cursor.getColumnIndex("groupShowNickName")));
                entity.groupName = cursor.getString(cursor.getColumnIndex("groupName"));
                entity.groupRoleCount = cursor.getInt(cursor.getColumnIndex("groupRoleCount"));
                entity.groupTableName = cursor.getString(cursor.getColumnIndex("groupTableName"));
                entity.groupHeader = BitmapFactory.decodeByteArray(in,0,in.length);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<WechatUserEntity>>() {}.getType();
                ArrayList<WechatUserEntity> roles = gson.fromJson(cursor.getString(cursor.getColumnIndex("groupRoles")), type);
                entity.groupRoles = roles;
            }
            byte data[] = cursor.getBlob(cursor.getColumnIndex("groupRedPacketInfo"));
            if (null != data){
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    entity.groupRedPacketInfo = (WechatScreenShotEntity) inputStream.readObject();
                    inputStream.close();
                    arrayInputStream.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if ("3".equals(entity.msgType)){
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财，大吉大利";
                }
            }else if ("4".equals(entity.msgType)){
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财，大吉大利";
                }
            }
        }
        //关闭游标
        cursor.close();
        if (null == entity){
            return 1;
        }else {
            return entity.id;
        }
    }

    /**
     * 查看是改id的用户是否在表里
     * @param wechatUserId
     * @return
     */
    public WechatUserEntity queryIfInThisTable(String wechatUserId){
        SQLiteDatabase db = getReadableDatabase();
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        WechatUserEntity entity = null;
        Cursor cursor = db.query(TABLE_NAME, null, "wechatUserId = ?", new String[]{wechatUserId}, null, null, null);
        while (cursor.moveToNext()) {
            entity = new WechatUserEntity();
            entity.id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id;
            entity.wechatUserId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            entity.avatarInt = cursor.getInt(cursor.getColumnIndex("avatarInt"));
            entity.wechatUserAvatar = cursor.getString(cursor.getColumnIndex("avatarStr"));
            entity.resourceName = cursor.getString(cursor.getColumnIndex("resourceName"));
            entity.timeType = cursor.getString(cursor.getColumnIndex("timeType"));
            entity.wechatUserNickName = cursor.getString(cursor.getColumnIndex("wechatUserNickName"));
            entity.msgType = cursor.getString(cursor.getColumnIndex("msgType"));
            entity.isComMsg = "1".equals(cursor.getString(cursor.getColumnIndex("isComMsg")));
            entity.top = "1".equals(cursor.getString(cursor.getColumnIndex("top")));
            entity.showPoint = "1".equals(cursor.getString(cursor.getColumnIndex("showPoint")));
            entity.lastTime = cursor.getLong(cursor.getColumnIndex("lastTime"));
            entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
            entity.unReadNum = cursor.getString(cursor.getColumnIndex("unReadNum"));
            entity.time = cursor.getLong(cursor.getColumnIndex("time"));
            entity.receive = "1".equals(cursor.getString(cursor.getColumnIndex("receive")));
            entity.money = cursor.getString(cursor.getColumnIndex("money"));
            entity.position = cursor.getInt(cursor.getColumnIndex("position"));
            entity.chatBg = cursor.getString(cursor.getColumnIndex("chatBg"));
            entity.alreadyRead = "1".equals(cursor.getString(cursor.getColumnIndex("alreadyRead")));

            /** ----------------- 群聊相关 ----------------- */
            byte[] in=cursor.getBlob(cursor.getColumnIndex("groupHeader"));
            if (null != in){
                entity.groupShowNickName = "1".equals(cursor.getString(cursor.getColumnIndex("groupShowNickName")));
                entity.groupName = cursor.getString(cursor.getColumnIndex("groupName"));
                entity.groupRoleCount = cursor.getInt(cursor.getColumnIndex("groupRoleCount"));
                entity.groupTableName = cursor.getString(cursor.getColumnIndex("groupTableName"));
                entity.groupHeader = BitmapFactory.decodeByteArray(in,0,in.length);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<WechatUserEntity>>() {}.getType();
                ArrayList<WechatUserEntity> roles = gson.fromJson(cursor.getString(cursor.getColumnIndex("groupRoles")), type);
                entity.groupRoles = roles;
            }
            byte data[] = cursor.getBlob(cursor.getColumnIndex("groupRedPacketInfo"));
            if (null != data){
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    entity.groupRedPacketInfo = (WechatScreenShotEntity) inputStream.readObject();
                    inputStream.close();
                    arrayInputStream.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            entity.chatType = cursor.getInt(cursor.getColumnIndex("chatType"));

            if ("3".equals(entity.msgType)){
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财，大吉大利";
                }
            }else if ("4".equals(entity.msgType)){
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财，大吉大利";
                }
            }
        }
        //关闭游标
        cursor.close();
        return entity;
    }
    public WechatUserEntity query(String wechatUserId){
        SQLiteDatabase db = getReadableDatabase();
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        WechatUserEntity entity = null;
        Cursor cursor = db.query(TABLE_NAME, null, "wechatUserId = ?", new String[]{wechatUserId}, null, null, null);
        while (cursor.moveToNext()) {
            entity = new WechatUserEntity();
            entity.id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id;
            entity.wechatUserId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            entity.avatarInt = cursor.getInt(cursor.getColumnIndex("avatarInt"));
            entity.wechatUserAvatar = cursor.getString(cursor.getColumnIndex("avatarStr"));
            entity.resourceName = cursor.getString(cursor.getColumnIndex("resourceName"));
            entity.timeType = cursor.getString(cursor.getColumnIndex("timeType"));
            entity.wechatUserNickName = cursor.getString(cursor.getColumnIndex("wechatUserNickName"));
            entity.msgType = cursor.getString(cursor.getColumnIndex("msgType"));
            entity.isComMsg = "1".equals(cursor.getString(cursor.getColumnIndex("isComMsg")));
            entity.top = "1".equals(cursor.getString(cursor.getColumnIndex("top")));
            entity.showPoint = "1".equals(cursor.getString(cursor.getColumnIndex("showPoint")));
            entity.lastTime = cursor.getLong(cursor.getColumnIndex("lastTime"));
            entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
            entity.unReadNum = cursor.getString(cursor.getColumnIndex("unReadNum"));
            entity.time = cursor.getLong(cursor.getColumnIndex("time"));
            entity.receive = "1".equals(cursor.getString(cursor.getColumnIndex("receive")));
            entity.money = cursor.getString(cursor.getColumnIndex("money"));
            entity.position = cursor.getInt(cursor.getColumnIndex("position"));
            entity.chatBg = cursor.getString(cursor.getColumnIndex("chatBg"));

            entity.alreadyRead = "1".equals(cursor.getString(cursor.getColumnIndex("alreadyRead")));

            /** ----------------- 群聊相关 ----------------- */
            byte[] in=cursor.getBlob(cursor.getColumnIndex("groupHeader"));
            if (null != in){
                entity.groupRoleCount = cursor.getInt(cursor.getColumnIndex("groupRoleCount"));
                entity.groupShowNickName = "1".equals(cursor.getString(cursor.getColumnIndex("groupShowNickName")));
                entity.groupName = cursor.getString(cursor.getColumnIndex("groupName"));
                entity.groupTableName = cursor.getString(cursor.getColumnIndex("groupTableName"));
                entity.groupHeader = BitmapFactory.decodeByteArray(in,0,in.length);
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<WechatUserEntity>>() {}.getType();
                ArrayList<WechatUserEntity> roles = gson.fromJson(cursor.getString(cursor.getColumnIndex("groupRoles")), type);
                entity.groupRoles = roles;
            }
            byte data[] = cursor.getBlob(cursor.getColumnIndex("groupRedPacketInfo"));
            if (null != data){
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                    entity.groupRedPacketInfo = (WechatScreenShotEntity) inputStream.readObject();
                    inputStream.close();
                    arrayInputStream.close();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            entity.chatType = cursor.getInt(cursor.getColumnIndex("chatType"));

            if ("3".equals(entity.msgType)){
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财，大吉大利";
                }
            }else if ("4".equals(entity.msgType)){
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财，大吉大利";
                }
            }
        }
        //关闭游标
        return entity;
    }
    /**
     * 删除指定id的数据
     * @param entity
     * @return
     */
    public int delete(WechatUserEntity entity){
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
    public int allCaseNum(String tableName){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT count(*) from " + tableName;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return (int) count;
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
}
