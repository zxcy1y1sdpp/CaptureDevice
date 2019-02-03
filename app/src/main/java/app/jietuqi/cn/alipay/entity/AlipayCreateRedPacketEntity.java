package app.jietuqi.cn.alipay.entity;

import app.jietuqi.cn.ui.entity.WechatUserEntity;

/**
 * 作者： liuyuanbo on 2018/10/31 17:20.
 * 时间： 2018/10/31 17:20
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class AlipayCreateRedPacketEntity extends WechatUserEntity {
    private static final long serialVersionUID = -8023298862404191672L;
    /**
     * 红包金额
     */
    public String money;
    /**
     * 红包编号
     */
    public String num;
}
