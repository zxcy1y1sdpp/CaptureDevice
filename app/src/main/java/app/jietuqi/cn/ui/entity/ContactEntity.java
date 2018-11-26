package app.jietuqi.cn.ui.entity;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2018/11/25 20:19.
 * 时间： 2018/11/25 20:19
 * 邮箱： 972383753@qq.com
 * 用途： 手机电话簿的实体
 */
public class ContactEntity implements Serializable {

    public ContactEntity(String phoneNumner, String nickName){
        this.phoneNumner = phoneNumner;
        this.nickName = nickName;
    }
    /**
     * 根据这个id进行删除
     */
    public long rawContactId;
    /**
     * 电话号码
     */
    public String phoneNumner;
    /**
     * 昵称
     */
    public String nickName;
}
