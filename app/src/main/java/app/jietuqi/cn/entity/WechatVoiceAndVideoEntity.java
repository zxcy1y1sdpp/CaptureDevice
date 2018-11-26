package app.jietuqi.cn.entity;

import java.io.File;

import app.jietuqi.cn.database.table.WechatUserTable;

/**
 * 作者： liuyuanbo on 2018/10/22 15:31.
 * 时间： 2018/10/22 15:31
 * 邮箱： 972383753@qq.com
 * 用途： 语音或者视频聊天的实体
 */

public class WechatVoiceAndVideoEntity extends WechatUserTable {
    /**
     * 视频通话的背景
     */
    public File wechatBg;
    /**
     * 0 -- 待接听
     * 1 -- 通话中
     */
    public int type;
    /**
     * 0 -- 语音通话
     * 1 -- 视频通话
     */
    public int chatType;
    /**
     * 通话时长
     */
    public String time;
    /**
     * 对方视频画面
     */
    public File otherFileBg;
    /**
     * 我的视频画面
     */
    public File myFileBg;
}
