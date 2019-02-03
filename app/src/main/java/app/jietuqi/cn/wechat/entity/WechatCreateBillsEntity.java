package app.jietuqi.cn.wechat.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/12/24 10:36.
 * 时间： 2018/12/24 10:36
 * 邮箱： 972383753@qq.com
 * 用途： 微信账单
 */
public class WechatCreateBillsEntity implements Serializable {
    private static final long serialVersionUID = 2458858534239570646L;
    public int id;
    /**
     * 类型
     * 红包，零钱，二维码收款，随机角色，相册自定义
     */
    public String type;
    /**
     * 来自sdcard的图片
     */
    public String iconString;
    /**
     * 本地的图片
     */
    public int iconInt;
    /**
     * 标题
     */
    public String title;
    /**
     * 账单的时间戳-long
     */
    public long timestamp;
    /**
     * 账单的展示时间
     */
    public String time;
    /**
     * 金额
     */
    public String money;
    /**
     * 收支类型
     * 0 -- 收入
     * 1 -- 支出
     */
    public int incomeAndExpenses;
    /**
     * 是否用有退款
     * 1 -- 有退款
     * 0 -- 没有退款
     */
    public boolean hasRefund;
    /**
     * 在列表中的位置
     */
    public int position;
    /*************************** 非数据库中的字段 *************************/
    /**
     * 用于区分eventbus的tag
     * 0 -- 新增
     * 1 -- 修改
     * 2 -- 删除
     */
    public int tag;
}
