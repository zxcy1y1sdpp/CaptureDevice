package app.jietuqi.cn.wechat.simulator.ui.activity

import android.Manifest
import android.animation.ObjectAnimator
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
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.animation.FrameAnimation
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
import app.jietuqi.cn.wechat.simulator.adapter.WechatGroupRolesAdapter
import app.jietuqi.cn.wechat.simulator.adapter.WechatSimulatorPreviewGroupAdapter
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorHelper
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.wechat.simulator.util.WechatGroupRedPacket
import app.jietuqi.cn.widget.menu.FloatMenu
import com.bm.zlzq.utils.ScreenUtil
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.zhouyou.http.EventBusUtil
import com.zhy.android.percent.support.PercentLinearLayout
import kotlinx.android.synthetic.main.activity_wechat_simulator_preview_group.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * 作者： liuyuanbo on 2018/12/4 22:46.
 * 时间： 2018/12/4 22:46
 * 邮箱： 972383753@qq.com
 * 用途： 微信截图预览页面
 */
@RuntimePermissions
class WechatSimulatorPreviewGroupActivity : BaseWechatActivity(),  WechatSimulatorPreviewGroupAdapter.WechatOperateListener {

    /**
     * 群聊里面的人
     */
    private var mGroupRolesList = arrayListOf<WechatUserEntity>()
    /**
     * 在列表中的数据
     */
    private var mListEntity = WechatUserEntity()

    private lateinit var mHelper: WechatSimulatorHelper
    private val mList: ArrayList<WechatScreenShotEntity> = arrayListOf()
    private lateinit var mAdapter:  WechatSimulatorPreviewGroupAdapter
    private lateinit var mRoleAdapter: WechatGroupRolesAdapter
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
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_preview_group
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
     * 准备编辑的消息的位置
     */
    private var mEditPosition = 0

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
                    if (!isFinishing){
                        changeRole()
                    }
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

    override fun needLoadingView() = false

    override fun initAllViews() {
        val hideCover = UserOperateUtil.hideWechatCover()
        if (hideCover){
            mWechatSimulatorPreviewCoverLayout.visibility = View.GONE
        }
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
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
        mWechatBubbleLayout.visibility = View.GONE
        mAdapter.changeRole(mComMsg)
        var builder = QMUITipDialog.Builder(this).setIconType(QMUITipDialog.Builder.ICON_TYPE_INFO)
        if (mComMsg){//对方说话
            builder.setTipWord("切换为 [最近发言人 ]说话")
        }else{
            builder.setTipWord("切换为 [ 自己 ]说话")
        }
        mChangeRoleDialog = builder.create()
        mChangeRoleDialog.show()
        mWechatSimulatorPreviewBackTv.postDelayed({ mChangeRoleDialog.dismiss() }, 1500)
        mAdapter.setOtherSide(mOtherSideEntity)
    }
    override fun initViewsListener() {
        mMyLayout.setOnClickListener(this)
        mRecentLayout.setOnClickListener(this)

        mWechatSimulatorPreviewCoverDontShowBtn.setOnClickListener(this)
        mWechatSimulatorPreviewCoverIKnowBtn.setOnClickListener(this)
        mWechatSimulatorPreviewCleanIv.setOnClickListener(this)
        mWechatSimulatorPreviewNickNameTv.setOnClickListener(this)
        mWechatSimulatorPreviewBackTv.setOnClickListener(this)
        mWechatSimulatorPreviewVoiceAndKeyBoardIv.setOnClickListener(this)
        mWechatSimulatorPreviewSendTv.setOnClickListener(this)
        mWechatSimulatorPreviewMoreIv.setOnClickListener(this)
        mWechatSimulatorPreviewEmojiIv.setOnClickListener(this)
        mOpenIv.setOnClickListener(this)
        mCloseHongbaoIv.setOnClickListener(this)
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
            mWechatBubbleLayout.visibility = View.GONE
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
                mWechatBubbleLayout.visibility = View.GONE
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
        val floatMenu = FloatMenu(this@WechatSimulatorPreviewGroupActivity)
//        floatMenu.items("删除", "编辑")
        floatMenu.items("删除", "编辑", "撤回", "排序")
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
                            LaunchUtil.startWechatSimulatorCreateGroupSystemMessageActivity(this, 1, mListEntity, entity)
                        }
                        16 ->{
                            LaunchUtil.startWechatSimulatorCreateFileActivity(this, mOtherSideEntity, entity, 1)
                        }
                    }
                }
                2 ->{
                    var entity = mList[adapterPosition]
                    entity.msgType = 8
                    entity.needEventBus = false
                    if (mComMsg){
                        entity.msg = StringUtils.insertFrontAndBack(entity.wechatUserNickName, "“", "”撤回了一条消息")
                    }else{
                        entity.msg = "你撤回了一条消息"
                    }
                    mHelper.update(entity, true)
                    mAdapter.notifyItemChanged(adapterPosition)
                    saveLastMsg()
                }
                3 ->{
                    LaunchUtil.startWechatSimulatorSortActivity(this, mList, null, mListEntity.groupTableName, 1)
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
            } else {
                imageViews[i].setBackgroundResource(R.drawable.wechat_simulator_unselect)
            }
            //把new出来的ImageView控件添加到线性布局中
            mWechatSimulatorPreviewIndicatorLayout.addView(imageViews[i])
        }
    }
    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mListEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity//获取对方信息
        mLastMsgTime = mListEntity.lastTime
        mGroupRolesList.addAll(mListEntity.groupRoles)//获取聊天对象
        var recentRoles: WechatUserEntity
        for (i in mGroupRolesList.indices){
            recentRoles = mGroupRolesList[i]
            recentRoles.groupTableName = mListEntity.groupTableName
            if (recentRoles.isRecentRole){
                mOtherSideEntity = recentRoles//找到最近的聊天对象
            }
        }

        mMySideEntity = UserOperateUtil.getWechatSimulatorMySelf()
        GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mMyAvatarIv)
        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mOtherAvatarIv)

        mRoleAdapter = WechatGroupRolesAdapter(mGroupRolesList)
        mWechatGroupRolesRv.adapter = mRoleAdapter
        mWechatGroupRolesRv.addOnItemTouchListener(object : OnRecyclerItemClickListener(mWechatGroupRolesRv) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                for (i in mGroupRolesList.indices){
                    mGroupRolesList[i].isRecentRole = i == vh.adapterPosition
                }
                mOtherSideEntity = mGroupRolesList[vh.adapterPosition]
                GlideUtil.displayHead(this@WechatSimulatorPreviewGroupActivity, mOtherSideEntity.getAvatarFile(), mOtherAvatarIv)
                mComMsg = true
                mWechatBubbleLayout.visibility = View.GONE
                mAdapter.changeRole(mComMsg)
                showToast("切换到对方聊天")
                mAdapter.setOtherSide(mOtherSideEntity)
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
        mHelper = WechatSimulatorHelper(this, mListEntity.groupTableName)
        if (TextUtils.isEmpty(mListEntity.groupName)){
            mWechatSimulatorPreviewNickNameTv.text = StringUtils.insertFrontAndBack(mListEntity.groupRoleCount, "群聊(", ")")
        }else{
            mWechatSimulatorPreviewNickNameTv.text = mListEntity.groupName
        }

        val list = mHelper.queryAll()//获取和对方聊天的记录
        if (null != list){
            mList.addAll(list)
        }
        if (TextUtils.isEmpty(mOtherSideEntity.chatBg)){//没有聊天背景
            mWechatSimulatorPreviewRecyclerViewBgIv.visibility = View.GONE
            mAdapter =  WechatSimulatorPreviewGroupAdapter(mList, this)
            mAdapter.setOtherSide(mOtherSideEntity)
            mAdapter.showChatBg(false)
        }else{
            GlideUtil.displayAll(this, File(mOtherSideEntity.chatBg), mWechatSimulatorPreviewRecyclerViewBgIv)
            mWechatSimulatorPreviewRecyclerViewBgIv.visibility = View.VISIBLE
            mAdapter =  WechatSimulatorPreviewGroupAdapter(mList, this)
            mAdapter.setOtherSide(mOtherSideEntity)
            mAdapter.showChatBg(true)
        }
        mAdapter.showNickName(mListEntity.groupShowNickName)
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
            R.id.mOpenIv ->{
                startAnim()
            }
            R.id.mCloseHongbaoIv ->{
                stopAnim()
            }
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
                            mWechatBubbleLayout.visibility = View.GONE
                            mWechatSimulatorPreviewContentEt.clearFocus()
                        }
                    }
                }
            }
            R.id.mWechatSimulatorPreviewCleanIv ->{
                LaunchUtil.startWechatSimulatorEditGroupInfoActivity(this, mListEntity)
//                LaunchUtil.startWechatEditOtherActivity(this, mOtherSideEntity)
            }
            R.id.mWechatSimulatorPreviewMoreIv ->{
                if (mWechatSimulatorPreviewFunLayout.visibility == View.VISIBLE){
                    mWechatSimulatorPreviewFunLayout.visibility = View.GONE
                    mWechatBubbleLayout.visibility = View.GONE
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
                mWechatBubbleLayout.visibility = View.VISIBLE
            }
            R.id.mRecentLayout ->{//切换到对方聊天
                mComMsg = true
                mWechatBubbleLayout.visibility = View.GONE
                mAdapter.changeRole(mComMsg)
                showToast("切换到对方聊天")
                mAdapter.setOtherSide(mOtherSideEntity)
            }
            R.id.mMyLayout ->{//切换到自己说话
                mComMsg = false
                mWechatBubbleLayout.visibility = View.GONE
                mAdapter.changeRole(mComMsg)
                showToast("切换到自己聊天")
                mAdapter.setOtherSide(mOtherSideEntity)
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
                mWechatBubbleLayout.visibility = View.GONE
            }
            R.id.wechatSimulatorFunVideoLayout ->{//发送视频
                showToast("开发中")
            }
            R.id.wechatSimulatorFunRedPacketLayout ->{//发送红包
                if (mComMsg){
                    LaunchUtil.startWechatSimulatorCreateGroupRedPacketActivity(this, mOtherSideEntity, mGroupRolesList.size, mComMsg)
                }else{
                    LaunchUtil.startWechatSimulatorCreateGroupRedPacketActivity(this, mMySideEntity, mGroupRolesList.size, mComMsg)
                }
            }
            R.id.wechatSimulatorFunTransferLayout ->{//发送转账
                LaunchUtil.startWechatSimulatorCreateTransferActivity(this, mOtherSideEntity, null, 0)
            }
            R.id.wechatSimulatorFunGroupLayout ->{//群聊
                showToast("开发中")
            }
            R.id.wechatSimulatorFunFilesLayout ->{//文件
                LaunchUtil.startWechatSimulatorCreateFileActivity(this, mOtherSideEntity, null, 0)
            }
            R.id.wechatSimulatorFunSystemLayout ->{//发送系统消息
                LaunchUtil.startWechatSimulatorCreateGroupSystemMessageActivity(this, 0, mListEntity, null)
            }
        }
    }
    private fun initPageAdapter() {
        /**
         * 对于这几个想要动态载入的page页面，使用LayoutInflater.inflate()来找到其布局文件，并实例化为View对象
         */
        val inflater = LayoutInflater.from(this)
        val page1 = inflater.inflate(R.layout.item_wechat_simulator_group1, null)
        val page2 = inflater.inflate(R.layout.item_wechat_simulator_group2, null)
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
                if (null != data){
                    if (mFiles.size >= 1){
                        mFunType = 1
                        saveMsg()
                    }
                }else{
                    mFiles.clear()
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
            3 -> {
                if (entity?.receive == false){
                    entity?.receive?.let { msgEntity.receive = it }
                    entity?.isComMsg?.let { msgEntity.isComMsg = it }
                    entity?.isComMsg?.let { mComMsg = it }

                    entity?.redPacketCount?.let { msgEntity.redPacketCount = it }
                    msgEntity.msg = entity?.msg
                    msgEntity.money = entity?.money
                    msgEntity.needEventBus = false
                    msgEntity.joinReceiveRedPacket = entity?.joinReceiveRedPacket
                    msgEntity.redPacketSenderNickName = entity?.wechatUserNickName
                    msgEntity.receiveCompleteTime = entity?.receiveCompleteTime
                    setRole(msgEntity)
                }
            }
            /*4 -> {
                if (entity?.receive == false){
                    entity?.receive?.let { msgEntity.receive = it }
                    entity?.isComMsg?.let { msgEntity.isComMsg = it }
                    entity?.isComMsg?.let { mComMsg = it }

                    entity?.redPacketCount?.let { msgEntity.redPacketCount = it }
                    msgEntity.msg = entity?.msg
                    msgEntity.money = entity?.money
                    msgEntity.needEventBus = false
                    msgEntity.joinReceiveRedPacket = entity?.joinReceiveRedPacket
                    msgEntity.redPacketSenderNickName = entity?.wechatUserNickName
                    msgEntity.receiveCompleteTime = entity?.receiveCompleteTime
                    setRole(msgEntity)
                }
            }*/
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
            16 -> {
                msgEntity.fileEntity = entity?.fileEntity
                setRole(msgEntity)
            }
        }
        msgEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
//        msgEntity.wechatUserNickName = entity?.wechatUserNickName
        mLastMsgTime = msgEntity.lastTime
        mHelper.save(msgEntity)
        mList.add(msgEntity)
        mAdapter.notifyDataSetChanged()
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            saveLastMsg()
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
                msgEntity.wechatUserNickName = mOtherSideEntity.wechatUserNickName
            }else{
                msgEntity.avatarInt = mMySideEntity.avatarInt
                msgEntity.resourceName = mMySideEntity.resourceName
                msgEntity.avatarStr = mMySideEntity.wechatUserAvatar
                msgEntity.wechatUserId = mMySideEntity.wechatUserId
                msgEntity.wechatUserNickName = mMySideEntity.wechatUserNickName
            }
        }else{//对方说话
            if (mFunType == 4){
                msgEntity.resourceName = mMySideEntity.resourceName
                msgEntity.avatarInt = mMySideEntity.avatarInt
                msgEntity.avatarStr = mMySideEntity.wechatUserAvatar
                msgEntity.wechatUserId = mMySideEntity.wechatUserId
                msgEntity.wechatUserNickName = mMySideEntity.wechatUserNickName
            }else{
                msgEntity.avatarInt = mOtherSideEntity.avatarInt
                msgEntity.resourceName = mOtherSideEntity.resourceName
                msgEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
                msgEntity.wechatUserId = mOtherSideEntity.wechatUserId
                msgEntity.wechatUserNickName = mOtherSideEntity.wechatUserNickName
            }
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
    private fun saveLastMsg(){
        val lastEntity = mHelper.queryLastMsg()
        var listHelper = WechatSimulatorListHelper(this)
        if (null != lastEntity){
            val userEntity = mListEntity
            userEntity.unReadNum = "0"
            userEntity.showPoint = false

            userEntity.msgType = lastEntity.msgType.toString()
            userEntity.timeType = lastEntity.timeType
            userEntity.msg = lastEntity.msg
            userEntity.isComMsg = lastEntity.isComMsg
            userEntity.timeType = lastEntity.timeType
            userEntity.alreadyRead = lastEntity.alreadyRead
            userEntity.groupRedPacketInfo = lastEntity.groupRedPacketInfo
            userEntity.lastTime = mLastMsgTime
            userEntity.wechatUserNickName = lastEntity.wechatUserNickName
            userEntity.wechatUserId = lastEntity.wechatUserId
            listHelper.update(userEntity)
            EventBusUtil.post(userEntity)
        }
    }

    override fun onResume() {
        super.onResume()
        needVipForCover()
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME)//这里选择感应频率
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
        mCheckLuckyTv.visibility = View.GONE
        mMeTakeOtherRedPacket = true
        showRedpacketLayout()
        mRedpacketEntity = entity
        mRedpacketPosition = position
        GlideUtil.displayHead(this, entity.avatar, mRedpacketAvatarIv)
        mRedpacketNickNameTv.text = StringUtils.insertBack(entity.wechatUserNickName, "的红包")
        mRedpacketMsgTv.text = entity.msg
    }

    //对方领取我的红包
    override fun otherTakeMyRedPacket(entity: WechatScreenShotEntity, position: Int) {
        mCheckLuckyTv.visibility = View.VISIBLE
        mMeTakeOtherRedPacket = false
        showRedpacketLayout()
        mRedpacketEntity = entity
        mRedpacketPosition = position
        GlideUtil.displayHead(this, entity.avatar, mRedpacketAvatarIv)
        mRedpacketNickNameTv.text = StringUtils.insertBack(entity.wechatUserNickName, "的红包")
        mRedpacketMsgTv.text = entity.msg
    }
    override fun checkRedpacketDetails(entity: WechatScreenShotEntity, position: Int) {
        showLoadingDialog("正在加载...")
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(150L) // 非阻塞的延迟一秒（默认单位是毫秒）
            dismissLoadingDialog()
            LaunchUtil.startWechatSimulatorGroupRedPacketActivity(this@WechatSimulatorPreviewGroupActivity, entity)
        }

    }
    private var mFrameAnimation: FrameAnimation? = null
    /**
     * 带有动画的红包的位置
     */
    private var mRedpacketPosition = -1
    private lateinit var mRedpacketEntity: WechatScreenShotEntity
    /**
     * 是别人领取我的红包还是我领取别人的红包
     */
    private var mMeTakeOtherRedPacket = false
    private val mImgResIds = intArrayOf(R.drawable.wechat_hongbao_animation_1,
            R.drawable.wechat_hongbao_animation_2,
            R.drawable.wechat_hongbao_animation_3,
            R.drawable.wechat_hongbao_animation_4,
            R.drawable.wechat_hongbao_animation_5)
    private fun startAnim() {
        if (mFrameAnimation != null) {
            //如果正在转动，则直接返回
            return
        }
        mFrameAnimation = FrameAnimation(mAnimationIv, mImgResIds, 125, true)
        mFrameAnimation?.setAnimationListener(object : FrameAnimation.AnimationListener {
            override fun onAnimationStart() {
                mOpenIv.visibility = View.GONE
                mAnimationIv.visibility = View.VISIBLE

            }

            override fun onAnimationEnd() {
                Log.e("Animation-", "end")
            }

            override fun onAnimationRepeat() {
                Log.e("Animation-", "repeat")
            }

            override fun onAnimationPause() {
                mAnimationIv.setBackgroundResource(R.drawable.wechat_hongbao_open)
            }
        })
        mRedpacketEntity.receive = true
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(1600L) // 非阻塞的延迟一秒（默认单位是毫秒）
            runOnUiThread {
                stopAnim()
                //所有可能领红包的人
                val allRolesList = mGroupRolesList
                val redPacket = WechatGroupRedPacket()
                var receiveRedPacketRoleList: ArrayList<WechatUserEntity>
                //领红包的随机金额
                var redPacketList: List<Int>
                redPacketList = redPacket.splitRedPacket((mRedpacketEntity.money.toFloat() * 100).toInt(), mRedpacketEntity.redPacketCount)
                if (mMeTakeOtherRedPacket){
                    if (mRedpacketEntity.joinReceiveRedPacket){//“自己”参与领红包
                        receiveRedPacketRoleList = getSubStringByRandom(allRolesList, mRedpacketEntity.redPacketCount - 1)
                        receiveRedPacketRoleList.add(mMySideEntity)
                        var receiveRoles: WechatUserEntity
                        for (i in receiveRedPacketRoleList.indices){
                            receiveRoles = receiveRedPacketRoleList[i]
                            receiveRoles.money = StringUtils.keep2Point(redPacketList[i].toFloat() / 100)//分配红包金额 单位：分
                            receiveRoles.lastTime = TimeUtil.getCurrentTimeEndMs()
                            if (receiveRoles.wechatUserId == mMySideEntity.wechatUserId){//如果是自己
                                var msgEntity = WechatScreenShotEntity()
                                msgEntity.receive = true
                                msgEntity.msgType = 4
                                msgEntity.isComMsg = mRedpacketEntity.isComMsg
                                msgEntity.receiveTransferId = mRedpacketEntity.id
                                msgEntity.redPacketCount = mRedpacketEntity.redPacketCount
                                msgEntity.msg = mRedpacketEntity.msg
                                msgEntity.money = receiveRedPacketRoleList[i].money
                                msgEntity.needEventBus = false
                                msgEntity.redPacketSenderNickName = mRedpacketEntity.redPacketSenderNickName


                                msgEntity.avatarInt = receiveRoles.avatarInt
                                msgEntity.resourceName = receiveRoles.resourceName
                                msgEntity.avatarStr = receiveRoles.wechatUserAvatar
                                msgEntity.wechatUserId = receiveRoles.wechatUserId
                                msgEntity.wechatUserNickName = receiveRoles.wechatUserNickName
                                msgEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
                                msgEntity.groupRedPacketInfo = mRedpacketEntity
                                mLastMsgTime = msgEntity.lastTime
                                mHelper.save(msgEntity)
                                mList.add(msgEntity)
                                mAdapter.notifyDataSetChanged()
                            }
                        }
                    }else{//“自己”不参与领红包
                        receiveRedPacketRoleList = getSubStringByRandom(allRolesList, mRedpacketEntity.redPacketCount)
                        var receiveRoles: WechatUserEntity
                        for (i in receiveRedPacketRoleList.indices){
                            receiveRoles = receiveRedPacketRoleList[i]
                            receiveRoles.lastTime = TimeUtil.getCurrentTimeEndMs()
                            receiveRoles.money = StringUtils.keep2Point(redPacketList[i] / 100)//分配红包金额 单位：分
                        }
                    }
                    for (i in receiveRedPacketRoleList.indices){
                        receiveRedPacketRoleList[i].isBestLuck = false
                    }
                    receiveRedPacketRoleList.sortByDescending { it.money.toFloat() }
                    receiveRedPacketRoleList[0].isBestLuck = true//排序过之后获取第一个就是手气最佳
                    GlobalScope.launch { // 在一个公共线程池中创建一个协程
                        mHelper.update(mRedpacketEntity,false)
                        delay(100L) // 非阻塞的延迟一秒（默认单位是毫秒）
                        runOnUiThread{
                            mWechatSimulatorPreviewRecyclerView.scrollToPosition(mAdapter.itemCount - 1)
                        }
                    }
                    receiveRedPacketRoleList.shuffle()
                    mRedpacketEntity.receiveRedPacketRoleList = receiveRedPacketRoleList
                    mHelper.update(mRedpacketEntity, false)
                    mAdapter.notifyItemChanged(mRedpacketPosition)


                }else{
                    if (mRedpacketEntity.joinReceiveRedPacket){//“自己”参与领红包
                        receiveRedPacketRoleList = getSubStringByRandom(allRolesList, mRedpacketEntity.redPacketCount - 1)
                        receiveRedPacketRoleList.add(mMySideEntity)
                        for (i in receiveRedPacketRoleList.indices){
                            receiveRedPacketRoleList[i].money = StringUtils.keep2Point(redPacketList[i].toFloat() / 100)//分配红包金额 单位：分
                            receiveRedPacketRoleList[i].lastTime = TimeUtil.getCurrentTimeEndMs()
                        }
                    }else{//“自己”不参与领红包
                        receiveRedPacketRoleList = getSubStringByRandom(allRolesList, mRedpacketEntity.redPacketCount)
                        var receiveRoles: WechatUserEntity
                        for (i in receiveRedPacketRoleList.indices){
                            receiveRoles = receiveRedPacketRoleList[i]
                            receiveRoles.lastTime = TimeUtil.getCurrentTimeEndMs()
                            receiveRoles.money = StringUtils.keep2Point(redPacketList[i] / 100)//分配红包金额 单位：分
                        }
                    }
                    for (i in receiveRedPacketRoleList.indices){
                        receiveRedPacketRoleList[i].isBestLuck = false
                    }
                    receiveRedPacketRoleList.sortByDescending { it.money.toFloat() }
                    receiveRedPacketRoleList[0].isBestLuck = true//排序过之后获取第一个就是手气最佳

                    GlobalScope.launch { // 在一个公共线程池中创建一个协程
                        mHelper.update(mRedpacketEntity,false)
                        delay(100L) // 非阻塞的延迟一秒（默认单位是毫秒）
                        runOnUiThread{
                            mWechatSimulatorPreviewRecyclerView.scrollToPosition(mAdapter.itemCount - 1)
                        }
                    }
                    receiveRedPacketRoleList.shuffle()

                    mRedpacketEntity.receiveRedPacketRoleList = receiveRedPacketRoleList

                    mHelper.update(mRedpacketEntity, false)
                    mAdapter.notifyItemChanged(mRedpacketPosition)

                    var receiveRoles: WechatUserEntity
                    for (i in receiveRedPacketRoleList.indices){
                        receiveRoles = receiveRedPacketRoleList[i]
                        receiveRoles.money = StringUtils.keep2Point(redPacketList[i].toFloat() / 100)//分配红包金额 单位：分
                        receiveRoles.lastTime = TimeUtil.getCurrentTimeEndMs()
                        var msgEntity = WechatScreenShotEntity()
                        msgEntity.receive = true
                        msgEntity.msgType = 4
                        msgEntity.isComMsg = mRedpacketEntity.isComMsg
                        msgEntity.receiveTransferId = mRedpacketEntity.id
                        msgEntity.redPacketCount = mRedpacketEntity.redPacketCount
                        msgEntity.msg = mRedpacketEntity.msg
                        msgEntity.money = receiveRedPacketRoleList[i].money
                        msgEntity.needEventBus = false
                        msgEntity.redPacketSenderNickName = mRedpacketEntity.redPacketSenderNickName

                        msgEntity.avatarInt = receiveRoles.avatarInt
                        msgEntity.resourceName = receiveRoles.resourceName
                        msgEntity.avatarStr = receiveRoles.wechatUserAvatar
                        msgEntity.wechatUserId = receiveRoles.wechatUserId
                        msgEntity.wechatUserNickName = receiveRoles.wechatUserNickName
                        msgEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
                        msgEntity.groupRedPacketInfo = mRedpacketEntity
                        mLastMsgTime = msgEntity.lastTime
                        if (i == receiveRedPacketRoleList.size - 1){//最后一条领红包的记录
                            if (receiveRoles.wechatUserId != mMySideEntity.wechatUserId) {//如果最后一个领红包的人不是我
                                msgEntity.lastReceive = true
//                                msgEntity.msg = StringUtils.insertBack(msgEntity.msg, "，你的红包已被领完")
                            }
                        }
                        mHelper.save(msgEntity)
                        mList.add(msgEntity)
                        mAdapter.notifyDataSetChanged()
                        GlobalScope.launch { // 在一个公共线程池中创建一个协程
                            saveLastMsg()
                        }
                    }
                }
                val intent = Intent(this@WechatSimulatorPreviewGroupActivity, WechatSimulatorGroupRedPacketActivity::class.java)
                intent.putExtra(IntentKey.ENTITY, mRedpacketEntity)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }

    private fun stopAnim() {
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
        setNavigationBarBg(ColorFinal.NEW_WECHAT_TITLEBAR)
        mFrameAnimation?.release()
        mFrameAnimation = null
        mHonebaoAnimationBgLayout.visibility = View.GONE
    }
    private fun showRedpacketLayout() {
        mOpenIv.visibility = View.VISIBLE
        mAnimationIv.visibility = View.GONE
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR2)
        setNavigationBarBg(ColorFinal.WHITE)
        showLoadingDialog("正在加载...")
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            delay(300L) // 非阻塞的延迟一秒（默认单位是毫秒）
            runOnUiThread {
                dismissLoadingDialog()
                mHonebaoAnimationBgLayout.visibility = View.VISIBLE
            }
        }
        val animatorX = ObjectAnimator.ofFloat(mHonebaoAnimationLayout, "scaleX", 0.5f, 1.1F, 0.97F, 1.0F)
        animatorX.duration = 500
        animatorX.startDelay = 300
        animatorX.start()
        val animatorY = ObjectAnimator.ofFloat(mHonebaoAnimationLayout, "scaleY", 0.5f, 1.1F, 0.97F, 1.0F)
        animatorY.duration = 500
        animatorY.startDelay = 300
        animatorY.start()
    }
    /**
     * 随机抽取领红包的人
     */
    private fun getSubStringByRandom(list: MutableList<WechatUserEntity>, count: Int): ArrayList<WechatUserEntity> {
        val list2 = mutableListOf<WechatUserEntity>()
        list2.addAll(list)
        val backList = ArrayList<WechatUserEntity>()
        val random = Random()
        var backSum = 0
        backSum = if (list2.size >= count) {
            count
        } else {
            list2.size
        }
        for (i in 0 until backSum) {
            val target = random.nextInt(list2.size)
            backList.add(list2[target])
            list2.removeAt(target)
        }
        return backList
    }
    override fun myTransferWasReceive(entity: WechatScreenShotEntity, position: Int) {
        mHelper.update(entity, true)
    }
    override fun closeBottomMenu() {
        mWechatSimulatorPreviewFunLayout.visibility = View.GONE
        mWechatBubbleLayout.visibility = View.GONE
        mWechatSimulatorPreviewEmojiLayout.visibility = View.GONE
    }
    override fun changeUserInfo(entity: WechatScreenShotEntity) {
        val editEntity = WechatUserEntity()
        editEntity.wechatUserNickName = entity.wechatUserNickName
        editEntity.wechatUserId = entity.wechatUserId
        editEntity.avatarInt = entity.avatarInt
        editEntity.wechatUserAvatar = entity.avatarStr
        editEntity.resourceName = entity.resourceName
        editEntity.isComMsg = entity.isComMsg
        editEntity.pinyinNickName = OtherUtil.transformPinYin(entity.wechatUserNickName)
        LaunchUtil.startWechatSimulatorEditRoleActivity(this, editEntity, mListEntity.groupTableName, mListEntity)
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
    fun onChangeUserInfo(userEntity: WechatUserEntity) {
        if ("删除并刷新" == userEntity.eventBusTag){
            val list = mHelper.queryAll()//获取和对方聊天的记录
            mList.clear()
            if (null != list){
                mList.addAll(list)
                mAdapter.notifyDataSetChanged()
            }
            var entity: WechatUserEntity
            for (i in mGroupRolesList.indices){
                entity = mGroupRolesList[i]
                if (entity.wechatUserId == userEntity.wechatUserId){
                    entity.wechatUserNickName = userEntity.wechatUserNickName
                    entity.pinyinNickName = userEntity.pinyinNickName
                    entity.firstChar = userEntity.firstChar
                    entity.avatarFile = userEntity.avatarFile
                    entity.wechatUserAvatar = userEntity.wechatUserAvatar
                    if (entity.isRecentRole){
                        mOtherSideEntity.wechatUserNickName = userEntity.wechatUserNickName
                        mOtherSideEntity.pinyinNickName = userEntity.pinyinNickName
                        mOtherSideEntity.firstChar = userEntity.firstChar
                        mOtherSideEntity.avatarFile = userEntity.avatarFile
                        mOtherSideEntity.wechatUserAvatar = userEntity.wechatUserAvatar
                        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mOtherAvatarIv)
                    }
                }
            }
            mRoleAdapter.notifyDataSetChanged()
        }
        if ("编辑" == userEntity.eventBusTag){
            mGroupRolesList.clear()
            mGroupRolesList.addAll(userEntity.groupRoles)
            mListEntity.groupRoles.clear()
            mListEntity.groupRoles.addAll(userEntity.groupRoles)
            mRoleAdapter.notifyDataSetChanged()
            if (TextUtils.isEmpty(userEntity.groupName)){
                mWechatSimulatorPreviewNickNameTv.text = StringUtils.insertFrontAndBack(userEntity.groupRoleCount, "群聊(", ")")
            }else{
                mWechatSimulatorPreviewNickNameTv.text = userEntity.groupName
            }
            var roleEntity: WechatUserEntity
            for (i in userEntity.groupRoles.indices){
                roleEntity = userEntity.groupRoles[i]
                if (roleEntity.isRecentRole){
                    if (roleEntity.wechatUserId != mOtherSideEntity.wechatUserId){
                        mOtherSideEntity = roleEntity
                        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mOtherAvatarIv)
                    }
                }
            }
            mOtherSideEntity.chatBg = userEntity.chatBg
            mAdapter.setOtherSide(mOtherSideEntity)
            mAdapter.showNickName(userEntity.groupShowNickName)
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
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onOperateEvent(operate: String) {
        if ("清除" == operate){
            mHelper.deleteAll()
            mLastMsgTime = mList[mList.size - 1].lastTime
            mList.clear()
            mAdapter.notifyDataSetChanged()
        }
        if ("排序" == operate){
            val list = mHelper.queryAll()//获取和对方聊天的记录
            if (null != list){
                mList.clear()
            }
            mList.addAll(list)
            mAdapter.notifyDataSetChanged()
            GlobalScope.launch { // 在一个公共线程池中创建一个协程
//                delay(150L) // 非阻塞的延迟一秒（默认单位是毫秒）
                saveLastMsg()
                runOnUiThread{
                    mWechatSimulatorPreviewRecyclerView.scrollToPosition(mAdapter.itemCount - 1)
                }
            }
        }
    }

    override fun finish() {
        if (mHonebaoAnimationBgLayout.visibility == View.VISIBLE){
            mHonebaoAnimationBgLayout.visibility = View.GONE
            return
        }else{
            saveLastMsg()
        }
//        saveLastMsg()
        super.finish()
    }
}
