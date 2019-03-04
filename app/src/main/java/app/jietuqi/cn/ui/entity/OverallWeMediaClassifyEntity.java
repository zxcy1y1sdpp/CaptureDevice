package app.jietuqi.cn.ui.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 作者： liuyuanbo on 2019/2/22 10:24.
 * 时间： 2019/2/22 10:24
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class OverallWeMediaClassifyEntity implements Serializable {
    private static final long serialVersionUID = 6691540651764074630L;

    public int id;
    public String title;
    public String name;
    public int industry_id;
    public String price;
    public int hits;
    public String hits_title;
    public int fans;
    public String fans_title;
    public int approve;
    public int bias;
    public int sort;
    public String content;
    public String picture_id;
    public int classify;
    public int video;

    public OverallIndustryEntity industry;
    public ArrayList<PictureEntity> picture;


    public static class PictureEntity implements Serializable {

        private static final long serialVersionUID = -5107772865332242044L;
        public String path;
        public String url;
        public String content;
        public int status;
        public int width;
        public int height;
    }
}
