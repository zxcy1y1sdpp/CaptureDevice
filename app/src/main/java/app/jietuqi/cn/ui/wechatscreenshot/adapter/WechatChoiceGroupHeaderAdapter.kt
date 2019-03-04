package app.jietuqi.cn.ui.wechatscreenshot.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil
import com.makeramen.roundedimageview.RoundedImageView

/**
 * 作者： liuyuanbo on 2019/3/4 11:32.
 * 时间： 2019/3/4 11:32
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatChoiceGroupHeaderAdapter(val mList: ArrayList<WechatUserEntity>) : RecyclerView.Adapter<WechatChoiceGroupHeaderAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_choice_group_header, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var entity = mList[position]
        holder.bind(entity)
    }

    override fun getItemCount() = mList.size
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var avatarIv: RoundedImageView = itemView.findViewById(R.id.sGroupHeaderIv)

        fun bind(entity: WechatUserEntity) {
            GlideUtil.displayHead(itemView.context, entity.getAvatarFile(), avatarIv)
        }
    }
}

