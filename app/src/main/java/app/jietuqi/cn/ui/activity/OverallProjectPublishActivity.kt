package app.jietuqi.cn.ui.activity

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.OverallIndustryEntity
import app.jietuqi.cn.ui.entity.OverallPublishEntity
import app.jietuqi.cn.ui.entity.ProjectMarketEntity
import app.jietuqi.cn.util.*
import app.jietuqi.cn.widget.dialog.EditDialog
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.body.UIProgressResponseCallBack
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_publish_project.*
import permissions.dispatcher.*
import java.io.File
/**
 * 作者： liuyuanbo on 2018/11/12 10:26.
 * 时间： 2018/1/22 21:37
 * 邮箱： 972383753@qq.com
 * 用途： 发布我的名片/群名片
 */
@RuntimePermissions
class OverallProjectPublishActivity : BaseOverallInternetActivity(), EditDialogChoiceListener{
    /**
     * 0 -- 发布
     * 1 -- 修改
     */
    private var mType = 0
    private var mEntity: ProjectMarketEntity? = null
    /**
     * 项目封面
     */
    private var mPicBgFile: File? = null
    private var mContent = ""
    private var mIds = ""
    /**
     * 图片地址
     * 包括本地地址和网络地址
     */
    private var mImageList = arrayListOf<OverallPublishEntity>()
    override fun setLayoutResourceId() = R.layout.activity_overall_publish_project

    override fun needLoadingView() = false
    override fun initAllViews() {
        setTopTitle("发布项目")
    }

    override fun initViewsListener() {
        mProjectPicLayout.setOnClickListener(this)
        mProjectClassifyLayout.setOnClickListener(this)
        mProjectTitleLayout.setOnClickListener(this)
        mProjectDescriptionLayout.setOnClickListener(this)
        mProjectWXNumberLayout.setOnClickListener(this)
        mProjectPhoneNumberLayout.setOnClickListener(this)
        mProjectQQNumberLayout.setOnClickListener(this)
        mProjectBtn.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        if (mType == 1){//修改
            setTopTitle("修改项目")
            mProjectBtn.text = "修改项目"
            mEntity = intent.getSerializableExtra(IntentKey.ENTITY) as ProjectMarketEntity?
            insertData()
        }
    }
    private fun insertData(){
        GlideUtil.display(this, mEntity?.logo, mProjectPicIv)
        mProjectClassifyTv.text = mEntity?.industry?.title
        mProjectTitleTv.text = mEntity?.name
        mProjectDescriptionTv.text = "已填写"
        mEntity?.picture?.let { mImageList.addAll(it) }
        for(i in mImageList.indices){
            mImageList[i].fromNet = true
        }
        mEntity?.content?.let { mContent = it }
        mProjectWXNumberTv.text = mEntity?.wx
        mProjectPhoneNumberTv.text = mEntity?.phone
        mProjectQQNumberTv.text = mEntity?.qq
        mProjectClassifyTv.tag = mEntity?.industry_id
        mEntity?.picture_id?.let { mIds = it }

    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mProjectTitleLayout ->{//项目标题
                LaunchUtil.startEditActivity(this, OtherUtil.getContent(mProjectTitleTv))
            }
            R.id.mProjectDescriptionLayout ->{//项目详情
                LaunchUtil.startOverallProjectPublishDetailsActivity(this, mType, mImageList, mContent)
            }
            R.id.mProjectWXNumberLayout ->{//微信号
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(2, "", mProjectWXNumberLayout.tag.toString()))
                dialog.show(supportFragmentManager, "publishProject")
            }
            R.id.mProjectPicLayout ->{
                openAlbumWithPermissionCheck()
            }
            R.id.mProjectPhoneNumberLayout ->{//手机号
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(3, "", mProjectPhoneNumberLayout.tag.toString(), true))
                dialog.show(supportFragmentManager, "publishProject")
            }
            R.id.mProjectQQNumberLayout ->{//QQ号
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(4, "", mProjectQQNumberLayout.tag.toString(), true))
                dialog.show(supportFragmentManager, "publishProject")
            }
            R.id.mProjectClassifyLayout ->{
                initIndustryOrGroup(7, showAllType = false)
            }
            R.id.mProjectBtn ->{
                if (UserOperateUtil.isCurrentLoginDirectlyLogin(this)){
                    if (canPublish()){
                        publishCard()
                    }
                }
            }
        }
    }
    override fun onChoice(entity: EditDialogEntity?) {
        when(entity?.position){
            2 ->{//微信号
                mProjectWXNumberTv.text = entity.content
            }
            3 ->{//手机号
                mProjectPhoneNumberTv.text = entity.content
            }
            4 ->{//QQ号
                mProjectQQNumberTv.text = entity.content
            }
        }
    }

    override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
        super.onOptionsSelect(options1, options2, options3, v)
        if (mPickerType == 7){
            var industryEntity: OverallIndustryEntity = v?.tag as OverallIndustryEntity
            mProjectClassifyTv.text = industryEntity.pickerViewText
            mProjectClassifyTv.tag = industryEntity.id
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.IMAGE_SELECT ->{
                if (null != data){
                    mPicBgFile = mFiles[0]
                    GlideUtil.display(this, mFiles[0], mProjectPicIv)
                }
            }
            RequestCode.PROJECT_TITLE ->{
                mProjectTitleTv.text = data?.getStringExtra(IntentKey.CONTENT)
            }
            RequestCode.CHOICE_PROJECT ->{
                if (null != data){
                    mContent = data.getStringExtra(IntentKey.DESCRIPTION)
                    mIds = data.getStringExtra(IntentKey.PICTURES_ID)
                    if (mImageList.size > 0){
                        mImageList.clear()
                    }
                    mImageList.addAll(data.getSerializableExtra(IntentKey.LIST) as ArrayList<OverallPublishEntity>)
                    if (!TextUtils.isEmpty(mContent) && !TextUtils.isEmpty(mIds)){
                        mProjectDescriptionTv.text = "已填写"
                    }
                }
            }
        }
    }
    /**
     * 是否可以发布
     */
    private fun canPublish(): Boolean{
        if(mType == 0){
            if (null == mPicBgFile){
                showToast("请选择项目封面")
                return false
            }
        }
        if (OtherUtil.getContent(mProjectClassifyTv).isEmpty()){
            showToast("请选择项目类型")
            return false
        }
        if (OtherUtil.getContent(mProjectTitleTv).isEmpty()){
            showToast("请输入项目标题")
            return false
        }
        if (OtherUtil.getContent(mProjectDescriptionTv).isEmpty()){
            showToast("请输入项目详情")
            return false
        }
        if (OtherUtil.getContent(mProjectWXNumberTv).isEmpty()){
            showToast("请填写微信号")
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
        var request = EasyHttp.post(HttpConfig.STORE).params("way", "edit")//way 必传add

        if (mType == 0){//发布
            request.params("users_id", UserOperateUtil.getUserId())
//            request.params("name", mProjectTitleTv.text.toString().trim())
//            request.params("industry_id", mProjectClassifyTv.tag.toString())
        }else{
            request.params("id",  mEntity?.id.toString())

        }
        if (null != mPicBgFile){
            request.params("logo", mPicBgFile, mPicBgFile?.name, mUIProgressResponseCallBack)
        }
        request.params("name", mProjectTitleTv.text.toString().trim())
        request.params("industry_id", mProjectClassifyTv.tag.toString())
        request.params("wx", OtherUtil.getContent(mProjectWXNumberTv))
        request.params("phone", OtherUtil.getContent(mProjectPhoneNumberTv))
        request.params("qq", OtherUtil.getContent(mProjectQQNumberTv))
        request.params("content", mContent)
        request.params("picture_id", mIds)
        request.execute(object : SimpleCallBack<String>() {
            override fun onError(e: ApiException) {}
            override fun onSuccess(t: String) {
                if (mType == 0){
                    showToast("发布成功")
                }else{
                    showToast("修改成功")
                }
                EventBusUtil.post("")
                finish()
            }
            override fun onStart() {
                super.onStart()
                showLoadingDialog("请稍后")
            }
        })
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
