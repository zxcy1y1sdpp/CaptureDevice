package app.jietuqi.cn.ui.activity

import android.content.Intent
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.ui.adapter.MyClientAdapter
import kotlinx.android.synthetic.main.activity_overall_my_client_details.*

/**
 * 作者： liuyuanbo on 2019/3/15 14:03.
 * 时间： 2019/3/15 14:03
 * 邮箱： 972383753@qq.com
 * 用途： 我的客户详情页
 */
class OverallMyClientDetailsActivity : BaseOverallActivity() {
    private var mList = arrayListOf<OverallUserInfoEntity>()
    private lateinit var mAdapter: MyClientAdapter
    override fun setLayoutResourceId() = R.layout.activity_overall_my_client_details

    override fun needLoadingView() = false

    override fun initAllViews() {
        mAdapter = MyClientAdapter(mList)
    }

    override fun initViewsListener() {}

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val list: ArrayList<OverallUserInfoEntity> = intent.getSerializableExtra(IntentKey.LIST) as ArrayList<OverallUserInfoEntity>
        mList.addAll(list)
        val title = intent.getStringExtra(IntentKey.TITLE)
        setTopTitle(title)
        mAdapter.setTitle(title)
        mMyClientDetailsRv.adapter = mAdapter
    }
}
