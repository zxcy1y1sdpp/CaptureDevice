package app.jietuqi.cn.wechat.simulator.widget

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import kotlinx.android.synthetic.main.dialog_sheet_wechat_simulator_bottom_funs.*


/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 微信模拟器中预览的底部功能按钮
 */

class WechatSimulatorBottomFunsDialog : BottomSheetDialogFragment(), View.OnClickListener{
    private lateinit var mListener: FunsChoiceListener
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.dialogWechatSimulatorPreviewBottomTimeTv ->{
                mListener.choice("时间", 2)
            }
            R.id.dialogSimulatorPreviewBottomPictureTv ->{
                mListener.choice("图片", 1)
            }
            R.id.dialogSimulatorPreviewBottomVoiceTv ->{
                mListener.choice("语音", 5)
            }
            R.id.dialogSimulatorPreviewBottomRedpacketTv ->{
                mListener.choice("红包", 3)
            }
            R.id.dialogSimulatorPreviewBottomTransferTv ->{
                mListener.choice("转账", 4)
            }
            R.id.dialogSimulatorPreviewBottomSystemTv ->{
                mListener.choice("系统提示", 6)
            }
        }
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_sheet_wechat_simulator_bottom_funs, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogWechatSimulatorPreviewBottomTimeTv.setOnClickListener(this)//时间
        dialogSimulatorPreviewBottomPictureTv.setOnClickListener(this)//图片
        dialogSimulatorPreviewBottomVoiceTv.setOnClickListener(this)//声音
        dialogSimulatorPreviewBottomRedpacketTv.setOnClickListener(this)//红包
        dialogSimulatorPreviewBottomTransferTv.setOnClickListener(this)//转账
        dialogSimulatorPreviewBottomSystemTv.setOnClickListener(this)//系统
        dialogChoiceTalkCancelTv.setOnClickListener(this)//取消
    }
    fun setListener(listener: FunsChoiceListener){
        mListener = listener
    }
    interface FunsChoiceListener{
        fun choice(title: String, funType: Int)
    }
}
