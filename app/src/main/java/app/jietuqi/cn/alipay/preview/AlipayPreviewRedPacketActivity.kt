package app.jietuqi.cn.alipay.preview

import android.content.Intent
import android.os.Bundle
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.alipay.entity.AlipayCreateRedPacketEntity
import app.jietuqi.cn.base.alipay.BaseAlipayActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
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

    override fun initAllViews() {}

    override fun initViewsListener() {
        mAlibabaRedPacketBackBtn.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        var entity: AlipayCreateRedPacketEntity = intent.getSerializableExtra(IntentKey.ENTITY) as AlipayCreateRedPacketEntity
        mAlibabaRedPacketNickNameTv.text = entity.wechatUserNickName
        mAlibabaRedPacketMsgTv.text = entity.msg
        mAlibabaRedPacketMoneyTv.text = StringUtils.keep2Point(entity.money)
        mAlipayRedPacketNumTv.text = StringUtils.insertFront(entity.num, "红包编号：")
        GlideUtil.displayHead(this, entity.getAvatarFile(), mAlibabaRedPacketAvatarIv)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAlibabaRedPacketBackBtn ->{
                finish()
            }
        }
    }
}
