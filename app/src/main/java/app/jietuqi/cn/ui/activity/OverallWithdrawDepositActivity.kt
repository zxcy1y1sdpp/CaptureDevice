package app.jietuqi.cn.ui.activity

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.AgencyInfoEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_withdraw_deposit.*

/**
 * 作者： liuyuanbo on 2019/3/15 15:23.
 * 时间： 2019/3/15 15:23
 * 邮箱： 972383753@qq.com
 * 用途： 提现
 */
class OverallWithdrawDepositActivity : BaseOverallActivity() {
    private lateinit var mAgencyEntity: AgencyInfoEntity
    override fun setLayoutResourceId() = R.layout.activity_overall_withdraw_deposit

    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("提现")

    }

    override fun initViewsListener() {
        mChangeAccountTv.setOnClickListener(this)
        mAllWithdrawalTv.setOnClickListener(this)
        mWithdrawalTv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mChangeAccountTv ->{
                LaunchUtil.startOverallEditAgencyInfoActivity(this, mAgencyEntity, 1)
            }
            R.id.mAllWithdrawalTv ->{
                mMoneyEt.setText(mAgencyEntity.cash)
            }
            R.id.mWithdrawalTv ->{
                if ("余额不足" == mWithdrawalTv.text.toString()){
                    showQQTipDialog("余额不足", 1)
                    return
                }
                withdrawal()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setInfo()
    }
    private fun setInfo(){
        mAgencyEntity = UserOperateUtil.getAgencyInfo()
        mAlipayAccountEt.setText(mAgencyEntity.alipay_account + " (" + mAgencyEntity.alipay_name + ")")
        mLeftMoneyTv.text = StringUtils.insertFrontAndBack(mAgencyEntity.cash, "账户余额", "元")
        if (mAgencyEntity.cash.toFloat() <= 0){
            mWithdrawalTv.text = "余额不足"
            mWithdrawalTv.solid = ContextCompat.getColor(this, R.color.wechatLightGray)
        }else{
            mWithdrawalTv.text = "提现"
            mWithdrawalTv.solid = ContextCompat.getColor(this, R.color.overallBlue)
            mWithdrawalTv.pressBgColor = Color.parseColor("#4169E1")
        }
    }

    private fun withdrawal(){
        EasyHttp.post(HttpConfig.AGENT, false)
                .params("way", "withdraw")
                .params("users_id", UserOperateUtil.getUserId())
                .params("money", OtherUtil.getContent(mMoneyEt))
                .execute(object : SimpleCallBack<String>() {
                    override fun onStart() {
                        super.onStart()
                        showQQWaitDialog("提现中")
                    }
                    override fun onError(e: ApiException) {
                        e.message?.let { dismissQQDialog(it, needFinish = true) }

                    }
                    override fun onSuccess(t: String) {
                        dismissQQDialog("提现成功", needFinish = true)
                    }
                })
    }
}
