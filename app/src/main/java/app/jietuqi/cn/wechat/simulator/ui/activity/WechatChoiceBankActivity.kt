package app.jietuqi.cn.wechat.simulator.ui.activity

import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.entity.BankEntity
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.entity.WechatBankEntity
import app.jietuqi.cn.widget.dialog.ChoiceBankDialog
import kotlinx.android.synthetic.main.activity_wechat_choice_bank.*

/**
 * 作者： liuyuanbo on 2019/1/25 22:08.
 * 时间： 2019/1/25 22:08
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatChoiceBankActivity : BaseOverallActivity(), ChoiceBankDialog.OnItemSelectListener{
    private var mEntity = WechatBankEntity()
    override fun select(entity: BankEntity) {
        when(entity.bankName){
            "农业银行" ->{
                mEntity.bankIcon = "R.drawable.wechat_nongyeyinhang"
                mEntity.bankName = "农业银行储蓄卡"
                mEntity.bankShortName = "农业银行"
                mEntity.bankLimitMoney = 20000
                mEntity.bankReachTime = "2小时内到账"
            }
            "中国银行" ->{
                mEntity.bankIcon = "R.drawable.wechat_zhongguoyinhang"
                mEntity.bankName = "中国银行储蓄卡"
                mEntity.bankShortName = "中国银行"
                mEntity.bankLimitMoney = 20000
                mEntity.bankReachTime = "当天24点前到账"
            }
            "建设银行" ->{
                mEntity.bankIcon = "R.drawable.wechat_jiansheyinhang"
                mEntity.bankName = "建设银行储蓄卡"
                mEntity.bankShortName = "建设银行"
                mEntity.bankLimitMoney = 50000
                mEntity.bankReachTime = "2小时内到账"
            }
            "招商银行" ->{
                mEntity.bankIcon = "R.drawable.wechat_zhaoshangyinhang"
                mEntity.bankName = "招商银行储蓄卡"
                mEntity.bankShortName = "招商银行"
                mEntity.bankLimitMoney = 20000
                mEntity.bankReachTime = "2小时内到账"
            }
            "民生银行" ->{
                mEntity.bankIcon = "R.drawable.wechat_minshengyinhang"
                mEntity.bankName = "民生银行储蓄卡"
                mEntity.bankShortName = "民生银行"
                mEntity.bankLimitMoney = 50000
                mEntity.bankReachTime = "2小时内到账"
            }
            "交通银行" ->{
                mEntity.bankIcon = "R.drawable.wechat_jiaotongyinhang"
                mEntity.bankName = "交通银行储蓄卡"
                mEntity.bankShortName = "交通银行"
                mEntity.bankLimitMoney = 10000
                mEntity.bankReachTime = "2小时内到账"
            }
            "华夏银行" ->{
                mEntity.bankIcon = "R.drawable.wechat_huaxiayinhang"
                mEntity.bankName = "华夏银行储蓄卡"
                mEntity.bankShortName = "华夏银行"
                mEntity.bankLimitMoney = 100000
                mEntity.bankReachTime = "2小时内到账"
            }
            "工商银行" ->{
                mEntity.bankIcon = "R.drawable.wechat_gongshangyinhang"
                mEntity.bankName = "工商银行储蓄卡"
                mEntity.bankShortName = "工商银行"
                mEntity.bankLimitMoney = 50000
            }
            "邮政银行" ->{
                mEntity.bankIcon = "R.drawable.wechat_youzhengyinhang"
                mEntity.bankName = "邮政银行储蓄卡"
                mEntity.bankShortName = "邮政银行"
                mEntity.bankLimitMoney = 20000
                mEntity.bankReachTime = "2小时内到账"
            }
        }
        mChoiceBankTv.text = mEntity.bankName
    }

    override fun setLayoutResourceId() = R.layout.activity_wechat_choice_bank

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("添加银行卡", rightTitle = "添加")
    }

    override fun initViewsListener() {
        mChoiceBankLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mChoiceBankLayout ->{
                var dialog = ChoiceBankDialog()
                dialog.setOnItemSelectListener(this)
                dialog.show(supportFragmentManager, "Dialog")
            }
            R.id.overAllRightTitleTv ->{
                var tail =OtherUtil.getContent(mBankTailEt)
                if (TextUtils.isEmpty(tail)){
                    showToast("请输入银行卡后四位")
                    return
                }
                if (tail.length < 4){
                    showToast("长度不足四位")
                    return
                }
                mEntity.bankTailNumber = tail
                var list = UserOperateUtil.getWechatSimulatorBank()
                list.add(mEntity)
                SharedPreferencesUtils.putListData(SharedPreferenceKey.WECHAT_SIMULATOR_BANK_LIST, list)
                finish()
            }
        }
    }
}
