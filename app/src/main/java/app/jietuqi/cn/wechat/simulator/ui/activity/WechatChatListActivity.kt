package app.jietuqi.cn.wechat.simulator.ui.activity

import android.graphics.Color
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.simulator.WechatSimulatorUnReadEntity
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.wechat.simulator.ui.fragment.WechatDiscoverFragment
import app.jietuqi.cn.wechat.simulator.ui.fragment.WechatFriendsFragment
import app.jietuqi.cn.wechat.simulator.ui.fragment.WechatListFragment
import app.jietuqi.cn.wechat.simulator.ui.fragment.WechatMyFragment
import app.jietuqi.cn.widget.MainNavigateTabBar
import app.jietuqi.cn.widget.badge.Badge
import app.jietuqi.cn.widget.badge.QBadgeView
import kotlinx.android.synthetic.main.activity_wechat_chatlist.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 作者： liuyuanbo on 2018/10/9 17:05.
 * 时间： 2018/10/9 17:05
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class WechatChatListActivity : BaseWechatActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_chatlist
    var mBadgeView0: Badge? = null
    var mBadgeView2: Badge? = null
    override fun needLoadingView() = false

    override fun initAllViews() {
        mBadgeView0 = QBadgeView(this).bindTarget(mWechatBadgeView0).setBadgeTextSize(10f, true)
        mBadgeView2 = QBadgeView(this).bindTarget(mWechatBadgeView2).setBadgeTextSize(10f, true)
        registerEventBus()
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
        setLightStatusBarForM(this, true)
        mMainTabBar.addTab(this, WechatListFragment::class.java, MainNavigateTabBar.TabParam(Color.parseColor("#F7F7F7"), R.drawable.wechat_unselect_home, R.drawable.wechat_select_home, "微信"))
        mMainTabBar.addTab(this, WechatFriendsFragment::class.java, MainNavigateTabBar.TabParam(Color.parseColor("#F7F7F7"), R.drawable.wechat_unselect_address_book, R.drawable.wechat_select_address_book, "通讯录"))
        mMainTabBar.addTab(this, WechatDiscoverFragment::class.java, MainNavigateTabBar.TabParam(Color.parseColor("#F7F7F7"), R.drawable.wechat_unselect_discover, R.drawable.wechat_select_discover, "发现"))
        mMainTabBar.addTab(this, WechatMyFragment::class.java, MainNavigateTabBar.TabParam(Color.parseColor("#F7F7F7"), R.drawable.wechat_unselect_my, R.drawable.wechat_select_my, "我"))
        mBadgeView0?.setGravityOffset(15f, 4f, true)
        mBadgeView0?.moreThan99(1)
        mBadgeView0?.setBadgeTextSize(35f, false)
        mBadgeView0?.isShowShadow = false

        mBadgeView2?.setGravityOffset(28f, 6f, true)
        mBadgeView2?.moreThan99(1)
        mBadgeView2?.setBadgeTextSize(35f, false)
        mBadgeView2?.isShowShadow = false

        var list = WechatSimulatorListHelper(this).queryAll()
        if (null != list){
            GlobalScope.launch { // 在一个公共线程池中创建一个协程
                var entity: WechatUserEntity
                var unReadNumber = 0
                for (i in list.indices) {
                    entity = list[i]
                    unReadNumber += entity.unReadNum.toInt()
                }
                runOnUiThread {
                    mBadgeView0?.badgeNumber = unReadNumber
                }
            }
        }
        if (UserOperateUtil.hasUnReadFriendCircle()){
            mBadgeView2?.badgeNumber = -1
        }else{
            mBadgeView2?.badgeNumber = 0
        }
    }

    override fun initViewsListener() {
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(entity: WechatUserEntity) {
        mMainTabBar.currentSelectedTab = 0
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onStatusBarChange(title: String) {
        when(title){
            "我" ->{
                setStatusBarColor(ColorFinal.WHITE)
            }
            "其他" ->{
                setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
            }
        }
        setLightStatusBarForM(this, true)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showOnReadNumbers(entity: WechatSimulatorUnReadEntity) {
        if (entity.tag == 0){
            mBadgeView0?.badgeNumber = entity.unRead
        }else if (entity.tag == 1){
            mBadgeView2?.badgeNumber = entity.unRead
        }
    }

    override fun onResume() {
        super.onResume()
        needVipForCover()
        when(mMainTabBar.currentSelectedTab){
            0 ->{
                setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
            }
            0 ->{
                setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
            }
            0 ->{
                setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
            }
            0 ->{
                setStatusBarColor(ColorFinal.WHITE)
            }
        }
        setLightStatusBarForM(this, true)
    }

}
