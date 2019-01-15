package app.jietuqi.cn.wechat.simulator.widget

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.LaunchUtil
import kotlinx.android.synthetic.main.dialog_sheet_wechat_simulator.*


/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 微信模拟器中的长按选项
 */

class WechatSimulatorDialog : BottomSheetDialogFragment(), View.OnClickListener{
    private var mRequestCode = -1
    private var mType = 0
    private lateinit var mUserEntity: WechatUserEntity
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.dialogPlaceTopTv ->{
                LaunchUtil.startForResultRoleOfLibraryActivity(activity, mRequestCode)
            }
            R.id.dialogTagUnreadTv ->{
                LaunchUtil.startForResultWechatCreateEditRoleActivity(activity, mUserEntity, mRequestCode)
            }
            R.id.dialogRedPointTv ->{
                LaunchUtil.startForResultWechatCreateEditRoleActivity(activity, mUserEntity, mRequestCode)
            }
            R.id.dialogDeleteTv ->{
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
        val view = View.inflate(context, R.layout.dialog_sheet_wechat_simulator, null)
        dialog.setContentView(view)

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogPlaceTopTv.setOnClickListener(this)//置顶
        dialogTagUnreadTv.setOnClickListener(this)//设置未读
        dialogRedPointTv.setOnClickListener(this)//圆点操作--有就隐藏，没有就显示
        dialogDeleteTv.setOnClickListener(this)//删除操作
        dialogCancelView.setOnClickListener(this)//删除操作
    }
    open fun setType(type: Int){
        mType = type
    }
}
