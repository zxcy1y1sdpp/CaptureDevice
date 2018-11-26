package app.jietuqi.cn.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import app.jietuqi.cn.AppManager
import app.jietuqi.cn.R
import app.jietuqi.cn.constant.ConditionFinal
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.entity.JsonBean
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallIndustryEntity
import app.jietuqi.cn.util.*
import app.jietuqi.cn.widget.MyGlideEngine
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.google.gson.Gson
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.request.PostRequest
import com.zhouyou.http.widget.ProgressUtils
import org.json.JSONArray
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

abstract class BaseActivity : AppCompatActivity(), View.OnClickListener, OnOptionsSelectListener {
    /**
     * 0 -- 性别选择
     * 1 -- 行业类别选择
     * 2 -- 地区选择
     * 3 -- 群类型选择
     * 4 -- 群人数
     */
    var mPickerType = 0
    /**
     * 是否展示全部
     * 人数不限
     * 数量不限
     */
    var mShowAll = true
    /*********************************************** 选择图片相关 *****************************************/
    /**
     * 选择的图片
     */
    var mAlbumList: List<Uri> = mutableListOf()
    /**
     * 最大选择的图片个数
     */
    private var mMaxCount = 1
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
    var mFiles = arrayListOf<File>()
    /**
     * 是否需要裁剪图片的功能
     */
    private var mNeedCrop = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeSetContentView()
        if (0 != setLayoutResourceId()){
            setContentView(setLayoutResourceId())
        }
        AppManager.getInstance().addActivity(this)
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
    open fun beforeSetContentView() {}

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
    private fun setWithSavedInstanceState(savedInstanceState: Bundle?) {}

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
    protected fun registerEventBus(){
        EventBusUtil.register(this)
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.overAllBackIv -> finish()
            R.id.overAllRightTitleTv -> {}
        }
    }

    /**
     * 设置页面的标题
     * @param title
     * @param type
     *        0 -- 只有一个返回键和标题
     *        1 -- 只有标题
     *        2 -- 包含右侧标题
     */
    protected fun setTitle(title: String, type: Int = 0, rightTitle: String = "") {
        val titleTv = findViewById<TextView>(R.id.overAllTitleTv)
        val iv = findViewById<ImageView>(R.id.overAllBackIv)
        if (!TextUtils.isEmpty(rightTitle)){
            val rightTitleTv = findViewById<TextView>(R.id.overAllRightTitleTv)
            rightTitleTv.text = rightTitle
            rightTitleTv.setOnClickListener(this)
            rightTitleTv.visibility = View.VISIBLE
        }
        when(type){
            1 ->{
                iv.visibility = View.GONE
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
    /**
     * 准备裁剪
     */
    private fun startCrop(uri: Uri) {
        //裁剪后保存到文件中
        val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        val date = Date()
        val imageName = simpleDateFormat.format(date)
        var destinationUri: Uri
        destinationUri = Uri.fromFile(File(cacheDir, "$imageName.jpeg"))
        UCrop.of(uri, destinationUri).start(this, RequestCode.CROP_IMAGE)
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
                        val list = ArrayList<String>()
                        for (i in list.indices) {

                        }
                        for (i in mAlbumList.indices) {
                            var file = FileUtil.getFileByUri(mAlbumList[i], this)
                            mFiles.add(file)
                        }
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
    /**
     * 初始化生意类别选择器
     * @param type 0 -- 性别, 1 -- 行业, 4 -- 群人数
     */
    fun initSingleOneOptionPicker(type: Int = 0, showAllType: Boolean = true) {//条件选择器初始化
        val list = arrayListOf<String>()
        list.addAll(ConditionFinal.SEXUALITY)
        var optionPickerView: OptionsPickerView<String>
        mPickerType = type
        mShowAll = showAllType
        var optionsPickerBuilder = OptionsPickerBuilder(this, this)
                .setContentTextSize(20)//设置滚轮文字大小
                .setLineSpacingMultiplier(2.0f)
                .setSelectOptions(0, 1)//默认选中项
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。

        when(type){
            0 ->{//性别选择
                optionsPickerBuilder.setTitleText("性别选择")
                if (!showAllType){
                    list.removeAt(0)
                }
            }
            1 ->{
                optionsPickerBuilder.setTitleText("行业类型选择")
                list.clear()
                list.addAll(ConditionFinal.BUSINESSTTYP)
            }
            4 ->{
                optionsPickerBuilder.setTitleText("选择群人数")
                list.clear()
                list.addAll(ConditionFinal.GROUPPEOPLE)
                if (!showAllType){
                    list.removeAt(0)
                }
            }
        }
        optionPickerView = optionsPickerBuilder.build()
        optionPickerView?.setPicker(list)//一级选择器
        optionPickerView.show(View(this))
    }
    /**
     * 初始化地区选择器
     * @param type
     *        0 -- 性别，1 -- 行业，2 -- 地区
     * @param itemCount : 需要几个条件
     */
    private var options1Items = ArrayList<String>()
    private var provinceList = arrayListOf<String>()
    val options2Items = ArrayList<ArrayList<String>>()
    val options3Items = ArrayList<ArrayList<ArrayList<String>>>()

    fun initAreaOptions(showAllType: Boolean = true) {//条件选择器初始化
        mPickerType = 2
        mShowAll = showAllType
        initJsonData()
        var optionPickerView: OptionsPickerView<String>
        var optionsPickerBuilder = OptionsPickerBuilder(this, this)
                .setContentTextSize(20)//设置滚轮文字大小
                .setLineSpacingMultiplier(2.0f)
                .setSelectOptions(0, 1)//默认选中项
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setTitleText("地区选择")
        optionPickerView = optionsPickerBuilder.build()
        optionPickerView.setPicker(options1Items, options2Items as List<MutableList<String>>?)//三级选择器
        optionPickerView.show(View(this))
    }

    /**
     * 1 -- 行业类别
     * 3 -- 群类型
     */
    fun initIndustryOrGroup(type: Int = 1) {//条件选择器初始化
        mPickerType = type
        var optionPickerView: OptionsPickerView<OverallIndustryEntity>
        var optionsPickerBuilder = OptionsPickerBuilder(this, this)
                .setContentTextSize(20)//设置滚轮文字大小
                .setLineSpacingMultiplier(2.0f)
                .setSelectOptions(0, 1)//默认选中项
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setTitleText("地区选择")
        optionPickerView = optionsPickerBuilder.build()
        if (type == 1){
            optionPickerView.setPicker(UserOperateUtil.getIndustrys())
        }else{
            optionPickerView.setPicker(UserOperateUtil.getGroupType())
        }
        optionPickerView.show(View(this))
    }

    private fun initJsonData() {//解析数据
        options1Items.clear()
        options2Items.clear()
        options3Items.clear()
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         */
        val jsonData = OtherUtil.getJson("province.json", this)//获取assets目录下的json文件数据
        val jsonBean = parseData(jsonData)//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        for (i in jsonBean.indices) {//遍历省份
            options1Items.add(jsonBean[i].name)
            val citylist = ArrayList<String>()//该省的城市列表（第二级）
            val provinceArealist = ArrayList<ArrayList<String>>()//该省的所有地区列表（第三极）

            for (c in 0 until jsonBean[i].cityList.size) {//遍历该省份的所有城市
                val cityName = jsonBean[i].cityList[c].name
                citylist.add(cityName)//添加城市
                val cityAreaList = ArrayList<String>()//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean[i].cityList[c].area == null || jsonBean[i].cityList[c].area.size == 0) {
                    cityAreaList.add("")
                } else {
                    cityAreaList.addAll(jsonBean[i].cityList[c].area)
                }
                provinceArealist.add(cityAreaList)//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(citylist)

            /**
             * 添加地区数据
             */
            options3Items.add(provinceArealist)
        }
        if (!mShowAll){
            options1Items.removeAt(0)
            options2Items.removeAt(0)
            options3Items.removeAt(0)
        }
    }

    private fun parseData(result: String): ArrayList<JsonBean> {//Gson 解析
        val detail = ArrayList<JsonBean>()
        try {
            val data = JSONArray(result)
            val gson = Gson()
            for (i in 0 until data.length()) {
                val entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean::class.java)
                detail.add(entity)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return detail
    }
    override fun onOptionsSelect(options1: Int, options2: Int, options3: Int, v: View?) {
        if (mPickerType == 0){
            if (!mShowAll){
                v?.tag = ConditionFinal.SEXUALITY[options1 + 1]
            }else{
                v?.tag = ConditionFinal.SEXUALITY[options1]
            }
        }
        if (mPickerType == 1){//行业类别
            v?.tag = UserOperateUtil.getIndustrys()[options1]
        }
        if (mPickerType == 2){
            if (!mShowAll){
//                v?.tag = options1Items[options1 + 1] + " " + options2Items[options1 + 1][options2 + 1]
                v?.tag = options1Items[options1] + " " + options2Items[options1][options2]
            }else{
                v?.tag = options1Items[options1] + " " + options2Items[options1][options2]
            }

        }
        if (mPickerType == 3){//群类型
            v?.tag = UserOperateUtil.getGroupType()[options1]
        }
        if (mPickerType == 4){
            if (!mShowAll){
                v?.tag = ConditionFinal.GROUPPEOPLE[options1 + 1]
            }else{
                v?.tag = ConditionFinal.GROUPPEOPLE[options1]
            }
        }
    }
    private var mLoadingDialog: ProgressUtils? = null

    /**
     * 可取消的加载狂
     * @param message : 展示的消息
     * @param canCancel：是否可以取消
     */
    fun showLoadingDialog(message: String = "请稍后...", canCancel: Boolean = true) {
        ProgressUtils.showProgressDialog(message, this, canCancel)
    }

    fun dismissLoadingDialog() {
        if (ProgressUtils.mProgressDialog != null){
            ProgressUtils.cancleProgressDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoadingDialog()
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
    fun refreshUserInfo(){
        var request: PostRequest = EasyHttp.post(HttpConfig.USERS).params("way", "id").params("id", UserOperateUtil.getUserId())
        request.execute(object : CallBackProxy<OverallApiEntity<OverallUserInfoEntity>, OverallUserInfoEntity>(object : SimpleCallBack<OverallUserInfoEntity>() {
            override fun onError(e: ApiException) {
                e.message?.let { showToast(it) }
            }

            override fun onSuccess(t: OverallUserInfoEntity) {
                refreshUser(t)
                SharedPreferencesUtils.saveBean2Sp(t, SharedPreferenceKey.USER_INFO)
            }
        }) {})
    }

    open fun refreshUser(user: OverallUserInfoEntity){}
}
