package app.jietuqi.cn.wechat.simulator.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.widget.EmojiWechatManager
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.util.WechatTimeUtil
import app.jietuqi.cn.wechat.simulator.widget.RedPointTextView
import com.zhy.android.percent.support.PercentRelativeLayout


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
        private val topLayout: PercentRelativeLayout = itemView.findViewById(R.id.sTopLayout)
        private val unReadTv: RedPointTextView = itemView.findViewById(R.id.unReadNumberTv)

        fun bind(entity: WechatUserEntity){
            lastMsg.setTextColor(ContextCompat.getColor(itemView.context, R.color.wechatLightGray))
            if (entity.chatType == 0){
                GlideUtil.displayHead(itemView.context, entity.getAvatarFile(), avatar)
                nickName.text = entity.wechatUserNickName
                if (!TextUtils.isEmpty(entity.msgType)){
                    when(entity.msgType.toInt()){
                        0 ->{//文字
                            val cs = EmojiWechatManager.parse(entity.msg, lastMsg.textSize)
                            lastMsg.setText(cs, TextView.BufferType.SPANNABLE)
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
                            if (entity.isComMsg){//自己的红包被领取
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
                        9 ->{//视频通话
                            lastMsg.text = "[视频通话]"
                        }
                        10 ->{//语音通话
                            lastMsg.text = "[语音通话]"
                        }
                        13 ->{//邀请加群
                            lastMsg.text = "[链接]邀请你加入群聊"
                        }
                        16 ->{//系统提示
                            lastMsg.text = "[文件]"
                        }
                    }
                }else{
                    lastMsg.text = ""
                }
            }else{
                var recentRoleEntity = WechatUserEntity()
                for (i in entity.groupRoles.indices){
                    if (entity.groupRoles[i].isRecentRole){
                        recentRoleEntity = entity.groupRoles[i]
                    }
                }
                avatar.setImageBitmap(entity.groupHeader)

                if (TextUtils.isEmpty(entity.groupName)){
                    val groupName = StringBuilder()
                    var entity2: WechatUserEntity
                    for (i in entity.groupRoles.indices){
                        entity2 = entity.groupRoles[i]
                        groupName.append(entity2.wechatUserNickName).append("、")
                    }
                    groupName.deleteCharAt(groupName.length - 1)
                    nickName.text = groupName.toString()
                }else{
                    nickName.text = entity.groupName
                }

                if (!TextUtils.isEmpty(entity.msgType)){
                    when(entity.msgType.toInt()){
                        0 ->{//文字
                            var cs: CharSequence = if (entity.isComMsg){
                                EmojiWechatManager.parse(recentRoleEntity.wechatUserNickName + ":" + entity.msg, lastMsg.textSize)
                            }else{
                                EmojiWechatManager.parse(entity.msg, lastMsg.textSize)
                            }

                            lastMsg.setText(cs, TextView.BufferType.SPANNABLE)
                        }
                        1 ->{//图片
                            if (entity.isComMsg){
                                lastMsg.text = recentRoleEntity.wechatUserNickName + ":" + "[图片]"
                            }else{
                                lastMsg.text = "[图片]"
                            }

                        }
                        2 ->{//时间
                            if (entity.isComMsg){
                                lastMsg.text = recentRoleEntity.wechatUserNickName + ":" + entity.msg
                            }else{
                                lastMsg.text = entity.msg
                            }
                        }
                        3 ->{//发红包
                            if (entity.isComMsg){
                                lastMsg.text = recentRoleEntity.wechatUserNickName + ":" + StringUtils.insertBack("[微信红包]", entity.msg)
                            }else{
                                lastMsg.text = StringUtils.insertBack("[微信红包]", entity.msg)
                            }

                        }
                        4 ->{//领红包
                            if (!entity.isComMsg){//自己的红包被领取、
                                if (entity.groupRedPacketInfo.wechatUserId == UserOperateUtil.getWechatSimulatorMySelf().wechatUserId){//红包是自己发的
                                    if (entity.wechatUserId == entity.groupRedPacketInfo.wechatUserId){//领红包的人是自己
                                        lastMsg.text = "你领取了自己发的红包"
                                    }else{
                                        lastMsg.text = StringUtils.insertBack(entity.wechatUserNickName, "领取了你的红包，你的红包已被领完")
                                    }
                                }else{
                                    lastMsg.text = StringUtils.insertBack(entity.groupRedPacketInfo.wechatUserNickName, "领取了你的红包，你的红包已被领完")
                                }
                            }else{//领取对方的红包
                                lastMsg.text = StringUtils.insertFrontAndBack(entity.groupRedPacketInfo.wechatUserNickName, "你领取了", "的红包")
                            }
                        }
                        7 ->{//语音
                            if (entity.isComMsg){//对方发送的语音消息
                                if (!entity.alreadyRead){//未读
                                    lastMsg.setTextColor(Color.parseColor("#20BE64"))
                                }else{//已读
                                    lastMsg.setTextColor(ContextCompat.getColor(itemView.context, R.color.wechatLightGray))
                                }
                                lastMsg.text = recentRoleEntity.wechatUserNickName + ":" + "[语音]"
                            }else{//我发送的语音消息
                                lastMsg.text = "[语音]"
                            }
                        }
                        8 ->{//系统提示
                            lastMsg.text = entity.msg
                        }
                    }
                }else{
                    lastMsg.text = ""
                }
            }


            if (entity.timeType == "12"){
                time.text = WechatTimeUtil.getNewChat12Time(entity.lastTime)
            }else{
                time.text = WechatTimeUtil.getNewChat24Time(entity.lastTime)
            }
            if (entity.top){
                topLayout.setBackgroundColor(Color.parseColor("#EDEDED"))
            }else{
                topLayout.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            if ("0" == entity.unReadNum){
                unReadTv.visibility = View.GONE
            }else{
                unReadTv.visibility = View.VISIBLE
                unReadTv.setText(entity.unReadNum)
                /*if (entity.unReadNum.toInt() >= 99){

                }else{
                    unReadTv.setText(entity.unReadNum)
                }*/
            }
        }
    }
}

