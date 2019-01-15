package app.jietuqi.cn.ui.wechatscreenshot.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import app.jietuqi.cn.database.MyOpenHelper;
import app.jietuqi.cn.ui.entity.WechatUserEntity;
import app.jietuqi.cn.util.UserOperateUtil;

/**
 * 作者： liuyuanbo on 2018/11/29 13:22.
 * 时间： 2018/11/29 13:22
 * 邮箱： 972383753@qq.com
 * 用途： 角色库的数据库操作
 */
public class RoleLibraryHelper extends MyOpenHelper {
    private final static String TABLE_NAME = "role";

    public RoleLibraryHelper(Context context) {
        super(context);
    }

    /**
     * 创建微信好友关系表
     */
    public void createRoleTable() {
        if (!isTableExists(TABLE_NAME)){
            SQLiteDatabase db = getWritableDatabase();
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ");
            builder.append(TABLE_NAME);
            builder.append(" (");
            builder.append("id Integer PRIMARY KEY AUTOINCREMENT,");
            builder.append("nickName text, avatarInt integer, avatarStr String, pinyinNickName String");
            builder.append(")");
            db.execSQL(builder.toString());
        }
    }

    /**
     * 添加用户
     * @param entity
     * @return
     */
    public int save(WechatUserEntity entity){
        createRoleTable();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
//        values.put("id", entity.userId);
        values.put("nickName", entity.wechatUserNickName);
        values.put("avatarStr", entity.wechatUserAvatar);
        values.put("avatarInt", entity.resAvatar);
        values.put("pinyinNickName", entity.pinyinNickName);
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
            int avatarInt = cursor.getInt(cursor.getColumnIndex("avatarInt"));
            String pinyinNickName = cursor.getString(cursor.getColumnIndex("pinyinNickName"));
            entity = new WechatUserEntity(userId, avatarInt, avatarStr, nickName, pinyinNickName);
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
            entity = new WechatUserEntity(userId, avatarInt, avatarStr, nickName, pinyinNickName);
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
            entity = new WechatUserEntity(userId, avatarInt, avatarStr, nickName, pinyinNickName);
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
    public int update(WechatUserEntity entity) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("avatarStr" , entity.wechatUserAvatar);
        cv.put("nickName" , entity.wechatUserNickName);
        cv.put("avatarInt", entity.resAvatar);
        cv.put("pinyinNickName", entity.pinyinNickName);
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
}
