package app.jietuqi.cn.alipay.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/11/1 13:09.
 * 时间： 2018/11/1 13:09
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝余额
 */

public class AlipayPreviewBalanceEntity implements Serializable{
    /**
     * 红包金额
     */
    public String balanceMoney;
    /**
     * 今天转入金额
     */
    public String todayMoney;
    /**
     * 是否显示余额宝
     */
    public boolean showYuEBao;
    /**
     * 是否开通余额宝
     */
    public boolean dredgeYuEBao;
}
