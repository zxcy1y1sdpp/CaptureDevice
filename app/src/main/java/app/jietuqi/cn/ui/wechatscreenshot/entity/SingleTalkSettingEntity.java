package app.jietuqi.cn.ui.wechatscreenshot.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/12/5 14:16.
 * 时间： 2018/12/5 14:16
 * 邮箱： 972383753@qq.com
 * 用途： 聊天页面的背景
 */
public class SingleTalkSettingEntity implements Serializable {
    private static final long serialVersionUID = -8938601180720471041L;

    public SingleTalkSettingEntity(boolean needBg, String bg, boolean messageFree, boolean earMode){
        this.needBg = needBg;
        this.bg = bg;
        this.messageFree = messageFree;
        this.earMode = earMode;
    }
    public SingleTalkSettingEntity(boolean needBg, String bg, boolean messageFree, boolean earMode, boolean showLqt){
        this.needBg = needBg;
        this.bg = bg;
        this.messageFree = messageFree;
        this.earMode = earMode;
        this.showLqt = showLqt;
    }
    public SingleTalkSettingEntity(){ }
    /**
     * 是否需要背景
     */
    public boolean needBg;
    /**
     * 背景
     */
    public String bg;
    /**
     * 消息免打扰
     */
    public boolean messageFree;
    /**
     * 听筒模式
     */
    public boolean earMode;
    /**
     * 转账的时候是否开启零钱通
     */
    public boolean showLqt;
}
