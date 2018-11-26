package app.jietuqi.cn.widget.dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.entity.EditDialogEntity
import kotlinx.android.synthetic.main.dialog_badge.*

/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 选择支付宝会员等级的底部弹框
 */

class BadgeDialog : BottomSheetDialogFragment(), View.OnClickListener{
    private var mItemSelectionListener: OnItemSelectListener? = null
    private var mEditDialogChoicelistener: EditDialogChoiceListener? = null
    /**
     * 确定选择点击的位置，防止展示红点的时候位置出错
     */
    private var mPosition = 0
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.point ->{ mItemSelectionListener?.select(0, mPosition) }
            R.id.number ->{
                val dialog = EditDialog()
                dialog.setData(mEditDialogChoicelistener, EditDialogEntity(mPosition, "", "设置消息数量"))
                dialog.show(activity?.supportFragmentManager, "edit")
            }
            R.id.nothing ->{ mItemSelectionListener?.select(2, mPosition)}
        }
        dismiss()
    }

    fun setOnItemSelectListener(itemSelection: OnItemSelectListener, listener: EditDialogChoiceListener, position: Int){
        mItemSelectionListener = itemSelection
        mEditDialogChoicelistener = listener
        this.mPosition = position
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_badge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        point.setOnClickListener(this)
        number.setOnClickListener(this)
        nothing.setOnClickListener(this)
        cancel.setOnClickListener(this)
    }

    interface OnItemSelectListener{
        fun select(position: Int, parentPosition: Int)
    }
}
