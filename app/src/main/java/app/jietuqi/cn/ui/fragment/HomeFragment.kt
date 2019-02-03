package app.jietuqi.cn.ui.fragment

import android.support.v7.widget.DefaultItemAnimator
import android.view.View
import android.widget.RelativeLayout
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseFragment
import app.jietuqi.cn.callback.FabScrollListener
import app.jietuqi.cn.callback.HideScrollListener
import app.jietuqi.cn.callback.LikeListener
import app.jietuqi.cn.entity.BannerEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.activity.OverallPublishFriendsCircleActivity
import app.jietuqi.cn.ui.adapter.HomeAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallDynamicEntity
import app.jietuqi.cn.ui.entity.ProductFlavorsEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.ninegrid.ImageInfo
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/10/23 17:27.
 * 时间： 2018/10/23 17:27
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class HomeFragment : BaseFragment(), LikeListener, HideScrollListener/*, HomeAdapter.StatusBarListener*/ {

    private var mAdapter: HomeAdapter? = null
    override fun setLayoutResouceId() = R.layout.fragment_home
    private var mList: ArrayList<OverallDynamicEntity> = arrayListOf()
    private var mBannerList: ArrayList<BannerEntity> = arrayListOf()

    override fun initAllViews() {
        EventBusUtil.register(this)
//        mAdapter = HomeAdapter(mList, mBannerList, this /*,this*/)
//        mRecyclerView.adapter = mAdapter
        setRefreshLayout(mOverallHomeRefreshLayout)
        (mRecyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        if (UserOperateUtil.needColseByChannel()) {
            if (UserOperateUtil.isWandoujiaChannel()) {
                mOverallPublishBtn.visibility = View.GONE
            }
        }
    }

    override fun initViewsListener() {
        mOverallHomeRefreshLayout.autoRefresh()
        mOverallPublishBtn.setOnClickListener(this)
        mRecyclerView.addOnScrollListener(FabScrollListener(this))
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallPublishBtn ->{
                if (UserOperateUtil.isCurrentLoginDirectlyLogin(activity)){
                    LaunchUtil.launch(this.context, OverallPublishFriendsCircleActivity::class.java)
                }
            }
        }
    }

    override fun loadFromServer() {
        super.loadFromServer()
        getData()
        getBannerData()
    }
    private fun getData(){
        EasyHttp.post(HttpConfig.INFO)
                .params("way", "article")
                .params("mid", UserOperateUtil.getUserId())
                .params("limit", mLimitSize.toString())
                .params("page", mPageSize.toString())
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallDynamicEntity>>, ArrayList<OverallDynamicEntity>>(object : SimpleCallBack<ArrayList<OverallDynamicEntity>>() {
                    override fun onSuccess(t: ArrayList<OverallDynamicEntity>) {
                        if (mPageSize == 1){
                            if (mList.size != 0){
                                mList.clear()
                            }
                        }

                        mOverallHomeRefreshLayout.finishRefresh(true)
                        mOverallHomeRefreshLayout.finishLoadMore(true)
                        mList.addAll(t)
                        var info: ImageInfo
                        var entity: OverallDynamicEntity
                        var cover: OverallDynamicEntity.Cover
                        for (i in mList.indices) {
                            entity = mList[i]
                            if (entity.infoList.size != 0){
                                entity.infoList.clear()
                            }
                            for (j in entity.cover.indices) {
                                cover = entity.cover[j]
                                info = ImageInfo()
                                info.bigImageUrl = cover.url
                                info.thumbnailUrl = cover.url
                                info.imageViewWidth = cover.width
                                info.imageViewHeight = cover.height
                                info.whPercentage = info.imageViewWidth.toFloat() / info.imageViewHeight.toFloat()
                                entity.infoList.add(info)
                            }
                        }
                        mAdapter?.notifyDataSetChanged()
                    }
                    override fun onError(e: ApiException) {
                        if (mPageSize == 1){
                            mList.clear()
                            mAdapter?.notifyDataSetChanged()
                        }else{
                            mOverallHomeRefreshLayout.finishLoadMoreWithNoMoreData()
                        }
                    }
                }) {})
    }

    /**
     * 点赞/取消点赞
     */
    private fun likeAndUnLike(entity: OverallDynamicEntity){
        EasyHttp.post(HttpConfig.INDEX)
                .params("way", "favour")
                .params("uid", UserOperateUtil.getUserId())
                .params("classify", "article")
                .params("info_id", entity.id.toString())
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }

                    override fun onSuccess(t: String) {
                        if (entity.is_favour == 0){
                            entity.is_favour = 1
                            entity.favour += 1
                        }else{
                            entity.is_favour = 0
                            entity.favour -= 1
                        }
                        var position = mList.indexOf(entity)
                        mAdapter?.notifyItemRangeChanged(position + 1, 1)
                    }
                })
    }
    private fun getBannerData(){
        EasyHttp.post(HttpConfig.INDEX)
                .params("way", "swipe")
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<BannerEntity>>, ArrayList<BannerEntity>>(object : SimpleCallBack<ArrayList<BannerEntity>>() {
                    override fun onSuccess(t: ArrayList<BannerEntity>?) {
                        if (mBannerList.size != 0){
                            mBannerList.clear()
                        }
                        t?.let { mBannerList.addAll(it) }
                        mAdapter = HomeAdapter(mList, mBannerList, this@HomeFragment /*,this*/)
                        mRecyclerView.adapter = mAdapter
//                        mAdapter?.notifyItemChanged(0)
                    }

                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }

                }){})

    }

    /**
     * 事件订阅者自定义的接收方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessageEvent(entity: ProductFlavorsEntity) {
        mAdapter?.notifyDataSetChanged()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDynamicChange(overallDynamicEntity: OverallDynamicEntity) {
        val position = overallDynamicEntity.position
        val entity = mList[position]
        entity.comment_number = overallDynamicEntity.comment_number
        entity.favour = overallDynamicEntity.favour
        entity.is_favour = overallDynamicEntity.is_favour
        if (null != entity.comment && entity.comment.size > 0){
            entity.comment.clear()
        }
        entity.comment.addAll(overallDynamicEntity.comment)
        mAdapter?.notifyItemChanged(position + 1)
    }
    override fun onDestroy() {
        super.onDestroy()
        EventBusUtil.unRegister(this)
    }
    override fun onHide() {
        val layoutParams = mOverallPublishBtn.layoutParams as RelativeLayout.LayoutParams
        mOverallPublishBtn.animate().translationX((mOverallPublishBtn.width / 2 + layoutParams.rightMargin).toFloat()).setDuration(300).alpha(0.5f)/*.interpolator = AccelerateInterpolator(3f)*/

    }

    override fun onShow() {
        mOverallPublishBtn.animate().translationX(0f).setDuration(300).alpha(1f)/*.interpolator = DecelerateInterpolator(3f)*/
    }
    override fun like(overallDynamicEntity: OverallDynamicEntity?, comment: OverallDynamicEntity.Comment?, type: Int) {
        overallDynamicEntity?.let { likeAndUnLike(it) }
    }
}
