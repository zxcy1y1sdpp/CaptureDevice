package app.jietuqi.cn.ui.activity

import android.graphics.Color
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.pay.alipay.AlipayUtil
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallVipCardOrderEntity
import app.jietuqi.cn.ui.entity.WbEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.request.PostRequest
import kotlinx.android.synthetic.main.activity_overall_wb.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 作者： liuyuanbo on 2019/1/29 10:29.
 * 时间： 2019/1/29 10:29
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class OverallWbActivity : BaseOverallInternetActivity() {
    private var mWbList = arrayListOf<WbEntity>()
    /**
     * 选中的Vip
     */
    private var mSelectCardEntity: WbEntity? = null
    private var mUserEntity: OverallUserInfoEntity? = null
    override fun setLayoutResourceId() = R.layout.activity_overall_wb

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        registerEventBus()
        refreshUserInfo()
        setTopTitle("我的微币", rightTitle = "交易记录", rightTvColor = R.color.transactionRecord)
    }

    override fun initViewsListener() {
        mWb100Layout.setOnClickListener(this)
        mWb400Layout.setOnClickListener(this)
        mWb1000Layout.setOnClickListener(this)
        mWb3000Layout.setOnClickListener(this)
        mBuyTv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.overAllRightTitleTv ->{
                LaunchUtil.launch(this, OverallWbDetailsActivity::class.java)
            }
            R.id.mWb100Layout ->{
                mSelectCardEntity = mWbList[0]
                mWb100Layout.setBackgroundResource(R.drawable.wb_select)
                mWb400Layout.setBackgroundResource(R.drawable.wb_normal)
                mWb1000Layout.setBackgroundResource(R.drawable.wb_normal)
                mWb3000Layout.setBackgroundResource(R.drawable.wb_normal)

                mWb100CountTitleTv.setTextColor(Color.parseColor("#F85A59"))
                mWb100CountTailTv.setTextColor(Color.parseColor("#F85A59"))
                mWb100CountPriceTv.setTextColor(Color.parseColor("#F85A59"))

                mWb400CountTitleTv.setTextColor(Color.parseColor("#212121"))
                mWb400CountTailTv.setTextColor(Color.parseColor("#212121"))
                mWb400CountPriceTv.setTextColor(Color.parseColor("#212121"))

                mWb1000CountTitleTv.setTextColor(Color.parseColor("#212121"))
                mWb1000CountTailTv.setTextColor(Color.parseColor("#212121"))
                mWb1000CountPriceTv.setTextColor(Color.parseColor("#212121"))

                mWb3000CountTitleTv.setTextColor(Color.parseColor("#212121"))
                mWb3000CountTailTv.setTextColor(Color.parseColor("#212121"))
                mWb3000CountPriceTv.setTextColor(Color.parseColor("#212121"))
            }
            R.id.mWb400Layout ->{
                mSelectCardEntity = mWbList[1]
                mWb400Layout.setBackgroundResource(R.drawable.wb_select)
                mWb100Layout.setBackgroundResource(R.drawable.wb_normal)
                mWb1000Layout.setBackgroundResource(R.drawable.wb_normal)
                mWb3000Layout.setBackgroundResource(R.drawable.wb_normal)


                mWb400CountTitleTv.setTextColor(Color.parseColor("#F85A59"))
                mWb400CountTailTv.setTextColor(Color.parseColor("#F85A59"))
                mWb400CountPriceTv.setTextColor(Color.parseColor("#F85A59"))

                mWb100CountTitleTv.setTextColor(Color.parseColor("#212121"))
                mWb100CountTailTv.setTextColor(Color.parseColor("#212121"))
                mWb100CountPriceTv.setTextColor(Color.parseColor("#212121"))

                mWb1000CountTitleTv.setTextColor(Color.parseColor("#212121"))
                mWb1000CountTailTv.setTextColor(Color.parseColor("#212121"))
                mWb1000CountPriceTv.setTextColor(Color.parseColor("#212121"))

                mWb3000CountTitleTv.setTextColor(Color.parseColor("#212121"))
                mWb3000CountTailTv.setTextColor(Color.parseColor("#212121"))
                mWb3000CountPriceTv.setTextColor(Color.parseColor("#212121"))
            }
            R.id.mWb1000Layout ->{
                mSelectCardEntity = mWbList[2]
                mWb1000Layout.setBackgroundResource(R.drawable.wb_select)
                mWb100Layout.setBackgroundResource(R.drawable.wb_normal)
                mWb400Layout.setBackgroundResource(R.drawable.wb_normal)
                mWb3000Layout.setBackgroundResource(R.drawable.wb_normal)

                mWb1000CountTitleTv.setTextColor(Color.parseColor("#F85A59"))
                mWb1000CountTailTv.setTextColor(Color.parseColor("#F85A59"))
                mWb1000CountPriceTv.setTextColor(Color.parseColor("#F85A59"))

                mWb400CountTitleTv.setTextColor(Color.parseColor("#212121"))
                mWb400CountTailTv.setTextColor(Color.parseColor("#212121"))
                mWb400CountPriceTv.setTextColor(Color.parseColor("#212121"))

                mWb100CountTitleTv.setTextColor(Color.parseColor("#212121"))
                mWb100CountTailTv.setTextColor(Color.parseColor("#212121"))
                mWb100CountPriceTv.setTextColor(Color.parseColor("#212121"))

                mWb3000CountTitleTv.setTextColor(Color.parseColor("#212121"))
                mWb3000CountTailTv.setTextColor(Color.parseColor("#212121"))
                mWb3000CountPriceTv.setTextColor(Color.parseColor("#212121"))
            }
            R.id.mWb3000Layout ->{
                mSelectCardEntity = mWbList[2]
                mWb3000Layout.setBackgroundResource(R.drawable.wb_select)
                mWb100Layout.setBackgroundResource(R.drawable.wb_normal)
                mWb400Layout.setBackgroundResource(R.drawable.wb_normal)
                mWb1000Layout.setBackgroundResource(R.drawable.wb_normal)

                mWb3000CountTitleTv.setTextColor(Color.parseColor("#F85A59"))
                mWb3000CountTailTv.setTextColor(Color.parseColor("#F85A59"))
                mWb3000CountPriceTv.setTextColor(Color.parseColor("#F85A59"))

                mWb400CountTitleTv.setTextColor(Color.parseColor("#212121"))
                mWb400CountTailTv.setTextColor(Color.parseColor("#212121"))
                mWb400CountPriceTv.setTextColor(Color.parseColor("#212121"))

                mWb100CountTitleTv.setTextColor(Color.parseColor("#212121"))
                mWb100CountTailTv.setTextColor(Color.parseColor("#212121"))
                mWb100CountPriceTv.setTextColor(Color.parseColor("#212121"))

                mWb1000CountTitleTv.setTextColor(Color.parseColor("#212121"))
                mWb1000CountTailTv.setTextColor(Color.parseColor("#212121"))
                mWb1000CountPriceTv.setTextColor(Color.parseColor("#212121"))
            }
            R.id.mBuyTv ->{
                if (canBuy()){
                    createOrder()
                }
            }
        }
    }

    private fun canBuy(): Boolean{
        if (null == mSelectCardEntity){
            showToast("请先选择需要购买微币的数量")
            return false
        }
        return true
    }
    override fun loadFromServer() {
        super.loadFromServer()
        getMoney()
    }
    private fun getMoney(){
        var request: PostRequest = EasyHttp.post(HttpConfig.GOLD, false).params("way", "goldprice")
        request.execute(object : CallBackProxy<OverallApiEntity<ArrayList<WbEntity>>, ArrayList<WbEntity>>(object : SimpleCallBack<ArrayList<WbEntity>>() {
            override fun onSuccess(t: ArrayList<WbEntity>) {
                mWbList = t
                mWb100CountTitleTv.text = mWbList[0].title
                mWb100CountPriceTv.text =  StringUtils.insertFront(mWbList[0].price, "¥")

                mWb400CountTitleTv.text = mWbList[1].title
                mWb400CountPriceTv.text = StringUtils.insertFront(mWbList[1].price, "¥")

                mWb1000CountTitleTv.text = mWbList[2].title
                mWb1000CountPriceTv.text = StringUtils.insertFront(mWbList[2].price, "¥")

                mWb3000CountTitleTv.text = mWbList[3].title
                mWb3000CountPriceTv.text = StringUtils.insertFront(mWbList[3].price, "¥")

            }
            override fun onError(e: ApiException) {
                e.message?.let { showToast(it) }
            }
        }) {})
    }
    /**
     * 创建订单的接口
     */
    private fun createOrder(){
        var request: PostRequest = EasyHttp.post(HttpConfig.GOLD, false)
                .params("way", "add")
                .params("pay", "appalipay")
                .params("money", mSelectCardEntity?.id.toString())
                .params("uid", UserOperateUtil.getUserId())
                .params("pay_channel", "支付宝")
                .params("classify", "weibi")
                .params("os", "android")

        request.execute(object : CallBackProxy<OverallApiEntity<OverallVipCardOrderEntity>, OverallVipCardOrderEntity>(object : SimpleCallBack<OverallVipCardOrderEntity>() {
            override fun onSuccess(t: OverallVipCardOrderEntity?) {
                mSelectCardEntity?.orderNum = t?.sn
                mSelectCardEntity?.payChannel = t?.pay_channel
                t?.info?.let { AlipayUtil().init(this@OverallWbActivity, it) }
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
    fun onSelectTimeEvent(result: String) {
        if (result == "微币充值成功"){
            refreshUserInfo()
        }
    }
    override fun refreshUser(user: OverallUserInfoEntity) {
        super.refreshUser(user)
        mUserEntity = user
        mWbTv.text = mUserEntity?.gold?.toString()
    }
}


