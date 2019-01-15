package app.jietuqi.cn.wechat.create

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.entity.BankEntity
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.util.*
import app.jietuqi.cn.wechat.entity.WechatWithdrawDepositEntity
import app.jietuqi.cn.widget.dialog.ChoiceBankDialog
import kotlinx.android.synthetic.main.activity_wechat_change_withdraw_deposit.*
import kotlinx.android.synthetic.main.include_wechat_preview_btn.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.RoundingMode
import java.text.NumberFormat

/**
 * 作者： liuyuanbo on 2018/10/22 15:56.
 * 时间： 2018/10/22 15:56
 * 邮箱： 972383753@qq.com
 * 用途： 微信提现
 */

class WechatChangeWithdrawDepositActivity : BaseWechatActivity(), ChoiceBankDialog.OnItemSelectListener{
    /**
     * 是否需要手续费
     */
    private var mNeedServiceCharge = true
    private var mEntity: WechatWithdrawDepositEntity? = WechatWithdrawDepositEntity()
    override fun setLayoutResourceId() = R.layout.activity_wechat_change_withdraw_deposit

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setWechatViewTitle("微信提现", 0)
        val time = TimeUtil.getNowTime()
        mExpectedToAccountTv.text = time
        mEntity?.time = time
        System.out.println("12.5的四舍五入值：" + Math.round(12.5))
        System.out.println("-12.5的四舍五入值：" + Math.round(-12.5))
//        System.out.println("0.105的四舍五入值：" + )

        val nf = NumberFormat.getNumberInstance()
        // 保留两位小数
        nf.maximumFractionDigits = 2
        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.roundingMode = RoundingMode.UP
        System.out.println("0.105的四舍五入值-----：" + nf.format(0.105
        ))
    }

    override fun initViewsListener() {
        mExpectedToAccountLayout.setOnClickListener(this)
        mSelectBankNumLayout.setOnClickListener(this)
        mOnOrOffIv.setOnClickListener(this)
        previewBtn.setOnClickListener(this)
        onlyTowEditTextNeedTextWatcher(mBankNumEt, mDepositMoneyEt, this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mExpectedToAccountLayout ->{//预计到账时间
                initTimePickerView("", 1)
            }
            R.id.mSelectBankNumLayout ->{//选择银行
                var dialog = ChoiceBankDialog()
                dialog.setOnItemSelectListener(this)
                dialog.show(supportFragmentManager, "Dialog")
            }
            R.id.mOnOrOffIv ->{//提现手续费
                mNeedServiceCharge = !mNeedServiceCharge
                OtherUtil.onOrOff(mNeedServiceCharge, mOnOrOffIv)
                mEntity?.serviceCharge = mNeedServiceCharge
            }
            R.id.previewBtn ->{//选择银行
                mEntity?.money = mDepositMoneyEt.text.toString()
                mEntity?.bankNum4 = StringUtils.insertFront(mBankNumEt.text.toString(), "尾号")
                mEntity?.bank = mBankTv.text.toString()
                LaunchUtil.startWechatPreviewChangeWithdrawDepositActivity(this, mEntity)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        mEntity?.time = timeEntity.timeWithoutS
        mExpectedToAccountTv.text = timeEntity.timeWithoutS
    }
    override fun select(entity: BankEntity) {
        mEntity?.bank = entity.bankName
        mBankTv.text = entity.bankName
    }
    override fun onDestroy() {
        EventBusUtil.unRegister(this)
        super.onDestroy()
    }
}
