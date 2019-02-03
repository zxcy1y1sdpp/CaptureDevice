package app.jietuqi.cn.wechat.simulator.ui.fragment

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatFragment
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.util.*
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatSimulatorMyInfoActivity
import kotlinx.android.synthetic.main.fragment_wechat_my.*

/**
 * 作者： liuyuanbo on 2018/10/9 17:42.
 * 时间： 2018/10/9 17:42
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 我的
 */

class WechatMyFragment : BaseWechatFragment() {
    override fun setLayoutResouceId() = R.layout.fragment_wechat_my

    override fun initAllViews() {
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
        activity?.let { setLightStatusBarForM(it, true) }
        val mySelf = UserOperateUtil.getWechatSimulatorMySelf()
        GlideUtil.displayHead(activity, mySelf.getAvatarFile(), sWechatMyAvatarIv)
        sWechatMyNickNameTv.text = mySelf.wechatUserNickName
    }

    override fun initViewsListener() {
        mMyWechatInfoLayout.setOnClickListener(this)
        mWechatMyPayLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mMyWechatInfoLayout ->{
                LaunchUtil.launch(activity, WechatSimulatorMyInfoActivity::class.java)
            }
            R.id.mWechatMyPayLayout ->{
                LaunchUtil.startWechatMyWalletActivity(activity, UserOperateUtil.getWechatSimulatorMySelf().cash.toString())
            }
        }
    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            EventBusUtil.post("我")
        }else{
            EventBusUtil.post("其他")
        }
    }

    override fun onStart() {
        super.onStart()
        EventBusUtil.post("我")
    }

    override fun onResume() {
        super.onResume()
        val mySelf = UserOperateUtil.getWechatSimulatorMySelf()
        GlideUtil.displayHead(activity, mySelf.getAvatarFile(), sWechatMyAvatarIv)
        sWechatMyNickNameTv.text = mySelf.wechatUserNickName
        sWechatMyWxNumberTv.text = StringUtils.insertFront(mySelf.wechatNumber, "微信号：")
    }
}
