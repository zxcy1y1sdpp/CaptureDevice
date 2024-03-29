package app.jietuqi.cn.lansong.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.lansong.BaseLansongActivity
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.lansong.entity.LansongImgEntity
import app.jietuqi.cn.lansong.ui.adapter.ChoiceImageAdapter
import app.jietuqi.cn.lansong.util.DemoUtil
import app.jietuqi.cn.lansong.widget.LSOProgressDialog
import com.lansosdk.LanSongAe.*
import com.lansosdk.box.BitmapLayer
import com.lansosdk.videoeditor.AudioPadExecute
import com.lansosdk.videoeditor.CopyFileFromAssets.copyAssets
import com.lansosdk.videoeditor.DrawPadAEExecute
import com.lansosdk.videoeditor.LanSongFileUtil
import kotlinx.android.synthetic.main.activity_lansong.*
import java.io.File
import java.util.*

/**
 * 作者： liuyuanbo on 2019/3/30 10:47.
 * 时间： 2019/3/30 10:47
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class LansongActivity : BaseLansongActivity() {
    private var dstPath: String? = null
    private var bgVideo = ""
    //    private var mvColorPath = ""
//    private var mvMaskPath = ""
//    private var jsonPath = ""
    private var addAudioPath = ""
    //    private var mDrawableList = arrayListOf<LSOAeDrawable>()
//    internal var mDrawableArray: Array<LSOAeDrawable>? = null
    lateinit var drawable1: LSOAeDrawable

    private var bmpImage0: Bitmap? = null
    private var bmpImage1: Bitmap? = null
    private var bmpImage2: Bitmap? = null
    private var bmpImage3: Bitmap? = null
    private var bmpImage4: Bitmap? = null
    private var bmpImage5: Bitmap? = null
    private var videoImage0: String = ""
    private var textDelegate: LSOTextDelegate? = null
    private lateinit var execute: DrawPadAEExecute
    private lateinit var bitmapLayer: BitmapLayer
    private var progressDialog: LSOProgressDialog? = null
    private var mWidth = 0f
    private var mHeight = 0f
    private var mType = 0

    /**
     * 当前选中替换图片的位置
     */
    private var mPosition = 0
    private var mLansongImgList = arrayListOf<LansongImgEntity>()
    private var mAdapter : ChoiceImageAdapter? = null
    override fun setLayoutResourceId() = R.layout.activity_lansong

    override fun needLoadingView() = false
    override fun initAllViews() {
        //获取到拷贝的路径
//        bgVideo = copyAssets(applicationContext, "rechenggouEx.mp4")
        /**/
    }
    override fun initViewsListener() {
        mChangeBgBtn.setOnClickListener(this)
        mCompoundBtn.setOnClickListener(this)
        mTestBtn.setOnClickListener(this)
        mTest2Btn.setOnClickListener(this)
        mChoiceImageRv.addOnItemTouchListener(object : OnRecyclerItemClickListener(mChoiceImageRv) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                mPosition = vh.adapterPosition
                val entity = mLansongImgList[mPosition]
//                callAlbumForLansong(width = entity.width, height = entity.height)
                callAlbumForLansong(needCrop = true, width = entity.width, height = entity.height)
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mChangeBgBtn ->{

            }
            R.id.mCompoundBtn ->{
                if (mType == 0){
                    parseJsonStartAE(copyAssets(applicationContext, "rechenggou.json"),
                            copyAssets(applicationContext, "rechegngou_mvColor.mp4"),
                            copyAssets(applicationContext, "rechegngou_mvMask.mp4"))
                }else{
                    parseJsonStartAE(copyAssets(applicationContext, "haokan.json"),
                            copyAssets(applicationContext, "haokan_mvColor.mp4"),
                            copyAssets(applicationContext, "haokan_mvMask.mp4"))
                }
            }
            R.id.mTestBtn ->{
                mType = 0
                getListData(copyAssets(applicationContext, "rechenggou.json"))
            }
            R.id.mTest2Btn ->{
                mType = 1
                getListData(copyAssets(applicationContext, "haokan.json"))
            }
        }
    }

    private fun getListData(json: String){
        LSOLoadAeJsons.loadAsync(applicationContext, arrayOf(json)) { drawables ->
            if (drawables != null && drawables.isNotEmpty()) {
                //分析得到的结果.因为只传递一个jsonPath; 故得到是一个drawable, 如有多个,则依次得到0,1,2;
                var entity: LansongImgEntity
                val maps: Map<String, LSOAeImage> = drawables[0].jsonImages as Map<String, LSOAeImage>
                for (key in maps.keys) {
                    var asset = maps[key]
                    asset?.width?.let { mWidth = it.toFloat() }
                    asset?.height?.let { mHeight = it.toFloat() }
                    Log.i("TAG", "asset: " + asset?.fileName + " id:" + asset?.id + " width:" + asset?.width + "height:" + asset?.height)
                    entity = LansongImgEntity()
                    entity.jsonImgId = asset?.id
                    entity.jsonImgName = asset?.fileName
                    asset?.width?.let { entity.width = it.toFloat() }
                    asset?.height?.let { entity.height = it.toFloat() }
                    mLansongImgList.add(entity)
                }
                mAdapter = ChoiceImageAdapter(mLansongImgList)
                mChoiceImageRv.adapter = mAdapter
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
             RequestCode.CROP_IMAGE ->{
                 if (null != data){
                     val entity = mLansongImgList[mPosition]
                     entity.bitmap = BitmapFactory.decodeFile(mFinalCropFile?.absolutePath.toString())
                     entity.imgFile = mFinalCropFile
                     entity.imgFilePath = mFinalCropFile?.absolutePath
                     mFinalCropFile = File("")
                     mAdapter?.notifyItemChanged(mPosition)
                 }
             }
            /*RequestCode.IMAGE_SELECT ->{
                if (null != data){
                    val entity = mLansongImgList[mPosition]
                    entity.bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(mFiles[0].absolutePath.toString()) ,entity.width.toInt(),entity.height.toInt(), true)
//                    entity.bitmap = BitmapFactory.decodeFile(mFiles[0].absolutePath.toString())
                    entity.imgFile = mFiles[0]
                    entity.imgFilePath = mFiles[0].absolutePath
                    mAdapter?.notifyItemChanged(mPosition)
                    mFiles.clear()
                }
            }*/
        }
    }

    private fun parseJsonStartAE(jsonPath: String, mvColorPath: String, mvMaskPath: String) {
        if (jsonPath != null) {  //有json
            LSOLoadAeJsons.loadAsync(applicationContext, arrayOf(jsonPath)) { drawables ->
                if (drawables != null && drawables.isNotEmpty()) {
                    //分析得到的结果.因为只传递一个jsonPath; 故得到是一个drawable, 如有多个,则依次得到0,1,2;
                    drawable1 = drawables[0]

                    //设置json的文字用到的字体路径
                    setJsonFontPath()
                    //拿到json中的文字和图片;
//                    val texts<LSOAeText> = drawable1.jsonTexts
                    var texts = drawable1.jsonTexts as ArrayList<LSOAeText>
                    for (text in texts) {
                        Log.e("LSTODO", "LSOAeText: is :" + text.text)
                    }

                    val maps: Map<String, LSOAeImage> = drawable1.jsonImages as Map<String, LSOAeImage>
//                    for (key in maps.keys) {
//                        val asset = maps[key]
//                        Log.i("TAG", "asset: " + asset?.fileName + " id:" + asset?.id + " width:" + asset?.width + "height:" + asset?.height)
//                    }

                    var entity: LansongImgEntity
                    for (i in mLansongImgList.indices){
                        entity = mLansongImgList[i]
                        //给json中的每个图片Id替换对应的图片对象.
                        drawable1.updateBitmap(entity.jsonImgId, entity.bitmap, true)
                    }
                    //给json中的每个图片Id替换对应的图片对象.
//                    drawable1.updateBitmap("image_0", bmpImage0, false)
//                    drawable1.updateBitmap("image_1", bmpImage1)
//                    drawable1.updateBitmap("image_2", bmpImage2)
//                    drawable1.updateBitmap("image_3", bmpImage3)
//                    drawable1.updateBitmap("image_4", bmpImage4)
//                    drawable1.updateBitmap("image_5", bmpImage5)

                    //图片替换为视频;
                    if (videoImage0 != null) {
                        drawable1.updateVideoBitmap("image_0", videoImage0)
                    }

                    //替换文字;
                    if (textDelegate != null) {
                        drawable1.textDelegate = textDelegate
                    }
                    startAE(mvColorPath, mvMaskPath)
                }
            }
        } else {
            showToast("背景视频或jsonPath出错:jsonPath :$jsonPath bgVideo$bgVideo")
        }
    }

    /**
     * 处理图片
     * @param bm 所要转换的bitmap
     * @param newWidth 新的宽
     * @param newHeight 新的高
     * @return 指定宽高的bitmap
     */
    fun zoomImg(bm: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        // 获得图片的宽高
        val width = bm.width
        val height = bm.height
        // 计算缩放比例
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 取得想要缩放的matrix参数
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true)
    }
    /**
     * 开始执行Ae
     *
     * 步骤是:
     * 1.一层一层的把各种文件增加进去, 先增加的放最底层, 后增加的放上层;(谁放第一层,谁放第二层,这个是你们配置文件解析得到)
     * 2. 设置各种监听.
     * 3, 开始运行.
     */
    private fun startAE(mvColorPath: String, mvMaskPath: String) {
        dstPath = LanSongFileUtil.createMp4FileInBox()

        progressDialog = LSOProgressDialog()
        progressDialog?.show(this)

        //创建Ae容器
        if (LanSongFileUtil.fileExist(bgVideo)) {
            execute = DrawPadAEExecute(applicationContext, bgVideo, dstPath) //如果有背景视频,则增加一层背景视频;
        } else {
            execute = DrawPadAEExecute(applicationContext, dstPath)
        }

        //增加一层:ae图层
        execute.addAeLayer(drawable1)
        //增加一层: mv图层;
        if (mvColorPath != null && mvMaskPath != null) {
            execute.addMVLayer(mvColorPath, mvMaskPath)
        }

        /**
         * 进度/完成/错误回调, 开始
         */
        execute.setDrawPadProgressListener { v, currentTimeUs -> showProgress(currentTimeUs) }

        execute.setDrawPadCompletedListener {
            execute.release()
            startPreview()
        }
        execute.setDrawPadErrorListener { d, what ->
            hideProgressDialog()
            execute.cancel()
            DemoUtil.showHintDialog(this, "AE合成错误,请联系我们!")
        }

        execute.setEncodeBitrate(8 * 1024 * 1024)  //设置码率(可选)

        addBitmapLayer()

        if (!execute.start()) {
            hideProgressDialog()
            DemoUtil.showHintDialog(this, "AE合成错误,请联系我们!")
        } else {
            bitmapLayer.setPosition(bitmapLayer.padWidth - bitmapLayer.layerWidth / 2.0f, bitmapLayer.layerHeight / 2.0f)
        }
    }

    private fun startPreview() {
        if (addAudioPath != null) {
            dstPath?.let { addAudio(it) }

        } else {
            hideProgressDialog()
            val intent = Intent(this, VideoPlayerActivity::class.java)
            intent.putExtra("videopath", dstPath)
            startActivity(intent)
        }
    }
    private fun showProgress(currentTimeUs: Long) {
        if (progressDialog != null && execute != null) {
            val time = currentTimeUs.toFloat() / execute.duration.toFloat()
            val b = Math.round(time * 100)
            if (b < 100) {
                progressDialog?.setProgress(b)
            }
        }
    }

    private fun hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog?.release()
            progressDialog = null
        }
    }

    private fun addBitmapLayer() {  //增加一个logo信息;
//        if (bitmapLayer == null) {
        val bmp = BitmapFactory.decodeResource(resources, R.drawable.head_default)
        bitmapLayer = execute.addBitmapLayer(bmp)
//        }
    }

    private var audioExecute: AudioPadExecute? = null
    private fun addAudio(srcpath: String) {
        audioExecute = AudioPadExecute(application, srcpath)
        audioExecute?.addAudioLayer(addAudioPath, true, 1.0f)
        audioExecute?.setOnAudioPadCompletedListener { path ->
            audioExecute?.release()
            audioExecute = null

            hideProgressDialog()
            val intent = Intent(this, VideoPlayerActivity::class.java)
            intent.putExtra("videopath", path)
            startActivity(intent)
            LanSongFileUtil.deleteFile(srcpath)
        }
        audioExecute?.start()
    }
    //设置字体;
    private fun setJsonFontPath() {

        /**
         * 设置字体监听, json在处理过程中, 会在第一次找字体的时候, 调用这个监听, 从而您设置json中字体文件的绝对路径
         * 如果您没有设置字体,则默认用系统字体, (系统字体可能不是您要的效果)
         */
        //        drawable1.setFontAssetListener( new LSOFontAssetListener(){
        //            public String getFontPath(String fontFamily) {
        //                Log.e("LSTODO", "------getfont path: " + fontFamily);  //返回的是字体在json中的字符串;
        //                return fontPath;  //返回这个字体名字对应的字体文件绝对路径
        //            }
        //        });
    }

}
