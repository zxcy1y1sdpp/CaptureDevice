package app.jietuqi.cn.ui.entity;

import java.io.File;
import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/11/20 12:19.
 * 时间： 2018/11/20 12:19
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class OverallPublishEntity implements Serializable {
    private static final long serialVersionUID = -3510765647013466262L;

    public OverallPublishEntity(){
    }
    public OverallPublishEntity(int lastPic){
        this.lastPic = lastPic;
    }
    /**
     * 图片id
     */
    public int id;
    /**
     * 图片地址
     */
    public String url;

    /**
     * 在列表中的位置
     */
    public int position;
    public int lastPic;
    public File pic;
    /**
     * 是否网络图片
     */
    public boolean fromNet;

    /**
     * 0 -- 未上传
     * 1 -- 上传中
     * 2 -- 上传成功
     * 3 -- 上传失败
     */
    public int uploadStatus = 0;
    /**
     * 上传的进度
     */
    public int progress;


    public int width;
    public int height;
    @Override
    public String toString() {
        return "OverallPublishEntity{" +
                "position=" + position +
                ", lastPic=" + lastPic +
                ", pic=" + pic +
                ", uploadStatus=" + uploadStatus +
                ", id=" + id +
                ", url='" + url + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
