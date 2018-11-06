package dasheng.com.capturedevice.alipay.create

import android.content.Intent
import android.view.View
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.alipay.entity.AlipayCreateRedPacketEntity
import dasheng.com.capturedevice.base.BaseCreateActivity
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.constant.RandomUtil
import dasheng.com.capturedevice.constant.RandomUtil.mNumArray
import dasheng.com.capturedevice.constant.RequestCode
import dasheng.com.capturedevice.database.table.WechatUserTable
import dasheng.com.capturedevice.util.GlideUtil
import dasheng.com.capturedevice.util.LaunchUtil
import dasheng.com.capturedevice.util.StringUtils
import dasheng.com.capturedevice.util.TimeUtil
import dasheng.com.capturedevice.wechat.ui.activity.WechatRoleActivity
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
