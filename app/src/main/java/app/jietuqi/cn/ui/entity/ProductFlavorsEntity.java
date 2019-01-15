package app.jietuqi.cn.ui.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/11/28 16:56.
 * 时间： 2018/11/28 16:56
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class ProductFlavorsEntity implements Serializable {
    /**
     * 版本号
     */
    public String appversion;
    /**
     * 渠道
     */
    public String place;
    /**
     * 新的版本号
     * 展示给用户看的
     */
    public String newver;
    /**
     * 更新地址
     */
    public String apkurl;
    /**
     * 更新内容
     */
    public String intro;
    /**
     * 更新时间
     */
    public String updatetime;
    /**
     * 0 -- 非强制更新
     * 1 -- 强制更新
     */
    public int forced;
    /**
     * 开关
     * 0 -- 不可用
     * 1 -- 可用
     */
    public int status;

    @Override
    public String toString() {
        return "ProductFlavorsEntity{" +
                "appversion='" + appversion + '\'' +
                ", status=" + status +
                '}';
    }
}
