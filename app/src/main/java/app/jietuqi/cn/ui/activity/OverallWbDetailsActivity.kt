package app.jietuqi.cn.ui.activity

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
 * 用途： 微币明细
 */
class OverallWbDetailsActivity : BaseOverallInternetActivity() {
    private var mList = arrayListOf<WbDetailsEntity>()
    private lateinit var mAdapter: WbDetailAdapter
    override fun setLayoutResourceId() = R.layout.activity_overall_wb_details

    override fun needLoadingView() = true

    override fun initAllViews() {
        setTopTitle("微币明细")
        setRefreshLayout(mWbRecordSrl)
        mAdapter = WbDetailAdapter(mList)
        mWbRecordRv.adapter = mAdapter
    }

    override fun initViewsListener() {}

    override fun loadFromServer() {
        super.loadFromServer()
        getData()
    }
    private fun getData(){
        val request = EasyHttp.post(HttpConfig.GOLD, true)
        request.params("way", "lists")
                .params("limit", mLimit)//添加了
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
                mAdapter.notifyDataSetChanged()
            }
            override fun onError(e: ApiException) {
                mWbRecordSrl.finishRefresh(true)
                if (mPage == 1){
                    mList.clear()
                    mAdapter?.notifyDataSetChanged()
                    showEmptyView()
                }else{
                    mWbRecordSrl.finishLoadMoreWithNoMoreData()
                }
            }
        }) {})
    }
}
