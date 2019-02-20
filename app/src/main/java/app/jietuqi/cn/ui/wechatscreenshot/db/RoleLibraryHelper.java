package app.jietuqi.cn.ui.wechatscreenshot.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import app.jietuqi.cn.ui.entity.WechatUserEntity;
import app.jietuqi.cn.util.UserOperateUtil;
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper;

import static app.jietuqi.cn.constant.DatabaseConfig.DATABASE_NAME;
import static app.jietuqi.cn.constant.DatabaseConfig.DATABASE_VERSION;

/**
 * 作者： liuyuanbo on 2018/11/29 13:22.
 * 时间： 2018/11/29 13:22
 * 邮箱： 972383753@qq.com
 * 用途： 角色库的数据库操作
 */
public class RoleLibraryHelper extends SQLiteOpenHelper {
    private final static String TABLE_NAME = "role";

    public RoleLibraryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//        create();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ArrayList<String> tableNameList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while(cursor.moveToNext()){
            //遍历出表名
            String name = cursor.getString(0);
//            if (name.startsWith("wechatSimulator")){
            tableNameList.add(name);
//            }
        }
        //修改role表
        String sqlRole = "Alter table " + "role" + " add column " + "resourceName text";
        db.execSQL(sqlRole);
        String tableName;
        for (int i = 0; i < tableNameList.size(); i++) {
            tableName = tableNameList.get(i);
            if ("wechat_screen_shot_single".equals(tableName)) {
                String sqlWechatScreenShot = "Alter table " + "wechat_screen_shot_single" + " add column " + "resourceName text";
                db.execSQL(sqlWechatScreenShot);
                String sqlWechatScreenShot1 = "Alter table " + "wechat_screen_shot_single" + " add column " + "timeType text";
                db.execSQL(sqlWechatScreenShot1);
            } else if("qq_screen_shot_single".equals(tableName)) {
                String sqlWechatScreenShot = "Alter table " + "qq_screen_shot_single" + " add column " + "resourceName text";
                db.execSQL(sqlWechatScreenShot);
                String sqlWechatScreenShot1 = "Alter table " + "qq_screen_shot_single" + " add column " + "timeType text";
                db.execSQL(sqlWechatScreenShot1);
            }else if ("alipay_screen_shot_single".equals(tableName)) {
                String sqlWechatScreenShot = "Alter table " + "alipay_screen_shot_single" + " add column " + "resourceName text";
                db.execSQL(sqlWechatScreenShot);
                String sqlWechatScreenShot1 = "Alter table " + "alipay_screen_shot_single" + " add column " + "timeType text";
                db.execSQL(sqlWechatScreenShot1);
            }else if ("wechatSimulatorList".equals(tableName)){
                //修改wechatSimulatorList表
                String sqlWechatSimulatorList1 = "Alter table " + "wechatSimulatorList" + " add column " + "chatBg text";
                db.execSQL(sqlWechatSimulatorList1);
                String sqlWechatSimulatorList2 = "Alter table " + "wechatSimulatorList" + " add column " + "resourceName text";
                db.execSQL(sqlWechatSimulatorList2);
                String sqlWechatSimulatorList3 = "Alter table " + "wechatSimulatorList" + " add column " + "timeType text";
                db.execSQL(sqlWechatSimulatorList3);
                /**
                 * 增加的东西
                 * wechatSimulatorList -- "groupHeader BLOB, chatType text, groupRoles text, groupName text, groupTableName text, recentRoles text"
                 */
            }else if (tableName.contains("wechatSimulator")){
                //修改用户聊天的表
                String sql = "Alter table " + tableName + " add column " + "resourceName text";
                db.execSQL(sql);
                String sq1 = "Alter table " + tableName + " add column " + "timeType text";
                db.execSQL(sq1);
                /**
                 * 增加的东西
                 * wechatSimulator -- redPacketCount integer
                 * wechatSimulator -- receiveCompleteTime string
                 */
            }
        }

    }
    /**
     * 创建微信好友关系表
     */
    public void create() {
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(TABLE_NAME);
        builder.append(" (");
        builder.append("id Integer PRIMARY KEY AUTOINCREMENT,");
        builder.append("nickName text, avatarInt integer, avatarStr String, pinyinNickName String, resourceName text");
        builder.append(")");
        db.execSQL(builder.toString());
    }

    /**
     * 添加用户
     * @param entity
     * @return
     */
    public int save(WechatUserEntity entity){
        create();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
//        values.put("id", entity.userId);
        values.put("nickName", entity.wechatUserNickName);
        values.put("avatarStr", entity.wechatUserAvatar);
        values.put("avatarInt", entity.avatarInt);
        values.put("pinyinNickName", entity.pinyinNickName);
        values.put("resourceName", entity.resourceName);
        long l = db.insert(TABLE_NAME,null,values);//插入第一条数据
        Log.e("insert 截图", l+"");
        return (int) l;
    }

    public ArrayList<WechatUserEntity> query(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WechatUserEntity> list = new ArrayList<>();
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        WechatUserEntity entity;
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String userId = cursor.getString(cursor.getColumnIndex("id"));
            String nickName = cursor.getString(cursor.getColumnIndex("nickName"));
            String avatarStr = cursor.getString(cursor.getColumnIndex("avatarStr"));
            String pinyinNickName = cursor.getString(cursor.getColumnIndex("pinyinNickName"));
            String resourceName = cursor.getString(cursor.getColumnIndex("resourceName"));
            entity = new WechatUserEntity(userId, resourceName, avatarStr, nickName, pinyinNickName);
            list.add(entity);
        }
        //关闭游标
        cursor.close();
        return list;
    }

    /**
     * 查询剔除“自己”的用户集合
     * @return
     */
    public ArrayList<WechatUserEntity> queryWithoutMe(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WechatUserEntity> list = new ArrayList<>();
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        WechatUserEntity entity;
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String userId = cursor.getString(cursor.getColumnIndex("id"));
            if (userId.equals(UserOperateUtil.getWechatSimulatorMySelf().wechatUserId)){
                continue;
            }
            String nickName = cursor.getString(cursor.getColumnIndex("nickName"));
            String avatarStr = cursor.getString(cursor.getColumnIndex("avatarStr"));
            String pinyinNickName = cursor.getString(cursor.getColumnIndex("pinyinNickName"));
            String resourceName = cursor.getString(cursor.getColumnIndex("resourceName"));
            entity = new WechatUserEntity(userId, resourceName, avatarStr, nickName, pinyinNickName);
            list.add(entity);
        }
        //关闭游标
        cursor.close();
        return list;
    }

    /**
     * 随机获取两个数据
     * @return
     */
    public ArrayList<WechatUserEntity> queryRandom2Item(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WechatUserEntity> list = new ArrayList<>();
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
//        String stu_table="SELECT * FROM TABLE_NAME ORDER BY RANDOM() limit 2";
//        //执行SQL语句
//        db.execSQL(stu_table);
        WechatUserEntity entity;
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "RANDOM() limit 2");
        while (cursor.moveToNext()) {
            String userId = cursor.getString(cursor.getColumnIndex("id"));
            String nickName = cursor.getString(cursor.getColumnIndex("nickName"));
            String avatarStr = cursor.getString(cursor.getColumnIndex("avatarStr"));
            int avatarInt = cursor.getInt(cursor.getColumnIndex("avatarInt"));
            String pinyinNickName = cursor.getString(cursor.getColumnIndex("pinyinNickName"));
            String resourceName = cursor.getString(cursor.getColumnIndex("resourceName"));
            entity = new WechatUserEntity(userId, resourceName, avatarStr, nickName, pinyinNickName);
            list.add(entity);
        }
        //关闭游标
        cursor.close();
        return list;
    }

    /**
     * 随机查询出一个用户信息
     * @return
     */
    public WechatUserEntity queryRandom1Item(){
        SQLiteDatabase db = getReadableDatabase();
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
//        String stu_table="SELECT * FROM TABLE_NAME ORDER BY RANDOM() limit 2";
//        //执行SQL语句
//        db.execSQL(stu_table);
        WechatUserEntity entity = null;
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, "RANDOM() limit 1");
        while (cursor.moveToNext()) {
            String userId = cursor.getString(cursor.getColumnIndex("id"));
            String nickName = cursor.getString(cursor.getColumnIndex("nickName"));
            String avatarStr = cursor.getString(cursor.getColumnIndex("avatarStr"));
            int avatarInt = cursor.getInt(cursor.getColumnIndex("avatarInt"));
            String pinyinNickName = cursor.getString(cursor.getColumnIndex("pinyinNickName"));
            String resourceName = cursor.getString(cursor.getColumnIndex("resourceName"));
            entity = new WechatUserEntity(userId, resourceName, avatarStr, nickName, pinyinNickName);
        }
        try{
            if (UserOperateUtil.getMySelf().wechatUserId.equals(entity.wechatUserId)){
                queryRandom1Item();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //关闭游标
        cursor.close();
        return entity;
    }
    /**
     * 更新角色库的角色
     * @param entity
     * @return
     */
    public int update(Context context, WechatUserEntity entity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("avatarStr" , entity.wechatUserAvatar);
        cv.put("nickName" , entity.wechatUserNickName);
        cv.put("avatarInt", entity.avatarInt);
        cv.put("pinyinNickName", entity.pinyinNickName);
        cv.put("resourceName", entity.resourceName);
        int result = 0;
        if (db.isOpen()) {
            try {
                String[] where = new String[] {entity.wechatUserId};
                result = db.update(TABLE_NAME, cv, "id=?", where);
                Log.e("update ", "result : " + result);
            }catch (Exception e){
                Log.e("db", "Exception : " + e.getMessage());
            }
        }

        WechatSimulatorListHelper listHelper = new WechatSimulatorListHelper(context);
        WechatUserEntity entityInList  = listHelper.queryIfInThisTable(entity.wechatUserId);
        if (null != entityInList){
            entityInList.wechatUserNickName = entity.wechatUserNickName;
            entityInList.wechatUserAvatar = entity.wechatUserAvatar;
            entityInList.avatarInt = entity.avatarInt;
            entityInList.pinyinNickName = entity.pinyinNickName;
            entityInList.resourceName = entity.resourceName;
            listHelper.update(entityInList);
        }
        return  result;
    }

    /**
     * 删除指定id的数据
     * @param entity
     * @return
     */
    public int delete(WechatUserEntity entity){
        int result = 0;
        SQLiteDatabase db = getWritableDatabase();
        String[] where = new String[] {entity.wechatUserId};
        result = db.delete(TABLE_NAME,"id=?", where);
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
    /**
     * 判断表是否存在
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
