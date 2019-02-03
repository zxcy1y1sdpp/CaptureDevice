package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.adapter.WechatScreenShotAdapter
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
import kotlinx.android.synthetic.main.activity_wechat_screenshot_create.*
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
class WechatScreenShotActivity : BaseCreateActivity(), ChoiceTalkTypeDialog.ChoiceTypeListener, WechatScreenShotAdapter.DeleteListener {
    override fun delete(entity: WechatScreenShotEntity, position: Int) {
        mHelper.delete(entity)
        mList.remove(entity)
        mAdapter?.notifyItemRemoved(position)
    }

    override fun choiceType(title: String, msgType: String) {
        when(title){
            "时间" ->{
                LaunchUtil.startWechatCreateTimeActivity(this, mOtherSideEntity, null, 0)
//                initTimePickerView(tag = "创建")
            }
            "文本" ->{
                LaunchUtil.startWechatCreateTextActivity(this, mOtherSideEntity, null, 0)
            }
            "图片和视频" ->{
                LaunchUtil.startWechatCreatePictureAndVideoActivity(this, mOtherSideEntity, null, 0)
            }
            "红包" ->{
                LaunchUtil.startWechatCreateRedPacketActivity(this, mOtherSideEntity, null, 0)
            }
            "转账" ->{
                LaunchUtil.startWechatCreateTransferActivity(this, mOtherSideEntity, null, 0)
            }
            "语音" ->{
                LaunchUtil.startWechatCreateVoiceActivity(this, mOtherSideEntity, null, 0)
            }
            "系统提示" ->{
                LaunchUtil.startWechatCreateSystemMessageActivity(this, mOtherSideEntity, null, 0, "微信")
            }
        }
    }

    private lateinit var mHelper: WechatScreenShotHelper
    private lateinit var mRoleHelper: RoleLibraryHelper
    private var mAdapter: WechatScreenShotAdapter? = null
    private val mList: ArrayList<WechatScreenShotEntity> = arrayListOf()
    private lateinit var mEditEntity: WechatScreenShotEntity
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
                val closeItem = SwipeMenuItem(this@WechatScreenShotActivity)
                        .setBackground(R.color.red_btn_bg_color)
                        .setText("删除")
                        .setTextColor(ContextCompat.getColor(this@WechatScreenShotActivity, R.color.white))
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
    override fun setLayoutResourceId() = R.layout.activity_wechat_screenshot_create

    override fun needLoadingView() = false

    override fun initAllViews() {
        setBlackTitle("聊天对话", 2)
        mHelper = WechatScreenShotHelper(this)
        mRoleHelper = RoleLibraryHelper(this)
        mMySideEntity = getMySelf()
        mOtherSideEntity = UserOperateUtil.getOtherSide()
        GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mWechatScreenShotMySideAvatarIv)
        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mWechatScreenShotOtherSideAvatarIv)
        mHelper.queryAll()?.let { mList.addAll(it) }
        mAdapter = WechatScreenShotAdapter(mList, this)
        mWechatScreenShotCreateMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator)
        mWechatScreenShotCreateMenuRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener)
        mWechatScreenShotCreateMenuRecyclerView.isLongPressDragEnabled = true // 拖拽排序，默认关闭。
        registerEventBus()
    }

    override fun initViewsListener() {
        wechatAddItemBtn.setOnClickListener(this)
        previewBtn.setOnClickListener(this)
        mWechatScreenShotCreateChangeRoleLayout.setOnClickListener(this)
        overallAllRightWithOutBgTv.setOnClickListener(this)
        mWechatScreenShotCreateMenuRecyclerView.setSwipeItemClickListener { _, position ->
            mEditEntity = mList[position]
            when(mEditEntity.msgType){
                0 ->{
                    LaunchUtil.startWechatCreateTextActivity(this@WechatScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                1 ->{
                    LaunchUtil.startWechatCreatePictureAndVideoActivity(this@WechatScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                2 ->{
                    LaunchUtil.startWechatCreateTimeActivity(this, mOtherSideEntity, mEditEntity, 1)
                    /*val calendar = Calendar.getInstance()
                    calendar.timeInMillis = mEditEntity.time*/
//                    initTimePickerView(tag = "修改", selectedDate = calendar)
                }
                3, 4 ->{
                    LaunchUtil.startWechatCreateRedPacketActivity(this@WechatScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                5, 6 ->{
                    LaunchUtil.startWechatCreateTransferActivity(this@WechatScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                7 ->{
                    LaunchUtil.startWechatCreateVoiceActivity(this@WechatScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                8 ->{
                    LaunchUtil.startWechatCreateSystemMessageActivity(this, mOtherSideEntity, mEditEntity, 1, "微信")
                }
            }
        }
        mWechatScreenShotCreateMenuRecyclerView.setOnItemMoveListener(object : OnItemMoveListener {
            override fun onItemMove(srcHolder: RecyclerView.ViewHolder, targetHolder: RecyclerView.ViewHolder): Boolean {
                if (srcHolder.itemViewType != targetHolder.itemViewType) return false

                // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
                val fromPosition = srcHolder.adapterPosition - mWechatScreenShotCreateMenuRecyclerView.headerItemCount
                val toPosition = targetHolder.adapterPosition - mWechatScreenShotCreateMenuRecyclerView.headerItemCount

                Collections.swap(mList, fromPosition, toPosition)
                mAdapter?.notifyItemMoved(fromPosition, toPosition)
                return true
            }

            override fun onItemDismiss(srcHolder: RecyclerView.ViewHolder) {

            }
        })
        mWechatScreenShotCreateMenuRecyclerView.adapter = mAdapter
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatScreenShotCreateChangeRoleLayout ->{
                LaunchUtil.startWechatCreateSettingInfoActivity(this, mMySideEntity, mOtherSideEntity, 0)
            }
            R.id.wechatAddItemBtn ->{
                val dialog = ChoiceTalkTypeDialog()
                dialog.setListener(this)
                dialog.show(supportFragmentManager, "chatType")
            }
            R.id.previewBtn ->{
                LaunchUtil.startWechatScreenShotPreviewActivity(this, mList)
            }
            R.id.overallAllRightWithOutBgTv ->{
                SweetAlertDialog(this@WechatScreenShotActivity, SweetAlertDialog.WARNING_TYPE)
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

    /**
     * 修改我的信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun changeRoleMySide(meSelfEntity: WechatUserEntity) {
        if (meSelfEntity.meSelf){
            mMySideEntity = meSelfEntity
            GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mWechatScreenShotMySideAvatarIv)
            GlobalScope.launch { // 在一个公共线程池中创建一个协程
                if (!mList.isNullOrEmpty()){
                    var wechatEntity: WechatScreenShotEntity
                    for (i in 0 until mList.size) {
                        wechatEntity = mList[i]
                        if (!wechatEntity.isComMsg){
                            wechatEntity.resourceName = mMySideEntity.resourceName
                            wechatEntity.avatarInt = mMySideEntity.resAvatar
                            wechatEntity.avatarStr = mMySideEntity.wechatUserAvatar
                            mHelper.update(wechatEntity)
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
            GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mWechatScreenShotOtherSideAvatarIv)
            GlobalScope.launch { // 在一个公共线程池中创建一个协程
                if (!mList.isNullOrEmpty()){
                    var wechatEntity: WechatScreenShotEntity
                    for (i in 0 until mList.size) {
                        wechatEntity = mList[i]
                        if (wechatEntity.isComMsg){
                            wechatEntity.resourceName = mOtherSideEntity.resourceName
                            wechatEntity.avatarInt = mOtherSideEntity.resAvatar
                            wechatEntity.avatarStr = mOtherSideEntity.wechatUserAvatar
                            mHelper.update(wechatEntity)
                        }
                    }
                    runOnUiThread {
                        mAdapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }
    /*@Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        if (timeEntity.tag == "修改"){
            var position = mList.indexOf(mEditEntity)
            mEditEntity.time = timeEntity.timeLong
            mHelper.update(mEditEntity)
            mAdapter?.notifyItemRangeChanged(position, 1)
        }else if (timeEntity.tag == "创建"){
            var entity = WechatScreenShotEntity("", -1, "", 2, true, timeEntity.timeLong, -1)
            mHelper.save(entity)
            mList.add(entity)
            mAdapter?.notifyItemInserted(mList.size - 1)
        }
    }*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNeedChangeOrAddData(entity: WechatScreenShotEntity) {
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
