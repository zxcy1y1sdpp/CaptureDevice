package app.jietuqi.cn.ui.activity

import android.Manifest
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.text.method.DigitsKeyListener
import android.util.Log
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.callback.DeleteListener
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.ProblemReportEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.ProblemReportAdapter
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.util.FileUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import com.zhihu.matisse.Matisse
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.body.UIProgressResponseCallBack
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.utils.HttpLog
import kotlinx.android.synthetic.main.activity_report_problem.*
import permissions.dispatcher.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

/**
 * 作者： liuyuanbo on 2018/10/25 15:54.
 * 时间： 2018/10/25 15:54
 * 邮箱： 972383753@qq.com
 * 用途： 问题反馈的页面
 */
@RuntimePermissions
class OverallProblemReportActivity : BaseOverallInternetActivity(), DeleteListener {

    /**
     * 反馈的图片
     */
    private var mPicList = arrayListOf<ProblemReportEntity>()
    private var mAdapter: ProblemReportAdapter? = null
    private var mContactWay = "wx"

    override fun setLayoutResourceId() = R.layout.activity_report_problem

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("问题反馈")
        mAdapter = ProblemReportAdapter(mPicList, this)
        mRecyclerView.adapter = mAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.IMAGE_SELECT ->{
                    mAlbumList = Matisse.obtainResult(data)
                    var entity: ProblemReportEntity
                    for (i in 0 until mAlbumList.size) {
                        entity = ProblemReportEntity()
                        entity.pic = mFiles[i]
                        mPicList.add(entity)
                        var picEntity: ProblemReportEntity
                        for (j in mPicList.indices) {
                            picEntity = mPicList[j]
                            picEntity.position = j
                            if (picEntity.uploadStatus == 0){
                                luban(picEntity)
                            }
                        }
                    }
                    mAdapter?.notifyDataSetChanged()
                    if (mPicList.size >= 3){
                        mSelectPicsIv.visibility = View.GONE
                    }
                    mFiles.clear()
                }
            }
        }
    }
    override fun initViewsListener() {
        mSelectPicsIv.setOnClickListener(this)
        mSuggestionTv.setOnClickListener(this)
        mFunProblemTv.setOnClickListener(this)
        mOverallReportProblemSubmitBtn.setOnClickListener(this)
        mWechatTv.setOnClickListener(this)
        mQQTv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatTv ->{
                mWechatTv.setBackgroundResource(R.drawable.report_problem1)
                mQQTv.setBackgroundResource(R.drawable.report_problem2)
                mWechatTv.setTextColor(ContextCompat.getColor(this, R.color.white))
                mQQTv.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
                mContactWay = "wx"
                mOverallReportProblemContactEt.keyListener = DigitsKeyListener.getInstance(resources.getString(R.string.rule_text))
            }
            R.id.mQQTv ->{
                mWechatTv.setBackgroundResource(R.drawable.report_problem3)
                mQQTv.setBackgroundResource(R.drawable.report_problem4)
                mWechatTv.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
                mQQTv.setTextColor(ContextCompat.getColor(this, R.color.white))
                mContactWay = "qq"
                mOverallReportProblemContactEt.keyListener = DigitsKeyListener.getInstance("0123456789")
            }
            R.id.mSelectPicsIv ->{
                openAlbumWithPermissionCheck()
            }
            R.id.mSuggestionTv ->{
                OtherUtil.changeReoprtProblemBtnBg(this, mSuggestionTv, mFunProblemTv)
                mOverallReportProblemTypeLayout.tag = "yijian"
            }
            R.id.mFunProblemTv ->{
                OtherUtil.changeReoprtProblemBtnBg(this, mFunProblemTv, mSuggestionTv)
                mOverallReportProblemTypeLayout.tag = "gongneng"
            }
            R.id.mOverallReportProblemSubmitBtn ->{
                if (TextUtils.isEmpty(mOverallReportProblemEt.text.toString().trim())){
                    showToast("请描述您的问题")
                    return
                }else{
                    report()
                }
            }
        }
    }
    override fun delete(position: Int) {
        mPicList.removeAt(position)
        mAdapter?.notifyItemRemoved(position)
        mSelectPicsIv.visibility = View.VISIBLE
    }
    private fun uploadPics(entity: ProblemReportEntity){
        HttpLog.e("未上传前： " + entity.toString())
        val mUIProgressResponseCallBack = object : UIProgressResponseCallBack() {
            override fun onUIResponseProgress(bytesRead: Long, contentLength: Long, done: Boolean) {
                val progress = (bytesRead * 100 / contentLength).toInt()
                entity.progress = progress
                if (done) {//完成
                }
                mAdapter?.notifyItemChanged(entity.position)
            }
        }
        EasyHttp.post(HttpConfig.UPLOAD)
                .params("way", "picture")
                .params("file", entity.pic, entity.pic.name, mUIProgressResponseCallBack)
                .execute(object : CallBackProxy<OverallApiEntity<ProblemReportEntity>, ProblemReportEntity>(object : SimpleCallBack<ProblemReportEntity>() {
                    override fun onSuccess(t: ProblemReportEntity) {
                        entity.id = t.id
                        entity.progress = 100
                        entity.uploadStatus = 2
                        mAdapter?.notifyItemChanged(entity.position)
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
    /**
     * 点赞/取消点赞
     */
    private fun report(){
        val sb = StringBuffer()
        var coverId = ""
        if (mPicList.size > 1){
            var entity: ProblemReportEntity
            for (i in mPicList.indices) {
                entity = mPicList[i]
                if (i != mPicList.size - 1){
                    sb.append(entity.id).append(",")
                }
            }
            coverId = sb.deleteCharAt(sb.length - 1).toString()
        }
        val post = EasyHttp.post(HttpConfig.INDEX)

        post.params("way", "guestbook")
                .params("content", mOverallReportProblemEt.text.toString())
                .params("classify", mOverallReportProblemTypeLayout.tag.toString())
                .params("cover_id", coverId)
                .params("os", "android")
                .params("uid", UserOperateUtil.getUserId())
                .params(mContactWay, mOverallReportProblemContactEt.text.toString().trim())
        post.execute(object : SimpleCallBack<String>() {
            override fun onStart() {
                super.onStart()
                showLoadingDialog()
            }
            override fun onError(e: ApiException) {}
            override fun onSuccess(t: String) {
                SweetAlertDialog(this@OverallProblemReportActivity, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("感谢您的反馈")
                        .setContentText("有结果会第一时间通知您")
                        .setConfirmText("知道了")
                        .setConfirmClickListener { sweetAlertDialog ->
                            sweetAlertDialog.dismissWithAnimation()
                            finish()
                        }.show()
            }
        })
    }
    private fun luban(entity: ProblemReportEntity){
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
        callAlbum(3 - mPicList.size)
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
