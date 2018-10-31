package dasheng.com.capturedevice.ui

import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseActivity
import dasheng.com.capturedevice.ui.fragment.HomeFragment
import dasheng.com.capturedevice.ui.fragment.MyFragment
import dasheng.com.capturedevice.util.PreferencesUtils
import dasheng.com.capturedevice.wechat.ui.fragment.WechatDiscoverFragment
import dasheng.com.capturedevice.wechat.ui.fragment.WechatFriendsFragment
import dasheng.com.capturedevice.wechat.ui.fragment.WechatListFragment
import dasheng.com.capturedevice.wechat.ui.fragment.WechatMyFragment
import dasheng.com.capturedevice.widget.MainNavigateTabBar
import kotlinx.android.synthetic.main.activity_wechat_chatlist.*

/**
 * 作者： liuyuanbo on 2018/10/23 17:14.
 * 时间： 2018/10/23 17:14
 * 邮箱： 972383753@qq.com
 * 用途： App首页
 */

class HomeActivity : BaseActivity() {
    override fun setLayoutResourceId() = R.layout.activity_home

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        PreferencesUtils.putMyInfo(this)

        mMainTabBar.addTab(this, HomeFragment::class.java, MainNavigateTabBar.TabParam(R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, "首页"))
        mMainTabBar.addTab(this, WechatFriendsFragment::class.java, MainNavigateTabBar.TabParam(R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, "去水印"))
        mMainTabBar.addTab(this, WechatDiscoverFragment::class.java, MainNavigateTabBar.TabParam(R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, "更多"))
        mMainTabBar.addTab(this, MyFragment::class.java, MainNavigateTabBar.TabParam(R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, "我的"))
    }

    override fun initViewsListener() {

    }
}
