package app.jietuqi.cn.ui.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseFragment
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.OverallCdkListAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallCdkEntity
import app.jietuqi.cn.util.OtherUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_cdk_list.*

/**
 * 作者： liuyuanbo on 2019/2/20 11:37.
 * 时间： 2019/2/20 11:37
 * 邮箱： 972383753@qq.com
 * 用途： 激活码列表
 */
class CdkListFragment : BaseFragment() {

    private var mList = arrayListOf<OverallCdkEntity>()
    private var mAdapter: OverallCdkListAdapter? = null
    private var mOrderId = ""
    override fun setLayoutResouceId() = R.layout.fragment_overall_cdk_list

    override fun initAllViews() {
        setRefreshLayout(mCdkListSrl)
        mAdapter = OverallCdkListAdapter(mList)
        mCdkListRv.adapter = mAdapter
    }

    override fun initViewsListener() {
        mCdkListRv.addOnItemTouchListener(object : OnRecyclerItemClickListener(mCdkListRv) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val entity = mList[vh.adapterPosition]
                OtherUtil.copy(activity, entity.cdk)
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
    }

    override fun getArguments(arguments: Bundle?) {
        super.getArguments(arguments)
        arguments?.getString(IntentKey.ID)?.let { mOrderId = it }
    }
    override fun loadFromServer() {
        super.loadFromServer()
        getOrders()
    }
    private fun getOrders(){
        EasyHttp.post(HttpConfig.ORDER)
                .params("way", "qingfen")
                .params("order_id", mOrderId)
                .params("limit", mLimitSize.toString())
                .params("page", mPageSize.toString())
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallCdkEntity>>, ArrayList<OverallCdkEntity>>(object : SimpleCallBack<ArrayList<OverallCdkEntity>>() {
                    override fun onSuccess(t: ArrayList<OverallCdkEntity>) {
                        if (mPageSize == 1){
                            if (mList.size != 0){
                                mList.clear()
                            }
                        }
                        mCdkListSrl.finishRefresh(true)
                        mCdkListSrl.finishLoadMore(true)
                        mList.addAll(t)
                        mAdapter?.notifyDataSetChanged()
                    }
                    override fun onError(e: ApiException) {
                        mCdkListSrl.finishRefresh(true)
                        if (mPageSize == 1){
                            mList.clear()
                            mAdapter?.notifyDataSetChanged()
                        }else{
                            mCdkListSrl.finishLoadMoreWithNoMoreData()
                        }
                    }
                }) {})
    }
}
