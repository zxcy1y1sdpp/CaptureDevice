package app.jietuqi.cn.ui.activity

import android.Manifest
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.callback.DeleteListener
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.OverallPublishFriendsCircleAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallPublishEntity
import app.jietuqi.cn.util.FileUtil
import app.jietuqi.cn.util.StringUtils
import com.zhihu.matisse.Matisse
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.body.UIProgressResponseCallBack
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.utils.HttpLog
import kotlinx.android.synthetic.main.activity_overall_project_publish_details.*
import permissions.dispatcher.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

/**
 * 作者： liuyuanbo on 2018/11/12 10:26.
 * 时间： 2018/1/22 21:37
 * 邮箱： 972383753@qq.com
 * 用途： 项目详情
 */
@RuntimePermissions
class OverallProjectPublishDetailsActivity : BaseOverallInternetActivity(), DeleteListener {

    private var mType = 0
    private var mList: ArrayList<OverallPublishEntity> = arrayListOf()
    private lateinit var mAdapter: OverallPublishFriendsCircleAdapter
    private var mLastEntity = OverallPublishEntity(R.drawable.project_add_pic)
    /**
     * 图片的id
     */
    private var mPicturesId = ""
    override fun setLayoutResourceId() = R.layout.activity_overall_project_publish_details

    override fun needLoadingView() = false
    override fun initAllViews() {
        setTopTitle("项目详情", rightTitle = "完成")
    }

    override fun initViewsListener() {
        mOverallProjectPublishDetailsRv.addOnItemTouchListener(object : OnRecyclerItemClickListener(mOverallProjectPublishDetailsRv) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val position = vh.adapterPosition
                if (position == mList.size - 1){//如果是选择的最后一个发布按钮
                    openAlbumWithPermissionCheck()
                }
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        var list: ArrayList<OverallPublishEntity> = intent.getSerializableExtra(IntentKey.LIST) as ArrayList<OverallPublishEntity>
        var content = intent.getStringExtra(IntentKey.CONTENT)
        mOverallProjectPublishDetailsEt.setText(content)
        if (null != list && list.size > 0){
            mList.addAll(list)
        }
        if (mList.size == 0){
            mLastEntity.position = 0
            mList.add(mLastEntity)
        }else if (mList.size in 1..8){
            mLastEntity.position = mList.size - 1
            mList.add(mLastEntity)
        }
        mOverallProjectPublishDetailsPicCountTv.text = StringUtils.insertBack((mList.size - 1).toString(), "/8")
        mAdapter = OverallPublishFriendsCircleAdapter(mList, 8, this)
        mAdapter.setType(mType)
        mOverallProjectPublishDetailsRv.adapter = mAdapter
//        mAdapter.notifyDataSetChanged()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.IMAGE_SELECT ->{
                    mAlbumList = Matisse.obtainResult(data)
                    mList.removeAt(mList.size - 1)
                    var entity: OverallPublishEntity
                    for (i in 0 until mAlbumList.size) {
                        entity = OverallPublishEntity()
                        entity.pic = mFiles[i]
                        entity.fromNet = false
                        mList.add(entity)
                    }
                    mLastEntity.position = mList.size
                    var uploadEntity: OverallPublishEntity
                    for (i in mList.indices) {
                        uploadEntity = mList[i]
                        if (!uploadEntity.fromNet){
                            uploadEntity.position = i
                            if (uploadEntity.uploadStatus == 0){
                                luban(mList[i])
                            }
                        }
                    }
                    mList.add(mLastEntity)
                    mOverallProjectPublishDetailsPicCountTv.text = StringUtils.insertBack((mList.size - 1).toString(), "/8")
                    mAdapter.notifyDataSetChanged()
                    mFiles.clear()
                }
            }
        }
    }
    private fun luban(entity: OverallPublishEntity){
        Luban.with(this)
                .load(entity.pic)//原图
                .ignoreBy(100)//多大不压缩
                .setTargetDir(cacheDir.path)//缓存压缩图片路径
                .setCompressListener(object : OnCompressListener {
                    override fun onStart() {}

                    override fun onSuccess(file: File) {
                        entity.pic = file
                        uploadPics(entity)
                        Log.e("luban --- hou", FileUtil.getFileOrFilesSize(entity.pic.absolutePath, FileUtil.SIZETYPE_KB).toString())
                    }
                    override fun onError(e: Throwable) {
                    }
                }).launch()
    }
    private fun uploadPics(entity: OverallPublishEntity){
        HttpLog.e("未上传前： " + entity.toString())
        val mUIProgressResponseCallBack = object : UIProgressResponseCallBack() {
            override fun onUIResponseProgress(bytesRead: Long, contentLength: Long, done: Boolean) {
                val progress = (bytesRead * 100 / contentLength).toInt()
                entity.progress = progress
                if (done) {//完成
                }
                mAdapter.notifyItemChanged(entity.position)
            }
        }
        EasyHttp.post(HttpConfig.UPLOAD)
                .params("way", "picture")
                .params("file", entity.pic, entity.pic.name, mUIProgressResponseCallBack)
                .execute(object : CallBackProxy<OverallApiEntity<OverallPublishEntity>, OverallPublishEntity>(object : SimpleCallBack<OverallPublishEntity>() {
                    override fun onSuccess(t: OverallPublishEntity) {
//                        mImageList.add(t)
                        entity.id = t.id
                        entity.url = t.url
                        entity.width = t.width
                        entity.height = t.height
                        entity.progress = 100
                        entity.uploadStatus = 2
                        mAdapter.notifyItemChanged(entity.position)
                    }
                    override fun onStart() {
                        super.onStart()
                        entity.uploadStatus = 1
                    }
                    override fun onError(e: ApiException) {
                        entity.uploadStatus = 3
                    }
                }) {})
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.overAllRightTitleTv -> {
                var content = mOverallProjectPublishDetailsEt.text.toString().trim()
                if (TextUtils.isEmpty(content)){
                    showToast("请填写项目介绍")
                    return
                }
                if (mList.size <= 1){
                    showToast("最少添加一张项目图片")
                    return
                }
                var intent = Intent()
                mList.removeAt(mList.size - 1)
                if (mList.size in 1..8){
                    var entity: OverallPublishEntity
                    val ids = StringBuilder()
                    for (i in mList.indices) {
                        entity = mList[i]
                        ids.append(entity.id).append(",")
                    }
                    mPicturesId = ids.deleteCharAt(ids.length - 1).toString()
                    intent.putExtra(IntentKey.PICTURES_ID, mPicturesId)
                }
                intent.putExtra(IntentKey.DESCRIPTION, mOverallProjectPublishDetailsEt.text.toString())
                intent.putExtra(IntentKey.LIST, mList)
                setResult(2, intent)
                finish()
            }
        }
    }
    override fun delete(position: Int) {
        mList.removeAt(position)
        mAdapter.notifyDataSetChanged()
        mOverallProjectPublishDetailsPicCountTv.text = StringUtils.insertBack((mList.size - 1).toString(), "/8")
    }
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openAlbum() {
        callAlbum(9 - mList.size)
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
