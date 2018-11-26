package app.jietuqi.cn.alipay.entity;

import app.jietuqi.cn.database.table.WechatUserTable;

/**
 * 作者： liuyuanbo on 2018/10/31 17:20.
 * 时间： 2018/10/31 17:20
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class AlipayCreateRedPacketEntity extends WechatUserTable {
    /**
     * 红包金额
     */
    public String money;
    /**
     * 红包编号
     */
    public String num;
}
