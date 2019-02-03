package app.jietuqi.cn.ui.activity

import android.support.v7.widget.RecyclerView
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.entity.BannerEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.OverallProjectMarketAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.ProjectMarketEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_project_market.*

/**
 * 作者： liuyuanbo on 2019/1/22 09:44.
 * 时间： 2019/1/22 09:44
 * 邮箱： 972383753@qq.com
 * 用途： 项目市场
 */
class OverallProjectMarketActivity : BaseOverallInternetActivity() {
    private lateinit var mAdapter: OverallProjectMarketAdapter
    private var mList = arrayListOf<ProjectMarketEntity>()
    private var mBannerList = arrayListOf<BannerEntity>()
    override fun setLayoutResourceId() = R.layout.activity_overall_project_market

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("项目市场")
        setRefreshLayout(mProjectMarketSrl)

    }

    override fun initViewsListener() {
        mProjectPublishLayout.setOnClickListener(this)
        mProjectMyLayout.setOnClickListener(this)
        mProjectMarketRv.addOnItemTouchListener(object : OnRecyclerItemClickListener(mProjectMarketRv) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                if (vh.adapterPosition == 0){
                    return
                }
                val entity = mList[vh.adapterPosition - 1]
                LaunchUtil.startOverallProjectShowActivity(this@OverallProjectMarketActivity, entity)
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mProjectPublishLayout ->{
                LaunchUtil.startOverallProjectPublishActivity(this, null, 0)
            }
            R.id.mProjectMyLayout ->{
                if (UserOperateUtil.isCurrentLoginDirectlyLogin(this)){
                    LaunchUtil.launch(this, OverallProjectMyActivity::class.java)
                }
            }
        }
    }

    override fun loadFromServer() {
        super.loadFromServer()
        getBannerData()
    }

    private fun getData(){
        EasyHttp.post(HttpConfig.STORE)
                .params("way", "lists")
                .params("mid", UserOperateUtil.getUserId())
                .params("limit", mLimit)
                .params("page", mPage.toString())
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<ProjectMarketEntity>>, ArrayList<ProjectMarketEntity>>(object : SimpleCallBack<ArrayList<ProjectMarketEntity>>() {
                    override fun onSuccess(t: ArrayList<ProjectMarketEntity>) {
                        if (mPage == 1){
                            if (mList.size != 0){
                                mList.clear()
                            }
                        }
                        mProjectMarketSrl.finishRefresh(true)
                        mProjectMarketSrl.finishLoadMore(true)
                        mList.addAll(t)
                        mAdapter.notifyDataSetChanged()
                    }
                    override fun onError(e: ApiException) {
                        mProjectMarketSrl.finishRefresh(true)
                        if (mPage == 1){
                            mList.clear()
                            mAdapter.notifyDataSetChanged()
                        }else{
                            mProjectMarketSrl.finishLoadMoreWithNoMoreData()
                        }
                    }
                }) {})
    }
    private fun getBannerData(){
        EasyHttp.post(HttpConfig.STORE)
                .params("way", "swipe")
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<BannerEntity>>, ArrayList<BannerEntity>>(object : SimpleCallBack<ArrayList<BannerEntity>>() {
                    override fun onSuccess(t: ArrayList<BannerEntity>?) {
                        if (mBannerList.size != 0){
                            mBannerList.clear()
                        }
                        t?.let { mBannerList.addAll(it) }
//                        mAdapter?.notifyItemChanged(0)
                        mAdapter = OverallProjectMarketAdapter(mList, mBannerList)
                        mProjectMarketRv.adapter = mAdapter
                        getData()
                    }

                    override fun onError(e: ApiException) {
                        showToast(e.message)
                    }

                }){})

    }
}
