package app.jietuqi.cn.wechat.simulator;

import java.io.Serializable;

/**
 * 作者： liuyuanbo on 2019/1/20 15:24.
 * 时间： 2019/1/20 15:24
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class WechatSimulatorUnReadEntity implements Serializable {

    private static final long serialVersionUID = 1522041403586095119L;

    public WechatSimulatorUnReadEntity(int tag, int unRead){
        this.tag = tag;
        this.unRead = unRead;
    }

    public int tag;
    public int unRead;
    /**
     * 需要极爱你少的个数
     */
    public int reduceNum;
}
