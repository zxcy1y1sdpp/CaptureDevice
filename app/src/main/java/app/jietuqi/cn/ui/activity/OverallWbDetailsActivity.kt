package app.jietuqi.cn.ui.activity

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.entity.WbDetailsEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.WbDetailAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_wb_details.*

/**
 * 作者： liuyuanbo on 2019/1/29 16:21.
 * 时间： 2019/1/29 16:21
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class OverallWbDetailsActivity : BaseOverallInternetActivity() {
    private var mList = arrayListOf<WbDetailsEntity>()
    private lateinit var mAdapter: WbDetailAdapter
    /**
     * 0 -- 消费记录
     * 1 -- 充值记录
     */
    private var mType = "0"
    override fun setLayoutResourceId() = R.layout.activity_overall_wb_details

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("微币明细")
        setRefreshLayout(mWbRecordSrl)
        mAdapter = WbDetailAdapter(mList)
        mWbRecordRv.adapter = mAdapter
    }

    override fun initViewsListener() {
        /*mReduceLayout.setOnClickListener(this)
        mBuyLayout.setOnClickListener(this)*/
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
//            R.id.mReduceLayout ->{
//                mType = "0"
//                mPage = 1
//                showLoadingDialog("请稍后")
//                getData()
//                mWbExpenseRecordTv.setTextColor(Color.parseColor("#111111"))
//                mWbExpenseRecordLineView.visibility = View.VISIBLE
//                mWbRechargeRecordTv.setTextColor(ContextCompat.getColor(this, R.color.wechatLightGray))
//                mWbRechargeRecordLineView.visibility = View.GONE
//            }
//            R.id.mBuyLayout ->{
//                mType = "1"
//                mPage = 1
//                showLoadingDialog("请稍后")
//                getData()
//                mWbRechargeRecordTv.setTextColor(Color.parseColor("#111111"))
//                mWbRechargeRecordLineView.visibility = View.VISIBLE
//                mWbExpenseRecordTv.setTextColor(ContextCompat.getColor(this, R.color.wechatLightGray))
//                mWbExpenseRecordLineView.visibility = View.GONE
//            }
        }
    }
    override fun loadFromServer() {
        super.loadFromServer()
        getData()
    }
    private fun getData(){
        val request = EasyHttp.post(HttpConfig.GOLD)
        request.params("way", "lists")
                .params("limit", mLimit)
                .params("page", mPage.toString())
                .params("users_id", UserOperateUtil.getUserId())
//                .params("type", mType)
        request.execute(object : CallBackProxy<OverallApiEntity<ArrayList<WbDetailsEntity>>, ArrayList<WbDetailsEntity>>(object : SimpleCallBack<ArrayList<WbDetailsEntity>>() {
            override fun onSuccess(t: ArrayList<WbDetailsEntity>) {
                if (mPage == 1){
                    if (mList.size != 0){
                        mList.clear()
                    }
                }
                mWbRecordSrl.finishRefresh(true)
                mWbRecordSrl.finishLoadMore(true)
                mList.addAll(t)
//                setPullAction()
                mAdapter.notifyDataSetChanged()
            }
            override fun onError(e: ApiException) {
                mWbRecordSrl.finishRefresh(true)
                if (mPage == 1){
                    mList.clear()
                    mAdapter?.notifyDataSetChanged()
                }else{
                    mWbRecordSrl.finishLoadMoreWithNoMoreData()
                }
            }
        }) {})
    }
}
