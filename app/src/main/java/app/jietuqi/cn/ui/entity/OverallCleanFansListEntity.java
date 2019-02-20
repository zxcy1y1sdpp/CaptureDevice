package app.jietuqi.cn.ui.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2019/2/19 16:54.
 * 时间： 2019/2/19 16:54
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class OverallCleanFansListEntity implements Serializable {
    private static final long serialVersionUID = -485556778539677201L;
    public int id;
    public String title;
    public long create_time;
    public String type;
    /**
     * 订单编号
     */
    public String sn;
    /**
     * 总价
     */
    public String money;
    public int number;
    /**
     * 单价
     */
    public String price;
    public int uid;
}
