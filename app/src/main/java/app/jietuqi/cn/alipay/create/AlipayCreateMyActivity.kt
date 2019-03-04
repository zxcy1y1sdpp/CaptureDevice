package app.jietuqi.cn.alipay.create

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.alipay.entity.AlipayCreateMyEntity
import app.jietuqi.cn.alipay.entity.AlipayVipLevelEntity
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.widget.dialog.ChoiceAlipayLevelDialog
import kotlinx.android.synthetic.main.activity_alipay_create_my.*
import kotlinx.android.synthetic.main.include_wechat_preview_btn.*

/**
 * 作者： liuyuanbo on 2018/11/4 18:36.
 * 时间： 2018/11/4 18:36
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝 -- 生成 -- 我的页面
 */

class AlipayCreateMyActivity : BaseCreateActivity(), ChoiceAlipayLevelDialog.OnItemSelectListener {
    override fun select(entity: AlipayVipLevelEntity) {
        mEntity.levelEntity = entity
    }
    internal val mEntity: AlipayCreateMyEntity = AlipayCreateMyEntity()
    override fun setLayoutResourceId() = R.layout.activity_alipay_create_my

    override fun needLoadingView() = false

    override fun initAllViews() {
        val userEntity = RoleLibraryHelper(this).queryRandomItem(1)[0]
        mEntity.avatarFile = userEntity.avatarFile
        mEntity.wechatUserAvatar = userEntity.wechatUserAvatar
        mEntity.avatarInt = userEntity.avatarInt
        mEntity.resourceName = userEntity.resourceName
        mEntity.wechatUserNickName = userEntity.wechatUserNickName
        GlideUtil.displayHead(this, mEntity.getAvatarFile(), mAlipayCreateMyAvatarIv)
        mAlipayCreateMyNickNameTv.text = mEntity.wechatUserNickName
        setBlackTitle("支付宝我的")
        mEntity.levelEntity = AlipayVipLevelEntity("大众会员", R.drawable.alipay_vip_level1)
        onlyThreeEditTextNeedTextWatcher(mAlipayCreateMyAccountTv, mAlipayCreateMyAntTv, mAlipayCreateMyBalanceTv)
        mAlipayCreateMyAccountTv.setText(RandomUtil.getRandomAccounts())
    }

    override fun initViewsListener() {
        mAlipayCreateMyYuLiBaoOnOrOffIv.setOnClickListener(this)
        mAlipayCreateMyThousandSecurityOnOrOffIv.setOnClickListener(this)
        mAlipayCreateMyRedPointOnOrOffIv.setOnClickListener(this)
        mAlipayCreateMyMerchantOnOrOffIv.setOnClickListener(this)

        mAlipayCreateMyChangeRoleLayout.setOnClickListener(this)
        mAlipayCreateMyOtherLevelLayout.setOnClickListener(this)
        previewBtn.setOnClickListener(this)
        mAlipayCreateMyRefreshIv.setOnClickListener(this)
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.previewBtn ->{
                mEntity.wechatUserNickName = mAlipayCreateMyNickNameTv.text.toString()
                mEntity.account = mAlipayCreateMyAccountTv.text.toString()
                mEntity.ant = mAlipayCreateMyAntTv.text.toString()
                mEntity.money = mAlipayCreateMyBalanceTv.text.toString()
                LaunchUtil.startAlipayPreviewMyActivity(this, mEntity)
            }
            R.id.mAlipayCreateMyChangeRoleLayout ->{
                operateRole(mEntity, type = 1)
            }
            R.id.mAlipayCreateMyRefreshIv ->{
                mAlipayCreateMyAccountTv.setText(RandomUtil.getRandomAccounts())
            }
            R.id.mAlipayCreateMyOtherLevelLayout ->{
                var dialog = ChoiceAlipayLevelDialog()
                dialog.setOnItemSelectListener(this)
                dialog.show(supportFragmentManager, "Dialog")
            }
            R.id.mAlipayCreateMyYuLiBaoOnOrOffIv ->{
                mEntity.showYuLiBao = !mEntity.showYuLiBao
                OtherUtil.onOrOff(mEntity.showYuLiBao, mAlipayCreateMyYuLiBaoOnOrOffIv)
            }
            R.id.mAlipayCreateMyThousandSecurityOnOrOffIv ->{
                mEntity.showThousandSecurity = !mEntity.showThousandSecurity
                OtherUtil.onOrOff(mEntity.showThousandSecurity, mAlipayCreateMyThousandSecurityOnOrOffIv)
            }
            R.id.mAlipayCreateMyRedPointOnOrOffIv ->{
                mEntity.showRedPoint = !mEntity.showRedPoint
                OtherUtil.onOrOff(mEntity.showRedPoint, mAlipayCreateMyRedPointOnOrOffIv)
            }
            R.id.mAlipayCreateMyMerchantOnOrOffIv ->{
                mEntity.showMerchant = !mEntity.showMerchant
                OtherUtil.onOrOff(mEntity.showMerchant, mAlipayCreateMyMerchantOnOrOffIv)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.MY_SIDE ->{
                if (data?.extras?.containsKey(IntentKey.ENTITY) == true){
                    mEntity.avatarFile = mMySideEntity.avatarFile
                    mEntity.wechatUserAvatar = mMySideEntity.wechatUserAvatar
                    mEntity.avatarInt = mMySideEntity.avatarInt
                    mEntity.resourceName = mMySideEntity.resourceName
                    mEntity.wechatUserNickName = mMySideEntity.wechatUserNickName
                    GlideUtil.displayHead(this, mEntity.getAvatarFile(), mAlipayCreateMyAvatarIv)
                    mAlipayCreateMyNickNameTv.text = mEntity.wechatUserNickName
                }
            }
        }
    }
}
