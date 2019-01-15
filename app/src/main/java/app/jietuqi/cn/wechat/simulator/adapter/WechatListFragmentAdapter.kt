package app.jietuqi.cn.wechat.simulator.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.WechatTimeUtil

/**
 * 作者： liuyuanbo on 2018/10/10 10:07.
 * 时间： 2018/10/10 10:07
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class WechatListFragmentAdapter(val mList: MutableList<WechatUserEntity>) : RecyclerView.Adapter<WechatListFragmentAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_fragment_wechat_list, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount() = mList.size
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatar: ImageView = itemView.findViewById(R.id.avatarIv)
        private val nickName: TextView = itemView.findViewById(R.id.nickNameTv)
        private val lastMsg: TextView = itemView.findViewById(R.id.lastMsgTv)
        private val time: TextView = itemView.findViewById(R.id.timeTv)

        fun bind(entity: WechatUserEntity){
            GlideUtil.displayHead(itemView.context, entity.getAvatarFile(), avatar)
            nickName.text = entity.wechatUserNickName
            lastMsg.setTextColor(ContextCompat.getColor(itemView.context, R.color.wechatLightGray))
            if (null != entity.msgType){
                when(entity.msgType.toInt()){
                    0 ->{//文字
                        lastMsg.text = entity.msg
                    }
                    1 ->{//图片
                        lastMsg.text = "[图片]"
                    }
                    2 ->{//时间
                        lastMsg.text = entity.msg
                    }
                    3 ->{//发红包
                        lastMsg.text = StringUtils.insertBack("[微信红包]", entity.msg)
                    }
                    4 ->{//领红包
                        if (!entity.isComMsg){//自己的红包被领取
                            lastMsg.text = StringUtils.insertBack(entity.wechatUserNickName, "领取了你的红包")
                        }else{//领取对方的红包
                            lastMsg.text = StringUtils.insertFrontAndBack(entity.wechatUserNickName, "你领取了", "的红包")
                        }
                    }
                    5 ->{//转账
                        if (entity.isComMsg){//对方发送给我的转账
                            lastMsg.text = StringUtils.insertBack("[转账]", "请你确认收钱")
                        }else{//我发送给对方的转账
                            lastMsg.text = StringUtils.insertBack("[转账]", "待朋友收钱")
                        }
                    }
                    6 ->{//收钱
                        if (entity.isComMsg){//自己的红包被领取
                            lastMsg.text = StringUtils.insertBack("[转账]", "朋友已确认收钱")
                        }else{//领取对方的红包
                            lastMsg.text = StringUtils.insertBack("[转账]", "你已确认收钱")
                        }
                    }
                    7 ->{//语音
                        if (entity.isComMsg){//对方发送的语音消息
                            if (!entity.alreadyRead){//未读
                                lastMsg.setTextColor(Color.parseColor("#20BE64"))
                            }else{//已读
                                lastMsg.setTextColor(ContextCompat.getColor(itemView.context, R.color.wechatLightGray))
                            }
                            lastMsg.text = "[语音]"
                        }else{//我发送的语音消息
                            lastMsg.text = "[语音]"
                        }
                    }
                    8 ->{//系统提示
                        lastMsg.text = entity.msg
                    }
                }
            }
            var timeStr = WechatTimeUtil.getNewChatTime(entity.lastTime)
            time.text = timeStr
        }
    }
}

