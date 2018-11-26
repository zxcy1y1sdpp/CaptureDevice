package app.jietuqi.cn.alipay.entity;

import app.jietuqi.cn.database.table.WechatUserTable;

/**
 * 作者： liuyuanbo on 2018/11/5 10:36.
 * 时间： 2018/11/5 10:36
 * 邮箱： 972383753@qq.com
 * 用途： 创建支付宝我的页面的实体
 */

public class AlipayCreateMyEntity extends WechatUserTable {
    /**
     * 支付宝账号
     */
    public String account;
    /**
     * 蚂蚁会员
     */
    public String ant;
    /**
     * 余额
     */
    public String money;
    /**
     * 余利宝
     */
    public boolean showYuLiBao;
    /**
     * 万元保障
     */
    public boolean showThousandSecurity;
    /**
     * 蚂蚁会员小红点
     */
    public boolean showRedPoint;
    /**
     * 商家服务
     */
    public boolean showMerchant;
    /**
     * 对方支付宝等级
     */
    public AlipayVipLevelEntity levelEntity;

}
