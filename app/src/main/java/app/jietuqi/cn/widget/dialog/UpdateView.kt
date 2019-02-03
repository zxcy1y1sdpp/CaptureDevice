package app.jietuqi.cn.widget.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import app.jietuqi.cn.R
import app.jietuqi.cn.widget.ProgressButton
import com.bm.zlzq.utils.ScreenUtil
import kotlinx.android.synthetic.main.update_view.*

/**
 * 作者： liuyuanbo on 2018/10/25 13:34.
 * 时间： 2018/10/25 13:34
 * 邮箱： 972383753@qq.com
 * 用途： 邀请的分享弹框
 */

class UpdateView : DialogFragment(), View.OnClickListener{
    private var mContent = ""
    private lateinit var mListener: UpdateListener
    private var url = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//注意此处
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.update_view, container, false)
    }
    override fun onStart() {
        super.onStart()
        var screenWidth = activity?.let { ScreenUtil.getScreenWidth(it) }
        val width = screenWidth?.div(4)?.times(3)
        var window = dialog.window
        var params = window?.attributes
        params?.gravity = Gravity.CENTER_HORIZONTAL
        width?.let { params?.width = it }
        window?.attributes = params
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateContentTv.text = mContent
        updateBtn.setOnClickListener(this)
        cancelBtn.setOnClickListener(this)
    }
    fun setData(content: String, url: String, listener: UpdateListener){
        mContent = content
        mListener = listener
        this.url = url
    }
    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.updateBtn -> {
                mListener.update(true, url, updateBtn)
            }
            R.id.cancelBtn -> {
                dismiss()
            }
        }
    }
    interface UpdateListener{
        fun update(update: Boolean, url: String, btn: ProgressButton)
    }
}
