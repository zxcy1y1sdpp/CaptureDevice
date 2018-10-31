package dasheng.com.capturedevice.wechat.ui.adapter

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.makeramen.roundedimageview.RoundedImageView
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.database.table.WechatSingleTalkEntity
import dasheng.com.capturedevice.database.table.WechatUserTable
import dasheng.com.capturedevice.entity.UserEntity
import dasheng.com.capturedevice.util.*
import dasheng.com.capturedevice.widget.bubble.BubbleLinearLayout
import dasheng.com.capturedevice.widget.bubble.BubbleTextView

/**
 * 作者： liuyuanbo on 2018/10/10 17:50.
 * 时间： 2018/10/10 17:50
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class WechatSingleTalkAdapter(val mList: MutableList<WechatSingleTalkEntity>, val mOtherEntity: WechatUserTable, val myInfo: UserEntity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    /**
     * 默认是本人操作
     */
    private var mSwitchRoles = true
    private val TEXT_TYPE_OTHER = 0//对方发送的文本聊天数据
    private val TEXT_TYPE_MY = 1//我发送的文本聊天数据
    private val IMG_TYPE_OTHER = 2//对方发送的图片聊天数据
    private val IMG_TYPE_MY = 3//我发送的图片聊天数据
    private val TIME_TYPE = 4//时间数据
    private val REDPACKET_TYPE_MY = 5//时间数据
    private val REDPACKET_TYPE_OTHER = 6//时间数据

    /**
     * 切换操作角色
     * true -- 本人操作
     * false -- 对方操作
     */
    fun switchRoles(me: Boolean){
        mSwitchRoles = me
    }
    override fun getItemViewType(position: Int): Int {
        val entity = mList[position]
        if (entity.isComMsg){//接收到的消息
            if (entity.msgType == 0){//文本消息
                return TEXT_TYPE_OTHER
            } else if(entity.msgType == 1){
                return IMG_TYPE_OTHER
            } else if (entity.msgType == 2){
                return TIME_TYPE
            }else if (entity.msgType == 3){
                return REDPACKET_TYPE_OTHER
            }
        }else{//发送的消息
            if (entity.msgType == 0){//文本消息
                return TEXT_TYPE_MY
            }else if (entity.msgType == 1){
                return IMG_TYPE_MY
            } else if (entity.msgType == 2){
                return TIME_TYPE
            }else if (entity.msgType == 3){
                return REDPACKET_TYPE_MY
            }
        }
        return 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TEXT_TYPE_OTHER -> return OtherTextHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_other_single_talk_text, parent, false))
            IMG_TYPE_OTHER -> return OtherImgHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_other_single_talk_img, parent, false))
            IMG_TYPE_MY -> return MyImgHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_my_single_talk_img, parent, false))
            TIME_TYPE -> return TimeHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_single_talk_time, parent, false))
            REDPACKET_TYPE_OTHER -> return OtherRedPacketHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_other_single_talk_redpackage, parent, false))
            REDPACKET_TYPE_MY -> return MyRedPacketHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_my_single_talk_redpackage, parent, false))
        }
        return return MyTextHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_my_single_talk_text, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TEXT_TYPE_OTHER -> if (holder is OtherTextHolder) {//不添加该判断会导致数据重复
                val viewHolder = holder
                viewHolder.bind(mList[position])
            }
            TEXT_TYPE_MY -> if (holder is MyTextHolder) {//不添加该判断会导致数据重复
                val viewHolder = holder
                viewHolder.bind(mList[position])
            }
            IMG_TYPE_OTHER -> if (holder is OtherImgHolder) {//不添加该判断会导致数据重复
                val viewHolder = holder
                viewHolder.bind(mList[position])
            }
            IMG_TYPE_MY -> if (holder is MyImgHolder) {//不添加该判断会导致数据重复
                val viewHolder = holder
                viewHolder.bind(mList[position])
            }
            TIME_TYPE -> if (holder is TimeHolder) {//不添加该判断会导致数据重复
                val viewHolder = holder
                viewHolder.bind(mList[position])
            }
            REDPACKET_TYPE_OTHER -> if (holder is OtherRedPacketHolder) {//不添加该判断会导致数据重复
                val viewHolder = holder
                viewHolder.bind(mList[position])
            }
            REDPACKET_TYPE_MY -> if (holder is MyRedPacketHolder) {//不添加该判断会导致数据重复
                val viewHolder = holder
                viewHolder.bind(mList[position])
            }
            else -> {
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class OtherTextHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.wechatOtherAvatar)
        private val otherText: TextView = itemView.findViewById(R.id.wechatOtherTextMsg)

        fun bind(entity: WechatSingleTalkEntity){
            GlideUtil.display(itemView.context, mOtherEntity, otherAvatar)
            otherText.text = entity.msg
        }
    }
    inner class MyTextHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.wechatMyAvatar)
        private val myText: BubbleTextView = itemView.findViewById(R.id.wechatMyTextMsg)

        fun bind(entity: WechatSingleTalkEntity){
            GlideUtil.display(itemView.context, myInfo.avatar, myAvatar)
            myText.text = entity.msg
        }
    }
    inner class OtherImgHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.wechatOtherAvatar)
        private val imageFile: RoundedImageView = itemView.findViewById(R.id.wechatOtherIv)

        fun bind(entity: WechatSingleTalkEntity){
            GlideUtil.display(itemView.context, mOtherEntity, otherAvatar)
            GlideUtil.display2(itemView.context, entity.img, imageFile)
        }
    }
    inner class MyImgHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.wechatMyAvatar)
        private val imageFile: RoundedImageView = itemView.findViewById(R.id.wechatMyIv)

        fun bind(entity: WechatSingleTalkEntity){
            GlideUtil.display(itemView.context, myInfo.avatar, myAvatar)
            GlideUtil.display2(itemView.context, entity.img, imageFile)
        }
    }
    inner class TimeHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val time: TextView = itemView.findViewById(R.id.wechatSingleTalkTimeTv)

        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(entity: WechatSingleTalkEntity){
            time.text = WechatTimeUtil.getNewChatTime(entity.time)
        }
    }

    inner class OtherRedPacketHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val otherAvatar: ImageView = itemView.findViewById(R.id.wechatOtherAvatar)
        private val receiveLayout: LinearLayout = itemView.findViewById(R.id.receiveRedPacketLayout)
        private val bubbleLayout: BubbleLinearLayout = itemView.findViewById(R.id.wechatBubbleLayout)
        private val tagIv: ImageView = itemView.findViewById(R.id.tagIv)
        private val msgTv: TextView = itemView.findViewById(R.id.wechatLeaveMessage)//留言
        private val statusTv: TextView = itemView.findViewById(R.id.wechatRedPackageStatusTv)//红包状态
        private val wechatReceiveRedPacket: TextView = itemView.findViewById(R.id.wechatReceiveRedPacket)//谁领了红包
        init {
            itemView.setOnClickListener{
                val entity = mList[adapterPosition]
                entity.position = adapterPosition//确定条目在列表中的位置
                if (mSwitchRoles){
                    if (!entity.receive){//如果红包没有被领取，就领取
                        entity.receive = true
                        EventBusUtil.post(entity)
                    }else{
                        Toast.makeText(itemView.context, "我查看对方的的红包的详细页面", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(itemView.context, "跳转到他的红包的详细页面", Toast.LENGTH_SHORT).show()
                }
            }
        }
        fun bind(entity: WechatSingleTalkEntity){
            GlideUtil.display(itemView.context, mOtherEntity, otherAvatar)
            msgTv.text = entity.msg
            if (entity.receive){
                receiveLayout.visibility = View.VISIBLE
                bubbleLayout.setBubbleColor("#FECF9F")//已领取
                GlideUtil.display(itemView.context, R.mipmap.wechat_redpacket_received, tagIv)
                statusTv.text = "红包已领取"
                wechatReceiveRedPacket.text = StringUtils.insertFrontAndBack(mOtherEntity.wechatUserNickName, "你领取了", "的")
            }else{
                receiveLayout.visibility = View.GONE
                bubbleLayout.setBubbleColor("#F89C46")
                GlideUtil.display(itemView.context, R.mipmap.wechat_redpacket_send, tagIv)
                statusTv.text = "领取红包"
            }
        }
    }
    inner class MyRedPacketHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val myAvatar: ImageView = itemView.findViewById(R.id.wechatMyAvatar)
        private val receiveLayout: LinearLayout = itemView.findViewById(R.id.receiveRedPacketLayout)
        private val bubbleLayout: BubbleLinearLayout = itemView.findViewById(R.id.wechatBubbleLayout)
        private val tagIv: ImageView = itemView.findViewById(R.id.tagIv)
        private val msgTv: TextView = itemView.findViewById(R.id.wechatLeaveMessage)//留言
        private val statusTv: TextView = itemView.findViewById(R.id.wechatRedPackageStatusTv)//红包状态
        private val wechatReceiveRedPacket: TextView = itemView.findViewById(R.id.wechatReceiveRedPacket)//谁领了红包
        init {
            itemView.setOnClickListener{
                val entity = mList[adapterPosition]
                entity.position = adapterPosition//确定条目在列表中的位置
                if (mSwitchRoles){
                    Toast.makeText(itemView.context, "我操作我自己的红包", Toast.LENGTH_SHORT).show()
                }else{
                    if (!entity.receive){//如果红包没有被领取，就领取
                        entity.receive = true
                        EventBusUtil.post(entity)
                    }else{
                        Toast.makeText(itemView.context, "对方查看我的红包的详情", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        fun bind(entity: WechatSingleTalkEntity){
            GlideUtil.display(itemView.context, myInfo.avatar, myAvatar)
            msgTv.text = entity.msg
            if (entity.receive){
                receiveLayout.visibility = View.VISIBLE
                bubbleLayout.setBubbleColor("#FECF9F")
                statusTv.text = "红包已被领完"
                GlideUtil.display(itemView.context, R.mipmap.wechat_redpacket_received, tagIv)
                wechatReceiveRedPacket.text = StringUtils.insertBack(mOtherEntity.wechatUserNickName, "领取了你的")
            }else{
                receiveLayout.visibility = View.GONE
                bubbleLayout.setBubbleColor("#F89C46")
                statusTv.text = "查看红包"
                GlideUtil.display(itemView.context, R.mipmap.wechat_redpacket_send, tagIv)
            }
        }
    }
}
