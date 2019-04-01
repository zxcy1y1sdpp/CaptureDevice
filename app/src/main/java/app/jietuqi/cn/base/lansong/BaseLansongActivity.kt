package app.jietuqi.cn.base.lansong

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.support.v4.app.ActivityCompat
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseActivity
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.util.FileUtil
import app.jietuqi.cn.widget.MyGlideEngine
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import com.yalantis.ucrop.model.AspectRatio
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 作者： liuyuanbo on 2019/3/30 11:09.
 * 时间： 2019/3/30 11:09
 * 邮箱： 972383753@qq.com
 * 用途： 蓝松sdk需要用到的自己编写的基类
 */
abstract class BaseLansongActivity : BaseActivity(){
    private var mWidth = 0f
    private var mHeight = 0f
    /**
     * 准备裁剪
     */
    private fun startCropFroLansong(uri: Uri) {

        //裁剪后保存到文件中
        val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        val date = Date()
        val imageName = simpleDateFormat.format(date)
        var destinationUri: Uri
        destinationUri = Uri.fromFile(File(cacheDir, "$imageName.jpeg"))

        var uCrop = UCrop.of(uri, destinationUri)
//        UCrop.of(uri, destinationUri).useSourceImageAspectRatio().start(this, RequestCode.CROP_IMAGE)
        //是否能调整裁剪框

        //初始化UCrop配置
        var options: UCrop.Options  = UCrop.Options()
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL)
        //是否隐藏底部容器，默认显示
//        options.setHideBottomControls(true)
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary))
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimary))
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(true)
        options.setAspectRatioOptions(2,
                AspectRatio("1:1", 1f, 1f),
                AspectRatio("3:4", 3f, 4f),
                AspectRatio("原始比例",
                        mWidth, mHeight),
                AspectRatio("3:2", 3f, 2f),
                AspectRatio("16:9", 16f, 9f))
//        AspectRatio("1:1", 893f, 515f),
        options.setShowCropFrame(true)
        //UCrop配置
        uCrop.withOptions(options)
        uCrop.start(this, RequestCode.CROP_IMAGE)
    }

    fun callAlbumForLansong(maxCount: Int = 1, needCrop: Boolean = false, width: Float = 893f, height: Float = 515f){
        mMaxCount = maxCount
        mNeedCrop = needCrop
        mWidth = width
        mHeight = height
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .showSingleMediaType(true)
//                .choose(MimeType.ofAll())
                .theme(R.style.Matisse_Dracula)// 黑色背景
//                        .capture(true)  // 开启相机，和 captureStrategy 一并使用否则报错
//                        .captureStrategy(CaptureStrategy(true,"app.jietuqi.cn.nougat")) // 拍照的图片路径
                .countable(true)
                .maxSelectable(maxCount)
//                        .addFilter(GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(MyGlideEngine())
                .forResult(RequestCode.IMAGE_SELECT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.IMAGE_SELECT ->{
                if (null != data){
                    mAlbumList = Matisse.obtainResult(data)
                    if (mNeedCrop){
                        startCropFroLansong(mAlbumList[0])
                    }else{
                        for (i in mAlbumList.indices) {
                            var file = FileUtil.getFileByUri(mAlbumList[i], this)
                            mFiles.add(file)
                        }
                    }
                }else{
                    mFiles.add(File(""))
                }
            }
            RequestCode.CROP_IMAGE ->{
                if (null != data){
                    mFinalPicUri = data?.let { UCrop.getOutput(it) }
                    var path = FileUtil.getFilePathByUri(this, mFinalPicUri)
                    mFinalCropFile = File(path) //转换为File
                }
            }
        }
    }
}
