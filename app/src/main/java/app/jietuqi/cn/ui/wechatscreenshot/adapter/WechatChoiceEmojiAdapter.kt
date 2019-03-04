package app.jietuqi.cn.ui.wechatscreenshot.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import app.jietuqi.cn.R
import app.jietuqi.cn.ResourceHelper
import app.jietuqi.cn.util.GlideUtil

/**
 * 作者： liuyuanbo on 2018/11/29 15:19.
 * 时间： 2018/11/29 15:19
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatChoiceEmojiAdapter(val mList: ArrayList<String>) : RecyclerView.Adapter<WechatChoiceEmojiAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_emoji, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var entity = mList[position]
        holder.bind(entity)
    }

    override fun getItemCount() = mList.size
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var avatarIv: ImageView = itemView.findViewById(R.id.sWechatGifIv)
        fun bind(name: String) {
            GlideUtil.displayGif(itemView.context, ResourceHelper.getAppIconId(name), avatarIv)
        }
    }
}
