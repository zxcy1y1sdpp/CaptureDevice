package app.jietuqi.cn.ui.activity

import android.Manifest
import android.content.*
import android.net.Uri
import android.os.Environment
import android.provider.ContactsContract
import android.view.View
import app.jietuqi.cn.GlideApp
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.entity.ContactEntity
import app.jietuqi.cn.ui.entity.OverallCardEntity
import app.jietuqi.cn.util.ContactUtil
import app.jietuqi.cn.util.FileUtil
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.xinlan.imageeditlibrary.ToastUtils
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.DownloadProgressCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_personal_card.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import permissions.dispatcher.*
import java.io.File


/**
 * 作者： liuyuanbo on 2018/11/11 13:32.
 * 时间： 2018/11/11 13:32
 * 邮箱： 972383753@qq.com
 * 用途： 个人名片
 */
@RuntimePermissions
class OverallPersonalCardActivity : BaseOverallInternetActivity() {
    private lateinit var mOverallCardEntity: OverallCardEntity
    override fun setLayoutResourceId() = R.layout.activity_overall_personal_card

    override fun needLoadingView() = false

    override fun initAllViews() {}

    override fun initViewsListener() {
        mOverallPersonalSaveQRCodeLayout.setOnClickListener(this)
        mOverallPersonalSaveWXNumberLayout.setOnClickListener(this)
        mOverallPersonalImportLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallPersonalSaveQRCodeLayout ->{
                downloadQRCodeWithPermissionCheck()
            }
            R.id.mOverallPersonalSaveWXNumberLayout ->{
                try {
                    //获取剪贴板管理器：
                    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    // 创建普通字符型ClipData
                    val mClipData = ClipData.newPlainText("Label", mOverallCardEntity.wxname)
                    // 将ClipData内容放到系统剪贴板里。
                    cm.primaryClip = mClipData
                    SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setCanTouchOutSideCancle(false)
                            .canCancle(false)
                            .setTitleText("提示")
                            .setContentText("已复制到粘贴板，是否立即前往添加？")
                            .setConfirmText("立即添加")
                            .setCancelText("取消")
                            .setConfirmClickListener { sweetAlertDialog ->
                                val intent = Intent()
                                val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
                                intent.action = Intent.ACTION_MAIN
                                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.component = cmp
                                startActivity(intent)
                                sweetAlertDialog.dismissWithAnimation()
                            }.setCancelClickListener {
                                it.dismissWithAnimation()
                            }.show()
                }catch (e: ActivityNotFoundException){
                    ToastUtils.showShort(this, "您还没有安装微信，请安装后使用")
                }
            }
            R.id.mOverallPersonalImportLayout ->{
                addInContactsWithPermissionCheck()
            }
        }
    }


    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val type: Int = intent.getIntExtra(IntentKey.TYPE, 0)
        mOverallCardEntity = intent.getSerializableExtra(IntentKey.ENTITY) as OverallCardEntity
        mOverallPersonalSaveQRCodeTv.tag = mOverallCardEntity.wx_qr
        if (type == 0){
            setTopTitle("个人名片", 0)
            mOverallPersonalWxNumberTv.text = StringUtils.insertFront(mOverallCardEntity.wxname, "微信号：")
        }else{
            setTopTitle("群名片", 0)
            mOverallPersonalSaveQRCodeTv.text = "保存群二维码"
            mOverallPersonalSaveWXNumberTv.text = "复制群主微信号"
            mOverallPersonalWxNumberTv.text = StringUtils.insertFront(mOverallCardEntity.wxname, "群主微信号：")
            mOverallPersonalImportLayout.visibility = View.GONE
            if(mOverallCardEntity.number == 2){
                mOverallPersonalNoticeTv.visibility = View.VISIBLE
            }
        }
        mOverallPersonalOtherTypeTv.text = StringUtils.insertFront(mOverallCardEntity.wxname, "微信号：")
        mOverallPersonalOtherTypeTv.text = StringUtils.insertFrontAndBack("/", mOverallCardEntity.area, mOverallCardEntity.industry.name)
        mOverallPersonalCardTitleTv.text = mOverallCardEntity.wxnickname
        mOverallPersonalContentTv.text = mOverallCardEntity.content
        GlideUtil.displayHead(this, mOverallCardEntity.headimgurl, mOverallPersonalCardPicIv)


        GlideApp.with(this)
                .load(mOverallCardEntity.wx_qr)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .error(R.mipmap.loading)
                .fallback(R.mipmap.loading)
                .into(mOverallPersonalWxQRCodeIv)
//        GlideUtil.display(this, mOverallCardEntity.wx_qr, mOverallPersonalWxQRCodeIv)
    }

    /**
     * 保存二维码
     */
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun downloadQRCode() {
        var path = mOverallPersonalSaveQRCodeTv.tag.toString()
        //系统相册目录
        val galleryPath = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator
        EasyHttp.downLoad(mOverallCardEntity.wx_qr)
                .savePath(galleryPath)
                .saveName(FileUtil.getFileName(path))
                .execute(object : DownloadProgressCallBack<String>() {
                    override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {}

                    override fun onStart() {
                        showLoadingDialog("请稍后...")
                    }

                    override fun onComplete(path: String) {
                        SweetAlertDialog(this@OverallPersonalCardActivity, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("温馨提示")
                                .setContentText("二维码已保存到相册，可前往微信扫一扫功能，然后从相册中选择二维码")
                                .setConfirmText("前往")
                                .setCancelText("取消")
                                .setConfirmClickListener { sweetAlertDialog ->
                                    sweetAlertDialog.dismissWithAnimation()
                                    val intent = Intent()
                                    val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
                                    intent.action = Intent.ACTION_MAIN
                                    intent.addCategory(Intent.CATEGORY_LAUNCHER)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    intent.component = cmp
                                    startActivity(intent)
                                }.setCancelClickListener {
                                    it.dismissWithAnimation()
                                }.show()
                        showToast(StringUtils.insertFront(path, "保存至"))
                        // 最后通知图库更新
                        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(path))))
                        dismissLoadingDialog()
                    }

                    override fun onError(e: ApiException) {
                        showToast("保存失败！-- 请检查一下您的存储权限是否开启" )
                        dismissLoadingDialog()
                    }
                })
    }

    // 删除联系人
    private fun deleteContact(rawContactId: Long) {
//第一步先删除Contacts表中的数据
        contentResolver.delete(ContactsContract.Contacts.CONTENT_URI, ContactsContract.Contacts._ID + " =?", arrayOf(rawContactId.toString()))
//第二步再删除RawContacts表的数据
        contentResolver.delete(ContactsContract.RawContacts.CONTENT_URI, ContactsContract.RawContacts.CONTACT_ID + " =?", arrayOf(rawContactId.toString()))
        showToast("删除成功")
    }

    @NeedsPermission(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS)
    fun addInContacts() {
        showLoadingDialog()
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            ContactUtil.addOneContact(this@OverallPersonalCardActivity, ContactEntity(mOverallCardEntity.mobile, mOverallCardEntity.wxnickname))
            runOnUiThread {
                dismissLoadingDialog()
                showToast("添加成功")
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showRationaleContacts(request: PermissionRequest) {
        request.proceed()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun neverAskAgainContacts() {
        showToast("请授权 [ 微商营销宝 ] 的 [ 通讯录 ] 访问权限")
    }
}
