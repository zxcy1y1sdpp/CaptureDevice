package app.jietuqi.cn.ui.activity

import android.Manifest
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallIndustryEntity
import app.jietuqi.cn.util.ContactUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_add_fans_and_group.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/11/10 14:47.
 * 时间： 2018/11/10 14:47
 * 邮箱： 972383753@qq.com
 * 用途： 加粉加群页面
 */
@RuntimePermissions
class OverallAddFansAndGroupsActivity : BaseOverallActivity() {
    private var mDialog: SweetAlertDialog? = null
    override fun setLayoutResourceId() = R.layout.activity_overall_add_fans_and_group

    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("加粉加群", 0)
        getIndustryData()
        getGroupIndustryData()
    }

    override fun initViewsListener() {
        mOverallAddFansAndGroupAddFansBtn.setOnClickListener(this)
        mOverallAddFansAndGroupAddGroupsBtn.setOnClickListener(this)
        mOverallAddFansAndGroupPreciseAddFansLayout.setOnClickListener(this)
        mOverallAddFansAndGroupNumberAddFansLayout.setOnClickListener(this)
        mOverallAddFansAndGroupVipBoomLayout.setOnClickListener(this)
        mOverallAddFansAndGroupClearAddressListLayout.setOnClickListener(this)
        mOverallAddFansAndGroupUseHelpLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallAddFansAndGroupAddFansBtn ->{//加粉
                LaunchUtil.startOverallJoinGroupsActivity(this, 0)
            }
            R.id.mOverallAddFansAndGroupAddGroupsBtn ->{//加群
                LaunchUtil.startOverallJoinGroupsActivity(this, 1)
            }
            R.id.mOverallAddFansAndGroupPreciseAddFansLayout ->{//精准加粉
                LaunchUtil.launch(this, OverallPreciseAddFansActivity::class.java)
            }
            R.id.mOverallAddFansAndGroupNumberAddFansLayout ->{//号段加粉
                LaunchUtil.launch(this, OverallNumberAddFansActivity::class.java)
            }
            R.id.mOverallAddFansAndGroupVipBoomLayout ->{//VIP高级爆粉
                if(UserOperateUtil.isCurrentLoginDirectlyLogin(this)){
                    if (UserOperateUtil.isVip()){
                        LaunchUtil.launch(this, OverallExplodeActivity::class.java)
                    }else{
                        SweetAlertDialog(this@OverallAddFansAndGroupsActivity, SweetAlertDialog.WARNING_TYPE)
                                .setCanTouchOutSideCancle(false)
                                .canCancle(false)
                                .setTitleText("您还不是会员")
                                .setContentText("会员拥有爆粉权利")
                                .setConfirmText("去开通")
                                .setCancelText("再想想")
                                .setConfirmClickListener { sweetAlertDialog ->
                                    sweetAlertDialog.dismissWithAnimation()
                                    LaunchUtil.launch(this, OverallPurchaseVipActivity::class.java)
                                }.setCancelClickListener {
                                    it.dismissWithAnimation()
                                }.show()
                    }
                }
            }
            R.id.mOverallAddFansAndGroupClearAddressListLayout ->{//清理通讯录
                clearContactsWithPermissionCheck()
//                clearContacts()
            }
            R.id.mOverallAddFansAndGroupUseHelpLayout ->{//使用教程
                LaunchUtil.startOverallWebViewActivity(this, "https://d.wps.cn/v/8u9sF", "使用教程")
            }
        }
    }

    /*private fun clearContacts(){

    }*/

    /**
     * 获取行业类别
     */
    private fun getIndustryData(){
        EasyHttp.post(HttpConfig.INFORMATION)
                .params("way", "industry")
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallIndustryEntity>>, ArrayList<OverallIndustryEntity>>(object : SimpleCallBack<ArrayList<OverallIndustryEntity>>() {
                    override fun onSuccess(t: ArrayList<OverallIndustryEntity>?) {
                        SharedPreferencesUtils.putListData(SharedPreferenceKey.INDUSTRY, t)
                    }
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }
                }) {})
    }
    /**
     * 获取行业类别
     */
    private fun getGroupIndustryData(){
        EasyHttp.post(HttpConfig.INFORMATION)
                .params("way", "heapsort")
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallIndustryEntity>>, ArrayList<OverallIndustryEntity>>(object : SimpleCallBack<ArrayList<OverallIndustryEntity>>() {
                    override fun onSuccess(t: ArrayList<OverallIndustryEntity>?) {
                        SharedPreferencesUtils.putListData(SharedPreferenceKey.HEAPSORT, t)
                    }
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }
                }) {})
    }

    @NeedsPermission(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS)
    fun clearContacts() {
        mDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("删除记录")
                .setContentText("只清除加粉时导入的数据，不会破坏您的原有通讯录")
                .setConfirmText("删除")
                .setCancelText("取消")
                .setConfirmClickListener {
                    showLoadingDialog("正在删除")
                    GlobalScope.launch { // 在一个公共线程池中创建一个协程
                        delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
                        dismissLoadingDialog()
                        runOnUiThread {
                            mDialog?.setTitleText("删除记录成功！")
                                    ?.setConfirmText("我知道了")
                                    ?.showCancelButton(false)
                                    ?.setConfirmClickListener {
                                        it.dismissWithAnimation()
                                    }
                                    ?.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                        }

                        ContactUtil.batchDelContact(this@OverallAddFansAndGroupsActivity)
                    }
                }
                .setCancelClickListener {
                    it.dismissWithAnimation()
                }
        mDialog?.show()
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS)
    fun showRationaleContacts(request: PermissionRequest) {
        request.proceed()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS)
    fun neverAskAgainContacts() {
        showToast("请授权 [ 微商营销宝 ] 的 [ 通讯录 ] 访问权限")
    }
}
