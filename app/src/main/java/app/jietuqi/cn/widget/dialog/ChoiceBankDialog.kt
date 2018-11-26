package app.jietuqi.cn.widget.dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.constant.BankFinal
import app.jietuqi.cn.entity.BankEntity
import app.jietuqi.cn.wechat.ui.adapter.ChoiceBankAdapter
import kotlinx.android.synthetic.main.dialog_select_bank.*

/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 选择银行卡的底部弹框
 */

class ChoiceBankDialog : BottomSheetDialogFragment(){
    private var mAdapter: ChoiceBankAdapter? = null
    private var mItemSelectionListener: OnItemSelectListener? = null

    fun setOnItemSelectListener(itemSelection: OnItemSelectListener){
        mItemSelectionListener = itemSelection
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.dialog_select_bank, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BankFinal.initBankData()
        mAdapter = ChoiceBankAdapter(BankFinal.BANK_LIST)
        mBankRecyclerView.adapter = mAdapter
        mBankRecyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(mBankRecyclerView) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val position = vh.adapterPosition
                mItemSelectionListener?.select(BankFinal.BANK_LIST[position])
                dismiss()
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
    }

    interface OnItemSelectListener{
        fun select(entity: BankEntity)
    }
}
