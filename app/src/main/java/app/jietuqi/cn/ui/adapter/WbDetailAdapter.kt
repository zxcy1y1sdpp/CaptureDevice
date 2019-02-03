package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.entity.WbDetailsEntity
import app.jietuqi.cn.util.StringUtils

/**
 * 作者： liuyuanbo on 2019/1/29 16:58.
 * 时间： 2019/1/29 16:58
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WbDetailAdapter(val mList: ArrayList<WbDetailsEntity>) : RecyclerView.Adapter<WbDetailAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_wb_details, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val titleTv: TextView = itemView.findViewById(R.id.sTitleTv)
        private val timeTv: TextView = itemView.findViewById(R.id.sTimeTv)
        private val priceTv: TextView = itemView.findViewById(R.id.sPriceTv)
        private val statusTv: TextView = itemView.findViewById(R.id.sStatusTv)
        fun bind(entity: WbDetailsEntity) {
            titleTv.text = entity.intro
            timeTv.text = entity.create_time
            if ("0" == entity.type){
                priceTv.text = StringUtils.insertFrontAndBack(entity.amount, "-", "微币")
                statusTv.text = "购买成功"
            }else{
                priceTv.text = StringUtils.insertFrontAndBack(entity.amount, "+", "微币")
                statusTv.text = "充值成功"
            }
        }
    }
}
