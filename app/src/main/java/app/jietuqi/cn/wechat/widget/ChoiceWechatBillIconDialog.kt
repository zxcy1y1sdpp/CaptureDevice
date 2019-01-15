package app.jietuqi.cn.wechat.widget

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.wechat.entity.WechatCreateBillsEntity
import kotlinx.android.synthetic.main.dialog_choice_wechat_bill_icon.*

/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 选择聊天截图的类型
 */

class ChoiceWechatBillIconDialog : BottomSheetDialogFragment(), View.OnClickListener{
    private lateinit var mListener: ChoiceTypeListener
    override fun onClick(v: View?) {
        var entity = WechatCreateBillsEntity()
        when(v?.id){
            R.id.dialogChoiceIconRedPacketTv ->{
                entity.iconInt = R.drawable.wechat_bill_icon1
                mListener.choiceType(R.drawable.wechat_bill_icon1, "红包")
            }
            R.id.dialogChoiceIconChangeTv ->{
                entity.iconInt = R.drawable.wechat_bill_icon2
                mListener.choiceType(R.drawable.wechat_bill_icon2, "零钱")
            }
            R.id.dialogChoiceIconQRCodeReceiveTv ->{
                entity.iconInt = R.drawable.wechat_bill_icon3
                mListener.choiceType(R.drawable.wechat_bill_icon3, "二维码收款")
            }
            R.id.dialogChoiceIconRandomRoleTv ->{
                entity.iconInt = RandomUtil.getRandomAvatar()
                mListener.choiceType(RandomUtil.getRandomAvatar(), "随机角色")
            }
            R.id.dialogChoiceIconCustomTv ->{
                mListener.choiceType(-1, "相册自定义")
            }
        }
        dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_choice_wechat_bill_icon, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialogChoiceIconRedPacketTv.setOnClickListener(this)
        dialogChoiceIconChangeTv.setOnClickListener(this)
        dialogChoiceIconQRCodeReceiveTv.setOnClickListener(this)
        dialogChoiceIconRandomRoleTv.setOnClickListener(this)
        dialogChoiceIconCustomTv.setOnClickListener(this)
        dialogChoiceIconCancelTv.setOnClickListener(this)
    }

    fun setListener(listener: ChoiceTypeListener){
        mListener = listener
    }

    interface ChoiceTypeListener{
        fun choiceType(icon: Int, type: String)
    }
}
