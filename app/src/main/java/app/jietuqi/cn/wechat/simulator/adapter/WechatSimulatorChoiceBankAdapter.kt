package app.jietuqi.cn.wechat.simulator.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ResourceHelper
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.wechat.entity.WechatBankEntity
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatChoiceBankActivity

/**
 * 作者： liuyuanbo on 2018/10/23 14:57.
 * 时间： 2018/10/23 14:57
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class WechatSimulatorChoiceBankAdapter(val mList: MutableList<WechatBankEntity>, val mCloseListener: ChoiceListener, val mType: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val CONTENT_TYPE = 1
    private val BOTTOM_TYPE = 2
    override fun getItemViewType(position: Int): Int {
        return if (position <= mList.size - 1){
            CONTENT_TYPE
        }else{
            BOTTOM_TYPE
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CONTENT_TYPE -> ContentHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_simulator_choice_bank2, parent, false))
            else -> BottomHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_simulator_choice_bank3, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == CONTENT_TYPE){
            if (holder is ContentHolder) {
                holder.bind(mList[position])
            }
        }
    }

    override fun getItemCount() = mList.size + 1

    inner class ContentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val bankPicIv: ImageView = itemView.findViewById(R.id.sBankIconIv)
        private val bankNameTv: TextView = itemView.findViewById(R.id.sBankNameTv)
        private val reachTimeTv: TextView = itemView.findViewById(R.id.sBankReachTimeTv)
        private val choice: CheckBox = itemView.findViewById(R.id.sBankChoiceCb)
        init {
            itemView.setOnClickListener{
                var entity: WechatBankEntity
                val choiceEntity = mList[adapterPosition]
                for (i in mList.indices) {
                    entity = mList[i]
                    entity.isCheck = false
                }
                choiceEntity.isCheck = true
                mCloseListener.choice(choiceEntity, adapterPosition)
                notifyDataSetChanged()

            }
        }
        fun bind(entity: WechatBankEntity){
            if (mType == 1){
                reachTimeTv.text = entity.bankReachTime
                reachTimeTv.visibility = View.VISIBLE
            }
            bankNameTv.text = entity.bankName + "(" + entity.bankTailNumber +")"
            bankPicIv.setImageResource(ResourceHelper.getAppIconId(entity.bankIcon))
            choice.isChecked = entity.isCheck
        }
    }
    inner class BottomHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val bankPicIv: ImageView = itemView.findViewById(R.id.sBankIconIv)
        private val bankNameTv: TextView = itemView.findViewById(R.id.sBankNameTv)
        init {
            if (mType == 1){
                bankPicIv.visibility = View.INVISIBLE
                bankNameTv.text = "使用新卡提现"
            }
            itemView.setOnClickListener{
                mCloseListener.choice(null, 0)
                LaunchUtil.launch(itemView.context, WechatChoiceBankActivity::class.java)
            }
        }
    }

    interface ChoiceListener{
        fun choice(bankEntity: WechatBankEntity?, position: Int)
    }
}
