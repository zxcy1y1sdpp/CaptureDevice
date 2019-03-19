package app.jietuqi.cn.ui.activity

import android.Manifest
import android.view.Gravity
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.util.ContactUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.dialog.customdialog.EnsureDialog
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
    override fun setLayoutResourceId() = R.layout.activity_overall_add_fans_and_group

    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("加粉加群", 0)
    }

    override fun initViewsListener() {
        mOverallAddFansAndGroupAddFansBtn.setOnClickListener(this)
        mOverallAddFansAndGroupAddGroupsBtn.setOnClickListener(this)
        mOverallAddFansAndGroupPreciseAddFansLayout.setOnClickListener(this)
        mOverallAddFansAndGroupNumberAddFansLayout.setOnClickListener(this)
        mOverallAddFansAndGroupVipBoomLayout.setOnClickListener(this)
        mOverallAddFansAndGroupAddFansLayout.setOnClickListener(this)
        mOverallAddFansAndGroupClearAddressListLayout.setOnClickListener(this)
        mOverallAddFansAndGroupUseHelpLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallAddFansAndGroupAddFansBtn ->{//加粉
                LaunchUtil.startOverallJoinGroupsActivity(this, 0)
            }
            R.id.mOverallAddFansAndGroupAddFansLayout ->{//代加粉
                LaunchUtil.startOverallWebViewActivity(this, "http://www.jietuqi.cn/index/index/news/id/7", "官方(代加粉)")
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
                        EnsureDialog(this).builder()
                                .setGravity(Gravity.CENTER)//默认居中，可以不设置
                                .setTitle("您还不是会员")//可以不设置标题颜色，默认系统颜色
                                .setSubTitle("会员拥有爆粉权利")
                                .setNegativeButton("再想想") {}
                                .setPositiveButton("去开通") {
                                    LaunchUtil.launch(this, OverallPurchaseVipActivity::class.java)
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
    @NeedsPermission(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS)
    fun clearContacts() {
        EnsureDialog(this).builder()
                .setGravity(Gravity.CENTER)//默认居中，可以不设置
                .setTitle("删除记录")//可以不设置标题颜色，默认系统颜色
                .setSubTitle("只清除加粉时导入的数据，不会破坏您的原有通讯录")
                .setNegativeButton("取消") {}
                .setPositiveButton("删除") {
                    showQQWaitDialog("删除中")
                    GlobalScope.launch { // 在一个公共线程池中创建一个协程
                        delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
                        ContactUtil.batchDelContact(this@OverallAddFansAndGroupsActivity)
                        runOnUiThread {
                            dismissQQDialog("删除记录成功")
                        }
                    }
                }.show()
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
