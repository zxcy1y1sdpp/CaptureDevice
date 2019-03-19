package app.jietuqi.cn.ui.activity

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.AgencyInfoEntity
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.request.PostRequest
import kotlinx.android.synthetic.main.activity_overall_agency.*

/**
 * 作者： liuyuanbo on 2019/3/13 16:28.
 * 时间： 2019/3/13 16:28
 * 邮箱： 972383753@qq.com
 * 用途： 代理中心
 */
class OverallAgencyActivity : BaseOverallInternetActivity() {

    private lateinit var mAgencyEntity: AgencyInfoEntity
    override fun setLayoutResourceId() = R.layout.activity_overall_agency

    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("代理中心")
//        window.decorView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
//            override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
//                initStatusBar(R.drawable.agency_status_bar)
//                window.decorView.removeOnLayoutChangeListener(this)
//            }
//        })
    }
    override fun initViewsListener() {
        mWithdrawNowTv.setOnClickListener(this)
        mClientIv.setOnClickListener(this)
        mEarningIv.setOnClickListener(this)
        mWithdrawIv.setOnClickListener(this)
        OperateIv.setOnClickListener(this)
        mEditInfoTv.setOnClickListener(this)
        mServerIv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWithdrawNowTv ->{
                LaunchUtil.launch(this, OverallWithdrawDepositActivity::class.java)
            }
            R.id.mClientIv ->{
                LaunchUtil.launch(this, OverallMyClientActivity::class.java)
            }
            R.id.mEarningIv ->{
                LaunchUtil.startOverallAgencyIncomeAndExpensesActivity(this, "1")
            }
            R.id.mWithdrawIv ->{
                LaunchUtil.startOverallAgencyIncomeAndExpensesActivity(this, "-1")
            }
            R.id.OperateIv ->{
                LaunchUtil.startOverallWebViewActivity(this, "http://www.jietuqi.cn/index/index/news/id/8", "操作说明")
            }
            R.id.mEditInfoTv ->{
                LaunchUtil.startOverallEditAgencyInfoActivity(this, mAgencyEntity, 1)
            }
            R.id.mServerIv ->{
                LaunchUtil.launch(this, OverallServerHelpActivity::class.java)
            }
        }
    }
    override fun loadFromServer() {
        super.loadFromServer()
        getAgencyInfo()
    }

    private fun getAgencyInfo() {
        var request: PostRequest = EasyHttp.post(HttpConfig.AGENT, false).params("way", "byusers").params("users_id", UserOperateUtil.getUserId())
        request.execute(object : CallBackProxy<OverallApiEntity<AgencyInfoEntity>, AgencyInfoEntity>(object : SimpleCallBack<AgencyInfoEntity>() {
            override fun onError(e: ApiException) {
                if (e.code == 402){
                    SharedPreferencesUtils.saveBean2Sp(null, SharedPreferenceKey.AGENCY_INFO)
                }
                showToast(e.message)
            }
            override fun onSuccess(t: AgencyInfoEntity) {
                SharedPreferencesUtils.saveBean2Sp(t, SharedPreferenceKey.AGENCY_INFO)
                mAgencyEntity = t
                mCashTv.text = StringUtils.insertFront(mAgencyEntity.cash, "¥")
                mAlreadyWithdrawTv.text = StringUtils.insertFront(mAgencyEntity.amount, " 元")
                mAlipayNickNameTv.text = StringUtils.insertFront(mAgencyEntity.alipay_account, "欢迎您：")
                mEndTimeTv.text = mAgencyEntity.agent_time
            }
        }) {})
    }
}
