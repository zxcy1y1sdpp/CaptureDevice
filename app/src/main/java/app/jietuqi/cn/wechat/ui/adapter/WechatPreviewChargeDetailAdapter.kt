package app.jietuqi.cn.wechat.ui.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.wechat.entity.WechatChargeDetailEntity

/**
 * 作者： liuyuanbo on 2018/10/30 16:46.
 * 时间： 2018/10/30 16:46
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class WechatPreviewChargeDetailAdapter(val mList: ArrayList<WechatChargeDetailEntity>) : RecyclerView.Adapter<WechatPreviewChargeDetailAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount() = mList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_preview_charge_detail, parent, false))
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val title: TextView = itemView.findViewById(R.id.typeNameTv)
        private val time: TextView = itemView.findViewById(R.id.timeTv)
        private val money: TextView = itemView.findViewById(R.id.moneyTv)
        fun bind(entity: WechatChargeDetailEntity){
            title.text = entity.name
            time.text = entity.time
            if (entity.type == "0"){
                money.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "+ ")
                money.setTextColor(ContextCompat.getColor(itemView.context, R.color.overallGreen))
            }else{
                money.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "- ")
                money.setTextColor(ContextCompat.getColor(itemView.context, R.color.wechat_deep_text_color))
            }
        }
    }
}
