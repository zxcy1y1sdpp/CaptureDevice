package app.jietuqi.cn.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/12/13 12:42.
 * 时间： 2018/12/13 12:42
 * 邮箱： 972383753@qq.com
 * 用途： 轮播图
 */
public class BannerEntity implements Serializable {

    private static final long serialVersionUID = -4089934159173047596L;
    public String title;
    public String content;
    public String imgurl;
    public String hrefurl;

    @Override
    public String toString() {
        return imgurl;
    }
}
