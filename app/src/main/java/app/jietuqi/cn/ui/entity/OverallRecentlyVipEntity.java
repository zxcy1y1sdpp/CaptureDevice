package app.jietuqi.cn.ui.entity;

import java.io.Serializable;

import app.jietuqi.cn.util.StringUtils;

/**
 * 作者： liuyuanbo on 2018/11/26 15:01.
 * 时间： 2018/11/26 15:01
 * 邮箱： 972383753@qq.com
 * 用途： 最近成为会员的实体
 */
public class OverallRecentlyVipEntity implements Serializable {
    private static final long serialVersionUID = 3417562760450939779L;
    /**
     * 2 -- 季度会员
     * 3 -- 年费会员
     * 4 -- 永久会员
     */
    public int status;
    public String type;
    public String nickname;
    public String content;

    @Override
    public String toString() {
        String vipType = "";
        if (2 == status){
            vipType = "三个月会员";
        }else if (3 == status){
            vipType = "12个月会员";
        }else if (3 == status){
            vipType = "永久会员";
        }
        return StringUtils.insertFrontAndBack("办理了", nickname, vipType);
    }
}
