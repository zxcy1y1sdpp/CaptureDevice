package app.jietuqi.cn.ui.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/11/16 11:30.
 * 时间： 2018/11/16 11:30
 * 邮箱： 972383753@qq.com
 * 用途： 第三方登录返回的实体类
 */
public class OverallThridLoginEntity implements Serializable {
    /**
     * 第三方登录授权后返回的用户的昵称
     */
    public String nickName;
    /**
     * 第三方登录授权后返回的用户的头像
     */
    /**
     * 第三方登录授权后返回的性别
     * 0 -- 保密
     * 1 -- 男
     * 2 -- 女
     */
    public String sex;
    public String openId;
    /**
     * 第三方登录授权后的用OpenId
     */
    public String avatar;
    /**
     * 省
     */
    public String province;
    /**
     * 市
     */
    public String district;
    /**
     * 登录方式
     * qq
     * wx
     */
    public String loginType;
}
