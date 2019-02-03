package app.jietuqi.cn.wechat.simulator.ui.fragment

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatFragment
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.simulator.WechatSimulatorUnReadEntity
import kotlinx.android.synthetic.main.fragment_wechat_discover.*

/**
 * 作者： liuyuanbo on 2018/10/9 17:42.
 * 时间： 2018/10/9 17:42
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 发现
 */

class WechatDiscoverFragment : BaseWechatFragment() {
    override fun setLayoutResouceId(): Int {
        return R.layout.fragment_wechat_discover
    }

    override fun initAllViews() {
        if (UserOperateUtil.hasUnReadFriendCircle()){
            GlideUtil.displayHead(activity, RandomUtil.getRandomAvatar(), mWechatSimulatorDiscoverUnReadIv)
        }else{
            mWechatSimulatorDiscoverUnReadLayout.visibility = View.GONE
        }
    }

    override fun initViewsListener() {
        mWechatSimulatorDiscoverCircleLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatSimulatorDiscoverCircleLayout ->{
                val hasUnRead = UserOperateUtil.hasUnReadFriendCircle()
                if (hasUnRead){
                    mWechatSimulatorDiscoverUnReadLayout.visibility = View.GONE
                    EventBusUtil.post(WechatSimulatorUnReadEntity(1, 0))
                }else{
                    GlideUtil.displayHead(activity, RandomUtil.getRandomAvatar(), mWechatSimulatorDiscoverUnReadIv)
                    mWechatSimulatorDiscoverUnReadLayout.visibility = View.VISIBLE
                    EventBusUtil.post(WechatSimulatorUnReadEntity(1, -1))
                }
                SharedPreferencesUtils.putData(SharedPreferenceKey.UNREAD_FRIEND_CIRCLE, !hasUnRead)
            }
        }
    }
}
