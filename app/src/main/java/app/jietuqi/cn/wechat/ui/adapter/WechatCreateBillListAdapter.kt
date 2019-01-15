package app.jietuqi.cn.wechat.ui.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.wechat.entity.WechatCreateBillsEntity
import java.io.File
import java.util.*

/**
 * 作者： liuyuanbo on 2018/12/24 13:39.
 * 时间： 2018/12/24 13:39
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatCreateBillListAdapter(val mList: ArrayList<WechatCreateBillsEntity>) : RecyclerView.Adapter<WechatCreateBillListAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_select_bank, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount() = mList.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val icon: ImageView = itemView.findViewById(R.id.sWechatCreateBillListIconIv)
        private val titleTv: TextView = itemView.findViewById(R.id.sWechatCreateBillTitleTv)
        private val time: TextView = itemView.findViewById(R.id.sWechatCreateBillTimeTv)
        private val money: TextView = itemView.findViewById(R.id.sWechatCreateBillMoneyTv)
        fun bind(entity: WechatCreateBillsEntity){
            if(entity.type != "相册自定义"){
                GlideUtil.displayHead(itemView.context, entity.iconInt, icon)
            }else{
                GlideUtil.displayHead(itemView.context, File(entity.iconString), icon)
            }
            titleTv.text = entity.title
            time.text = entity.time
            if (entity.incomeAndExpenses == 0){//收入
                money.setTextColor(ContextCompat.getColor(itemView.context, R.color.pickerview_timebtn_nor))
                money.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "+")
            }else{
                money.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                money.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "-")
            }

        }
    }
}
