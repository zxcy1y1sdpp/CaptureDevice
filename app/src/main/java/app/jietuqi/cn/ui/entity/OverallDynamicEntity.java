package app.jietuqi.cn.ui.entity;

import java.io.Serializable;
import java.util.ArrayList;

import app.jietuqi.cn.widget.ninegrid.ImageInfo;

/**
 * 作者： liuyuanbo on 2018/11/20 18:21.
 * 时间： 2018/11/20 18:21
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class OverallDynamicEntity implements Serializable{
    /**
     * id : 56
     * title :
     * category_id : 0
     * uid : 13
     * cover_id : 455,454,456,453
     * description : null
     * content : sadf
     * status : 1
     * is_top : 0
     * view : 0
     * update_time : 1542708629
     * create_time : 1542708629
     * favour : 0
     * collect : 0
     * nickname : 139*****206
     * headimgurl : null
     * cover : [{"id":455,"path":"/uploads/picture/20181120/48aaa650f4ad45492f1a83232de759be.jpeg","url":"http://www.jietuqi.cn/uploads/picture/20181120/48aaa650f4ad45492f1a83232de759be.jpeg","md5":"381564cb1b92dc99c9660b8547c37c51","sha1":"3a93732cc29056dace831ce66626059311b33454","status":1,"create_time":1542708576,"width":1024,"height":768},{"id":454,"path":"/uploads/picture/20181120/af8d827044ee3640b887f2765298c779.jpeg","url":"http://www.jietuqi.cn/uploads/picture/20181120/af8d827044ee3640b887f2765298c779.jpeg","md5":"de7bf680797ea70edba6cffaaa51ec4d","sha1":"dc8a15100521685cec5f68964b52ba45705e25fc","status":1,"create_time":1542708576,"width":700,"height":700},{"id":456,"path":"/uploads/picture/20181120/ace9e4ac0451d99b724b23a94b74264e.jpeg","url":"http://www.jietuqi.cn/uploads/picture/20181120/ace9e4ac0451d99b724b23a94b74264e.jpeg","md5":"d32847c50209dae0f6f921408c273932","sha1":"c79100f93a5ea3aef40a2631103ce43ec6ea3435","status":1,"create_time":1542708576,"width":1280,"height":851},{"id":453,"path":"/uploads/picture/20181120/c2919441e0016a5b8eb468ad98138840.png","url":"http://www.jietuqi.cn/uploads/picture/20181120/c2919441e0016a5b8eb468ad98138840.png","md5":"9309fb99c5ace6705780f4b23597c74b","sha1":"b864f66133a5276764025016cece679f80c933a0","status":1,"create_time":1542708576,"width":300,"height":300}]
     * comment : [{"id":25,"title":"","uid":13,"cover_id":null,"description":null,"content":"222222","status":1,"is_top":0,"view":0,"update_time":1542705736,"create_time":1542705736,"article_id":56,"pid":0,"favour":1,"nickname":"139*****206","headimgurl":null,"is_favour":0,"_child":[]}]
     * is_favour : 0
     */

    public int id;
    public String title;
    public int category_id;
    public int uid;
    public String cover_id;
    public String description;
    public String content;
    public int status;
    public int is_top;
    public int view;
    public long update_time;
    public long create_time;
    /**
     * 点赞个数
     */
    public int favour;
    /**
     * 收藏个数
     */
    public int collect;
    /**
     * 评论个数
     */
    public int comment_number;
    public String nickname;
    public String headimgurl;
    public int is_favour;
    public ArrayList<Cover> cover;
    /**
     * 评论
     */
    public ArrayList<Comment> comment;

    /**
     * 是否显示全文的状态
     */
    public int showAllStatus = -1;
    /**
     * 传给九宫格用的
     */
    public ArrayList<ImageInfo> infoList = new ArrayList<>();
    /**
     * 在列表中的位置
     */
    public int position = -1;

    public static class Cover implements Serializable {
        /**
         * id : 455
         * path : /uploads/picture/20181120/48aaa650f4ad45492f1a83232de759be.jpeg
         * url : http://www.jietuqi.cn/uploads/picture/20181120/48aaa650f4ad45492f1a83232de759be.jpeg
         * md5 : 381564cb1b92dc99c9660b8547c37c51
         * sha1 : 3a93732cc29056dace831ce66626059311b33454
         * status : 1
         * create_time : 1542708576
         * width : 1024
         * height : 768
         */

        public int id;
        public String url;
        public int status;
        public long create_time;
        public int width;
        public int height;

    }

    public static class Comment implements Serializable {
        /**
         * id : 25
         * title :
         * uid : 13
         * cover_id : null
         * description : null
         * content : 222222
         * status : 1
         * is_top : 0
         * view : 0
         * update_time : 1542705736
         * create_time : 1542705736
         * article_id : 56
         * pid : 0
         * favour : 1
         * nickname : 139*****206
         * headimgurl : null
         * is_favour : 0
         * _child : []
         */

        public int id;
        public String title;
        public int uid;
        public String cover_id;
        public String description;
        public String content;
        public int status;
        public int is_top;
        public int view;
        public int update_time;
        public long create_time;
        public int article_id;
        public int pid;
        public int favour;
        public String nickname;
        public String headimgurl;
        public int is_favour;
        public ArrayList<?> _child;
        public int position = -1;

    }
}