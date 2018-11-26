package app.jietuqi.cn.wechat.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/10/23 14:17.
 * 时间： 2018/10/23 14:17
 * 邮箱： 972383753@qq.com
 * 用途： 微信提现的实体类
 */

public class WechatWithdrawDepositEntity implements Serializable{
    /**
     * 预计到账时间
     */
    public String time;
    /**
     * 选择的银行
     */
    public String bank;
    /**
     * 银行卡号后四位
     */
    public String bankNum4;
    /**
     * 提现的金额
     */
    public String money;
    /**
     * 是否有手续费
     */
    public boolean serviceCharge = true;
}
