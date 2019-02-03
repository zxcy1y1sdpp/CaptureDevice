package app.jietuqi.cn.ui.wechatscreenshot.adapter

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
import com.coorchice.library.SuperTextView

/**
 * 作者： liuyuanbo on 2018/12/1 16:03.
 * 时间： 2018/12/1 16:03
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatScreenShotAdapter(val mList: ArrayList<WechatScreenShotEntity>, val mListener: DeleteListener) : RecyclerView.Adapter<WechatScreenShotAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_screenshot, parent, false))


    override fun getItemCount() = mList.size
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        override fun onClick(v: View?) {
            when(v?.id){
                R.id.sWechatScreenShotDeleteTv ->{
                    var entity = mList[adapterPosition]
                    mListener.delete(entity, adapterPosition)
                }
                else ->{
                }
            }
        }
        init {
            itemView.findViewById<SuperTextView>(R.id.sWechatScreenShotDeleteTv).setOnClickListener(this)
            itemView.setOnClickListener(this)
        }
        private var contentTv: TextView = itemView.findViewById(R.id.sWechatScreenShotContentTv)
        private var avatarIv: ImageView = itemView.findViewById(R.id.sWechatScreenShotAvatarIv)

        fun bind(entity: WechatScreenShotEntity) {
            GlideUtil.displayHead(itemView.context, entity.avatar, avatarIv)
            when {
                entity.msgType == 0 -> {
                    var msg = StringUtils.insertFront(entity.msg, "[文本]")
                    val cs = EmojiWechatManager.parse(msg, contentTv.textSize)
                    contentTv.setText(cs, TextView.BufferType.SPANNABLE)
                }
                entity.msgType == 1 -> {
                    contentTv.text = "[图片]"
                }
                entity.msgType == 2 -> {//时间
                    if ("12" == entity.timeType){
                        contentTv.text = StringUtils.insertFront(WechatTimeUtil.getNewChat12Time(entity.time), "[时间]")
                    }else{
                        contentTv.text = StringUtils.insertFront(WechatTimeUtil.getNewChat24Time(entity.time), "[时间]")
                    }
                }
                entity.msgType == 3 -> {
                    contentTv.text = "[发红包]"
                }
                entity.msgType == 4 -> {
                    contentTv.text = "[收红包]"
                }
                entity.msgType == 5 -> {
                    contentTv.text = StringUtils.insertFront(StringUtils.insertFront(StringUtils.keep2Point(entity.money), "¥"), "[转账]")
                }
                entity.msgType == 6 -> {
                    contentTv.text = StringUtils.insertFront(StringUtils.insertFront(StringUtils.keep2Point(entity.money), "¥"), "[收钱]")
                }
                entity.msgType == 7 -> {
                    contentTv.text = StringUtils.insertFrontAndBack(entity.voiceLength, "[语音]", "\"")
                }
                entity.msgType == 8 -> {
                    contentTv.text = StringUtils.insertFront(entity.msg, "[系统提示]")
                }
            }
        }
    }
    interface DeleteListener{
        fun delete(entity: WechatScreenShotEntity, position: Int)
    }
}
