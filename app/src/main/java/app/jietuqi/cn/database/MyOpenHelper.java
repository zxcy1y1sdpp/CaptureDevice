package app.jietuqi.cn.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

import app.jietuqi.cn.ui.entity.SingleTalkEntity;
import app.jietuqi.cn.ui.entity.WechatUserEntity;
import app.jietuqi.cn.util.FileUtil;

/**
 * Created by 刘远博 on 2017/2/15.
 */
public class MyOpenHelper extends SQLiteOpenHelper implements IOpenHelper {
    /**
     * 数据库的名称
     */
    private final static String DATABASE_NAME = "chat.db";
    /**
     * 数据库版本号
     */
    private static final int DATABASE_VERSION = 2;

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 创建微信好友关系表
     */
    public void createTable() {
        if (!isTableExists("wechatUser")){
            SQLiteDatabase db = getWritableDatabase();
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ");
            builder.append("wechatUser");
            builder.append(" (");
            builder.append("id Integer PRIMARY KEY AUTOINCREMENT,");
            builder.append("wechatUserId text, wechatUserAvatar text, resAvatar integer, wechatUserNickName text, msgType text, msg text, lastTime integer");
            builder.append(")");
            db.execSQL(builder.toString());
        }
    }

    /**
     * 添加聊天列表
     * @param entity
     */
    public int saveWechatMsg(WechatUserEntity entity){
        createTable();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
        values.put("wechatUserId", entity.wechatUserId);
        if (entity.resAvatar <= 0){
            values.put("wechatUserAvatar", entity.wechatUserAvatar);
        }else {
            values.put("resAvatar", entity.resAvatar);
        }
        values.put("wechatUserNickName", entity.wechatUserNickName);
        values.put("msgType", entity.msgType);
        values.put("msg",entity.msg);
        values.put("lastTime",entity.lastTime);

        long l = db.insert("wechatUser",null,values);//插入第一条数据
        return (int) l;
    }

    /**
     * 微信聊天更新列表页面
     * @param entity
     * @return
     */
    public int updateWechatUser(WechatUserEntity entity) {
        String tableName = "wechatUser";
        String msgType = entity.msgType;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("wechatUserId" , entity.wechatUserId);
        cv.put("wechatUserNickName" , entity.wechatUserNickName);
        cv.put("msgType", entity.msgType);
        cv.put("lastTime", entity.lastTime);
        cv.put("msg" , entity.msg);
        cv.put("wechatUserAvatar" , entity.wechatUserAvatar);
        cv.put("resAvatar" , entity.resAvatar);
        int result = 0;
        if (db.isOpen()) {
            try {
                String[] where = new String[] {String.valueOf(entity.wechatUserId)};
                result = db.update(tableName, cv, "wechatUserId=?", where);
                Log.e("update ", "result : " + result);
            }catch (Exception e){
                Log.e("db", "Exception : " + e.getMessage());
            }
        }
//        db.close();
        return  result;
    }
    /**
     * 查询微信聊天列表页面的数据集合
     * @return
     */
    public ArrayList<WechatUserEntity> queryWechatLastMsgList(Context context){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WechatUserEntity> list = new ArrayList<>();
        String tableName = "wechatUser";
//如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(tableName)) {
            return null;
        }
        WechatUserEntity entity;
        Cursor cursor = db.query(tableName, null, null, null, null, null, "lastTime asc");
        while (cursor.moveToNext()) {
            String wechatUserId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            String wechatUserAvatar = cursor.getString(cursor.getColumnIndex("wechatUserAvatar"));
            int resAvatar = cursor.getInt(cursor.getColumnIndex("resAvatar"));
            String wechatUserNickName = cursor.getString(cursor.getColumnIndex("wechatUserNickName"));
            String msgType = cursor.getString(cursor.getColumnIndex("msgType"));
            String msg = cursor.getString(cursor.getColumnIndex("msg"));
            long lastTime = cursor.getLong(cursor.getColumnIndex("lastTime"));
            entity = new WechatUserEntity(wechatUserAvatar, resAvatar, wechatUserNickName, msgType, msg, lastTime);
            if (resAvatar <= 0){
                String path = FileUtil.getFilePathByUri(context, Uri.parse(wechatUserAvatar));
                File avatarFile = new File(path);
                entity.avatarFile = avatarFile;
            }
            entity.wechatUserId = wechatUserId;
            list.add(entity);
        }
        //关闭游标
        cursor.close();
        return list;
    }


    /**
     * 单聊表名的规则 -- wechat_single + 对象id
     * @param wechatUserId
     */
    public void createSingleTalkTable(String wechatUserId) {
        String tableName = "wechat_single" + wechatUserId;
        if (!isTableExists(tableName)){
            SQLiteDatabase db = getWritableDatabase();
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ");
            builder.append(tableName);
            builder.append(" (");
            builder.append("id Integer PRIMARY KEY AUTOINCREMENT,");
            builder.append("wechatUserId text, msgType integer, msg text, img integer, time integer, receive text, money text, isComMsg text, position integer, lastTime integer");
            builder.append(")");
            db.execSQL(builder.toString());
//            db.close();
        }
    }
    public int saveWechatSingleMsg(SingleTalkEntity singleTalkEntity){
        String tableName = "wechat_single" + singleTalkEntity.wechatUserId;
        createSingleTalkTable(singleTalkEntity.wechatUserId);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
        values.put("wechatUserId", singleTalkEntity.wechatUserId);
        values.put("msgType", singleTalkEntity.msgType);
        if (singleTalkEntity.msgType == 0){
            values.put("msg", singleTalkEntity.msg);
        }else if (singleTalkEntity.msgType == 1){
            values.put("img", singleTalkEntity.img);
            values.put("msg", "图片");
        }else if (singleTalkEntity.msgType == 2){
            values.put("time", singleTalkEntity.time);
        }else if (singleTalkEntity.msgType == 3){
            values.put("receive", singleTalkEntity.receive);
            values.put("money", singleTalkEntity.money);
            values.put("msg", singleTalkEntity.msg);
        }
        values.put("isComMsg", singleTalkEntity.isComMsg);
        values.put("lastTime", singleTalkEntity.lastTime);
        if (allCaseNum(tableName) == 0){
            values.put("position", 0);
        }else {
            values.put("position", allCaseNum(tableName) - 1);
        }
        long l = db.insert(tableName,null,values);//插入第一条数据
        Log.e("insert wechat_single", l+"");
        return (int) l;
    }
    /**
     * 查询微信聊天与某人的单聊消息
     * @return
     */
    public ArrayList<SingleTalkEntity> queryWechtSingleMsgList(String wechatUserId){
        String tableName = "wechat_single" + wechatUserId;
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<SingleTalkEntity> list = new ArrayList<>();
//如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(tableName)) {
            return null;
        }
        SingleTalkEntity entity;
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id
            String userId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            int msgType = cursor.getInt(cursor.getColumnIndex("msgType"));
            boolean isComMsg = "1".equals(cursor.getString(cursor.getColumnIndex("isComMsg")));
            long lastTime = cursor.getLong(cursor.getColumnIndex("lastTime"));
            entity = new SingleTalkEntity();
            entity.id = id;
            entity.wechatUserId = userId;
            entity.msgType = msgType;
            entity.isComMsg = isComMsg;
            entity.lastTime = lastTime;
            entity.position = cursor.getInt(cursor.getColumnIndex("position"));
            if (msgType == 0){
                String msg = cursor.getString(cursor.getColumnIndex("msg"));
                entity = new SingleTalkEntity(userId, msgType, isComMsg, msg, lastTime);
            }else if (msgType == 1){
                int img = cursor.getInt(cursor.getColumnIndex("img"));
                entity.img = img;
            }else if (msgType == 2){
                int time = cursor.getInt(cursor.getColumnIndex("time"));
                entity = new SingleTalkEntity(userId, msgType, isComMsg, time, lastTime);
            }else if (msgType == 3 || msgType == 4){
                boolean receive = "1".equals(cursor.getString(cursor.getColumnIndex("receive")));
                String money = cursor.getString(cursor.getColumnIndex("money"));
                String msg = cursor.getString(cursor.getColumnIndex("msg"));
                entity = new SingleTalkEntity(userId, msgType, isComMsg, receive, money, msg, lastTime);
            }else {

            }

            list.add(entity);
        }
        //关闭游标
        cursor.close();
        return list;
    }
    public int updateWechatSingleMsg(SingleTalkEntity entity) {
        String tableName = "wechat_single" + entity.wechatUserId;
        int msgType = entity.msgType;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("wechatUserId" , entity.wechatUserId);
        cv.put("position" , entity.position);
        cv.put("msgType", entity.msgType);
        cv.put("isComMsg", entity.isComMsg);
        cv.put("lastTime", entity.lastTime);
        if (msgType == 0){
            cv.put("msg" , entity.msg);
        }else if (msgType == 1){
            cv.put("img" , entity.img);
        }else if (msgType == 2){
            cv.put("time" , entity.time);
        }else if (msgType == 3){
            cv.put("money" , entity.money);
            cv.put("receive" , entity.receive);
            cv.put("msg" , entity.msg);
        }else {
        }
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