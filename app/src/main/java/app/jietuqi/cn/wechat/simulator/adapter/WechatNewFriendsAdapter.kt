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
 * 作者： liuyuanbo on 2018/10/23 14:57.
 * 时间： 2018/10/23 14:57
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class WechatNewFriendsAdapter(val mList: ArrayList<WechatUserEntity>, val mListener: EditListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
            TYPE1 -> TitleHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_new_friend1, parent, false))
            else -> ContentHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_new_friend2, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE1){
            if (holder is TitleHolder) { }
        }else{
            if (holder is ContentHolder) {
                holder.bind(mList[position - 1])
            }
        }
    }

    override fun getItemCount() = mList.size + 1

    inner class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatarIv: ImageView = itemView.findViewById(R.id.sAvatarIv)
        private val nickNameTv: TextView = itemView.findViewById(R.id.sNickNameTv)
        private val msgTv: TextView = itemView.findViewById(R.id.sMsgTv)
        private val acceptTv: TextView = itemView.findViewById(R.id.sAcceptTv)
        init {
            itemView.setOnClickListener{
                val entity = mList[adapterPosition - 1]
                mListener.edit(entity, adapterPosition - 1)
            }
            acceptTv.setOnClickListener{
                var entity = mList[adapterPosition - 1]
                mListener.accept(entity, adapterPosition - 1)
            }
        }
        fun bind(entity: WechatUserEntity){
            GlideUtil.displayHead(itemView.context, entity.getAvatarFile(), avatarIv)
            nickNameTv.text = entity.wechatUserNickName
            msgTv.text = entity.msg
            if (entity.isChecked){
                acceptTv.visibility = View.GONE
            }else{
                acceptTv.visibility = View.VISIBLE
            }
        }
    }
    inner class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    interface EditListener{
        fun edit(entity: WechatUserEntity?, position: Int)
        fun accept(entity: WechatUserEntity?, position: Int)
    }
}
