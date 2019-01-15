package app.jietuqi.cn.ui.entity;

import java.io.Serializable;
import java.util.ArrayList;

import app.jietuqi.cn.widget.ninegrid.ImageInfo;

/**
 * 作者： liuyuanbo on 2018/11/7 17:06.
 * 时间： 2018/11/7 17:06
 * 邮箱： 972383753@qq.com
 * 用途： 评论以及评论详情的实体
 */

public class OverallCommunicateEntity implements Serializable {
    public int id;
    /**
     * 头像
     */
    public String avatar;
    /**
     * 昵称
     */
    public String nickName;
    /**
     * 2 -- 季度会员
     * 3 -- 年费会员
     * 4 -- 永久会员
     */
    public int vip;
    /**
     * 评论内容
     */
    public String content;
    /**
     * 喜欢的人数
     */
    public String likeCount;
    /**
     * 评论的人数
     */
    public String pingLunCount;
    /**
     * 有没有点赞
     */
    public boolean like;
    /**
     * 创建时间
     */
    public long createTime;
    /**
     * 图片
     */
    public ArrayList<ImageInfo> pics;
    /*************************************** 自定义参数 *****************************************/
    /**
     * 全文/收起的状态
     * -1 位置状态
     * 1 文本行数小于最大可显示行数
     * 2 折叠状态
     * 3 展开状态
     */
    public int status = -1;
    /**
     * 头像测试用的
     */
    public int avatarInt;
}
