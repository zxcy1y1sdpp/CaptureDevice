package app.jietuqi.cn.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2019/1/29 16:54.
 * 时间： 2019/1/29 16:54
 * 邮箱： 972383753@qq.com
 * 用途： 微币明细
 */
public class WbDetailsEntity implements Serializable {
    private static final long serialVersionUID = 355456456705256345L;
    public int id;
    public int amount;
    public String classify;
    public int users_id;
    public String intro;
    public String type;
    public String create_time;

    public String group;
}
