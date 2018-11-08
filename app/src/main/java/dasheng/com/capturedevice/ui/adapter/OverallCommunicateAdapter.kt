package dasheng.com.capturedevice.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.R.id.overallCommunicateAvatarIv
import dasheng.com.capturedevice.ui.entity.OverallCommunicateEntity
import dasheng.com.capturedevice.util.GlideUtil
import dasheng.com.capturedevice.widget.ninegrid.NineGridView
import dasheng.com.capturedevice.widget.ninegrid.preview.NineGridViewClickAdapter

/**
 * 作者： liuyuanbo on 2018/11/7 17:03.
 * 时间： 2018/11/7 17:03
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class OverallCommunicateAdapter(val mList: ArrayList<OverallCommunicateEntity>) : RecyclerView.Adapter<OverallCommunicateAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_communicate, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var avatar: ImageView = itemView.findViewById(R.id.overallCommunicateAvatarIv)
        private var nickName: TextView = itemView.findViewById(R.id.overallCommunicateNickNameTv)
        private var content: TextView = itemView.findViewById(R.id.overallCommunicateContentTv)
        private var pics: NineGridView = itemView.findViewById(R.id.overallCommunicateNineGridView)
        fun bind(entity: OverallCommunicateEntity){
            GlideUtil.display(itemView.context, entity.avatar, avatar)
            nickName.text = entity.nickName
            content.text = entity.content
            pics.setAdapter(NineGridViewClickAdapter(itemView.context, entity.pics))
        }
    }
}
