package dasheng.com.capturedevice.base

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.constant.RequestCode
import dasheng.com.capturedevice.util.FileUtil
import dasheng.com.capturedevice.widget.MyGlideEngine
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @作者：liuyuanbo
 * *
 * @时间：2018/9/30
 * *
 * @邮箱：972383753@qq.com
 * *
 * @用途：所有Activity的基类
 */

abstract class BaseActivity : AppCompatActivity(), View.OnClickListener {
    /*********************************************** 选择图片相关 *****************************************/
    /**
     * 选择的图片
     */
    var mAlbumList: List<Uri> = mutableListOf()
    /**
     * 最大选择的图片个数
     */
    var mMaxCount = 1
    /**
     * 最后裁剪得到的图片的File
     */
    var mFinalCropFile: File? = null
    /**
     * 最后裁剪得到的图片的File
     */
    var mFinalPicUri: Uri? = null
    /**
     * 选图片单张选的时候返回的file
     */
    var mFile: File? = null
    /**
     * 是否需要裁剪图片的功能
     */
    var mNeedCrop = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeSetContentView()
        if (0 != setLayoutResourceId()){
            setContentView(setLayoutResourceId())
        }
        setContextS()
        initAllViews()
        setWithSavedInstanceState(savedInstanceState)
        initViewsListener()
        //        initLoadingView();
        getAttribute(intent)
        //        onLoad();
        //        onLoadWithResume();
    }

    /**
     * 一些在setContent之前才生效的方法
     * 类似于设置全屏

     * @return
     */
    protected fun beforeSetContentView() {}

    /**
     * 设置Context

     * @return
     */
    protected abstract fun setLayoutResourceId(): Int

    /**
     * 简化的findViewById
     * @param id
     * *
     * @param <T>
     * *
     * @return
    </T> */
    protected fun <T : View> findView(id: Int): T {
        return super.findViewById<View>(id) as T
    }

    /**
     * 设置和savedInstanceState相关的东西

     * @return
     */
    protected fun setWithSavedInstanceState(savedInstanceState: Bundle?) {}

    /**
     * 设置根布局资源id

     * @return
     */
    protected fun setContextS() {}

    /**
     * 设置需不需要加载动画

     * @return
     */
    protected abstract fun needLoadingView(): Boolean

    /**
     * 初始化控件的方法
     */
    protected abstract fun initAllViews()

    /**
     * 设置控件的事件 －－ 点击事件之类的
     */
    protected abstract fun initViewsListener()

    /**
     * 获取传递参数
     */
    protected open fun getAttribute(intent: Intent) {}

    override fun onClick(v: View) {
        when(v.id){
            R.id.overAllBackIv -> finish()
        }
    }

    /**
     * 设置页面的标题
     * @param title
     * @param type
     *        0 -- 只有一个返回键和标题
     *        1 -- 只有标题
     */
    protected fun setTitle(title: String, type: Int = 0) {
        val titleTv = findViewById<TextView>(R.id.overAllTitleTv)
        val iv = findViewById<ImageView>(R.id.overAllBackIv)
        when(type){
            1 ->{
                iv.visibility = View.GONE
            }
            2 ->{
            }
        }
        iv.setOnClickListener(this)
        titleTv.setOnClickListener(this)
        titleTv.text = title
    }

    fun callAlbum(maxCount: Int = 1, needCrop: Boolean = false){
        mMaxCount = maxCount
        mNeedCrop = needCrop
        Matisse.from(this)
                .choose(MimeType.ofAll())
                .theme(R.style.Matisse_Dracula)// 黑色背景
//                        .capture(true)  // 开启相机，和 captureStrategy 一并使用否则报错
//                        .captureStrategy(CaptureStrategy(true,"dasheng.com.capturedevice.nougat")) // 拍照的图片路径
                .countable(true)
                .maxSelectable(maxCount)
//                        .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(MyGlideEngine())
                .forResult(RequestCode.IMAGE_SELECT)
    }
    /**
     * 准备裁剪
     */
    fun startCrop(uri: Uri) {
        //裁剪后保存到文件中
        val sourceUri = uri
        val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        val date = Date()
        val imageName = simpleDateFormat.format(date)
        var destinationUri: Uri
        destinationUri = Uri.fromFile(File(cacheDir, imageName + ".jpeg"))
        UCrop.of(sourceUri, destinationUri).start(this, RequestCode.CROP_IMAGE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.IMAGE_SELECT ->{
                    mAlbumList = Matisse.obtainResult(data)
                    if (mNeedCrop){
                        startCrop(mAlbumList[0])
                    }else{
                        mFile = FileUtil.getFileByUri(mAlbumList[0], this)
                    }
                }
                RequestCode.CROP_IMAGE ->{
                    mFinalPicUri = data?.let { UCrop.getOutput(it) }
                    var path = FileUtil.getFilePathByUri(this, mFinalPicUri)
                    mFinalCropFile = File(path) //转换为File
                }
            }
        }
    }
}
