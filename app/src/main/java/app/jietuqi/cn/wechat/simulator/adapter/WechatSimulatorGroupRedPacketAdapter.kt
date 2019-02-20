package app.jietuqi.cn.wechat.simulator.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.util.WechatTimeUtil
import app.jietuqi.cn.wechat.entity.WechatBankEntity
import app.jietuqi.cn.wechat.wechatfont.WechatChar5TextView
import com.makeramen.roundedimageview.RoundedImageView
import com.zhy.android.percent.support.PercentLinearLayout

/**
 * 作者： liuyuanbo on 2019/2/13 16:33.
 * 时间： 2019/2/13 16:33
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatSimulatorGroupRedPacketAdapter(val mEntity: WechatScreenShotEntity, val mMyRedPacket: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE1 = 1
    private val TYPE2 = 2
    override fun getItemViewType(position: Int): Int {
        return if (position == 0){
            TYPE1
        }else{
            TYPE2
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE1 -> TitleHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_simulator_group_red_packet1, parent, false))
            else -> ContentHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_simulator_group_red_packet2, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE1){
            if (holder is TitleHolder) {
                holder.bind()
            }
        }
        if (getItemViewType(position) == TYPE2){
            if (holder is ContentHolder) {
                holder.bind(mEntity.receiveRedPacketRoleList[position - 1])
            }
        }
    }

    override fun getItemCount() = mEntity.receiveRedPacketRoleList.size + 1

    inner class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatar: ImageView = itemView.findViewById(R.id.mWechatScreenShotReceiveAvatarIv)
        private val nickName: TextView = itemView.findViewById(R.id.mWechatScreenShotRedPacketNickNameTv)
        private val msg: TextView = itemView.findViewById(R.id.mWechatScreenShotRedPacketPreviewMsgTv)
        private val money: WechatChar5TextView = itemView.findViewById(R.id.mWechatScreenShotRedPacketMoneyTv)
        private val packetCount: TextView = itemView.findViewById(R.id.mWechatScreenShotSendRedPacketMoneyTv)
        private val joinReceive: PercentLinearLayout = itemView.findViewById(R.id.sJoinLayout)
        private val emojiLayout: PercentLinearLayout = itemView.findViewById(R.id.sEmojiLayout)

        /*init {
            itemView.setOnClickListener{
                var entity: WechatBankEntity
                val choiceEntity = mList[adapterPosition]
                for (i in mList.indices) {
                    entity = mList[i]
                    entity.isCheck = false
                }
                choiceEntity.isCheck = true
                mCloseListener.choice(choiceEntity, adapterPosition)
                notifyDataSetChanged()

            }
        }*/
        fun bind(){
            GlideUtil.displayHead(itemView.context, mEntity.avatar, avatar)
            nickName.text = StringUtils.insertBack(mEntity.redPacketSenderNickName, "的红包")
            msg.text = mEntity.msg
            if (mEntity.joinReceiveRedPacket){//如果自己参与抢红包就展示金额
                emojiLayout.visibility = View.VISIBLE
                var entity: WechatUserEntity
                for (i in mEntity.receiveRedPacketRoleList.indices){
                    entity = mEntity.receiveRedPacketRoleList[i]
                    if (entity.wechatUserId == UserOperateUtil.getWechatSimulatorMySelf().wechatUserId){//找到“自己”
                        money.text = StringUtils.keep2Point(entity.money)
                    }
                }
            }else{
                joinReceive.visibility = View.GONE
                emojiLayout.visibility = View.GONE
            }

            if (mMyRedPacket){//自己发的红包
                packetCount.text = mEntity.redPacketCount.toString() + "个红包共"+StringUtils.keep2Point(mEntity.money)+"元，"+mEntity.receiveCompleteTime+"被抢光"
            }else{
                packetCount.text = mEntity.redPacketCount.toString() + "个红包"+mEntity.receiveCompleteTime+"被抢光"
            }

        }
    }
    inner class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatar: RoundedImageView = itemView.findViewById(R.id.mWechatScreenShotSendRedPacketReceiverAvatarIv)
        private val nickName: TextView = itemView.findViewById(R.id.mWechatScreenShotSendRedPacketReceiverNickNameTv)
        private val time: TextView = itemView.findViewById(R.id.mWechatScreenShotSendRedPacketReceiveTimeTv)
        private val money: TextView = itemView.findViewById(R.id.mWechatScreenShotSendRedPacketMoney2Tv)
        private val bestLuck: PercentLinearLayout = itemView.findViewById(R.id.sBestLuckLayout)

        fun bind(entity: WechatUserEntity){
            GlideUtil.displayHead(itemView.context, entity.getAvatarFile(), avatar)
            nickName.text = entity.wechatUserNickName
            time.text = WechatTimeUtil.getNewChatTimeHongbaoPreview(entity.lastTime)//设置领取时间
            money.text = StringUtils.insertBack(StringUtils.keep2Point(entity.money), "元")
            if (entity.isBestLuck){
                bestLuck.visibility = View.VISIBLE
            }else{
                bestLuck.visibility = View.GONE
            }
        }
    }

    interface ChoiceListener{
        fun choice(bankEntity: WechatBankEntity?, position: Int)
    }
}