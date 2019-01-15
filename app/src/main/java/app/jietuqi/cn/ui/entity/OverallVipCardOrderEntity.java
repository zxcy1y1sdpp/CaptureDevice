package app.jietuqi.cn.ui.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/11/17 11:29.
 * 时间： 2018/11/17 11:29
 * 邮箱： 972383753@qq.com
 * 用途： 购买会员卡时返回的实体
 */
public class OverallVipCardOrderEntity implements Serializable {
    public String add;
    public String pay;
    public String money;
    public String uid;
    public String classify;
    public String type;
    public String title;
    public String number;
    /**
     * 支付渠道
     */
    public String pay_channel;
    public String plat;
    public String price_id;
    public String status;
    /**
     * 订单号
     */
    public String sn;
    public String create_time;
    public String update_time;
    public String id;
    public String info;
}
