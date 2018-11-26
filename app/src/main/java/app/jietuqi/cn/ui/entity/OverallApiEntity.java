package app.jietuqi.cn.ui.entity;

import com.zhouyou.http.model.ApiResult;

/**
 * 作者： liuyuanbo on 2018/11/15 12:14.
 * 时间： 2018/11/15 12:14
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class OverallApiEntity<T> extends ApiResult<T> {
    public String time;
    @Override
    public boolean isOk() {
        return getCode() == 200;
    }

    @Override
    public String toString() {
        return "OverallApiEntity{" +
                "time='" + time + '\'' +
                '}';
    }
}
