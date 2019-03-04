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
class WechatSimulatorEditGroupInfoAdapter(val mList: ArrayList<WechatUserEntity>, val mListener: OperateListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE1 = 0
    private val TYPE2 = 1
    private val TYPE3 = 2
    override fun getItemViewType(position: Int): Int {

        return when {
            position <= mList.size - 1 -> TYPE1
            position == mList.size -> TYPE2
            else -> TYPE3
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE1 ->{
                Holder1(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_edit_group_roles_1, parent, false))
            }
            TYPE2 -> {
                Holder2(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_edit_group_roles_2, parent, false))
            }
            else -> {
                Holder3(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_edit_group_roles_3, parent, false))
            }
        }
    }


    override fun getItemCount(): Int {
        return mList.size + 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE1 -> if (holder is Holder1) {
                holder.bind(mList[position])
            }
            TYPE2 -> if (holder is Holder2) {
            }
            TYPE3 -> if (holder is Holder3) {
            }
        }
    }
    internal inner class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatar: ImageView = itemView.findViewById(R.id.sWechatGroupRolesAvatarIv)
        private val nickName: TextView = itemView.findViewById(R.id.sWechatGroupRolesNickNameTv)

        fun bind(entity: WechatUserEntity) {
            GlideUtil.displayHead(itemView.context, entity.getAvatarFile(), avatar)
            nickName.text = entity.wechatUserNickName

        }
    }

    inner class Holder2(itemView: View) : RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener{
                mListener.addRoles()
            }
        }
    }
    inner class Holder3(itemView: View) : RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener{
                mListener.reduceRoles()
            }
        }
    }
    interface OperateListener{
        fun addRoles()
        fun reduceRoles()
    }
}


