package dasheng.com.capturedevice.wechat.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.zhy.android.percent.support.PercentLinearLayout
import dasheng.com.capturedevice.GlideApp
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseWechatActivity
import dasheng.com.capturedevice.constant.ColorFinal
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.util.StringUtils
import kotlinx.android.synthetic.main.activity_wechat_my_wallet.*

/**
 * 作者： liuyuanbo on 2018/10/8 15:09.
 * 时间： 2018/10/8 15:09
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 我的钱包
 */

class WechatMyWalletActivity : BaseWechatActivity() {
    /**
     * 不用父类的statusbar颜色就需要重写oncreate（）并且设置颜色
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ColorFinal.wechatMyWallet)
    }
    override fun setLayoutResourceId() = R.layout.activity_wechat_my_wallet

    override fun needLoadingView() = false

    override fun initAllViews() {

    }

    override fun initViewsListener() {

    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mRecyclerView.adapter = MyWalletAdapter(intent.getStringExtra(IntentKey.MONEY))
    }

    inner class MyWalletAdapter(val balance: String): RecyclerView.Adapter<MyWalletAdapter.Holder>() {
        override fun getItemCount() = 1

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item1_wechat_my_wallet, parent, false))

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
            private val balanceTv: TextView = itemView.findViewById(R.id.balanceTv)
            override fun onClick(v: View?) {
                when(v?.id){
                    R.id.receivingLayout ->{}
                    R.id.chargeLayout ->{}
                    R.id.bankCardLayout ->{}
                    R.id.mMyWalletCreditCardLayout ->{}
                    R.id.mMyWalletPrePaidPhoneLayout ->{}
                    R.id.mMyWalletFinancialLayout ->{}
                    R.id.mMyWalletLifePayCostLayout ->{}
                    R.id.mMyWalletQBLayout ->{}
                    R.id.mMyWalletCityServiceLayout ->{}
                    R.id.mMyWalletTencentPublicWelfareLayout ->{}
                    R.id.mMyWalletTicketLayout ->{}
                    R.id.mMyWalletDiDiLayout ->{}
                    R.id.mMyWalletJDLayout ->{}
                    R.id.mMyWalletTakeOutLayout ->{}
                    R.id.mMyWalletFilmLayout ->{}
                    R.id.mMyWalletHavingFunLayout ->{}
                    R.id.mMyWalletHotelLayout ->{}
                    R.id.mMyWalletBikeLayout ->{}
                    R.id.mMyWalletWomenClothingLayout ->{}
                    R.id.mMyWallet58Layout ->{}
                    R.id.mMyWalletVIPShopLayout ->{}
                }
            }

            init {
                itemView.findViewById<LinearLayout>(R.id.receivingLayout).setOnClickListener(this)//收付款
                itemView.findViewById<RelativeLayout>(R.id.chargeLayout).setOnClickListener(this)//零钱
                itemView.findViewById<TextView>(R.id.balanceTv).setOnClickListener(this)//余额
                itemView.findViewById<LinearLayout>(R.id.bankCardLayout).setOnClickListener(this)//银行卡
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletCreditCardLayout).setOnClickListener(this)//信用卡还款
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletPrePaidPhoneLayout).setOnClickListener(this)//手机充值
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletFinancialLayout).setOnClickListener(this)//理财通
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletLifePayCostLayout).setOnClickListener(this)//生活缴费
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletQBLayout).setOnClickListener(this)//Q币充值
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletCityServiceLayout).setOnClickListener(this)//城市服务
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletTencentPublicWelfareLayout).setOnClickListener(this)//腾讯公益
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletTicketLayout).setOnClickListener(this)//火车票机票
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletDiDiLayout).setOnClickListener(this)//滴滴出行
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletJDLayout).setOnClickListener(this)//京东优选
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletTakeOutLayout).setOnClickListener(this)//美团外卖
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletFilmLayout).setOnClickListener(this)//电影演出赛事
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletHavingFunLayout).setOnClickListener(this)//吃喝玩乐
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletHotelLayout).setOnClickListener(this)//酒店
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletBikeLayout).setOnClickListener(this)//摩拜单车
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletWomenClothingLayout).setOnClickListener(this)//蘑菇街女装
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWallet58Layout).setOnClickListener(this)//58到家
                itemView.findViewById<PercentLinearLayout>(R.id.mMyWalletVIPShopLayout).setOnClickListener(this)//唯品会
            }
            fun bind() {
                balanceTv.text = StringUtils.insertFront(StringUtils.keep2Point(balance), "¥")
            }
        }
    }
}
