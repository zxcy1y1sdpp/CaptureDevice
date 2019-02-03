package app.jietuqi.cn.wechat.simulator.widget

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import app.jietuqi.cn.R
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.entity.WechatBankEntity
import kotlinx.android.synthetic.main.dialog_wechat_input_password.*
import java.lang.reflect.Method
import java.util.*

/**
 * 作者： liuyuanbo on 2019/1/26 11:40.
 * 时间： 2019/1/26 11:40
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatInputPasswordDialog : BottomSheetDialogFragment(), View.OnClickListener{
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.payDialogCloseBtn ->{
                dismiss()
            }
        }
    }

    private var enterAnim: Animation? = null
    private var exitAnim: Animation? = null
    private var gridView: GridView? = null
    private var valueList: ArrayList<Map<String, String>>? = null
    private var mEntity: WechatBankEntity = WechatBankEntity()
    private var mType = 0
    private var mMoney = ""
    private var mServerMoney = ""
    /**
     * 输入监听事件
     */
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun afterTextChanged(s: Editable) {
            if(s.length == 6){
                mOnInputEndListener?.end()
                dismiss()
            }
        }
    }
    override fun onStart() {
        super.onStart()
        view?.post {
            val parent = view?.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior<*>
            bottomSheetBehavior.isHideable = false
            view?.measuredHeight?.let { bottomSheetBehavior.peekHeight = it }
//            parent.setBackgroundColor(Color.WHITE)
            parent.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    private var mOnInputEndListener: OnInputEndListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_wechat_input_password, container, false)
    }

    fun setData(bankEntity: WechatBankEntity, type: Int, money: String, serverMoney: String){
        mEntity = bankEntity
        mType = type
        mMoney = money
        mServerMoney = serverMoney
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mType == 1){
            titleTv.text = "零钱提现"
            serverMoneyTv.text = StringUtils.insertFrontAndBack(mServerMoney, "额外扣除Ұ", "服务费")
            serverMoneyTv.visibility = View.VISIBLE
//            mLayout.visibility = View.GONE
        }
        payDialogMoneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(mMoney), "¥")
        GlideUtil.displayHead(activity, UserOperateUtil.getWechatSimulatorMySelf().getAvatarFile(), payDialogAvatarIv)
//        payDialogBankIconIv.setImageResource(ResourceHelper.getAppIconId(mEntity.bankIcon))
//        payDialogBankNameIv.text = mEntity.bankName + "(" + mEntity.bankTailNumber + ")"
        payDialogCloseBtn.setOnClickListener(this)
        payDialogPassWordEt.addTextChangedListener(textWatcher)
        enterAnim = AnimationUtils.loadAnimation(activity, R.anim.push_bottom_in)
        exitAnim = AnimationUtils.loadAnimation(activity, R.anim.push_bottom_out)

        // 设置不调用系统键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            payDialogPassWordEt.inputType = InputType.TYPE_NULL
        } else {
            activity?.window?.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            try {
                val cls = EditText::class.java
                val setShowSoftInputOnFocus: Method
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType!!)
                setShowSoftInputOnFocus.isAccessible = true
                setShowSoftInputOnFocus.invoke(payDialogPassWordEt, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        valueList = payDialogBanKeyboardView.valueList
        payDialogBanKeyboardView.layoutBack.setOnClickListener {
            payDialogBanKeyboardView.startAnimation(exitAnim)
            payDialogBanKeyboardView.visibility = View.GONE
        }
        gridView = payDialogBanKeyboardView.gridView
        gridView?.onItemClickListener = onItemClickListener

        payDialogPassWordEt.setOnClickListener {
            payDialogBanKeyboardView.isFocusable = true
            payDialogBanKeyboardView.isFocusableInTouchMode = true

            payDialogBanKeyboardView.startAnimation(enterAnim)
            payDialogBanKeyboardView.visibility = View.VISIBLE
        }
    }

    fun setListener(listener: OnInputEndListener){
        mOnInputEndListener = listener
    }
    interface OnInputEndListener{
        fun end()
    }
    private val onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
        if (position < 11 && position != 9) {    //点击0~9按钮

            var amount = payDialogPassWordEt.text.toString().trim { it <= ' ' }
            amount += valueList?.get(position)?.get("name")!!

            payDialogPassWordEt.setText(amount)

            val ea = payDialogPassWordEt.text
            payDialogPassWordEt.setSelection(ea.length)
        } else {

            if (position == 9) {      //点击退格键
                var amount = payDialogPassWordEt.text.toString().trim { it <= ' ' }
                if (!amount.contains(".")) {
                    amount += valueList?.get(position)?.get("name")!!
                    payDialogPassWordEt.setText(amount)

                    val ea = payDialogPassWordEt.text
                    payDialogPassWordEt.setSelection(ea.length)
                }
            }

            if (position == 11) {      //点击退格键
                var amount = payDialogPassWordEt.text.toString().trim { it <= ' ' }
                if (amount.isNotEmpty()) {
                    amount = amount.substring(0, amount.length - 1)
                    payDialogPassWordEt.setText(amount)

                    val ea = payDialogPassWordEt.text
                    payDialogPassWordEt.setSelection(ea.length)
                }
            }
        }
    }
}
