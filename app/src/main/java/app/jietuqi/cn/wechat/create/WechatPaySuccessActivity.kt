package app.jietuqi.cn.wechat.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.widget.dialog.ChangeWechatTransferDialog
import kotlinx.android.synthetic.main.activity_wechat_pay_success.*
import kotlinx.android.synthetic.main.include_wechat_preview_btn.*

/**
 * 作者： liuyuanbo on 2019/3/22 12:17.
 * 时间： 2019/3/22 12:17
 * 邮箱： 972383753@qq.com
 * 用途： 支付成功的页面
 */
class WechatPaySuccessActivity : BaseWechatActivity(), ChangeWechatTransferDialog.OnItemSelectListener {
    private var mUserEntity = WechatUserEntity()
    override fun setLayoutResourceId() = R.layout.activity_wechat_pay_success

    override fun needLoadingView() = false

    override fun initAllViews() {
        setWechatViewTitle("转账成功")
        mUserEntity = RoleLibraryHelper(this).queryRandomItem(1)[0]
        mReceiverNickNameTv.text = mUserEntity.wechatUserNickName
        GlideUtil.displayHead(this, mUserEntity.getAvatarFile(), mReceiverAvatarTv)
    }

    override fun initViewsListener() {
        mReceiverLayout.setOnClickListener(this)
        mStatusLayout.setOnClickListener(this)
        previewBtn.setOnClickListener(this)
        onlyTowEditTextNeedTextWatcher(mRealNameEt, mRealMoneyEt, this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mReceiverLayout ->{
                operateRole(mUserEntity, 1)
            }
            R.id.mStatusLayout ->{
                var dialog = ChangeWechatTransferDialog()
                dialog.setOnItemSelectListener(this)
                dialog.setTitle("向个人付款", "向个人转账", "向商户付款")
                dialog.show(supportFragmentManager, "transfer")
            }
            R.id.previewBtn ->{
                when(mStatusTv.text.toString()){
                    "向个人付款" ->{

                    }
                    "向个人转账" ->{
                        val realName = OtherUtil.getContent(mRealNameEt)
                        if (TextUtils.isEmpty(realName)){
                            showQQTipDialog("请输入收款人真实姓名")
                            return
                        }
                        val money = OtherUtil.getContent(mRealMoneyEt)
                        if (TextUtils.isEmpty(money)){
                            showQQTipDialog("请输入具体收款金额")
                            return
                        }
                        mUserEntity.eventBusTag = realName
                        mUserEntity.money = money
                        LaunchUtil.startWechatPreviewPaySuccessActivity(this, mUserEntity)
                    }
                    "向商户付款" ->{

                    }
                }

            }
        }

    }

    override fun click(type: String) {
        super.click(type)
        if ("向个人转账" != type){
            showQQTipDialog("暂未开放", 1)
            return
        }
        mStatusTv.text = type
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.OTHER_SIDE ->{
                if (data?.extras?.containsKey(IntentKey.ENTITY) == true){
                    mUserEntity = mOtherSideEntity
                    mReceiverNickNameTv.text = mUserEntity.wechatUserNickName
                    GlideUtil.displayHead(this, mUserEntity.getAvatarFile(), mReceiverAvatarTv)
                }
            }
        }
    }
}
