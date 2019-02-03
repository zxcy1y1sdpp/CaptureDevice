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
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.OverallPublishFriendsCircleAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallPublishEntity
import app.jietuqi.cn.util.FileUtil
import app.jietuqi.cn.util.UserOperateUtil
import com.zhihu.matisse.Matisse
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.body.UIProgressResponseCallBack
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.utils.HttpLog
import kotlinx.android.synthetic.main.activity_overall_publish_friends_circle.*
import permissions.dispatcher.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File


/**
 * 作者： liuyuanbo on 2018/11/20 10:27.
 * 时间： 2018/11/20 10:27
 * 邮箱： 972383753@qq.com
 * 用途： 发布说说的按钮
 */
@RuntimePermissions
class OverallPublishFriendsCircleActivity : BaseOverallInternetActivity(), DeleteListener {
    override fun delete(position: Int) {
        mList.removeAt(position)
        mAdapter.notifyDataSetChanged()
    }

    override fun setLayoutResourceId() = R.layout.activity_overall_publish_friends_circle
    private var mList: ArrayList<OverallPublishEntity> = arrayListOf()
    private var mAdapter: OverallPublishFriendsCircleAdapter = OverallPublishFriendsCircleAdapter(mList, 9, this)
    private var mLastEntity = OverallPublishEntity(R.mipmap.add_more_pics)
    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("发布动态", rightTitle = "发布")
        mLastEntity.position = 0
        mList.add(mLastEntity)
        mOverallPublishFriendsCircleRecyclerView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()

    }
    override fun initViewsListener() {
        mOverallPublishFriendsCircleRecyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(mOverallPublishFriendsCircleRecyclerView) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val position = vh.adapterPosition
                if (position == mList.size - 1){//如果是选择的最后一个发布按钮
                    openAlbumWithPermissionCheck()
                }
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {

            }
        })
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.overAllRightTitleTv ->{
                if (UserOperateUtil.isCurrentLoginDirectlyLogin(this)) {
                    publish()
                }
            }
        }
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
                        mList.add(entity)
                    }
                    mLastEntity.position = mList.size
                    var uploadEntity: OverallPublishEntity
                    for (i in mList.indices) {
                        uploadEntity = mList[i]
                        uploadEntity.position = i
                        if (uploadEntity.uploadStatus == 0){
                            luban(mList[i])
                        }
                    }
                    mList.add(mLastEntity)
                    mAdapter.notifyDataSetChanged()
                    mFiles.clear()
                }
            }
        }
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
    private fun publish(){
        var content = mOverallPublishFriendsCircleEt.text.toString().trim()
        if (TextUtils.isEmpty(content)){
            showToast("请输入文字！")
            return
        }
        val sb = StringBuffer()
        var coverId = ""
        if (mList.size > 1){
            var entity: OverallPublishEntity
            for (i in mList.indices) {
                entity = mList[i]
                if (i != mList.size - 1){
                    sb.append(entity.id).append(",")
                }
            }
            coverId = sb.deleteCharAt(sb.length - 1).toString()
        }
        EasyHttp.post(HttpConfig.INFO)
                .params("way", "article_add")
                .params("uid", UserOperateUtil.getUserId())
                .params("content", mOverallPublishFriendsCircleEt.text.toString())
                .params("cover_id", coverId)
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException) {
                        showToast("发布失败")
                    }

                    override fun onSuccess(libMyResult5: String) {
                        showToast("发布成功，请等待审核")
                        finish()
                    }

                })
    }
    private fun luban(entity: OverallPublishEntity){
        Log.e("luban --- ", FileUtil.getFileOrFilesSize(entity.pic.absolutePath, FileUtil.SIZETYPE_KB).toString())
        Luban.with(this)
                .load(entity.pic)//原图
                .ignoreBy(100)//多大不压缩
                .setTargetDir(cacheDir.path)//缓存压缩图片路径
//                .filter(CompressionPredicate { path -> !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif")) })
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
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openAlbum() {
        callAlbum(10 - mList.size)
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
