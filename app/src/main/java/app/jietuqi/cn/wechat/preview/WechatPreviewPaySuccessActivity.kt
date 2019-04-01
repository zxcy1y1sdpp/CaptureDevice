package app.jietuqi.cn.wechat.preview

import android.content.Intent
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.StringUtils
import kotlinx.android.synthetic.main.activity_wechat_preview_pay_success.*

/**
 * 作者： liuyuanbo on 2019/3/22 15:57.
 * 时间： 2019/3/22 15:57
 * 邮箱： 972383753@qq.com
 * 用途： 转账成功预览
 */
class WechatPreviewPaySuccessActivity : BaseWechatActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_preview_pay_success

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {

    }

    override fun initViewsListener() {
        mFinishTv.setOnClickListener { finish() }
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val entity: WechatUserEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity
        mMoneyTv.text = StringUtils.insertFront(StringUtils.keep2Point(entity.money), "¥")
        mNickNameTv.text = StringUtils.insertFrontAndBack(entity.eventBusTag, "待", "确认收钱")
    }
}
