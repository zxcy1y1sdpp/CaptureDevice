package app.jietuqi.cn.wechat.ui.activity

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.wechat.ui.fragment.WechatDiscoverFragment
import app.jietuqi.cn.wechat.ui.fragment.WechatFriendsFragment
import app.jietuqi.cn.wechat.ui.fragment.WechatListFragment
import app.jietuqi.cn.wechat.ui.fragment.WechatMyFragment
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
        mMainTabBar.addTab(this, WechatListFragment::class.java, MainNavigateTabBar.TabParam(R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, "微信"))
        mMainTabBar.addTab(this, WechatFriendsFragment::class.java, MainNavigateTabBar.TabParam(R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, "通讯录"))
        mMainTabBar.addTab(this, WechatDiscoverFragment::class.java, MainNavigateTabBar.TabParam(R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, "发现"))
        mMainTabBar.addTab(this, WechatMyFragment::class.java, MainNavigateTabBar.TabParam(R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, "我的"))
    }

    override fun initViewsListener() {
        mAddObjectIv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAddObjectIv ->{
                LaunchUtil.launch(this, WechatAddTalkObjectActivity::class.java)
            }
        }
    }
}
