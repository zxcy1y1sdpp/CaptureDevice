package app.jietuqi.cn.entity;

import app.jietuqi.cn.ui.entity.WechatUserEntity;

/**
 * 作者： liuyuanbo on 2018/10/22 14:36.
 * 时间： 2018/10/22 14:36
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class WechatTransferEntity extends WechatUserEntity {

    /**
     * 转账金额
     */
    public String money;
    /**
     * 转账状态
     * 已收钱
     * 代收款
     * 已退款
     */
    public String transferType;

    /**
     * 收钱还是转出
     * 0 -- 收钱
     * 1 -- 转出
     */
    public int type;

    /**
     * 转出时间
     */
    public String outTime;
    /**
     * 收钱时间
     */
    public String receiveTime;

}
