package dasheng.com.capturedevice.ui.fragment

import android.view.View
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseFragment
import dasheng.com.capturedevice.ui.activity.*
import dasheng.com.capturedevice.util.LaunchUtil
import dasheng.com.capturedevice.widget.dialog.InviteDialog
import kotlinx.android.synthetic.main.fragment_my.*

/**
 * 作者： liuyuanbo on 2018/10/23 17:30.
 * 时间： 2018/10/23 17:30
 * 邮箱： 972383753@qq.com
 * 用途： 我的页面
 */

class MyFragment : BaseFragment() {
    override fun setLayoutResouceId() = R.layout.fragment_my

    override fun initAllViews() {
        setTitle("个人中心", 1)
    }

    override fun initViewsListener() {
        mOpenVipLayout.setOnClickListener(this)
        mOpenShopLayout.setOnClickListener(this)
        mInviteLayout.setOnClickListener(this)
        mCreditLayout.setOnClickListener(this)
        mQQGroupLayout.setOnClickListener(this)
        mContactServerLayout.setOnClickListener(this)
        mProblemReportLayout.setOnClickListener(this)
        mSettingLayout.setOnClickListener(this)
        mExitLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v?.id){
            R.id.mOpenVipLayout ->{
                LaunchUtil.launch(activity, OverallPurchaseVipActivity::class.java)
            }
            R.id.mOpenShopLayout ->{ }
            R.id.mInviteLayout ->{
                InviteDialog().show(activity?.supportFragmentManager, "invite")
            }
            R.id.mCreditLayout ->{ LaunchUtil.launch(activity, OverallCommunicateActivity::class.java) }

            R.id.mQQGroupLayout ->{ }
            R.id.mContactServerLayout ->{
                LaunchUtil.launch(activity, OverAllChoiceServiceActivity::class.java)
            }
            R.id.mProblemReportLayout ->{
                LaunchUtil.launch(activity, ProblemReportActivity::class.java)
            }
            R.id.mSettingLayout ->{
                LaunchUtil.launch(activity, SettingActivity::class.java)
            }
            R.id.mExitLayout ->{ }
        }
    }
}
