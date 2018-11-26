package app.jietuqi.cn.alipay.create

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.alipay.entity.AlipayCreateRedPacketEntity
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.database.table.WechatUserTable
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.wechat.ui.activity.WechatRoleActivity
import kotlinx.android.synthetic.main.activity_alipay_create_red_packet.*

/**
 * 作者： liuyuanbo on 2018/10/31 17:05.
 * 时间： 2018/10/31 17:05
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝 -- 生成 -- 红包页面
 */

class AlipayCreateRedPacketActivity : BaseCreateActivity() {

    internal var mEntity: AlipayCreateRedPacketEntity = AlipayCreateRedPacketEntity()
    override fun setLayoutResourceId() = R.layout.activity_alipay_create_red_packet

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        mEntity.num = RandomUtil.getRandomNum(28)
        setCreateTitle("支付宝红包", 0)
        onlyOneEditTextNeedTextWatcher(mAlipayCreateRedPacketInputMoneyEt)
    }

    override fun initViewsListener() {
        mAlipayCreateRedPacketChangeRoleLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.previewBtn ->{
                mEntity.money = mAlipayCreateRedPacketInputMoneyEt.text.toString()
                mEntity.msg = mAlipayCreateRedPacketLeaveMsgEt.text.toString()
                LaunchUtil.startAlipayPreviewRedPacketActivity(this, mEntity)
            }
            R.id.mAlipayCreateRedPacketChangeRoleLayout ->{
                LaunchUtil.launch(this, WechatRoleActivity::class.java, RequestCode.CHANGE_ROLE)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.CHANGE_ROLE ->{
                    val entity: WechatUserTable = data?.getSerializableExtra(IntentKey.ENTITY) as WechatUserTable
                    mEntity.avatar = entity.avatar
                    mEntity.wechatUserNickName = entity.wechatUserNickName
                    mAlipayCreateRedPacketNickNameTv.text = mEntity.wechatUserNickName
                    GlideUtil.display(this, mEntity.avatar, mAlipayCreateRedPacketAvatarIv)
                }
            }
        }
    }
}
