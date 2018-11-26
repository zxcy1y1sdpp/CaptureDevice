package app.jietuqi.cn.ui.fragment

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseFragment
import app.jietuqi.cn.callback.LikeListener
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.activity.OverallPublishFriendsCircleActivity
import app.jietuqi.cn.ui.adapter.HomeAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallDynamicEntity
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

class HomeFragment : BaseFragment(), LikeListener {
    override fun like(overallDynamicEntity: OverallDynamicEntity?, comment: OverallDynamicEntity.Comment?, type: Int) {
        overallDynamicEntity?.let { likeAndUnLike(it) }
    }

    private var mAdapter: HomeAdapter? = null
    override fun setLayoutResouceId() = R.layout.fragment_home
    private var mList: ArrayList<OverallDynamicEntity> = arrayListOf()

    override fun initAllViews() {
        EventBusUtil.register(this)
        activity?.getString(R.string.app_name)?.let { setTitle(it, 1)}
//        setTitle(activity?.getString(R.string.app_name), 1)
        mAdapter = HomeAdapter(mList, this)
        mRecyclerView.adapter = mAdapter
        setRefreshLayout(mOverallHomeRefreshLayout)
        (mRecyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
    }

    override fun initViewsListener() {
        mOverallPublisBtn.setOnClickListener(this)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var mScrollThreshold = 0
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val isSignificantDelta = Math.abs(dy) > mScrollThreshold
                if (isSignificantDelta) {
                    if (dy > 0) {
                        Log.e("direction", "上")
//                        mOverallPublisBtn.animation = AnimationUtil.moveToViewLocation()
//                        onScrollUp()
                    } else {
//                        mOverallPublisBtn.animation = AnimationUtil.moveToViewBottom()
                        Log.e("direction", "下=-----------")
//                        onScrollDown()
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE ->{// 当屏幕停止滚动

                    }
                    RecyclerView.SCROLL_STATE_DRAGGING ->{//当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片

                    }

                    RecyclerView.SCROLL_STATE_SETTLING ->{ //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                    }
                    else -> { }
                }
            }
        })
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallPublisBtn ->{
                LaunchUtil.launch(this.context, OverallPublishFriendsCircleActivity::class.java)
            }
        }
    }

    override fun loadFromServer() {
        super.loadFromServer()
        getData()
    }
    private fun getData(){
        EasyHttp.post(HttpConfig.INFO)
                .params("way", "article")
                .params("uid", UserOperateUtil.getUserId())
                .params("limit", mLimitSize.toString())
                .params("page", mPageSize.toString())
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallDynamicEntity>>, ArrayList<OverallDynamicEntity>>(object : SimpleCallBack<ArrayList<OverallDynamicEntity>>() {
                    override fun onSuccess(t: ArrayList<OverallDynamicEntity>) {
                        if (mPageSize == 1){
                            if (null != mList && mList.size != 0){
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
                        e.message?.let { showToast(it) }
                        mOverallHomeRefreshLayout.finishRefresh(true)
                        mOverallHomeRefreshLayout.finishLoadMore(true)
                    }

//                    override fun onEmpty() {
//                        super.onEmpty()
//                        mOverallHomeRefreshLayout.finishLoadMoreWithNoMoreData()
//                    }
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
                    override fun onError(e: ApiException) {}

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

    object AnimationUtil {
        private val TAG = AnimationUtil::class.java.simpleName

        /**
         * 从控件所在位置移动到控件的底部
         *
         * @return
         */
        fun moveToViewBottom(): TranslateAnimation {
            val mHiddenAction = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 1.0f)
            mHiddenAction.duration = 500
            return mHiddenAction
        }

        /**
         * 从控件的底部移动到控件所在位置
         *
         * @return
         */
        fun moveToViewLocation(): TranslateAnimation {
            val mHiddenAction = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    1.0f, Animation.RELATIVE_TO_SELF, 0.0f)
            mHiddenAction.duration = 500
            return mHiddenAction
        }
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
}
