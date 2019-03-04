package app.jietuqi.cn.widget.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.widget.ShapeCornerBgView


/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 选择头像
 */

class ChoiceGroupHeaderDialog : BottomSheetDialogFragment(), View.OnClickListener{
    private var mRequestCode = -1
    private var mNeedChangeShared = true
    private lateinit var mListener: OperateListener
    /**
     * 是否需要同时操作数据库
     */
    private var mNeedOperateDb = false
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.dialogChangeRoleView ->{
//                LaunchUtil.startForResultRoleOfLibraryActivity(activity, mRequestCode, mNeedChangeShared)
                mListener.operate(0)
            }
            R.id.dialogAlbumView ->{
                mListener.operate(1)
            }
        }
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val view = View.inflate(context, R.layout.dialog_choice_group_header, null)
        dialog.setContentView(view)
        view.findViewById<ShapeCornerBgView>(R.id.dialogChangeRoleView).setOnClickListener(this)
        view.findViewById<ShapeCornerBgView>(R.id.dialogAlbumView).setOnClickListener(this)
        view.findViewById<ShapeCornerBgView>(R.id.dialogCancelView).setOnClickListener(this)
        return dialog
    }
    fun setRequestCode(requestCode: Int, listener: OperateListener, needDb: Boolean){
        mRequestCode = requestCode
        mListener = listener
//        mNeedChangeShared = needChangeSharedPreference
        mNeedOperateDb = needDb
    }
    interface OperateListener{
        fun operate(item: Int)
    }
}
