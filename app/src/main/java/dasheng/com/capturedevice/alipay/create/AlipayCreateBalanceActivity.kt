package dasheng.com.capturedevice.alipay.create

import android.view.View
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.alipay.entity.AlipayPreviewBalanceEntity
import dasheng.com.capturedevice.base.BaseCreateActivity
import dasheng.com.capturedevice.util.LaunchUtil
import dasheng.com.capturedevice.util.OtherUtil
import kotlinx.android.synthetic.main.activity_alipay_create_balance.*

/**
 * 作者： liuyuanbo on 2018/11/1 10:32.
 * 时间： 2018/11/1 10:32
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝 -- 生成 -- 余额页面
 */

class AlipayCreateBalanceActivity : BaseCreateActivity() {
    /**
     * 是否显示余额宝
     */
    internal var mShowYuEBao = false
   /**
     * 是否开通余额宝
     */
    internal var mDredgeYuEBao = false
    var mEntity: AlipayPreviewBalanceEntity = AlipayPreviewBalanceEntity()

    override fun setLayoutResourceId() = R.layout.activity_alipay_create_balance

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setCreateTitle("支付宝余额", 0)
        onlyOneEditTextNeedTextWatcher(mAlipayCreateBalanceMoneyEt)
    }

    override fun initViewsListener() {
        mAlipayCreateBalanceShowYuEBaoIv.setOnClickListener(this)
        mAlipayCreateBalanceDredgeYuEBaoIv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAlipayCreateBalanceShowYuEBaoIv ->{//是否显示余额宝
                mShowYuEBao = !mShowYuEBao
                OtherUtil.onOrOff(mShowYuEBao, mAlipayCreateBalanceShowYuEBaoIv)
                if (mShowYuEBao){
                    mAlipayCreateBalanceDredgeYuEBaoLayout.visibility = View.VISIBLE
                }else{
                    mDredgeYuEBao = false
                    OtherUtil.onOrOff(mDredgeYuEBao, mAlipayCreateBalanceDredgeYuEBaoIv)
                    mAlipayCreateBalanceDredgeYuEBaoLayout.visibility = View.GONE
                    mAlipayCreateBalanceShiftToMoneyEt.visibility = View.GONE
                }
                mEntity.showYuEBao = mShowYuEBao
            }
            R.id.mAlipayCreateBalanceDredgeYuEBaoIv ->{//是否开通余额宝
                mDredgeYuEBao = !mDredgeYuEBao
                OtherUtil.onOrOff(mDredgeYuEBao, mAlipayCreateBalanceDredgeYuEBaoIv)
                if (mDredgeYuEBao){
                    mAlipayCreateBalanceShiftToMoneyEt.visibility = View.VISIBLE
                }else{
                    mAlipayCreateBalanceShiftToMoneyEt.visibility = View.GONE
                }
                mEntity.dredgeYuEBao = mDredgeYuEBao
            }
            R.id.previewBtn ->{//预览
                mEntity.balanceMoney = mAlipayCreateBalanceMoneyEt.text.toString()
                mEntity.todayMoney = mAlipayCreateBalanceShiftToMoneyEt.text.toString()
                LaunchUtil.startAlipayPreviewBalanceActivity(this, mEntity)
            }
        }
    }
}
