package app.jietuqi.cn.ui.alipayscreenshot.ui.create

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.ui.alipayscreenshot.adapter.AlipayScreenShotAdapter
import app.jietuqi.cn.ui.alipayscreenshot.db.AlipayScreenShotHelper
import app.jietuqi.cn.ui.alipayscreenshot.entity.AlipayScreenShotEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.util.UserOperateUtil.getAlipayMySelf
import app.jietuqi.cn.widget.dialog.ChoiceTalkTypeDialog
import app.jietuqi.cn.widget.dialog.customdialog.EnsureDialog
import com.yanzhenjie.recyclerview.swipe.*
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener
import kotlinx.android.synthetic.main.activity_alipay_screenshot_create.*
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
 * 用途： 支付宝单聊生成页面
 */
class AlipayScreenShotActivity : BaseCreateActivity(), ChoiceTalkTypeDialog.ChoiceTypeListener, AlipayScreenShotAdapter.DeleteListener {
    override fun delete(entity: AlipayScreenShotEntity, position: Int) {
        mHelper.delete(entity)
        mList.remove(entity)
        mAdapter?.notifyItemRemoved(position)
    }

    override fun choiceType(title: String, msgType: String) {
        when(title){
            "时间" ->{
                initTimePickerView(tag = "创建")
            }
            "文本" ->{
                LaunchUtil.startAlipayCreateTextActivity(this, mOtherSideEntity, null, 0)
            }
            "图片和视频" ->{
                LaunchUtil.startAlipayCreatePictureActivity(this, mOtherSideEntity, null, 0)
            }
            "红包" ->{
                LaunchUtil.startAlipayCreateRedPacketActivity(this, mOtherSideEntity, null, 0)
            }
            "转账" ->{
                LaunchUtil.startAlipayCreateTransferActivity(this, mOtherSideEntity, null, 0)
            }
            "语音" ->{
                LaunchUtil.startAlipayCreateVoiceActivity(this, mOtherSideEntity, null, 0)
            }
            "系统提示" ->{
                LaunchUtil.startAlipayCreateSystemMessageActivity(this, mOtherSideEntity, null, 0)
            }
        }
    }

    private lateinit var mHelper: AlipayScreenShotHelper
    private lateinit var mRoleHelper: RoleLibraryHelper
    private var mAdapter: AlipayScreenShotAdapter? = null
    private val mList: ArrayList<AlipayScreenShotEntity> = arrayListOf()
    private lateinit var mEditEntity: AlipayScreenShotEntity
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
                val closeItem = SwipeMenuItem(this@AlipayScreenShotActivity)
                        .setBackground(R.color.red_btn_bg_color)
                        .setText("删除")
                        .setTextColor(ContextCompat.getColor(this@AlipayScreenShotActivity, R.color.white))
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
//        val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。

        if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
            mHelper.delete(mList[adapterPosition])
            mList.removeAt(adapterPosition)
            mAdapter?.notifyItemRemoved(adapterPosition)
        }
    }
    override fun setLayoutResourceId() = R.layout.activity_alipay_screenshot_create

    override fun needLoadingView() = false

    override fun initAllViews() {
        setBlackTitle("支付宝单聊", 2)
        mHelper = AlipayScreenShotHelper(this)
        mRoleHelper = RoleLibraryHelper(this)
        mOtherSideEntity = UserOperateUtil.getAlipayOtherSide()
        mMySideEntity = getAlipayMySelf()
        Log.e("limit -- ", mMySideEntity.wechatUserId + "---------" + mOtherSideEntity.wechatUserId)
        GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mAlipayScreenShotMySideAvatarIv)
        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mAlipayScreenShotOtherSideAvatarIv)
        mHelper.queryAll()?.let { mList.addAll(it) }
        mAdapter = AlipayScreenShotAdapter(mList, this)
        mAlipayScreenShotCreateMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator)
        mAlipayScreenShotCreateMenuRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener)
        mAlipayScreenShotCreateMenuRecyclerView.isLongPressDragEnabled = true // 拖拽排序，默认关闭。
        registerEventBus()
    }

    override fun initViewsListener() {
        wechatAddItemBtn.setOnClickListener(this)
        previewBtn.setOnClickListener(this)
        mAlipayScreenShotCreateChangeRoleLayout.setOnClickListener(this)
        overallAllRightWithOutBgTv.setOnClickListener(this)
        mAlipayScreenShotCreateMenuRecyclerView.setSwipeItemClickListener { _, position ->
            mEditEntity = mList[position]
            when(mEditEntity.msgType){
                0 ->{
                    LaunchUtil.startAlipayCreateTextActivity(this@AlipayScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                1 ->{
                    LaunchUtil.startAlipayCreatePictureActivity(this@AlipayScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                2 ->{
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = mEditEntity.time
                    initTimePickerView(tag = "修改", selectedDate = calendar)
                }
                3, 4 ->{
                    LaunchUtil.startAlipayCreateRedPacketActivity(this@AlipayScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                5 ->{
                    LaunchUtil.startAlipayCreateTransferActivity(this@AlipayScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                7 ->{
                    LaunchUtil.startAlipayCreateVoiceActivity(this@AlipayScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                8 ->{
                    LaunchUtil.startAlipayCreateSystemMessageActivity(this, mOtherSideEntity, mEditEntity, 1)
                }
            }
        }
        mAlipayScreenShotCreateMenuRecyclerView.setOnItemMoveListener(object : OnItemMoveListener {
            override fun onItemMove(srcHolder: RecyclerView.ViewHolder, targetHolder: RecyclerView.ViewHolder): Boolean {
                if (srcHolder.itemViewType != targetHolder.itemViewType) return false

                // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
                val fromPosition = srcHolder.adapterPosition - mAlipayScreenShotCreateMenuRecyclerView.headerItemCount
                val toPosition = targetHolder.adapterPosition - mAlipayScreenShotCreateMenuRecyclerView.headerItemCount

                Collections.swap(mList, fromPosition, toPosition)
                mAdapter?.notifyItemMoved(fromPosition, toPosition)
                return true
            }
            override fun onItemDismiss(srcHolder: RecyclerView.ViewHolder) {}
        })

        mAlipayScreenShotCreateMenuRecyclerView.setOnItemStateChangedListener { _, actionState ->
            if (actionState === ACTION_STATE_IDLE) {
                showQQWaitDialog("请稍后")
                mHelper.deleteAll()

                if (mHelper.saveAll(mList)){
                    dismissQQDialog()
                }else{
                    showToast("排序失败")
                }
            }
        }
        mAlipayScreenShotCreateMenuRecyclerView.adapter = mAdapter
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAlipayScreenShotCreateChangeRoleLayout ->{
                LaunchUtil.startWechatCreateSettingInfoActivity(this, mMySideEntity, mOtherSideEntity, 1)
            }
            R.id.wechatAddItemBtn ->{
                val dialog = ChoiceTalkTypeDialog()
                dialog.setListener(this)
                dialog.show(supportFragmentManager, "chatType")
            }
            R.id.previewBtn ->{
                LaunchUtil.startAlipayScreenShotPreviewActivity(this, mList)
            }
            R.id.overallAllRightWithOutBgTv ->{
                EnsureDialog(this@AlipayScreenShotActivity).builder()
                        .setGravity(Gravity.CENTER)//默认居中，可以不设置
                        .setTitle("确定要删除所有数据吗？")//可以不设置标题颜色，默认系统颜色
                        .setSubTitle("点击删除将清空所有的历史数据!")
                        .setCancelable(false)
                        .setNegativeButton("取消") {}
                        .setPositiveButton("删除") {
                            mHelper.deleteAll()
                            mList.clear()
                            mAdapter?.notifyDataSetChanged()
                        }.show()
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeRoleMySide(meSelfEntity: WechatUserEntity) {
        if (meSelfEntity.meSelf){
            mMySideEntity = meSelfEntity
            GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mAlipayScreenShotMySideAvatarIv)
            GlobalScope.launch { // 在一个公共线程池中创建一个协程
                if (!mList.isNullOrEmpty()){
                    var alipayEntity: AlipayScreenShotEntity
                    for (i in 0 until mList.size) {
                        alipayEntity = mList[i]
                        if (!alipayEntity.isComMsg){
                            alipayEntity.resourceName = mMySideEntity.resourceName
                            alipayEntity.avatarInt = mMySideEntity.avatarInt
                            alipayEntity.avatarStr = mMySideEntity.wechatUserAvatar
                            mHelper.update(alipayEntity)
                        }
                    }
                    runOnUiThread {
                        mAdapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    /**
     * 修改对方的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeRoleOtherSide(otherSideEntity: WechatUserEntity) {
        if (!otherSideEntity.meSelf){
            mOtherSideEntity = otherSideEntity
            GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mAlipayScreenShotOtherSideAvatarIv)
            GlobalScope.launch { // 在一个公共线程池中创建一个协程
                if (!mList.isNullOrEmpty()){
                    var alipayEntity: AlipayScreenShotEntity
                    for (i in 0 until mList.size) {
                        alipayEntity = mList[i]
                        if (alipayEntity.isComMsg){
                            alipayEntity.resourceName = mOtherSideEntity.resourceName
                            alipayEntity.avatarInt = mOtherSideEntity.avatarInt
                            alipayEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
                            mHelper.update(alipayEntity)
                        }
                    }
                    runOnUiThread {
                        mAdapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        if (timeEntity.tag == "修改"){
            var position = mList.indexOf(mEditEntity)
            mEditEntity.time = timeEntity.timeLong
            mHelper.update(mEditEntity)
            mAdapter?.notifyItemRangeChanged(position, 1)
        }else if (timeEntity.tag == "创建"){
            var entity = AlipayScreenShotEntity("", -1, "", 2, true, timeEntity.timeLong, -1)
            mHelper.save(entity)
            mList.add(entity)
            mAdapter?.notifyItemInserted(mList.size - 1)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNeedChangeOrAddData(entity: AlipayScreenShotEntity) {
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
