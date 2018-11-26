package app.jietuqi.cn.database.table;

import android.text.TextUtils;

import java.io.File;
import java.io.Serializable;

import app.jietuqi.cn.util.TimeUtil;

/**
 * 作者： liuyuanbo on 2018/10/10 15:11.
 * 时间： 2018/10/10 15:11
 * 邮箱： 972383753@qq.com
 * 用途： 微信用户表 -- 好友表
 */

public class WechatUserTable implements Serializable {
    public WechatUserTable() {
    }

    public WechatUserTable(String wechatUserAvatar, int resAvatar, String wechatUserNickName, String msgType, String msg, long lastTime){
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
    public WechatUserTable(String wechatUserAvatar, String wechatUserNickName){
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
    public WechatUserTable(int resAvatar, String wechatUserNickName){
        this.resAvatar = resAvatar;
        this.wechatUserNickName = wechatUserNickName;
        this.msgType = "0";
        this.msg = "";
        this.lastTime = TimeUtil.getCurrentTimeEndMs();
    }
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
     * 3 -- 红包
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
    public File avatar;
}
