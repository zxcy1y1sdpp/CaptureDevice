package app.jietuqi.cn.wechat.create

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.util.LaunchUtil
import kotlinx.android.synthetic.main.activity_wechat_loosechange.*

/**
 * 作者： liuyuanbo on 2018/10/3 21:45.
 * 时间： 2018/10/3 21:45
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 我的钱包
 */

class WechatLooseChangeActivity : BaseWechatActivity() {
    /**
     * 0 -- 我的钱包
     * 1 -- 微信零钱
     */
    private var mType = 0
    override fun setLayoutResourceId() = R.layout.activity_wechat_loosechange

    override fun needLoadingView() = false

    override fun initAllViews() {

        onlyOneEditTextNeedTextWatcher(mWechatLooseChangeEt, this)
    }

    override fun initViewsListener() {}

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        if (mType == 0){
            setWechatViewTitle("我的钱包")
        }else{
            setWechatViewTitle("微信零钱")
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.previewBtn ->{
                if (mType == 0){
                    LaunchUtil.startWechatMyWalletActivity(this, mWechatLooseChangeEt.text.toString())
                }else{
                    LaunchUtil.startWechatChangeActivity(this, mWechatLooseChangeEt.text.toString())
                }
            }
        }
    }
}
