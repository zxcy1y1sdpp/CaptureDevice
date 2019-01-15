package app.jietuqi.cn.ui.fragment

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseFragment
import app.jietuqi.cn.http.RemoveWaterMarkEntity
import app.jietuqi.cn.http.RemoveWaterMarkParentEntity
import app.jietuqi.cn.http.util.MD5
import app.jietuqi.cn.util.FileUtil
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import cn.jzvd.Jzvd
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.DownloadProgressCallBack
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.fragment_remove_watermark.*
import java.io.File


/**
 * 作者： liuyuanbo on 2018/11/14 15:56.
 * 时间： 2018/11/14 15:56
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class RemoveWatermarkFragment : BaseFragment() {
    /** iiiLab分配的客户ID  */
    private val client = "87a735c046ac30a1"
    /** iiiLab分配的客户密钥  */
    private val clientSecretKey = "fb24e8bf6411850558f843209ebe0fb8"
    private var mSaveToCameraDialog: SweetAlertDialog? = null
    private lateinit var mClipboardManager: ClipboardManager
    private var mOnPrimaryClipChangedListener: ClipboardManager.OnPrimaryClipChangedListener? = null
    override fun setLayoutResouceId() = R.layout.fragment_remove_watermark

    override fun initAllViews() {
        setTitle("视频下载")
    }

    override fun initViewsListener() {
        mRemoveWaterMarkBtn.setOnClickListener(this)
        mRemoveWaterMarkCopyVideoPathBtn.setOnClickListener(this)
        mRemoveWaterMarkDownLoadVideoPathBtn.setOnClickListener(this)
        registerClipEvents()
    }

    /**
     * 注册剪切板复制、剪切事件监听
     */
    private fun registerClipEvents() {
        mClipboardManager = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        mOnPrimaryClipChangedListener = ClipboardManager.OnPrimaryClipChangedListener {
            if (mClipboardManager.hasPrimaryClip() && mClipboardManager.primaryClip.itemCount > 0) {
                // 获取复制、剪切的文本内容
                val content = mClipboardManager.primaryClip?.getItemAt(0)?.text.toString()
                SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定提取视频吗")
                        .setContentText(content)
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .setConfirmClickListener {
                            mRemoveWaterMarkUrlEt.setText(content)
                            removeWaterMark()
                            it.dismissWithAnimation()
                        }.setCancelClickListener {
                            it.dismissWithAnimation()
                        }.show()
                Log.d("TAG", "复制、剪切的内容为：$content")
            }
        }
        mClipboardManager.addPrimaryClipChangedListener(mOnPrimaryClipChangedListener)
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mRemoveWaterMarkBtn ->{
                removeWaterMark()
            }
            R.id.mRemoveWaterMarkCopyVideoPathBtn ->{
                if (TextUtils.isEmpty(mRemoveWaterMarkUrlEt.text.toString())){
                    showToast("链接为空")
                    return
                }
                OtherUtil.copy(activity, mRemoveWaterMarkUrlEt.text.toString())
            }
            R.id.mRemoveWaterMarkDownLoadVideoPathBtn ->{
                if (UserOperateUtil.isCurrentLogin(activity)){
                    mSaveToCameraDialog = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定保存该视频到相册吗")
                            .setContentText("保存到相册的视频需要您手动去清理")
                            .setConfirmText("保存")
                            .setCancelText("取消")
                            .setConfirmClickListener {
                                downLoadVideo()
                            }.setCancelClickListener {
                                it.dismissWithAnimation()
                            }
                    mSaveToCameraDialog?.show()

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
        EasyHttp.post("/video/download")
                .baseUrl("http://service.iiilab.com")
                .params("link", link)
                .params("timestamp", timestamp)
                .params("sign", sign)
                .params("client", client)
                .execute(object : CallBackProxy<RemoveWaterMarkParentEntity<RemoveWaterMarkEntity>, RemoveWaterMarkEntity>(object : SimpleCallBack<RemoveWaterMarkEntity>() {

                    override fun onStart() {
                        super.onStart()
                        showLoadingDialog("提取中...")
                    }
                    override fun onError(e: ApiException) {
                        Toast.makeText(activity, "解析失败", Toast.LENGTH_SHORT).show()
                        dismissLoadingDialog()
                    }

                    override fun onSuccess(removeWaterMarkEntity: RemoveWaterMarkEntity) {
                        dismissLoadingDialog()
                        mRemoveWaterMarkVideoPlayerLayout.visibility = View.VISIBLE
                        mRemoveWaterMarkVideoPlayer2.setUp(removeWaterMarkEntity.video, "", Jzvd.SCREEN_WINDOW_NORMAL)
                        mRemoveWaterMarkCopyVideoPathBtn.tag = removeWaterMarkEntity.video
                        GlideUtil.display(activity, removeWaterMarkEntity.cover, mRemoveWaterMarkVideoPlayer2.thumbImageView)
                    }
                }) {})
    }

    private fun downLoadVideo() {
        var videoRealPath = mRemoveWaterMarkCopyVideoPathBtn.tag.toString()
        //系统相册目录
        val galleryPath = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator
        EasyHttp.downLoad(videoRealPath)
                .savePath(galleryPath)
                .saveName(FileUtil.getFileName(videoRealPath))
                .execute(object : DownloadProgressCallBack<String>() {
                    override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                    }

                    override fun onStart() {
                        mSaveToCameraDialog?.changeAlertType(SweetAlertDialog.PROGRESS_TYPE)
                        mSaveToCameraDialog?.contentText = "请耐心等候"
                        mSaveToCameraDialog?.titleText = "保存中..."
                        mSaveToCameraDialog?.confirmText = "朕知道了"
                        mSaveToCameraDialog?.showCancelButton(false)
                    }

                    override fun onComplete(path: String) {
                        mSaveToCameraDialog?.confirmText = "退下吧"
                        mSaveToCameraDialog?.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                        mSaveToCameraDialog?.titleText = "朕知道了"
                        mSaveToCameraDialog?.showContentText(false)
                        mSaveToCameraDialog?.setConfirmClickListener {
                            it.dismissWithAnimation()
                        }
                        // 最后通知图库更新
                        activity?.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(path))))
                        dismissLoadingDialog()
                    }

                    override fun onError(e: ApiException) {
                        showToast("保存失败！")
                        dismissLoadingDialog()
                    }
                })
    }
    /**
     * 注销监听，避免内存泄漏。
     */
    override fun onDestroy() {
        super.onDestroy()
        if (mOnPrimaryClipChangedListener != null) {
            mClipboardManager.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener)
        }
    }

    override fun visiableForUser() {
        super.visiableForUser()
        registerClipEvents()
    }

    override fun invisiableForUser() {
        super.invisiableForUser()
        Jzvd.releaseAllVideos()
        if (mOnPrimaryClipChangedListener != null) {
            mClipboardManager.removePrimaryClipChangedListener(mOnPrimaryClipChangedListener)
        }
    }
}
