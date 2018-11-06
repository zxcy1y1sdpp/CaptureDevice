package dasheng.com.capturedevice.alipay.preview

import android.content.Intent
import android.os.Bundle
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.alipay.entity.AlipayCreateRedPacketEntity
import dasheng.com.capturedevice.base.alipay.BaseAlipayActivity
import dasheng.com.capturedevice.constant.ColorFinal
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.util.GlideUtil
import dasheng.com.capturedevice.util.StringUtils
import kotlinx.android.synthetic.main.activity_alipay_preview_red_packet.*

/**
 * 作者： liuyuanbo on 2018/10/31 14:00.
 * 时间： 2018/10/31 14:00
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝 -- 预览 -- 红包页面
 */

class AlipayPreviewRedPacketActivity : BaseAlipayActivity() {
    /**
     * 不用父类的statusbar颜色就需要重写oncreate（）并且设置颜色
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ColorFinal.alipayRedPacketRed)
    }
    override fun setLayoutResourceId() = R.layout.activity_alipay_preview_red_packet

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {

    }

    override fun initViewsListener() {

    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        var entity: AlipayCreateRedPacketEntity = intent.getSerializableExtra(IntentKey.ENTITY) as AlipayCreateRedPacketEntity
        mAlibabaRedPacketNickNameTv.text = entity.wechatUserNickName
        mAlibabaRedPacketMsgTv.text = entity.msg
        mAlibabaRedPacketMoneyTv.text = StringUtils.keep2Point(entity.money)
        mAlipayRedPacketNumTv.text = StringUtils.insertFront(entity.num, "红包编号：")
        GlideUtil.display(this, entity.avatar, mAlibabaRedPacketAvatarIv)
    }
}
