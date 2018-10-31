package dasheng.com.capturedevice.ui.activity

import android.view.View
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseActivity
import kotlinx.android.synthetic.main.activity_setting.*

/**
 * 作者： liuyuanbo on 2018/10/25 15:10.
 * 时间： 2018/10/25 15:10
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class SettingActivity : BaseActivity() {
    override fun setLayoutResourceId() = R.layout.activity_setting

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTitle("设置")
    }

    override fun initViewsListener() {
        mClearCacheLayout.setOnClickListener(this)
        mWechatNumLayout.setOnClickListener(this)
        mAboutUsLayout.setOnClickListener(this)
        mGiveUsSupportLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mClearCacheLayout ->{}
            R.id.mWechatNumLayout ->{}
            R.id.mAboutUsLayout ->{}
            R.id.mGiveUsSupportLayout ->{}
        }
    }
}
