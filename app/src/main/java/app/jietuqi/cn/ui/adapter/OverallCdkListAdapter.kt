package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.OverallCdkEntity

/**
 * 作者： liuyuanbo on 2018/11/20 11:31.
 * 时间： 2018/11/20 11:31
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class OverallCdkListAdapter(val mList: ArrayList<OverallCdkEntity>) : RecyclerView.Adapter<OverallCdkListAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverallCdkListAdapter.Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_cdk_list, parent, false))
    }

    override fun getItemCount() = mList.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val orderTv: TextView = itemView.findViewById(R.id.sCdkOrderTv)
        private val contentTv: TextView = itemView.findViewById(R.id.sCdkContentTv)
        fun bind(entity: OverallCdkEntity){
            orderTv.text = (adapterPosition + 1).toString()
            contentTv.text = entity.cdk
        }
    }
}
