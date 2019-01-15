package app.jietuqi.cn.ui.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2019/1/9 11:50.
 * 时间： 2019/1/9 11:50
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class HomeFunEntity implements Serializable {
    public HomeFunEntity(String funName, int funIcon, boolean needHot){
        this.funName = funName;
        this.funIcon = funIcon;
        this.needHot = needHot;
    }
    /**
     * 功能名称
     */
    public String funName;
    /**
     * 功能图标
     */
    public int funIcon;
    /**
     * 是否需要最火标志
     */
    public boolean needHot;
}
