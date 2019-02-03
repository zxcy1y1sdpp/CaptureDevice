package app.jietuqi.cn.wechat.screenshot

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.util.StringUtils
import kotlinx.android.synthetic.main.activity_wechat_my_wallet.*

/**
 * 作者： liuyuanbo on 2019/2/1 09:50.
 * 时间： 2019/2/1 09:50
 * 邮箱： 972383753@qq.com
 * 用途： 微信截图 -- 我的钱包
 */
class WechatScreenShotMyWalletActivity : BaseWechatActivity() {

    private lateinit var mAdapter: MyWalletAdapter
    override fun setLayoutResourceId() = R.layout.activity_wechat_screenshot_my_wallet

    override fun needLoadingView() = false

    override fun initAllViews() {
        setStatusBarColor(ColorFinal.wechatMyWallet)
        setLightStatusBarForM(this, true)
    }

    override fun initViewsListener() {
        mWechatWalletBackTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mAdapter = MyWalletAdapter()
        mAdapter.setMoney(intent.getStringExtra(IntentKey.MONEY))
        mRecyclerView.adapter = mAdapter
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatWalletBackTv ->{
                finish()
            }
        }
    }
    inner class MyWalletAdapter: RecyclerView.Adapter<MyWalletAdapter.Holder>() {
        var balance: String = ""
        fun setMoney(ba: String){
            balance = ba
            notifyDataSetChanged()
        }
        override fun getItemCount() = 1

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.bind()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item1_wechat_my_wallet, parent, false))

        inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
            private val balanceTv: TextView = itemView.findViewById(R.id.balanceTv)
            fun bind() {
                balanceTv.text = StringUtils.insertFront(StringUtils.keep2Point(balance), "¥")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        needVip()
    }
}
