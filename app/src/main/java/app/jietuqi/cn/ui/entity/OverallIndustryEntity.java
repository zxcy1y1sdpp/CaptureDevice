package app.jietuqi.cn.ui.entity;

import com.contrarywind.interfaces.IPickerViewData;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/11/23 09:40.
 * 时间： 2018/11/23 09:40
 * 邮箱： 972383753@qq.com
 * 用途： 行业类型的数据类型
 */
public class OverallIndustryEntity implements IPickerViewData, Serializable {
    private static final long serialVersionUID = -8113608684847687480L;
    public int id;
    public int status;
    /**
     * 全称
     */
    public String title;
    /**
     * 简称
     */
    public String name;
    public int pid;
    public int classify;
    public String background;

    public long create_time;
    /**
     * 分类的图标
     */
    public String logo;
    @Override
    public String getPickerViewText() {
        return title;
    }
}
