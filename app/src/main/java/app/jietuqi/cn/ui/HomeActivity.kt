package app.jietuqi.cn.ui

import android.widget.Toast
import app.jietuqi.cn.AppManager
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseActivity
import app.jietuqi.cn.ui.fragment.HomeFragment
import app.jietuqi.cn.ui.fragment.MyFragment
import app.jietuqi.cn.widget.MainNavigateTabBar
import cn.jzvd.Jzvd
import kotlinx.android.synthetic.main.activity_wechat_chatlist.*

/**
 * 作者： liuyuanbo on 2018/10/23 17:14.
 * 时间： 2018/10/23 17:14
 * 邮箱： 972383753@qq.com
 * 用途： App首页
 */

open class HomeActivity : BaseActivity() {
    override fun setLayoutResourceId() = R.layout.activity_home

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        mMainTabBar.addTab(this, HomeFragment::class.java, MainNavigateTabBar.TabParam(R.drawable.overall_home_unselect, R.drawable.overall_home_select, "首页"))
//        mMainTabBar.addTab(this, RemoveWatermarkFragment::class.java, MainNavigateTabBar.TabParam(R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, "去水印"))
//        mMainTabBar.addTab(this, WechatDiscoverFragment::class.java, MainNavigateTabBar.TabParam(R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, "更多"))
        mMainTabBar.addTab(this, MyFragment::class.java, MainNavigateTabBar.TabParam(R.drawable.overall_my_unselect, R.drawable.overall_my_select, "我的"))
    }

    override fun initViewsListener() {}

    private var mExitTime: Long = 0
    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        if (System.currentTimeMillis() - mExitTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
            mExitTime = System.currentTimeMillis()
        } else {
            Jzvd.releaseAllVideos()
            AppManager.getInstance().AppExit(this)
        }
    }
}
