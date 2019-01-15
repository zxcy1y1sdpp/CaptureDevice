package app.jietuqi.cn.ui.qqscreenshot.ui.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.qq.BaseQQScreenShotCreateActivity
import kotlinx.android.synthetic.main.activity_qq_create_transfer.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*

/**
 * 作者： liuyuanbo on 2018/12/6 17:29.
 * 时间： 2018/12/6 17:29
 * 邮箱： 972383753@qq.com
 * 用途： QQ转账截图
 */
class QQCreateTransferActivity : BaseQQScreenShotCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_qq_create_transfer

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
        if (mType == 1){
            if (TextUtils.isEmpty(mMsgEntity.msg) || mMsgEntity.msg.startsWith("QQ转账")){
                mMsgEntity.msg = ""
                mQQCreateTransferMsgEt.setText("")
            }else{
                mQQCreateTransferMsgEt.setText(mMsgEntity.msg)
            }
            mQQCreateTransferMoneyEt.setText(if (mMsgEntity.money.toFloat() > 0) mMsgEntity.money else "")
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.overallAllRightWithBgTv ->{
                var money = mQQCreateTransferMoneyEt.text.toString()
                if (money.isNotBlank()){
                    mMsgEntity.money = mQQCreateTransferMoneyEt.text.toString()
                }
                val msg = mQQCreateTransferMsgEt.text.toString().trim()
                if (msg.isNotBlank()){
                    mMsgEntity.msg = msg
                }else{
                    val msg = mQQCreateTransferMsgEt.text.toString().trim()
                    if (TextUtils.isEmpty(msg)){
                        mMsgEntity.msg = ""
                        mQQCreateTransferMsgEt.setText("")
                    }
                }
                if (mType == 1){//修改
                    val receiveEntity = mHelper.query(mMsgEntity.id)//与被修改的数据关联在一起的收钱的数据
                    if (null != receiveEntity){
                        receiveEntity.transferReceiveTime = mMsgEntity.transferReceiveTime
                        receiveEntity.transferOutTime = mMsgEntity.transferOutTime
                        receiveEntity.money = mMsgEntity.money
                        mHelper.update(receiveEntity)
                    }
                }
            }
        }
        super.onClick(v)
    }
}