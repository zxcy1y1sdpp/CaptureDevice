package app.jietuqi.cn.entity;

import java.io.File;
import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/11/14 09:29.
 * 时间： 2018/11/14 09:29
 * 邮箱： 972383753@qq.com
 * 用途： 登录和注册的实体
 */
public class OverallUserInfoEntity implements Serializable {
    public int id;
    public String title;
    public String headimgurl;
    /**
     * 个人介绍
     */
    public String description;
    public String content;
    /**
     * -1：已删除
     * 0：进入了黑名单
     * 1：普通会员
     * 2：季度会员
     * 3：年度会员
     * 4：永久会员
     */
    public int status;
    public long update_time;
    public long create_time;
    public String mobile;
    public String wx_openid;
    public String qq_openid;
    public String ali_openid;
    public String wb_openid;
    public String cash;
    public String fund;
    public int credit;
    public int district_id;
    public String address;
    public String name;
    public int sex;
    public int birthday;
    public String password;
    public String pid;
    public String nickname;
    public int vip_time;
    public int agency;
    public int ratio;
    public String wx_qr;
    public int aid;
    /**
     * 邀请了多少人
     */
    public int share_number;
    ///////////////////////////////////////////////////
    /**
     * 本地头像
     * File形式
     */
    public File localHead;

    @Override
    public String toString() {
        return "OverallUserInfoEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", description='" + description + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", update_time=" + update_time +
                ", create_time=" + create_time +
                ", mobile='" + mobile + '\'' +
                ", wx_openid='" + wx_openid + '\'' +
                ", qq_openid='" + qq_openid + '\'' +
                ", ali_openid='" + ali_openid + '\'' +
                ", wb_openid='" + wb_openid + '\'' +
                ", cash='" + cash + '\'' +
                ", fund='" + fund + '\'' +
                ", credit=" + credit +
                ", district_id=" + district_id +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", password='" + password + '\'' +
                ", pid='" + pid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", vip_time=" + vip_time +
                ", agency=" + agency +
                ", ratio=" + ratio +
                ", wx_qr='" + wx_qr + '\'' +
                ", aid=" + aid +
                '}';
    }
}
