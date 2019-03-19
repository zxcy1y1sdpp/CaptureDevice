package app.jietuqi.cn.widget.dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import kotlinx.android.synthetic.main.dialog_choice_wechat_background.*
/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 选择角色或者编辑角色
 */

class ChoiceWechatBackgroundDialog : BottomSheetDialogFragment(), View.OnClickListener{
    private lateinit var mListener: OnChoiceSingleTalkBgListener
    private var mFirstTitle = "自定义背景"
    private var mSecondTitle = "默认背景"
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_choice_wechat_background, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogCustomBg.text = mFirstTitle
        dialogDefaultBg.text = mSecondTitle
        dialogCustomBg.setOnClickListener(this)
        dialogDefaultBg.setOnClickListener(this)
        dialogCancelView.setOnClickListener(this)
    }
    fun setListener(listener: OnChoiceSingleTalkBgListener){
        mListener = listener
    }
    fun setTitle(firstTitle: String, secondTitle: String){
        mFirstTitle = firstTitle
        mSecondTitle = secondTitle
    }
    interface OnChoiceSingleTalkBgListener{
        fun onChoice(need: Boolean)
    }
}
