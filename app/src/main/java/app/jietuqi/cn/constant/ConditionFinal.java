package app.jietuqi.cn.constant;

import java.util.ArrayList;

/**
 * 作者： liuyuanbo on 2018/11/11 17:04.
 * 时间： 2018/11/11 17:04
 * 邮箱： 972383753@qq.com
 * 用途： 条件选择器中需要用到的数据
 */
public class ConditionFinal {
    /**
     * 性别选择
     */
    public final static ArrayList<String> SEXUALITY = new ArrayList<String>(){{
        add("性别不限");
        add("男");
        add("女");
    }};
    public final static ArrayList<String> BUSINESSTTYP = new ArrayList<String>(){{
        add("全部类型");
        add("美装");
        add("食品");
        add("快餐");
        add("快递");
        add("生活用品");
        add("床上用品");
        add("厨房用品");
        add("IT");
        add("计算机维修");
        add("装修");
        add("C TO C");
        add("B TO C");
        add("Android");
        add("Ios");
        add("Java");
        add("Php");
    }};
    public final static ArrayList<String> GROUPTYPE = new ArrayList<String>(){{
        add("全部类型");
        add("互粉群");
        add("兼职群");
        add("创业群");
        add("交友群");
        add("宝妈群");
        add("娱乐互动群");
        add("微商教学群");
        add("兴趣爱好群");
        add("其他");
    }};
    public final static ArrayList<String> GROUPPEOPLE = new ArrayList<String>(){{
        add("人数不限");
        add(">100人");
        add("<100人");
    }};
    public final static ArrayList<String> ADD_FANS_COUNT = new ArrayList<String>(){{
        add("人数不限");
        add("10");
        add("50");
        add("100");
        add("200");
        add("300");
    }};
    public final static ArrayList<String> ADD_FANS_COUNT_2 = new ArrayList<String>(){{
        add("10");
        add("50");
        add("100(VIP)");
        add("200(VIP)");
        add("300(VIP)");
        add("500(年费以上VIP)");
    }};
}
