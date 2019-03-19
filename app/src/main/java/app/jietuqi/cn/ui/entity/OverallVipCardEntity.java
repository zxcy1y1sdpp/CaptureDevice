package app.jietuqi.cn.ui.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/11/16 13:56.
 * 时间： 2018/11/16 13:56
 * 邮箱： 972383753@qq.com
 * 用途： 会员卡的相关信息
 */
public class OverallVipCardEntity implements Serializable {
    private static final long serialVersionUID = 5441381029049166811L;
    /**
     * 会员卡的id
     */
    public String id;
    /**
     * 会员卡的标题
     */
    public String title;
    /**
     * 会员卡的价格
     */
    public String price;
    /**
     * 订单号
     */
    public String orderNum;
    /**
     * 支付渠道
     */
    public String payChannel;
    public String picture_url;
    public String time_name;
    /**
     * -1：已删除
     * 0：进入了黑名单
     * 1：普通会员
     * 2：季度会员
     * 3：年度会员
     * 4：永久会员
     */
    public String status;
    public int level;
    public int gold;

}
