package app.jietuqi.cn.ui.entity;

import java.io.Serializable;
import java.util.ArrayList;

import app.jietuqi.cn.entity.OverallUserInfoEntity;

/**
 * 作者： liuyuanbo on 2019/1/22 20:59.
 * 时间： 2019/1/22 20:59
 * 邮箱： 972383753@qq.com
 * 用途： 项目市场的实体
 */
public class ProjectMarketEntity implements Serializable {
    private static final long serialVersionUID = -7538980940649792737L;
    public int id;
    /**发布人的id*/
    public int users_id;
    /**图片封面*/
    public String logo;
    /**标题*/
    public String name;
    /**更新时间 -- 秒*/
    public long update_time;
    /**类别的id*/
    public int industry_id;
    /**微信号*/
    public String wx;
    /**手机号*/
    public String phone;
    /** qq号*/
    public String qq;
    /*** 项目描述*/
    public String content;
    /** 图片的id集合*/
    public String picture_id;
    /** 0 -- 未审核通过*/
    public int status;
    /** 0 -- 未置顶*/
    public int is_top;
    /** 到期时间*/
    public int top_time;
    /**浏览次数*/
    public int view;
    /**
     * 0 -- 未收藏
     * 1 -- 收藏
     */
    public int is_favour;
    /** 收藏数量*/
    public int favour;

    /**分类*/
    public OverallIndustryEntity industry;

    public OverallUserInfoEntity users;

    public ArrayList<OverallPublishEntity> picture;
}
