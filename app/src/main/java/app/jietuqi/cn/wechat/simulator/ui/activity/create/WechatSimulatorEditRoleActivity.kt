package app.jietuqi.cn.wechat.simulator.ui.activity.create

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.ResourceHelper
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorHelper
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.wechat.widget.groupicon.CombineBitmap
import app.jietuqi.cn.wechat.widget.groupicon.layout.WechatLayoutManager
import app.jietuqi.cn.wechat.widget.groupicon.listener.OnProgressListener
import app.jietuqi.cn.widget.dialog.EditDialog
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.activity_wechat_simulator_edit_role.*
import permissions.dispatcher.*
import java.io.File

/**
 * 作者： liuyuanbo on 2018/12/3 16:18.
 * 时间： 2018/12/3 16:18
 * 邮箱： 972383753@qq.com
 * 用途： 微信模拟器 -- 群聊修改角色
 */
@RuntimePermissions
class WechatSimulatorEditRoleActivity : BaseWechatActivity(), EditDialogChoiceListener {
    override fun onChoice(entity: EditDialogEntity?) {
        mWechatEditRoleNickNameTv.text = entity?.content
        mUserEntity?.wechatUserNickName = entity?.content
        mUserEntity?.pinyinNickName = OtherUtil.transformPinYin(entity?.content)
        mUserEntity?.firstChar = OtherUtil.getFirstLetter(mUserEntity?.pinyinNickName)
    }
    private var mUserEntity: WechatUserEntity? = null
    private lateinit var mHelper: WechatSimulatorHelper
    /**
     * 聊天列表中的联系人
     */
    private lateinit var mRolesEntity: WechatUserEntity
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_edit_role

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setBlackTitle("修改角色", 1)
        mMySideEntity = UserOperateUtil.getWechatSimulatorMySelf()
    }

    override fun initViewsListener() {
        mWechatEditRoleAvatarLayout.setOnClickListener(this)
        mWechatEditRoleNickNameLayout.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val tableName = intent.getStringExtra(IntentKey.GROUP_TABLE_NAME)
        mHelper = WechatSimulatorHelper(this, tableName)
        mUserEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity?
        mRolesEntity = intent.getSerializableExtra(IntentKey.ROLE_ENTITY) as WechatUserEntity
        mWechatEditRoleNickNameTv.text = mUserEntity?.wechatUserNickName
        mUserEntity?.getAvatarFile()?.let { GlideUtil.displayHead(this, it, mWechatEditRoleAvatarIv) }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.overallAllRightWithBgTv ->{
                RoleLibraryHelper(this).update(this, mUserEntity)//修改通讯录中的数据
                mHelper.queryByIdAndUpdate(mUserEntity)
                mUserEntity?.eventBusTag = "删除并刷新"
                EventBusUtil.post(mUserEntity)
                EventBusUtil.post("聊天页面改变用户信息")
                createMsg()
                finish()
            }
            R.id.mWechatEditRoleAvatarLayout ->{
                openAlbumWithPermissionCheck()
            }
            R.id.mWechatEditRoleNickNameLayout ->{
                val dialog = EditDialog()
                mUserEntity?.wechatUserNickName?.let { dialog.setData(this, EditDialogEntity(0, it, "填写角色昵称")) }
                dialog.show(supportFragmentManager, "payment")
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK){
            when(requestCode){
                RequestCode.CROP_IMAGE ->{
                    GlideUtil.display(this, mFinalCropFile, mWechatEditRoleAvatarIv)
                    mUserEntity?.avatarFile = mFinalCropFile
                    mUserEntity?.wechatUserAvatar = mFinalCropFile?.absolutePath
                }
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

    private fun createMsg(){
        var deleteEntity: WechatUserEntity
        for (i in mRolesEntity.groupRoles.indices){
            deleteEntity = mRolesEntity.groupRoles[i]
            if (deleteEntity.wechatUserId == mUserEntity?.wechatUserId){
                if (deleteEntity.isRecentRole){
                    mUserEntity?.isRecentRole = true
                }
                mRolesEntity.groupRoles.remove(deleteEntity)
                mRolesEntity.groupRoles.add(mUserEntity)
            }
        }
        val roleList = arrayListOf<WechatUserEntity>()
        roleList.addAll(mRolesEntity.groupRoles)
        roleList.add(mMySideEntity)
        var entity: WechatUserEntity
        var listHelper = WechatSimulatorListHelper(this)
//        if (mEntity.groupRoles.size < 9){
            val bitmap = arrayOfNulls<Bitmap>(roleList.size)
            for (i in roleList.indices){
                if (i < 9){
                    entity = roleList[i]
                    if (!TextUtils.isEmpty(entity.wechatUserAvatar)){//选择的
                        val file = File(entity.wechatUserAvatar)
                        val uri = Uri.fromFile(file)
                        val header = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        bitmap[i] = header
                    }else{
                        bitmap[i] = BitmapFactory.decodeResource(resources, ResourceHelper.getAppIconId(entity.resourceName))
                    }
                }
            }
            CombineBitmap.init(this)
                    .setLayoutManager(WechatLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                    .setSize(180) // 必选，组合后Bitmap的尺寸，单位dp
                    .setGap(9) // 单个图片之间的距离，单位dp，默认0dp
                    .setGapColor(Color.parseColor("#DDDEE0")) // 单个图片间距的颜色，默认白色
                    .setPlaceholder(R.drawable.head_default) // 单个图片加载失败的默认显示图片
                    .setBitmaps(*bitmap) // 要加载的图片bitmap数组
                    // 设置“子图片”的点击事件，需使用setImageView()，index和图片资源数组的索引对应
                    .setOnSubItemClickListener { }
                    // 加载进度的回调函数，如果不使用setImageView()方法，可在onComplete()完成最终图片的显示
                    .setOnProgressListener(object : OnProgressListener {
                        override fun onStart() {}
                        override fun onComplete(bitmap: Bitmap) {
                            mRolesEntity.groupHeader = bitmap
                            mRolesEntity.eventBusTag = "修改群头像"
                            EventBusUtil.post(mRolesEntity)
                            listHelper.update(mRolesEntity)
                            finish()
                        }
                    }).build()
//        }else{
//            EventBusUtil.post(mEntity)
//            listHelper.update(mEntity)
//            finish()
//        }
    }
}
