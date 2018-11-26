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

import app.jietuqi.cn.database.table.WechatSingleTalkEntity;
import app.jietuqi.cn.database.table.WechatUserTable;
import app.jietuqi.cn.util.FileUtil;

/**
 * Created by 刘远博 on 2017/2/15.
 */
public class MyOpenHelper extends SQLiteOpenHelper implements IOpenHelper {
    /**
     * 数据库的名称
     */
    public final static String DATABASE_NAME = "chat.db";
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
     * @param wechatTable
     */
    public int saveWechatMsg(Context context, WechatUserTable wechatTable){
        createTable();
        ArrayList<WechatUserTable> list = queryWechtLastMsgList(context);
        String userId;
        if (null == list || list.size() <= 0){
            userId = "1";
        }else {
            userId = String.valueOf(list.size() + 1);
        }
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
        values.put("wechatUserId", userId);
        if (wechatTable.resAvatar <= 0){
            values.put("wechatUserAvatar", wechatTable.wechatUserAvatar);
        }else {
            values.put("resAvatar", wechatTable.resAvatar);
        }
        values.put("wechatUserNickName", wechatTable.wechatUserNickName);
        values.put("msgType", wechatTable.msgType);
        values.put("msg",wechatTable.msg);
        values.put("lastTime",wechatTable.lastTime);
        long l = db.insert("wechatUser",null,values);//插入第一条数据
        return (int) l;
    }

    /**
     * 微信聊天更新列表页面
     * @param entity
     * @return
     */
    public int updateWechatUser(WechatUserTable entity) {
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
    public ArrayList<WechatUserTable> queryWechtLastMsgList(Context context){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WechatUserTable> list = new ArrayList<>();
        String tableName = "wechatUser";
//如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(tableName)) {
            return null;
        }
        WechatUserTable entity;
        Cursor cursor = db.query(tableName, null, null, null, null, null, "lastTime asc");
        while (cursor.moveToNext()) {
            String wechatUserId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            String wechatUserAvatar = cursor.getString(cursor.getColumnIndex("wechatUserAvatar"));
            int resAvatar = cursor.getInt(cursor.getColumnIndex("resAvatar"));
            String wechatUserNickName = cursor.getString(cursor.getColumnIndex("wechatUserNickName"));
            String msgType = cursor.getString(cursor.getColumnIndex("msgType"));
            String msg = cursor.getString(cursor.getColumnIndex("msg"));
            long lastTime = cursor.getLong(cursor.getColumnIndex("lastTime"));
            entity = new WechatUserTable(wechatUserAvatar, resAvatar, wechatUserNickName, msgType, msg, lastTime);
            if (resAvatar <= 0){
                String path = FileUtil.getFilePathByUri(context, Uri.parse(wechatUserAvatar));
                File avatarFile = new File(path);
                entity.avatar = avatarFile;
            }
            entity.wechatUserId = wechatUserId;
            list.add(entity);
        }
        //关闭游标
        cursor.close();
        return list;
    }
   /* *//**
     * 保存消息
     * @param wechatTable
     *//*
    public void saveWechatMsg(WechatTable wechatTable){
        createTable(wechatTable);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
        values.put("msgType", wechatTable.msgType);
        values.put("sendOrReceiveMsg", wechatTable.sendOrReceiveMsg);
        values.put("otherId", wechatTable.otherId);
        values.put("otherNickName", wechatTable.otherNickName);
        values.put("otherAvatar",wechatTable.otherAvatar);
        values.put("textType",wechatTable.textType);
        values.put("timeType",wechatTable.timeType);
        long l = db.insert("wechat" + wechatTable.otherId,null,values);//插入第一条数据
        Log.e("insert", l+"");
    }*/

    /**
     * 单聊表名的规则 -- wechat_single + 对象id
     * @param wechatUserId
     */
    public void createSingleTalkTable(String wechatUserId) {
        String tableName = "wechat_single" +wechatUserId;
        if (!isTableExists(tableName)){
            SQLiteDatabase db = getWritableDatabase();
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE IF NOT EXISTS ");
            builder.append(tableName);
            builder.append(" (");
            builder.append("id Integer PRIMARY KEY AUTOINCREMENT,");
            builder.append("wechatUserId text, msgType integer, msg text, img integer, time integer, receive text, money integer, isComMsg text, lastTime integer");
            builder.append(")");
            db.execSQL(builder.toString());
//            db.close();
        }
    }
    public int saveWechatSingleMsg(WechatSingleTalkEntity wechatSingleTalkEntity){
        String tableName = "wechat_single" + wechatSingleTalkEntity.wechatUserId;
        createSingleTalkTable(wechatSingleTalkEntity.wechatUserId);
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
        values.put("wechatUserId", wechatSingleTalkEntity.wechatUserId);
        values.put("msgType", wechatSingleTalkEntity.msgType);
        if (wechatSingleTalkEntity.msgType == 0){
            values.put("msg", wechatSingleTalkEntity.msg);
        }else if (wechatSingleTalkEntity.msgType == 1){
            values.put("img", wechatSingleTalkEntity.img);
            values.put("msg", "图片");
        }else if (wechatSingleTalkEntity.msgType == 2){
            values.put("time", wechatSingleTalkEntity.time);
        }else if (wechatSingleTalkEntity.msgType == 3){
            values.put("receive", wechatSingleTalkEntity.receive);
            values.put("money", wechatSingleTalkEntity.money);
            values.put("msg", wechatSingleTalkEntity.msg);
        }
        values.put("isComMsg", wechatSingleTalkEntity.isComMsg);
        values.put("lastTime",wechatSingleTalkEntity.lastTime);
        long l = db.insert(tableName,null,values);//插入第一条数据
        Log.e("insert wechat_single", l+"");
        return (int) l;
    }
    /**
     * 查询微信聊天与某人的单聊消息
     * @return
     */
    public ArrayList<WechatSingleTalkEntity> queryWechtSingleMsgList(String wechatUserId){
        String tableName = "wechat_single" + wechatUserId;
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WechatSingleTalkEntity> list = new ArrayList<>();
//如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(tableName)) {
            return null;
        }
        WechatSingleTalkEntity entity = null;
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id
            String userId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            int msgType = cursor.getInt(cursor.getColumnIndex("msgType"));
            boolean isComMsg = cursor.getString(cursor.getColumnIndex("isComMsg")).equals("1");
            long lastTime = cursor.getLong(cursor.getColumnIndex("lastTime"));
            if (msgType == 0){
                String msg = cursor.getString(cursor.getColumnIndex("msg"));
                entity = new WechatSingleTalkEntity(userId, msgType, isComMsg, msg, lastTime);
            }else if (msgType == 1){
                int img = cursor.getInt(cursor.getColumnIndex("img"));
                entity = new WechatSingleTalkEntity(userId, msgType, isComMsg, img, lastTime);
            }else if (msgType == 2){
                int time = cursor.getInt(cursor.getColumnIndex("time"));
                entity = new WechatSingleTalkEntity(userId, msgType, isComMsg, time, lastTime);
            }else if (msgType == 3 || msgType == 4){
                boolean receive = cursor.getString(cursor.getColumnIndex("receive")).equals("1");
                int money = cursor.getInt(cursor.getColumnIndex("money"));
                String msg = cursor.getString(cursor.getColumnIndex("msg"));
                entity = new WechatSingleTalkEntity(userId, msgType, isComMsg, receive, money, msg, lastTime);
            }else {

            }
            entity.id = id;
            list.add(entity);
        }
        //关闭游标
        cursor.close();
        return list;
    }
    public int updateWechatSingleMsg(WechatSingleTalkEntity entity) {
        String tableName = "wechat_single" + entity.wechatUserId;
        int msgType = entity.msgType;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("wechatUserId" , entity.wechatUserId);
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
     * 单聊数据
     * @return
     */
    /*public ArrayList<WechatTable> queryWechtSingleList(WechatUserTable userTable){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WechatTable> list = new ArrayList<>();
        String tableName = "wechatUser";
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(tableName)) {
            return null;
        }
        WechatTable entity = null;
        Cursor cursor = db.query("wechat" + userTable.wechatUserId, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String wechatUserId = cursor.getString(1);
            int wechatUserAvatar = cursor.getInt(2);
            String wechatUserNickName = cursor.getString(3);
            String msgType = cursor.getString(4);
            String msg = cursor.getString(5);
            int lastTime = cursor.getInt(6);
            entity = new WechatUserTable(wechatUserId, wechatUserAvatar, wechatUserNickName, msgType, msg, lastTime);
            list.add(entity);
        }
        //关闭游标
        cursor.close();
       return list;
    } */
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


//    public void queryWechatMsg(Object object, String userId, String userNickName){
////        如果该表不存在数据库中，则不需要进行操作
////        if (!isTableExists(table.getName())) {
////            return null;
////        }
//
//        SQLiteDatabase db = getReadableDatabase();
//        //获取表名，因为表名是采用完全包名的形式存储，按照表名规则，不允许有 "." 的存在,所以采用"_"进行替换
//        String tableName = table.getName().replaceAll("\\.", "_");
//        //通过表名查询所有的数据
//        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
//        //通过initList拿到对应的数据
//        List<T> result = initList(table, cursor);
//        //关闭游标
//        cursor.close();
//        db.close();
//        //返回结果
//        return result;
//    }
//    /**
//     * 保存对应的对象到数据库，该类的类名就是插入到数据库的表名
//     * @param obj 插入到数据库的对象
//     */
//    @Override
//    public void save(Object obj) {
//        //获取类类型
//        Class<?> table = obj.getClass();
//        //创建对应的表
//        createTableIfNotExists(table);
//        //具体实现保存数据方法
//        save(obj, table, getWritableDatabase());
//    }
//    @Override
//    public void save(Object obj, int id) {
//        //获取类类型
//        Class<?> table = obj.getClass();
//        //创建对应的表
//        createTableIfNotExists(table);
//        //具体实现保存数据方法
//        save(obj, table, getWritableDatabase());
//    }
//    /**
//     *  保存数据的主要操作
//     * @param obj 数据库对象
//     * @param table 对象类类型
//     * @param db 操作数据库
//     */
//    private void save(Object obj, Class<?> table, SQLiteDatabase db) {
//        //将一个对象中的所有字段添加到该数据集中
//        ContentValues contentValues = new ContentValues();
//        //通过反射获取一个类中的所有属性
//        Field[] declaredFields = table.getDeclaredFields();
//        //遍历所有的属性
//        for (Field field : declaredFields) {
//            //获取对应的修饰类型
//            int modifiers = field.getModifiers();
//            //如果不是静态的就插入到数据库
//            if (!Modifier.isStatic(modifiers)) {
//                //设置一下数据访问权限为最高级别，也就是public
//                field.setAccessible(true);
//                try {
//                    //将每一个字段的信息保存到数据集中
//                    contentValues.put(field.getName(), field.get(obj) + "");
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        //对于一般的数据操作，我们采用通常是insert来插入数据，但是为了防止同一个对象的数据进行刷新，所以采用直接替换掉
//        db.replace(table.getName().replaceAll("\\.", "_"), null, contentValues);
//        db.close();
//    }
//
//    /**
//     * 这里是保存统一对象的多个数据，通过获取集合中的对象，来保存所有的数据
//     * @param collection
//     */
//    @Override
//    public void saveAll(Collection collection) {
//        //如果集合为空直接不需要操作
//        if (collection.isEmpty()) {
//            return;
//        }
//        SQLiteDatabase db = getWritableDatabase();
//        //获取该集合的一个对象即可，因为一个集合中保存的都是同一个对象
//        Object next = collection.iterator().next();
//        //然后创建该对象所对应的表
//        createTableIfNotExists(next.getClass());
//        //这里为了提高效率采用了事务处理方式，对于事务这里不做过多的讲解
//        db.beginTransaction();
//        for (Object o : collection) {
//            save(o);
//        }
//        //设置事务为成功状态
//        db.setTransactionSuccessful();
//        //当事务结束，才会一次性执行上面for中的所有save方法，如果该事务没有结束，则for中的save方法一个都不会执行
//        db.endTransaction();
//        db.close();
//    }
//
//    /**
//     * 通过表名，查询所有的数据，表名对应于类名
//     * @param table 类类型
//     * @param <T> 泛型参数，任意类型
//     * @return
//     */
//    @Override
//    public <T> List<T> queryAll(Class<T> table) {
//        //如果该表不存在数据库中，则不需要进行操作
//        if (!isTableExists(table.getName())) {
//            return null;
//        }
//
//        SQLiteDatabase db = getReadableDatabase();
//        //获取表名，因为表名是采用完全包名的形式存储，按照表名规则，不允许有 "." 的存在,所以采用"_"进行替换
//        String tableName = table.getName().replaceAll("\\.", "_");
//        //通过表名查询所有的数据
//        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
//        //通过initList拿到对应的数据
//        List<T> result = initList(table, cursor);
//        //关闭游标
//        cursor.close();
//        db.close();
//        //返回结果
//        return result;
//    }
//
//    /**
//     * 通过指定的顺序返回所有查询的结果
//     * @param table 类类型
//     * @param orderBy 指定顺序
//     * @param <T> 泛型参数
//     * @return
//     */
//    @Override
//    public <T> List<T> queryAll(Class<T> table, String orderBy) {
//        //这里所有的操作和上面类似，就不依依介绍了
//        if (!isTableExists(table.getName())) {
//            return null;
//        }
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query(table.getName().replaceAll("\\.", "_"), null, null, null, null, null, orderBy);
//        List<T> result = initList(table, cursor);
//        //关闭游标
//        cursor.close();
//        db.close();
//        return result;
//    }
//
//    /**
//     * 通过指定的顺序和查询多少页来查询所有的数据
//     * @param table 类类型
//     * @param orderBy 指定顺序
//     * @param limit 指定的页数
//     * @param <T>
//     * @return
//     */
//    @Override
//    public <T> List<T> queryAll(Class<T> table, String orderBy, int limit) {
//        //如上雷同，不做介绍
//        if (!isTableExists(table.getName())) {
//            return null;
//        }
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query(table.getName().replaceAll("\\.", "_"), null, null, null, null, null, orderBy, String.valueOf(limit));
//        List<T> result = initList(table, cursor);
//        //关闭游标
//        cursor.close();
//        db.close();
//        return result;
//    }
//
//    /**
//     * 通过id来查询对应的数据
//     * @param table
//     * @param id
//     * @param <T>
//     * @return
//     */
//    @Override
//    public <T> T queryById(Class<T> table, Object id) {
//        if (!isTableExists(table.getName())) {
//            return null;
//        }
//        Field idField = getFieldId(table);
//        List<T> list = null;
//        try {
//            SQLiteDatabase db = getReadableDatabase();
//            Cursor cursor = db.query(table.getName().replaceAll("\\.", "_"), null,
//                    (idField == null ? "_id" : idField.getName()) + " = ?", //判断，如果对应的类中存在id，则通过该类中的id查找数据，如果不存在id就采用使用默认的_id来查询数据
//                    new String[]{String.valueOf(id)}, null, null, null);
//            list = initList(table, cursor);
//
//            cursor.close();
//            db.close();
//        }catch (IllegalStateException exception){
//            Log.e("MyOpenHelper", "数据库已关闭： "+exception.getMessage());
//        }
//        if (list.isEmpty()) {
//            return null;
//        } else {
//            return list.get(0);//这里是通过id查询数据，因为id唯一，所以查到的数据最多也就一个，直接返回list.get(0)
//        }
//    }
//
//    @Override
//    public <T> T queryByHxId(Class<T> table, String id) {
//        Field idField = null;
//        //获取属性id
//        idField = getFieldId(table);
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query(table.getName().replaceAll("\\.", "_"), null, (idField == null ? "hxId" : idField.getName()) + " = ?", //判断，如果对应的类中存在id，则通过该类中的id查找数据，如果不存在id就采用使用默认的_id来查询数据
//                new String[]{String.valueOf(id)}, null, null, null);
//        List<T> list = initList(table, cursor);
//        cursor.close();
//        db.close();
//        if (list.isEmpty()) {
//            return null;
//        } else {
//            return list.get(0);//这里是通过id查询数据，因为id唯一，所以查到的数据最多也就一个，直接返回list.get(0)
//        }
//    }
//
//    /**
//     * 通过表名清空所有的数据
//     * @param table 类类型
//     */
//    @Override
//    public void clear(Class table) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.delete(table.getName().replaceAll("\\.","_"), null, null);
//    }
//
//    /**
//     * 删除数据
//     * @param obj 指定对象（表）中的数据
//     */
//    @Override
//    public void delete(Object obj) {
//        SQLiteDatabase db = getWritableDatabase();
//        delete(obj, db);
//    }
//
//    /**
//     * 主要删除操作，主要是通过id来删除，因为删除一条操作必须有一个唯一列项
//     * @param obj 指定对象（表）中的数据
//     * @param db
//     */
//    private void delete(Object obj, SQLiteDatabase db){
//        //首先获取该类中的id，如果有就会获取到
//        Field idField = getFieldId(obj.getClass());
//        //如果不存在属性id，就不需要删除
//        if (idField != null) {
//            idField.setAccessible(true);
//            try {
//                db.delete(obj.getClass().getName().replaceAll("\\.", "_"),
//                        idField.getName() + " = ?",
//                        new String[]{idField.get(obj).toString()});
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }finally {
//                db.close();
//            }
//        }
//    }
//
//    /**
//     * 删除集合中所有的对象数据
//     * @param collection
//     */
//    @Override
//    public void deleteAll(Collection collection) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.beginTransaction();
//        for (Object o : collection) {
//            delete(o, db);
//        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
//    }
//
//   /* @Override
//    public <T> int updateUserInfo(Class<T> table, ChatWithOther entity, String whereClause, String[] whereArgs) {
//        String tableName = table.getName().replaceAll("\\.", "_");
//        SQLiteDatabase db =getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put("id" , entity.id);
//        cv.put("nickName", entity.nickName);
//        cv.put("avatar", entity.avatar);
//        int result = 0;
//        if (db.isOpen()) {
//            try {
//                result = db.update(tableName, cv, whereClause ,whereArgs);
//            }catch (Exception e){
//                Log.e("db", "Exception : " + e.getMessage());
//            }
//        }
//        db.close();
//        return  result;
//    }*/
//
//    /**
//     * 这个方法的主要功能是将数据中查询到的数据放到集合中。
//     * 类似于我们查询到对应的数据重新封装到一个对象中，然后把这个对象
//     * 放入集合中。这样就能拿到我们的数据集了
//     * @param table
//     * @param cursor
//     * @param <T>
//     * @return
//     */
//    private <T> List<T> initList(Class<T> table, Cursor cursor) {
//        List<T> result = new ArrayList<>();
//        //这里可能大家不了解，这是Gson为我们提供的一个通过JDK内部API 来创建对象实例，这里不做过多讲解
//        UnsafeAllocator allocator = UnsafeAllocator.create();
//        while (cursor.moveToNext()) {
//            try {
//                //创建具体的实例
//                T t = allocator.newInstance(table);
//                boolean flag = true;
//                //遍历所有的游标数据
//                for (int i = 0; i < cursor.getColumnCount(); i++) {
//
//                    //每次都去查找该类中有没有自带的id，如果没有，就不应该执行下面的语句
//                    //因为下面获取属性名时，有一个异常抛出，要是找不到属性就会结束这个for循环
//                    //后面的所有数据就拿不到了,只要检测到没有id，就不需要再检测了。
//                    if(flag){
//                        Field fieldId = getFieldId(table);
//                        if(fieldId == null){
//                            flag = !flag;
//                            continue;
//                        }
//                    }
//                    //通过列名获取对象中对应的属性名
//                    Field field = table.getDeclaredField(cursor.getColumnName(i));
//                    //获取属性的类型
//                    Class<?> type = field.getType();
//                    //设置属性的访问权限为最高权限，因为要设置对应的数据
//                    field.setAccessible(true);
//                    //获取到数据库中的值，由于sqlite是采用若语法，都可以使用getString来获取
//                    String value = cursor.getString(i);
//                    //通过判断类型，保存到指定类型的属性中，这里判断了我们常用的数据类型。
//                    if (type.equals(Byte.class) || type.equals(Byte.TYPE)) {
//                        field.set(t, Byte.parseByte(value));
//                    } else if (type.equals(Short.class) || type.equals(Short.TYPE)) {
//                        field.set(t, Short.parseShort(value));
//                    } else if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
//                        field.set(t, Integer.parseInt(value));
//                    } else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
//                        field.set(t, Long.parseLong(value));
//                    } else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
//                        field.set(t, Float.parseFloat(value));
//                    } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
//                        field.set(t, Double.parseDouble(value));
//                    } else if (type.equals(Character.class) || type.equals(Character.TYPE)) {
//                        field.set(t, value.charAt(0));
//                    } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
//                        field.set(t, Boolean.parseBoolean(value));
//                    } else if (type.equals(String.class)) {
//                        field.set(t, value);
//                    }
//                }
//                result.add(t);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        if (cursor != null){
//            cursor.close();
//            cursor = null;
//        }
//        return result;
//    }
//
//    /**
//     * 判断表格是否存在
//     * @param tableName
//     * @return
//     */
//    private boolean isTableExists(String tableName) {
//        boolean flag = false;
//        SQLiteDatabase db = getReadableDatabase();//获取一个可读的数据库对象
//        Cursor cursor = null;
//        try {
//            cursor = db.query("sqlite_master", null, "type = 'table' and name = ?", new String[]{tableName.replaceAll("\\.", "_")}, null, null, null);
//            while (cursor.moveToNext()) {
//                if (cursor.getCount() > 0) {
//                    flag = true;
//                }
//            }
//        } catch (Exception e) {
//            Log.e("cursor", "isExistTabValus  error");
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            db.close();//关闭数据库
//        }
//        return flag;
//    }
//
//    /**
//     * 如果表格不存在就创建该表。如果存在就不创建
//     * @param table
//     */
//    private void createTableIfNotExists(Class table) {
//        if (!isTableExists(table.getName())) {
//            SQLiteDatabase db = getWritableDatabase();
//            StringBuilder builder = new StringBuilder();
//            builder.append("CREATE TABLE IF NOT EXISTS ");
//            builder.append(table.getName().replaceAll("\\.", "_"));
//            builder.append(" (");
//            Field id = getFieldId(table);
//            if (id == null) {
//                builder.append("_id Integer PRIMARY KEY AUTOINCREMENT,");
//            } else {
//                builder.append(id.getName()).append("  PRIMARY KEY, ");
//            }
//            for (Field field : table.getDeclaredFields()) {
//                int modifiers = field.getModifiers();
//                if (!field.equals(id) && !Modifier.isStatic(modifiers)) {
//                    builder.append(field.getName()).append(",");
//                }
//            }
//            builder.deleteCharAt(builder.length() - 1);
//            builder.append(")");
//            db.execSQL(builder.toString());
//            db.close();
//        }
//    }
//
//    /**
//     * 获取对象属性中的id字段，如果有就获取，没有就不获取
//     * @param table
//     * @return
//     */
//    private Field getFieldId(Class table) {
//        Field fieldId = null;
//        try {
//            fieldId = table.getDeclaredField("id");
//            if (fieldId == null) {
//                table.getDeclaredField("_id");
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//        return fieldId;
//    }
//    /**
//     * 数据库操作的类
//     */
//    private static MyOpenHelper mMyOpenHelper;
//
//    /**
//     * 用户是否在数据表中存在
//     * @param hxId
//     * @return
//     */
//    /*public static boolean userExits(String hxId){
//        mMyOpenHelper = DatabaseUtils.getHelper();
//        ChatWithOther entity = mMyOpenHelper.queryById(ChatWithOther.class, hxId);
//        if (entity == null){
//            return false;
//        }
//        String id = entity.id;
//        if (id.equals(hxId)){
//            return true;
//        }else {
//            return false;
//        }
//    }*/
}