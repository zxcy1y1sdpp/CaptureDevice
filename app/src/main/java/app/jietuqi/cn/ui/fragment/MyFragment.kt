package app.jietuqi.cn.ui.fragment

import android.graphics.Color
import android.view.View
import android.widget.Toast
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
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import com.xinlan.imageeditlibrary.ToastUtils
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.request.PostRequest
import kotlinx.android.synthetic.main.fragment_my.*


/**
 * 作者： liuyuanbo on 2018/10/23 17:30.
 * 时间： 2018/10/23 17:30
 * 邮箱： 972383753@qq.com
 * 用途： 我的页面
 */

class MyFragment : BaseFragment(), RefreshListener {
    override fun needLoading() = false

    private var mUserEntity: OverallUserInfoEntity? = null
    override fun setLayoutResouceId() = R.layout.fragment_my
    override fun initAllViews() {

        if (UserOperateUtil.needColseByChannel()) {
            if (UserOperateUtil.isWandoujiaChannel()) {
                mMyPublishLayout.visibility = View.GONE
            }
        }
    }
    override fun refresh() {
        if (UserOperateUtil.isCurrentLoginNoDialog()){
            loadFromServer()
        }else{
            mUserEntity = OverallUserInfoEntity()
            mMyRefreshLayout.finishRefresh(true)
        }
    }
    override fun initViewsListener() {
        setRefreshLayout(mMyRefreshLayout, this)
        mUserInfoLayout.setOnClickListener(this)
        mMyPublishLayout.setOnClickListener(this)
        mOpenAgentLayout.setOnClickListener(this)
        mOpenVipLayout.setOnClickListener(this)
        mOpenShopLayout.setOnClickListener(this)
        mInviteLayout.setOnClickListener(this)
        mQQGroupLayout.setOnClickListener(this)
        mProblemReportLayout.setOnClickListener(this)
        mSettingLayout.setOnClickListener(this)
        mWbLayout.setOnClickListener(this)
        mServerHelpLayout.setOnClickListener(this)
        mQQGroupType.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (UserOperateUtil.isCurrentLoginDirectlyLogin(activity)){
            when(v.id){
                R.id.mOpenAgentLayout ->{
                    LaunchUtil.launch(activity, OverOpenAgentActivity::class.java)
                }
                R.id.mMyPublishLayout ->{
                    LaunchUtil.startOverallMyPublishActivity(activity, UserOperateUtil.getUserId(), UserOperateUtil.getUserNickName())
                }
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

                R.id.mQQGroupLayout ->{

                }
                R.id.mProblemReportLayout ->{
                    LaunchUtil.launch(activity, OverallProblemReportActivity::class.java)
                }
                R.id.mSettingLayout ->{
                    LaunchUtil.launch(activity, SettingActivity::class.java)
                }
                R.id.mWbLayout ->{
                    LaunchUtil.launch(activity, OverallWbActivity::class.java)
                }
                R.id.mServerHelpLayout ->{
                    LaunchUtil.launch(activity, OverallServerHelpActivity::class.java)
                }
                R.id.mQQGroupType ->{
                    try {
                        SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                .setCanTouchOutSideCancle(false)
                                .canCancle(false)
                                .setTitleText("复制成功")
                                .setContentColor(Color.RED)
                                .setConfirmText("立即前往")
                                .setCancelText("取消")
                                .setConfirmClickListener { sweetAlertDialog ->
                                    try {
                                        val intent = activity?.packageManager?.getLaunchIntentForPackage("com.tencent.mobileqq")
                                        startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(activity, "尚未安装QQ", Toast.LENGTH_SHORT).show()
                                    }
                                    sweetAlertDialog.dismissWithAnimation()
                                }.setCancelClickListener {
                                    it.dismissWithAnimation()
                                }.show()

                    } catch (e: Exception) {
                        e.printStackTrace()
                        ToastUtils.showShort(activity, "请检查是否安装QQ客户端")
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refresh()
        refreshUserInfo()
    }
    private fun refreshUserInfo(){
        GlideUtil.displayHead(activity, mUserEntity?.headimgurl, mAvatarIv)
        mNickNameTv.text = mUserEntity?.nickname
        mOpenIsVipTv.text = if (UserOperateUtil.isVip()) "已开通" else "未开通"
        mIdTv.text = mUserEntity?.id.toString()
        if (mUserEntity?.status == 2 || mUserEntity?.status == 3 || mUserEntity?.status == 4 || mUserEntity?.status == 5){
            mQQGroupType.text = "824315403"
            mQQGroupLayout.visibility = View.VISIBLE
        }else{
            mQQGroupLayout.visibility = View.GONE
        }
    }
    override fun loadFromServer() {
        super.loadFromServer()
        if (UserOperateUtil.isCurrentLoginNoDialog()){
            var request: PostRequest = EasyHttp.post(HttpConfig.USERS, false).params("way", "id").params("id", UserOperateUtil.getUserId())
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
