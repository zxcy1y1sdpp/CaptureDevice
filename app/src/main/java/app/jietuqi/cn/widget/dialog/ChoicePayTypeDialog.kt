package app.jietuqi.cn.widget.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import android.widget.TextView
import app.jietuqi.cn.R



/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 选择支付宝会员等级的底部弹框
 */

class ChoicePayTypeDialog : BottomSheetDialogFragment(), View.OnClickListener{
    private var mOnPayTypeSelectListener: OnPayTypeSelectListener? = null
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.sAiPayItemView ->{
                mOnPayTypeSelectListener?.payType("支付宝")
            }
            R.id.sWeChatPayItemView ->{
                mOnPayTypeSelectListener?.payType("微信")
            }
        }
        dismiss()
    }
    fun setListener(itemSelection: OnPayTypeSelectListener){
        mOnPayTypeSelectListener = itemSelection
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.dialog_choice_pay_type, null)
        dialog.setContentView(view)
        view.findViewById<TextView>(R.id.sAiPayItemView).setOnClickListener(this)
        view.findViewById<TextView>(R.id.sWeChatPayItemView).setOnClickListener(this)
        view.findViewById<TextView>(R.id.sCancelItemView).setOnClickListener(this)
        dialog.window.findViewById<View>(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent)
        return dialog
    }
    interface OnPayTypeSelectListener{
        /**
         * 支付宝
         * 微信
         */
        fun payType(type: String)
    }

    override fun onDestroy() {
        super.onDestroy()
        mOnPayTypeSelectListener = null
    }
}
