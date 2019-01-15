package app.jietuqi.cn.widget

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import app.jietuqi.cn.R
import com.bm.zlzq.utils.ScreenUtil
import kotlinx.android.synthetic.main.dialog_qrcode.*

/**
 * 作者： liuyuanbo on 2018/11/27 12:31.
 * 时间： 2018/11/27 12:31
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class QRCodeDialog : DialogFragment(){
    private var mQrBitmap: Bitmap? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.attributes?.windowAnimations = R.style.ChangeRoleDialog
        return inflater.inflate(R.layout.dialog_qrcode, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mQrCodeIv.setImageBitmap(mQrBitmap)
    }
    fun setData(qrCodeBitmap: Bitmap){
        mQrBitmap = qrCodeBitmap
    }
    override fun onStart() {
        super.onStart()
        var screenWidth = activity?.let { ScreenUtil.getScreenWidth(it) }
        val width = screenWidth?.div(7)?.times(6)
        var window = dialog.window
        var params = window?.attributes
        params?.gravity = Gravity.CENTER_HORIZONTAL
        width?.let { params?.width = it }
        window?.attributes = params
    }
}

