package app.jietuqi.cn.database.table;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/10/11 09:59.
 * 时间： 2018/10/11 09:59
 * 邮箱： 972383753@qq.com
 * 用途： 微信单聊
 */

public class WechatSingleTalkEntity implements Serializable {
    public WechatSingleTalkEntity(){}
    /**
     * 文字消息
     * @param wechatUserId
     * @param msgType
     * @param msg
     * @param isComMsg
     * @param lastTime
     */
    public WechatSingleTalkEntity(String wechatUserId, int msgType, boolean isComMsg, String msg , long lastTime){
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
    public WechatSingleTalkEntity(String wechatUserId, int msgType, boolean isComMsg, int img , long lastTime){
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
    public WechatSingleTalkEntity(String wechatUserId, int msgType, boolean isComMsg, long time , long lastTime){
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
    public WechatSingleTalkEntity(String wechatUserId, int msgType, boolean isComMsg, boolean receive, int money, String msg, long lastTime){
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
     * 聊天数据的类型
     * 0 -- 文字, 1 -- 图片, 2 -- 时间
     * 3 -- 红包
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
    public int money;

    /**
     * 是否为收到消息
     * 0 -- false
     * 1 -- true
     */
    public boolean isComMsg;

    public long lastTime;

    /*************************** 非数据库中的字段 *************************/
    public int position;
}
