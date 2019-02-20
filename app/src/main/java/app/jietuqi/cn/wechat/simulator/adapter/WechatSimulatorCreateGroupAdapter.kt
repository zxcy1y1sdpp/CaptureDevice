package app.jietuqi.cn.wechat.simulator.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.ToastUtils
import com.makeramen.roundedimageview.RoundedImageView

/**
 * 作者： liuyuanbo on 2018/11/29 15:19.
 * 时间： 2018/11/29 15:19
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatSimulatorCreateGroupAdapter(val mList: ArrayList<WechatUserEntity>, val mMyEntity: WechatUserEntity) : RecyclerView.Adapter<WechatSimulatorCreateGroupAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_simulator_create_group_chat, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var entity = mList[position]
        holder.bind(entity)
    }

    override fun getItemCount() = mList.size
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private var avatarIv: RoundedImageView = itemView.findViewById(R.id.sHeaderAvatarIv)
        private var nickNameTv: TextView = itemView.findViewById(R.id.sNickNameTv)
        private var choiceCb: CheckBox = itemView.findViewById(R.id.sChoiceCb)
        override fun onClick(v: View?) {
            val position = adapterPosition
            val entity: WechatUserEntity = mList[position]
            if(entity.wechatUserId == mMyEntity.wechatUserId){//选中的事“自己”
                ToastUtils.showShort(itemView.context, "不可以选择【 自己 】")
                return
            }
            entity.isChecked = !entity.isChecked
            notifyItemChanged(position)
        }

        init {
            itemView.setOnClickListener(this)
            choiceCb.setOnClickListener(this)

        }
        fun bind(entity: WechatUserEntity) {
            GlideUtil.displayHead(itemView.context, entity.getAvatarFile(), avatarIv)
            nickNameTv.text = entity.wechatUserNickName
            choiceCb.isChecked = entity.isChecked
        }
    }
}
