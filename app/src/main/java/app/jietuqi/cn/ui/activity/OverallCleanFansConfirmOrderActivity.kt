package app.jietuqi.cn.ui.activity

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.pay.alipay.AlipayUtil
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallVipCardEntity
import app.jietuqi.cn.ui.entity.OverallVipCardOrderEntity
import app.jietuqi.cn.util.BigDecimalUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.request.PostRequest
import kotlinx.android.synthetic.main.activity_overall_clean_fans_confirm_order.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2019/2/19 13:40.
 * 时间： 2019/2/19 13:40
 * 邮箱： 972383753@qq.com
 * 用途： 清理死粉 - 下单确认页面
 */
class OverallCleanFansConfirmOrderActivity : BaseOverallInternetActivity() {
    private var mCount = 1
    private var mCurrentUseEntity = OverallVipCardEntity()
    override fun setLayoutResourceId() = R.layout.activity_overall_clean_fans_confirm_order

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("下单确认")
    }

    override fun initViewsListener() {
        mReduceTv.setOnClickListener(this)
        mPlusTv.setOnClickListener(this)
        mBuyTv.setOnClickListener(this)
        mCountEt.addTextChangedListener(mCountWatcher)
    }
    private val mCountWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun afterTextChanged(editable: Editable) {
            var count = editable.toString()
            if (TextUtils.isEmpty(editable.toString())){
                count = "1"
            }
            if (count.toInt() <= 0){
                count = "1"
            }
            mCount = count.toInt()
            mTotalCountTv.text = StringUtils.insertFrontAndBack(count, "共", "件 小计")
            mTotalPriceTv.text = StringUtils.insertFront(BigDecimalUtil.mul(mCurrentUseEntity.price.toDouble(), count.toDouble()), "¥")
            mCountEt.removeTextChangedListener(this)
            mCountEt.setText(count)
            mCountEt.setSelection(OtherUtil.getContentLength(mCountEt))//将光标移至文字末尾
            mCountEt.addTextChangedListener(this)
        }
    }
    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val vipEntity = intent.getSerializableExtra(IntentKey.VIP_Entity) as OverallVipCardEntity
        val notVipEntity = intent.getSerializableExtra(IntentKey.NOT_VIP_Entity) as OverallVipCardEntity
        mCleanFansTitleTv.text = "无打扰清粉 - 周卡 非VIP${notVipEntity.price},VIP${vipEntity.price}"
        if (UserOperateUtil.isVip()){
            mCurrentUseEntity = vipEntity
            mVipTagTv.visibility = View.VISIBLE
        }else{
            mCurrentUseEntity = notVipEntity
            mVipTagTv.visibility = View.GONE
        }
        mSinglePriceTv.text = StringUtils.insertFront(BigDecimalUtil.mul(mCurrentUseEntity.price.toDouble(), mCount.toDouble()), "¥")
        mTotalCountTv.text = StringUtils.insertFrontAndBack(mCount, "共", "件 小计")
        mTotalPriceTv.text = StringUtils.insertFront(BigDecimalUtil.mul(mCurrentUseEntity.price.toDouble(), mCount.toDouble()), "¥")
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mReduceTv ->{
                if (mCount > 1){
                    mCount-=1
                    mCountEt.setText(mCount.toString())
                    mTotalCountTv.text = StringUtils.insertFrontAndBack(mCount, "共", "件 小计")
                    mTotalPriceTv.text = StringUtils.insertFront(BigDecimalUtil.mul(mCurrentUseEntity.price.toDouble(), mCount.toDouble()), "¥")
                }
            }
            R.id.mPlusTv ->{
                mCount+=1
                mCountEt.setText(mCount.toString())
                mTotalCountTv.text = StringUtils.insertFrontAndBack(mCount, "共", "件 小计")
                mTotalPriceTv.text = StringUtils.insertFront(BigDecimalUtil.mul(mCurrentUseEntity.price.toDouble(), mCount.toDouble()), "¥")
            }
            R.id.mBuyTv ->{
                createOrder()
            }
        }
    }
    /**
     * 创建订单的接口
     */
    private fun createOrder(){
        var request: PostRequest = EasyHttp.post(HttpConfig.ORDER, false)
                .params("way", "add")
                .params("pay", "appalipay")
                .params("money", mCurrentUseEntity.id)
                .params("uid", UserOperateUtil.getUserId())
                .params("pay_channel", "支付宝")
                .params("number", mCount.toString())
                .params("os", "android")

        request.execute(object : CallBackProxy<OverallApiEntity<OverallVipCardOrderEntity>, OverallVipCardOrderEntity>(object : SimpleCallBack<OverallVipCardOrderEntity>() {
            override fun onSuccess(t: OverallVipCardOrderEntity?) {
                t?.info?.let { AlipayUtil().init(this@OverallCleanFansConfirmOrderActivity, it, 2) }
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPaySuccess(result: String) {
        if (result == "激活码购买成功"){
            finish()
        }
    }
}
