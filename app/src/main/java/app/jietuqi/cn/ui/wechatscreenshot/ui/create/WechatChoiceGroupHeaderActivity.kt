package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.ResourceHelper
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.adapter.WechatChoiceGroupHeaderAdapter
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.wechat.widget.groupicon.CombineBitmap
import app.jietuqi.cn.wechat.widget.groupicon.layout.WechatLayoutManager
import app.jietuqi.cn.wechat.widget.groupicon.listener.OnProgressListener
import app.jietuqi.cn.widget.dialog.ChoiceGroupHeaderDialog
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.activity_choice_group_header.*
import permissions.dispatcher.*
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * 作者： liuyuanbo on 2019/3/4 11:01.
 * 时间： 2019/3/4 11:01
 * 邮箱： 972383753@qq.com
 * 用途： 选择群头像
 */
@RuntimePermissions
class WechatChoiceGroupHeaderActivity : BaseCreateActivity(), ChoiceGroupHeaderDialog.OperateListener{
    override fun operate(item: Int) {
        if (item == 0){
            LaunchUtil.startForResultRoleOfLibraryActivity(this, 100, false)
        }else{
            openAlbumWithPermissionCheck()
        }
    }
    private var mGroupHeaderList = arrayListOf<WechatUserEntity>()
    private lateinit var mAdapter: WechatChoiceGroupHeaderAdapter
    private lateinit var mHelper: RoleLibraryHelper
    /**
     * 需要修改的位置
     */
    private var mPosition = 0

    override fun setLayoutResourceId() = R.layout.activity_choice_group_header

    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("选择群头像", rightTitle = "确定")
        mHelper = RoleLibraryHelper(this)
//        for (i in 0..8) {
        mGroupHeaderList.addAll(mHelper.queryRandomItem(9))
//        }
        mAdapter = WechatChoiceGroupHeaderAdapter(mGroupHeaderList)
        mGroupHeaderRv.adapter = mAdapter
    }

    override fun initViewsListener() {
        mGroupHeaderRandomTv.setOnClickListener(this)
        mGroupHeaderRv.addOnItemTouchListener(object : OnRecyclerItemClickListener(mGroupHeaderRv) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                mPosition = vh.adapterPosition
                val dialog = ChoiceGroupHeaderDialog()
                dialog.setRequestCode(100, this@WechatChoiceGroupHeaderActivity, false)
                dialog.show(supportFragmentManager, "choiceHeader")
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mGroupHeaderRandomTv ->{
                mGroupHeaderList.clear()
                mGroupHeaderList.addAll(mHelper.queryRandomItem(9))
                mAdapter.notifyDataSetChanged()
            }
            R.id.overAllRightTitleTv ->{
                createGroupHeader()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.CROP_IMAGE ->{
                if (null != data){
                    mGroupHeaderList[mPosition].wechatUserAvatar = mFinalCropFile?.absolutePath
                    mAdapter.notifyItemChanged(mPosition)
                }
            }
            100 ->{
                if (null != data){
                    mGroupHeaderList[mPosition] = data?.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity
                    mAdapter.notifyItemChanged(mPosition)
                }
            }
        }
    }
    private fun createGroupHeader(){
        var entity: WechatUserEntity
        val bitmap = arrayOfNulls<Bitmap>(mGroupHeaderList.size)
        for (i in mGroupHeaderList.indices){
            entity = mGroupHeaderList[i]
            if (!TextUtils.isEmpty(entity.wechatUserAvatar)){//选择的
                val file = File(entity.wechatUserAvatar)
                val uri = Uri.fromFile(file)
                val header = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                bitmap[i] = header
            }else{
                bitmap[i] = BitmapFactory.decodeResource(resources, ResourceHelper.getAppIconId(entity.resourceName))
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
//                        mBitmap = bitmap
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                        val bitmapByte = baos.toByteArray()
                        EventBusUtil.post(bitmapByte)
                        finish()
                    }
                }).build()
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
