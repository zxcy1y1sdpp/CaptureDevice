package dasheng.com.capturedevice.wechat.ui.activity

import android.view.View
import android.widget.Toast
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseWechatActivity
import dasheng.com.capturedevice.wechat.db.WechatChargeHelper
import dasheng.com.capturedevice.wechat.entity.WechatChargeDetailEntity
import dasheng.com.capturedevice.wechat.ui.adapter.WechatPreviewChargeDetailAdapter
import kotlinx.android.synthetic.main.activity_wechat_preview_charge_detail.*
import java.util.ArrayList

/**
 * 作者： liuyuanbo on 2018/10/31 11:55.
 * 时间： 2018/10/31 11:55
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 预览 -- 零钱明细
 */

class WechatPreviewChargeDetailActivity : BaseWechatActivity() {
    val mHelper: WechatChargeHelper = WechatChargeHelper(this)
    private var mAdapter: WechatPreviewChargeDetailAdapter? = null
    private var mList: ArrayList<WechatChargeDetailEntity> = arrayListOf()
    override fun setLayoutResourceId() = R.layout.activity_wechat_preview_charge_detail

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        wechatTitleTv.text = "零钱明细"
        mAdapter = WechatPreviewChargeDetailAdapter(mList)
        val list = mHelper.query()
        if (null != list && list.isNotEmpty()){
            if (mList.size != 0){
                mList.clear()
            }
            mList.addAll(list)
        }
        mChargeRecyclerView.adapter = mAdapter
        val footerView = layoutInflater.inflate(R.layout.item_wechat_preview_charge_detail_footerview, mChargeRecyclerView, false)
        mChargeRecyclerView.addFooterView(footerView)
    }

    override fun initViewsListener() {

    }
}
