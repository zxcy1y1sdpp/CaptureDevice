package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.AgencyIncomeAndExpensesEntity
import app.jietuqi.cn.util.StringUtils

/**
 * 作者： liuyuanbo on 2019/1/29 16:58.
 * 时间： 2019/1/29 16:58
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class AgencyIncomeAndExpensesAdapter(val mList: ArrayList<AgencyIncomeAndExpensesEntity.Data>) : RecyclerView.Adapter<AgencyIncomeAndExpensesAdapter.Holder>() {

    private var mType = "1"
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_agency_income_and_expenses, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val titleTv: TextView = itemView.findViewById(R.id.sTitleTv)
        private val timeTv: TextView = itemView.findViewById(R.id.sTimeTv)
        private val priceTv: TextView = itemView.findViewById(R.id.sPriceTv)
        fun bind(entity: AgencyIncomeAndExpensesEntity.Data) {
            timeTv.text = entity.create_time
            if (-1 == entity.type.value){//提现支出
                when(entity.status){
                    0 ->{//正在提现
                        titleTv.text = "正在提现"
                    }
                    1 ->{//提现成功
                        titleTv.text = "提现完成"
                    }
                }
                priceTv.text = StringUtils.insertFrontAndBack(entity.money, "-", "元")
            }else{//收入
                titleTv.text = entity.type.text
                priceTv.text = StringUtils.insertFrontAndBack(entity.money, "+", "元")
            }
        }
    }
}
