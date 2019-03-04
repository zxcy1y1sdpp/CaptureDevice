package app.jietuqi.cn.alipay.create

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.alipay.entity.AlipayCreateWithdrawDepositBillEntity
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.entity.BankEntity
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.TimeUtil
import app.jietuqi.cn.widget.dialog.ChoiceBankDialog
import app.jietuqi.cn.widget.dialog.EditDialog
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.activity_alipay_create_withdraw_deposit_bill.*
import kotlinx.android.synthetic.main.include_wechat_preview_btn.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/11/3 16:30.
 * 时间： 2018/11/3 16:30
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝 -- 生成 -- 提现账单
 */

class AlipayCreateWithdrawDepositBillActivity : BaseCreateActivity(), ChoiceBankDialog.OnItemSelectListener, EditDialogChoiceListener {
    val mEntity: AlipayCreateWithdrawDepositBillEntity = AlipayCreateWithdrawDepositBillEntity()
    override fun onChoice(entity: EditDialogEntity?) {
        mAlipayCreateWithdrawDepositBillClassifyTv.text = entity?.content
    }
    override fun select(entity: BankEntity) {
        mEntity.bank = entity.bankName
        mEntity.bankPic = entity.bankPic
        mAlipayCreateWithdrawDepositBillBankNameTv.text = entity.bankName
    }

    override fun setLayoutResourceId() = R.layout.activity_alipay_create_withdraw_deposit_bill

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        registerEventBus()
        setCreateTitle("提现账单", 0)
        mEntity.serviceCharge = true
        mAlipayCreateWithdrawDepositBillTimeTv.text = TimeUtil.getNowTime()
        mAlipayCreateWithdrawDepositBillArriveTimeTv.text = TimeUtil.getNowTime()
        mEntity.bank = "中国银行"
        mEntity.bankPic = R.mipmap.bank_boc
        onlyThreeEditTextNeedTextWatcher(mAlipayCreateWithdrawDepositBillBankNumEt, mAlipayCreateWithdrawDepositBillNameEt, mAlipayCreateWithdrawDepositBillMoneyEt)
    }

    override fun initViewsListener() {
        mAlipayCreateWithdrawDepositBillSelectBankLayout.setOnClickListener(this)
        previewBtn.setOnClickListener(this)
        mAlipayCreateWithdrawDepositBillOnOrOffIv.setOnClickListener(this)
        mAlipayCreateWithdrawDepositBillClassifyLayout.setOnClickListener(this)
        mAlipayCreateWithdrawDepositBillTimeLayout.setOnClickListener(this)
        mAlipayCreateWithdrawDepositBillArriveTimeLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAlipayCreateWithdrawDepositBillTimeLayout ->{
                initTimePickerView("提现时间", 1)
            }
            R.id.mAlipayCreateWithdrawDepositBillArriveTimeLayout ->{
                initTimePickerView("到账时间", 1)
            }
            R.id.mAlipayCreateWithdrawDepositBillSelectBankLayout ->{
                var dialog = ChoiceBankDialog()
                dialog.setOnItemSelectListener(this)
                dialog.show(supportFragmentManager, "Dialog")
            }
            R.id.mAlipayCreateWithdrawDepositBillOnOrOffIv ->{
                mEntity.serviceCharge = !mEntity.serviceCharge
                OtherUtil.onOrOff(mEntity.serviceCharge, mAlipayCreateWithdrawDepositBillOnOrOffIv)
            }
            R.id.previewBtn ->{
                mEntity.bankNum4 = StringUtils.insertFrontAndBack(mAlipayCreateWithdrawDepositBillBankNumEt.text.toString(), "（", "）")
                mEntity.name = mAlipayCreateWithdrawDepositBillNameEt.text.toString()
                mEntity.money = mAlipayCreateWithdrawDepositBillMoneyEt.text.toString()
                mEntity.classify = mAlipayCreateWithdrawDepositBillClassifyTv.text.toString()
                mEntity.withdrawDepositTime = mAlipayCreateWithdrawDepositBillTimeTv.text.toString().substring(5, mAlipayCreateWithdrawDepositBillTimeTv.text.toString().length)
                mEntity.createTime = mAlipayCreateWithdrawDepositBillTimeTv.text.toString()
                mEntity.successTime = mAlipayCreateWithdrawDepositBillArriveTimeTv.text.toString().substring(5, mAlipayCreateWithdrawDepositBillArriveTimeTv.text.toString().length)

                LaunchUtil.startAlipayPreviewWithdrawDepositBillActivity(this, mEntity)
            }
            R.id.mAlipayCreateWithdrawDepositBillClassifyLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(-1, "账单分类", "其他"))
                dialog.show(supportFragmentManager, "classify")
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        if (timeEntity.tag == "提现时间"){
            mAlipayCreateWithdrawDepositBillTimeTv.text = timeEntity.timeWithoutS

        }else{
            mAlipayCreateWithdrawDepositBillArriveTimeTv.text = timeEntity.timeWithoutS
        }
    }

    override fun onDestroy() {
        EventBusUtil.unRegister(this)
        super.onDestroy()
    }
}

