package app.jietuqi.cn.base

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import app.jietuqi.cn.AppManager
import app.jietuqi.cn.R
import app.jietuqi.cn.constant.*
import app.jietuqi.cn.entity.JsonBean
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.activity.OverallPurchaseVipActivity
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallIndustryEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.*
import app.jietuqi.cn.widget.MyGlideEngine
import app.jietuqi.cn.widget.dialog.ChoiceRoleDialog
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.google.gson.Gson
import com.jaeger.library.StatusBarUtil
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.request.PostRequest
import com.zhouyou.http.widget.ProgressUtils
import kotlinx.android.synthetic.main.include_base_overall_top.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*
import org.jetbrains.annotations.NotNull
import org.json.JSONArray
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * @作者：liuyuanbo
 * @时间：2018/9/30
 * @邮箱：972383753@qq.com
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
     * 更换角色和修改角色时用到的
     */
    lateinit var mMySideEntity: WechatUserEntity
    lateinit var mOtherSideEntity: WechatUserEntity
    lateinit var mQQDialog: QMUITipDialog
    /**
     * 是否需要裁剪图片的功能
     */
    private var mNeedCrop = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeSetContentView()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.parseColor("#F7F7F7")
        }
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
     * 设置状态栏的颜色
     */
    fun setStatusBarColor(color: Int = ColorFinal.wechatTitleBar, alpha: Int = 0){
        StatusBarUtil.setColor(this, color, alpha)
    }
    /**
     * Android6.0设置亮色状态栏模式
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun setLightStatusBarForM(@NotNull activity: Activity, dark: Boolean) {
        val window = activity.window
        if (window != null) {
            val decor = window.decorView
            var ui = decor.systemUiVisibility
            ui = if (dark) {
                ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                ui and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decor.systemUiVisibility = ui
        }
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
     * 设置和savedInstanceState相关的东西

     * @return
     */
    private fun setWithSavedInstanceState(savedInstanceState: Bundle?) {}

    /**
     * 设置根布局资源id

     * @return
     */
    private fun setContextS() {}

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
            R.id.overAllBackLayout -> {
                finish()
            }
            R.id.overAllBackBlackLayout -> {
                finish()
            }
            R.id.overAllRightTitleTv -> {}
            R.id.overAllRightIv -> {}
            R.id.overAllTitleTv -> {}

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
    protected fun setTopTitle(title: String, type: Int = 0, rightTitle: String = ""
                              , leftColor: Int = R.color.black
                              , leftIv: Int = R.mipmap.back, bgColor: Int = R.color.white
                              , contentColor: Int = R.color.black, rightIv: Int = -1, showBottomLine: Boolean = true) {
        if (!showBottomLine){
            overAllBottomLine.visibility = View.GONE
        }
        overAllBackTv.setTextColor(ContextCompat.getColor(this, leftColor))
        overAllBackLayout.setOnClickListener(this)
        overallTitleBgLayout.setBackgroundColor(ContextCompat.getColor(this, bgColor))
        overAllTitleTv.setTextColor(ContextCompat.getColor(this, contentColor))
        overAllBackIv.setImageResource(leftIv)

        if (!TextUtils.isEmpty(rightTitle)){
            overAllRightTitleTv.text = rightTitle
            overAllRightTitleTv.setOnClickListener(this)
            overAllRightTitleTv.visibility = View.VISIBLE
        }
        if(rightIv > 0){
            overAllRightIv.setImageResource(rightIv)
            overAllRightIv.setOnClickListener(this)
            overAllRightIv.visibility = View.VISIBLE
        }
        when(type){
            1 ->{
                overAllBackLayout.visibility = View.GONE
            }
        }
        overAllTitleTv.setOnClickListener(this)
        overAllTitleTv.text = title
    }

    /**
     * type -- 1 -- 确定按钮
     * 2 -- 删除按钮
     */
    protected fun setBlackTitle(title: String, type: Int = 0) {
        overAllBackBlackLayout.setOnClickListener {
            finish()
        }
        if (type == 1){
            overallAllRightWithBgTv.setOnClickListener(this)
            overallAllRightWithBgTv.visibility = View.VISIBLE
        }
        if (type == 2){
            overallAllRightWithOutBgTv.setOnClickListener(this)
            overallAllRightWithOutBgTv.visibility = View.VISIBLE
        }
        overAllTitleBlackTv.text = title
    }

    /**
     * 初始化生意类别选择器
     * @param type 0 -- 性别, 1 -- 行业, 4 -- 群人数, 5 -- 加粉数量, 6 -- 号段加粉
     */
    fun initSingleOneOptionPicker(type: Int = 0, showAllType: Boolean = true) {//条件选择器初始化
        val list = arrayListOf<String>()
        list.addAll(ConditionFinal.SEXUALITY)
        var optionPickerView: OptionsPickerView<String>? = null
        mPickerType = type
        mShowAll = showAllType
        var optionsPickerBuilder = OptionsPickerBuilder(this, this)
                .setContentTextSize(20)//设置滚轮文字大小
                .setLineSpacingMultiplier(2.0f)
                .setSelectOptions(0, 1)//默认选中项
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOptionsSelectChangeListener { options1, _, _ ->
                    if (type == 6){
                        val vipStatus = UserOperateUtil.getUserInfo().status
                        if (options1 > 1){//vip的功能
                            if (vipStatus < 2){
                                showToast("对不起，只有VIP会员才有此权限")
                                optionPickerView?.setSelectOptions(1)
                                return@setOptionsSelectChangeListener
                            }
                        }
                        if (options1 == 5){
                            if (vipStatus != 4){
                                optionPickerView?.setSelectOptions(1)
                                showToast("对不起，只有年费以上VIP会员才有此权限")
                                return@setOptionsSelectChangeListener
                            }
                        }
                    }
                }

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
            5 ->{
                optionsPickerBuilder.setTitleText("选择数量")
                list.clear()
                list.addAll(ConditionFinal.ADD_FANS_COUNT)
                if (!showAllType){
                    list.removeAt(0)
                }
            }
            6 ->{
                optionsPickerBuilder.setTitleText("选择数量")
                list.clear()
                list.addAll(ConditionFinal.ADD_FANS_COUNT_2)
                if (!showAllType){
                    list.removeAt(0)
                }
            }
        }
        optionPickerView = optionsPickerBuilder.build()
        optionPickerView?.setPicker(list)//一级选择器
        optionPickerView?.show(View(this))
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
    fun initIndustryOrGroup(type: Int = 1, showAllType: Boolean = true) {//条件选择器初始化
        mShowAll = showAllType
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
        val list: ArrayList<OverallIndustryEntity> = arrayListOf()
        if (type == 1){
            list.addAll(UserOperateUtil.getIndustrys())
        }else{
            list.addAll(UserOperateUtil.getGroupType())
        }
        if (showAllType){
            var all = OverallIndustryEntity()
            all.title = "全部类型"
            list.add(0, all)
        }
        optionPickerView.setPicker(list)
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
            val list: ArrayList<OverallIndustryEntity> =  UserOperateUtil.getIndustrys()
            if (mShowAll){
                var all = OverallIndustryEntity()
                all.title = "全部类型"
                list.add(0, all)
            }
            v?.tag = list[options1]
        }
        if (mPickerType == 2){
            if (!mShowAll){
                v?.tag = options1Items[options1] + " " + options2Items[options1][options2]
            }else{
                v?.tag = options1Items[options1] + " " + options2Items[options1][options2]
            }

        }
        if (mPickerType == 3){//群类型
            val list: ArrayList<OverallIndustryEntity> =  UserOperateUtil.getGroupType()
            if (mShowAll){
                var all = OverallIndustryEntity()
                all.title = "全部类型"
                list.add(0, all)
            }
            v?.tag = list[options1]
        }
        if (mPickerType == 4){
            if (!mShowAll){
                v?.tag = ConditionFinal.GROUPPEOPLE[options1 + 1]
            }else{
                v?.tag = ConditionFinal.GROUPPEOPLE[options1]
            }
        }
        if (mPickerType == 5){
            if (!mShowAll){
                v?.tag = ConditionFinal.ADD_FANS_COUNT[options1 + 1]
            }else{
                v?.tag = ConditionFinal.ADD_FANS_COUNT[options1]
            }
        }
        if (mPickerType == 6){
            when(ConditionFinal.ADD_FANS_COUNT_2[options1]){
                "10" ->{
                    v?.tag = 10
                }
                "50" ->{
                    v?.tag = 50
                }
                "100(VIP)" ->{
                    v?.tag = 100
                }
                "200(VIP)" ->{
                    v?.tag = 200
                }
                "500(年费以上VIP)" ->{
                    v?.tag = 500
                }
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
    fun showQQWaitDialog(msg: String = "请稍后"): QMUITipDialog{
        mQQDialog = QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create()
        mQQDialog.show()
        return mQQDialog
    }
    fun dismissQQDialog(){
        mQQDialog.dismiss()
    }

    fun dismissLoadingDialog() {
        if (ProgressUtils.mProgressDialog != null){
            ProgressUtils.cancleProgressDialog()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoadingDialog()
        EventBusUtil.unRegister(this)
    }

    fun showToast(msg: String?) {
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
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {//点击editText控件外部
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) {
                    assert(v != null)
                    closeKeyboard()
                    if (editText != null) {
                        editText!!.clearFocus()
                    }
                }
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return window.superDispatchTouchEvent(ev) || onTouchEvent(ev)
    }

    private var editText: EditText? = null

    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            editText = v
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }

    // 返回时关闭软键盘
    private fun closeKeyboard() {
        if (currentFocus != null) {
            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * 更换角色，修改角色
     * 0 -- 操作自己
     * 1 -- 操作对方
     */
    fun operateRole(roleEntity: WechatUserEntity, side: Int = 0){
        val dialog = ChoiceRoleDialog()
        if (side == 0){
            dialog.setRequestCode(RequestCode.MY_SIDE, roleEntity)
        }else{
            dialog.setRequestCode(RequestCode.OTHER_SIDE, roleEntity)
        }
        dialog.show(supportFragmentManager, "choiceRole")
    }

    /**
     * 需要VIP才可以使用的功能
     */
    fun needVip(){
        if (!UserOperateUtil.isVip()){
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setCanTouchOutSideCancle(false)
                    .canCancle(false)
                    .setTitleText("本功能为VIP功能")
                    .setContentText("开通VIP才能继续使用哦！")
                    .setConfirmText("马上开通")
                    .setConfirmClickListener {
                        LaunchUtil.launch(this, OverallPurchaseVipActivity::class.java)
                        it.dismissWithAnimation()

                    }.setCancelText("取消")
                    .setCancelClickListener {
                        it.dismissWithAnimation()
                        finish()
                    }.show()
        }
    }

    fun callAlbum(maxCount: Int = 1, needCrop: Boolean = false){
        mMaxCount = maxCount
        mNeedCrop = needCrop
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
        when(requestCode){
            RequestCode.IMAGE_SELECT ->{
                if (null != data){
                    mAlbumList = Matisse.obtainResult(data)
                    if (mNeedCrop){
                        startCrop(mAlbumList[0])
                    }else{
                        for (i in mAlbumList.indices) {
                            var file = FileUtil.getFileByUri(mAlbumList[i], this)
                            mFiles.add(file)
                        }
                    }
                }
            }
            RequestCode.CROP_IMAGE ->{
                mFinalPicUri = data?.let { UCrop.getOutput(it) }
                var path = FileUtil.getFilePathByUri(this, mFinalPicUri)
                mFinalCropFile = File(path) //转换为File
            }

            RequestCode.MY_SIDE -> {
                if (data?.extras?.containsKey(IntentKey.ENTITY) == true){
                    mMySideEntity = data.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity
                }
            }
            RequestCode.OTHER_SIDE -> {
                if (data?.extras?.containsKey(IntentKey.ENTITY) == true){
                    mOtherSideEntity = data.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity
                }
            }
        }
    }
}
