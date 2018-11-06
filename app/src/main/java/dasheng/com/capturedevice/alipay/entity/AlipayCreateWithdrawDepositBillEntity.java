package dasheng.com.capturedevice.alipay.entity;

import dasheng.com.capturedevice.wechat.entity.WechatWithdrawDepositEntity;

/**
 * 作者： liuyuanbo on 2018/11/3 16:45.
 * 时间： 2018/11/3 16:45
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class AlipayCreateWithdrawDepositBillEntity extends WechatWithdrawDepositEntity {
    /**
     * 持卡人姓名
     */
    public String name;
    /**
     * 账单分类
     */
    public String classify;
    /**
     * 提现时间
     */
    public String withdrawDepositTime;
    /**
     * 成功时间
     */
    public String successTime;
    /**
     * 银行图标
     */
    public int bankPic;
    /**
     * 创建时间
     */
    public String createTime;

}
