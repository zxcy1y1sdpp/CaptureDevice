package app.jietuqi.cn.ui.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/10/11 09:59.
 * 时间： 2018/10/11 09:59
 * 邮箱： 972383753@qq.com
 * 用途： 单聊
 */

public class SingleTalkEntity implements Serializable {
    public SingleTalkEntity(){}
    /**
     * 文字消息
     * @param wechatUserId
     * @param msgType
     * @param msg
     * @param isComMsg
     * @param lastTime
     */
    public SingleTalkEntity(String wechatUserId, int msgType, boolean isComMsg, String msg , long lastTime){
        this.wechatUserId = wechatUserId;
        this.msgType = msgType;
        this.isComMsg = isComMsg;
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
    public SingleTalkEntity(String wechatUserId, int msgType, boolean isComMsg, int img , long lastTime){
        this.wechatUserId = wechatUserId;
        this.msgType = msgType;
        this.isComMsg = isComMsg;
        this.img = img;
        if (lastTime <= 0){
            this.lastTime = System.currentTimeMillis();
        }else {
            this.lastTime = lastTime;
        }
    }
    public SingleTalkEntity(String wechatUserId, int msgType, boolean isComMsg, long time , long lastTime){
        this.wechatUserId = wechatUserId;
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
    public SingleTalkEntity(String wechatUserId, int msgType, boolean isComMsg, boolean receive, String money, String msg, long lastTime){
        this.wechatUserId = wechatUserId;
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

    /**
     * 主键id，通过该id可以定位到那一条消息
     * 每一条消息都有一个唯一的id
     */
    public int id;
    /**
     * 用户id
     */
    public String wechatUserId;
    /**
     * 头像
     */
    public String avatarStr;
    /**
     * 头像
     */
    public int avatarInt;
    /**
     * 聊天数据的类型
     * 0 -- 文字, 1 -- 图片, 2 -- 时间
     * 3 -- 发红包, 4 -- 领取红包, 5 -- 转账，收钱
     * 6 -- 转账被领取，7 -- 语音，8 -- 系统提示
     */
    public int msgType;
    /**
     * 聊天数据的文字描述
     */
    public String msg;
    /**
     * 图片文件
     */
    public int img;
    /**
     * 图片文件
     */
    public String filePath;
    /**
     * 时间数据
     */
    public long time;
    /**
     * 红包是否被领取过了
     */
    public boolean receive;
    /**
     * 红包的金额
     */
    public String money = "0.0";

    /**
     * 是否为收到消息
     * 0 -- false
     * 1 -- true
     */
    public boolean isComMsg;
    /**
     * 领取转账的id
     * 转账被领取（5）的时候需要通过这个id将领取转账（6）的状态也得改变
     */
    public int receiveTransferId;
    /**
     * 转账时间
     */
    public long transferOutTime;
    /**
     * 语音长度
     */
    public int voiceLength;
    /**
     * 是否已读
     * 0 -- 唯独
     * 1 -- 已读
     */
    public boolean alreadyRead;
    /**
     * 语音转文字
     * 如果不为空就是开启状态
     */
    public String voiceToText;
    /**
     * 收钱时间
     */
    public long transferReceiveTime;

    public long lastTime;


    public int position = -1;
    /*************************** 非数据库中的字段 *************************/
    /**
     * 用于区分eventbus的tag
     * 0 -- 新增
     * 1 -- 修改
     * 2 -- 删除
     * 3 -- 更新模拟器聊天列表
     */
    public int tag;

    public Object getAvatar(){
        if (!TextUtils.isEmpty(avatarStr)){
            return avatarStr;
        }else {
            return avatarInt;
        }
    }
}
