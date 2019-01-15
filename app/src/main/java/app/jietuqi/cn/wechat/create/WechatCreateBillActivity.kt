package app.jietuqi.cn.wechat.create

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.TimeUtil
import app.jietuqi.cn.wechat.db.WechatCreateBillsHelper
import app.jietuqi.cn.wechat.entity.WechatCreateBillsEntity
import app.jietuqi.cn.wechat.widget.ChoiceWechatBillIconDialog
import kotlinx.android.synthetic.main.activity_wechat_create_bill.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*

/**
 * 作者： liuyuanbo on 2018/12/24 09:54.
 * 时间： 2018/12/24 09:54
 * 邮箱： 972383753@qq.com
 * 用途：
 */
@RuntimePermissions
class WechatCreateBillActivity : BaseCreateActivity(), ChoiceWechatBillIconDialog.ChoiceTypeListener {
    private var mBillEntity: WechatCreateBillsEntity = WechatCreateBillsEntity()
    private var mHelper = WechatCreateBillsHelper(this)
    private var mBillType = "红包"
    /**
     * 0 -- 创建
     * 1 -- 修改
     */
    private var mType = 0
    override fun choiceType(icon: Int, type: String) {
        mBillType = type
        if (icon != -1){
            mBillEntity.iconInt = icon
            GlideUtil.displayHead(this, icon, mWechatCreateBillIconIv)
        }else{
            openAlbumWithPermissionCheck()
        }
    }

    override fun setLayoutResourceId() = R.layout.activity_wechat_create_bill

    override fun needLoadingView() = false

    override fun initAllViews() {
        setBlackTitle("账单", 1)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mType == 1){
            mBillEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatCreateBillsEntity
        }else{
            mBillEntity.iconInt = R.drawable.wechat_bill_icon1
            mBillEntity.title = "微信红包"
            mBillEntity.timestamp = TimeUtil.getCurrentTimeEndMs()
            mBillEntity.time = TimeUtil.stampToDateYMDHM(mBillEntity.timestamp)
            mWechatCreateBillTimeTv.text = TimeUtil.getNowTime2()
            GlideUtil.displayHead(this, R.drawable.wechat_bill_icon1, mWechatCreateBillIconIv)
            mBillEntity.incomeAndExpenses = 0
        }
    }

    override fun initViewsListener() {
        mWechatCreateBillReceiveTv.setOnClickListener(this)
        mWechatCreateBillSendTv.setOnClickListener(this)
        mWechatCreateBillRefreshIconLayout.setOnClickListener(this)
        mWechatCreateBillTitleLayout.setOnClickListener(this)
        mWechatCreateBillTimeLayout.setOnClickListener(this)
        overallAllRightWithBgTv.setOnClickListener(this)
        mWechatCreateBillSendOnOrOffIv.setOnClickListener(this)
        mWechatCreateBillRefreshTitleIv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatCreateBillReceiveTv ->{
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreateBillReceiveTv, mWechatCreateBillSendTv)
                mWechatCreateBillSendOnOrOffLayout.visibility = View.GONE
                mBillEntity.incomeAndExpenses = 0
            }
            R.id.mWechatCreateBillSendTv ->{
                OtherUtil.changeWechatTwoBtnBg(this, mWechatCreateBillSendTv, mWechatCreateBillReceiveTv)
                mWechatCreateBillSendOnOrOffLayout.visibility = View.VISIBLE
                mBillEntity.incomeAndExpenses = 1
            }
            R.id.mWechatCreateBillRefreshIconLayout ->{
                val dialog = ChoiceWechatBillIconDialog()
                dialog.setListener(this)
                dialog.show(supportFragmentManager, "icon")
            }
            R.id.mWechatCreateBillRefreshTitleIv ->{
                if (mBillEntity.incomeAndExpenses == 0){//收入
                    if (mBillType == "红包"){
                        mWechatCreateBillTitleEt.setText(StringUtils.insertFront(RandomUtil.getRandomNickName(), "微信红包-来自"))
                    }
                }else{
                    mWechatCreateBillTitleEt.setText("微信红包")
                }
                mBillEntity.title = mWechatCreateBillTitleEt.text.toString().trim()
            }
            R.id.mWechatCreateBillSendOnOrOffIv ->{
                mBillEntity.hasRefund = !mBillEntity.hasRefund
                OtherUtil.onOrOff(mBillEntity.hasRefund, mWechatCreateBillSendOnOrOffIv)
            }
            R.id.mWechatCreateBillTimeLayout ->{
                initTimePickerView("", 1)
            }
            R.id.overallAllRightWithBgTv ->{
                val money = mWechatCreateBillMoneyEt.text.toString().trim()
                if (TextUtils.isEmpty(money)){
                    showToast("请输入金额")
                    return
                }
                if(mType == 0){
                    mHelper.save(mBillEntity)
                }else{
                    mHelper.update(mBillEntity)
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        mBillEntity.timestamp = timeEntity.timeLong
        mBillEntity.time = TimeUtil.stampToDateMDHM(timeEntity.timeLong)
        mWechatCreateBillTimeTv.text = TimeUtil.stampToDateYMDHM(timeEntity.timeLong)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.CROP_IMAGE ->{
                mBillEntity.iconString = mFinalCropFile?.absolutePath
                GlideUtil.displayHead(this, mFinalCropFile, mWechatCreateBillIconIv)
            }
        }
    }
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openAlbum() {
        callAlbum(needCrop = true)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onShowRationale(request: PermissionRequest) {
        request.proceed()
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onNeverAskAgain() {
        showToast("请授权 [ 微商营销宝 ] 的 [ 存储 ] 访问权限")
    }
}
