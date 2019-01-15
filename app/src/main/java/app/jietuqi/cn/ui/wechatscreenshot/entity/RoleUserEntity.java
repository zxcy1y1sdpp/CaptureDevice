package app.jietuqi.cn.ui.wechatscreenshot.entity;

import android.text.TextUtils;

import java.io.File;
import java.io.Serializable;

import app.jietuqi.cn.util.OtherUtil;

/**
 * 作者： liuyuanbo on 2018/11/29 13:19.
 * 时间： 2018/11/29 13:19
 * 邮箱： 972383753@qq.com
 * 用途： 微信单聊用到的实体
 */
public class RoleUserEntity implements Serializable {
    public RoleUserEntity(int avatarInt, String avatarStr, String nickName, String pinyinNickName) {
        this.avatarStr = avatarStr;
        this.avatarInt = avatarInt;
        this.nickName = nickName;
        this.pinyinNickName = pinyinNickName;
        this.firstChar = OtherUtil.getFirstLetter(pinyinNickName);
    }
    public RoleUserEntity(int userId, int avatarInt, String avatarStr, String nickName, String pinyinNickName) {
        this.userId = userId;
        this.avatarInt = avatarInt;
        this.avatarStr = avatarStr;
        this.nickName = nickName;
        this.pinyinNickName = pinyinNickName;
        this.firstChar = OtherUtil.getFirstLetter(pinyinNickName);
    }

    public int userId;
    public String avatarStr;
    public File avatarFile;
    public int avatarInt;
    public String nickName;
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

    public Object getAvatar(){
        if (!TextUtils.isEmpty(avatarStr)){
            return avatarStr;
        }else if (null != avatarFile){
            return avatarFile;
        }else {
            return avatarInt;
        }
    }
}
