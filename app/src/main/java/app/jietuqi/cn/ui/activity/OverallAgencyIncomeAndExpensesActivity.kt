package app.jietuqi.cn.ui.activity

import android.content.Intent
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.AgencyIncomeAndExpensesAdapter
import app.jietuqi.cn.ui.entity.AgencyIncomeAndExpensesEntity
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_agency_income_and_expenses.*

/**
 * 作者： liuyuanbo on 2019/1/29 16:21.
 * 时间： 2019/1/29 16:21
 * 邮箱： 972383753@qq.com
 * 用途： 代理收支明细
 */
class OverallAgencyIncomeAndExpensesActivity : BaseOverallInternetActivity() {
    /**
     * 1 -- 收入
     * -1 -- 支出
     */
    private var mType = "1"
    private var mList = arrayListOf<AgencyIncomeAndExpensesEntity.Data>()
    private lateinit var mAdapter: AgencyIncomeAndExpensesAdapter
    override fun setLayoutResourceId() = R.layout.activity_overall_agency_income_and_expenses

    override fun needLoadingView() = true

    override fun initAllViews() {
        setTopTitle("收支明细")
        setRefreshLayout(mRecordSrl)
        mAdapter = AgencyIncomeAndExpensesAdapter(mList)
        mRecordRv.adapter = mAdapter
    }

    override fun initViewsListener() {}

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getStringExtra(IntentKey.TYPE)
    }
    override fun loadFromServer() {
        super.loadFromServer()
        getData()
    }
    private fun getData(){
        EasyHttp.post(HttpConfig.AGENT, true).params("way", "logbyuser")
                .params("limit", mLimit)//添加了
                .params("page", mPage.toString())
                .params("users_id", UserOperateUtil.getUserId())
                .params("type", mType)
                .execute(object : CallBackProxy<OverallApiEntity<AgencyIncomeAndExpensesEntity>, AgencyIncomeAndExpensesEntity>(object : SimpleCallBack<AgencyIncomeAndExpensesEntity>() {
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }

                    override fun onSuccess(t: AgencyIncomeAndExpensesEntity) {
                        mRecordSrl.finishRefresh(true)
                        if (t.total >= 0){
                            mRecordSrl.finishLoadMore(true)
                            if (mPage == 1){
                                if (mList.size != 0){
                                    mList.clear()
                                }
                                mList.addAll(t.data)
                                mAdapter.notifyDataSetChanged()
                            }
                        }else{
                            mRecordSrl.finishLoadMoreWithNoMoreData()
                            showEmptyView()
                        }
                    }
                }) {})
    }
}
