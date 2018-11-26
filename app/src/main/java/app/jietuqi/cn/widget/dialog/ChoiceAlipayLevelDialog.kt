package app.jietuqi.cn.widget.dialog

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import app.jietuqi.cn.alipay.entity.AlipayVipLevelEntity
import kotlinx.android.synthetic.main.dialog_alipay_choice_level.*

/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 选择支付宝会员等级的底部弹框
 */

class ChoiceAlipayLevelDialog : BottomSheetDialogFragment(), View.OnClickListener{
    override fun onClick(v: View?) {
        var entity: AlipayVipLevelEntity = AlipayVipLevelEntity("大众会员", R.drawable.alipay_vip_level1)
        when(v?.id){
            R.id.level0 ->{ entity = AlipayVipLevelEntity("无", 0) }
            R.id.level1 ->{ entity = AlipayVipLevelEntity("大众会员", R.drawable.alipay_vip_level1)}
            R.id.level2 ->{ entity = AlipayVipLevelEntity("黄金会员", R.drawable.alipay_vip_level2)}
            R.id.level3 ->{ entity = AlipayVipLevelEntity("铂金会员", R.drawable.alipay_vip_level3)}
            R.id.level4 ->{ entity = AlipayVipLevelEntity("钻石会员", R.drawable.alipay_vip_level4)}
        }
        mItemSelectionListener?.select(entity)
        dismiss()
    }

    private var mItemSelectionListener: OnItemSelectListener? = null

    fun setOnItemSelectListener(itemSelection: OnItemSelectListener){
        mItemSelectionListener = itemSelection
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_alipay_choice_level, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        level0.setOnClickListener(this)
        level1.setOnClickListener(this)
        level2.setOnClickListener(this)
        level3.setOnClickListener(this)
        level4.setOnClickListener(this)
        levelCancel.setOnClickListener(this)
    }

    interface OnItemSelectListener{
        fun select(entity: AlipayVipLevelEntity)
    }
}
