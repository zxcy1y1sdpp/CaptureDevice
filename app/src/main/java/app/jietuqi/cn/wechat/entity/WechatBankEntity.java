package app.jietuqi.cn.wechat.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2019/1/25 17:40.
 * 时间： 2019/1/25 17:40
 * 邮箱： 972383753@qq.com
 * 用途： 微信模拟器中的银行卡信息
 */
public class WechatBankEntity implements Serializable {
    private static final long serialVersionUID = -1041380457942877991L;
    public WechatBankEntity(String bankIcon, String bankName, String bankShortName, int bankLimitMoney, String bankTailNumber, String bankReachTime){
        this.bankIcon = bankIcon;
        this.bankLimitMoney = bankLimitMoney;
        this.bankName = bankName;
        this.bankShortName = bankShortName;
        this.bankTailNumber = bankTailNumber;
        this.bankReachTime = bankReachTime;
    }
    public WechatBankEntity(){}
    /**
     * 银行卡图标的名称
     */
    public String bankIcon;
    /**
     * 银行卡名称
     */
    public String bankName;
    /**
     * 银行卡的名称简称
     */
    public String bankShortName;
    /**
     * 银行卡限额
     */
    public int bankLimitMoney;
    /**
     * 银行卡尾号
     */
    public String bankTailNumber;
    /**
     * 到账时间
     */
    public String bankReachTime;

    public boolean isCheck;

    /*
    *
    * if ([bank containsString:@"中国"]) {
        return @"20000.00";
    }else if ([bank containsString:@"华夏"]){

        return @"100000.00";
    }else if ([bank containsString:@"邮储"]){

        return @"20000.00";
    }else if ([bank containsString:@"农业"]){

        return @"20000.00";
    }else if ([bank containsString:@"民生"]){

        return @"50000.00";
    }else if ([bank containsString:@"交通"]){

        return @"10000.00";
    }else if ([bank containsString:@"建设"]){

        return @"50000.00";
    }else if ([bank containsString:@"工商"]){

        return @"50000.00";
    }else{
        return @"20000.00";
    }*/
}
