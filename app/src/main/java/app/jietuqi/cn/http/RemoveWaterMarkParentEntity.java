package app.jietuqi.cn.http;

import com.zhouyou.http.model.ApiResult;

/**
 * 作者： liuyuanbo on 2018/11/14 16:32.
 * 时间： 2018/11/14 16:32
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class RemoveWaterMarkParentEntity<T> extends ApiResult<T> {

    int retCode;
    String retDesc;
    String succ;
    @Override
    public int getCode() {
        return retCode;
    }

    @Override
    public String getMsg() {
        return retDesc;
    }

   /* @Override
    public T getData() {
        return data;
    }*/

    @Override
    public boolean isOk() {
        return retCode == 200;
    }

    public String getSucc() {
        return succ;
    }

    public void setSucc(String succ) {
        this.succ = succ;
    }
}
