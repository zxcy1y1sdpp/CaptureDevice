package dasheng.com.capturedevice.entity;

import java.io.File;
import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/10/25 16:41.
 * 时间： 2018/10/25 16:41
 * 邮箱： 972383753@qq.com
 * 用途： 问题反馈的实体
 */

public class ProblemReportEntity implements Serializable{
    public File pic;
    public int position;
    public int res;
    /**
     * 是不是图示图标
     * 0 -- 提示图标
     * 1 -- 正常图片
     */
    public int type;
}
