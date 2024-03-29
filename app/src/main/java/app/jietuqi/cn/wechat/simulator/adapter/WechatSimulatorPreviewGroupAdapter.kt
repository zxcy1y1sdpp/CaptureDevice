package app.jietuqi.cn.wechat.simulator.adapter
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

class WechatSimulatorPreviewGroupAdapter(val mList: MutableList<WechatScreenShotEntity>, val mListener: WechatOperateListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
    private val VOICE_MY = 12//我发送的语音消息
    private val VOICE_OTHER = 13//对方发送的语音消息
    private val SYSTEM_TYPE = 14//系统提示

    private val FILE_TYPE_MY = 29//“我”发送的文件消息
    private val FILE_TYPE_OTHER = 30//“对方”发送的文件消息
    /**
     * 是否显示群昵称
     */
    private var mShowNickName = false
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
    fun showNickName(showNickName: Boolean){
        mShowNickName = showNickName
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
                entity.msgType == 7 -> {
                    return VOICE_OTHER
                }
                entity.msgType == 8 -> {
                    return SYSTEM_TYPE
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
                entity.msgType == 7 -> {
                    return VOICE_MY
                }
                entity.msgType == 8 -> {
                    return SYSTEM_TYPE
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
            VOICE_MY -> return MyVoiceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_my_voice, parent, false))
            VOICE_OTHER -> return OtherVoiceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_other_voice, parent, false))
            SYSTEM_TYPE -> return SystemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_system_time, parent, false))

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
            VOICE_MY -> if (holder is MyVoiceHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            VOICE_OTHER -> if (holder is OtherVoiceHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            SYSTEM_TYPE -> if (holder is SystemHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            FILE_TYPE_MY -> if (holder is MyFileHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position].fileEntity)
            }
            FILE_TYPE_OTHER -> if (holder is OtherFileHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position].fileEntity)
            }
        }
    }

    override fun getItemCount() = mList.size

    inner class OtherTextHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.sWechatScreenShotOtherAvatarIv)
        private val otherText: AutoLinkTextView = itemView.findViewById(R.id.sWechatScreenShotOtherContentTv)
        private val nickName: TextView = itemView.findViewById(R.id.sWechatNickNameTv)

        init {
            otherAvatar.setOnClickListener{
                val entity = mList[adapterPosition]
                mListener.changeUserInfo(entity)
            }
        }
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
            GlideUtil.displayHead(itemView.context, entity.avatar, otherAvatar)
            if (mShowNickName){
                nickName.text = entity.wechatUserNickName
                nickName.visibility = View.VISIBLE
            }else{
                nickName.visibility = View.GONE
            }
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
        private val nickName: TextView = itemView.findViewById(R.id.sWechatNickNameTv)
        init {
            otherAvatar.setOnClickListener{
                val entity = mList[adapterPosition]
                mListener.changeUserInfo(entity)
            }
        }
        fun bind(entity: WechatScreenShotEntity){
            if (mShowNickName){
                nickName.text = entity.wechatUserNickName
                nickName.visibility = View.VISIBLE
            }else{
                nickName.visibility = View.GONE
            }
            GlideUtil.displayHead(itemView.context, entity.avatar, otherAvatar)
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
        private val nickName: TextView = itemView.findViewById(R.id.sWechatNickNameTv)
        init {
            otherAvatar.setOnClickListener{
                val entity = mList[adapterPosition]
                mListener.changeUserInfo(entity)
            }
            bubbleLayout.setOnClickListener{
                mListener.closeBottomMenu()
                val entity = mList[adapterPosition]
                if (entity.receive){//如果被领取了，就是查看详情
                    mListener.checkRedpacketDetails(entity, adapterPosition)
                }else{//“对方操作“对方”未被领取的红包”
//                    entity.receive = true
                    mListener.meTakeOtherRedPacket(entity, adapterPosition)
                }
            }
        }
        fun bind(entity: WechatScreenShotEntity){
            if (mShowNickName){
                nickName.text = entity.wechatUserNickName
                nickName.visibility = View.VISIBLE
            }else{
                nickName.visibility = View.GONE
            }
            GlideUtil.displayHead(itemView.context, entity.avatar, otherAvatar)
            msgTv.text = entity.msg
            if (entity.receive){
                if (entity.joinReceiveRedPacket){
                    statusTv.text = "已领取"
                }else{
                    statusTv.text = "已被领完"
                }
                bubbleLayout.setBackgroundResource(R.drawable.bg_hongbao_d)//已领取
                GlideUtil.display(itemView.context, R.drawable.wechat_redpacket_received, tagIv)
                statusTv.visibility = View.VISIBLE
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
                mListener?.closeBottomMenu()
                val entity = mList[adapterPosition]
                if (entity.receive) {//如果被领取了，就是查看详情
                    mListener.checkRedpacketDetails(entity, adapterPosition)
//                    LaunchUtil.startWechatSimulatorGroupRedPacketActivity(itemView.context, entity)
                }else{
                    mListener.otherTakeMyRedPacket(entity, adapterPosition)
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
    inner class ReceiveRedPacketHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val contentTv: TextView = itemView.findViewById(R.id.wechatReceiveRedPacketContentTv)//谁领了红包
        private val lastTv: TextView = itemView.findViewById(R.id.wechatReceiveRedPacketLastContentTv)//最后一个领红包的人
        fun bind(entity: WechatScreenShotEntity){
            if (mShowChatBg){
                contentTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                lastTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
            }else{
                contentTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.wechatLightGray))
                lastTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.wechatLightGray))
            }
            if (entity.groupRedPacketInfo.wechatUserId == mMyEntity.wechatUserId){//自己发送的红包
                if (entity.wechatUserId == mMyEntity.wechatUserId){//自己领取自己发送的红包
                    contentTv.text = "你领取了自己发的"
                }else{
                    if (entity.lastReceive){
                        lastTv.visibility = View.VISIBLE
                        contentTv.text = StringUtils.insertBack(entity.wechatUserNickName, "领取了你的")
                    }else{
                        lastTv.visibility = View.GONE
                        contentTv.text = StringUtils.insertBack(entity.wechatUserNickName, "领取了你的")
                    }

                }
            }else{//对方发送的红包
                lastTv.visibility = View.GONE
                contentTv.text = StringUtils.insertFrontAndBack(entity.redPacketSenderNickName, "你领取了", "的")
            }
        }
    }

    inner class OtherVoiceHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.mWechatVoiceOtherAvatarIv)
        private val voiceLayout: LinearLayout = itemView.findViewById(R.id.mWechatVoiceOtherLayout)
        private val lengthTv: TextView = itemView.findViewById(R.id.mWechatVoiceOtherLengthTv)//秒数
        private val alreadyIv: ImageView = itemView.findViewById(R.id.mWechatVoiceOtherAlreadyIv)//是否已经读过了
        private val transferTv: TextView = itemView.findViewById(R.id.mWechatOtherVoiceTransferToTextTv)//转换文字
        private val transferToTextLayout: PercentRelativeLayout = itemView.findViewById(R.id.mWechatOtherVoiceTransferToTextLayout)//转文字
        private val nickName: TextView = itemView.findViewById(R.id.sWechatNickNameTv)
        init {
            otherAvatar.setOnClickListener{
                val entity = mList[adapterPosition]
                mListener.changeUserInfo(entity)
            }
        }
        fun bind(entity: WechatScreenShotEntity){
            if (mShowNickName){
                nickName.text = entity.wechatUserNickName
                nickName.visibility = View.VISIBLE
            }else{
                nickName.visibility = View.GONE
            }
            OtherUtil.setWechatVoiceLength(itemView.context, voiceLayout, entity.voiceLength)
            GlideUtil.displayHead(itemView.context, entity.avatar, otherAvatar)
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
            if (entity.msg.contains("撤销")){
                spannableString = SpannableStringBuilder(entity.msg)
                spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#576b95")), spannableString.length - 2, spannableString.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                systemContent.text = spannableString
            }else if (!entity.msg.startsWith("“") && entity.msg.contains("撤回")){
                spannableString = SpannableStringBuilder(entity.msg)
                spannableString.append(" 重新编辑")
                spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#576b95")), spannableString.length - 4, spannableString.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                systemContent.text = spannableString
            }else{
                systemContent.text = entity.msg
            }
        }
    }

    inner class OtherFileHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.sFileOtherAvatarTv)
        private val fileIconIv: ImageView = itemView.findViewById(R.id.sFileIconIv)
        private val title: TextView = itemView.findViewById(R.id.sFileTitleTv)
        private val size: TextView = itemView.findViewById(R.id.sFileContentTv)
        init {
            otherAvatar.setOnClickListener{
                val entity = mList[adapterPosition]
                mListener.changeUserInfo(entity)
            }
        }
        fun bind(entity: FileEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
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

    interface WechatOperateListener{
        fun otherTakeMyRedPacket(entity: WechatScreenShotEntity, position: Int)//对方领取我的红包
        fun meTakeOtherRedPacket(entity: WechatScreenShotEntity, position: Int)//我领取对方的红包
        fun myTransferWasReceive(entity: WechatScreenShotEntity, position: Int)//我领取对方的红包
        fun checkRedpacketDetails(entity: WechatScreenShotEntity, position: Int)//查看红包的领取详情
        fun closeBottomMenu()//关闭底部菜单
        fun changeUserInfo(entity: WechatScreenShotEntity)
    }
}