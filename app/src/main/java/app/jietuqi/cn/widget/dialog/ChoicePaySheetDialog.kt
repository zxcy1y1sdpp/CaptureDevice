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

class ChoicePaySheetDialog : BottomSheetDialogFragment(), View.OnClickListener{
    private var mOnItemChangeListener: OnItemChangeListener? = null
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.dialogChangeRoleView ->{
                mOnItemChangeListener?.click("更换角色")
            }
            R.id.dialogEditRoleView ->{
                mOnItemChangeListener?.click("编辑角色")
            }
        }
        dismiss()
    }
    fun setListener(listener: OnItemChangeListener){
        mOnItemChangeListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.dialogsheet_change_role, null)
        dialog.setContentView(view)
        view.findViewById<TextView>(R.id.dialogChangeRoleView).setOnClickListener(this)
        view.findViewById<TextView>(R.id.dialogEditRoleView).setOnClickListener(this)
        view.findViewById<TextView>(R.id.dialogCancelView).setOnClickListener(this)
        return dialog
    }
    interface OnItemChangeListener{
        /**
         * 支付宝
         * 微信
         */
        fun click(type: String)
    }

    override fun onDestroy() {
        super.onDestroy()
        mOnItemChangeListener = null
    }
}
