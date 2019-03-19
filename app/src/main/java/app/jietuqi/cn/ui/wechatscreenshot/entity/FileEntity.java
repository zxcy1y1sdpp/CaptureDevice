package app.jietuqi.cn.ui.wechatscreenshot.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2019/3/6 10:37.
 * 时间： 2019/3/6 10:37
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class FileEntity implements Serializable {
    private static final long serialVersionUID = -3768849094045604750L;
    public FileEntity(String icon, String suffix, boolean check){
        this.icon = icon;
        this.suffix = suffix;
        this.check = check;
    }
    public FileEntity(){ }

    public String title;
    /**
     * 后缀
     */
    public String suffix;
    /**
     * 大小
     */
    public String size;
    /**
     * 单位
     */
    public String unit;
    public String icon;
    /**
     * 选中的文件位置
     */
    public int position;
    public boolean check;
}
