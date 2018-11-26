package app.jietuqi.cn.ui.entity;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * 作者： liuyuanbo on 2018/11/23 09:40.
 * 时间： 2018/11/23 09:40
 * 邮箱： 972383753@qq.com
 * 用途： 行业类型的数据类型
 */
public class OverallIndustryEntity implements IPickerViewData {
    public int id;
    public int status;
    public String title;
    public int pid;

    @Override
    public String getPickerViewText() {
        return title;
    }
}
