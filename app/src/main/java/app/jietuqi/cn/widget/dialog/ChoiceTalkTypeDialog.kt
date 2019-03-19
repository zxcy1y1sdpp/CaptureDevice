package app.jietuqi.cn.widget.dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import kotlinx.android.synthetic.main.dialog_choice_talk_type.*

/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 选择聊天截图的类型
 */
class ChoiceTalkTypeDialog : BottomSheetDialogFragment(), View.OnClickListener{
    private lateinit var mListener: ChoiceTypeListener
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.dialogChoiceTalkTimeTv ->{
                mListener.choiceType("时间", "2")
            }
            R.id.dialogChoiceTalkTextTv ->{
                mListener.choiceType("文本", "0")
            }
            R.id.dialogChoiceTalkPictureAndVideoTv ->{
                mListener.choiceType("图片和视频", "1")
            }
            R.id.dialogChoiceTalkRedPacketTv ->{
                mListener.choiceType("红包", "3")
            }
            R.id.dialogChoiceTalkTransferTv ->{
                mListener.choiceType("转账", "5")
            }
            R.id.dialogChoiceVoiceTv ->{
                mListener.choiceType("语音", "7")
            }
            R.id.dialogChoiceTalkSystemTv ->{
                mListener.choiceType("系统提示", "7")
            }
            R.id.dialogChoiceTalkVideoTv ->{
                mListener.choiceType("视频和语音聊天", "9")
            }
            R.id.dialogChoiceTalkTransmitTv ->{
                mListener.choiceType("转发", "11")
            }
            R.id.dialogChoiceTalkCardTv ->{
                mListener.choiceType("个人名片", "12")
            }
            R.id.dialogChoiceTalkInviteTv ->{
                mListener.choiceType("加群", "13")
            }
            R.id.dialogChoiceTalkFileTv ->{
                mListener.choiceType("文件", "14")
            }
        }
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_choice_talk_type, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogChoiceTalkTimeTv.setOnClickListener(this)
        dialogChoiceTalkTextTv.setOnClickListener(this)
        dialogChoiceTalkPictureAndVideoTv.setOnClickListener(this)
        dialogChoiceTalkRedPacketTv.setOnClickListener(this)
        dialogChoiceTalkTransferTv.setOnClickListener(this)
        dialogChoiceVoiceTv.setOnClickListener(this)
        dialogChoiceTalkCancelTv.setOnClickListener(this)
        dialogChoiceTalkSystemTv.setOnClickListener(this)
        dialogChoiceTalkVideoTv.setOnClickListener(this)
        dialogChoiceTalkTransmitTv.setOnClickListener(this)
        dialogChoiceTalkCardTv.setOnClickListener(this)
        dialogChoiceTalkInviteTv.setOnClickListener(this)
        dialogChoiceTalkFileTv.setOnClickListener(this)
    }

    fun setListener(listener: ChoiceTypeListener){
        mListener = listener
    }

    interface ChoiceTypeListener{
        fun choiceType(title: String, msgType: String)
    }
}
