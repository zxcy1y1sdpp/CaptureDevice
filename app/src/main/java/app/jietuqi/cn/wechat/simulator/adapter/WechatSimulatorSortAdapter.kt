package app.jietuqi.cn.wechat.simulator.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.ui.wechatscreenshot.widget.EmojiWechatManager
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.WechatTimeUtil

/**
 * 作者： liuyuanbo on 2019/3/1 11:10.
 * 时间： 2019/3/1 11:10
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatSimulatorSortAdapter(val mList: ArrayList<WechatScreenShotEntity>, val mType: Int) : RecyclerView.Adapter<WechatSimulatorSortAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_simulator_sort, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount() = mList.size
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatar: ImageView = itemView.findViewById(R.id.avatarIv)
        private val msg: TextView = itemView.findViewById(R.id.nickNameTv)

        fun bind(entity: WechatScreenShotEntity){
            when(entity.msgType){
                0 ->{//文字
                    GlideUtil.displayHead(itemView.context, entity.avatar, avatar)
                    val cs = EmojiWechatManager.parse(StringUtils.insertFront(entity.msg, "[文本] "), msg.textSize)
                    msg.setText(cs, TextView.BufferType.SPANNABLE)
                }
                1 ->{//图片
                    GlideUtil.displayHead(itemView.context, entity.avatar, avatar)
                    msg.text = "[图片] "
                }
                2 ->{//时间
                    GlideUtil.displayHead(itemView.context, R.drawable.message_time, avatar)
                    if ("12" == entity.timeType){
                        msg.text = WechatTimeUtil.getNewChat12Time(entity.time)
                    }else{
                        msg.text = WechatTimeUtil.getNewChat24Time(entity.time)
                    }
                }
                3 ->{//发红包
                    GlideUtil.displayHead(itemView.context, entity.avatar, avatar)
                    msg.text = StringUtils.insertBack("[群微信红包] ", entity.msg)
                }
                4 ->{//领红包
                    GlideUtil.displayHead(itemView.context, entity.avatar, avatar)
                    if (entity.isComMsg){//自己的红包被领取
                        msg.text = StringUtils.insertFrontAndBack(entity.wechatUserNickName, "[群微信红包] ", "领取了你的红包")
                    }else{//领取对方的红包
                        msg.text = "[微信红包] " + StringUtils.insertFrontAndBack(entity.wechatUserNickName, "你领取了", "的红包")
                    }
                }
                5 ->{//转账
                    GlideUtil.displayHead(itemView.context, entity.avatar, avatar)
                    if (entity.isComMsg){//对方发送给我的转账
                        msg.text = StringUtils.insertFrontAndBack(entity.money, "[转账] ¥", " -- 请你确认收钱")
                    }else{//我发送给对方的转账
                        msg.text = StringUtils.insertFrontAndBack(entity.money, "[转账] ¥", " -- 待朋友收钱")
                    }
                }
                6 ->{//收钱
                    GlideUtil.displayHead(itemView.context, entity.avatar, avatar)
                    if (entity.isComMsg){//自己的红包被领取
                        msg.text = StringUtils.insertFrontAndBack(entity.money, "[转账] ¥", " -- 朋友已确认收钱")
                    }else{//领取对方的红包
                        msg.text = StringUtils.insertFrontAndBack(entity.money, "[转账] ¥", " -- 你已确认收钱")
                    }
                }
                7 ->{//语音
                    GlideUtil.displayHead(itemView.context, entity.avatar, avatar)
                    if (entity.isComMsg){//对方发送的语音消息
                        msg.text = "[语音]"
                    }else{//我发送的语音消息
                        msg.text = "[语音]"
                    }
                }
                8 ->{//系统提示
                    GlideUtil.displayHead(itemView.context, R.drawable.message_system, avatar)
                    msg.text = StringUtils.insertFront(entity.msg, "[系统提示] ")
                }

            }
        }
    }
}


