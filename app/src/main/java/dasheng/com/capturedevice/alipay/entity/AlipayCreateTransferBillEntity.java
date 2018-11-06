package dasheng.com.capturedevice.alipay.entity;

import dasheng.com.capturedevice.database.table.WechatUserTable;

/**
 * 作者： liuyuanbo on 2018/11/2 09:45.
 * 时间： 2018/11/2 09:45
 * 邮箱： 972383753@qq.com
 * 用途： 创建微信转账账单的实体
 */

public class AlipayCreateTransferBillEntity extends WechatUserTable{
    /**
     * 对方账号
     */
    public String account;
    /**
     * 转账金额
     */
    public String money;
    /**
     * 转账编号
     */
    public String num;
    /**
     * 收款方式
     */
    public String paymentMethods;
    /**
     * 创建时间
     */
    public String createTime;
    /**
     * 账单分类
     */
    public String billClassify;
    /**
     * 交易状态
     */
    public String transferStatus;
    /**
     * 0 -- 转入
     * 1 -- 转出
     */
    public int type;
    /**
     * 是否显示往来记录
     */
    public boolean showContactRecord = false;

}
