package app.jietuqi.cn.ui.wechatscreenshot.adapter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.SingleTalkEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.WechatScreenShotHelper
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.ui.wechatscreenshot.widget.EmojiWechatManager
import app.jietuqi.cn.util.*
import app.jietuqi.cn.widget.autolink.AutoLinkMode
import app.jietuqi.cn.widget.autolink.AutoLinkTextView
import com.makeramen.roundedimageview.RoundedImageView
import com.xinlan.imageeditlibrary.ToastUtils
import com.zhy.android.percent.support.PercentRelativeLayout
import java.io.File

/**
 * 作者： liuyuanbo on 2018/10/10 17:50.
 * 时间： 2018/10/10 17:50
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class WechatScreenShotPreviewAdapter(val mList: MutableList<WechatScreenShotEntity>, val mShowBg: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * 默认是本人操作
     */
    private val TEXT_TYPE_OTHER = 0//、对方发送的文本聊天数据
    private val TEXT_TYPE_MY = 1//我发送的文本聊天数据
    private val IMG_TYPE_OTHER = 2//对方发送的图片聊天数据
    private val IMG_TYPE_MY = 3//我发送的图片聊天数据
    private val TIME_TYPE = 4//时间数据
    private val REDPACKET_TYPE_MY = 5//我发送的红包
    private val REDPACKET_TYPE_OTHER = 6//对方发送的红包
    private val REVEIVE_RED_PACKET = 7//收红包
    private val TRANSFER_TYPE_MY = 8//我发送的转账
    private val TRANSFER_TYPE_OTHER = 9//对方发送的转账
    private val TRANSFER_RECEIVED_TYPE_MY = 10//对方发送的转账
    private val TRANSFER_RECEIVED_TYPE_OTHER = 11//对方发送的转账
    private val VOICE_MY = 12//对方发送的转账
    private val VOICE_OTHER = 13//对方发送的转账
    private val SYSTEM_TYPE = 14//系统提示
    private val VIDEO_CHAT_TYPE_MY = 15//“我方”视频聊天
    private val VIDEO_CHAT_TYPE_OTHER = 16//“对方”视频聊天
    private val VOICE_CHAT_TYPE_MY = 17//“我方”语音聊天
    private val VOICE_CHAT_TYPE_OTHER = 18//“对方”语音聊天
    private val SHARE_TYPE_MY = 19//“我”转发
    private val SHARE_TYPE_OTHER = 20//“对方”转发
    private val CARD_TYPE_MY = 21//“我”发送的名片
    private val CARD_TYPE_OTHER = 22//“对方”发送的名片
    private val JOIN_GROUP_TYPE_MY = 23//“我”邀请加群
    private val JOIN_GROUP_TYPE_OTHER = 24//“对方邀请加群

    private val mOtherEntity = UserOperateUtil.getOtherSide()
    private val mMyEntity = UserOperateUtil.getMySelf()
    override fun getItemViewType(position: Int): Int {
        val entity = mList[position]
        if (entity.isComMsg){//接收到的消息
            when {
                entity.msgType == 0 -> {//文本消息
                    return TEXT_TYPE_OTHER
                }
                entity.msgType == 1 -> {
                    return IMG_TYPE_OTHER
                }
                entity.msgType == 2 -> {
                    return TIME_TYPE
                }
                entity.msgType == 3 -> {
                    return REDPACKET_TYPE_OTHER
                }
                entity.msgType == 4 -> {
                    return REVEIVE_RED_PACKET
                }
                entity.msgType == 5 -> {
                    return TRANSFER_TYPE_OTHER
                }
                entity.msgType == 6 -> {
                    return TRANSFER_RECEIVED_TYPE_OTHER
                }
                entity.msgType == 7 -> {
                    return VOICE_OTHER
                }
                entity.msgType == 8 -> {
                    return SYSTEM_TYPE
                }
                entity.msgType == 9 -> {
                    return VIDEO_CHAT_TYPE_OTHER
                }
                entity.msgType == 10 -> {
                    return VOICE_CHAT_TYPE_OTHER
                }
                entity.msgType == 11 -> {
                    return SHARE_TYPE_OTHER
                }
                entity.msgType == 12 -> {
                    return CARD_TYPE_OTHER
                }
                entity.msgType == 13 -> {
                    return JOIN_GROUP_TYPE_OTHER
                }
            }
        }else{//发送的消息
            when {
                entity.msgType == 0 -> {//文本消息
                    return TEXT_TYPE_MY
                }
                entity.msgType == 1 -> {
                    return IMG_TYPE_MY
                }
                entity.msgType == 2 -> {
                    return TIME_TYPE
                }
                entity.msgType == 3 -> {
                    return REDPACKET_TYPE_MY
                }
                entity.msgType == 4 -> {
                    return REVEIVE_RED_PACKET
                }
                entity.msgType == 5 -> {
                    return TRANSFER_TYPE_MY
                }
                entity.msgType == 6 -> {
                    return TRANSFER_RECEIVED_TYPE_MY
                }
                entity.msgType == 7 -> {
                    return VOICE_MY
                }
                entity.msgType == 8 -> {
                    return SYSTEM_TYPE
                }
                entity.msgType == 9 -> {
                    return VIDEO_CHAT_TYPE_MY
                }
                entity.msgType == 10 -> {
                    return VOICE_CHAT_TYPE_MY
                }
                entity.msgType == 11 -> {
                    return SHARE_TYPE_MY
                }
                entity.msgType == 12 -> {
                    return CARD_TYPE_MY
                }
                entity.msgType == 13 -> {
                    return JOIN_GROUP_TYPE_MY
                }
            }
        }
        return 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TEXT_TYPE_OTHER -> return OtherTextHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_screenshot_other_single_talk_text, parent, false))
            IMG_TYPE_OTHER -> return OtherImgHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_other_single_talk_img, parent, false))
            IMG_TYPE_MY -> return MyImgHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_my_single_talk_img, parent, false))
            TIME_TYPE -> return TimeHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_single_talk_time, parent, false))
            REDPACKET_TYPE_OTHER -> return OtherRedPacketHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_screenshot_other_single_talk_redpackage, parent, false))
            REDPACKET_TYPE_MY -> return MyRedPacketHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_my_redpackage, parent, false))
            REVEIVE_RED_PACKET -> return ReceiveRedPacketHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_receive, parent, false))
            TRANSFER_TYPE_OTHER -> return OtherTransferHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_other_transfer, parent, false))
            TRANSFER_TYPE_MY -> return MyTransferHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_my_transfer, parent, false))
            TRANSFER_RECEIVED_TYPE_MY -> return MyTransferReceivedHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_my_transfer_received, parent, false))
            TRANSFER_RECEIVED_TYPE_OTHER -> return OtherTransferReceivedHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_other_transfer_received, parent, false))
            VOICE_MY -> return MyVoiceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_my_voice, parent, false))
            VOICE_OTHER -> return OtherVoiceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_other_voice, parent, false))
            SYSTEM_TYPE -> return SystemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_system_time, parent, false))

            VIDEO_CHAT_TYPE_MY -> return MyVideoChatHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_my_video_chat, parent, false))
            VIDEO_CHAT_TYPE_OTHER -> return OtherVideoChatHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_other_video_chat, parent, false))
            VOICE_CHAT_TYPE_MY -> return MyVoiceChatHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_my_voice_chat, parent, false))
            VOICE_CHAT_TYPE_OTHER -> return OtherVoiceChatHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_other_voice_chat, parent, false))
            SHARE_TYPE_MY -> return MyShareHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_share_my, parent, false))
            SHARE_TYPE_OTHER -> return OtherShareHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_share_othert, parent, false))
            CARD_TYPE_MY -> return MyCardHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_card_my, parent, false))
            CARD_TYPE_OTHER -> return OtherCardHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_card_other, parent, false))

            JOIN_GROUP_TYPE_MY -> return MyJoinGroupHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_invite_join_group_my, parent, false))
            JOIN_GROUP_TYPE_OTHER -> return OtherJoinGroupHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_invite_join_group_other, parent, false))
        }
        return MyTextHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_screenshot_my_single_talk_text, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TEXT_TYPE_OTHER -> if (holder is OtherTextHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            TEXT_TYPE_MY -> if (holder is MyTextHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            IMG_TYPE_OTHER -> if (holder is OtherImgHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            IMG_TYPE_MY -> if (holder is MyImgHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            TIME_TYPE -> if (holder is TimeHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            REDPACKET_TYPE_OTHER -> if (holder is OtherRedPacketHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            REDPACKET_TYPE_MY -> if (holder is MyRedPacketHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            REVEIVE_RED_PACKET -> if (holder is ReceiveRedPacketHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            TRANSFER_TYPE_MY -> if (holder is MyTransferHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            TRANSFER_TYPE_OTHER -> if (holder is OtherTransferHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            TRANSFER_RECEIVED_TYPE_MY -> if (holder is MyTransferReceivedHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            TRANSFER_RECEIVED_TYPE_OTHER -> if (holder is OtherTransferReceivedHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            VOICE_MY -> if (holder is MyVoiceHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            VOICE_OTHER -> if (holder is OtherVoiceHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            SYSTEM_TYPE -> if (holder is SystemHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            VIDEO_CHAT_TYPE_MY -> if (holder is MyVideoChatHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            VIDEO_CHAT_TYPE_OTHER -> if (holder is OtherVideoChatHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            VOICE_CHAT_TYPE_MY -> if (holder is MyVoiceChatHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            VOICE_CHAT_TYPE_OTHER -> if (holder is OtherVoiceChatHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            SHARE_TYPE_MY -> if (holder is MyShareHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            SHARE_TYPE_OTHER -> if (holder is OtherShareHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            CARD_TYPE_MY -> if (holder is MyCardHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            CARD_TYPE_OTHER -> if (holder is OtherCardHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            JOIN_GROUP_TYPE_MY -> if (holder is MyJoinGroupHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            JOIN_GROUP_TYPE_OTHER -> if (holder is OtherJoinGroupHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            else -> { }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class OtherTextHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.sWechatScreenShotOtherAvatarIv)
        private val otherText: AutoLinkTextView = itemView.findViewById(R.id.sWechatScreenShotOtherContentTv)

        fun bind(entity: SingleTalkEntity){
            otherText.addAutoLinkMode(
                    AutoLinkMode.MODE_HASHTAG,
                    AutoLinkMode.MODE_PHONE,
                    AutoLinkMode.MODE_Number_7,
                    AutoLinkMode.MODE_URL,
                    AutoLinkMode.MODE_EMAIL,
                    AutoLinkMode.MODE_MENTION)
            otherText.setHashtagModeColor(ContextCompat.getColor(itemView.context, R.color.wechatAutoColor))
            otherText.setPhoneModeColor(ContextCompat.getColor(itemView.context, R.color.wechatAutoColor))
            otherText.setCustomModeColor(ContextCompat.getColor(itemView.context, R.color.wechatAutoColor))
            otherText.setMentionModeColor(ContextCompat.getColor(itemView.context, R.color.wechatAutoColor))
            otherText.setNumber7Color(ContextCompat.getColor(itemView.context, R.color.wechatAutoColor))
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            val msg = entity.msg
            val cs = EmojiWechatManager.parse(msg, otherText.textSize)
            otherText.setText(cs, TextView.BufferType.SPANNABLE)
        }
    }
    inner class MyTextHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.sWechatScreenShotMyAvatarIv)
        private val myText: AutoLinkTextView = itemView.findViewById(R.id.sWechatScreenShotMyContentTv)

        fun bind(entity: SingleTalkEntity){
            myText.addAutoLinkMode(
                    AutoLinkMode.MODE_HASHTAG,
                    AutoLinkMode.MODE_PHONE,
                    AutoLinkMode.MODE_Number_7,
                    AutoLinkMode.MODE_URL,
                    AutoLinkMode.MODE_EMAIL,
                    AutoLinkMode.MODE_MENTION)
            myText.setHashtagModeColor(ContextCompat.getColor(itemView.context, R.color.wechatAutoColor))
            myText.setPhoneModeColor(ContextCompat.getColor(itemView.context, R.color.wechatAutoColor))
            myText.setCustomModeColor(ContextCompat.getColor(itemView.context, R.color.wechatAutoColor))
            myText.setMentionModeColor(ContextCompat.getColor(itemView.context, R.color.wechatAutoColor))
            myText.setNumber7Color(ContextCompat.getColor(itemView.context, R.color.wechatAutoColor))
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            val msg = entity.msg
            val cs = EmojiWechatManager.parse(msg, myText.textSize)
            myText.setText(cs, TextView.BufferType.SPANNABLE)
        }
    }
    inner class OtherImgHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.wechatOtherAvatar)
        private val imageFile: RoundedImageView = itemView.findViewById(R.id.wechatOtherIv)

        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            GlideUtil.display(itemView.context, File(entity.filePath), imageFile)
        }
    }
    inner class MyImgHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.wechatMyAvatar)
        private val imageFile: RoundedImageView = itemView.findViewById(R.id.wechatMyIv)

        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            GlideUtil.display(itemView.context, File(entity.filePath), imageFile)
        }
    }
    inner class TimeHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val time: TextView = itemView.findViewById(R.id.wechatSingleTalkTimeTv)

        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(entity: SingleTalkEntity){
            if (mShowBg){
                time.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            }else{
                time.setTextColor(ContextCompat.getColor(itemView.context, R.color.wechatLightGray))
            }
            if ("12" == entity.timeType){
                time.text = WechatTimeUtil.getNewChat12Time(entity.time)
            }else{
                time.text = WechatTimeUtil.getNewChat24Time(entity.time)
            }
        }
    }
    inner class OtherRedPacketHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.wechatOtherAvatar)
        //        private val receiveLayout: LinearLayout = itemView.findViewById(R.id.receiveRedPacketLayout)
//        private val wechatReceiveRedPacket: TextView = itemView.findViewById(R.id.wechatReceiveRedPacket)//谁领了红包
        private val bubbleLayout: PercentRelativeLayout = itemView.findViewById(R.id.wechatBubbleLayout)
        private val tagIv: ImageView = itemView.findViewById(R.id.tagIv)
        private val msgTv: TextView = itemView.findViewById(R.id.wechatLeaveMessage)//留言
        private val statusTv: TextView = itemView.findViewById(R.id.wechatRedPackageStatusTv)//红包状态
        init {
            itemView.setOnClickListener{
                val entity = mList[adapterPosition]
                if (!entity.receive){//如果红包没有被领取，就领取
                    entity.receive = true
                    WechatScreenShotHelper(itemView.context).update(entity)
                }/*else{
                    ToastUtils.showShort(itemView.context, "我查看对方的的红包的详细页面")
                }*/
            }
        }
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            msgTv.text = entity.msg
            if (entity.receive){
                bubbleLayout.setBackgroundResource(R.drawable.bg_hongbao_d)//已领取
                GlideUtil.display(itemView.context, R.drawable.wechat_redpacket_received, tagIv)
                statusTv.visibility = View.VISIBLE
                statusTv.text = "已领取"
            }else{
                GlideUtil.display(itemView.context, R.drawable.wechat_redpacket_send, tagIv)
                statusTv.text = "领取红包"
                statusTv.visibility = View.GONE
            }
        }
    }
    inner class MyRedPacketHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.wechatMyAvatar)
        private val bubbleLayout: PercentRelativeLayout = itemView.findViewById(R.id.wechatBubbleLayout)
        private val tagIv: ImageView = itemView.findViewById(R.id.tagIv)
        private val msgTv: TextView = itemView.findViewById(R.id.wechatLeaveMessage)//留言
        private val statusTv: TextView = itemView.findViewById(R.id.wechatRedPackageStatusTv)//红包状态
        init {
            itemView.setOnClickListener{
                val entity = mList[adapterPosition]
                if (!entity.receive){//如果红包没有被领取，就领取
                    entity.receive = true
                    WechatScreenShotHelper(itemView.context).update(entity)
                }else{
                    ToastUtils.showShort(itemView.context, "对方查看我的红包的详情")
                }
            }
        }
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            msgTv.text = entity.msg
            if (entity.receive){
                bubbleLayout.setBackgroundResource(R.drawable.bg_hongbao_c)//已领取
                statusTv.text = "已领取"
                statusTv.visibility = View.VISIBLE
                GlideUtil.display(itemView.context, R.drawable.wechat_redpacket_received, tagIv)
            }else{
                statusTv.text = "查看红包"
                statusTv.visibility = View.GONE
                GlideUtil.display(itemView.context, R.drawable.wechat_redpacket_send, tagIv)
            }
        }
    }
    inner class OtherTransferHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.wechatOtherTransferAvatarIv)
        private val bubbleLayout: PercentRelativeLayout = itemView.findViewById(R.id.wechatOtherTransferLayout)
        private val tagIv: ImageView = itemView.findViewById(R.id.tagIv)
        private val msgTv: TextView = itemView.findViewById(R.id.wechatOtherTransferLeaveMessageTv)//留言
        private val moneyTv: TextView = itemView.findViewById(R.id.wechatOtherTransferMoneyTv)//红包状态
        init {
            itemView.setOnClickListener{
                val entity = mList[adapterPosition]
                if (!entity.receive){//如果红包没有被领取，就领取
                    entity.receive = true
                    WechatScreenShotHelper(itemView.context).update(entity)
                }
            }
        }
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            if (entity.receive){
//                receiveLayout.visibility = View.VISIBLE
                bubbleLayout.setBackgroundResource(R.drawable.bg_hongbao_d)//已领取
                GlideUtil.display(itemView.context, R.drawable.wechat_transfer_receive, tagIv)
                if (entity.msg.startsWith("转账给")){
                    msgTv.text = "已被领取"
                }else{
                    msgTv.text = StringUtils.insertFront(entity.msg, "已被领取-")
                }
            }else{
                msgTv.text = if (entity.msg.isNotBlank()) entity.msg else "转账给你"
                GlideUtil.display(itemView.context, R.drawable.wechat_transfer_send, tagIv)
            }
            moneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "¥")
        }
    }
    inner class MyTransferHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.wechatMyTransferAvatarIv)
        private val bubbleLayout: PercentRelativeLayout = itemView.findViewById(R.id.wechatMyTransferLayout)
        private val tagIv: ImageView = itemView.findViewById(R.id.tagIv)
        private val msgTv: TextView = itemView.findViewById(R.id.wechatMyTransferLeaveMessageTv)//留言
        private val moneyTv: TextView = itemView.findViewById(R.id.wechatMyTransferMoneyTv)//转账金额
        init {
            itemView.setOnClickListener{
                val entity = mList[adapterPosition]
//                entity.position = adapterPosition//确定条目在列表中的位置
                if (!entity.receive){//如果红包没有被领取，就领取
                    entity.receive = true
                    WechatScreenShotHelper(itemView.context).update(entity)
                }
            }
        }
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            msgTv.text = entity.msg
            if (entity.receive){
                bubbleLayout.setBackgroundResource(R.drawable.bg_hongbao_c)//已领取
                if (entity.msg.startsWith("转账给")){
                    msgTv.text = "已被领取"
                }else{
                    msgTv.text = StringUtils.insertFront(entity.msg, "已被领取-")
                }
                GlideUtil.display(itemView.context, R.drawable.wechat_transfer_receive, tagIv)
            }else{
                msgTv.text = if (entity.msg.isNotBlank()) entity.msg else StringUtils.insertFront(mOtherEntity.wechatUserNickName, "转账给")
                GlideUtil.display(itemView.context, R.drawable.wechat_transfer_send, tagIv)
            }
            moneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "¥")
        }
    }
    inner class ReceiveRedPacketHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val contentTv: TextView = itemView.findViewById(R.id.wechatReceiveRedPacketContentTv)//谁领了红包
        fun bind(entity: SingleTalkEntity){
            if (mShowBg){
                contentTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            }else{
                contentTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.wechatLightGray))
            }
            if (entity.wechatUserId == mMyEntity.wechatUserId){//如果是“我”发送的红包
                if (entity.receive){
                    contentTv.text = StringUtils.insertFrontAndBack(mOtherEntity.wechatUserNickName, "你领取了", "的")
                }
            }else{
                if (entity.receive){
                    contentTv.text = StringUtils.insertBack(mOtherEntity.wechatUserNickName, "领取了你的")
                }
            }
        }
    }
    inner class OtherTransferReceivedHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.wechatOtherTransferReceivedAvatarIv)
        private val moneyTv: TextView = itemView.findViewById(R.id.wechatOtherTransferMoneyTv)//红包状态
        init {
            itemView.setOnClickListener{
                val entity = mList[adapterPosition]
                if (!entity.receive){//如果转账没有被领取，就领取
                    entity.receive = true
                    WechatScreenShotHelper(itemView.context).update(entity)
                }
            }
        }
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            moneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "¥")
        }
    }
    inner class MyTransferReceivedHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.wechatMyTransferReceivedAvatarIv)
        private val moneyTv: TextView = itemView.findViewById(R.id.wechatMyTransferReceivedMoneyTv)//转账金额
        init {
            itemView.setOnClickListener{
                val entity = mList[adapterPosition]
                if (!entity.receive){//如果红包没有被领取，就领取
                    entity.receive = true
                    WechatScreenShotHelper(itemView.context).update(entity)
                }
            }
        }
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            moneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "¥")
        }
    }
    inner class OtherVoiceHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.mWechatVoiceOtherAvatarIv)
        private val voiceLayout: LinearLayout = itemView.findViewById(R.id.mWechatVoiceOtherLayout)
        private val lengthTv: TextView = itemView.findViewById(R.id.mWechatVoiceOtherLengthTv)//秒数
        private val alreadyIv: ImageView = itemView.findViewById(R.id.mWechatVoiceOtherAlreadyIv)//是否已经读过了
        private val transferTv: TextView = itemView.findViewById(R.id.mWechatOtherVoiceTransferToTextTv)//转换文字
        private val transferToTextLayout: PercentRelativeLayout = itemView.findViewById(R.id.mWechatOtherVoiceTransferToTextLayout)//转文字

        fun bind(entity: SingleTalkEntity){
            OtherUtil.setWechatVoiceLength(itemView.context, voiceLayout, entity.voiceLength)
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            lengthTv.text = StringUtils.insertBack(entity.voiceLength.toString(), "\"")
            if (entity.alreadyRead){
                alreadyIv.visibility = View.GONE
            }else{
                alreadyIv.visibility = View.VISIBLE
            }
            if (TextUtils.isEmpty(entity.voiceToText)){
                transferToTextLayout.visibility = View.GONE
            }else{
                transferTv.text = entity.voiceToText
                transferToTextLayout.visibility = View.VISIBLE
            }
        }
    }
    inner class MyVoiceHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.mWechatVoiceMyAvatarIv)
        private val voiceLayout: LinearLayout = itemView.findViewById(R.id.mWechatVoiceMyLayout)//长度
        private val lengthTv: TextView = itemView.findViewById(R.id.mWechatVoiceMyLengthTv)//秒数
        private val transferTv: TextView = itemView.findViewById(R.id.mWechatMyVoiceTransferToTextTv)//转换文字
        private val transferToTextLayout: PercentRelativeLayout = itemView.findViewById(R.id.mWechatMyVoiceTransferToTextLayout)//转文字
        fun bind(entity: SingleTalkEntity){
            OtherUtil.setWechatVoiceLength(itemView.context, voiceLayout, entity.voiceLength)
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            lengthTv.text = StringUtils.insertBack(entity.voiceLength.toString(), "\"")
            if (TextUtils.isEmpty(entity.voiceToText)){
                transferToTextLayout.visibility = View.GONE
            }else{
                transferTv.text = entity.voiceToText
                transferToTextLayout.visibility = View.VISIBLE
            }
        }
    }
    inner class SystemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val systemContent: TextView = itemView.findViewById(R.id.wechatSystemTv)
        fun bind(entity: SingleTalkEntity){
            if (mShowBg){
                systemContent.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            }else{
                systemContent.setTextColor(ContextCompat.getColor(itemView.context, R.color.wechatLightGray))
            }
            val spannableString: SpannableStringBuilder
            if (entity.msg.contains("重新编辑")){
                spannableString = SpannableStringBuilder(entity.msg)
                spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#576b95")), spannableString.length - 4, spannableString.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                systemContent.text = spannableString
            }else{
                systemContent.text = entity.msg
            }
        }
    }

    inner class OtherVideoChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.sWechatVideoOtherAvatarIv)
        private val msgTv: TextView = itemView.findViewById(R.id.sWechatVideoOtherMsgTv)
        private val alreadyIv: ImageView = itemView.findViewById(R.id.sWechatVoiceOtherAlreadyIv)//是否已经读过了

        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            msgTv.text = entity.msg
            if (entity.alreadyRead){
                alreadyIv.visibility = View.GONE
            }else{
                alreadyIv.visibility = View.VISIBLE
            }
        }
    }
    inner class MyVideoChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.sWechatVideoMyAvatarIv)
        private val msgTv: TextView = itemView.findViewById(R.id.sWechatVideoMyMsgTv)
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            msgTv.text = entity.msg
        }
    }
    inner class OtherVoiceChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.sWechatVideoOtherAvatarIv)
        private val msgTv: TextView = itemView.findViewById(R.id.sWechatVideoOtherMsgTv)
        private val alreadyIv: ImageView = itemView.findViewById(R.id.sWechatVoiceOtherAlreadyIv)//是否已经读过了

        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            msgTv.text = entity.msg
            if (entity.alreadyRead){
                alreadyIv.visibility = View.GONE
            }else{
                alreadyIv.visibility = View.VISIBLE
            }
        }
    }
    inner class MyVoiceChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.sWechatVideoMyAvatarIv)
        private val msgTv: TextView = itemView.findViewById(R.id.sWechatVideoMyMsgTv)
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            msgTv.text = entity.msg
        }
    }
    inner class OtherShareHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatarIv: ImageView = itemView.findViewById(R.id.sWechatShareOtherAvatarIv)
        private val titleTv: TextView = itemView.findViewById(R.id.sWechatShareTitleTv)
        private val contentTv: TextView = itemView.findViewById(R.id.sWechatShareContentTv)
        private val iconIv: ImageView = itemView.findViewById(R.id.sWechatShareIconIv)

        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), avatarIv)
            titleTv.text = entity.msg
            contentTv.text = entity.content
            if (TextUtils.isEmpty(entity.filePath)){
                iconIv.setImageResource(R.drawable.wechat_share_default)
            }else{
                GlideUtil.display(itemView.context, File(entity.filePath), iconIv)
            }
        }
    }
    inner class MyShareHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatarIv: ImageView = itemView.findViewById(R.id.sWechatShareMyAvatarIv)
        private val titleTv: TextView = itemView.findViewById(R.id.sWechatShareTitleTv)
        private val contentTv: TextView = itemView.findViewById(R.id.sWechatShareContentTv)
        private val iconIv: ImageView = itemView.findViewById(R.id.sWechatShareIconIv)
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), avatarIv)
            titleTv.text = entity.msg
            contentTv.text = entity.content
            if (TextUtils.isEmpty(entity.filePath)){
                iconIv.setImageResource(R.drawable.wechat_share_default)
            }else{
                GlideUtil.display(itemView.context, File(entity.filePath), iconIv)
            }

        }
    }
    inner class OtherCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatarIv: ImageView = itemView.findViewById(R.id.sWechatCardMyAvatarIv)
        private val cardAvatar: ImageView = itemView.findViewById(R.id.sWechatCardAvatarIv)
        private val nickName: TextView = itemView.findViewById(R.id.sWechatCardNickNameIv)

        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), avatarIv)
            GlideUtil.displayHead(itemView.context, entity.card.getAvatarFile(), cardAvatar)
            nickName.text = entity.card.wechatUserNickName
        }
    }
    inner class MyCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatarIv: ImageView = itemView.findViewById(R.id.sWechatCardMyAvatarIv)
        private val cardAvatar: ImageView = itemView.findViewById(R.id.sWechatCardAvatarIv)
        private val nickName: TextView = itemView.findViewById(R.id.sWechatCardNickNameIv)
        private val wxNumber: TextView = itemView.findViewById(R.id.sWechatCardWxNumberIv)
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), avatarIv)
            GlideUtil.displayHead(itemView.context, entity.card.getAvatarFile(), cardAvatar)
            nickName.text = entity.card.wechatUserNickName
            wxNumber.text = entity.card.wxNumber
        }
    }
    inner class OtherJoinGroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatarIv: ImageView = itemView.findViewById(R.id.sGroupOtherAvatarTv)
        private val groupIcon: ImageView = itemView.findViewById(R.id.sGroupHeaderIv)
        private val msgTv: TextView = itemView.findViewById(R.id.sGroupMsgTv)

        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), avatarIv)
            groupIcon.setImageBitmap(BitmapFactory.decodeByteArray(entity.groupInfo.groupHeaderByte, 0, entity.groupInfo.groupHeaderByte.size))
            msgTv.text = "“" + entity.groupInfo.wechatUserNickName + "”邀请你加入群聊" + entity.groupInfo.groupName + "，进入可查看详情。"
        }
    }
    inner class MyJoinGroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatarIv: ImageView = itemView.findViewById(R.id.sGroupMyAvatarTv)
        private val groupIcon: ImageView = itemView.findViewById(R.id.sGroupHeaderIv)
        private val msgTv: TextView = itemView.findViewById(R.id.sGroupMsgTv)
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), avatarIv)
            groupIcon.setImageBitmap(BitmapFactory.decodeByteArray(entity.groupInfo.groupHeaderByte, 0, entity.groupInfo.groupHeaderByte.size))
            msgTv.text = "“" + entity.groupInfo.wechatUserNickName + "”邀请你加入群聊" + entity.groupInfo.groupName + "，进入可查看详情。"
        }
    }
}