package app.jietuqi.cn.ui.activity

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.adapter.OverallWeMediaDetailsAdapter
import app.jietuqi.cn.ui.entity.OverallWeMediaClassifyEntity
import app.jietuqi.cn.util.LaunchUtil
import kotlinx.android.synthetic.main.activity_overall_we_media_details.*

/**
 * 作者： liuyuanbo on 2019/2/22 11:55.
 * 时间： 2019/2/22 11:55
 * 邮箱： 972383753@qq.com
 * 用途： 自媒体详情
 */
class OverallWeMediaDetailsActivity : BaseOverallInternetActivity() {
    private lateinit var mAdapter: OverallWeMediaDetailsAdapter
    override fun setLayoutResourceId() = R.layout.activity_overall_we_media_details

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("商品详情", rightTitle = "联系客服", rightTvColor = R.color.transactionRecord)
    }

    override fun initViewsListener() {}

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val entity = intent.getSerializableExtra(IntentKey.ENTITY) as OverallWeMediaClassifyEntity
        val type = intent.getIntExtra(IntentKey.TYPE, 0)
        mAdapter = OverallWeMediaDetailsAdapter()
        mAdapter.setData(entity, type)
        mWeMediaDetailsRv.adapter = mAdapter
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.overAllRightTitleTv ->{
                LaunchUtil.launch(this, OverallServerHelpActivity::class.java)
            }
        }
    }
}
