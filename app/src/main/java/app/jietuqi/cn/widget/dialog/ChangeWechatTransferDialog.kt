package app.jietuqi.cn.widget.dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import kotlinx.android.synthetic.main.dialog_change_wechat_transfer.*

/**
 * 作者： liuyuanbo on 2018/10/15 13:40.
 * 时间： 2018/10/15 13:40
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class ChangeWechatTransferDialog: BottomSheetDialogFragment(), View.OnClickListener {
    private var mItemSelectionListener: OnItemSelectListener? = null
    private val mTitle = arrayOfNulls<String>(3)
//    val mTitle = arrayOf<String>()
    /**
     * 0 -- 微信转账状态
     * 1 -- 微信截图的语音\视频的通话状态
     */
    private var mType = 0
    override fun onClick(v: View) {
        mItemSelectionListener?.click(v.tag.toString())
        dismiss()
    }

    fun setOnItemSelectListener(itemSelection: OnItemSelectListener){
        mItemSelectionListener = itemSelection
    }
    fun setType(type: Int){
        mType = type
    }

    fun setTitle(vararg title: String) {
        mTitle[0] = title[0]
        mTitle[1] = title[1]
        mTitle[2] = title[2]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_change_wechat_transfer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogType1.setOnClickListener(this)
        dialogType2.setOnClickListener(this)
        dialogType3.setOnClickListener(this)
        dialogCancel.setOnClickListener(this)
        dialogType1.text = mTitle[0]
        dialogType1.tag = mTitle[0]
        dialogType2.text = mTitle[1]
        dialogType2.tag = mTitle[1]
        dialogType3.text = mTitle[2]
        dialogType3.tag = mTitle[2]
        /*if (mType == 1){
            dialogType1.text = "已接通"
            dialogType1.tag = "已接通"
            dialogType2.text = "已取消"
            dialogType2.tag = "已取消"
            dialogType3.text = "已拒绝"
            dialogType3.tag = "已拒绝"
        }*/
    }

    interface OnItemSelectListener{
        fun click(type: String)
    }
}
