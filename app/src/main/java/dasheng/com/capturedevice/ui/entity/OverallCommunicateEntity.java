package dasheng.com.capturedevice.ui.entity;

import java.io.Serializable;
import java.util.ArrayList;

import dasheng.com.capturedevice.widget.ninegrid.ImageInfo;

/**
 * 作者： liuyuanbo on 2018/11/7 17:06.
 * 时间： 2018/11/7 17:06
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class OverallCommunicateEntity implements Serializable {
    /**
     * 头像
     */
    public String avatar;
    /**
     * 头像
     */
    public int avatarInt;
    /**
     * 昵称
     */
    public String nickName;
    /**
     * 评论内容
     */
    public String content;
    /**
     * 图片
     */
    public ArrayList<ImageInfo> pics;
}
