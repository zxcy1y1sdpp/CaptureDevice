package app.jietuqi.cn.widget.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.BuildConfig
import app.jietuqi.cn.R
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.entity.ProductFlavorsEntity
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UpdateApkUtil
import app.jietuqi.cn.widget.DownloadView
import app.jietuqi.cn.widget.dialog.blurdialogfragment.SupportBlurDialogFragment
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import com.bm.zlzq.utils.ScreenUtil

/**
 * 作者： liuyuanbo on 2018/12/19 11:59.
 * 时间： 2018/12/19 11:59
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class UpdateDialog : SupportBlurDialogFragment(){
    private lateinit var mDownloadView: DownloadView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener { _, keyCode, _ ->
            keyCode == KeyEvent.KEYCODE_BACK
        }
        val bundle: Bundle? = arguments
        val bean: ProductFlavorsEntity = bundle?.getSerializable(IntentKey.ENTITY) as ProductFlavorsEntity
        val message = bean.intro.split(",")
        val view = inflater.inflate(R.layout.dialog_show_update, container, false)
        var messageTv: TextView = view.findViewById(R.id.upgrade_message)
        var version: TextView = view.findViewById(R.id.upgrade_version)
        val cancelIv: ImageView = view.findViewById(R.id.downloadCancleIv)
        version.text = StringUtils.insertFront(BuildConfig.VERSION_NAME, "v")
        if (message.isNotEmpty()) {
            val builder = StringBuilder()
            var i = 0
            val size = message.size
            while (i < size) {
                builder.append(message[i]).append("\n")
                i++
            }
            messageTv.text = builder.toString()
            messageTv.visibility = View.VISIBLE
        }

        if (bean.forced == 0){
            cancelIv.visibility = View.VISIBLE
            cancelIv.setOnClickListener {
                val dialog = SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                dialog.setCancelable(true)
                dialog.setTitleText("忽略更新能容可能会导致一些问题")
                        .setContentText("忽略之后可以通过: 我的->系统设置->检查更新,进行更新")
                        .setConfirmText("更新")
                        .setCancelText("以后再说")
                        .setCancelClickListener { sDialog ->
                            sDialog.cancel()
                            dismiss()
                            if (mDownloadView.isLoading){
                                mDownloadView.cancel()
                            }
                        }
                        .setConfirmClickListener { sDialog ->
                            if (mDownloadView.isLoading){

                            }
                            UpdateApkUtil(activity as AppCompatActivity?).updataApk(bean.apkurl, this, activity)
                            mDownloadView.performClick()
                            sDialog.dismissWithAnimation()
                        }.show()

            }
        }
        mDownloadView = view.findViewById(R.id.downLoadView)
        mDownloadView.setOnDownloadViewStatusListener(object : DownloadView.onDownloadViewStatusListener {
            override fun onCancle() {}

            override fun onContinue() {}

            override fun onStart() {
                UpdateApkUtil(activity as AppCompatActivity?).updataApk(bean.apkurl, this@UpdateDialog, activity)
                /* if ("app" == bean.channel){
                     UpdateDialog(activity as AppCompatActivity?).updataApk(bean.path, this@UpgradAppDialog, activity)
                 }else{
                     val intent = Intent()
                     intent.action = "android.intent.action.VIEW"
                     val content_url = Uri.parse("http://www.pgyer.com/ZLZQ")
                     intent.data = content_url
                     startActivity(intent)
                     dismiss()
                 }*/
                cancelIv.visibility = View.GONE
            }

            override fun onPause() {}
        })
        return view
    }

    override fun onStart() {
        super.onStart()
        var window = dialog.window
        var params = window?.attributes
        params?.gravity = Gravity.CENTER_HORIZONTAL
        var width = activity?.let { ScreenUtil.getScreenWidth(it) * 0.8 }
//        var width = ScreenUtil.getScreenWidth(context) * 0.8
        width?.let { params?.width = it.toInt() }
        window?.attributes = params
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
    override fun isDebugEnable(): Boolean = false

    /**
     * 背景是否变暗
     */
    override fun isDimmingEnable(): Boolean = true

    override fun isActionBarBlurred(): Boolean = false

    override fun getDownScaleFactor(): Float {
        return 2f
    }

    override fun getBlurRadius(): Int {
        return 2
    }

    override fun isRenderScriptEnable(): Boolean = false

    fun setProgress(progress: Int) {
        if (mDownloadView.isLoading){
            mDownloadView.setProgress(progress)
        }
    }
}
