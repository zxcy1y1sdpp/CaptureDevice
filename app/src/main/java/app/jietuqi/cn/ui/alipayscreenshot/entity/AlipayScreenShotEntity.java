package app.jietuqi.cn.ui.alipayscreenshot.entity;

import android.text.TextUtils;

import app.jietuqi.cn.ui.entity.SingleTalkEntity;

/**
 * 作者： liuyuanbo on 2018/12/3 11:28.
 * 时间： 2018/12/3 11:28
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class AlipayScreenShotEntity extends SingleTalkEntity {
    public AlipayScreenShotEntity(){}
    /**
     * 文字消息
     * @param wechatUserId
     * @param msgType
     * @param msg
     * @param isComMsg
     * @param lastTime
     */
    public AlipayScreenShotEntity(String wechatUserId, int avatarInt, String avatarStr, int msgType, boolean isComMsg, String msg , long lastTime){
        this.wechatUserId = wechatUserId;
        this.msgType = msgType;
        this.isComMsg = isComMsg;
        this.avatarInt = avatarInt;
        this.avatarStr = avatarStr;
        this.msg = msg;
        if (lastTime <= 0){
            this.lastTime = System.currentTimeMillis();
        }else {
            this.lastTime = lastTime;
        }
    }
    /**
     * 图片消息
     * @param wechatUserId
     * @param msgType
     * @param img
     * @param isComMsg
     * @param lastTime
     */
    public AlipayScreenShotEntity(String wechatUserId, int avatarInt, String avatarStr, int msgType, boolean isComMsg, int img , long lastTime){
        this.wechatUserId = wechatUserId;
        this.avatarInt = avatarInt;
        this.avatarStr = avatarStr;
        this.msgType = msgType;
        this.isComMsg = isComMsg;
        this.img = img;
        if (lastTime <= 0){
            this.lastTime = System.currentTimeMillis();
        }else {
            this.lastTime = lastTime;
        }
    }
    public AlipayScreenShotEntity(String wechatUserId, int avatarInt, String avatarStr, int msgType, boolean isComMsg, long time , long lastTime){
        this.wechatUserId = wechatUserId;
        this.avatarInt = avatarInt;
        this.avatarStr = avatarStr;
        this.msgType = msgType;
        this.isComMsg = isComMsg;
        this.time = time;
        if (lastTime <= 0){
            this.lastTime = System.currentTimeMillis();
        }else {
            this.lastTime = lastTime;
        }
    }
    /**
     * 红包相关
     * @param wechatUserId
     * @param msgType
     * @param isComMsg
     * @param receive : 红包是否被领取了
     * @param money : 当前红包的金额
     * @param msg : 留言（如果没有备注默认 -- 恭喜发财，大吉大利）
     * @param lastTime
     */
    public AlipayScreenShotEntity(String wechatUserId, int avatarInt, String avatarStr, int msgType, boolean isComMsg, boolean receive, String money, String msg, long lastTime){
        this.wechatUserId = wechatUserId;
        this.avatarInt = avatarInt;
        this.avatarStr = avatarStr;
        this.msgType = msgType;
        this.isComMsg = isComMsg;
        this.receive = receive;
        this.money = money;
        if (!TextUtils.isEmpty(msg)){
            this.msg = msg;
        }else {
            this.msg = "恭喜发财，大吉大利";
        }
        if (lastTime <= 0){
            this.lastTime = System.currentTimeMillis();
        }else {
            this.lastTime = lastTime;
        }
    }
    public int avatarInt;
    public String avatarStr;

    public Object getAvatar(){
        if (!TextUtils.isEmpty(avatarStr)){
            return avatarStr;
        }else {
            return avatarInt;
        }
    }
}
