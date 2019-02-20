package app.jietuqi.cn.wechat.simulator.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Vibrator
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.ui.wechatscreenshot.widget.EmojiWechatManager
import app.jietuqi.cn.util.*
import app.jietuqi.cn.wechat.entity.WechatMsgEditEntity
import app.jietuqi.cn.wechat.simulator.adapter.WechatSimulatorPreviewAdapter
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorHelper
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.widget.menu.FloatMenu
import com.bm.zlzq.utils.ScreenUtil
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.zhy.android.percent.support.PercentLinearLayout
import kotlinx.android.synthetic.main.activity_wechat_simulator_preview.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*
import java.io.File

/**
 * 作者： liuyuanbo on 2018/12/4 22:46.
 * 时间： 2018/12/4 22:46
 * 邮箱： 972383753@qq.com
 * 用途： 微信截图预览页面
 */
@RuntimePermissions
class WechatSimulatorPreviewActivity : BaseWechatActivity(), WechatSimulatorPreviewAdapter.WechatOperateListener {

    private lateinit var mHelper: WechatSimulatorHelper
    private val mList: MutableList<WechatScreenShotEntity> = mutableListOf()
    private lateinit var mAdapter: WechatSimulatorPreviewAdapter
    /**
     * 0 -- 文本
     * 1-- 图片
     * 2 -- 时间
     * 3 -- 红包
     * 4 -- 转账
     * 5 -- 语音
     * 6 -- 系统提示
     */
    private var mFunType = 0
    /**
     * 是否是自己说话
     */
    private var mComMsg = false
    private lateinit var mChangeRoleDialog: QMUITipDialog
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_preview
    private var mViewPagerLists = arrayListOf<View>()
    private lateinit var mPageAdapter: PagerAdapter
    //定义一个ImageVIew数组，来存放生成的小园点
    private var imageViews: ArrayList<ImageView> = arrayListOf()
    private var imageView: ImageView? = null
    private val mPoint = Point()
    /**
     * 最后一条数据的显示时间
     */
    private var mLastMsgTime = 0L
    /**
     * true -- 修改聊天背景
     * false -- 发送图片
     */
    private var mChangeChatBg = false
    /**
     * 准备编辑的消息的位置
     */
    private var mEditPosition = 0

    override fun needLoadingView() = false

    override fun initAllViews() {
        val hideCover = UserOperateUtil.hideWechatCover()
        if (hideCover){
            mWechatSimulatorPreviewCoverLayout.visibility = View.GONE
        }
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mOtherSideEntity = WechatUserEntity()
        registerEventBus()
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
        setLightStatusBarForM(this, true)
        mMySideEntity = UserOperateUtil.getWechatSimulatorMySelf()
        mWechatSimulatorPreviewVoiceAndKeyBoardIv.tag = "0"
        initPageAdapter()
        initPointer()
    }
    private fun changeRole(){
        vibrator.vibrate(300)
        mComMsg = !mComMsg
        var builder = QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
        if (mComMsg){//对方说话
            builder.setTipWord("切换为对方说话")
        }else{
            builder.setTipWord("切换为自己说话")
        }
        mChangeRoleDialog = builder.create()
        mChangeRoleDialog.show()
        mWechatSimulatorPreviewBackTv.postDelayed({ mChangeRoleDialog.dismiss() }, 1500)
        mAdapter.changeRole(mComMsg)
    }
    override fun initViewsListener() {
        mWechatSimulatorPreviewCoverDontShowBtn.setOnClickListener(this)
        mWechatSimulatorPreviewCoverIKnowBtn.setOnClickListener(this)
        mWechatSimulatorPreviewCleanIv.setOnClickListener(this)
        mWechatSimulatorPreviewNickNameTv.setOnClickListener(this)
        mWechatSimulatorPreviewBackTv.setOnClickListener(this)
        mWechatSimulatorPreviewVoiceAndKeyBoardIv.setOnClickListener(this)
        mWechatSimulatorPreviewSendTv.setOnClickListener(this)
        mWechatSimulatorPreviewMoreIv.setOnClickListener(this)
        mWechatSimulatorPreviewEmojiIv.setOnClickListener(this)
        mWechatSimulatorPreviewContentEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()){
                    mWechatSimulatorPreviewMoreIv.visibility = View.GONE
                    mWechatSimulatorPreviewSendTv.visibility = View.VISIBLE
                }else{
                    mWechatSimulatorPreviewMoreIv.visibility = View.VISIBLE
                    mWechatSimulatorPreviewSendTv.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        mWechatSimulatorPreviewRecyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(mWechatSimulatorPreviewRecyclerView) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {}
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {
                initMenu(vh.adapterPosition)
            }
        })
        mWechatSimulatorPreviewRecyclerView.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                mWechatSimulatorPreviewRecyclerView.postDelayed({ mWechatSimulatorPreviewRecyclerView.scrollToPosition(mList.size - 1) }, 0)
            }
        }
        mWechatSimulatorPreviewRecyclerView.setOnTouchListener { v, _ ->
            mWechatSimulatorPreviewFunLayout.visibility = View.GONE
            mWechatSimulatorPreviewEmojiLayout.visibility = View.GONE
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(v.windowToken, 0)
            false
        }

        //设置监听
        mWechatSimulatorPreviewViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            //页面滑动是执行
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            //页面滑动完成后执行
            override fun onPageSelected(position: Int) {
                //判断当前是在那个page，就把对应下标的ImageView原点设置为选中状态的图片
                for (i in imageViews.indices) {
                    imageViews[position].setBackgroundResource(R.drawable.wechat_simulator_select)
                    if (position != i) {
                        imageViews[i].setBackgroundResource(R.drawable.wechat_simulator_unselect)
                    }
                }
            }
            //监听页面的状态，0--静止 1--滑动  2--滑动完成
            override fun onPageScrollStateChanged(state: Int) {}
        })
        mWechatSimulatorPreviewContentEt.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                // 此处为得到焦点时的处理内容
                mWechatSimulatorPreviewEmojiIv.setImageResource(R.drawable.wechat_input_emoticon)
                mWechatSimulatorPreviewEmojiLayout.visibility = View.GONE
                mWechatSimulatorPreviewFunLayout.visibility = View.GONE
            } else {
                // 此处为失去焦点时的处理内容
            }
        }
        mWechatSimulatorPreviewEmojiView.setItemClickListener { code ->
            //emoji点击事件
            if (code == "/DEL") {//点击了删除图标
                mWechatSimulatorPreviewContentEt.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
            } else {
                mWechatSimulatorPreviewContentEt.text.insert(mWechatSimulatorPreviewContentEt.selectionStart, code)
            }
        }
        mWechatSimulatorPreviewContentEt.addTextChangedListener(textWatcher)
    }
    private fun initMenu(adapterPosition: Int){
        val floatMenu = FloatMenu(this@WechatSimulatorPreviewActivity)
        floatMenu.items("删除", "编辑", "撤回")
        floatMenu.show(mPoint)
        floatMenu.setOnItemClickListener { _, position ->
            when(position){
                0 ->{
                    var entity = mList[adapterPosition]
                    GlobalScope.launch { // 在一个公共线程池中创建一个协程
                        mHelper.delete(entity)
                    }
                    mList.removeAt(adapterPosition)
                    mAdapter.notifyItemRemoved(adapterPosition)
                    if (mList.size >=  1){
                        mLastMsgTime = mList[mList.size - 1].lastTime
                    }
                }
                1 ->{
                    mEditPosition = adapterPosition
                    var entity = mList[adapterPosition]
                    when(entity.msgType){//文字
                        0 ->{
                            LaunchUtil.startWechatSimulatorCreateTextActivity(this, mOtherSideEntity, entity, 1)
                        }
                        1 ->{
                            LaunchUtil.startWechatSimulatorCreatePictureActivity(this, mOtherSideEntity, entity, 1)
                        }
                        2 ->{
                            LaunchUtil.startWechatSimulatorCreateTimeActivity(this, mOtherSideEntity, entity, 1)
                        }
                        3 ->{
                            LaunchUtil.startWechatSimulatorCreateRedPacketActivity(this, mOtherSideEntity, entity, 1)
                        }
                        4 ->{
                            LaunchUtil.startWechatSimulatorCreateRedPacketActivity(this, mOtherSideEntity, entity, 1)
                        }
                        5 ->{
                            LaunchUtil.startWechatSimulatorCreateTransferActivity(this, mOtherSideEntity, entity, 1)
                        }
                        6 ->{
                            LaunchUtil.startWechatSimulatorCreateTransferActivity(this, mOtherSideEntity, entity, 1)
                        }
                        7 ->{
                            LaunchUtil.startWechatSimulatorCreateVoiceActivity(this, mOtherSideEntity, entity, 1)
                        }
                        8 ->{
                            LaunchUtil.startWechatSimulatorCreateSystemMessageActivity(this, mOtherSideEntity, entity, 1, "微信")
                        }
                    }
                }
                2 ->{
                    var entity = mList[adapterPosition]
                    entity.msgType = 8
                    entity.msg = "你撤回了一条消息"
                    entity.needEventBus = false
                    mHelper.update(entity, true)
                    mAdapter.notifyItemChanged(adapterPosition)
                }
            }
        }
    }
    /**
     * 输入监听事件
     */
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun afterTextChanged(editable: Editable) {
            val start = mWechatSimulatorPreviewContentEt.selectionStart
            val end = mWechatSimulatorPreviewContentEt.selectionEnd
            mWechatSimulatorPreviewContentEt.removeTextChangedListener(this)
            val cs = EmojiWechatManager.parse(editable.toString(), mWechatSimulatorPreviewContentEt.textSize)
            mWechatSimulatorPreviewContentEt.setText(cs, TextView.BufferType.SPANNABLE)
            mWechatSimulatorPreviewContentEt.setSelection(start, end)
            mWechatSimulatorPreviewContentEt.addTextChangedListener(this)
        }
    }
    //根据页面的个数动态添加圆点指示器
    private fun initPointer() {
        //有多少个界面就new多长的数组
        for (i in mViewPagerLists.indices) {
            //new新的ImageView控件
            imageView = ImageView(this)
            //设置控件的宽高
            imageView?.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            var params = imageView?.layoutParams as LinearLayout.LayoutParams
            params.setMargins(10, 0, 10, 0)
            //设置控件的padding属性
//            imageView?.setPadding(20, 0, 20, 0)
            imageView?.let { imageViews.add(it) }

            //初始化第一个page页面的图片的原点为选中状态
            if (i == 1) {
                //表示当前图片
                imageViews[i].setBackgroundResource(R.drawable.wechat_simulator_select)
                /**
                 * 在java代码中动态生成ImageView的时候
                 * 要设置其BackgroundResource属性才有效
                 * 设置ImageResource属性无效
                 */
            } else {
                imageViews[i].setBackgroundResource(R.drawable.wechat_simulator_unselect)
            }
            //把new出来的ImageView控件添加到线性布局中
            mWechatSimulatorPreviewIndicatorLayout.addView(imageViews[i])
        }
    }
    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity//获取对方信息
        mLastMsgTime = mOtherSideEntity.lastTime
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            mOtherSideEntity.listId = WechatSimulatorListHelper(this@WechatSimulatorPreviewActivity).queryByWechatUserId(mOtherSideEntity.wechatUserId)
        }
        mHelper = WechatSimulatorHelper(this, mOtherSideEntity)
        mWechatSimulatorPreviewNickNameTv.text = mOtherSideEntity.wechatUserNickName
        val list = mHelper.queryAll()//获取和对方聊天的记录
        if (null != list){
            mList.addAll(list)
        }
        if (TextUtils.isEmpty(mOtherSideEntity.chatBg)){//没有聊天背景
            mWechatSimulatorPreviewRecyclerViewBgIv.visibility = View.GONE
            mAdapter = WechatSimulatorPreviewAdapter(mList, this)
            mAdapter.setOtherSide(mOtherSideEntity)
            mAdapter.showChatBg(false)
        }else{
            GlideUtil.displayAll(this, File(mOtherSideEntity.chatBg), mWechatSimulatorPreviewRecyclerViewBgIv)
            mWechatSimulatorPreviewRecyclerViewBgIv.visibility = View.VISIBLE
            mAdapter = WechatSimulatorPreviewAdapter(mList, this)
            mAdapter.setOtherSide(mOtherSideEntity)
            mAdapter.showChatBg(true)
        }
        mWechatSimulatorPreviewRecyclerView.adapter = mAdapter
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(150L) // 非阻塞的延迟一秒（默认单位是毫秒）
            runOnUiThread{
                mWechatSimulatorPreviewRecyclerView.scrollToPosition(mAdapter.itemCount - 1)
            }
        }
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(150L) // 非阻塞的延迟一秒（默认单位是毫秒）
            runOnUiThread{
                mWechatSimulatorPreviewRecyclerView.scrollToPosition(mAdapter.itemCount - 1)
            }
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatSimulatorPreviewCoverDontShowBtn ->{//不再提示
                SharedPreferencesUtils.putData(SharedPreferenceKey.WECHAT_COVER, true)
                mWechatSimulatorPreviewCoverLayout.visibility = View.GONE
            }
            R.id.mWechatSimulatorPreviewCoverIKnowBtn ->{//我知道了
                mWechatSimulatorPreviewCoverLayout.visibility = View.GONE
            }
            R.id.mWechatSimulatorPreviewBackTv ->{
                finish()
            }
            R.id.mWechatSimulatorPreviewEmojiIv ->{
                if (mWechatSimulatorPreviewEmojiLayout.visibility == View.VISIBLE){
                    mWechatSimulatorPreviewEmojiLayout.visibility = View.GONE
                    mWechatSimulatorPreviewEmojiIv.setImageResource(R.drawable.wechat_input_emoticon)
                }else{
                    closeKeyboard(mWechatSimulatorPreviewEmojiIv)
                    GlobalScope.launch { // 在一个公共线程池中创建一个协程
                        delay(100L) // 非阻塞的延迟一秒（默认单位是毫秒）
                        runOnUiThread {
                            mWechatSimulatorPreviewEmojiIv.setImageResource(R.drawable.wechat_input_keyboard)
                            mWechatSimulatorPreviewEmojiLayout.visibility = View.VISIBLE
                            mWechatSimulatorPreviewFunLayout.visibility = View.GONE
                            mWechatSimulatorPreviewContentEt.clearFocus()
                        }
                    }
                }
            }
            R.id.mWechatSimulatorPreviewCleanIv ->{
                LaunchUtil.startWechatEditOtherActivity(this, mOtherSideEntity)
            }
            R.id.mWechatSimulatorPreviewMoreIv ->{
                if (mWechatSimulatorPreviewFunLayout.visibility == View.VISIBLE){
                    mWechatSimulatorPreviewFunLayout.visibility = View.GONE
                    mWechatSimulatorPreviewContentEt.requestFocus()
                }else{
                    closeKeyboard(mWechatSimulatorPreviewMoreIv)
                    GlobalScope.launch {
                        // 在一个公共线程池中创建一个协程
                        delay(100L) // 非阻塞的延迟一秒（默认单位是毫秒）
                        runOnUiThread {
                            mWechatSimulatorPreviewFunLayout.visibility = View.VISIBLE
                            mWechatSimulatorPreviewContentEt.clearFocus()
                            mWechatSimulatorPreviewEmojiLayout.visibility = View.GONE
                        }
                    }
                }
            }
            R.id.mWechatSimulatorPreviewNickNameTv ->{//切换聊天对象
                changeRole()
            }
            R.id.mWechatSimulatorPreviewVoiceAndKeyBoardIv ->{
                if (mWechatSimulatorPreviewVoiceAndKeyBoardIv.tag.toString() == "0"){
                    mWechatSimulatorPreviewVoiceAndKeyBoardIv.tag = "1"
                    mWechatSimulatorPreviewVoiceAndKeyBoardIv.setImageResource(R.drawable.wechat_input_keyboard)
                    mWechatSimulatorPreviewHoldDownTheTalkTv.visibility = View.VISIBLE
                }else{
                    mWechatSimulatorPreviewVoiceAndKeyBoardIv.tag = "0"
                    mWechatSimulatorPreviewVoiceAndKeyBoardIv.setImageResource(R.drawable.wechat_input_voice)
                    mWechatSimulatorPreviewHoldDownTheTalkTv.visibility = View.GONE
                }
                LaunchUtil.startWechatSimulatorCreateVoiceActivity(this, mOtherSideEntity, null, 0)
            }
            R.id.mWechatSimulatorPreviewSendTv ->{
                mFunType = 0
                saveMsg()
                mWechatSimulatorPreviewContentEt.setText("")
            }
            R.id.wechatSimulatorFunTimeLayout ->{//发送时间
                LaunchUtil.startWechatSimulatorCreateTimeActivity(this, mOtherSideEntity, null, 0)
//                initTimePickerView(tag = "创建")
            }
            R.id.wechatSimulatorFunPictureLayout ->{//发送图片
                openAlbumWithPermissionCheck()
                mWechatSimulatorPreviewFunLayout.visibility = View.GONE
            }
            R.id.wechatSimulatorFunVideoLayout ->{//发送视频
                showToast("开发中")
            }
            R.id.wechatSimulatorFunRedPacketLayout ->{//发送红包
                LaunchUtil.startWechatSimulatorCreateRedPacketActivity(this, mOtherSideEntity, null, 0)
            }
            R.id.wechatSimulatorFunTransferLayout ->{//发送转账
                LaunchUtil.startWechatSimulatorCreateTransferActivity(this, mOtherSideEntity, null, 0)
            }
            R.id.wechatSimulatorFunGroupLayout ->{//群聊
                showToast("开发中")
            }
            R.id.wechatSimulatorFunFilesLayout ->{//文件
                showToast("开发中")
            }
            R.id.wechatSimulatorFunSystemLayout ->{//发送系统消息
                LaunchUtil.startWechatSimulatorCreateSystemMessageActivity(this, mOtherSideEntity, null, 0, "微信")
            }
        }
    }
    private fun initPageAdapter() {
        /**
         * 对于这几个想要动态载入的page页面，使用LayoutInflater.inflate()来找到其布局文件，并实例化为View对象
         */
        val inflater = LayoutInflater.from(this)
        val page1 = inflater.inflate(R.layout.item_wechat_simulator_1, null)
        val page2 = inflater.inflate(R.layout.item_wechat_simulator_2, null)
        page2.findViewById<PercentLinearLayout>(R.id.wechatSimulatorFunTimeLayout).setOnClickListener(this)
        page2.findViewById<PercentLinearLayout>(R.id.wechatSimulatorFunPictureLayout).setOnClickListener(this)
        page2.findViewById<PercentLinearLayout>(R.id.wechatSimulatorFunVideoLayout).setOnClickListener(this)
        page2.findViewById<PercentLinearLayout>(R.id.wechatSimulatorFunRedPacketLayout).setOnClickListener(this)
        page2.findViewById<PercentLinearLayout>(R.id.wechatSimulatorFunTransferLayout).setOnClickListener(this)
        page2.findViewById<PercentLinearLayout>(R.id.wechatSimulatorFunGroupLayout).setOnClickListener(this)
        page2.findViewById<PercentLinearLayout>(R.id.wechatSimulatorFunFilesLayout).setOnClickListener(this)
        page2.findViewById<PercentLinearLayout>(R.id.wechatSimulatorFunSystemLayout).setOnClickListener(this)
        //添加到集合中
        mViewPagerLists.add(page1)
        mViewPagerLists.add(page2)

        mPageAdapter = object : PagerAdapter() {
            //获取当前界面个数
            override fun getCount(): Int {
                return mViewPagerLists.size
            }

            //判断是否由对象生成页面
            override fun isViewFromObject(view: View, `object`: Any): Boolean {
                return view === `object`
            }

            //移除一个view
            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(mViewPagerLists[position])
            }

            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view = mViewPagerLists[position]
                container.addView(view)
                return view
            }
        }
        mWechatSimulatorPreviewViewPager.adapter = mPageAdapter
        mWechatSimulatorPreviewViewPager.setCurrentItem(1, false)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.IMAGE_SELECT ->{
                if (mChangeChatBg){
                    mOtherSideEntity.chatBg = mFiles[0].absolutePath
                    GlideUtil.displayAll(this, File(mOtherSideEntity.chatBg), mWechatSimulatorPreviewRecyclerViewBgIv)
                    mWechatSimulatorPreviewRecyclerViewBgIv.visibility = View.VISIBLE
                    mAdapter.showChatBg(true)
                    mAdapter.notifyDataSetChanged()
                }else{
                    if (mFiles.size >= 1){
                        mFunType = 1
                        saveMsg()
                    }
                }
            }
        }
    }
    private fun saveMsg(entity: WechatScreenShotEntity? = null){
        var msgEntity = WechatScreenShotEntity()
        msgEntity.msgType = mFunType//默认为文本聊天
        msgEntity.isComMsg = mComMsg//对方说话
        //如果是自己说话
        when (mFunType) {
            0 -> {
                var msg = mWechatSimulatorPreviewContentEt.text.toString()
                if (TextUtils.isEmpty(msg)){
                    showToast("请输入聊天内容")
                    return
                }
                msgEntity.msg = msg
                msgEntity.needEventBus = false
                setRole(msgEntity)
            }
            1 -> {
                msgEntity.filePath = mFiles[0].absolutePath
                msgEntity.needEventBus = false
                setRole(msgEntity)
                mFiles.clear()
            }
            2 -> {
                entity?.time?.let { msgEntity.time = it }
                msgEntity.timeType = entity?.timeType
                msgEntity.needEventBus = false
                setRole(msgEntity)
            }
            3 -> {//发红包
                if (entity?.receive == true && entity.msgType == 3){
                    msgEntity.msgType = 4
                    msgEntity.receive = true
                    entity.isComMsg.let { msgEntity.isComMsg = !it }
                    entity.isComMsg.let { mComMsg = !it }
                    entity.id.let { msgEntity.receiveTransferId = it } //修改的时候同时修改
                    if (entity.msgType == 3){//领红包
                        msgEntity.msgType = 4
                    }
                }else{
                    entity?.receive?.let { msgEntity.receive = it }
                    entity?.isComMsg?.let { msgEntity.isComMsg = it }
                    entity?.isComMsg?.let { mComMsg = it }
                }
                msgEntity.msg = entity?.msg
                msgEntity.money = entity?.money
                msgEntity.needEventBus = false
                setRole(msgEntity)
            }
            4 -> {//领取红包
                msgEntity.receive = true
                entity?.isComMsg?.let { msgEntity.isComMsg = !it }
                entity?.isComMsg?.let { mComMsg = !it }
                entity?.id?.let { msgEntity.receiveTransferId = it } //修改的时候同时修改
                msgEntity.msg = entity?.msg
                msgEntity.money = entity?.money
                msgEntity.needEventBus = false
                if (!mComMsg){//自己的红包被领取
                    msgEntity.resourceName = mOtherSideEntity.resourceName
                    msgEntity.avatarInt = mOtherSideEntity.avatarInt
                    msgEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
                    msgEntity.wechatUserId = mOtherSideEntity.wechatUserId
                }else{//领取对方的红包
                    msgEntity.avatarInt = mMySideEntity.avatarInt
                    msgEntity.resourceName = mMySideEntity.resourceName
                    msgEntity.avatarStr = mMySideEntity.wechatUserAvatar
                    msgEntity.wechatUserId = mMySideEntity.wechatUserId
                }
            }
            5 -> {//转账
                if (entity?.receive == true && entity.msgType == 5){
                    var en: WechatScreenShotEntity
                    for (i in mList.indices) {
                        en = mList[i]
                        if(en.id == entity.id){
                            mHelper.update(entity, true)
                            EventBusUtil.unRegister(this@WechatSimulatorPreviewActivity)
                            mList.removeAt(en.position)
                            mList.add(en.position, entity)
                            mAdapter.notifyItemChanged(en.position)
                        }
                    }
                    msgEntity.msgType = 6
                    msgEntity.receive = true
                    entity.isComMsg.let { msgEntity.isComMsg = !it }
                    entity.isComMsg.let { mComMsg = !it }
                    entity.id.let { msgEntity.receiveTransferId = it } //修改的时候同时修改
                    msgEntity.transferReceiveTime = entity.transferReceiveTime
                    msgEntity.transferOutTime = entity.transferOutTime
                }else{
                    entity?.receive?.let { msgEntity.receive = it }
                    entity?.isComMsg?.let { msgEntity.isComMsg = it }
                    entity?.isComMsg?.let { mComMsg = it }
                    entity?.transferOutTime?.let {  msgEntity.transferOutTime = it }
                    entity?.transferReceiveTime?.let { msgEntity.transferReceiveTime = it }
                }
                msgEntity.msg = entity?.msg
                msgEntity.money = entity?.money
                msgEntity.needEventBus = false
                setRole(msgEntity)
            }
            6 -> {//转账
                msgEntity.receive = true
                entity?.isComMsg?.let { msgEntity.isComMsg = it }
                entity?.isComMsg?.let { mComMsg = it }
                entity?.id?.let { msgEntity.receiveTransferId = it } //修改的时候同时修改
                msgEntity.msg = entity?.msg
                msgEntity.money = entity?.money
                msgEntity.needEventBus = false
            }
            7 -> {
                entity?.voiceLength?.let { msgEntity.voiceLength = it }
                entity?.voiceToText?.let { msgEntity.voiceToText = it }
                entity?.isComMsg?.let { mComMsg = it }
                entity?.isComMsg?.let { msgEntity.isComMsg = it }
                entity?.alreadyRead?.let { msgEntity.alreadyRead = it }
                setRole(msgEntity)
            }
            8 -> {
                msgEntity.msg = entity?.msg
                entity?.msgType?.let { msgEntity.msgType = it }
            }
        }
        msgEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
        mLastMsgTime = msgEntity.lastTime
        mHelper.save(msgEntity)
        mList.add(msgEntity)
        mAdapter.notifyDataSetChanged()
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(100L) // 非阻塞的延迟一秒（默认单位是毫秒）
            runOnUiThread{
                mWechatSimulatorPreviewRecyclerView.scrollToPosition(mAdapter.itemCount - 1)
            }
        }

    }

    private fun setRole(msgEntity: WechatScreenShotEntity){
        if (!mComMsg){//如果是自己说话
            if (mFunType == 4){
                msgEntity.avatarInt = mOtherSideEntity.avatarInt
                msgEntity.resourceName = mOtherSideEntity.resourceName
                msgEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
                msgEntity.wechatUserId = mOtherSideEntity.wechatUserId
            }else{
                msgEntity.avatarInt = mMySideEntity.avatarInt
                msgEntity.resourceName = mMySideEntity.resourceName
                msgEntity.avatarStr = mMySideEntity.wechatUserAvatar
                msgEntity.wechatUserId = mMySideEntity.wechatUserId
            }
        }else{//对方说话
            if (mFunType == 4){
                msgEntity.resourceName = mMySideEntity.resourceName
                msgEntity.avatarInt = mMySideEntity.avatarInt
                msgEntity.avatarStr = mMySideEntity.wechatUserAvatar
                msgEntity.wechatUserId = mMySideEntity.wechatUserId
            }else{
                msgEntity.avatarInt = mOtherSideEntity.avatarInt
                msgEntity.resourceName = mOtherSideEntity.resourceName
                msgEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
                msgEntity.wechatUserId = mOtherSideEntity.wechatUserId
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(entity: WechatScreenShotEntity) {
        mFunType = entity.msgType
        saveMsg(entity)
        EventBusUtil.unRegister(this)
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(1000L) // 非阻塞的延迟一秒（默认单位是毫秒）
            registerEventBus()
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEditMsg(entity: WechatMsgEditEntity) {
        mList[mEditPosition] = entity.editEntity
        mAdapter.notifyItemChanged(mEditPosition)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChangeUserInfo(entity: WechatUserEntity) {
        mWechatSimulatorPreviewNickNameTv.text = entity.wechatUserNickName
        mOtherSideEntity = entity
        mAdapter.setOtherSide(mOtherSideEntity)
        if (TextUtils.isEmpty(mOtherSideEntity.chatBg)){//没有聊天背景
            mWechatSimulatorPreviewRecyclerViewBgIv.visibility = View.GONE
            mAdapter.showChatBg(false)
        }else{
            GlideUtil.displayAll(this, File(mOtherSideEntity.chatBg), mWechatSimulatorPreviewRecyclerViewBgIv)
            mWechatSimulatorPreviewRecyclerViewBgIv.visibility = View.VISIBLE
            mAdapter.showChatBg(true)
        }
        mAdapter.notifyDataSetChanged()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onClean(clean: String) {
        if ("清除" == clean){
            mHelper.deleteAll()
            mLastMsgTime = mList[mList.size - 1].lastTime
            mList.clear()
            mAdapter.notifyDataSetChanged()
        }
    }
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openAlbum() {
        callAlbum()
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

    override fun finish() {
        EventBusUtil.unRegister(this)
        val lastEntity = mHelper.queryLastMsg()
        var listHelper = WechatSimulatorListHelper(this)
        if (null != lastEntity){
            val userEntity = WechatUserEntity()
            userEntity.id = mOtherSideEntity.listId
            userEntity.wechatUserId = mOtherSideEntity.wechatUserId
            userEntity.avatarInt = mOtherSideEntity.avatarInt
            userEntity.wechatUserAvatar = mOtherSideEntity.wechatUserAvatar
            userEntity.wechatUserNickName = mOtherSideEntity.wechatUserNickName
            userEntity.avatarFile = mOtherSideEntity.avatarFile
            userEntity.pinyinNickName = mOtherSideEntity.pinyinNickName
            userEntity.firstChar = mOtherSideEntity.firstChar
            userEntity.isFirst = mOtherSideEntity.isFirst
            userEntity.isLast = mOtherSideEntity.isLast
            userEntity.meSelf = mOtherSideEntity.meSelf
            userEntity.top = mOtherSideEntity.top
            userEntity.chatBg = mOtherSideEntity.chatBg
            userEntity.resourceName = mOtherSideEntity.resourceName
            userEntity.unReadNum = "0"
            userEntity.showPoint = false

            userEntity.msgType = lastEntity.msgType.toString()
            userEntity.timeType = lastEntity.timeType
            userEntity.msg = lastEntity.msg
            userEntity.isComMsg = lastEntity.isComMsg
            userEntity.timeType = lastEntity.timeType

            userEntity.alreadyRead = lastEntity.alreadyRead
            userEntity.lastTime = mLastMsgTime
            if (null == listHelper.query(userEntity.wechatUserId)){//没有该数据，说明没有保存到聊天列表中
                listHelper.save(userEntity)
            }else{
                listHelper.update(userEntity)
            }
            EventBusUtil.post(userEntity)
        }else{
            val userEntity = WechatUserEntity()
            userEntity.id = mOtherSideEntity.listId
            userEntity.wechatUserId = mOtherSideEntity.wechatUserId
            userEntity.avatarInt = mOtherSideEntity.avatarInt
            userEntity.wechatUserAvatar = mOtherSideEntity.wechatUserAvatar
            userEntity.wechatUserNickName = mOtherSideEntity.wechatUserNickName
            userEntity.avatarFile = mOtherSideEntity.avatarFile
            userEntity.pinyinNickName = mOtherSideEntity.pinyinNickName
            userEntity.firstChar = mOtherSideEntity.firstChar
            userEntity.isFirst = mOtherSideEntity.isFirst
            userEntity.isLast = mOtherSideEntity.isLast
            userEntity.meSelf = mOtherSideEntity.meSelf
            userEntity.top = mOtherSideEntity.top
            userEntity.resourceName = mOtherSideEntity.resourceName
            userEntity.unReadNum = "0"
            userEntity.showPoint = false

            userEntity.msgType = null
            userEntity.timeType = "24"
            userEntity.msg = ""
            userEntity.lastTime = mLastMsgTime
            if (null == listHelper.query(userEntity.wechatUserId)){//没有该数据，说明没有保存到聊天列表中
                listHelper.save(userEntity)
            }else{
                listHelper.update(userEntity)
            }
        }
        super.finish()
    }

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private lateinit var vibrator: Vibrator
    private val UPTATE_INTERVAL_TIME = 50
    private val SPEED_SHRESHOLD = 100//这个值调节灵敏度
    private var lastUpdateTime: Long = 0
    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var lastZ: Float = 0f
    private var mCanShake = true
    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
        needVipForCover()
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        }
        if (sensor != null) {
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME)//这里选择感应频率
        }
    }
    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val currentUpdateTime = System.currentTimeMillis()
            val timeInterval = currentUpdateTime - lastUpdateTime
            if (timeInterval < UPTATE_INTERVAL_TIME) {
                return
            }
            lastUpdateTime = currentUpdateTime
            // 传感器信息改变时执行该方法
            val values = event.values
            val x = values[0] // x轴方向的重力加速度，向右为正
            val y = values[1] // y轴方向的重力加速度，向前为正
            val z = values[2] // z轴方向的重力加速度，向上为正
            val deltaX = x - lastX
            val deltaY = y - lastY
            val deltaZ = z - lastZ
            lastX = x
            lastY = y
            lastZ = z
            val speed = Math.sqrt((deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ).toDouble()) / timeInterval * 100
            if (speed >= SPEED_SHRESHOLD) {
                if (mCanShake){
                    mCanShake = false
                    changeRole()
                    GlobalScope.launch { // 在一个公共线程池中创建一个协程
                        delay(1500L) // 非阻塞的延迟一秒（默认单位是毫秒）
                        mCanShake = true
                    }
                }
            }
        }


        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

        }
    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action === MotionEvent.ACTION_DOWN) {
            mPoint.x = ev.rawX.toInt()
            mPoint.y = ev.rawY.toInt()
            val v = currentFocus
            if (isShouldHideInput2(v, ev)) {//点击的是其他区域，则调用系统方法隐藏软键盘
                (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(v.windowToken, 0)
            }
            return window.superDispatchTouchEvent(ev) || onTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }
    private fun isShouldHideInput2(v: View?, event: MotionEvent): Boolean {
        if (v != null) {
            return when (v.id) {
                R.id.mWechatSimulatorPreviewRecyclerView -> {
                    val leftTop = intArrayOf(0, 0)
                    //获取输入框当前的location位置
                    v.getLocationInWindow(leftTop)
                    val left = leftTop[0]
                    val top = leftTop[1]
                    val bottom = top + v.height
                    val right = ScreenUtil.getScreenWidth(this)
                    (event.x > left && event.x < right && event.y > top && event.y < bottom)
                }
                else -> {
                    false
                }
            }
        }
        return false
    }
    //我领取对方的红包
    override fun meTakeOtherRedPacket(entity: WechatScreenShotEntity, position: Int) {
        mHelper.update(entity, true)
        mAdapter.notifyItemChanged(position)
    }

    //对方领取我的红包
    override fun otherTakeMyRedPacket(entity: WechatScreenShotEntity, position: Int) {
        mHelper.update(entity, true)
        mAdapter.notifyItemChanged(position)
    }
    override fun myTransferWasReceive(entity: WechatScreenShotEntity, position: Int) {
        mHelper.update(entity, true)
    }
    override fun closeBottomMenu() {
        mWechatSimulatorPreviewFunLayout.visibility = View.GONE
        mWechatSimulatorPreviewEmojiLayout.visibility = View.GONE
    }
}
