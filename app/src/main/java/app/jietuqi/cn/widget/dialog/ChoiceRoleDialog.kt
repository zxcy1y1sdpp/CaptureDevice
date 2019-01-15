package app.jietuqi.cn.widget.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.widget.ShapeCornerBgView


/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 选择角色或者编辑角色
 */

class ChoiceRoleDialog : BottomSheetDialogFragment(), View.OnClickListener{
    private var mRequestCode = -1
    private lateinit var mUserEntity: WechatUserEntity
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.dialogChangeRoleView ->{
                LaunchUtil.startForResultRoleOfLibraryActivity(activity, mRequestCode)
            }
            R.id.dialogEditRoleView ->{
                LaunchUtil.startForResultWechatCreateEditRoleActivity(activity, mUserEntity, mRequestCode)
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
        val view = View.inflate(context, R.layout.dialog_choice_role, null)
        dialog.setContentView(view)
        view.findViewById<ShapeCornerBgView>(R.id.dialogChangeRoleView).setOnClickListener(this)
        view.findViewById<ShapeCornerBgView>(R.id.dialogEditRoleView).setOnClickListener(this)
        view.findViewById<ShapeCornerBgView>(R.id.dialogCancelView).setOnClickListener(this)
        return dialog
    }
    open fun setRequestCode(requestCode: Int, entity: WechatUserEntity){
        mRequestCode = requestCode
        mUserEntity = entity
    }
}
