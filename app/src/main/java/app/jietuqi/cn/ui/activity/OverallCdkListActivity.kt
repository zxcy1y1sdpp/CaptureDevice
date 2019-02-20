package app.jietuqi.cn.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.OverallCdkListAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallCdkEntity
import app.jietuqi.cn.ui.fragment.CdkListFragment
import app.jietuqi.cn.ui.fragment.HowToUseFragment
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_cdk_list.*


/**
 * 作者： liuyuanbo on 2019/2/20 10:17.
 * 时间： 2019/2/20 10:17
 * 邮箱： 972383753@qq.com
 * 用途： 查看激活码页面
 */
class OverallCdkListActivity : BaseOverallInternetActivity() {
    private lateinit var mCdkFragment: CdkListFragment
    private var mFragmentList = arrayListOf<Fragment>()
    val TYPE = "type"
    /**
     * 用于对Fragment进行管理
     */
    private val fragmentManager: FragmentManager? = null

    private var mList = arrayListOf<OverallCdkEntity>()
    private var mAdapter: OverallCdkListAdapter? = null
    private var mOrderId = ""
    override fun setLayoutResourceId() = R.layout.activity_overall_cdk_list

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("查看激活码")
        mCdkFragment = CdkListFragment()
        mFragmentList.add(mCdkFragment)
        mFragmentList.add(HowToUseFragment())
    }

    override fun initViewsListener() {
        mCdkListTv.setOnClickListener(this)
        mHowToUseTv.setOnClickListener(this)
        mCdkVp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            //页面滑动是执行
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            //页面滑动完成后执行
            override fun onPageSelected(position: Int) {
                when(position){
                    0 ->{
                        showListFragment()
                    }
                    1 ->{
                        showHowToUseFragment()
                    }
                }

            }
            //监听页面的状态，0--静止 1--滑动  2--滑动完成
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun showListFragment(){
        mCdkListTv.setBackgroundResource(R.drawable.cdk_list1)
        mHowToUseTv.setBackgroundResource(R.drawable.cdk_list2)
        mCdkListTv.setTextColor(ContextCompat.getColor(this, R.color.white))
        mHowToUseTv.setTextColor(ContextCompat.getColor(this, R.color.overallRed))
    }
    private fun showHowToUseFragment(){
        mCdkListTv.setBackgroundResource(R.drawable.cdk_list3)
        mHowToUseTv.setBackgroundResource(R.drawable.cdk_list4)
        mCdkListTv.setTextColor(ContextCompat.getColor(this, R.color.overallRed))
        mHowToUseTv.setTextColor(ContextCompat.getColor(this, R.color.white))
    }
    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mOrderId = intent.getStringExtra(IntentKey.ID)
        val bundle = Bundle()
        bundle.putSerializable(IntentKey.ID, mOrderId)
        mCdkFragment.arguments = bundle
        val adapter = CdkAdapter(supportFragmentManager)
        mCdkVp.adapter = adapter
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mCdkListTv ->{
                mCdkVp.currentItem = 0
            }
            R.id.mHowToUseTv ->{
                mCdkVp.currentItem = 1
            }
        }
    }
    override fun loadFromServer() {
        super.loadFromServer()
        getOrders()
    }
    private fun getOrders(){
        EasyHttp.post(HttpConfig.ORDER)
                .params("way", "qingfen")
                .params("order_id", mOrderId)
                .params("limit", mLimit)
                .params("page", mPage.toString())
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallCdkEntity>>, ArrayList<OverallCdkEntity>>(object : SimpleCallBack<ArrayList<OverallCdkEntity>>() {
                    override fun onSuccess(t: ArrayList<OverallCdkEntity>) {
                        if (mPage == 1){
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
                        if (mPage == 1){
                            mList.clear()
                            mAdapter?.notifyDataSetChanged()
                        }else{
                            mCdkListSrl.finishLoadMoreWithNoMoreData()
                        }
                    }
                }) {})
    }

    inner class CdkAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }
    }
}
