package app.jietuqi.cn.alipay.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.alipay.entity.AlipayCreateRedPacketEntity
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
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
        val userEntity = RoleLibraryHelper(this).queryRandom1Item()
        mEntity.avatarFile = userEntity.avatarFile
        mEntity.wechatUserAvatar = userEntity.wechatUserAvatar
        mEntity.avatarInt = userEntity.avatarInt
        mEntity.resourceName = userEntity.resourceName
        mEntity.wechatUserNickName = userEntity.wechatUserNickName

        GlideUtil.displayHead(this, mEntity.getAvatarFile(), mAlipayCreateRedPacketAvatarIv)
        mAlipayCreateRedPacketNickNameTv.text = mEntity.wechatUserNickName
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
                var msg = mAlipayCreateRedPacketLeaveMsgEt.text.toString()
                if (TextUtils.isEmpty(msg)){
                    msg = "恭喜发财，万事如意！"
                }
                mEntity.money = mAlipayCreateRedPacketInputMoneyEt.text.toString()
                mEntity.msg = msg
                LaunchUtil.startAlipayPreviewRedPacketActivity(this, mEntity)
            }
            R.id.mAlipayCreateRedPacketChangeRoleLayout ->{
                operateRole(mEntity, 1, 1)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.OTHER_SIDE ->{
                if (data?.extras?.containsKey(IntentKey.ENTITY) == true){
                    mEntity.avatarFile = mOtherSideEntity.avatarFile
                    mEntity.wechatUserAvatar = mOtherSideEntity.wechatUserAvatar
                    mEntity.avatarInt = mOtherSideEntity.avatarInt
                    mEntity.resourceName = mOtherSideEntity.resourceName
                    mEntity.wechatUserNickName = mOtherSideEntity.wechatUserNickName
                    GlideUtil.displayHead(this, mEntity.getAvatarFile(), mAlipayCreateRedPacketAvatarIv)
                    mAlipayCreateRedPacketNickNameTv.text = mEntity.wechatUserNickName
                }
            }

        }
    }
}
