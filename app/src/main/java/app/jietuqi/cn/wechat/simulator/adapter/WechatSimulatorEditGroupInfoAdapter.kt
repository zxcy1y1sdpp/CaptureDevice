package app.jietuqi.cn.wechat.simulator.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil

/**
 * 作者： liuyuanbo on 2019/2/18 14:44.
 * 时间： 2019/2/18 14:44
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatSimulatorEditGroupInfoAdapter(val mList: ArrayList<WechatUserEntity>) : RecyclerView.Adapter<WechatSimulatorEditGroupInfoAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_group_roles, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount() = mList.size
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatar: ImageView = itemView.findViewById(R.id.sWechatGroupRolesAvatarIv)
        private val nickName: TextView = itemView.findViewById(R.id.sWechatGroupRolesNickNameTv)

        fun bind(entity: WechatUserEntity) {
            GlideUtil.displayHead(itemView.context, entity.getAvatarFile(), avatar)
            nickName.text = entity.wechatUserNickName

        }
    }
}


