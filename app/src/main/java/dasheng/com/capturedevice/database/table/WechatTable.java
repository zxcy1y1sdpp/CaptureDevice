//package dasheng.com.capturedevice.database.table;
//
//import java.io.Serializable;
//import java.util.Date;
//
//import dasheng.com.capturedevice.util.TimeUtil;
//
///**
// * 作者： liuyuanbo on 2018/10/10 10:37.
// * 时间： 2018/10/10 10:37
// * 邮箱： 972383753@qq.com
// * 用途： 微信聊天的数据表
// */
//public class WechatTable implements Serializable{
//    /**
//     *
//     * @param msgType
//     * @param sendOrReceiveMsg
//     * @param otherId
//     * @param otherNickName
//     * @param otherAvatar
//     * @param textType
//     */
//    public WechatTable(int msgType, int sendOrReceiveMsg, String otherId, String otherNickName, int otherAvatar, String textType){
//        this.msgType = msgType;
//        this.sendOrReceiveMsg = sendOrReceiveMsg;
//        this.otherId = otherId;
//        this.otherNickName = otherNickName;
//        this.otherAvatar = otherAvatar;
//        this.textType = textType;
//        this.timeType = TimeUtil.getCurrentTimeEndMs();
//        this.tableName = "wechat" + otherId;
//    }
//    /**
//     * 自增长主键
//     */
//    public long id;
//    /**
//     * 类型
//     * 0 -- 文字类型
//     */
//    public int msgType;
//    /**
//     * 当前消息是发送还是接收
//     * 0 -- 发送，1 -- 接收
//     */
//    public int sendOrReceiveMsg;
//    /**
//     * 对方id
//     */
//    public String otherId;
//    /**
//     * 对方昵称
//     */
//    public String otherNickName;
//    /**
//     * 对方头像
//     */
//    public int otherAvatar;
//    /**
//     * 文字消息
//     */
//    public String textType;
//    /**
//     * 时间
//     */
//    public int timeType;
//    /**
//     * 表名称
//     */
//    public String tableName;
//
////    /**
////     * 转账 -- 发送类型
////     */
////    @Column
////    public String sendTransferAccountsType;
////    /**
////     * 语音
////     */
////    @Column
////    public String voice;
////    /**
////     * 转账 -- 领取类型
////     */
////    @Column
////    public String receiveTransferAccountsType;
////    /**
////     * 图片
////     */
////    @Column
////    public String imgType;
////    /**
////     * 红包发送
////     */
////    @Column
////    public String sendRedPocketType;
////    /**
////     * 红包领取
////     */
////    @Column
////    public String receiveRedPocketType;
////    /**
////     * 视频聊天
////     */
////    @Column
////    public String videoType;
////    /**
////     * 文件
////     */
////    @Column
////    public String fileType;
////    /**
////     * 系统消息
////     */
////    @Column
////    public String system;
////    /**
////     * 名片
////     */
////    @Column
////    public String card;
////    /**
////     * 发送人
////     */
////    @Column
////    public String sender;
////    /**
////     * 接收人
////     */
////    @Column
////    public int receiveType;
//
//    @Override
//    public String toString() {
//        return super.toString();
//    }
//}
