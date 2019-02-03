package app.jietuqi.cn.wechat.simulator.widget

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.widget.dialog.EditDialog
import kotlinx.android.synthetic.main.dialog_sheet_wechat_simulator.*


/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 微信模拟器中的长按选项
 */

class WechatSimulatorDialog : BottomSheetDialogFragment(), View.OnClickListener, EditDialogChoiceListener {
    override fun onChoice(entity: EditDialogEntity?) {
        mEntity.unReadNum = entity?.content
        mHelper.update(mEntity)
        if (mEntity.unReadNum.toInt() >= 0){
            mListener.operate("标为未读", mEntity)
        }else{
            mListener.operate("标为已读", mEntity)
        }

    }

    private lateinit var mListener: OperateListener
    private lateinit var mEntity: WechatUserEntity
    private lateinit var mHelper: WechatSimulatorListHelper
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.dialogPlaceTopTv ->{
                mEntity.top = !mEntity.top
                mHelper.update(mEntity)
                mListener.operate(dialogPlaceTopTv.text.toString(), mEntity)
            }
            R.id.dialogTagUnreadTv ->{
                if ("标为已读" == dialogTagUnreadTv.text.toString()){
                    mEntity.unReadNum = "0"
                    mHelper.update(mEntity)
                    mListener.operate("标为已读", mEntity)
                }else{
                    val dialogEdit = EditDialog()
                    dialogEdit.setData(this, EditDialogEntity(0, "", "填写未读消息数量", true))
                    dialogEdit.show(activity?.supportFragmentManager, "unReadNum")
                }

            }
            R.id.dialogRedPointTv ->{
                mEntity.showPoint = !mEntity.showPoint
                mListener.operate(dialogRedPointTv.text.toString(), mEntity)
                mHelper.update(mEntity)
            }
            R.id.dialogDeleteTv ->{
                mHelper.delete(mEntity)
                mListener.operate("删除", mEntity)
            }
        }
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sheet_wechat_simulator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogPlaceTopTv.setOnClickListener(this)//置顶
        dialogTagUnreadTv.setOnClickListener(this)//设置未读
        dialogRedPointTv.setOnClickListener(this)//圆点操作--有就隐藏，没有就显示
        dialogDeleteTv.setOnClickListener(this)//删除操作
        dialogCancelView.setOnClickListener(this)//删除操作
        if (mEntity.top){
            dialogPlaceTopTv.text = "取消置顶"
        }
        if (mEntity.unReadNum.toInt() > 0){
            dialogTagUnreadTv.text = "标为已读"
        }
        if (mEntity.showPoint){
            dialogRedPointTv.text = "隐藏小圆点"
        }
    }

    fun setOperateListener(listener: OperateListener){
        mListener = listener
    }
    fun setData(entity: WechatUserEntity, helper: WechatSimulatorListHelper){
        mEntity = entity
        mHelper = helper
    }

    interface OperateListener{
        fun operate(type: String, userEntity: WechatUserEntity)
    }
}
