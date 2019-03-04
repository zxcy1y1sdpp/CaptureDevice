package app.jietuqi.cn.ui.wechatscreenshot.entity;

import android.text.TextUtils;

import java.util.ArrayList;

import app.jietuqi.cn.ResourceHelper;
import app.jietuqi.cn.ui.entity.SingleTalkEntity;
import app.jietuqi.cn.ui.entity.WechatUserEntity;

/**
 * 作者： liuyuanbo on 2018/12/3 11:28.
 * 时间： 2018/12/3 11:28
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class WechatScreenShotEntity extends SingleTalkEntity {
    private static final long serialVersionUID = 8377233446853070151L;

    public WechatScreenShotEntity(){}
    public WechatScreenShotEntity(String wechatUserId, int avatarInt, String avatarStr, int msgType, boolean isComMsg, long time , long lastTime){
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
//    public int avatarInt;
//    public String avatarStr;
    /**
     * 转账状态
     * 已收钱
     * 代收款
     * 已退款
     */
    public String transferType;
    /**
     * 转出时间
     */
    public String outTime;
    /**
     * 收钱时间
     */
    public String receiveTime;
    public String wechatUserNickName;
    /**
     * 收钱还是转出
     * 0 -- 收钱
     * 1 -- 转出
     */
    public int type;

    /**
     * 是否需要eventbus发送消息
     */
    public boolean needEventBus = true;
    /**
     * 群红包个数
     */
    public int redPacketCount;
    /**
     * 红包领取完成时间
     */
    public String receiveCompleteTime;
    /**
     * “我”是否参与领红包
     */
    public boolean joinReceiveRedPacket = true;
    /**
     * 红包发送人的昵称
     */
    public String redPacketSenderNickName;

    /**
     * 是不是最后一个领红包的人
     */
    public boolean lastReceive;
    /**
     * 红包的信息
     */
    public WechatScreenShotEntity groupRedPacketInfo;

    /**
     * 每一条红包信息对应的领取人的信息集合
     */
    public ArrayList<WechatUserEntity> receiveRedPacketRoleList;
    public Object getAvatar(){
        if (!TextUtils.isEmpty(avatarStr)){
            return avatarStr;
        }else if (!TextUtils.isEmpty(resourceName)){
            return ResourceHelper.getAppIconId(resourceName);
        }else {
            return avatarInt;
        }
    }
}
