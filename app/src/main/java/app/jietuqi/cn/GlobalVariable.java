package app.jietuqi.cn;

import java.util.ArrayList;

import app.jietuqi.cn.ui.entity.OverallIndustryEntity;

/**
 * 作者： liuyuanbo on 2018/11/23 09:55.
 * 时间： 2018/11/23 09:55
 * 邮箱： 972383753@qq.com
 * 用途： 全局变量
 *       行业类别
 */
public class GlobalVariable {
    private static GlobalVariable mInstant;
    /**
     * 行业类别
     */
    public ArrayList<OverallIndustryEntity> INDUSTRY_LIST;

    // 单例模式中获取唯一的GlobalVariable实例
    public static GlobalVariable getInstance() {
        if (null == mInstant) {
            mInstant = new GlobalVariable();
        }
        return mInstant;
    }
}
