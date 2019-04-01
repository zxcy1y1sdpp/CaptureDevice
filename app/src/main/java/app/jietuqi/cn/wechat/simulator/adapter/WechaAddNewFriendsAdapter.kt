package app.jietuqi.cn.wechat.simulator.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil
import com.makeramen.roundedimageview.RoundedImageView

/**
 * 作者： liuyuanbo on 2019/2/13 11:01.
 * 时间： 2019/2/13 11:01
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechaAddNewFriendsAdapter(val mList: ArrayList<WechatUserEntity>) : RecyclerView.Adapter<WechaAddNewFriendsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_add_new_friends, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount() = if (mList.size > 4) 4 else mList.size
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatar: RoundedImageView = itemView.findViewById(R.id.sAvatarIv)
        private val nickNameLayout: LinearLayout = itemView.findViewById(R.id.sNickNameLayout)
        private val nickName: TextView = itemView.findViewById(R.id.sNickName)
        private val msgTv: TextView = itemView.findViewById(R.id.sMsgTv)

        fun bind(entity: WechatUserEntity) {
            GlideUtil.displayHead(itemView.context, entity.getAvatarFile(), avatar)
            if (mList.size > 1){
                nickNameLayout.visibility = View.GONE
            }else{
                nickNameLayout.visibility = View.VISIBLE
                nickName.text = entity.wechatUserNickName
                msgTv.text = entity.msg
            }
        }
    }
}


