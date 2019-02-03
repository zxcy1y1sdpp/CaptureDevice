package app.jietuqi.cn.ui.wechatscreenshot.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/12/5 14:16.
 * 时间： 2018/12/5 14:16
 * 邮箱： 972383753@qq.com
 * 用途： 聊天页面的背景
 */
public class ChangeSingleTaklBgEntity implements Serializable {
    private static final long serialVersionUID = -8938601180720471041L;

    public ChangeSingleTaklBgEntity(boolean needBg, String bg){
        this.needBg = needBg;
        this.bg = bg;
    }
    /**
     * 是否需要背景
     */
    public boolean needBg;
    /**
     * 背景
     */
    public String bg;
}
