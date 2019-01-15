package app.jietuqi.cn.wechat.preview

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.wechat.db.WechatChargeHelper
import app.jietuqi.cn.wechat.entity.WechatChargeDetailEntity
import app.jietuqi.cn.wechat.ui.adapter.WechatPreviewChargeDetailAdapter
import com.jaeger.library.StatusBarUtil
import kotlinx.android.synthetic.main.activity_wechat_preview_charge_detail.*
import java.util.*

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
        setStatusBarColor(ColorFinal.chargeDetail)
        StatusBarUtil.setLightMode(this)
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
        wechatTitleLeftIv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.wechatTitleLeftIv ->{
                finish()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        needVip()
    }
}
