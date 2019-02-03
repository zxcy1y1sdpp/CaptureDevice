package app.jietuqi.cn.ui.alipayscreenshot.adapter
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.alipayscreenshot.db.AlipayScreenShotHelper
import app.jietuqi.cn.ui.alipayscreenshot.entity.AlipayScreenShotEntity
import app.jietuqi.cn.ui.alipayscreenshot.widget.BubbleImageView
import app.jietuqi.cn.ui.alipayscreenshot.widget.EmojiAlipayManager
import app.jietuqi.cn.ui.entity.SingleTalkEntity
import app.jietuqi.cn.util.*
import com.zhy.android.percent.support.PercentRelativeLayout
import java.io.File

/**
 * 作者： liuyuanbo on 2018/10/10 17:50.
 * 时间： 2018/10/10 17:50
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class AlipayScreenShotPreviewAdapter(val mList: MutableList<AlipayScreenShotEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
    //    private val TRANSFER_RECEIVED_TYPE_MY = 10//对方发送的转账
//    private val TRANSFER_RECEIVED_TYPE_OTHER = 11//对方发送的转账
    private val VOICE_MY = 12//对方发送的转账
    private val VOICE_OTHER = 13//对方发送的转账
    private val SYSTEM_TYPE = 14//系统提示

    private val mOtherEntity = UserOperateUtil.getAlipayOtherSide()
    private val mMyEntity = UserOperateUtil.getAlipayMySelf()
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
                entity.msgType == 7 -> {
                    return VOICE_OTHER
                }
                entity.msgType == 8 -> {
                    return SYSTEM_TYPE
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
                entity.msgType == 7 -> {
                    return VOICE_MY
                }
                entity.msgType == 8 -> {
                    return SYSTEM_TYPE
                }
            }
        }
        return 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TEXT_TYPE_OTHER -> return OtherTextHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alipay_screenshot_other_text, parent, false))
            IMG_TYPE_OTHER -> return OtherImgHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alipay_other_img, parent, false))
            IMG_TYPE_MY -> return MyImgHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alipay_my_img, parent, false))
            TIME_TYPE -> return TimeHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alipay_screenshot_time, parent, false))
            REDPACKET_TYPE_OTHER -> return OtherRedPacketHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alipay_other_redpackage, parent, false))
            REDPACKET_TYPE_MY -> return MyRedPacketHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alipay_my_redpackage, parent, false))
            REVEIVE_RED_PACKET -> return ReceiveRedPacketHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alipay_receive, parent, false))
            TRANSFER_TYPE_OTHER -> return OtherTransferHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alipay_other_transfer, parent, false))
            TRANSFER_TYPE_MY -> return MyTransferHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alipay_my_transfer, parent, false))
            VOICE_MY -> return MyVoiceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alipay_my_voice, parent, false))
            VOICE_OTHER -> return OtherVoiceHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alipay_other_voice, parent, false))
            SYSTEM_TYPE -> return SystemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alipay_system_msg, parent, false))
        }
        return return MyTextHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_alipay_screenshot_my_text, parent, false))
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
//            TRANSFER_RECEIVED_TYPE_MY -> if (holder is WechatScreenShotPreviewAdapter.MyTransferReceivedHolder) {//不添加该判断会导致数据重复
//                holder.bind(mList[position])
//            }
//            TRANSFER_RECEIVED_TYPE_OTHER -> if (holder is WechatScreenShotPreviewAdapter.OtherTransferReceivedHolder) {//不添加该判断会导致数据重复
//                holder.bind(mList[position])
//            }
            VOICE_MY -> if (holder is MyVoiceHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            VOICE_OTHER -> if (holder is OtherVoiceHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            SYSTEM_TYPE -> if (holder is SystemHolder) {//不添加该判断会导致数据重复
                holder.bind(mList[position])
            }
            else -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class OtherTextHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.sAlipayScreenShotOtherAvatarIv)
        private val otherText: TextView = itemView.findViewById(R.id.sAlipayScreenShotOtherContentTv)

        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            val msg = entity.msg + " "
            val cs = EmojiAlipayManager.parse(msg, otherText.textSize)
            otherText.setText(cs, TextView.BufferType.SPANNABLE)
        }
    }
    inner class MyTextHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.sAlipayScreenShotMyAvatarIv)
        private val myText: TextView = itemView.findViewById(R.id.sAlipayScreenShotMyContentTv)

        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            val msg = entity.msg + " "
            val cs = EmojiAlipayManager.parse(msg, myText.textSize)
            myText.setText(cs, TextView.BufferType.SPANNABLE)
        }
    }
    inner class OtherImgHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.sAlipayOtherAvatarIv)
        private val imageFile: BubbleImageView = itemView.findViewById(R.id.sAlipayOtherImgIv)

        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            GlideUtil.displayAlipay(itemView.context, File(entity.filePath), imageFile)
        }
    }
    inner class MyImgHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.sAlipayMyAvatarIv)
        private val imageFile: BubbleImageView = itemView.findViewById(R.id.sAlipayMyImgIv)

        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            GlideUtil.displayAlipay2(itemView.context, File(entity.filePath), imageFile)
        }
    }
    inner class TimeHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val time: TextView = itemView.findViewById(R.id.sAlipayTimeTv)
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(entity: SingleTalkEntity){
            time.text = WechatTimeUtil.getNewChatTime(entity.time)
        }
    }

    inner class OtherRedPacketHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.sAlipayOtherAvatar)
        private val bubbleLayout: PercentRelativeLayout = itemView.findViewById(R.id.sAlipayBubbleLayout)
        private val tagIv: ImageView = itemView.findViewById(R.id.tagIv)
        private val msgTv: TextView = itemView.findViewById(R.id.sAlipayLeaveMessage)//留言
        private val statusTv: TextView = itemView.findViewById(R.id.sAlipayRedPackageStatusTv)//红包状态
        init {
            itemView.setOnClickListener{
                val entity = mList[adapterPosition]
                if (!entity.receive){//如果红包没有被领取，就领取
                    entity.receive = true
                    AlipayScreenShotHelper(itemView.context).update(entity)
                }else{
                    ToastUtils.showShort(itemView.context, "我查看对方的的红包的详细页面")
                }
            }
        }
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            msgTv.text = entity.msg
            if (entity.receive){
                bubbleLayout.setBackgroundResource(R.drawable.alipay_chat_hongbao_select_b)//已领取
                GlideUtil.display(itemView.context, R.drawable.alipay_chat_select_hongbao, tagIv)
                statusTv.text = "红包已领取"
            }else{
                GlideUtil.display(itemView.context, R.drawable.alipay_chat_hongbao, tagIv)
                statusTv.text = "领取红包"
            }
        }
    }
    inner class MyRedPacketHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.sAlipayMyAvatar)
        private val bubbleLayout: PercentRelativeLayout = itemView.findViewById(R.id.sAlipayBubbleLayout)
        private val tagIv: ImageView = itemView.findViewById(R.id.tagIv)
        private val msgTv: TextView = itemView.findViewById(R.id.sAlipayLeaveMessage)//留言
        private val statusTv: TextView = itemView.findViewById(R.id.sAlipayRedPackageStatusTv)//红包状态
        init {
            itemView.setOnClickListener{
                val entity = mList[adapterPosition]
//                entity.position = adapterPosition//确定条目在列表中的位置
                if (!entity.receive){//如果红包没有被领取，就领取
                    entity.receive = true
                    AlipayScreenShotHelper(itemView.context).update(entity)
//123                    EventBusUtil.post(entity)
                }else{
                    ToastUtils.showShort(itemView.context, "对方查看我的红包的详情")
                }
            }
        }
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            msgTv.text = entity.msg
            if (entity.receive){
//                receiveLayout.visibility = View.VISIBLE
                bubbleLayout.setBackgroundResource(R.drawable.alipay_chat_hongbao_select_a)//已领取
                statusTv.text = "红包已被领完"
                GlideUtil.display(itemView.context, R.drawable.alipay_chat_select_hongbao, tagIv)
//                wechatReceiveRedPacket.text = StringUtils.insertBack(mOtherEntity.wechatUserNickName, "领取了你的")
            }else{
//                receiveLayout.visibility = View.GONE
                statusTv.text = "查看红包"
                GlideUtil.display(itemView.context, R.drawable.alipay_chat_hongbao, tagIv)
            }
        }
    }
    inner class OtherTransferHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.sAlipayOtherTransferAvatarIv)
        private val msgTv: TextView = itemView.findViewById(R.id.sAlipayOtherTransferLeaveMessageTv)//留言
        private val moneyTv: TextView = itemView.findViewById(R.id.sAlipayOtherTransferMoneyTv)//红包状态
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            msgTv.text = entity.msg
            moneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "¥")
        }
    }
    inner class MyTransferHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.sAlipayMyTransferAvatarIv)
        private val msgTv: TextView = itemView.findViewById(R.id.sAlipayMyTransferLeaveMessageTv)//留言
        private val moneyTv: TextView = itemView.findViewById(R.id.sAlipayMyTransferMoneyTv)//转账金额
        fun bind(entity: SingleTalkEntity){
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            msgTv.text = entity.msg
            moneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "¥")
        }
    }
    inner class ReceiveRedPacketHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val contentTv: TextView = itemView.findViewById(R.id.sAlipayReceiveRedPacketContentTv)//谁领了红包
        fun bind(entity: SingleTalkEntity){
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

    inner class OtherVoiceHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.mAlipayVoiceOtherAvatarIv)
        private val voiceLayout: LinearLayout = itemView.findViewById(R.id.mAlipayVoiceOtherLayout)
        private val lengthTv: TextView = itemView.findViewById(R.id.mAlipayVoiceOtherLengthTv)//秒数
        private val alreadyIv: ImageView = itemView.findViewById(R.id.mAlipayVoiceOtherAlreadyIv)//是否已经读过了

        fun bind(entity: SingleTalkEntity){
            OtherUtil.setAlipayVoiceLength(itemView.context, voiceLayout, entity.voiceLength)
            GlideUtil.displayHead(itemView.context, mOtherEntity.getAvatarFile(), otherAvatar)
            lengthTv.text = StringUtils.insertBack(entity.voiceLength.toString(), "\"")
            lengthTv.text = entity.voiceLength.toString()
            if (entity.alreadyRead){
                alreadyIv.visibility = View.GONE
            }else{
                alreadyIv.visibility = View.VISIBLE
            }
        }
    }
    inner class MyVoiceHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.mAlipayVoiceMyAvatarIv)
        private val voiceLayout: LinearLayout = itemView.findViewById(R.id.mAlipayVoiceMyLayout)//长度
        private val lengthTv: TextView = itemView.findViewById(R.id.mAlipayVoiceMyLengthTv)//秒数
        private val alreadyIv: ImageView = itemView.findViewById(R.id.mAlipayVoiceMyAlreadyIv)//是否已经读过了
        fun bind(entity: SingleTalkEntity){
            OtherUtil.setAlipayVoiceLength(itemView.context, voiceLayout, entity.voiceLength)
            GlideUtil.displayHead(itemView.context, mMyEntity.getAvatarFile(), myAvatar)
            lengthTv.text = StringUtils.insertBack(entity.voiceLength.toString(), "\"")
            if (entity.alreadyRead){
                alreadyIv.visibility = View.GONE
            }else{
                alreadyIv.visibility = View.VISIBLE
            }
        }
    }
    inner class SystemHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val systemContent: TextView = itemView.findViewById(R.id.sAlipaySystemMessageTv)
        fun bind(entity: SingleTalkEntity){
            systemContent.text = entity.msg
        }
    }
}