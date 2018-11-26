package app.jietuqi.cn.ui.fragment

import android.util.Log
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseFragment
import app.jietuqi.cn.callback.RefreshListener
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.activity.*
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.dialog.InviteDialog
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.request.PostRequest
import kotlinx.android.synthetic.main.fragment_my.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 作者： liuyuanbo on 2018/10/23 17:30.
 * 时间： 2018/10/23 17:30
 * 邮箱： 972383753@qq.com
 * 用途： 我的页面
 */

class MyFragment : BaseFragment(), RefreshListener {
    private var mUserEntity: OverallUserInfoEntity? = null
    override fun setLayoutResouceId() = R.layout.fragment_my
    override fun initAllViews() {
        setTitle("个人中心", 1)
    }
    override fun refresh() {
        if (UserOperateUtil.isCurrentLoginNoDialog()){
            loadFromServer()
        }else{
            mMyRefreshLayout.finishRefresh(true)
        }
    }

    override fun initViewsListener() {
        setRefreshLayout(mMyRefreshLayout, this)
        mUserInfoLayout.setOnClickListener(this)
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
        if (UserOperateUtil.isCurrentLoginDirectlyLogin(activity)){
            when(v.id){
                R.id.mUserInfoLayout ->{
                    LaunchUtil.launch(activity, OverallAccountManagementActivity::class.java)
                }
                R.id.mOpenVipLayout ->{
                    LaunchUtil.launch(activity, OverallPurchaseVipActivity::class.java)
                }
                R.id.mOpenShopLayout ->{
                    LaunchUtil.launch(activity, OverallAddFansAndGroupsActivity::class.java)
                }
                R.id.mInviteLayout ->{
                    InviteDialog().show(activity?.supportFragmentManager, "invite")
                }
                R.id.mCreditLayout ->{ LaunchUtil.launch(activity, OverallCommunicateActivity::class.java) }

                R.id.mQQGroupLayout ->{

                }
                R.id.mContactServerLayout ->{
                    LaunchUtil.launch(activity, OverAllChoiceServiceActivity::class.java)
                }
                R.id.mProblemReportLayout ->{
                    LaunchUtil.launch(activity, ProblemReportActivity::class.java)
                }
                R.id.mSettingLayout ->{
                    LaunchUtil.launch(activity, SettingActivity::class.java)
                }
                R.id.mExitLayout ->{
                    mUserEntity = OverallUserInfoEntity()
                    showLoadingDialog("正在退出...")
                    SharedPreferencesUtils.putData(SharedPreferenceKey.IS_LOGIN, false)
                    SharedPreferencesUtils.saveBean2Sp(mUserEntity, SharedPreferenceKey.USER_INFO)
                    launch(Dispatchers.Default) { // 在一个公共线程池中创建一个协程
                        delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
                        dismissLoadingDialog()
                    }
                    refreshUserInfo()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("resume", "1111111111111111111111111")
        refresh()
        refreshUserInfo()
    }
    private fun refreshUserInfo(){
        if (UserOperateUtil.isCurrentLoginNoDialog()){
            mExitLayout.visibility = View.VISIBLE
        }else{
            mExitLayout.visibility = View.GONE
        }
        GlideUtil.displayHead(activity, mUserEntity?.headimgurl, mAvatarIv)
        mNickNameTv.text = mUserEntity?.nickname
        mIdTv.text = mUserEntity?.id.toString()
    }

    override fun loadFromServer() {
        super.loadFromServer()
        if (UserOperateUtil.isCurrentLoginNoDialog()){
            var request: PostRequest = EasyHttp.post(HttpConfig.USERS).params("way", "id").params("id", UserOperateUtil.getUserId())
            request.execute(object : CallBackProxy<OverallApiEntity<OverallUserInfoEntity>, OverallUserInfoEntity>(object : SimpleCallBack<OverallUserInfoEntity>() {
                override fun onError(e: ApiException) {
                    e.message?.let { showToast(it) }
                    mMyRefreshLayout.finishRefresh(true)
                }

                override fun onSuccess(t: OverallUserInfoEntity) {
                    mMyRefreshLayout.finishRefresh(true)
                    mUserEntity = t
                    SharedPreferencesUtils.saveBean2Sp(t, SharedPreferenceKey.USER_INFO)
                    refreshUserInfo()
                }
            }) {})
        }
    }


}
