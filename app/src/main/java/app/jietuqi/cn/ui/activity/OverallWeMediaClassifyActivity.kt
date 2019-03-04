package app.jietuqi.cn.ui.activity

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.OverallWeMediaClassifyAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallWeMediaClassifyEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.TimeUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_we_media_classify.*

/**
 * 作者： liuyuanbo on 2019/2/22 10:09.
 * 时间： 2019/2/22 10:09
 * 邮箱： 972383753@qq.com
 * 用途： 自媒体分类页面
 */
class OverallWeMediaClassifyActivity : BaseOverallInternetActivity() {
    /**
     * 0 -- 抖音
     * 1 -- 快手
     */
    private var mType = 0
    private var mList = arrayListOf<OverallWeMediaClassifyEntity>()
    private var mAdapter: OverallWeMediaClassifyAdapter? = null
    override fun setLayoutResourceId() = R.layout.activity_overall_we_media_classify

    override fun needLoadingView() = true

    override fun initAllViews() {
        setRefreshLayout(mWeMediaClassifySrl)
    }

    override fun initViewsListener() {
        mWeMediaClassifyRv.addOnItemTouchListener(object : OnRecyclerItemClickListener(mWeMediaClassifyRv) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val entity = mList[vh.adapterPosition]
                LaunchUtil.startOverallWeMediaDetailsActivity(this@OverallWeMediaClassifyActivity, mType, entity)
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })

    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        when(mType){
            0 -> setTopTitle("抖音出售")
            1 -> setTopTitle("快手出售")
        }
        mAdapter = OverallWeMediaClassifyAdapter(mList, mType)
        mWeMediaClassifyRv.adapter = mAdapter
    }

    override fun loadFromServer() {
        super.loadFromServer()
        getData()
    }

    private fun getData(){
        val postRequest = EasyHttp.post(HttpConfig.GOODS, true).params("limit", mLimit).params("page", mPage.toString())//添加了
        if (mType == 0){
            postRequest.params("way", "douyin")
        }else{
            postRequest.params("way", "kuaishou")
        }

        postRequest.execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallWeMediaClassifyEntity>>, ArrayList<OverallWeMediaClassifyEntity>>(object : SimpleCallBack<ArrayList<OverallWeMediaClassifyEntity>>() {
            override fun onStart() {
                super.onStart()
                Log.e("时间： -   onStart" , TimeUtil.getAllSpecTime(TimeUtil.getCurrentTimeEndMs()))
            }
            override fun onSuccess(t: ArrayList<OverallWeMediaClassifyEntity>) {
                Log.e("时间： -   onSuccess" , TimeUtil.getAllSpecTime(TimeUtil.getCurrentTimeEndMs()))
                if (mPage == 1){
                    if (mList.size != 0){
                        mList.clear()
                    }
                }
                mWeMediaClassifySrl.finishRefresh(true)
                mWeMediaClassifySrl.finishLoadMore(true)
                mList.addAll(t)
                Log.e("时间：-  onSuccess 开始刷新" , TimeUtil.getAllSpecTime(TimeUtil.getCurrentTimeEndMs()))
                mAdapter?.notifyDataSetChanged()
                Log.e("时间： -  onSuccess 结束刷新" , TimeUtil.getAllSpecTime(TimeUtil.getCurrentTimeEndMs()))
            }
            override fun onError(e: ApiException) {
                mWeMediaClassifySrl.finishRefresh(true)
                if (mPage == 1){
                    mList.clear()
                    mAdapter?.notifyDataSetChanged()
                    showEmptyView()
                }else{
                    mWeMediaClassifySrl.finishLoadMoreWithNoMoreData()
                }
            }
        }) {})
    }
}
