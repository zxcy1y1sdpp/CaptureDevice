package app.jietuqi.cn.ui.activity

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.http.RemoveWaterMarkEntity
import app.jietuqi.cn.http.RemoveWaterMarkParentEntity
import app.jietuqi.cn.http.util.MD5
import app.jietuqi.cn.util.*
import app.jietuqi.cn.widget.dialog.customdialog.EnsureDialog
import cn.jzvd.Jzvd
import com.xinlan.imageeditlibrary.ToastUtils
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.DownloadProgressCallBack
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_remove_watermark.*
import permissions.dispatcher.*
import java.io.File

/**
 * 作者： liuyuanbo on 2018/11/21 21:27.
 * 时间： 2018/11/21 21:27
 * 邮箱： 972383753@qq.com
 * 用途： 视频去水印
 */
@RuntimePermissions
class OverallRemoveWatermarkActivity : BaseOverallInternetActivity() {
    /** iiiLab分配的客户ID  */
    private val client = "87a735c046ac30a1"
    /** iiiLab分配的客户密钥  */
    private val clientSecretKey = "fb24e8bf6411850558f843209ebe0fb8"
    private var mClipboardManager: ClipboardManager? = null
    private var mOnPrimaryClipChangedListener: ClipboardManager.OnPrimaryClipChangedListener? = null
    private var mWaitDialog: EnsureDialog? = null
    override fun setLayoutResourceId() = R.layout.activity_overall_remove_watermark
    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("视频去水印")
    }
    override fun initViewsListener() {
        mRemoveWaterMarkBtn.setOnClickListener(this)
        mRemoveWaterMarkCopyVideoPathBtn.setOnClickListener(this)
        mRemoveWaterMarkDownLoadVideoPathBtn.setOnClickListener(this)
    }
    /**
     * 注册剪切板复制、剪切事件监听
     */
    private fun registerClipEvents() {
        mClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        mOnPrimaryClipChangedListener = ClipboardManager.OnPrimaryClipChangedListener {
            if (mClipboardManager?.hasPrimaryClip() == true && mClipboardManager?.primaryClip?.itemCount?.let { it > 0 } == true) {
                // 获取复制、剪切的文本内容
                val content = mClipboardManager?.primaryClip?.getItemAt(0)?.text.toString()
                if (TextUtils.isEmpty(content)){
                    return@OnPrimaryClipChangedListener
                }
                mWaitDialog = EnsureDialog(this).builder()
                        .setGravity(Gravity.CENTER)//默认居中，可以不设置
                        .setTitle("确定提取视频吗")//可以不设置标题颜色，默认系统颜色
                        .setSubTitle(content)
                        .setNegativeButton("取消") {}
                        .setPositiveButton("提取") {
                            mRemoveWaterMarkUrlEt.setText(content)
                            canAnalysis()
                        }
                mWaitDialog?.show()
            }
        }
        mClipboardManager?.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener)
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mRemoveWaterMarkBtn ->{
                if (UserOperateUtil.isCurrentLogin(this)){
                    canAnalysis()
                }
            }
            R.id.mRemoveWaterMarkCopyVideoPathBtn ->{
                if (TextUtils.isEmpty(mRemoveWaterMarkUrlEt.text.toString())){
                    showToast("链接为空")
                    return
                }
                OtherUtil.copy(this, mRemoveWaterMarkCopyVideoPathBtn.tag.toString())
            }
            R.id.mRemoveWaterMarkDownLoadVideoPathBtn ->{
                if (UserOperateUtil.isCurrentLogin(this)){
                    EnsureDialog(this).builder()
                            .setGravity(Gravity.CENTER)//默认居中，可以不设置
                            .setTitle("确定保存该视频到相册吗？")//可以不设置标题颜色，默认系统颜色
                            .setSubTitle("保存到相册的视频需要您手动去清理")
                            .setNegativeButton("取消") {}
                            .setPositiveButton("保存") {
                                downLoadVideoWithPermissionCheck()
                            }.show()
                }
            }
        }
    }

    /**
     * 提取视频
     */
    private fun removeWaterMark() {
        if (TextUtils.isEmpty(mRemoveWaterMarkUrlEt.text.toString())){
            showToast("链接为空")
            return
        }
        val link =  OtherUtil.readRealPath(mRemoveWaterMarkUrlEt.text.toString())
        val timestamp = System.currentTimeMillis().toString()
        val sign = MD5.string2MD5(link + timestamp + clientSecretKey)
        EasyHttp.post("/video/download", false)
                .baseUrl("http://service.iiilab.com")
                .params("link", link)
                .params("timestamp", timestamp)
                .params("sign", sign)
                .params("client", client)
                .execute(object : CallBackProxy<RemoveWaterMarkParentEntity<RemoveWaterMarkEntity>, RemoveWaterMarkEntity>(object : SimpleCallBack<RemoveWaterMarkEntity>() {
                    override fun onError(e: ApiException) {
                        ToastUtils.showShort(this@OverallRemoveWatermarkActivity, "解析失败")
                    }
                    override fun onStart() {
                        super.onStart()
                        showQQWaitDialog("提取中")
                    }
                    override fun onSuccess(removeWaterMarkEntity: RemoveWaterMarkEntity) {
                        dismissQQDialog()
                        mRemoveWaterMarkVideoPlayerLayout.visibility = View.VISIBLE
                        mRemoveWaterMarkVideoPlayer.setUp(removeWaterMarkEntity.video, "", Jzvd.SCREEN_WINDOW_NORMAL)
                        mRemoveWaterMarkCopyVideoPathBtn.tag = removeWaterMarkEntity.video
                        try {
                            if (!isDestroyed){
                                GlideUtil.display(this@OverallRemoveWatermarkActivity, removeWaterMarkEntity.cover, mRemoveWaterMarkVideoPlayer.thumbImageView)
                            }
                        }catch (e: Exception){
                            e.printStackTrace()
                        }

                    }
                }) {})
    }
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun downLoadVideo() {
        var videoRealPath = mRemoveWaterMarkCopyVideoPathBtn.tag.toString()
        //系统相册目录
        val galleryPath = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator
        EasyHttp.downLoad(videoRealPath)
                .savePath(galleryPath)
                .saveName(FileUtil.getFileName(videoRealPath))
                .execute(object : DownloadProgressCallBack<String>() {
                    override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                        Log.e("RxEasyHttp-----下载", bytesRead.toString())
                    }

                    override fun onStart() {
                        showQQWaitDialog("保存中")
                    }
                    override fun onComplete(path: String) {
                        dismissQQDialog("保存成功")
                        // 最后通知图库更新
                        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(path))))
                        dismissLoadingDialog()
                    }
                    override fun onError(e: ApiException) {
                        dismissQQDialog("保存失败", 1)
                    }
                })
    }

    private fun canAnalysis(){
        EasyHttp.post(HttpConfig.INDEX, false)
                .params("way", "watermark")
                .params("uid", UserOperateUtil.getUserId())
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException) {
                        EnsureDialog(this@OverallRemoveWatermarkActivity).builder()
                                .setGravity(Gravity.CENTER)//默认居中，可以不设置
                                .setTitle("友情提示")//可以不设置标题颜色，默认系统颜色
                                .setSubTitle(StringUtils.insertBack(e.message, "是否去开通会员？"))
                                .setNegativeButton("取消") {}
                                .setPositiveButton("开通") {
                                    LaunchUtil.launch(this@OverallRemoveWatermarkActivity, OverallPurchaseVipActivity::class.java)
                                }.show()
                    }
                    override fun onSuccess(t: String) {
                        removeWaterMark()
                    }
                })
    }
    /**
     * 注销监听，避免内存泄漏。
     */
    override fun onDestroy() {
        super.onDestroy()
        mOnPrimaryClipChangedListener?.let { mClipboardManager?.removePrimaryClipChangedListener(it) }
    }

    override fun onResume() {
        super.onResume()
        mOnPrimaryClipChangedListener?.let { mClipboardManager?.removePrimaryClipChangedListener(it) }
    }

    override fun onPause() {
        super.onPause()
        mWaitDialog?.dismiss()
        registerClipEvents()
        Jzvd.releaseAllVideos()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onShowRationale(request: PermissionRequest) {
        request.proceed()
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onNeverAskAgain() {
        showToast("请授权 [ 微商营销宝 ] 的 [ 存储 ] 访问权限")
    }
}
