package app.jietuqi.cn.alipay.create

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.alipay.entity.AlipayCreateMyEntity
import app.jietuqi.cn.alipay.entity.AlipayVipLevelEntity
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.database.table.WechatUserTable
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.wechat.ui.activity.WechatRoleActivity
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
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setColor(this, ColorFinal.wechatTitleBar, 0)
    }*/
    override fun select(entity: AlipayVipLevelEntity) {
        mEntity.levelEntity = entity
    }
    internal val mEntity: AlipayCreateMyEntity = AlipayCreateMyEntity()
    override fun setLayoutResourceId() = R.layout.activity_alipay_create_my

    override fun needLoadingView() = false

    override fun initAllViews() {
        setCreateTitle("支付宝我的")
        mEntity.levelEntity = AlipayVipLevelEntity("大众会员", R.drawable.alipay_vip_level1)
        onlyThreeEditTextNeedTextWatcher(mAlipayCreateMyAccountTv, mAlipayCreateMyAntTv, mAlipayCreateMyBalanceTv)
    }

    override fun initViewsListener() {
        mAlipayCreateMyYuLiBaoOnOrOffIv.setOnClickListener(this)
        mAlipayCreateMyThousandSecurityOnOrOffIv.setOnClickListener(this)
        mAlipayCreateMyRedPointOnOrOffIv.setOnClickListener(this)
        mAlipayCreateMyMerchantOnOrOffIv.setOnClickListener(this)

        mAlipayCreateMyChangeRoleLayout.setOnClickListener(this)
        mAlipayCreateMyOtherLevelLayout.setOnClickListener(this)
        previewBtn.setOnClickListener(this)
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
                LaunchUtil.launch(this, WechatRoleActivity::class.java, RequestCode.CHANGE_ROLE)
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
                OtherUtil.onOrOff(mEntity.showThousandSecurity, mAlipayCreateMyRedPointOnOrOffIv)
            }
            R.id.mAlipayCreateMyMerchantOnOrOffIv ->{
                mEntity.showMerchant = !mEntity.showMerchant
                OtherUtil.onOrOff(mEntity.showMerchant, mAlipayCreateMyMerchantOnOrOffIv)
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
                    mEntity.wechatUserNickName = mEntity.wechatUserNickName
                    mAlipayCreateMyNickNameTv.text = mEntity.wechatUserNickName
                    GlideUtil.display(this, mEntity.avatar, mAlipayCreateMyAvatarIv)
                }
            }
        }
    }
}
