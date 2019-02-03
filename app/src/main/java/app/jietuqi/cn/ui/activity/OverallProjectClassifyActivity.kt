package app.jietuqi.cn.ui.activity

import android.content.Intent
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.OverallProjectClassifyAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.ProjectMarketEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_project_classify.*

/**
 * 作者： liuyuanbo on 2019/1/25 09:37.
 * 时间： 2019/1/25 09:37
 * 邮箱： 972383753@qq.com
 * 用途： 莫一种分类的项目列表
 */
class OverallProjectClassifyActivity : BaseOverallInternetActivity(), OverallProjectClassifyAdapter.OperateListener {
    override fun collect(entity: ProjectMarketEntity, position: Int) {
        unLike(entity, position)
    }

    override fun delete(entity: ProjectMarketEntity, position: Int) {
        deleteData(entity, position)
    }

    override fun modify(entity: ProjectMarketEntity, position: Int) {
        LaunchUtil.startOverallProjectPublishActivity(this, entity, 1)
    }

    override fun popup(entity: ProjectMarketEntity, position: Int) {
        LaunchUtil.startProjectPopularizeActivity(this, entity)
    }

    private lateinit var mAdapter: OverallProjectClassifyAdapter
    private var mList: ArrayList<ProjectMarketEntity> = arrayListOf()
    /**
     * 0 -- 查询分类下的项目列表
     * 1 -- 查询我发布的项目
     * 2 -- 查询我收藏的项目
     */
    private var mType = 0
    private var mIndustryId = ""
    private var mUsersId = ""
    override fun setLayoutResourceId() = R.layout.activity_overall_project_classify

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setRefreshLayout(mOverallProjectClassifySrl)
    }

    override fun initViewsListener() {}

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        setTopTitle(intent.getStringExtra(IntentKey.TITLE))

        when (mType) {
            0 -> mIndustryId = intent.getStringExtra(IntentKey.INDUSTRY_ID)
            1 -> mUsersId = intent.getStringExtra(IntentKey.USER_ID)
            2 -> mUsersId = intent.getStringExtra(IntentKey.USER_ID)
        }
        mAdapter = OverallProjectClassifyAdapter(mList, mType, this)
        mOverallProjectClassifyRv.adapter = mAdapter
    }

    override fun loadFromServer() {
        super.loadFromServer()
        getData()
    }

    private fun getData(){
        val request = EasyHttp.post(HttpConfig.STORE)

        request.params("limit", mLimit).params("mid", UserOperateUtil.getUserId()).params("page", mPage.toString())
        when (mType) {
            0 -> {//查询分类列表
                request.params("way", "lists")
                request.params("industry_id", mIndustryId)
            }
            1 -> {//我发布的项目
                request.params("way", "lists")
                request.params("users_id", mUsersId)
            }
            2 -> {//我收藏的项目
                request.params("way", "myfavour")
                request.params("users_id", mUsersId)
            }
        }


        request.execute(object : CallBackProxy<OverallApiEntity<ArrayList<ProjectMarketEntity>>, ArrayList<ProjectMarketEntity>>(object : SimpleCallBack<ArrayList<ProjectMarketEntity>>() {
            override fun onSuccess(t: ArrayList<ProjectMarketEntity>) {
                if (mPage == 1){
                    if (mList.size != 0){
                        mList.clear()
                    }
                }
                mOverallProjectClassifySrl.finishRefresh(true)
                mOverallProjectClassifySrl.finishLoadMore(true)
                mList.addAll(t)
                mAdapter.notifyDataSetChanged()
            }
            override fun onError(e: ApiException) {
                mOverallProjectClassifySrl.finishRefresh(true)
                if (mPage == 1){
                    mList.clear()
                    mAdapter?.notifyDataSetChanged()
                }else{
                    mOverallProjectClassifySrl.finishLoadMoreWithNoMoreData()
                }
            }
        }) {})
    }
    /**
     * 点赞/取消点赞
     */
    private fun unLike(entity: ProjectMarketEntity, position: Int){
        EasyHttp.post(HttpConfig.STORE)
                .params("way", "favour")
                .params("uid", UserOperateUtil.getUserId())
                .params("info_id", entity.id.toString())
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }

                    override fun onSuccess(t: String) {
                        mList.removeAt(position)
                        mAdapter?.notifyItemRemoved(position)
                    }
                })
    }
    /**
     * 点赞/取消点赞
     */
    private fun deleteData(entity: ProjectMarketEntity, position: Int){
        EasyHttp.post(HttpConfig.STORE)
                .params("way", "delete")
                .params("id", entity.id.toString())
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }

                    override fun onSuccess(t: String) {
                        mList.removeAt(position)
                        mAdapter?.notifyItemRemoved(position)
                    }
                })
    }
}
