package app.jietuqi.cn.ui.qqscreenshot.ui.create

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.ui.alipayscreenshot.db.AlipayScreenShotHelper
import app.jietuqi.cn.ui.alipayscreenshot.entity.AlipayScreenShotEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.qqscreenshot.adapter.QQScreenShotAdapter
import app.jietuqi.cn.ui.qqscreenshot.db.QQScreenShotHelper
import app.jietuqi.cn.ui.qqscreenshot.entity.QQScreenShotEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.ui.wechatscreenshot.db.WechatScreenShotHelper
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.util.UserOperateUtil.getMySelf
import app.jietuqi.cn.widget.dialog.ChoiceTalkTypeDialog
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import com.yanzhenjie.recyclerview.swipe.*
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener
import kotlinx.android.synthetic.main.activity_qq_screenshot_create.*
import kotlinx.android.synthetic.main.include_base_bottom_add_item.*
import kotlinx.android.synthetic.main.include_base_overall_top_black.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 作者： liuyuanbo on 2018/11/29 12:17.
 * 时间： 2018/11/29 12:17
 * 邮箱： 972383753@qq.com
 * 用途： 微信单聊生成页面
 */
class QQScreenShotActivity : BaseCreateActivity(), ChoiceTalkTypeDialog.ChoiceTypeListener {
    override fun choiceType(title: String, msgType: String) {
        when(title){
            "时间" ->{
                initTimePickerView(tag = "创建")
            }
            "文本" ->{
                LaunchUtil.startQQCreateTextActivity(this, mOtherSideEntity, null, 0)
            }
            "图片和视频" ->{
                LaunchUtil.startQQCreatePictureActivity(this, mOtherSideEntity, null, 0)
            }
            "红包" ->{
                LaunchUtil.startQQCreateRedPacketActivity(this, mOtherSideEntity, null, 0)
            }
            "转账" ->{
                LaunchUtil.startQQCreateTransferActivity(this, mOtherSideEntity, null, 0)
            }
            "语音" ->{
                LaunchUtil.startQQCreateVoiceActivity(this, mOtherSideEntity, null, 0)
            }
            "系统提示" ->{
                LaunchUtil.startQQCreateSystemMessageActivity(this, mOtherSideEntity, null, 0, "微信")
            }
        }
    }

    private lateinit var mHelper: QQScreenShotHelper
    private lateinit var mRoleHelper: RoleLibraryHelper
    private var mAdapter: QQScreenShotAdapter? = null
    private val mList: ArrayList<QQScreenShotEntity> = arrayListOf()
    private lateinit var mEditEntity: QQScreenShotEntity
    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private val swipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(swipeLeftMenu: SwipeMenu, swipeRightMenu: SwipeMenu, viewType: Int) {
            val width = resources.getDimensionPixelSize(R.dimen.wechatMoneyTextSize)

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            val height = ViewGroup.LayoutParams.MATCH_PARENT

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            run {
                val closeItem = SwipeMenuItem(this@QQScreenShotActivity)
                        .setBackground(R.color.red_btn_bg_color)
                        .setText("删除")
                        .setTextColor(ContextCompat.getColor(this@QQScreenShotActivity, R.color.white))
//                        .setTextSize(R.dimen.wechatNormalTextSize)
                        .setWidth(width)
                        .setHeight(height)
                swipeRightMenu.addMenuItem(closeItem) // 添加菜单到左侧。
            }
        }
    }
    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private val mMenuItemClickListener = SwipeMenuItemClickListener { menuBridge ->
        menuBridge.closeMenu()

        val direction = menuBridge.direction // 左侧还是右侧菜单。
        val adapterPosition = menuBridge.adapterPosition // RecyclerView的Item的position。

        if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
            mHelper.delete(mList[adapterPosition])
            mList.removeAt(adapterPosition)
            mAdapter?.notifyItemRemoved(adapterPosition)
        }
    }
    override fun setLayoutResourceId() = R.layout.activity_qq_screenshot_create

    override fun needLoadingView() = false

    override fun initAllViews() {
        registerEventBus()
        setBlackTitle("QQ单聊", 2)
        mHelper = QQScreenShotHelper(this)
        mRoleHelper = RoleLibraryHelper(this)
        mOtherSideEntity = UserOperateUtil.getOtherSide()
        mMySideEntity = getMySelf()
        Log.e("limit -- ", mMySideEntity.wechatUserId + "---------" + mOtherSideEntity.wechatUserId)
        GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(),mQQScreenShotMySideAvatarIv)
        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(),mQQScreenShotOtherSideAvatarIv)
        mHelper.queryAll()?.let { mList.addAll(it) }
        mAdapter = QQScreenShotAdapter(mList)
        mQQScreenShotCreateMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator)
        mQQScreenShotCreateMenuRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener)
        mQQScreenShotCreateMenuRecyclerView.isLongPressDragEnabled = true // 拖拽排序，默认关闭。
    }

    override fun initViewsListener() {
        wechatAddItemBtn.setOnClickListener(this)
        previewBtn.setOnClickListener(this)
        mQQScreenShotCreateChangeRoleLayout.setOnClickListener(this)
        overallAllRightWithOutBgTv.setOnClickListener(this)
        mQQScreenShotCreateMenuRecyclerView.setSwipeItemClickListener { _, position ->
            mEditEntity = mList[position]
            when(mEditEntity.msgType){
                0 ->{
                    LaunchUtil.startQQCreateTextActivity(this@QQScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                1 ->{
                    LaunchUtil.startQQCreatePictureActivity(this@QQScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                2 ->{
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = mEditEntity.time
                    initTimePickerView(tag = "修改", selectedDate = calendar)
                }
                3, 4 ->{
                    LaunchUtil.startQQCreateRedPacketActivity(this@QQScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                5, 6 ->{
                    LaunchUtil.startQQCreateTransferActivity(this@QQScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                7 ->{
                    LaunchUtil.startQQCreateVoiceActivity(this@QQScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                8 ->{
                    LaunchUtil.startQQCreateSystemMessageActivity(this, mOtherSideEntity, mEditEntity, 1, "微信")
                }
            }
        }
        mQQScreenShotCreateMenuRecyclerView.setOnItemMoveListener(object : OnItemMoveListener {
            override fun onItemMove(srcHolder: RecyclerView.ViewHolder, targetHolder: RecyclerView.ViewHolder): Boolean {
                if (srcHolder.itemViewType != targetHolder.itemViewType) return false

                // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
                val fromPosition = srcHolder.adapterPosition -mQQScreenShotCreateMenuRecyclerView.headerItemCount
                val toPosition = targetHolder.adapterPosition -mQQScreenShotCreateMenuRecyclerView.headerItemCount

                Collections.swap(mList, fromPosition, toPosition)
                mAdapter?.notifyItemMoved(fromPosition, toPosition)
                return true
            }

            override fun onItemDismiss(srcHolder: RecyclerView.ViewHolder) {

            }
        })
        mQQScreenShotCreateMenuRecyclerView.adapter = mAdapter
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mQQScreenShotCreateChangeRoleLayout ->{
                LaunchUtil.startWechatCreateSettingInfoActivity(this, mMySideEntity, mOtherSideEntity, 2)
            }
            R.id.wechatAddItemBtn ->{
                val dialog = ChoiceTalkTypeDialog()
                dialog.setListener(this)
                dialog.show(supportFragmentManager, "chatType")
            }
            R.id.previewBtn ->{
                LaunchUtil.startQQScreenShotPreviewActivity(this, mList)
            }
            R.id.overallAllRightWithOutBgTv ->{
                SweetAlertDialog(this@QQScreenShotActivity, SweetAlertDialog.WARNING_TYPE)
                        .setCanTouchOutSideCancle(false)
                        .canCancle(false)
                        .setTitleText("删除提示！")
                        .setContentText("点击删除将清空所有的历史数据")
                        .setConfirmText("删除")
                        .setCancelText("取消")
                        .setConfirmClickListener { sweetAlertDialog ->
                            sweetAlertDialog.dismissWithAnimation()
                            mHelper.deleteAll()
                            mList.clear()
                            mAdapter?.notifyDataSetChanged()
                        }.setCancelClickListener {
                            it.dismissWithAnimation()
                        }.show()
            }
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.ROLE_CHANGE -> {
                if (data?.extras?.containsKey(IntentKey.MY_SIDE) == true){
                    mMySideEntity= data.getSerializableExtra(IntentKey.MY_SIDE) as WechatUserEntity
                    GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(),mQQScreenShotMySideAvatarIv)
                    GlobalScope.launch { // 在一个公共线程池中创建一个协程
                        var entity: QQScreenShotEntity
                        for (i in 0 until mList.size) {
                            entity = mList[i]
                            if (!entity.isComMsg){
                                entity.avatarInt = mMySideEntity.resAvatar
                                entity.avatarStr = mMySideEntity.wechatUserAvatar
                                mHelper.update(entity)
                            }
                        }
                        runOnUiThread {
                            mAdapter?.notifyDataSetChanged()
                        }
                    }
                }
                if (data?.extras?.containsKey(IntentKey.OTHER_SIDE) == true){
                    mOtherSideEntity = data.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity
                    GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(),mQQScreenShotOtherSideAvatarIv)
                    GlobalScope.launch { // 在一个公共线程池中创建一个协程
                        var entity: QQScreenShotEntity
                        for (i in 0 until mList.size) {
                            entity = mList[i]
                            if (entity.isComMsg){
                                entity.avatarInt = mOtherSideEntity.resAvatar
                                entity.avatarStr = mOtherSideEntity.wechatUserAvatar
                                mHelper.update(entity)
                            }
                        }
                        runOnUiThread {
                            mAdapter?.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        if (timeEntity.tag == "修改"){
            var position = mList.indexOf(mEditEntity)
            mEditEntity.time = timeEntity.timeLong
            mHelper.update(mEditEntity)
            mAdapter?.notifyItemRangeChanged(position, 1)
        }else if (timeEntity.tag == "创建"){
            var entity = QQScreenShotEntity("", -1, "", 2, true, timeEntity.timeLong, -1)
            mHelper.save(entity)
            mList.add(entity)
            mAdapter?.notifyItemInserted(mList.size - 1)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNeedChangeOrAddData(entity: QQScreenShotEntity) {
        when {
            entity.tag == 0 -> {
                mList.add(entity)
                mAdapter?.notifyItemInserted(entity.position)
            }
            entity.tag == 1 -> {
                mList[entity.position] = entity
                mAdapter?.notifyItemChanged(entity.position)
            }
            entity.tag == 2 -> {
                mList.remove(entity)
                mAdapter?.notifyItemRemoved(entity.position)
            }
        }
    }
    /**
     * 修改我的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeRoleMySide(meSelfEntity: WechatUserEntity) {
        if (meSelfEntity.meSelf){
            mMySideEntity = meSelfEntity
            GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mQQScreenShotMySideAvatarIv)
            changeMyInfo()
        }
    }
    /**
     * 修改对方的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeRoleOtherSide(otherSideEntity: WechatUserEntity) {
        if (!otherSideEntity.meSelf){
            mOtherSideEntity = otherSideEntity
            GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mQQScreenShotOtherSideAvatarIv)
            changeOtherInfo()
        }
    }
    private fun changeMyInfo(){
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            if (!mList.isNullOrEmpty()){
                var qqEntity: QQScreenShotEntity
                for (i in 0 until mList.size) {
                    qqEntity = mList[i]
                    if (!qqEntity.isComMsg){
                        qqEntity.avatarInt = mMySideEntity.resAvatar
                        qqEntity.avatarStr = mMySideEntity.wechatUserAvatar
                        mHelper.update(qqEntity)
                    }
                }
                runOnUiThread {
                    mAdapter?.notifyDataSetChanged()
                }
            }
        }
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            val wechatHelper = WechatScreenShotHelper(this@QQScreenShotActivity)
            val list = wechatHelper.queryAll()
            if (!list.isNullOrEmpty()){
                var wechatEntity: WechatScreenShotEntity
                for (i in 0 until list.size) {
                    wechatEntity = list[i]
                    if (!wechatEntity.isComMsg){
                        wechatEntity.avatarInt = mMySideEntity.resAvatar
                        wechatEntity.avatarStr = mMySideEntity.wechatUserAvatar
                        wechatHelper.update(wechatEntity)
                    }
                }
            }
        }
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            val alipayHelper = AlipayScreenShotHelper(this@QQScreenShotActivity)
            val list = alipayHelper.queryAll()
            if (!list.isNullOrEmpty()){
                var alipayEntity: AlipayScreenShotEntity
                for (i in 0 until list.size) {
                    alipayEntity = list[i]
                    if (!alipayEntity.isComMsg){
                        alipayEntity.avatarInt = mMySideEntity.resAvatar
                        alipayEntity.avatarStr = mMySideEntity.wechatUserAvatar
                        alipayHelper.update(alipayEntity)
                    }
                }
            }
        }
    }
    private fun changeOtherInfo(){
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            if (!mList.isNullOrEmpty()){
                var qqyEntity: QQScreenShotEntity
                for (i in 0 until mList.size) {
                    qqyEntity = mList[i]
                    if (qqyEntity.isComMsg){
                        qqyEntity.avatarInt = mOtherSideEntity.resAvatar
                        qqyEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
                        mHelper.update(qqyEntity)
                    }
                }
                runOnUiThread {
                    mAdapter?.notifyDataSetChanged()
                }
            }
        }
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            val wechatHelper = WechatScreenShotHelper(this@QQScreenShotActivity)
            val list = wechatHelper.queryAll()
            if (!list.isNullOrEmpty()){
                var wechatEntity: WechatScreenShotEntity
                for (i in 0 until list.size) {
                    wechatEntity = list[i]
                    if (wechatEntity.isComMsg){
                        wechatEntity.avatarInt = mOtherSideEntity.resAvatar
                        wechatEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
                        wechatHelper.update(wechatEntity)
                    }
                }
            }
        }
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            val alipayHelper = AlipayScreenShotHelper(this@QQScreenShotActivity)
            val list = alipayHelper.queryAll()
            if (!list.isNullOrEmpty()){
                var alipayEntity: AlipayScreenShotEntity
                for (i in 0 until list.size) {
                    alipayEntity = list[i]
                    if (alipayEntity.isComMsg){
                        alipayEntity.avatarInt = mOtherSideEntity.resAvatar
                        alipayEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
                        alipayHelper.update(alipayEntity)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        var i = 0
        val size = mList.size
        mHelper.deleteAll()
        while (i < size) {
            mHelper.save(mList[i])
            i++
        }
    }
}
