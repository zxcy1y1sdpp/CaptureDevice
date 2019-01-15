package app.jietuqi.cn.ui.entity;

import android.text.TextUtils;

import java.io.File;
import java.io.Serializable;

import app.jietuqi.cn.util.OtherUtil;
import app.jietuqi.cn.util.TimeUtil;

/**
 * 作者： liuyuanbo on 2018/10/10 15:11.
 * 时间： 2018/10/10 15:11
 * 邮箱： 972383753@qq.com
 * 用途： 微信用户表 -- 好友表
 */

public class WechatUserEntity implements Serializable {
    public WechatUserEntity() {
    }
    public WechatUserEntity(String wechatUserAvatar, int resAvatar, String wechatUserNickName, String msgType, String msg, long lastTime){
        this.wechatUserAvatar = wechatUserAvatar;
        this.resAvatar = resAvatar;
        this.wechatUserNickName = wechatUserNickName;
        if (TextUtils.isEmpty(msgType)){
            this.msgType = "0";
        }else {
            this.msgType = msgType;
        }

        this.msg = msg;
        if (lastTime <= 0){
            this.lastTime = TimeUtil.getCurrentTimeEndMs();
        }else {
            this.lastTime = lastTime;
        }
    }

    /**
     * 相册选择头像
     * @param wechatUserAvatar
     * @param wechatUserNickName
     */
    public WechatUserEntity(String wechatUserAvatar, String wechatUserNickName){
        this.wechatUserAvatar = wechatUserAvatar;
        this.wechatUserNickName = wechatUserNickName;
        this.msgType = "0";
        this.msg = "";
        this.lastTime = TimeUtil.getCurrentTimeEndMs();
    }

    /**
     * 随机本地头像
     * @param resAvatar
     * @param wechatUserNickName
     */
    public WechatUserEntity(int resAvatar, String wechatUserNickName){
        this.resAvatar = resAvatar;
        this.wechatUserNickName = wechatUserNickName;
        this.msgType = "0";
        this.msg = "";
        this.lastTime = TimeUtil.getCurrentTimeEndMs();
    }
    public int id;
    /**
     * 用户id
     */
    public String wechatUserId;
    /**
     * 随机产生的头像
     */
    public int resAvatar;
    /**
     * 用户头像
     */
    public String wechatUserAvatar;
    /**
     * 用户昵称
     */
    public String wechatUserNickName;

    /**
     * 聊天数据的类型
     * 0 -- 文字, 1 -- 图片, 2 -- 时间
     * 3 -- 发红包, 4 -- 领取红包, 5 -- 转账，收钱
     * 6 -- 转账被领取，7 -- 语音，8 -- 系统提示
     */
    public String msgType;
    /**
     * 最后一条聊天数据的文字描述
     * 0 -- 文字
     */
    public String msg;

    public long lastTime;

    /**
     * 本地头像文件
     */
    public File avatarFile;

    /*模拟器中要用到的*/
    public boolean isComMsg;
    public boolean alreadyRead;
    /**
     * 红包是否被领取过了
     */
    public boolean receive;
    /**
     * 红包的金额
     */
    public String money = "0.0";
    /**
     * 时间数据
     */
    public long time;
    public int position;


    public Object getAvatarFile() {
        if (!TextUtils.isEmpty(wechatUserAvatar)){
            return wechatUserAvatar;
        }else if (null != avatarFile){
            return avatarFile;
        }else {
            return resAvatar;
        }
    }

    /**
     * 昵称的拼音
     */
    public String pinyinNickName;
    /**
     * 昵称的首字母
     */
    public String firstChar;
    /**
     * 是不是在某一类中排第一个
     */
    public boolean isFirst;
    /**
     * 是不是在某一类中排最后一个
     */
    public boolean isLast;
    /**
     * 是不是“我”
     */
    public boolean meSelf;
    public WechatUserEntity(int avatarInt, String avatarStr, String nickName, String pinyinNickName) {
        this.resAvatar = avatarInt;
        this.wechatUserAvatar = avatarStr;
        this.wechatUserNickName = nickName;
        this.pinyinNickName = pinyinNickName;
        this.firstChar = OtherUtil.getFirstLetter(pinyinNickName);
    }
    public WechatUserEntity(String userId, int avatarInt, String avatarStr, String nickName, String pinyinNickName) {
        this.wechatUserId = userId;
        this.resAvatar = avatarInt;
        this.wechatUserAvatar = avatarStr;
        this.wechatUserNickName = nickName;
        this.pinyinNickName = pinyinNickName;
        this.firstChar = OtherUtil.getFirstLetter(pinyinNickName);
    }
}
