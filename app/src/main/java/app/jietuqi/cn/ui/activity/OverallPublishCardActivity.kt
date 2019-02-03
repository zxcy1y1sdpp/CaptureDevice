package app.jietuqi.cn.ui.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.OverallCardEntity
import app.jietuqi.cn.ui.entity.OverallIndustryEntity
import app.jietuqi.cn.util.*
import app.jietuqi.cn.widget.dialog.EditDialog
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import cn.bingoogolapple.qrcode.core.QRCodeView
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.body.UIProgressResponseCallBack
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_publish_my_card.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*
import permissions.dispatcher.*
import java.io.File

/**
 * 作者： liuyuanbo on 2018/11/12 10:26.
 * 时间： 2018/11/12 10:26
 * 邮箱： 972383753@qq.com
 * 用途： 发布我的名片/群名片
 */
@RuntimePermissions
class OverallPublishCardActivity : BaseOverallInternetActivity(), EditDialogChoiceListener, QRCodeView.Delegate{
    /**
     * 0 -- 我的名片
     * 1 -- 修改我的名片
     * 2 -- 群名片
     * 3 -- 修改群名片
     */
    private var mCardType = 0
    /**
     * 微信二维码
     */
    private var mWechatBgFile: File? = null
    /**
     * 编辑的时候是否修改了二维码选择了不是二维码的图片
     */
    private var mChangeQRCode = false
    override fun setLayoutResourceId() = R.layout.activity_overall_publish_my_card

    override fun needLoadingView() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ColorFinal.wechatTitleBar)
        setLightStatusBarForM(this, false)
    }
    override fun initAllViews() {}

    override fun initViewsListener() {
        mOverallPublishMyCardLayout.setOnClickListener(this)
        mOverallPublishCardNumberLayout.setOnClickListener(this)
        mOverallPublishCardPhoneNumberLayout.setOnClickListener(this)
        mOverallPublishCardPhoneNumberTv.setOnClickListener(this)
        mOverallPublishCardQrCodeLayout.setOnClickListener(this)
        mOverallPublishCardIntroductionLayout.setOnClickListener(this)
        mOverallPublishCardTypeLayout.setOnClickListener(this)
        mOverallPublishCardAreaLayout.setOnClickListener(this)
        mOverallPublishCardSexualityOrNumberLayout.setOnClickListener(this)
        mOverallPublishCardBtn.setOnClickListener(this)
        mOverallPublishCardZXingView.setDelegate(this)
        overallAllRightWithOutBgTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mCardType = intent.getIntExtra(IntentKey.TYPE, 0)
        when (mCardType) {
            0 -> setBlackTitle("我的名片")
            1 -> {//修改我的名片
                setBlackTitle("编辑名片", 2)
                val cardEntity: OverallCardEntity = intent.getSerializableExtra(IntentKey.ENTITY) as OverallCardEntity
                mOverallPublishCardPhoneNumberTv.text = cardEntity.mobile
                for (entity in UserOperateUtil.getIndustrys()) {
                    if (entity.id == cardEntity.industry_id){
                        mOverallPublishCardTypeTv.text = entity.title
                        mOverallPublishCardTypeTv.tag = cardEntity.industry_id
                    }
                }
                mOverallPublishCardSexualityOrNumberTv.text = if(cardEntity.sex == 1) "男" else "女"
                mOverallPublishCardBtn.text = "修改名片"
                insertData(cardEntity)
            }
            2,3 -> {
                setBlackTitle("群名片")
                mOverallPublishCardNameTitleTv.text = "群名称"
                mOverallPublishCardNameTv.hint = "请输入群名称"
                mOverallPublishMyCardLayout.tag = "群名称"

                mOverallPublishCardNumberTitleTv.text = "群主微信号"
                mOverallPublishCardNumberTv.hint = "请输入群主微信号"
                mOverallPublishCardNumberLayout.tag = "群主微信号"

                mOverallPublishCardQrCodeTitleTv.text = "群二维码"

                mOverallPublishCardIntroductionTitleTv.text = "群介绍"
                mOverallPublishCardIntroductionTv.hint = "请输入群介绍"
                mOverallPublishCardIntroductionLayout.tag = "群介绍"

                mOverallPublishCardTypeTitleTv.text = "群类型"
                mOverallPublishCardTypeTv.hint = "请选择群类型"

                mOverallPublishCardSexualityOrNumberTitleTv.text = "群人数"
                mOverallPublishCardSexualityOrNumberTv.hint = "请选择群人数"
                mOverallPublishCardPhoneNumberLayout.visibility = View.GONE
                mOverallPublishCardBtn.text = "发布群名片"
                if (mCardType == 3){
                    setBlackTitle("编辑群名片", 2)
                    val cardEntity: OverallCardEntity = intent.getSerializableExtra(IntentKey.ENTITY) as OverallCardEntity
                    insertData(cardEntity)
                    for (entity in UserOperateUtil.getGroupType()) {
                        if (entity.id == cardEntity.industry_id){
                            mOverallPublishCardTypeTv.text = entity.title
                            mOverallPublishCardTypeTv.tag = cardEntity.industry_id
                        }
                    }
                    mOverallPublishCardSexualityOrNumberTv.text = if(cardEntity.number == 1) "<100人" else ">100人"
                    mOverallPublishCardBtn.text = "修改群名片"
                }
            }
        }
    }
    private fun insertData(careEntity: OverallCardEntity){
        mOverallPublishCardNameTv.text = careEntity.wxnickname
        mOverallPublishCardNumberTv.text = careEntity.wxname
        GlideUtil.display(this, careEntity.wx_qr, mOverallPublishCardQrCodeIv)
        mOverallPublishCardIntroductionTv.text = careEntity.content
        mOverallPublishCardAreaTv.tag = careEntity.province
        mOverallPublishCardAreaLayout.tag = careEntity.district
        mOverallPublishCardAreaTv.text = StringUtils.insertFrontAndBack(" ", careEntity.province, careEntity.district)
        mOverallPublishCardBtn.tag = careEntity.id
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallPublishMyCardLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(0, "", mOverallPublishMyCardLayout.tag.toString()))
                dialog.show(supportFragmentManager, "publishMyCard")
            }
            R.id.mOverallPublishCardNumberLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(1, "", mOverallPublishCardNumberLayout.tag.toString()))
                dialog.show(supportFragmentManager, "publishMyCard")
            }
            R.id.mOverallPublishCardPhoneNumberLayout, R.id.mOverallPublishCardPhoneNumberTv ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(2, "", mOverallPublishCardPhoneNumberLayout.tag.toString()))
                dialog.show(supportFragmentManager, "publishMyCard")
            }
            R.id.mOverallPublishCardQrCodeLayout ->{
                openAlbumWithPermissionCheck()
            }
            R.id.mOverallPublishCardIntroductionLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(3, "", mOverallPublishCardIntroductionLayout.tag.toString()))
                dialog.show(supportFragmentManager, "publishMyCard")
            }
            R.id.mOverallPublishCardTypeLayout ->{
                if (mCardType < 2){
                    initIndustryOrGroup(showAllType = false)
                }else{
                    initIndustryOrGroup(3, showAllType = false)
                }
            }
            R.id.mOverallPublishCardAreaLayout ->{
                initAreaOptions(false)
            }
            R.id.mOverallPublishCardSexualityOrNumberLayout ->{
                if (mCardType < 2){
                    initSingleOneOptionPicker(0, false)
                }else{
                    initSingleOneOptionPicker(4, false)
                }
            }
            R.id.mOverallPublishCardBtn ->{
                if (mCardType < 2){//我的名片
                    if (UserOperateUtil.isCurrentLoginDirectlyLogin(this)){
                        if (canPublish()){
                            publishCard()
                        }
                    }
                }else{//群名片
                    if (UserOperateUtil.isCurrentLoginDirectlyLogin(this)){
                        if (canPublish()){
                            publishCard()
                        }
                    }
                }
            }
            R.id.overallAllRightWithOutBgTv ->{
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("提示")
                        .setContentText("确定要删除您的名片吗？")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .setConfirmClickListener { sweetAlertDialog ->
                            sweetAlertDialog.dismissWithAnimation()
                            deleteCard()
                        }.setCancelClickListener {
                            it.dismissWithAnimation()
                        }.show()
            }
        }
    }
    override fun onChoice(entity: EditDialogEntity?) {
        when(entity?.position){
            0 ->{//微信昵称
                mOverallPublishCardNameTv.text = entity.content
            }
            1 ->{//微信号
                mOverallPublishCardNumberTv.text = entity.content
            }
            2 ->{//手机号
                mOverallPublishCardPhoneNumberTv.text = entity.content
            }
            3 ->{//个人介绍
                mOverallPublishCardIntroductionTv.text = entity.content
            }
        }
    }

    override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
        super.onOptionsSelect(options1, options2, options3, v)
        if (mPickerType == 0 || mPickerType == 4){
            mOverallPublishCardSexualityOrNumberTv.text = v?.tag.toString()
        }
        if (mPickerType == 1){
            var industryEntity: OverallIndustryEntity = v?.tag as OverallIndustryEntity
            mOverallPublishCardTypeTv.text = industryEntity.pickerViewText
            mOverallPublishCardTypeTv.tag = industryEntity.id
        }
        if (mPickerType == 2){
            var area = v?.tag.toString()
            mOverallPublishCardAreaTv.text = area
            val arr=  area.split(" ")
            mOverallPublishCardAreaTv.tag = arr[0]
            mOverallPublishCardAreaLayout.tag = arr[1]
        }
        if (mPickerType == 3){
            mOverallPublishCardTypeTv.text = v?.tag.toString()
            var industryEntity: OverallIndustryEntity = v?.tag as OverallIndustryEntity
            mOverallPublishCardTypeTv.text = industryEntity.pickerViewText
            mOverallPublishCardTypeTv.tag = industryEntity.id
        }
        if (mPickerType == 4){
            mOverallPublishCardSexualityOrNumberTv.text = v?.tag.toString()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK){
            when(requestCode){
                RequestCode.IMAGE_SELECT ->{
                    mWechatBgFile = mFiles[0]
                    mOverallPublishCardZXingView.decodeQRCode(mFiles[0].absolutePath)
                    GlideUtil.display(this, mFiles[0], mOverallPublishCardQrCodeIv)
                    mChangeQRCode = false
                }
            }
        }
    }
    /**
     * 是否可以发布
     */
    private fun canPublish(): Boolean{
        if (mCardType < 2){//发布我的名片/修改我的名片
            if (OtherUtil.getContent(mOverallPublishCardNameTv).isEmpty()){
                showToast("请输入微信昵称")
                return false
            }
            if (OtherUtil.getContent(mOverallPublishCardNumberTv).isEmpty()){
                showToast("请输入微信号")
                return false
            }
            if (OtherUtil.getContent(mOverallPublishCardPhoneNumberTv).isEmpty()){
                showToast("请输入手机号")
                return false
            }
            if (mCardType == 1 || mCardType == 3){
                if (mChangeQRCode){
                    showToast("请选择二维码")
                    return false
                }
            } else{
                if (null == mWechatBgFile){
                    showToast("请选择二维码")
                    return false
                }
            }

            if (OtherUtil.getContent(mOverallPublishCardIntroductionTv).isEmpty()){
                showToast("请输入个人介绍")
                return false
            }
            if (OtherUtil.getContent(mOverallPublishCardTypeTv).isEmpty()){
                showToast("请选择行业类别")
                return false
            }
            if (OtherUtil.getContent(mOverallPublishCardSexualityOrNumberTv).isEmpty()){
                showToast("请选择性别")
                return false
            }
        }else{
            if (OtherUtil.getContent(mOverallPublishCardNameTv).isEmpty()){
                showToast("请输入群名称")
                return false
            }
            if (OtherUtil.getContent(mOverallPublishCardNumberTv).isEmpty()){
                showToast("请输入群主微信号")
                return false
            }
            if (mCardType == 2){
                if (null == mWechatBgFile){
                    showToast("请选择群二维码")
                    return false
                }
            }
            if (OtherUtil.getContent(mOverallPublishCardIntroductionTv).isEmpty()){
                showToast("请输入群介绍")
                return false
            }
            if (OtherUtil.getContent(mOverallPublishCardTypeTv).isEmpty()){
                showToast("请选择群类别")
                return false
            }
            if (OtherUtil.getContent(mOverallPublishCardSexualityOrNumberTv).isEmpty()){
                showToast("请选择群人数")
                return false
            }

        }
        if (OtherUtil.getContent(mOverallPublishCardAreaTv).isEmpty()){
            showToast("请选择所在地区")
            return false
        }
        return true
    }

    /**
     * 发布名片
     */
    private fun publishCard(){
        val mUIProgressResponseCallBack = object : UIProgressResponseCallBack() {
            override fun onUIResponseProgress(bytesRead: Long, contentLength: Long, done: Boolean) {}
        }
        var request = EasyHttp.post(HttpConfig.INFORMATION).params("way", "add")//way 必传add
        if (mCardType < 2){//发布我的名片/修改我的名片
            if (mCardType == 0){//发布我的名片
                request.params("uid", UserOperateUtil.getUserId())
            }else{//修改我的名片
                request.params("id",  mOverallPublishCardBtn.tag.toString())
            }
            request.params("mobile",OtherUtil.getContent(mOverallPublishCardPhoneNumberTv))
            request.params("sex", if(OtherUtil.getContent(mOverallPublishCardSexualityOrNumberTv) == "男"){
                "1"
            }else{
                "2"
            })
        }else{
            if (mCardType == 3){
                request.params("id",  mOverallPublishCardBtn.tag.toString())
            }
            request.params("uid", UserOperateUtil.getUserId())
            request.params("classify", "group")
            request.params("number", if(OtherUtil.getContent(mOverallPublishCardSexualityOrNumberTv) == "<100人"){
                "1"
            }else{
                "2"
            })
        }
        if (null != mWechatBgFile){
            request.params("wx_qr", mWechatBgFile, mWechatBgFile?.name, mUIProgressResponseCallBack)
        }
        request.params("wxnickname", OtherUtil.getContent(mOverallPublishCardNameTv))
        request.params("wxname", OtherUtil.getContent(mOverallPublishCardNumberTv))
        request.params("content", OtherUtil.getContent(mOverallPublishCardIntroductionTv))
        request.params("province", mOverallPublishCardAreaTv.tag.toString())
        request.params("district", mOverallPublishCardAreaLayout.tag.toString())
        request.params("industry_id", mOverallPublishCardTypeTv.tag.toString())
        request.execute(object : SimpleCallBack<String>() {
            override fun onError(e: ApiException) {}
            override fun onSuccess(t: String) {
                EventBusUtil.post("")
                when(mCardType){
                    0 ->{
                        showToast("发布成功")
                    }
                    1 ->{
                        showToast("修改成功")
                    }
                    2 ->{
                        showToast("发布成功")
                    }
                    3 ->{
                        showToast("修改成功")
                    }
                }

                finish()
            }
            override fun onStart() {
                super.onStart()
                showLoadingDialog("请稍后")
            }
        })
    }

    /**
     * 删除名片
     */
    private fun deleteCard(){
        EasyHttp.post(HttpConfig.INFORMATION).params("way", "del")//way 必传add
                .params("id", mOverallPublishCardBtn.tag.toString())
                .params("uid", UserOperateUtil.getUserId())
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }
                    override fun onSuccess(t: String) {
                        EventBusUtil.post("")
                        showToast("删除成功")
                        finish()
                    }

                    override fun onStart() {
                        super.onStart()
                        showLoadingDialog("请稍后")
                    }
                })
    }
    override fun onScanQRCodeSuccess(result: String?) {
        if (TextUtils.isEmpty(result)){
            mChangeQRCode = true
            showToast("图像中不包含二维码，请重新选择")
            mWechatBgFile = null
            mFinalCropFile = null
            GlideUtil.display(this, "", mOverallPublishCardQrCodeIv)
        }
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onScanQRCodeOpenCameraError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openAlbum() {
        callAlbum()
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
