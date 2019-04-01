package app.jietuqi.cn.wechat.simulator.ui.activity

import android.graphics.Color
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.simulator.WechatSimulatorUnReadEntity
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.wechat.simulator.ui.fragment.WechatDiscoverFragment
import app.jietuqi.cn.wechat.simulator.ui.fragment.WechatFriendsFragment
import app.jietuqi.cn.wechat.simulator.ui.fragment.WechatListFragment
import app.jietuqi.cn.wechat.simulator.ui.fragment.WechatMyFragment
import app.jietuqi.cn.widget.MainNavigateTabBar
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

class WechatChatListActivity : BaseWechatActivity(), MainNavigateTabBar.onTabChangeListener{
    override fun onTabSelected(index: Int) {
        if (1 == index){
            SharedPreferencesUtils.putData(SharedPreferenceKey.SHOW_BOTTOM_RP, false)
            noNewFriendsCount(0)
        }
    }

    override fun setLayoutResourceId() = R.layout.activity_wechat_chatlist
    override fun needLoadingView() = false

    override fun initAllViews() {
        registerEventBus()
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
        setLightStatusBarForM(this, true)
        mMainTabBar.addTab(this, WechatListFragment::class.java, MainNavigateTabBar.TabParam(Color.parseColor("#F7F7F7"), R.drawable.wechat_unselect_home, R.drawable.wechat_select_home, "微信"))
        mMainTabBar.addTab(this, WechatFriendsFragment::class.java, MainNavigateTabBar.TabParam(Color.parseColor("#F7F7F7"), R.drawable.wechat_unselect_address_book, R.drawable.wechat_select_address_book, "通讯录"))
        mMainTabBar.addTab(this, WechatDiscoverFragment::class.java, MainNavigateTabBar.TabParam(Color.parseColor("#F7F7F7"), R.drawable.wechat_unselect_discover, R.drawable.wechat_select_discover, "发现"))
        mMainTabBar.addTab(this, WechatMyFragment::class.java, MainNavigateTabBar.TabParam(Color.parseColor("#F7F7F7"), R.drawable.wechat_unselect_my, R.drawable.wechat_select_my, "我"))
        mMainTabBar.setOnTabChangeListener(this)
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
                    mWechatBadgeView0.setText(unReadNumber.toString())
                }
            }
        }
        if (UserOperateUtil.hasUnReadFriendCircle()){
            mWechatBadgeView1.visibility = View.VISIBLE
        }else{
            mWechatBadgeView1.visibility = View.GONE
        }
        if (UserOperateUtil.showNewFriendsBottomRp()){
            val listAll = UserOperateUtil.getWechatNewFriendsList()
            val listUnAdd = arrayListOf<WechatUserEntity>()
            var entity: WechatUserEntity
            for (i in listAll.indices){
                entity = listAll[i]
                if (!entity.isChecked){
                    if (!entity.alreadyShow){
                        listUnAdd.add(entity)
                    }
                }
            }
            noNewFriendsCount(listUnAdd.size)
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
        if (entity.tag == 0) {
            mWechatBadgeView0.setText(entity.unRead.toString())
        }else if (entity.tag == 1){
            if (entity.unRead == 0){
                mWechatBadgeView1.visibility = View.GONE
            }else{
                mWechatBadgeView1.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        SharedPreferencesUtils.putData(SharedPreferenceKey.IS_IN_SIMULATOR, true)
        needVipForCover()
        when(mMainTabBar.currentSelectedTab){
            0 ->{
                setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
            }
            1 ->{
                setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
            }
            2 ->{
                setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
            }
            3 ->{
                setStatusBarColor(ColorFinal.WHITE)
            }
        }
        setLightStatusBarForM(this, true)
    }

    fun noNewFriendsCount(count: Int){
        if (count <= 0){
            mWechatNewFriendsTv.visibility = View.GONE
        }else{
            mWechatNewFriendsTv.visibility = View.VISIBLE
            mWechatNewFriendsTv.setText(count.toString())
        }
    }
}
