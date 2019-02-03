package app.jietuqi.cn.wechat.simulator.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.EditText
import android.widget.GridView
import app.jietuqi.cn.R
import app.jietuqi.cn.ResourceHelper
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.*
import app.jietuqi.cn.wechat.entity.WechatBankEntity
import app.jietuqi.cn.wechat.entity.WechatWithdrawDepositEntity
import app.jietuqi.cn.wechat.simulator.widget.WechatInputPasswordDialog
import app.jietuqi.cn.wechat.simulator.widget.WechatPayDialog
import app.jietuqi.cn.wechat.simulator.widget.WechatSimulatorChoiceBankDialog
import com.bm.zlzq.utils.ScreenUtil
import kotlinx.android.synthetic.main.activity_wechat_simulator_recharge.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.reflect.Method
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*


/**
 * 作者： liuyuanbo on 2019/1/25 13:50.
 * 时间： 2019/1/25 13:50
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatSimulatorRechargeActivity : BaseWechatActivity(), WechatSimulatorChoiceBankDialog.OnItemSelectListener, ViewTreeObserver.OnGlobalLayoutListener, WechatInputPasswordDialog.OnInputEndListener {
    /**
     * 选择的银行
     */
    private var mSelectBank: WechatBankEntity = WechatBankEntity()
    /**
     * 0 -- 充值
     * 1 -- 提现
     */
    private var mType = 0
    private lateinit var mMyEntity: WechatUserEntity
    /**
     * 选中的位置
     */
    private var mPosition = 0
    /**
     * 输入监听事件
     */
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun afterTextChanged(s: Editable) {
            if(s.isNotEmpty()){
                if (s.toString().toDouble() > mMyEntity.cash){
                    mShowAllMoneyTv.text = "输入金额超过零钱余额"
                    mShowAllMoneyTv.setTextColor(Color.parseColor("#D0161F"))
                    mWithdrawAllMoneyTv.visibility = View.GONE
                }else{
                    mWithdrawAllMoneyTv.visibility = View.VISIBLE
                    if (s.toString().toDouble() < 105){
                        mShowAllMoneyTv.tag = 0.10
                        mShowAllMoneyTv.text = "额外扣除¥0.10服务费（费率0.10%）"
                    }else if (s.toString().toDouble() >= 105){
                        val money = BigDecimal(s.toString().toDouble() * 0.001).setScale(2, RoundingMode.UP).toDouble()
                        mShowAllMoneyTv.text = "额外扣除¥ " + money + "服务费（费率0.10%）"
                        mShowAllMoneyTv.tag = money
                    }
                    mShowAllMoneyTv.setTextColor(ContextCompat.getColor(this@WechatSimulatorRechargeActivity, R.color.wechatLightGray))
                }

                mNexMoveTv.alpha = 1f
                mWithdrawAllMoneyTv.visibility = View.GONE
            }else{
                mNexMoveTv.alpha = 0.3f
                mShowAllMoneyTv.text = StringUtils.insertFrontAndBack(StringUtils.keep2Point(mMyEntity.cash), "当前零钱余额", "元，")
                mWithdrawAllMoneyTv.visibility = View.VISIBLE
            }
        }
    }
    override fun end() {
        mWechatPayDialog.show()//显示,显示时页面不可点击,只能点击返回
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
            runOnUiThread {
                mWechatPayDialog.dismiss()
                if (mType == 0){
                    LaunchUtil.startWechatSimulatorRechargeSuccessActivity(this@WechatSimulatorRechargeActivity, OtherUtil.getContent(mChargeEt), mSelectBank)
                }else{
                    var entity = WechatWithdrawDepositEntity()
                    entity.bank = mSelectBank.bankShortName
                    entity.bankNum4 = mSelectBank.bankTailNumber
                    entity.money = OtherUtil.getContent(mChargeEt)
                    entity.serviceCharge = true
                    if ("当天24点前到账" == mSelectBank.bankReachTime){
                        entity.time = TimeUtil.stampToDateWithYMDHM(TimeUtil.getCurrentTimeEndMs() + 3600000 * 24) + " 23:59"
                    }else{
                        entity.time = TimeUtil.stampToDateWithYMDHM(TimeUtil.getCurrentTimeEndMs() + 3600000 * 2)
                    }
                    var mySelf = UserOperateUtil.getWechatSimulatorMySelf()
                    mySelf.cash -= entity.money.toDouble()
                    SharedPreferencesUtils.saveBean2Sp(mySelf, SharedPreferenceKey.WECHAT_SIMULATOR_MY_SIDE)
                    LaunchUtil.startWechatPreviewChangeWithdrawDepositActivity(this@WechatSimulatorRechargeActivity, entity)
                }
                finish()
            }
        }
    }

    override fun select(entity: WechatBankEntity, position: Int) {
        mPosition = position
        mSelectBank = entity
        mWechatSimulatorRechargeBankIv.setImageResource(ResourceHelper.getAppIconId(entity.bankIcon))
        mWechatSimulatorRechargeBankNameTv.text = entity.bankName + "(" + entity.bankTailNumber + ")"
        mWechatSimulatorRechargeBankLimitTv.text = StringUtils.insertFront(StringUtils.keep2Point(entity.bankLimitMoney), "单日交易限额¥")
    }

    private var enterAnim: Animation? = null
    private var exitAnim: Animation? = null
    private var gridView: GridView? = null
    private var valueList: ArrayList<Map<String, String>>? = null
    private lateinit var mWechatPayDialog: WechatPayDialog
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_recharge

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        mMyEntity = UserOperateUtil.getWechatSimulatorMySelf()
        val list = UserOperateUtil.getWechatSimulatorBank()
        mSelectBank = list[0]
        mWechatSimulatorRechargeBankIv.setImageResource(ResourceHelper.getAppIconId(mSelectBank.bankIcon))
        mWechatSimulatorRechargeBankNameTv.text = mSelectBank.bankShortName + "(" + mSelectBank.bankTailNumber + ")"
        mWechatSimulatorRechargeBankLimitTv.text = StringUtils.insertFront(StringUtils.keep2Point(mSelectBank.bankLimitMoney), "单日交易限额¥")
        setStatusBarColor(Color.parseColor("#EDEDED"))
        setLightStatusBarForM(this, true)
        mChargeEt.typeface = Typeface.createFromAsset(assets, "WeChatSansSS-Medium.ttf")
        enterAnim = AnimationUtils.loadAnimation(this, R.anim.push_bottom_in)
        exitAnim = AnimationUtils.loadAnimation(this, R.anim.push_bottom_out)

        // 设置不调用系统键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            mChargeEt.inputType = InputType.TYPE_NULL
        } else {
            this.window.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            try {
                val cls = EditText::class.java
                val setShowSoftInputOnFocus: Method
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", Boolean::class.javaPrimitiveType!!)
                setShowSoftInputOnFocus.isAccessible = true
                setShowSoftInputOnFocus.invoke(mChargeEt, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        valueList = mWechatSimulatorRechargeKeyBoardView.valueList
    }

    override fun initViewsListener() {
        mChargeScrollView.viewTreeObserver.addOnGlobalLayoutListener(this)
        mWechatSimulatorPreviewBackITv.setOnClickListener(this)
        mWechatSimulatorRechargeKeyBoardView.layoutBack.setOnClickListener {
            mWechatSimulatorRechargeKeyBoardView.startAnimation(exitAnim)
            mWechatSimulatorRechargeKeyBoardView.visibility = View.GONE
        }

        gridView = mWechatSimulatorRechargeKeyBoardView.gridView
        gridView?.onItemClickListener = onItemClickListener

        mChargeEt.setOnClickListener {
            mWechatSimulatorRechargeKeyBoardView.isFocusable = true
            mWechatSimulatorRechargeKeyBoardView.isFocusableInTouchMode = true

            mWechatSimulatorRechargeKeyBoardView.startAnimation(enterAnim)
            mWechatSimulatorRechargeKeyBoardView.visibility = View.VISIBLE
        }
        mChoiceOtherBankLayout.setOnClickListener(this)
        mNexMoveTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        if (mType == 1){
            mTitleTv.text = "零钱提现"
            mTypeTitleTv.text = "到账银行"
            mTypeTitle2Tv.text = "提现金额"
            mNexMoveTv.text = "提现"
            mNexMoveTv.alpha = 0.2f
            mWechatSimulatorRechargeReachTimeTv.visibility = View.VISIBLE
            mWechatSimulatorRechargeBankLimitTv.visibility = View.GONE
            mChargeEt.addTextChangedListener(textWatcher)
            mAllMoneyLayout.visibility = View.VISIBLE
            mWithdrawAllMoneyTv.setOnClickListener(this)

            mShowAllMoneyTv.text = StringUtils.insertFrontAndBack(StringUtils.keep2Point(mMyEntity.cash), "当前零钱余额", "元，")
            mShowAllMoneyTv.tag = mMyEntity.cash
            if ("当天24点前到账" == mSelectBank.bankReachTime){
                mWechatSimulatorRechargeReachTimeTv.setTextColor(Color.parseColor("#E3B676"))
            }else{
                mWechatSimulatorRechargeReachTimeTv.setTextColor(ContextCompat.getColor(this, R.color.wechatLightGray))
            }
        }
    }
    override fun onGlobalLayout() {
        mChargeScrollView.post {
            GlobalScope.launch { // 在一个公共线程池中创建一个协程
                delay(500L) // 非阻塞的延迟一秒（默认单位是毫秒）
                runOnUiThread {
                    mChargeScrollView.fullScroll(View.FOCUS_DOWN)
                    mChargeScrollView.smoothScrollTo(0, 200)
                    mChargeScrollView.viewTreeObserver.removeOnGlobalLayoutListener(this@WechatSimulatorRechargeActivity)
                }
            }
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWithdrawAllMoneyTv ->{
                mChargeEt.setText(StringUtils.keep2Point(mShowAllMoneyTv.tag.toString()))
            }
            R.id.mWechatSimulatorPreviewBackITv ->{
                finish()
            }
            R.id.mChoiceOtherBankLayout ->{
                var dialog = WechatSimulatorChoiceBankDialog()
                dialog.setData(this, mType, mPosition)
                dialog.show(supportFragmentManager, "Dialog")

            }
            R.id.mNexMoveTv ->{
                if (TextUtils.isEmpty(OtherUtil.getContent(mChargeEt))){
                    showToast("请输入正确的金额")
                    return
                }
                mWechatPayDialog = WechatPayDialog(this)
                mWechatPayDialog.show()//显示,显示时页面不可点击,只能点击返回
                val params: WindowManager.LayoutParams  = mWechatPayDialog.window.attributes
                params.width = ScreenUtil.getScreenWidth(this) / 2
                params.height = params.width
                mWechatPayDialog?.window?.attributes = params
                GlobalScope.launch { // 在一个公共线程池中创建一个协程
                    delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
                    runOnUiThread {
                        mWechatPayDialog.dismiss()
                        showLoadingDialog("正在加载")
                        GlobalScope.launch {
                            delay(300L) // 非阻塞的延迟一秒（默认单位是毫秒）
                            dismissLoadingDialog()
                            var dialog = WechatInputPasswordDialog()
                            dialog.setData(mSelectBank, mType, OtherUtil.getContent(mChargeEt), mShowAllMoneyTv.tag.toString())
                            dialog.setListener(this@WechatSimulatorRechargeActivity)
                            dialog.show(supportFragmentManager, "Dialog")
                        }
                    }
                }
            }
        }
    }
    private val onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
        if (position < 11 && position != 9) {    //点击0~9按钮

            var amount = mChargeEt.text.toString().trim { it <= ' ' }
            amount += valueList?.get(position)?.get("name")!!

            mChargeEt.setText(amount)

            val ea = mChargeEt.text
            mChargeEt.setSelection(ea.length)
        } else {

            if (position == 9) {      //点击退格键
                var amount = mChargeEt.text.toString().trim { it <= ' ' }
                if (!amount.contains(".")) {
                    amount += valueList?.get(position)?.get("name")!!
                    mChargeEt.setText(amount)

                    val ea = mChargeEt.text
                    mChargeEt.setSelection(ea.length)
                }
            }

            if (position == 11) {      //点击退格键
                var amount = mChargeEt.text.toString().trim { it <= ' ' }
                if (amount.isNotEmpty()) {
                    amount = amount.substring(0, amount.length - 1)
                    mChargeEt.setText(amount)

                    val ea = mChargeEt.text
                    mChargeEt.setSelection(ea.length)
                }
            }
        }
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onResume() {
        super.onResume()
        needVipForCover()
    }
}
