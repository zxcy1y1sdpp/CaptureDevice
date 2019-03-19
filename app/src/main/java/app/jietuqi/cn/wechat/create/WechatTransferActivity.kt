package app.jietuqi.cn.wechat.create

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.TimeUtil
import app.jietuqi.cn.widget.dialog.ChangeWechatTransferDialog
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
    val mEntity: WechatScreenShotEntity = WechatScreenShotEntity()
    private var mShowLqt = true
    override fun click(type: String) {
        mEntity.transferType = type
        if ("已收钱" == mEntity.transferType){
            if (mEntity.type == 0){
                mLqtLayout.visibility = View.VISIBLE
            }else{
                mLqtLayout.visibility = View.GONE
            }
        }else{
            mLqtLayout.visibility = View.GONE
        }
        mStatusTv.text = type
    }

    override fun setLayoutResourceId() = R.layout.activity_wechat_transfer

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        val nowTime = TimeUtil.getNowTime()
        mEntity.transferType = "已收钱"
        mEntity.outTime = nowTime
        mEntity.receiveTime = nowTime
        setWechatViewTitle("微信转账", 0)
        registerEventBus()
        onlyOneEditTextNeedTextWatcher(mMoneyEt, this)
        mNickNameEt.setText(RandomUtil.getRandomNickName())
        mTransferOutTimeTv.text = nowTime
        mReceiveTimeTv.text = nowTime
        onlyOneEditTextNeedTextWatcher(mMoneyEt, this)
    }

    override fun initViewsListener() {
        mReciveMoneyTv.setOnClickListener(this)
        mSendMoneyTv.setOnClickListener(this)
        mStatusLayout.setOnClickListener(this)
        mTransferOutTimeLayout.setOnClickListener(this)
        mReceiveTimeLayout.setOnClickListener(this)
        mRefreshIv.setOnClickListener(this)
        mWechatShowLqt.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mReciveMoneyTv ->{
                mNickNameLayout.visibility = View.GONE
                OtherUtil.changeWechatTwoBtnBg(this, mReciveMoneyTv, mSendMoneyTv)
                mEntity.type = 0
                if ("已收钱" == mEntity.transferType){
                    mLqtLayout.visibility = View.VISIBLE
                }
            }
            R.id.mSendMoneyTv ->{
                mLqtLayout.visibility = View.GONE
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
            R.id.mWechatShowLqt ->{
                mShowLqt = !mShowLqt
                if (mShowLqt){
                    mWechatPercentEt.visibility = View.VISIBLE
                }else{
                    mWechatPercentEt.visibility = View.GONE
                }
                OtherUtil.onOrOff(mShowLqt, mWechatShowLqt)
            }
            R.id.previewBtn ->{
                mEntity.wechatUserNickName = mNickNameEt.text.toString()
                mEntity.money = mMoneyEt.text.toString()
                LaunchUtil.startWechatScreenShotTransferDetailActivity(this, mEntity, OtherUtil.getContent(mWechatPercentEt), mShowLqt)
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        if (timeEntity.tag == "转账时间"){
            mTransferOutTimeTv.text = timeEntity.timeWithoutS
            mEntity.outTime = timeEntity.timeWithoutS
        }else{
            mReceiveTimeTv.text = timeEntity.timeWithoutS
            mEntity.receiveTime = timeEntity.timeWithoutS
        }
    }
}
