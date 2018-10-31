package dasheng.com.capturedevice.wechat.ui.activity

import android.view.View
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseWechatActivity
import dasheng.com.capturedevice.constant.RandomUtil
import dasheng.com.capturedevice.entity.WechatTransferEntity
import dasheng.com.capturedevice.entity.eventbusentity.EventBusTimeEntity
import dasheng.com.capturedevice.util.EventBusUtil
import dasheng.com.capturedevice.util.LaunchUtil
import dasheng.com.capturedevice.util.OtherUtil
import dasheng.com.capturedevice.util.TimeUtil
import dasheng.com.capturedevice.widget.dialog.ChangeWechatTransferDialog
import kotlinx.android.synthetic.main.activity_wechat_transfer.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/10/19 15:53.
 * 时间： 2018/10/19 15:53
 * 邮箱： 972383753@qq.com
 * 用途： 微信转账页面
 */

class WechatTransferActivity : BaseWechatActivity(), ChangeWechatTransferDialog.OnItemSelectListener {
    internal val mEntity: WechatTransferEntity = WechatTransferEntity()
    override fun click(type: String) {
        mEntity.transferType = type
        mStatusTv.text = type
    }

    override fun setLayoutResourceId() = R.layout.activity_wechat_transfer

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        val notTime = TimeUtil.getNowTime()
        mEntity.transferType = "已收钱"
        mEntity.outTime = notTime
        mEntity.receiveTime = notTime
        setWechatViewTitle("微信转账", 0)
        registerEventBus()
        onlyOneEditTextNeedTextWatcher(mMoneyEt, this)
        mNickNameEt.setText(RandomUtil.getRandomNickName())
        mTransferOutTimeTv.text = notTime
        mReceiveTimeTv.text = notTime
        onlyOneEditTextNeedTextWatcher(mMoneyEt, this)
    }

    override fun initViewsListener() {
        mReciveMoneyTv.setOnClickListener(this)
        mSendMoneyTv.setOnClickListener(this)
        mStatusLayout.setOnClickListener(this)
        mTransferOutTimeLayout.setOnClickListener(this)
        mReceiveTimeLayout.setOnClickListener(this)
        mRefreshIv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mReciveMoneyTv ->{
                mNickNameLayout.visibility = View.GONE
                OtherUtil.changeWechatTwoBtnBg(this, mReciveMoneyTv, mSendMoneyTv)
                mEntity.type = 0
            }
            R.id.mSendMoneyTv ->{
                mEntity.type = 1
                mNickNameLayout.visibility = View.VISIBLE
                OtherUtil.changeWechatTwoBtnBg(this, mSendMoneyTv, mReciveMoneyTv)
            }
            R.id.mStatusLayout ->{
                var dialog = ChangeWechatTransferDialog()
                dialog.setOnItemSelectListener(this)
                dialog.show(supportFragmentManager, "Dialog")
            }
            R.id.mTransferOutTimeLayout ->{
                initTimePickerView("转账时间", 1)
            }
            R.id.mReceiveTimeLayout ->{
                initTimePickerView("收钱时间", 1)
            }
            R.id.mRefreshIv ->{
                mNickNameEt.setText(RandomUtil.getRandomNickName())
            }
            R.id.wechatPreviewBtn ->{
                mEntity.wechatUserNickName = mNickNameEt.text.toString()
                mEntity.money = mMoneyEt.text.toString()
                LaunchUtil.startWechatTransferDetailActivity(this, mEntity)
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelecTimeEvent(timeEntity: EventBusTimeEntity) {
        if (timeEntity.tag == "转账时间"){
            mTransferOutTimeTv.text = timeEntity.timeWithoutS
            mEntity.outTime = timeEntity.timeWithoutS
        }else{
            mReceiveTimeTv.text = timeEntity.timeWithoutS
            mEntity.receiveTime = timeEntity.timeWithoutS
        }
    }
    override fun onDestroy() {
        EventBusUtil.unRegister(this)
        super.onDestroy()
    }
}
