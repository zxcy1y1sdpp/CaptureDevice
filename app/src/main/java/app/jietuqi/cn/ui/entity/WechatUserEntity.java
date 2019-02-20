package app.jietuqi.cn.ui.entity;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import app.jietuqi.cn.ResourceHelper;
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity;
import app.jietuqi.cn.util.OtherUtil;
import app.jietuqi.cn.util.TimeUtil;

/**
 * 作者： liuyuanbo on 2018/10/10 15:11.
 * 时间： 2018/10/10 15:11
 * 邮箱： 972383753@qq.com
 * 用途： 微信用户表 -- 好友表
 */

public class WechatUserEntity implements Serializable {

    private static final long serialVersionUID = 8232355915974607667L;

    public WechatUserEntity() { }
    public WechatUserEntity(String wechatUserAvatar, int resAvatar, String wechatUserNickName, String msgType, String msg, long lastTime){
        this.wechatUserAvatar = wechatUserAvatar;
        this.avatarInt = resAvatar;
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
        this.avatarInt = resAvatar;
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
     * 微信号
     */
    public String wechatNumber;
    /**
     * 随机产生的头像
     */
    public int avatarInt;
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
    /**
     * 未读消息数量
     * 0 -- 没有未读
     */
    public String unReadNum = "0";

    public long lastTime;
    /**
     * 聊天背景
     */
    public String chatBg;

    /**
     * 本地头像文件
     */
    public File avatarFile;

    /*模拟器中要用到的*/
    public boolean isComMsg;
    public boolean alreadyRead;
    /**
     * 12小时制还是24小时制
     */
    public String timeType;
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
    /**
     * 是否置顶
     * 0 -- false
     * 1 -- true
     */
    public boolean top;
    /**
     * 是否显示小圆点
     * 0 -- false
     * 1 -- true
     */
    public boolean showPoint;
    public int position;
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
    /**
     * 收钱还是转出
     * 0 -- 收钱
     * 1 -- 转出
     */
    public int type;
    /**
     * 默认账户余额
     */
    public double cash;

    /**
     * 0 -- 单聊
     * 1 -- 群聊
     */
    public int chatType;
    /** ----------------------------------------- 群聊相关 -----------------------------------------*/
    /**
     * 群聊头像
     */
    public Bitmap groupHeader;
    /**
     * 群聊里面的人
     */
    public ArrayList<WechatUserEntity> groupRoles;
    /**
     * 最近的聊天对象
     */
    public WechatUserEntity recentRoles;
    /**
     * 群聊名称
     */
    public String groupName;

    /**
     * 群聊的表名
     */
    public String groupTableName;
    /**
     * 群聊人数
     */
    public int groupRoleCount;
    /**
     * 是否显示群成员昵称
     */
    public boolean groupShowNickName;
    /**
     * 是否是最近的聊天对象
     */
    public boolean isRecentRole;
    /**
     * 是否手气最佳
     */
    public boolean isBestLuck;
    /**
     * 红包的信息
     */
    public WechatScreenShotEntity groupRedPacketInfo;
    /**
     * 获取聊天列表展示的头像
     * @return
     */
    public Object getListAvatarFile() {
        if (0 == chatType){
            if (!TextUtils.isEmpty(wechatUserAvatar)){
                return wechatUserAvatar;
            }else if (null != avatarFile){
                return avatarFile;
            }else if (!TextUtils.isEmpty(resourceName)){
                return ResourceHelper.getAppIconId(resourceName);
            }else {
                return avatarInt;
            }
        }else {
            return groupHeader;
        }
    }

    /**
     * 用户头像
     * @return
     */
    public Object getAvatarFile() {
        if (!TextUtils.isEmpty(wechatUserAvatar)){
            return wechatUserAvatar;
        }else if (null != avatarFile){
            return avatarFile;
        }else if (!TextUtils.isEmpty(resourceName)){
            return ResourceHelper.getAppIconId(resourceName);
        }else {
            return avatarInt;
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

    /**
     * 在聊天列表中的主键id
     */
    public int listId;

    /**
     * 是否被选中
     * 在选择群聊对象的时候会用到
     */
    public boolean isChecked;


    /**
     * 资源对应的名称
     */
    public String resourceName;
    public WechatUserEntity(int avatarInt, String avatarStr, String nickName, String pinyinNickName) {
        this.avatarInt = avatarInt;
        this.wechatUserAvatar = avatarStr;
        this.wechatUserNickName = nickName;
        this.pinyinNickName = pinyinNickName;
        this.firstChar = OtherUtil.getFirstLetter(pinyinNickName);
    }
    public WechatUserEntity(String resourceName, String nickName, String pinyinNickName) {
        this.resourceName = resourceName;
        this.wechatUserNickName = nickName;
        this.pinyinNickName = pinyinNickName;
        this.firstChar = OtherUtil.getFirstLetter(pinyinNickName);
    }
    public WechatUserEntity(String userId, int avatarInt, String avatarStr, String nickName, String pinyinNickName) {
        this.wechatUserId = userId;
        this.avatarInt = avatarInt;
        this.wechatUserAvatar = avatarStr;
        this.wechatUserNickName = nickName;
        this.pinyinNickName = pinyinNickName;
        this.firstChar = OtherUtil.getFirstLetter(pinyinNickName);
    }
    public WechatUserEntity(String userId, String resourceName, String avatarStr, String nickName, String pinyinNickName) {
        this.wechatUserId = userId;
        this.resourceName = resourceName;
        this.wechatUserAvatar = avatarStr;
        this.wechatUserNickName = nickName;
        this.pinyinNickName = pinyinNickName;
        this.firstChar = OtherUtil.getFirstLetter(pinyinNickName);
    }
}
