package app.jietuqi.cn.entity;

import java.io.File;
import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/10/25 16:41.
 * 时间： 2018/10/25 16:41
 * 邮箱： 972383753@qq.com
 * 用途： 问题反馈的实体
 */

public class ProblemReportEntity implements Serializable{
    private static final long serialVersionUID = 1906773951499633086L;
    public int id;
    public File pic;
    public int position;
    public int res;
    /**
     * 是不是图示图标
     * 0 -- 提示图标
     * 1 -- 正常图片
     */
    public int type;
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
}
