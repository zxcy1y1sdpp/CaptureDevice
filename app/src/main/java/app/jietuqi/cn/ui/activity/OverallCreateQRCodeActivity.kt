package app.jietuqi.cn.ui.activity

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.widget.ShapeCornerBgView
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.bm.zlzq.utils.ScreenUtil
import kotlinx.android.synthetic.main.activity_overall_create_qrcode.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import permissions.dispatcher.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * 作者： liuyuanbo on 2018/12/22 13:36.
 * 时间： 2018/12/22 13:36
 * 邮箱： 972383753@qq.com
 * 用途： 生成二维码
 */
@RuntimePermissions
class OverallCreateQRCodeActivity : BaseOverallActivity() {
    var mList: ArrayList<Int> = arrayListOf()
    private var mLogoBitmap: Bitmap? = null
    private var mChoiceColor = Color.parseColor("#000000")
    private var mFinalCreateBitmap: Bitmap? = null
    override fun setLayoutResourceId() = R.layout.activity_overall_create_qrcode

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("二维码生成")
        mList.add(Color.parseColor("#ff0000"))
        mList.add(Color.parseColor("#cc00cc"))
        mList.add(Color.parseColor("#33ff00"))
        mList.add(Color.parseColor("#FF7F00"))
        mList.add(Color.parseColor("#FF00FF"))
        mList.add(Color.parseColor("#BFEFFF"))
        mList.add(Color.parseColor("#836FFF"))
        mList.add(Color.parseColor("#000000"))
        mList.add(Color.parseColor("#1E90FF"))
        mList.add(Color.parseColor("#CAFF70"))
        mQRCodeChoiceBgRecyclerView.adapter = CreateQRCodeAdapter()
    }

    override fun initViewsListener() {
        mCreateQRCodeTv.setOnClickListener(this)
        mQRCodeChangeColorTv.setOnClickListener(this)
        mQRCodeCreateLogoTv.setOnClickListener(this)
        mSaveQRCode2Gallery.setOnClickListener(this)
    }

    fun createQRCode(color: Int = Color.parseColor("#000000"), logoBitmap: Bitmap? = null){
        val path = mQRCodePathEd.text.toString().trim()
        if (TextUtils.isEmpty(path)){
            showToast("请输入要生成的地址")
            return
        }
        val width = ScreenUtil.getScreenWidth(this@OverallCreateQRCodeActivity).div(2)
        mFinalCreateBitmap = if (null == logoBitmap){
            width?.let { QRCodeEncoder.syncEncodeQRCode(path, it, color) }
        }else{
            width?.let { QRCodeEncoder.syncEncodeQRCode(path, width, color, logoBitmap)}
        }
        mQRCodeIv.setImageBitmap(mFinalCreateBitmap)
        mQRCodeIv.visibility = View.VISIBLE
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mCreateQRCodeTv ->{
                createQRCode()
            }
            R.id.mQRCodeChangeColorTv ->{
                mQRCodeChoiceBgRecyclerView.visibility = View.VISIBLE
            }
            R.id.mQRCodeCreateLogoTv ->{
                openAlbumWithPermissionCheck()
            }
            R.id.mSaveQRCode2Gallery ->{
                mFinalCreateBitmap?.let { saveBmp2Gallery(it) }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.CROP_IMAGE ->{
                mLogoBitmap = BitmapFactory.decodeFile(mFinalCropFile?.absolutePath, getBitmapOption(2)) //将图片的长和宽缩小味原来的1/2
                createQRCode(mChoiceColor, mLogoBitmap)
            }
        }
    }

    private fun getBitmapOption(inSampleSize: Int): BitmapFactory.Options {
        System.gc()
        val options = BitmapFactory.Options()
        options.inPurgeable = true
        options.inSampleSize = inSampleSize
        return options
    }
    inner class CreateQRCodeAdapter : RecyclerView.Adapter<CreateQRCodeAdapter.Holder1>() {
        override fun onBindViewHolder(holder: Holder1, position: Int) {
            holder.bind(mList[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder1 {
            return Holder1(LayoutInflater.from(parent.context).inflate(R.layout.item_create_qrcode, parent, false))
        }

        override fun getItemCount(): Int {
            return  mList.size
        }

        inner class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
            private var bgView: ShapeCornerBgView = itemView.findViewById(R.id.sCreateQRCodeBv)
            init {
                itemView.findViewById<ShapeCornerBgView>(R.id.sCreateQRCodeBv).setOnClickListener(this)
            }
            fun bind(color: Int) {
                bgView.setBgColor(color)
            }
            override fun onClick(v: View?) {
                mChoiceColor = mList[adapterPosition]
                createQRCode(mChoiceColor, mLogoBitmap)
            }
        }
    }

    /**
     * @param bmp 获取的bitmap数据
     */
    private fun saveBmp2Gallery(bmp: Bitmap) {
        showLoadingDialog("保存中，请稍后。。。")
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            var picName = System.currentTimeMillis().toString()
            var fileName: String? = null
            //系统相册目录
            val galleryPath = (Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator)
            // 声明文件对象
            var file: File? = null
            // 声明输出流
            var outStream: FileOutputStream? = null
            try {
                // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
                file = File(galleryPath, "$picName.jpg")
                // 获得文件相对路径
                fileName = file.toString()
                // 获得输出流，如果文件中有内容，追加内容
                outStream = FileOutputStream(fileName)
                if (null != outStream) {
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream)
                }

            } catch (e: Exception) {
                e.stackTrace
            } finally {
                try {
                    outStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            //通知相册更新
            MediaStore.Images.Media.insertImage(contentResolver, bmp, fileName, null)
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val uri = Uri.fromFile(file)
            intent.data = uri
            sendBroadcast(intent)
            runOnUiThread {
                showToast("图片保存成功")
                dismissLoadingDialog()
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
}
