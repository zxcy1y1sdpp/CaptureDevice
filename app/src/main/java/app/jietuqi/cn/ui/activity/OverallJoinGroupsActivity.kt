package app.jietuqi.cn.ui.activity

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.OverallJoinGroupsAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallCardEntity
import app.jietuqi.cn.ui.entity.OverallIndustryEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_join_groups.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/11/11 19:55.
 * 时间： 2018/11/11 19:55
 * 邮箱： 972383753@qq.com
 * 用途： 加群互粉页面
 */
class OverallJoinGroupsActivity : BaseOverallInternetActivity(){
    private var mAdapter: OverallJoinGroupsAdapter? = null
    private var mList: ArrayList<OverallCardEntity> = arrayListOf()
    /**
     * 0 -- 互粉
     * 1 -- 加群
     */
    private var mType = 0
    private var mCardEntity: OverallCardEntity? = null

    override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
        super.onOptionsSelect(options1, options2, options3, v)
        when (mPickerType) {
            0 -> {
                val sex = v?.tag.toString()
                mOverallJoinGroupsPeopleCountTv.text = sex
                when(sex){
                    "性别不限" ->{
                        mOverallJoinGroupsPeopleCountTv.tag = ""
                    }
                    "男" ->{
                        mOverallJoinGroupsPeopleCountTv.tag = "1"
                    }
                    else ->{
                        mOverallJoinGroupsPeopleCountTv.tag = "2"
                    }
                }

            }
            1 -> {
                var industryEntity: OverallIndustryEntity = v?.tag as OverallIndustryEntity
                mOverallJoinGroupsBusinessTv.text = industryEntity.pickerViewText
                mOverallJoinGroupsBusinessTv.tag = industryEntity.id
            }
            2 -> {
                var area = v?.tag.toString()
                mOverallJoinGroupsAreasTv.text = area
                val arr=  area.split(" ")
                mOverallJoinGroupsAreasLayout.tag = arr[0]
                mOverallJoinGroupsAreasTv.tag = arr[1]
            }
            3 -> {
                var groupEntity: OverallIndustryEntity = v?.tag as OverallIndustryEntity
                mOverallJoinGroupsBusinessTv.text = groupEntity.pickerViewText
                mOverallJoinGroupsBusinessTv.tag = groupEntity.id
            }
            4 ->{
                val number = v?.tag.toString()
                mOverallJoinGroupsPeopleCountTv.text = v?.tag.toString()
                when(number){
                    "人数不限" ->{
                        mOverallJoinGroupsPeopleCountTv.tag = ""
                    }
                    "<100人" ->{
                        mOverallJoinGroupsPeopleCountTv.tag = "1"
                    }
                    ">100人" ->{
                        mOverallJoinGroupsPeopleCountTv.tag = "2"
                    }
                }
            }
        }
        mPage = 1
        getCardsData()
    }
    override fun setLayoutResourceId() = R.layout.activity_overall_join_groups

    override fun needLoadingView() = false

    override fun initAllViews() {
        setRefreshLayout(mOverallJoinGroupsRefreshLayout)
        registerEventBus()
    }

    override fun initViewsListener() {
        mOverallJoinGroupsAreasLayout.setOnClickListener(this)
        mOverallJoinGroupsBusinessLayout.setOnClickListener(this)
        mOverallJoinGroupsPeopleCountLayout.setOnClickListener(this)
        mOverallJoinGroupsPublishCardLayout.setOnClickListener(this)

        mOverallJoinGroupsRecyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(mOverallJoinGroupsRecyclerView) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val entity = mList[vh.adapterPosition]
                LaunchUtil.startOverallPersonalCardActivity(this@OverallJoinGroupsActivity, mType, entity)
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        if(mType == 0){
            setTopTitle("互粉")
            mOverallJoinGroupsPublishCardTv.text = "发布名片"
            mOverallJoinGroupsPeopleCountTv.text = "性别"
        }else{
            setTopTitle("加群")
            mOverallJoinGroupsPublishCardTv.text = "发布群名片"
        }
        mAdapter = OverallJoinGroupsAdapter()
        mAdapter?.setData(mList, mType)
        mOverallJoinGroupsRecyclerView.adapter = mAdapter
        getCard()
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallJoinGroupsAreasLayout ->{
                initAreaOptions()
            }
            R.id.mOverallJoinGroupsBusinessLayout ->{
                if (mType == 0){
                    initIndustryOrGroup()
                }else{
                    initIndustryOrGroup(3)
                }
            }
            R.id.mOverallJoinGroupsPeopleCountLayout ->{
                if (mType == 0){
                    initSingleOneOptionPicker(0)
                }else{
                    initSingleOneOptionPicker(4)
                }
            }
            R.id.mOverallJoinGroupsPublishCardLayout ->{
                val tag = mOverallJoinGroupsPublishCardTv.text.toString()
                when(tag){
                    "发布名片" ->{
                        LaunchUtil.startOverallPublishCardActivity(this, 0, mCardEntity)
                    }
                    "修改我的名片" ->{
                        LaunchUtil.startOverallPublishCardActivity(this, 1, mCardEntity)
                    }
                    "发布群名片" ->{
                        LaunchUtil.startOverallPublishCardActivity(this, 2, mCardEntity)
                    }
                    "修改群名片" ->{
                        LaunchUtil.startOverallPublishCardActivity(this, 3, mCardEntity)
                    }
                }
            }
        }
    }


    override fun loadFromServer() {
        super.loadFromServer()
        getCardsData()
    }
    /**
     * 获取我的名片
     */
    private fun getCard(){
        val postRequest = EasyHttp.post(HttpConfig.INFORMATION)
        postRequest.params("way", "id").params("uid", UserOperateUtil.getUserId())
        if (mType == 1){
            postRequest.params("classify", "group")
        }
        postRequest.execute(object : CallBackProxy<OverallApiEntity<OverallCardEntity>, OverallCardEntity>(object : SimpleCallBack<OverallCardEntity>() {
            override fun onSuccess(t: OverallCardEntity?) {
                mCardEntity = t
                if (mType == 0){
                    mOverallJoinGroupsPublishCardTv.text = "修改我的名片"
                }else{
                    mOverallJoinGroupsPublishCardTv.text = "修改群名片"
                }
            }
            override fun onError(e: ApiException) {
                if (mType == 0){
                    mOverallJoinGroupsPublishCardTv.text = "发布名片"
                }else{
                    mOverallJoinGroupsPublishCardTv.text = "发布群名片"
                }
            }
        }) {})
    }

    private fun getCardsData(){
        val postRequest = EasyHttp.post(HttpConfig.INFORMATION)
        postRequest.params("way", "lists")//way 必传add
                . params("limit", mLimit)
                . params("uid", UserOperateUtil.getUserId())
                . params("page", mPage.toString())
                .params("order", "update_time")
        if (mType == 0){//互粉
            if (mOverallJoinGroupsPeopleCountTv.tag.toString().isNotBlank()){
                postRequest.params("sex", mOverallJoinGroupsPeopleCountTv.tag.toString())
            }
        }else{//加群
            postRequest.params("classify", "group")
            if (mOverallJoinGroupsPeopleCountTv.tag.toString().isNotBlank()){
                postRequest.params("number", mOverallJoinGroupsPeopleCountTv.tag.toString())
            }
        }

        if (mOverallJoinGroupsBusinessTv.tag.toString().isNotBlank()){
            postRequest.params("industry_id", mOverallJoinGroupsBusinessTv.tag.toString())
        }
        if (mOverallJoinGroupsAreasLayout.tag.toString().isNotBlank() && mOverallJoinGroupsAreasTv.tag.toString().isNotBlank()){
            postRequest.params("province", mOverallJoinGroupsAreasLayout.tag.toString())
            postRequest.params("district", mOverallJoinGroupsAreasTv.tag.toString())
        }
        postRequest.execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallCardEntity>>, ArrayList<OverallCardEntity>>(object : SimpleCallBack<ArrayList<OverallCardEntity>>() {
            override fun onSuccess(t: ArrayList<OverallCardEntity>) {
                mOverallJoinGroupsRefreshLayout.finishRefresh()
                mOverallJoinGroupsRefreshLayout.finishLoadMore(true)
                if (mPage == 1){
                    if (mList.size != 0){
                        mList.clear()
                    }
                }
                t.let { mList.addAll(it) }
                mAdapter?.notifyDataSetChanged()

            }
            override fun onError(e: ApiException) {
                mOverallJoinGroupsRefreshLayout.finishRefresh(false)
                if (mPage == 1){
                    mList.clear()
                    mAdapter?.notifyDataSetChanged()
                }else{
                    mOverallJoinGroupsRefreshLayout.finishLoadMoreWithNoMoreData()
                }
            }
        }) {})
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNeedLoadMyCardData(operate: String) {
        mOverallJoinGroupsRefreshLayout.autoRefresh()
        getCard()
    }
    override fun onDestroy() {
        EventBusUtil.unRegister(this)
        super.onDestroy()
    }
}
