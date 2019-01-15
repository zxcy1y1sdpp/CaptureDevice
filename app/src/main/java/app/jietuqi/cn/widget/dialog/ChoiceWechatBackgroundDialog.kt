package app.jietuqi.cn.widget.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.widget.ShapeCornerBgView


/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 选择角色或者编辑角色
 */

class ChoiceWechatBackgroundDialog : BottomSheetDialogFragment(), View.OnClickListener{
    private lateinit var mListener: OnChoiceSingleTalkBgListener
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.dialogCustomBg ->{
                mListener.onChoice(true)
            }
            R.id.dialogDefaultBg ->{
                mListener.onChoice(false)
            }
        }
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.dialog_choice_wechat_background, null)
        dialog.setContentView(view)
        view.findViewById<ShapeCornerBgView>(R.id.dialogCustomBg).setOnClickListener(this)
        view.findViewById<ShapeCornerBgView>(R.id.dialogDefaultBg).setOnClickListener(this)
        view.findViewById<ShapeCornerBgView>(R.id.dialogCancelView).setOnClickListener(this)
        return dialog
    }
    fun setListener(listener: OnChoiceSingleTalkBgListener){
        mListener = listener
    }
    interface OnChoiceSingleTalkBgListener{
        fun onChoice(need: Boolean)
    }
}
