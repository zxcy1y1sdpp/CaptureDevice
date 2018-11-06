package dasheng.com.capturedevice.alipay.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/11/5 11:16.
 * 时间： 2018/11/5 11:16
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝会员等级
 */

public class AlipayVipLevelEntity implements Serializable {
    public String levelName;
    public int levelPic;
    public AlipayVipLevelEntity(String levelName, int levelPic){
        this.levelName = levelName;
        this.levelPic = levelPic;
    }
}
