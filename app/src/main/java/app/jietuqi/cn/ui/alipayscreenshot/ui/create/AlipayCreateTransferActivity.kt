package app.jietuqi.cn.ui.alipayscreenshot.ui.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.alipay.BaseAlipayScreenShotCreateActivity
import app.jietuqi.cn.util.StringUtils
import kotlinx.android.synthetic.main.activity_alipay_create_transfer.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*

/**
 * 作者： liuyuanbo on 2018/12/6 17:29.
 * 时间： 2018/12/6 17:29
 * 邮箱： 972383753@qq.com
 * 用途： 微信转账截图
 */
class AlipayCreateTransferActivity : BaseAlipayScreenShotCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_alipay_create_transfer

    override fun needLoadingView() = false

    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 5
        setBlackTitle("转账", 1)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        overallAllRightWithBgTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (TextUtils.isEmpty(mMsgEntity.msg) || mMsgEntity.msg.startsWith("转账给")){
            mMsgEntity.msg = ""
        }
        mAlipayCreateTransferMoneyEt.setText(if (mMsgEntity.money.toFloat() > 0) mMsgEntity.money else "")
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.overallAllRightWithBgTv ->{
                var money = mAlipayCreateTransferMoneyEt.text.toString()
                if (money.isNotBlank()){
                    mMsgEntity.money = mAlipayCreateTransferMoneyEt.text.toString()
                }
                val msg = mAlipayCreateTransferMsgEt.text.toString().trim()
                if (msg.isNotBlank()){
                    mMsgEntity.msg = msg
                }else{
                    if (mMsgEntity.wechatUserId == mOtherSideEntity.wechatUserId){
                        mMsgEntity.msg = "转账给你"
                    }else{
                        mMsgEntity.msg = StringUtils.insertFront(mOtherSideEntity.wechatUserNickName, "转账给")
                    }
                }
                /*if (mType == 1){//修改
                    val receiveEntity = mHelper.query(mMsgEntity.id)//与被修改的数据关联在一起的收钱的数据
                    if (null != receiveEntity){
                        receiveEntity.transferReceiveTime = mMsgEntity.transferReceiveTime
                        receiveEntity.transferOutTime = mMsgEntity.transferOutTime
                        receiveEntity.money = mMsgEntity.money
                        mHelper.update(receiveEntity)
//123                        EventBusUtil.post(receiveEntity)
                    }
                }*/
            }
        }
        super.onClick(v)
    }
}