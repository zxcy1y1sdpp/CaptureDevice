package app.jietuqi.cn.wechat.simulator.adapter
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
import app.jietuqi.cn.ResourceHelper
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.FileEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.ui.wechatscreenshot.widget.EmojiWechatManager
import app.jietuqi.cn.util.*
import app.jietuqi.cn.widget.autolink.AutoLinkMode
import app.jietuqi.cn.widget.autolink.AutoLinkTextView
import com.makeramen.roundedimageview.RoundedImageView
import com.zhy.android.percent.support.PercentRelativeLayout
import java.io.File


/**
 * 作者： liuyuanbo on 2018/10/10 17:50.
 * 时间： 2018/10/10 17:50
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class WechatSimulatorPreviewAdapter(val mList: ArrayList<WechatScreenShotEntity>, val mListener: WechatOperateListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
    private val TRANSFER_RECEIVED_TYPE_MY = 10//我领取对方转账
    private val TRANSFER_RECEIVED_TYPE_OTHER = 11//对方领取我的转账
    private val VOICE_MY = 12//我发送的语音消息
    private val VOICE_OTHER = 13//对方发送的语音消息
    private val SYSTEM_TYPE = 14//系统提示
    private val VIDEO_CHAT_TYPE_MY = 15//“我方”视频聊天
    private val VIDEO_CHAT_TYPE_OTHER = 16//“对方”视频聊天
    private val VOICE_CHAT_TYPE_MY = 17//“我方”语音聊天
    private val VOICE_CHAT_TYPE_OTHER = 18//“对方”语音聊天
    private val JOIN_GROUP_TYPE_MY = 23//“我”邀请加群
    private val JOIN_GROUP_TYPE_OTHER = 24//“对方邀请加群
    private val FILE_TYPE_MY = 29//“我”发送的文件消息
    private val FILE_TYPE_OTHER = 30//“对方”发送的文件消息
    /**
     * 默认是自己发送
     */
    private var mIsComMsg: Boolean = false
    /**
     * 是否显示聊天背景
     */
    private var mShowChatBg: Boolean = false
    var mOtherEntity: WechatUserEntity = WechatUserEntity()
    fun changeRole(isComMsg: Boolean){
        mIsComMsg =  isComMsg
    }
    fun showChatBg(showBg: Boolean){
        mShowChatBg = showBg
    }
    fun setOtherSide(entity: WechatUserEntity){
        mOtherEntity = entity
    }
    private val mMyEntity = UserOperateUtil.getWechatSimulatorMySelf()
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
                entity.msgType == 13 -> {
                    return JOIN_GROUP_TYPE_OTHER
                }
                entity.msgType == 16 -> {
                    return FILE_TYPE_OTHER
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
                entity.msgType == 13 -> {
                    return JOIN_GROUP_TYPE_MY
                }
                entity.msgType == 16 -> {
                    return FILE_TYPE_MY
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
            JOIN_GROUP_TYPE_MY -> return MyJoinGroupHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_my_invite_join_group, parent, false))
            JOIN_GROUP_TYPE_OTHER -> return OtherJoinGroupHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_other_invite_join_group, parent, false))
            FILE_TYPE_MY -> return MyFileHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_my_file, parent, false))
            FILE_TYPE_OTHER -> return OtherFileHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_other_file, parent, false))
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
            JOIN_GROUP_TYPE_MY -> if (holder is MyJoinGroupHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            JOIN_GROUP_TYPE_OTHER -> if (holder is OtherJoinGroupHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            FILE_TYPE_MY -> if (holder is MyFileHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position].fileEntity)
            }
            FILE_TYPE_OTHER -> if (holder is OtherFileHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position].fileEntity)
            }
            else -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class OtherTextHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.sWechatScreenShotOtherAvatarIv)
        private val otherText: AutoLinkTextView = itemView.findViewById(R.id.sWechatScreenShotOtherContentTv)

        fun bind(entity: WechatScreenShotEntity){
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

        fun bind(entity: WechatScreenShotEntity){
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

        fun bind(entity: WechatScreenShotEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            GlideUtil.display(itemView.context, File(entity.filePath), imageFile)
        }
    }
    inner class MyImgHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.wechatMyAvatar)
        private val imageFile: RoundedImageView = itemView.findViewById(R.id.wechatMyIv)

        fun bind(entity: WechatScreenShotEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            GlideUtil.display(itemView.context, File(entity.filePath), imageFile)
        }
    }
    inner class TimeHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val time: TextView = itemView.findViewById(R.id.wechatSingleTalkTimeTv)

        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(entity: WechatScreenShotEntity){
            if (mShowChatBg){
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
        private val bubbleLayout: PercentRelativeLayout = itemView.findViewById(R.id.wechatBubbleLayout)
        private val tagIv: ImageView = itemView.findViewById(R.id.tagIv)
        private val msgTv: TextView = itemView.findViewById(R.id.wechatLeaveMessage)//留言
        private val statusTv: TextView = itemView.findViewById(R.id.wechatRedPackageStatusTv)//红包状态
        init {
            bubbleLayout.setOnClickListener{
                mListener.closeBottomMenu()
                val entity = mList[adapterPosition]
                val senderEntity = WechatUserEntity()//发送人
                if (!mIsComMsg){//如果当前操作对象是“我”，如果没有领取红包，就领取，如果已经领取就是查看对方红包详情
                    if (entity.receive){//如果被领取了，就是查看详情
                        senderEntity.money = entity.money
                        senderEntity.wechatUserId = entity.wechatUserId
                        senderEntity.wechatUserNickName = mOtherEntity.wechatUserNickName
                        senderEntity.avatarFile = mOtherEntity.avatarFile
                        senderEntity.wechatUserAvatar = mOtherEntity.wechatUserAvatar
                        senderEntity.resourceName = mOtherEntity.resourceName
                        senderEntity.avatarInt = mOtherEntity.avatarInt
                        senderEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
                        LaunchUtil.startWechatSimulatorReceiveRedPacketActivity(itemView.context, senderEntity)
                    }else{//“对方操作“对方”未被领取的红包”
                        entity.receive = true
                        mListener.meTakeOtherRedPacket(entity, adapterPosition)
                    }
                }else{//如果当前操作对象是“对方”，查看详情
                    if (entity.receive){//如果被领取了，就是查看详情
                        senderEntity.money = entity.money
                        senderEntity.wechatUserId = entity.wechatUserId
                        senderEntity.wechatUserNickName = mOtherEntity.wechatUserNickName
                        senderEntity.avatarFile = mOtherEntity.avatarFile
                        senderEntity.wechatUserAvatar = mOtherEntity.wechatUserAvatar
                        senderEntity.resourceName = mOtherEntity.resourceName
                        senderEntity.avatarInt = mOtherEntity.avatarInt
                        senderEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
                        LaunchUtil.startWechatSimulatorReceiveRedPacketActivity(itemView.context, senderEntity)
                    }else{//“我操作”“我未被领取的红包”
                        entity.receive = true
                        mListener.meTakeOtherRedPacket(entity, adapterPosition)
                    }
                }
            }
        }
        fun bind(entity: WechatScreenShotEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            msgTv.text = entity.msg
            if (entity.receive){
                bubbleLayout.setBackgroundResource(R.drawable.bg_hongbao_d)//已领取
                GlideUtil.display(itemView.context, R.drawable.wechat_redpacket_received, tagIv)
                statusTv.visibility = View.VISIBLE
                statusTv.text = "已领取"
            }else{
                bubbleLayout.setBackgroundResource(R.drawable.bg_hongbao_b)//未领取
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
            bubbleLayout.setOnClickListener{
                mListener.closeBottomMenu()
                val entity = mList[adapterPosition]
                val senderEntity = WechatUserEntity()//发送人
                val receiveEntity = WechatUserEntity()//接收人
                if (!mIsComMsg){//如果当前操作对象是“我”，如果没有领取红包，就领取，如果已经领取就是查看详情
                    if (entity.receive){//如果被领取了，就是查看详情
                        senderEntity.money = entity.money
                        senderEntity.wechatUserId = entity.wechatUserId
                        senderEntity.wechatUserNickName = mMyEntity.wechatUserNickName
                        senderEntity.avatarFile = mMyEntity.avatarFile
                        senderEntity.wechatUserAvatar = mMyEntity.wechatUserAvatar
                        senderEntity.avatarInt = mMyEntity.avatarInt
                        senderEntity.resourceName = mMyEntity.resourceName
                        senderEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
                        receiveEntity.money = entity.money
                        receiveEntity.wechatUserId = entity.wechatUserId
                        receiveEntity.wechatUserNickName = mOtherEntity.wechatUserNickName
                        receiveEntity.avatarFile = mOtherEntity.avatarFile
                        receiveEntity.wechatUserAvatar = mOtherEntity.wechatUserAvatar
                        receiveEntity.avatarInt = mOtherEntity.avatarInt
                        receiveEntity.resourceName = mOtherEntity.resourceName
                        receiveEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
                        LaunchUtil.startWechatSimulatorSendRedPacketActivity(itemView.context, senderEntity, receiveEntity)
                    }else{//“我操作”“我未被领取的红包”
                        ToastUtils.showShort(itemView.context, "请切换到 [对方] 进行红包领取")
                    }
                }else{//如果当前操作对象是“对方”，查看详情
                    if (entity.receive){//如果被领取了，就是查看详情
                        senderEntity.money = entity.money
                        senderEntity.wechatUserId = entity.wechatUserId
                        senderEntity.wechatUserNickName = mMyEntity.wechatUserNickName
                        senderEntity.avatarFile = mMyEntity.avatarFile
                        senderEntity.wechatUserAvatar = mMyEntity.wechatUserAvatar
                        senderEntity.resourceName = mMyEntity.resourceName
                        senderEntity.avatarInt = mMyEntity.avatarInt
                        senderEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
                        receiveEntity.money = entity.money
                        receiveEntity.wechatUserId = entity.wechatUserId
                        receiveEntity.wechatUserNickName = mOtherEntity.wechatUserNickName
                        receiveEntity.avatarFile = mOtherEntity.avatarFile
                        receiveEntity.wechatUserAvatar = mOtherEntity.wechatUserAvatar
                        receiveEntity.resourceName = mOtherEntity.resourceName
                        receiveEntity.avatarInt = mOtherEntity.avatarInt
                        receiveEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
                        LaunchUtil.startWechatSimulatorSendRedPacketActivity(itemView.context, senderEntity, receiveEntity)
                    }else{//“我操作”“我未被领取的红包”
                        entity.receive = true
                        mListener.otherTakeMyRedPacket(entity, adapterPosition)
                    }
                }
            }
        }
        fun bind(entity: WechatScreenShotEntity){
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
                bubbleLayout.setBackgroundResource(R.drawable.bg_hongbao_a)//已领取
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
            bubbleLayout.setOnClickListener{
                mListener.closeBottomMenu()
                val entity = mList[adapterPosition]
                entity.itemPosition = adapterPosition
                if (!mIsComMsg){//如果当前操作对象是“我”，如果没有领取转账，就领取，如果已经领取就是查看对方转账详情
                    entity.type = 0
                    if (entity.receive){//如果被领取了，就是查看详情
                        entity.transferType = "已收钱"
                        entity.money = entity.money
                        LaunchUtil.startWechatTransferDetailActivity(itemView.context, entity, true)
                    }else{//“我操作”对方“未被领取的转账
                        entity.transferType = "待收款"
                        entity.money = entity.money
                        LaunchUtil.startWechatTransferDetailActivity(itemView.context, entity, true)
                    }
                }else{//如果当前操作对象是“对方”
                    if (entity.receive){//如果被领取了，就是查看详情
                        entity.type = 1
                        entity.transferType = "已收钱"
                        entity.money = entity.money
                        LaunchUtil.startWechatTransferDetailActivity(itemView.context, entity, true)
                    }else{//“对方"操作”对方“未被领取的转账
                        entity.type = 0
                        entity.transferType = "待收款"
                        entity.money = entity.money
                        LaunchUtil.startWechatTransferDetailActivity(itemView.context, entity, true)
                    }
                }
            }
        }
        fun bind(entity: WechatScreenShotEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            if (entity.receive){
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
                bubbleLayout.setBackgroundResource(R.drawable.bg_hongbao_b)//未领取
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
            bubbleLayout.setOnClickListener{
                mListener.closeBottomMenu()
                val entity = mList[adapterPosition]
                entity.itemPosition = adapterPosition
                if (!mIsComMsg){//如果当前操作对象是“我”，如果没有领取转账，
                    entity.type = 1
                    if (entity.receive){//如果被领取了，就是查看详情
                        entity.transferType = "已收钱"
                        entity.money = entity.money
                        LaunchUtil.startWechatTransferDetailActivity(itemView.context, entity, true)
                    }else{//“我操作”我“未被领取的转账”
                        entity.transferType = "待收款"
                        entity.outTime = TimeUtil.stampToDateWithS(entity.transferOutTime)
                        entity.receiveTime = TimeUtil.stampToDateWithS(entity.transferReceiveTime)
                        entity.money = entity.money
                        LaunchUtil.startWechatTransferDetailActivity(itemView.context, entity, true)
                    }
                }else{//如果当前操作对象是“对方”
                    entity.type = 1
                    if (entity.receive){//对方操作我被领取的转账
                        entity.transferType = "已收钱"
                        entity.money = entity.money
                        LaunchUtil.startWechatTransferDetailActivity(itemView.context, entity, true)
                    }else{//“对方"操作”我“未被领取的转账
                        entity.transferType = "已收钱"
                        entity.receive = true
                        mListener.myTransferWasReceive(entity, adapterPosition)
                        notifyItemChanged(adapterPosition)
                    }
                }
            }
        }
        fun bind(entity: WechatScreenShotEntity){
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
                bubbleLayout.setBackgroundResource(R.drawable.bg_hongbao_a)//已领取
            }
            moneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "¥")
        }
    }
    inner class ReceiveRedPacketHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val contentTv: TextView = itemView.findViewById(R.id.wechatReceiveRedPacketContentTv)//谁领了红包
        fun bind(entity: WechatScreenShotEntity){
            if (mShowChatBg){
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
        private val bubbleLayout: PercentRelativeLayout = itemView.findViewById(R.id.wechatOtherTransferReceivedLayout)//红包状态
        init {
            bubbleLayout.setOnClickListener{
                mListener.closeBottomMenu()
                val entity = mList[adapterPosition]
                entity.itemPosition = adapterPosition
                if (!mIsComMsg){//如果当前操作对象是“我”，如果没有领取转账，
                    entity.type = 1
                    entity.transferType = "已收钱"
                    entity.outTime = TimeUtil.stampToDateWithS(entity.transferOutTime)
                    entity.receiveTime = TimeUtil.stampToDateWithS(entity.transferReceiveTime)
                    entity.money = entity.money
                    LaunchUtil.startWechatTransferDetailActivity(itemView.context, entity, true)
                }else{//如果当前操作对象是“对方”
                    entity.type = 0
                    entity.transferType = "已收钱"
                    entity.outTime = TimeUtil.stampToDateWithS(entity.transferOutTime)
                    entity.receiveTime = TimeUtil.stampToDateWithS(entity.transferReceiveTime)
                    entity.money = entity.money
                    LaunchUtil.startWechatTransferDetailActivity(itemView.context, entity, true)
                }
            }
        }
        fun bind(entity: WechatScreenShotEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            moneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "¥")
        }
    }
    inner class MyTransferReceivedHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.wechatMyTransferReceivedAvatarIv)
        private val moneyTv: TextView = itemView.findViewById(R.id.wechatMyTransferReceivedMoneyTv)//转账金额
        private val bubbleLayout: PercentRelativeLayout = itemView.findViewById(R.id.wechatMyTransferReceivedLayout)//红包状态
        init {
            bubbleLayout.setOnClickListener{
                mListener.closeBottomMenu()
                val entity = mList[adapterPosition]
                entity.itemPosition = adapterPosition
                if (!mIsComMsg){//如果当前操作对象是“我”，如果没有领取转账，
                    entity.type = 0
                    entity.transferType = "已收钱"
                    entity.outTime = TimeUtil.stampToDateWithS(entity.transferOutTime)
                    entity.receiveTime = TimeUtil.stampToDateWithS(entity.transferReceiveTime)
                    entity.money = entity.money
                    LaunchUtil.startWechatTransferDetailActivity(itemView.context, entity, true)
                }else{//如果当前操作对象是“对方”
                    entity.type = 1
                    entity.transferType = "已收钱"
                    entity.outTime = TimeUtil.stampToDateWithS(entity.transferOutTime)
                    entity.receiveTime = TimeUtil.stampToDateWithS(entity.transferReceiveTime)
                    entity.money = entity.money
                    LaunchUtil.startWechatTransferDetailActivity(itemView.context, entity, true)
                }
            }
        }
        fun bind(entity: WechatScreenShotEntity){
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

        fun bind(entity: WechatScreenShotEntity){
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
                transferTv.text = StringUtils.insertBack(entity.voiceToText, "。")
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
        fun bind(entity: WechatScreenShotEntity){
            OtherUtil.setWechatVoiceLength(itemView.context, voiceLayout, entity.voiceLength)
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            lengthTv.text = StringUtils.insertBack(entity.voiceLength.toString(), "\"")
            if (TextUtils.isEmpty(entity.voiceToText)){
                transferToTextLayout.visibility = View.GONE
            }else{
                transferTv.text = StringUtils.insertBack(entity.voiceToText, "。")
                transferToTextLayout.visibility = View.VISIBLE
            }
        }
    }

    inner class SystemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val systemContent: TextView = itemView.findViewById(R.id.wechatSystemTv)
        fun bind(entity: WechatScreenShotEntity){
            if (mShowChatBg){
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

        fun bind(entity: WechatScreenShotEntity){
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
        fun bind(entity: WechatScreenShotEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            msgTv.text = entity.msg
        }
    }
    inner class OtherVoiceChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.sWechatVideoOtherAvatarIv)
        private val msgTv: TextView = itemView.findViewById(R.id.sWechatVideoOtherMsgTv)
        private val alreadyIv: ImageView = itemView.findViewById(R.id.sWechatVoiceOtherAlreadyIv)//是否已经读过了

        fun bind(entity: WechatScreenShotEntity){
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
        fun bind(entity: WechatScreenShotEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            msgTv.text = entity.msg
        }
    }

    inner class OtherFileHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatarIv: ImageView = itemView.findViewById(R.id.sFileOtherAvatarTv)
        private val fileIconIv: ImageView = itemView.findViewById(R.id.sFileIconIv)
        private val title: TextView = itemView.findViewById(R.id.sFileTitleTv)
        private val size: TextView = itemView.findViewById(R.id.sFileContentTv)

        fun bind(entity: FileEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), avatarIv)
            GlideUtil.displayHead(itemView.context, ResourceHelper.getAppIconId(entity.icon), fileIconIv)
            title.text = StringUtils.insertBack(entity.title, entity.suffix)
            size.text = StringUtils.insertBack(entity.size, entity.unit)
        }
    }
    inner class MyFileHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatarIv: ImageView = itemView.findViewById(R.id.sFileMyAvatarTv)
        private val fileIconIv: ImageView = itemView.findViewById(R.id.sFileIconIv)
        private val title: TextView = itemView.findViewById(R.id.sFileTitleTv)
        private val size: TextView = itemView.findViewById(R.id.sFileContentTv)
        fun bind(entity: FileEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), avatarIv)
            GlideUtil.displayHead(itemView.context, ResourceHelper.getAppIconId(entity.icon), fileIconIv)
            title.text = StringUtils.insertBack(entity.title, entity.suffix)
            size.text = StringUtils.insertBack(entity.size, entity.unit)
        }
    }
    inner class OtherJoinGroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatarIv: ImageView = itemView.findViewById(R.id.sGroupOtherAvatarTv)
        private val groupIcon: ImageView = itemView.findViewById(R.id.sGroupHeaderIv)
        private val msgTv: TextView = itemView.findViewById(R.id.sGroupMsgTv)

        fun bind(entity: WechatScreenShotEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), avatarIv)
            groupIcon.setImageBitmap(BitmapFactory.decodeByteArray(entity.groupInfo.groupHeaderByte, 0, entity.groupInfo.groupHeaderByte.size))
            msgTv.text = "“" + entity.groupInfo.wechatUserNickName + "”邀请你加入群聊" + entity.groupInfo.groupName + "，进入可查看详情。"
        }
    }
    inner class MyJoinGroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatarIv: ImageView = itemView.findViewById(R.id.sGroupMyAvatarTv)
        private val groupIcon: ImageView = itemView.findViewById(R.id.sGroupHeaderIv)
        private val msgTv: TextView = itemView.findViewById(R.id.sGroupMsgTv)
        fun bind(entity: WechatScreenShotEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), avatarIv)
            groupIcon.setImageBitmap(BitmapFactory.decodeByteArray(entity.groupInfo.groupHeaderByte, 0, entity.groupInfo.groupHeaderByte.size))
            msgTv.text = "“" + entity.groupInfo.wechatUserNickName + "”邀请你加入群聊" + entity.groupInfo.groupName + "，进入可查看详情。"
        }
    }
    interface WechatOperateListener{
        fun otherTakeMyRedPacket(entity: WechatScreenShotEntity, position: Int)//对方领取我的红包
        fun meTakeOtherRedPacket(entity: WechatScreenShotEntity, position: Int)//我领取对方的红包
        fun myTransferWasReceive(entity: WechatScreenShotEntity, position: Int)//我领取对方的红包
        fun closeBottomMenu()//关闭底部菜单
    }
}