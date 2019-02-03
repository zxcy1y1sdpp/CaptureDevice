package app.jietuqi.cn.wechat.simulator.widget

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialogFragment
import android.support.design.widget.CoordinatorLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.entity.WechatBankEntity
import app.jietuqi.cn.wechat.simulator.adapter.WechatSimulatorChoiceBankAdapter
import com.bm.zlzq.utils.ScreenUtil
import kotlinx.android.synthetic.main.dialog_wechat_simulator_select_bank.*


/**
 * 作者： liuyuanbo on 2018/10/23 14:29.
 * 时间： 2018/10/23 14:29
 * 邮箱： 972383753@qq.com
 * 用途： 选择银行卡的底部弹框
 */

class WechatSimulatorChoiceBankDialog : BottomSheetDialogFragment(), WechatSimulatorChoiceBankAdapter.ChoiceListener{
    private var mAdapter: WechatSimulatorChoiceBankAdapter? = null
    private var mItemSelectionListener: OnItemSelectListener? = null
    private var mType: Int = 0
    private var mSingleHeight = 0//每一条数据的高度
    private var mTotalHeight = 0//总高度
    private var mPosition = 0
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val bottomSheet = dialog.findViewById<View>(R.id.design_bottom_sheet)
            bottomSheet.layoutParams.height = mTotalHeight //可以写入自己想要的高度
//            bottomSheet.layoutParams.height = activity?.let { ScreenUtil.getScreenHeight(it) }?.div(2) ?: 0 //可以写入自己想要的高度
        }
        view?.post {
            val parent = view?.parent as View
            val params = parent.layoutParams as CoordinatorLayout.LayoutParams
            val behavior = params.behavior
            val bottomSheetBehavior = behavior as BottomSheetBehavior<*>
            bottomSheetBehavior.isHideable = false
            view?.measuredHeight?.let { bottomSheetBehavior.peekHeight = it }
            parent.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun choice(bankEntity: WechatBankEntity?, position: Int) {
        if (null != bankEntity){
            mItemSelectionListener?.select(bankEntity, position)
        }
        dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_wechat_simulator_select_bank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = UserOperateUtil.getWechatSimulatorBank()
        val screenHeight = activity?.let { ScreenUtil.getScreenHeight(it) }
        val screenWidth = activity?.let { ScreenUtil.getScreenWidth(it) }
        if (null != screenWidth){
            mSingleHeight = (screenWidth * 0.165).toInt()
        }
        if (mType == 1){
            dialogReachTimeTv.visibility = View.VISIBLE
            if (null != screenWidth){
                mTotalHeight = list.size * mSingleHeight + mSingleHeight + (screenWidth.times(0.2258)).toInt()
            }
            if (null != screenHeight){
                if (mTotalHeight > screenHeight / 2){
                    mTotalHeight = screenHeight / 2
                }
            }
        }else{
            mTotalHeight = list.size * mSingleHeight + mSingleHeight * 2
            if (null != screenHeight){
                if (mTotalHeight > screenHeight / 2){
                    mTotalHeight = screenHeight / 2
                }
            }
        }

        list[mPosition].isCheck = true
        mAdapter = WechatSimulatorChoiceBankAdapter(list, this, mType)
        dialogRv.adapter = mAdapter
    }

    fun setData(listener: OnItemSelectListener, type: Int, position: Int){
        mItemSelectionListener = listener
        mType = type
        mPosition = position
    }
    interface OnItemSelectListener{
        fun select(entity: WechatBankEntity, position: Int)
    }
}
