package app.jietuqi.cn.alipay.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.alipay.entity.AlipayCreateTransferBillEntity
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.widget.dialog.EditDialog
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.activity_alipay_create_transfer_bill.*
import kotlinx.android.synthetic.main.include_wechat_preview_btn.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/11/1 14:25.
 * 时间： 2018/11/1 14:25
 * 邮箱： 972383753@qq.com
 * 用途： 支付宝 -- 生成 -- 转账账单页面/收款账单页面
 */

class AlipayCreateTransferBillActivity : BaseCreateActivity(), EditDialogChoiceListener {
    /**
     * 0 -- 转账账单
     * 1 -- 收款账单
     */
    private var mType = 0
    private var mEntity: AlipayCreateTransferBillEntity = AlipayCreateTransferBillEntity()
    override fun setLayoutResourceId() = R.layout.activity_alipay_create_transfer_bill

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        val userEntity = RoleLibraryHelper(this).queryRandomItem(1)[0]
        mEntity.avatarFile = userEntity.avatarFile
//        mEntity.avatarInt = userEntity.avatarInt
        mEntity.resourceName = userEntity.resourceName
        mEntity.wechatUserNickName = userEntity.wechatUserNickName
        mAlipayCreateTransferBillNickNameTv.text = userEntity.wechatUserNickName
        GlideUtil.displayHead(this, userEntity.getAvatarFile(), mAlipayCreateTransferBillAvatarIv)
        registerEventBus()
        onlyTowEditTextNeedTextWatcher(mAlipayCreateTransferBillAccountEt, mAlipayCreateTransferBillMoneyEt)
        mAlipayCreateTransferBillNumEt.setText(RandomUtil.getRandomNum(20))
    }

    override fun initViewsListener() {
        mAlipayCreateTransferBillSenderLayout.setOnClickListener(this)
        mAlipayCreateTransferBillReceiveTv.setOnClickListener(this)
        mAlipayCreateTransferBillOutTv.setOnClickListener(this)
        mAlipayCreateTransferBillRefreshIv.setOnClickListener(this)
        mAlipayCreateTransferBillNumRefreshIv.setOnClickListener(this)
        mAlipayCreateTransferBillPaymentMethodsLayout.setOnClickListener(this)
        mAlipayCreateTransferBillCreateTimeLayout.setOnClickListener(this)
        mAlipayCreateTransferBillClassifyLayout.setOnClickListener(this)
        mAlipayCreateTransferBillTransferTypeLayout.setOnClickListener(this)
        mAlipayCreateTransferBillContactRecordIv.setOnClickListener(this)
        previewBtn.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        if(mType == 0){
            setCreateTitle("转账账单", 0)
            mEntity.showContactRecord = true

            mEntity.msg = "转账"
        }else{
            setCreateTitle("收款账单", 0)
            mAlipayCreateTransferBillReceiveOrOutLayout.visibility = View.GONE
            mAlipayCreateTransferBillContactRecordLayout.visibility = View.VISIBLE
            mEntity.msg = "个人收款"
            mEntity.transferStatus = "交易成功"
            mEntity.type = 0
            mAlipayCreateTransferBillMsgEt.hint = "收款理由"
            mEntity.showContactRecord = false
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAlipayCreateTransferBillSenderLayout ->{
                operateRole(mEntity, type = 1)
            }
            R.id.mAlipayCreateTransferBillReceiveTv ->{
                mAlipayCreateTransferBillPaymentMethodsTitleTv.text = "收款方式"
                mEntity.type = 0
                OtherUtil.changeWechatTwoBtnBg(this, mAlipayCreateTransferBillReceiveTv, mAlipayCreateTransferBillOutTv)
            }
            R.id.mAlipayCreateTransferBillOutTv ->{
                mAlipayCreateTransferBillPaymentMethodsTitleTv.text = "付款方式"
                mEntity.type = 1
                OtherUtil.changeWechatTwoBtnBg(this, mAlipayCreateTransferBillOutTv, mAlipayCreateTransferBillReceiveTv)
            }
            R.id.mAlipayCreateTransferBillRefreshIv ->{
                mAlipayCreateTransferBillAccountEt.setText(RandomUtil.getRandomAccounts())
            }
            R.id.mAlipayCreateTransferBillNumRefreshIv ->{
                mAlipayCreateTransferBillNumEt.setText(RandomUtil.getRandomNum(20))
            }
            R.id.mAlipayCreateTransferBillPaymentMethodsLayout ->{//收款方式
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(0, "余额", "收款方式"))
                dialog.show(supportFragmentManager, "payment")
            }
            R.id.mAlipayCreateTransferBillCreateTimeLayout ->{//创建时间
                initTimePickerView()
            }
            R.id.mAlipayCreateTransferBillClassifyLayout ->{//账单分类
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(1, "其他", "账单分类"))
                dialog.show(supportFragmentManager, "classify")
            }
            R.id.mAlipayCreateTransferBillTransferTypeLayout ->{//交易状态
                var status = mAlipayCreateTransferBillTransferTypeTv.text.toString()
                if (mType == 0){//转账账单
                    if (status == "交易成功"){
                        mAlipayCreateTransferBillTransferTypeTv.text = "处理中"
                    }else{
                        mAlipayCreateTransferBillTransferTypeTv.text = "交易成功"
                    }
                }else{//收款账单
                    if (status == "交易成功"){
                        mAlipayCreateTransferBillTransferTypeTv.text = "等待对方付款"
                        mAlipayCreateTransferBillPaymentMethodsLayout.visibility = View.GONE
                    }else{
                        mAlipayCreateTransferBillTransferTypeTv.text = "交易成功"
                        mAlipayCreateTransferBillPaymentMethodsLayout.visibility = View.VISIBLE
                    }
                }
            }
            R.id.mAlipayCreateTransferBillContactRecordIv ->{
                mEntity.showContactRecord = !mEntity.showContactRecord
                OtherUtil.onOrOff(mEntity.showContactRecord, mAlipayCreateTransferBillContactRecordIv)
            }
            R.id.previewBtn ->{
                mEntity.account = mAlipayCreateTransferBillAccountEt.text.toString()
                mEntity.money = mAlipayCreateTransferBillMoneyEt.text.toString()
                if (!TextUtils.isEmpty(mAlipayCreateTransferBillMsgEt.text.toString())){
                    mEntity.msg = mAlipayCreateTransferBillMsgEt.text.toString()
                }
                mEntity.num = mAlipayCreateTransferBillNumEt.text.toString()
                mEntity.paymentMethods = mAlipayCreateTransferBillPaymentMethodsTv.text.toString()
                mEntity.createTime = mAlipayCreateTransferBillCreateTimeTv.text.toString()
                mEntity.billClassify = mAlipayCreateTransferBillClassifyTv.text.toString()
                mEntity.transferStatus = mAlipayCreateTransferBillTransferTypeTv.text.toString()
                LaunchUtil.startAlipayPreviewTransferBillActivity(this, mEntity, mType)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.MY_SIDE ->{
                if (data?.extras?.containsKey(IntentKey.ENTITY) == true){
                    val userEntity = data.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity
                    mEntity.avatarFile = userEntity.avatarFile
                    mEntity.avatarInt = userEntity.avatarInt
                    mEntity.resourceName = userEntity.resourceName
                    mEntity.wechatUserNickName = userEntity.wechatUserNickName
                    mAlipayCreateTransferBillNickNameTv.text = mEntity.wechatUserNickName
                    GlideUtil.displayHead(this, mEntity.getAvatarFile(), mAlipayCreateTransferBillAvatarIv)
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        mAlipayCreateTransferBillCreateTimeTv.text = timeEntity.time
        mEntity.createTime = timeEntity.time
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(entity: EditDialogEntity) {
        if (entity.position == 0){
            mAlipayCreateTransferBillPaymentMethodsTv.text = entity.content
            mEntity.paymentMethods = entity.content
        }else{
            mAlipayCreateTransferBillClassifyTv.text = entity.content
            mEntity.billClassify = entity.content
        }
    }
    override fun onChoice(entity: EditDialogEntity) {
        if (entity.position == 0){
            mAlipayCreateTransferBillPaymentMethodsTv.text = entity.content
            mEntity.paymentMethods = entity.content
        }else{
            mAlipayCreateTransferBillClassifyTv.text = entity.content
            mEntity.billClassify = entity.content
        }
    }
    override fun onDestroy() {
        EventBusUtil.unRegister(this)
        super.onDestroy()
    }
}
