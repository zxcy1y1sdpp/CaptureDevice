package dasheng.com.capturedevice.wechat.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.entity.BankEntity
import dasheng.com.capturedevice.util.GlideUtil
import java.util.ArrayList

/**
 * 作者： liuyuanbo on 2018/10/23 14:57.
 * 时间： 2018/10/23 14:57
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class ChoiceBankAdapter(val mList: ArrayList<BankEntity>) : RecyclerView.Adapter<ChoiceBankAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_select_bank, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount() = mList.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val bankPicIv: ImageView = itemView.findViewById(R.id.bankPicIv)
        private val bankNameTv: TextView = itemView.findViewById(R.id.bankNameTv)
        fun bind(entity: BankEntity){
            bankNameTv.text = entity.bankName
            GlideUtil.display(itemView.context, entity.bankPic, bankPicIv)
        }
    }
}
