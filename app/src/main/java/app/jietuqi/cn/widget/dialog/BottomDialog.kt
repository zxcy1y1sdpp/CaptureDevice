package app.jietuqi.cn.widget.dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R

import kotlinx.android.synthetic.main.dialog_photo.*

/**
 * 作者： liuyuanbo on 2018/10/15 13:40.
 * 时间： 2018/10/15 13:40
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class BottomDialog : BottomSheetDialogFragment(), View.OnClickListener {
    private var mItemSelectionListener: ItemSelection? = null
    override fun onClick(v: View) {
        when(v.id){
            R.id.firstItem ->{
                mItemSelectionListener?.firstClick()
            }
            R.id.secondItem ->{
                mItemSelectionListener?.secondClick()
            }
            R.id.cancelItem ->{
            }
        }
        dismiss()
    }

    fun setItemSelectionListener(itemSelection: ItemSelection){
        mItemSelectionListener = itemSelection
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstItem.setOnClickListener(this)
        secondItem.setOnClickListener(this)
        cancelItem.setOnClickListener(this)
    }

    interface ItemSelection{
        fun firstClick()
        fun secondClick()
    }
}
