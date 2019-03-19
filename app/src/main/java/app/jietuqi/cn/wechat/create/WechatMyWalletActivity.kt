package app.jietuqi.cn.wechat.create

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
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatSimulatorWalletActivity
import com.zhy.android.percent.support.PercentRelativeLayout
import kotlinx.android.synthetic.main.activity_wechat_my_wallet.*

/**
 * 作者： liuyuanbo on 2018/10/8 15:09.
 * 时间： 2018/10/8 15:09
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 我的钱包
 */

class WechatMyWalletActivity : BaseWechatActivity() {
    private lateinit var mAdapter: MyWalletAdapter
    override fun setLayoutResourceId() = R.layout.activity_wechat_my_wallet

    override fun needLoadingView() = false

    override fun initAllViews() {
        setStatusBarColor(ColorFinal.wechatMyWallet)
        setLightStatusBarForM(this, true)
    }

    override fun initViewsListener() {
        mWechatWalletBackTv.setOnClickListener(this)
//        mMoreOperateIv.setOnClickListener(this)
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
            /*R.id.mMoreOperateIv ->{
                var showLqt = UserOperateUtil.showLqt()
                val menuItems = arrayListOf<MenuItem>()
                if (showLqt){
                    menuItems.add(MenuItem("关闭零钱通"))
                }else{
                    menuItems.add(MenuItem("展示零钱通"))
                }
                TopRightMenu(this).dimBackground(true)           //背景变暗，默认为true
                        .needAnimationStyle(true)   //显示动画，默认为true
                        .showIcon(false)
                        .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                        .addMenuList(menuItems)
                        .setOnMenuItemClickListener { position ->
                            when(position){
                                0 ->{
                                    SharedPreferencesUtils.putData(SharedPreferenceKey.SHOW_LQT, !showLqt)
                                }
                            }
                        }
                        .showAsDropDown(mMoreOperateIv, -180, 0)
            }*/
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
            init {
                itemView.findViewById<PercentRelativeLayout>(R.id.chargeLayout).setOnClickListener{
                    LaunchUtil.launch(itemView.context, WechatSimulatorWalletActivity::class.java)
                }
            }
            fun bind() {
                balanceTv.text = StringUtils.insertFront(StringUtils.keep2Point(balance), "¥")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        needVipForCover()
        var cash = UserOperateUtil.getWechatSimulatorMySelf().cash
        mAdapter.setMoney(cash.toString())
        mAdapter.notifyDataSetChanged()
    }
}
