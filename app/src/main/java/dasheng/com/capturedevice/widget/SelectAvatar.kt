package dasheng.com.capturedevice.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout

import dasheng.com.capturedevice.R
import kotlinx.android.synthetic.main.dialog_photo.view.*

/**
 * 作者： liuyuanbo on 2018/10/15 12:45.
 * 时间： 2018/10/15 12:45
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class SelectAvatar : LinearLayout {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.dialog_photo, this, true)
    }

}
