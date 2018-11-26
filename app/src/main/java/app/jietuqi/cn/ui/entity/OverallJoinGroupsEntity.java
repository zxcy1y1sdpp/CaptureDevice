package app.jietuqi.cn.ui.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/11/11 20:15.
 * 时间： 2018/11/11 20:15
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class OverallJoinGroupsEntity implements Serializable {

    public int id;
    /**
     * 标题
     */
    public String title;
    /**
     * 是否置顶
     */
    public boolean top;
    /**
     * 内容
     */
    public String content;

    public int titleColor;

}
