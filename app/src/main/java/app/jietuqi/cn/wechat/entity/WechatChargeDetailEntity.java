package app.jietuqi.cn.wechat.entity;

import app.jietuqi.cn.ui.entity.WechatUserEntity;

/**
 * 作者： liuyuanbo on 2018/10/30 15:32.
 * 时间： 2018/10/30 15:32
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class WechatChargeDetailEntity extends WechatUserEntity {
    private static final long serialVersionUID = -8511153219728847984L;

    public WechatChargeDetailEntity() {}
    public WechatChargeDetailEntity(int id, String type, String name, String money, String time) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.money = money;
        this.time = time;
    }
    public int id;
    /**
     * 收支类型
     * 0 -- 收入
     * 1 -- 支出
     */
    public String type = "0";
    /**
     * 具体收入或者支出的名称
     */
    public String name;
    /**
     * 金额
     */
    public String money;
    /**
     * 收入或者支出的时间
     */
    public String time;
}
