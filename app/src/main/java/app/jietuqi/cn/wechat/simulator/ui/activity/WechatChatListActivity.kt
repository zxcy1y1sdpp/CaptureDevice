package app.jietuqi.cn.wechat.simulator.ui.activity

import android.graphics.Color
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.wechat.simulator.ui.fragment.WechatDiscoverFragment
import app.jietuqi.cn.wechat.simulator.ui.fragment.WechatFriendsFragment
import app.jietuqi.cn.wechat.simulator.ui.fragment.WechatListFragment
import app.jietuqi.cn.wechat.simulator.ui.fragment.WechatMyFragment
import app.jietuqi.cn.widget.MainNavigateTabBar
import kotlinx.android.synthetic.main.activity_wechat_chatlist.*


/**
 * 作者： liuyuanbo on 2018/10/9 17:05.
 * 时间： 2018/10/9 17:05
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class WechatChatListActivity : BaseWechatActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_chatlist

    override fun needLoadingView() = false

    override fun initAllViews() {
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
        setLightStatusBarForM(this, true)
        mMainTabBar.addTab(this, WechatListFragment::class.java, MainNavigateTabBar.TabParam(Color.parseColor("#F7F7F7"), R.drawable.wechat_unselect_home, R.drawable.wechat_select_home, "微信"))
        mMainTabBar.addTab(this, WechatFriendsFragment::class.java, MainNavigateTabBar.TabParam(Color.parseColor("#F7F7F7"), R.drawable.wechat_unselect_address_book, R.drawable.wechat_select_address_book, "通讯录"))
        mMainTabBar.addTab(this, WechatDiscoverFragment::class.java, MainNavigateTabBar.TabParam(Color.parseColor("#F7F7F7"), R.drawable.wechat_unselect_discover, R.drawable.wechat_select_discover, "发现"))
        mMainTabBar.addTab(this, WechatMyFragment::class.java, MainNavigateTabBar.TabParam(Color.parseColor("#F7F7F7"), R.drawable.wechat_unselect_my, R.drawable.wechat_select_my, "我"))
    }

    override fun initViewsListener() {

    }

}
