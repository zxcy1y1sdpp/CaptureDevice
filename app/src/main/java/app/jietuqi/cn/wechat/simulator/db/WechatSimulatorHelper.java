package app.jietuqi.cn.wechat.simulator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhouyou.http.EventBusUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import app.jietuqi.cn.database.IOpenHelper;
import app.jietuqi.cn.database.MyOpenHelper;
import app.jietuqi.cn.ui.entity.WechatUserEntity;
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity;

/**
 * 作者： liuyuanbo on 2019/1/9 10:25.
 * 时间： 2019/1/9 10:25
 * 邮箱： 972383753@qq.com
 * 用途： 微信模拟器的数据操作
 */
public class WechatSimulatorHelper extends MyOpenHelper implements IOpenHelper {
    /**
     * 数据库的名称
     */
    public String TABLE_NAME = "wechatSimulator";
    public WechatSimulatorHelper(Context context, WechatUserEntity entity) {
        super(context);
        if (TABLE_NAME.length() <= 15){
            TABLE_NAME = TABLE_NAME + entity.wechatUserId;
        }
    }
    public WechatSimulatorHelper(Context context, String tabName) {
        super(context);
        TABLE_NAME = tabName;//通过时间戳进行命名数据表
    }
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
            builder.append("wechatUserId text, avatarInt integer, avatarStr text, msgType integer" +
                    ", msg text, img integer, filePath text, time integer, transferOutTime integer, " +
                    "transferReceiveTime integer, receiveTransferId integer, receive text, money text, " +
                    "voiceLength integer, alreadyRead text, voiceToText text, position integer, isComMsg text, " +
                    "lastTime integer, resourceName text, timeType text, " +

                    "redPacketCount integer, receiveCompleteTime text, joinReceiveRedPacket text, receiveRedPacketRoleList text, redPacketSenderNickName text, " +
                    "wechatUserNickName text, groupRedPacketInfo BLOB");
            builder.append(")");
            db.execSQL(builder.toString());
//            db.close();
        }
    }

    public int save(WechatScreenShotEntity wechatScreenShotEntity){
        wechatScreenShotEntity.tag = 0;
        create();
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //开始添加第一条数据
        values.put("wechatUserId", wechatScreenShotEntity.wechatUserId);
        values.put("avatarInt", wechatScreenShotEntity.avatarInt);
        values.put("avatarStr", wechatScreenShotEntity.avatarStr);
        values.put("resourceName", wechatScreenShotEntity.resourceName);
        values.put("timeType", wechatScreenShotEntity.timeType);
        values.put("msgType", wechatScreenShotEntity.msgType);
        values.put("wechatUserNickName", wechatScreenShotEntity.wechatUserNickName);
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
            values.put("receiveCompleteTime", wechatScreenShotEntity.receiveCompleteTime);
            values.put("redPacketCount", wechatScreenShotEntity.redPacketCount);
            values.put("joinReceiveRedPacket", wechatScreenShotEntity.joinReceiveRedPacket);
            values.put("wechatUserNickName", wechatScreenShotEntity.wechatUserNickName);
            values.put("redPacketSenderNickName", wechatScreenShotEntity.redPacketSenderNickName);

            if (null != wechatScreenShotEntity.receiveRedPacketRoleList){
                Gson gson = new Gson();
                String inputString= gson.toJson(wechatScreenShotEntity.receiveRedPacketRoleList);
                values.put("receiveRedPacketRoleList", inputString);
            }
        }else if (wechatScreenShotEntity.msgType == 4){
            values.put("receive", wechatScreenShotEntity.receive);
            values.put("money", wechatScreenShotEntity.money);
            values.put("receiveTransferId", wechatScreenShotEntity.receiveTransferId);
            values.put("redPacketSenderNickName", wechatScreenShotEntity.redPacketSenderNickName);

            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
                objectOutputStream.writeObject(wechatScreenShotEntity.groupRedPacketInfo);
                objectOutputStream.flush();
                byte data[] = arrayOutputStream.toByteArray();
                values.put("groupRedPacketInfo", data);
                objectOutputStream.close();
                arrayOutputStream.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }else if (wechatScreenShotEntity.msgType == 5){
            values.put("msg", wechatScreenShotEntity.msg);
            values.put("transferOutTime", wechatScreenShotEntity.transferOutTime);
            values.put("transferReceiveTime", wechatScreenShotEntity.transferReceiveTime);
            values.put("receive", wechatScreenShotEntity.receive);
            values.put("money", wechatScreenShotEntity.money);
        }else if (wechatScreenShotEntity.msgType == 6){
            values.put("receive", wechatScreenShotEntity.receive);
            values.put("money", wechatScreenShotEntity.money);
            values.put("transferOutTime", wechatScreenShotEntity.transferOutTime);
            values.put("transferReceiveTime", wechatScreenShotEntity.transferReceiveTime);
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
        Log.e("insert " , l+"");
        wechatScreenShotEntity.id = (int) l;
        if (wechatScreenShotEntity.needEventBus){
            EventBusUtil.post(wechatScreenShotEntity);
        }
        return (int) l;
    }
    public boolean saveAll(ArrayList<WechatScreenShotEntity> list){
        create();
        SQLiteDatabase db = getWritableDatabase();
        //开启事物
        db.beginTransaction();
        try{
            ContentValues values;
            WechatScreenShotEntity entity;
            for (int i = 0, size = list.size(); i < size; i++) {
                values = new ContentValues();
                entity = list.get(i);
                //开始添加第一条数据
                values.put("wechatUserId", entity.wechatUserId);
                values.put("avatarInt", entity.avatarInt);
                values.put("avatarStr", entity.avatarStr);
                values.put("resourceName", entity.resourceName);
                values.put("timeType", entity.timeType);
                values.put("msgType", entity.msgType);
                values.put("wechatUserNickName", entity.wechatUserNickName);
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
                    values.put("receiveCompleteTime", entity.receiveCompleteTime);
                    values.put("redPacketCount", entity.redPacketCount);
                    values.put("joinReceiveRedPacket", entity.joinReceiveRedPacket);
                    values.put("wechatUserNickName", entity.wechatUserNickName);
                    values.put("redPacketSenderNickName", entity.redPacketSenderNickName);

                    if (null != entity.receiveRedPacketRoleList){
                        Gson gson = new Gson();
                        String inputString= gson.toJson(entity.receiveRedPacketRoleList);
                        values.put("receiveRedPacketRoleList", inputString);
                    }
                }else if (entity.msgType == 4){
                    values.put("receive", entity.receive);
                    values.put("money", entity.money);
                    values.put("receiveTransferId", entity.receiveTransferId);
                    values.put("redPacketSenderNickName", entity.redPacketSenderNickName);

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

                }else if (entity.msgType == 5){
                    values.put("msg", entity.msg);
                    values.put("transferOutTime", entity.transferOutTime);
                    values.put("transferReceiveTime", entity.transferReceiveTime);
                    values.put("receive", entity.receive);
                    values.put("money", entity.money);
                }else if (entity.msgType == 6){
                    values.put("receive", entity.receive);
                    values.put("money", entity.money);
                    values.put("transferOutTime", entity.transferOutTime);
                    values.put("transferReceiveTime", entity.transferReceiveTime);
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
                entity.position = position;
                long l = db.insert(TABLE_NAME,null,values);//插入第一条数据
                Log.e("insert " , l+"");
                entity.id = (int) l;

            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            //事务已经执行成功
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        }
    }

    /**
     * 查询微信聊天与某人的单聊消息
     * @return
     */
    public ArrayList<WechatScreenShotEntity> queryAll(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<WechatScreenShotEntity> list = new ArrayList<>();
//如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            WechatScreenShotEntity entity = new WechatScreenShotEntity();
            int id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id
            entity.id = id;
            entity.wechatUserId = cursor.getString(cursor.getColumnIndex("wechatUserId"));
            entity.wechatUserNickName = cursor.getString(cursor.getColumnIndex("wechatUserNickName"));
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
                entity.redPacketSenderNickName = cursor.getString(cursor.getColumnIndex("redPacketSenderNickName"));
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财，大吉大利";
                }
                entity.redPacketCount = cursor.getInt(cursor.getColumnIndex("redPacketCount"));
                entity.receiveCompleteTime = cursor.getString(cursor.getColumnIndex("receiveCompleteTime"));
                entity.joinReceiveRedPacket = "1".equals(cursor.getString(cursor.getColumnIndex("joinReceiveRedPacket")));
                entity.wechatUserNickName = cursor.getString(cursor.getColumnIndex("wechatUserNickName"));

                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<WechatUserEntity>>() {}.getType();
                String role = cursor.getString(cursor.getColumnIndex("receiveRedPacketRoleList"));
                ArrayList<WechatUserEntity> roles = gson.fromJson(role, type);
                entity.receiveRedPacketRoleList = roles;
            }else if (entity.msgType == 4){
                entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
                entity.money = cursor.getString(cursor.getColumnIndex("money"));
                entity.redPacketSenderNickName = cursor.getString(cursor.getColumnIndex("redPacketSenderNickName"));
                if (TextUtils.isEmpty(entity.msg)){
                    entity.msg = "恭喜发财，大吉大利";
                }
                entity.receive = "1".equals(cursor.getString(cursor.getColumnIndex("receive")));
                entity.receiveTransferId = cursor.getInt(cursor.getColumnIndex("receiveTransferId"));


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

    /**
     * 随机获取两个数据
     * @return
     */
    public WechatScreenShotEntity queryLastMsg(){
        SQLiteDatabase db = getReadableDatabase();
        //如果该表不存在数据库中，则不需要进行操作
        if (!isTableExists(TABLE_NAME)) {
            return null;
        }
        WechatScreenShotEntity entity = null;
        Cursor cursor = db.query(TABLE_NAME,null,  null,  null, null, null, "id desc LIMIT 1");
        while (cursor.moveToNext()) {
            entity = new WechatScreenShotEntity();
            entity.id = cursor.getInt(cursor.getColumnIndex("id"));//相当于消息的唯一id;
            entity.wechatUserNickName = cursor.getString(cursor.getColumnIndex("wechatUserNickName"));
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
            entity.redPacketCount = cursor.getInt(cursor.getColumnIndex("redPacketCount"));
            entity.receiveCompleteTime = cursor.getString(cursor.getColumnIndex("receiveCompleteTime"));
            entity.joinReceiveRedPacket = "1".equals(cursor.getString(cursor.getColumnIndex("joinReceiveRedPacket")));
            entity.redPacketSenderNickName = cursor.getString(cursor.getColumnIndex("redPacketSenderNickName"));
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
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<WechatUserEntity>>() {}.getType();
            String role = cursor.getString(cursor.getColumnIndex("receiveRedPacketRoleList"));
            ArrayList<WechatUserEntity> roles = gson.fromJson(role, type);
            entity.receiveRedPacketRoleList = roles;
        }

        //关闭游标
        cursor.close();
        return entity;
    }
    public int update(WechatScreenShotEntity wechatScreenShotEntity, boolean needEventBus) {
        wechatScreenShotEntity.tag = 1;
        int msgType = wechatScreenShotEntity.msgType;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("wechatUserId" , wechatScreenShotEntity.wechatUserId);
        cv.put("wechatUserNickName" , wechatScreenShotEntity.wechatUserNickName);
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
            cv.put("redPacketCount" , wechatScreenShotEntity.redPacketCount);
            cv.put("receiveCompleteTime" , wechatScreenShotEntity.receiveCompleteTime);
            cv.put("joinReceiveRedPacket", wechatScreenShotEntity.joinReceiveRedPacket);
            cv.put("wechatUserNickName", wechatScreenShotEntity.wechatUserNickName);
            cv.put("redPacketSenderNickName", wechatScreenShotEntity.redPacketSenderNickName);


            if (null != wechatScreenShotEntity.receiveRedPacketRoleList){
                Gson gson = new Gson();
                String inputString= gson.toJson(wechatScreenShotEntity.receiveRedPacketRoleList);
                cv.put("receiveRedPacketRoleList", inputString);
            }
        }else if (msgType == 4){
            cv.put("receive" , wechatScreenShotEntity.receive);
            cv.put("redPacketSenderNickName" , wechatScreenShotEntity.redPacketSenderNickName);
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
                objectOutputStream.writeObject(wechatScreenShotEntity.groupRedPacketInfo);
                objectOutputStream.flush();
                byte data[] = arrayOutputStream.toByteArray();
                cv.put("groupRedPacketInfo", data);
                objectOutputStream.close();
                arrayOutputStream.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else if (msgType == 5){
            cv.put("transferOutTime" , wechatScreenShotEntity.transferOutTime);
            cv.put("transferReceiveTime" , wechatScreenShotEntity.transferReceiveTime);
//            cv.put("receive" , wechatScreenShotEntity.receive);
            if (wechatScreenShotEntity.receive){
                cv.put("receive" , 1);
                cv.put("img", 1);
            }else {
                cv.put("receive" , 0);
                cv.put("img", 0);
            }
            cv.put("money" , wechatScreenShotEntity.money);
            cv.put("msg" , wechatScreenShotEntity.msg);
        }else if (msgType == 6){
            cv.put("transferOutTime" , wechatScreenShotEntity.transferOutTime);
            cv.put("transferReceiveTime" , wechatScreenShotEntity.transferReceiveTime);
            if (wechatScreenShotEntity.receive){
                cv.put("receive" , 1);
            }else {
                cv.put("receive" , 0);
            }
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
        if (needEventBus){
            if (wechatScreenShotEntity.msgType != 8){
                EventBusUtil.post(wechatScreenShotEntity);
            }
        }

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
            entity.wechatUserNickName = cursor.getString(cursor.getColumnIndex("wechatUserNickName"));
            entity.avatarInt = cursor.getInt(cursor.getColumnIndex("avatarInt"));
            entity.avatarStr = cursor.getString(cursor.getColumnIndex("avatarStr"));
            entity.resourceName = cursor.getString(cursor.getColumnIndex("resourceName"));
            entity.timeType = cursor.getString(cursor.getColumnIndex("timeType"));
            entity.msgType = cursor.getInt(cursor.getColumnIndex("msgType"));
            entity.lastTime = cursor.getLong(cursor.getColumnIndex("lastTime"));
            entity.msg = cursor.getString(cursor.getColumnIndex("msg"));
            entity.filePath = cursor.getString(cursor.getColumnIndex("filePath"));
            entity.img = cursor.getInt(cursor.getColumnIndex("img"));
            entity.time = cursor.getLong(cursor.getColumnIndex("time"));
            entity.receive = "1".equals(cursor.getString(cursor.getColumnIndex("receive")));
            entity.money = cursor.getString(cursor.getColumnIndex("money"));
            entity.position = cursor.getInt(cursor.getColumnIndex("position"));
            entity.redPacketCount = cursor.getInt(cursor.getColumnIndex("redPacketCount"));
            entity.receiveCompleteTime = cursor.getString(cursor.getColumnIndex("receiveCompleteTime"));
            entity.redPacketSenderNickName = cursor.getString(cursor.getColumnIndex("redPacketSenderNickName"));
            entity.joinReceiveRedPacket = "1".equals(cursor.getString(cursor.getColumnIndex("joinReceiveRedPacket")));

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
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<WechatUserEntity>>() {}.getType();
            String role = cursor.getString(cursor.getColumnIndex("receiveRedPacketRoleList"));
            ArrayList<WechatUserEntity> roles = gson.fromJson(role, type);
            entity.receiveRedPacketRoleList = roles;
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
