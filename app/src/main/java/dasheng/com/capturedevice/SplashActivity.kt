package dasheng.com.capturedevice

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import dasheng.com.capturedevice.base.BaseActivity
import dasheng.com.capturedevice.constant.RequestCode
import dasheng.com.capturedevice.ui.HomeActivity
import dasheng.com.capturedevice.util.LaunchUtil
import dasheng.com.capturedevice.widget.sweetalert.SweetAlertDialog
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/10/15 10:43.
 * 时间： 2018/10/15 10:43
 * 邮箱： 972383753@qq.com
 * 用途： 开机启动页面
 */

@RuntimePermissions
class SplashActivity : BaseActivity() {
    lateinit var mDialog: SweetAlertDialog
    override fun setLayoutResourceId(): Int {
        return 0
    }

    override fun needLoadingView(): Boolean {
        return false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initAllViews() {
        requestPrivatePermission()
//        SplashActivityPermissionsDispatcher.requestPrivateAuthorityWithPermissionCheck(this)
    }

    override fun initViewsListener() {
    }

    /*@NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun requestPrivateAuthority() {
        //7.0或者往上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isFinishing) {
                if (checkInstallPermission()) {
                    LaunchUtil.launch(this, HomeActivity::class.java)
                    finish()
                }
            }
        } else {
            LaunchUtil.launch(this, HomeActivity::class.java)
            finish()
        }
    }*/

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        SplashActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults)
//    }

//    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    fun requestPrivateAuthorityRationale(request: PermissionRequest) {
//        goSetPermission()
//    }

//    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    fun requestPrivateAuthorityDenied() {
//        goSetPermission()
//    }

//    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    fun requestPrivateAuthorityNeverAskAgain() {
//        goSetPermission()
//    }

    private fun checkInstallPermission(): Boolean {
        // 核心是下面几句代码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            if (!packageManager.canRequestPackageInstalls()) {//没有权限
//                startInstallPermissionSettingActivity()
                SweetAlertDialog(this@SplashActivity, SweetAlertDialog.WARNING_TYPE)
                        .setCanTouchOutSideCancle(false)
                        .canCancle(false)
                        .setTitleText("允许安装提示")
                        .setContentText("安装应用需要打开未知来源权限，请去设置中开启权限")
                        .setConfirmText("下一步")
                        .setConfirmClickListener { sweetAlertDialog ->
                            startInstallPermissionSettingActivity()
                            sweetAlertDialog.dismissWithAnimation()
                        }.show()
                return false
            } else {
                return true
            }
        }
        return true
    }

//    public override fun onDestroy() {
//        super.onDestroy()
//        if (mDialog != null && mDialog.isShowing) {
//            mDialog.cancel()
//        }
//    }
    /**
     * 让用户去开启权限
     */
    private fun goSetPermission() {
        if (!this.isFinishing) {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setCanTouchOutSideCancle(false)
                    .setTitleText("未取得您的使用权限，App无法开启。请在应用权限设置中打开权限")
                    ?.setContentText("必要权限不开启会影响使用")
                    ?.setCanTouchOutSideCancle(false)
                    ?.setConfirmText("好，去设置")
                    ?.setConfirmClickListener { sDialog ->
                        sDialog.dismissWithAnimation()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        intent.data = Uri.parse("package:" + packageName)
                        this.startActivityForResult(intent, RequestCode.PERMISSION_NEED)
                    }?.show()
        }
    }

    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun startInstallPermissionSettingActivity() {
        val packageURI = Uri.parse("package:" + packageName)
        //注意这个是8.0新API
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
        startActivityForResult(intent, RequestCode.UP_DATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.UP_DATE -> if (!isFinishing) {
                if (checkInstallPermission()) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            RequestCode.PERMISSION_NEED -> requestPrivatePermission()
        }
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun requestPrivatePermission() {
        //7.0或者往上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isFinishing) {
                if (checkInstallPermission()) {
                    LaunchUtil.launch(this, HomeActivity::class.java)
                    finish()
                }
            }
        } else {
            LaunchUtil.launch(this, HomeActivity::class.java)
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun requestPrivatePermissionOnShowRationale(request: PermissionRequest) {
        goSetPermission()
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun requestPrivatePermissionOnPermissionDenied() {
        goSetPermission()
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun requestPrivatePermissionOnNeverAskAgain() {
        goSetPermission()
    }
}