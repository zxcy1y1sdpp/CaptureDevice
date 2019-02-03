package app.jietuqi.cn.ui.entity;

import java.io.Serializable;

import app.jietuqi.cn.util.StringUtils;

/**
 * 作者： liuyuanbo on 2018/11/23 15:13.
 * 时间： 2018/11/23 15:13
 * 邮箱： 972383753@qq.com
 * 用途： 名片
 */
public class OverallCardEntity implements Serializable {
    private static final long serialVersionUID = 5907454140242663844L;
    public int id;
    public String title;
    /**
     * 行业类别的id
     */
    public int industry_id;
    public int uid;
    public int cover_id;
    public String description;
    public String content;
    public int status;
    /**
     * 会员等级
     */
    public int vip;
    public int is_top;
    public int view;
    public long update_time;
    public long create_time;
    public int district_id;
    public int sex;
    public String wx_qr;
    public String mobile;
    public String wxname;
    public String wxnickname;
    /**
     *1 -- 小于100
     * 2 -- 大于100
     */
    public int number;
    public String classify;
    public String district;
    public String province;
    public String headimgurl;
    public IndustryEntity industry;
    public String getArea(){
        return StringUtils.insertFrontAndBack(" ", province, district);
    }

    public class IndustryEntity implements Serializable{
        private static final long serialVersionUID = -7715651537649468983L;
        public int id;
        public String name;
        public String background;
    }
}
