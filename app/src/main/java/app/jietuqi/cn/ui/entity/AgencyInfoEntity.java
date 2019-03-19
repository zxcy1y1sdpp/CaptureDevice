package app.jietuqi.cn.ui.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2019/3/14 10:18.
 * 时间： 2019/3/14 10:18
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class AgencyInfoEntity implements Serializable {
    private static final long serialVersionUID = -2223696615605308243L;

    public String id;
    public String users_id;
    public String alipay_name;
    public String alipay_account;
    /**
     * 当前余额
     */
    public String cash;
    /**
     * 累计提现
     */
    public String amount;
    /**
     * 正在提现的金额
     */
    public String money;
    public String agent_time;
    public String code;
    public String wx_account;
    public String create_time;
    public String update_time;

}
