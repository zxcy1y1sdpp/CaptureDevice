package dasheng.com.capturedevice.alipay.preview

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import com.bm.zlzq.utils.ScreenUtil
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.alipay.entity.AlipayCreateMyEntity
import dasheng.com.capturedevice.base.alipay.BaseAlipayActivity
import dasheng.com.capturedevice.callback.EditDialogChoiceListener
import dasheng.com.capturedevice.constant.ColorFinal
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.entity.EditDialogEntity
import dasheng.com.capturedevice.util.GlideUtil
import dasheng.com.capturedevice.util.OtherUtil
import dasheng.com.capturedevice.util.StringUtils
import dasheng.com.capturedevice.widget.dialog.BadgeDialog
import kotlinx.android.synthetic.main.activity_alipay_preview_my.*
import q.rorbin.badgeview.Badge
import q.rorbin.badgeview.QBadgeView

/**
 * 作者： liuyuanbo on 2018/11/5 10:48.
 * 时间： 2018/11/5 10:48
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝 -- 预览 -- 我的页面
 */

class AlipayPreviewMyActivity : BaseAlipayActivity(), EditDialogChoiceListener, BadgeDialog.OnItemSelectListener {
    /**
     * 不用父类的statusbar颜色就需要重写oncreate（）并且设置颜色
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ColorFinal.alipayMy)
    }
    var mBadgeView0: Badge? = null
    var mBadgeView1: Badge? = null
    var mBadgeView2: Badge? = null
    var mBadgeView3: Badge? = null
    var mBadgeView4: Badge? = null
    var mBadgePadding: Float = 0f
    override fun select(position: Int, parentPosition: Int) {
        if (parentPosition == 0){//首页
            if (position == 0){//红点
                mBadgeView0?.badgeNumber = -1
                mBadgeView0?.setGravityOffset(mBadgePadding, 5f, true)
            }else if (position == 2){
                mBadgeView0?.badgeNumber = 0
            }
        }else if (parentPosition == 1){//财富
            if (position == 0){//红点
                mBadgeView1?.badgeNumber = -1
                mBadgeView1?.setGravityOffset(mBadgePadding, 5f, true)
            }else if (position == 2){
                mBadgeView1?.badgeNumber = 0
            }
        }else if (parentPosition == 2){//口碑
            if (position == 0){//红点
                mBadgeView2?.badgeNumber = -1
                mBadgeView2?.setGravityOffset(mBadgePadding, 5f, true)
            }else if (position == 2){
                mBadgeView2?.badgeNumber = 0
            }
        }else if (parentPosition == 3){//朋友
            if (position == 0){//红点
                mBadgeView3?.badgeNumber = -1
                mBadgeView3?.setGravityOffset(mBadgePadding, 5f, true)
            }else if (position == 2){
                mBadgeView3?.badgeNumber = 0
            }
        }else if (parentPosition == 4){//我的
            if (position == 0){//红点
                mBadgeView4?.badgeNumber = -1
                mBadgeView4?.setGravityOffset(mBadgePadding, 5f, true)
            }else if (position == 2){
                mBadgeView4?.badgeNumber = 0
            }
        }

    }

    override fun onChoice(entity: EditDialogEntity) {
        if (entity.position == 0){
            mBadgeView0?.setGravityOffset(13f, -1f, true)
            mBadgeView0?.badgeNumber = entity.content.toInt()
        }else if (entity.position == 1){
            mBadgeView1?.setGravityOffset(13f, -1f, true)
            mBadgeView1?.badgeNumber = entity.content.toInt()
        }else if (entity.position == 2){
            mBadgeView2?.setGravityOffset(13f, -1f, true)
            mBadgeView2?.badgeNumber = entity.content.toInt()
        }else if (entity.position == 3){
            mBadgeView3?.setGravityOffset(13f, -1f, true)
            mBadgeView3?.badgeNumber = entity.content.toInt()
        }else if (entity.position == 4){
            mBadgeView4?.setGravityOffset(13f, -1f, true)
            mBadgeView4?.badgeNumber = entity.content.toInt()
        }
    }

    override fun setLayoutResourceId() = R.layout.activity_alipay_preview_my

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        mBadgeView0 = QBadgeView(this).bindTarget(mAlipayMyBadgeView0).setBadgeTextSize(10f, true)
        mBadgeView1 = QBadgeView(this).bindTarget(mAlipayMyBadgeView1).setBadgeTextSize(10f, true)
        mBadgeView2 = QBadgeView(this).bindTarget(mAlipayMyBadgeView2).setBadgeTextSize(10f, true)
        mBadgeView3 = QBadgeView(this).bindTarget(mAlipayMyBadgeView3).setBadgeTextSize(10f, true)
        mBadgeView4 = QBadgeView(this).bindTarget(mAlipayMyBadgeView4).setBadgeTextSize(10f, true)
        setAlipayPreviewTitle("我的", R.color.white, R.color.wechatBlue, showFinish = false, showRight = true, rightContext ="设置", showBottomLine = false)

    }

    override fun initViewsListener() {
        mAlipayPreviewMyBadge0Tv.setOnClickListener(this)
        mAlipayPreviewMyBadge1Tv.setOnClickListener(this)
        mAlipayPreviewMyBadge2Tv.setOnClickListener(this)
        mAlipayPreviewMyBadge3Tv.setOnClickListener(this)
        mAlipayPreviewMyBadge4Tv.setOnClickListener(this)
        mBadgeView0?.setOnDragStateChangedListener({ _, _, _ -> })
        mBadgeView1?.setOnDragStateChangedListener({ _, _, _ -> })
        mBadgeView2?.setOnDragStateChangedListener({ _, _, _ -> })
        mBadgeView3?.setOnDragStateChangedListener({ _, _, _ -> })
        mBadgeView4?.setOnDragStateChangedListener({ _, _, _ -> })
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val entity: AlipayCreateMyEntity = intent.getSerializableExtra(IntentKey.ENTITY) as AlipayCreateMyEntity
        GlideUtil.display(this, entity.avatar, mAlipayPreviewMyAvatarIv)
        mAlipayPreviewMyNickNameTv.text = entity.wechatUserNickName
        mAlipayPreviewMyAntTv.text = StringUtils.insertBack(entity.ant, " 积分")
        if (entity.showMerchant){
            mAlipayPreviewMyMerchantLayout.visibility = View.VISIBLE
        }
        if (entity.showThousandSecurity){
            mAlipayPreviewMyThousandSecurityTv.text = "账户安全保障中"
            mAlipayPreviewMyThousandSecurityTv.setTextColor(ContextCompat.getColor(this, R.color.alipayMyThousandSecurity))
        }
        if (entity.showYuLiBao){
            mAlipayPreviewMyYulibaoLayout.visibility = View.VISIBLE
        }
        if (entity.showRedPoint){
            mAlipayPreviewMyRedPointIv.visibility = View.VISIBLE
        }
        mAlipayPreviewMyBalanceTv.text = StringUtils.insertBack(OtherUtil.formatPrice(entity.money), " 元")
        var account = ""
//        if (entity.account.contains("@")){//邮箱
//            account = entity.account.replace("(\\w{3})(\\w+)(@\\w+\\.[a-z]+(\\.[a-z]+)?)".toRegex(), "$1***$3")
//        }else{
        account = entity.account.replace("(\\d{3})\\d{6}(\\d{2})".toRegex(), "$1******$2")
//        }
        mAlipayPreviewMyAccountTv.text = account
        GlideUtil.display(this, entity.levelEntity.levelPic, mAlipayPreviewMyVipLevelIv)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        val dialog = BadgeDialog()
        when(v.id){
            R.id.mAlipayPreviewMyBadge0Tv ->{
                dialog.setOnItemSelectListener(this, this, 0)
            }
            R.id.mAlipayPreviewMyBadge1Tv ->{
                dialog.setOnItemSelectListener(this, this, 1)
            }
            R.id.mAlipayPreviewMyBadge2Tv ->{
                dialog.setOnItemSelectListener(this, this, 2)
            }
            R.id.mAlipayPreviewMyBadge3Tv ->{
                dialog.setOnItemSelectListener(this, this, 3)
            }
            R.id.mAlipayPreviewMyBadge4Tv ->{
                dialog.setOnItemSelectListener(this, this, 4)
            }
        }
        dialog.show(supportFragmentManager, "edit")
    }

    override fun onResume() {
        super.onResume()
        mBadgePadding = (ScreenUtil.getScreenWidth(this) * 0.05 / 4) .toFloat()
    }
}
