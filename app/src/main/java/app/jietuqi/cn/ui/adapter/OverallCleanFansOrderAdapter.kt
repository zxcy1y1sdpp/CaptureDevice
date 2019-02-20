package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.OverallCleanFansListEntity
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.TimeUtil

/**
 * 作者： liuyuanbo on 2018/11/20 11:31.
 * 时间： 2018/11/20 11:31
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class OverallCleanFansOrderAdapter(val mList: ArrayList<OverallCleanFansListEntity>) : RecyclerView.Adapter<OverallCleanFansOrderAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverallCleanFansOrderAdapter.Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_clean_fans_order, parent, false))
    }

    override fun getItemCount() = mList.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val orderTv: TextView = itemView.findViewById(R.id.sGoodsOrderTv)
//        private val goodsIconIv: ImageView = itemView.findViewById(R.id.sGoodsIconIv)
        private val titleTv: TextView = itemView.findViewById(R.id.sGoodsTitleTv)
        private val priceTv: TextView = itemView.findViewById(R.id.sGoodsPriceTv)
        private val countTv: TextView = itemView.findViewById(R.id.sGoodsCountTv)
        private val timeTv: TextView = itemView.findViewById(R.id.sGoodsTimeTv)
        private val totalPriceTv: TextView = itemView.findViewById(R.id.sGoodsTotalPriceTv)
        fun bind(entity: OverallCleanFansListEntity){
            orderTv.text = StringUtils.insertFront(entity.sn, "订单编号：")
            titleTv.text = entity.title
            priceTv.text = StringUtils.insertFront(entity.price, "¥")
            countTv.text = StringUtils.insertFront(entity.number, "x")
            timeTv.text = TimeUtil.getAllSpecTime(entity.create_time)
            totalPriceTv.text = StringUtils.insertFront(entity.money, "¥")
        }
    }
}
