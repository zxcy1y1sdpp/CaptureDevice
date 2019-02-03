package app.jietuqi.cn.ui.activity

import android.content.Intent
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.callback.LikeListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.MyPublishAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallDynamicEntity
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.ninegrid.ImageInfo
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_my_publish.*

/**
 * 作者： liuyuanbo on 2018/12/13 14:49.
 * 时间： 2018/12/13 14:49
 * 邮箱： 972383753@qq.com
 * 用途： 我的发布
 */
class OverallMyPublishActivity : BaseOverallInternetActivity(), LikeListener {

    private var mList: ArrayList<OverallDynamicEntity> = arrayListOf()
    private lateinit var mAdapter: MyPublishAdapter
    /**
     * 0 -- 自己发布的
     * 1 -- 其他用户的
     */
    private var mType = 0
    private var mUserId = ""
    private var mNickName = ""
    override fun setLayoutResourceId() = R.layout.activity_overall_my_publish

    override fun needLoadingView() = false

    override fun initAllViews() {
        mOverallMyPublishRefreshLayout.autoRefresh()
        setRefreshLayout(mOverallMyPublishRefreshLayout)
    }

    override fun initViewsListener() {
        mAdapter = MyPublishAdapter(mList, this)
        mOverallMyPublishRecyclerView.adapter = mAdapter
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mUserId = intent.getStringExtra(IntentKey.USER_ID)
        mNickName = intent.getStringExtra(IntentKey.NICKNAME)
        if (mUserId == UserOperateUtil.getUserId()){//自己发布的
            mType = 0
            setTopTitle("我的发布")
        }else{
            mType = 1
            setTopTitle(mNickName)
        }

    }
    override fun loadFromServer() {
        super.loadFromServer()
        getData()
    }

    private fun getData(){
        EasyHttp.post(HttpConfig.INFO)
                .params("way", "article")
                .params("mid", if (mType == 0) UserOperateUtil.getUserId() else "0")
                .params("uid", mUserId)
                .params("limit", mLimit)
                .params("page", mPage.toString())
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallDynamicEntity>>, ArrayList<OverallDynamicEntity>>(object : SimpleCallBack<ArrayList<OverallDynamicEntity>>() {
                    override fun onSuccess(t: ArrayList<OverallDynamicEntity>) {
                        if (mPage == 1){
                            if (mList.size != 0){
                                mList.clear()
                            }
                        }
                        mOverallMyPublishRefreshLayout.finishRefresh(true)
                        mOverallMyPublishRefreshLayout.finishLoadMore(true)
                        mList.addAll(t)
                        var info: ImageInfo
                        var entity: OverallDynamicEntity
                        var cover: OverallDynamicEntity.Cover
                        for (i in mList.indices) {
                            entity = mList[i]
                            if (entity.infoList.size != 0){
                                entity.infoList.clear()
                            }
                            entity.position = i
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
                        mAdapter.notifyDataSetChanged()
                    }
                    override fun onError(e: ApiException) {
                        mOverallMyPublishRefreshLayout.finishRefresh(true)
                        if (mPage == 1){
                            mList.clear()
                            mAdapter.notifyDataSetChanged()
                        }else{
                            mOverallMyPublishRefreshLayout.finishLoadMoreWithNoMoreData()
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
                        var holder = mOverallMyPublishRecyclerView.findViewHolderForAdapterPosition(entity.position) as MyPublishAdapter.Holder
                        holder.likeBtn.setChecked(false, true)
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
                        mAdapter?.notifyItemRangeChanged(position , 1)
                    }
                })
    }
    override fun like(overallDynamicEntity: OverallDynamicEntity?, comment: OverallDynamicEntity.Comment?, type: Int) {
        overallDynamicEntity?.let { likeAndUnLike(it) }
    }

}
