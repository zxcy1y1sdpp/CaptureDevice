package app.jietuqi.cn.wechat.simulator.ui.activity

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateGroupChatActivity
import app.jietuqi.cn.wechat.ui.activity.WechatAddTalkObjectActivity
import kotlinx.android.synthetic.main.activity_wechat_add_chat.*

/**
 * 作者： liuyuanbo on 2019/3/6 18:11.
 * 时间： 2019/3/6 18:11
 * 邮箱： 972383753@qq.com
 * 用途： 创建对话
 */
class WechatAddChatActivity : BaseWechatActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_add_chat

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("添加对话")
    }

    override fun initViewsListener() {
        mAddSingleChatIv.setOnClickListener(this)
        mAddGroupChatIv.setOnClickListener(this)
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAddSingleChatIv ->{
                LaunchUtil.launch(this, WechatAddTalkObjectActivity::class.java)
                finish()
            }
            R.id.mAddGroupChatIv ->{
                LaunchUtil.launch(this, WechatSimulatorCreateGroupChatActivity::class.java)
                finish()
            }
        }
    }
}
