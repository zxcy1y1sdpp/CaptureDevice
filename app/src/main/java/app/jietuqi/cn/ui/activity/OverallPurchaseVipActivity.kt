package app.jietuqi.cn.ui.activity

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.pay.alipay.AlipayUtil
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallRecentlyVipEntity
import app.jietuqi.cn.ui.entity.OverallVipCardEntity
import app.jietuqi.cn.ui.entity.OverallVipCardOrderEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.TimeUtil
import app.jietuqi.cn.widget.dialog.ChoicePayTypeDialog
import com.jaeger.library.StatusBarUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.request.PostRequest
import kotlinx.android.synthetic.main.activity_overall_purchase_vip.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 作者： liuyuanbo on 2018/11/6 14:49.
 * 时间： 2018/11/6 14:49
 * 邮箱： 972383753@qq.com
 * 用途： 开通vip页面
 */

class OverallPurchaseVipActivity : BaseOverallInternetActivity(), ChoicePayTypeDialog.OnPayTypeSelectListener {

    private var mVipCardList: ArrayList<OverallVipCardEntity> = arrayListOf()
    /**
     * 选中的Vip
     */
    private var mSelectCardEntity: OverallVipCardEntity = OverallVipCardEntity()
    private var mUserEntity: OverallUserInfoEntity? = null
    /**
     * 默认购买的是季度会员
     */
    private var mChoiceVipType = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mOverallPurchaseVipStatusLayout.setPadding(0, OtherUtil.getStatusBarHeight(this), 0, 0)
        StatusBarUtil.setTranslucentForImageView(this, 0, need)
    }
    override fun setLayoutResourceId() = R.layout.activity_overall_purchase_vip
    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        registerEventBus()
        refreshUserInfo()
        setFlickerAnimation()
        mScrollView.setImageView(mOverallPurchaseVipStatusLayout)
    }

    override fun initViewsListener() {
        mOverallPurchaseVipJDIv.setOnClickListener(this)
        mOverallPurchaseVipNDIv.setOnClickListener(this)
        mOverallPurchaseVipYJIv.setOnClickListener(this)
        mOverallPurchaseVipBackIv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        var dialog = ChoicePayTypeDialog()
        dialog.setListener(this)
        dialog.show(supportFragmentManager, "pay")
        when(v.id){
            R.id.mOverallPurchaseVipBackIv ->{
                finish()
            }
            R.id.mOverallPurchaseVipJDIv ->{
                mChoiceVipType = 2
                mOverallPurchaseVipJDIv.borderWidth = 8f
                mOverallPurchaseVipNDIv.borderWidth = 0f
                mOverallPurchaseVipYJIv.borderWidth = 0f
                mOverallPurchaseVipMarkIv.setImageResource(R.drawable.overall_purchase_vip_jdhy_mark)
                mSelectCardEntity = mVipCardList[0]
//                pay()
            }
            R.id.mOverallPurchaseVipNDIv ->{
                mChoiceVipType = 3
                mOverallPurchaseVipJDIv.borderWidth = 0f
                mOverallPurchaseVipNDIv.borderWidth = 8f
                mOverallPurchaseVipYJIv.borderWidth = 0f
                mOverallPurchaseVipMarkIv.setImageResource(R.drawable.overall_purchase_vip_ndhy_mark)
                mSelectCardEntity = mVipCardList[1]
            }
            R.id.mOverallPurchaseVipYJIv ->{
                mChoiceVipType = 4
                mOverallPurchaseVipJDIv.borderWidth = 0f
                mOverallPurchaseVipNDIv.borderWidth = 0f
                mOverallPurchaseVipYJIv.borderWidth = 8f
                mOverallPurchaseVipMarkIv.setImageResource(R.drawable.overall_purchase_vip_yjhy_mark)
                mSelectCardEntity = mVipCardList[2]
            }
        }
    }

    private fun setFlickerAnimation() {
        val animation = AlphaAnimation(1f, 0f)
        animation.duration = 750//闪烁时间间隔
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.repeatCount = Animation.INFINITE
        animation.repeatMode = Animation.REVERSE
        mOverallPurchaseVipMarkIv.animation = animation
    }
    private fun getMoney(){
        var request: PostRequest = EasyHttp.post(HttpConfig.INDEX)
                .params("way", "price")
                .params("classify", "vip")
        request.execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallVipCardEntity>>, ArrayList<OverallVipCardEntity>>(object : SimpleCallBack<ArrayList<OverallVipCardEntity>>() {
            override fun onSuccess(t: ArrayList<OverallVipCardEntity>) {
                mVipCardList = t
            }
            override fun onError(e: ApiException) {
                e.message?.let { showToast(it) }
            }
        }) {})
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(result: String) {
        if (result == "购买会员卡成功"){
            refreshUserInfo()
        }
    }
    /**
     * 创建订单的接口
     */
    private fun createOrder(payChannel: String = "微信"){
        var request: PostRequest = EasyHttp.post(HttpConfig.ORDER)
                .params("way", "add")
                .params("way", "add")
                .params("pay", "appalipay")
                .params("money", mSelectCardEntity.id)
                .params("uid", mUserEntity?.id?.toString())
                .params("pay_channel", payChannel)
                .params("classify", "vip")
                .params("os", "android")

        request.execute(object : CallBackProxy<OverallApiEntity<OverallVipCardOrderEntity>, OverallVipCardOrderEntity>(object : SimpleCallBack<OverallVipCardOrderEntity>() {
            override fun onSuccess(t: OverallVipCardOrderEntity?) {
                mSelectCardEntity.orderNum = t?.sn
                mSelectCardEntity.payChannel = t?.pay_channel
                t?.info?.let { AlipayUtil().init(this@OverallPurchaseVipActivity, it) }
            }
            override fun onStart() {
                super.onStart()
                showLoadingDialog("订单创建中，请稍后...")
            }
            override fun onError(e: ApiException) {
                dismissLoadingDialog()
                e.message?.let { showToast(it) }
            }
        }) {})
    }
    override fun loadFromServer(){
        getMoney()
        getRecentlyVip()
    }

    override fun refreshUser(user: OverallUserInfoEntity) {
        super.refreshUser(user)
        mUserEntity = user
        if (mUserEntity?.status == 2 || mUserEntity?.status == 3 || mUserEntity?.status == 4){
            GlideUtil.display(this, R.drawable.overall_purchase_renewal_vip, mOverallPurchaseVipTitleIv)
        }
        GlideUtil.display(this, mUserEntity?.headimgurl, mOverallPurchaseVipAvatarIv)
        mOverallPurchaseVipNickNameTv.text = mUserEntity?.nickname
//        mOverallPurchaseVipValidityTimeTv.text = StringUtils.insertFront(TimeUtil.stampToDate(user.vip_time),"会员有效期：")
        when {
            user.status == 1 -> mOverallPurchaseVipValidityTimeTv.text = "您还不是VIP会员"
            user.status == 2 -> mOverallPurchaseVipValidityTimeTv.text = StringUtils.insertFront(TimeUtil.stampToDate(user.vip_time),"会员有效期：")
            user.status == 3 -> mOverallPurchaseVipValidityTimeTv.text = StringUtils.insertFront(TimeUtil.stampToDate(user.vip_time),"会员有效期：")
            user.status == 4 -> mOverallPurchaseVipValidityTimeTv.text = "永久会员"
        }
    }

    private fun getRecentlyVip(){
        var request: PostRequest = EasyHttp.post(HttpConfig.INDEX)
                .params("way", "carousel")
        request.execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallRecentlyVipEntity>>, ArrayList<OverallRecentlyVipEntity>>(object : SimpleCallBack<ArrayList<OverallRecentlyVipEntity>>() {
            override fun onSuccess(t: ArrayList<OverallRecentlyVipEntity>) {
                val info = arrayListOf<String>()
                for (entity in t) {
                    info.add(entity.content)
                }
                mOverallPurchaseVipMarqueeTv.startWithList(info)
            }

            override fun onError(e: ApiException) {
                e.message?.let { showToast(it) }
            }
        }) {})
    }
    override fun payType(type: String) {
        if (type == "微信"){
            createOrder()
        }else{
            createOrder("支付宝")
        }
    }
}
