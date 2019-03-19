package app.jietuqi.cn.ui.activity

import android.content.Intent
import android.support.v7.widget.RecyclerView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.OverallCleanFansOrderAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallCleanFansListEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_clean_fans_order.*

/**
 * 作者： liuyuanbo on 2019/2/19 16:39.
 * 时间： 2019/2/19 16:39
 * 邮箱： 972383753@qq.com
 * 用途： 清粉激活码的购买记录
 */
class OverallCleanFansOrderActivity : BaseOverallInternetActivity() {
    private var mList = arrayListOf<OverallCleanFansListEntity>()
    private var mAdapter: OverallCleanFansOrderAdapter? = null
    /**
     * 0 -- 清粉
     * 1 -- 微爆粉
     */
    private var mType = 0

    override fun setLayoutResourceId() = R.layout.activity_overall_clean_fans_order

    override fun needLoadingView() = true

    override fun initAllViews() {
        setTopTitle("购买记录")
        setRefreshLayout(mCleanFansOrderSrl)
        mCleanFansOrderRv.addOnItemTouchListener(object : OnRecyclerItemClickListener(mCleanFansOrderRv) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val entity = mList[vh.adapterPosition]
                LaunchUtil.startOverallCdkListActivity(this@OverallCleanFansOrderActivity, entity.id.toString(), mType)
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
    }

    override fun initViewsListener() {}

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        mAdapter = OverallCleanFansOrderAdapter(mList, mType)
        mCleanFansOrderRv.adapter = mAdapter
    }
    override fun loadFromServer() {
        super.loadFromServer()
        getOrders()
    }
    private fun getOrders(){
        val postRequest = EasyHttp.post(HttpConfig.ORDER, true).params("way", "lists")
                .params("users_id", UserOperateUtil.getUserId())
                .params("limit", mLimit)//添加了
                .params("page", mPage.toString())
        if(mType == 0){
            postRequest.params("classify", "qingfen")
        }else{
            postRequest.params("classify", "qingfen2")
        }
        postRequest.execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallCleanFansListEntity>>, ArrayList<OverallCleanFansListEntity>>(object : SimpleCallBack<ArrayList<OverallCleanFansListEntity>>() {
            override fun onSuccess(t: ArrayList<OverallCleanFansListEntity>) {
                if (mPage == 1){
                    if (mList.size != 0){
                        mList.clear()
                    }
                }
                mCleanFansOrderSrl.finishRefresh(true)
                mCleanFansOrderSrl.finishLoadMore(true)
                mList.addAll(t)
                mAdapter?.notifyDataSetChanged()
            }
            override fun onError(e: ApiException) {
                mCleanFansOrderSrl.finishRefresh(true)
                if (mPage == 1){
                    mList.clear()
                    mAdapter?.notifyDataSetChanged()
                    showEmptyView()
                }else{
                    mCleanFansOrderSrl.finishLoadMoreWithNoMoreData()
                }
            }
        }) {})
    }
}
