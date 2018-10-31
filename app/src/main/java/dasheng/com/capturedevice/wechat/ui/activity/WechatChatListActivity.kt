package dasheng.com.capturedevice.wechat.ui.activity

import android.view.View
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseWechatActivity
import dasheng.com.capturedevice.wechat.ui.fragment.WechatFriendsFragment
import dasheng.com.capturedevice.wechat.ui.fragment.WechatDiscoverFragment
import dasheng.com.capturedevice.wechat.ui.fragment.WechatListFragment
import dasheng.com.capturedevice.wechat.ui.fragment.WechatMyFragment
import dasheng.com.capturedevice.widget.MainNavigateTabBar
import kotlinx.android.synthetic.main.activity_wechat_chatlist.*
import com.zhihu.matisse.engine.impl.GlideEngine
import android.content.pm.ActivityInfo
import com.zhihu.matisse.filter.Filter.K
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.internal.entity.CaptureStrategy
import dasheng.com.capturedevice.constant.RequestCode
import dasheng.com.capturedevice.util.LaunchUtil
import dasheng.com.capturedevice.widget.MyGlideEngine


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
