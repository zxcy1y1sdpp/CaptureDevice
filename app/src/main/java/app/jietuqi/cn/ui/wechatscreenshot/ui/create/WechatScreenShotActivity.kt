package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE
import android.view.Gravity
import android.view.View
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
import app.jietuqi.cn.widget.dialog.customdialog.EnsureDialog
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
            "视频和语音聊天" ->{
                LaunchUtil.startWechatCreateVideoActivity(this, mOtherSideEntity, null, 0)
            }
            "转发" ->{
                LaunchUtil.startWechatCreateShareActivity(this, mOtherSideEntity, null, 0)
            }
            "个人名片" ->{
                LaunchUtil.startWechatCreateCardActivity(this, mOtherSideEntity, null, 0)
            }
            "加群" ->{
                LaunchUtil.startWechatCreateInviteJoinGroupActivity(this, mOtherSideEntity, null, 0)
            }
            "文件" ->{
                LaunchUtil.startWechatCreateFileActivity(this, mOtherSideEntity, null, 0)
            }
        }
    }

    private lateinit var mHelper: WechatScreenShotHelper
    private lateinit var mRoleHelper: RoleLibraryHelper
    private var mAdapter: WechatScreenShotAdapter? = null
    private val mList: ArrayList<WechatScreenShotEntity> = arrayListOf()
    private lateinit var mEditEntity: WechatScreenShotEntity
    override fun setLayoutResourceId() = R.layout.activity_wechat_screenshot_create

    override fun needLoadingView() = false

    override fun initAllViews() {
        setBlackTitle("聊天对话", 2, "清空对话")
        mHelper = WechatScreenShotHelper(this)
        mRoleHelper = RoleLibraryHelper(this)
        mMySideEntity = getMySelf()
        mOtherSideEntity = UserOperateUtil.getOtherSide()
        GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mWechatScreenShotMySideAvatarIv)
        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mWechatScreenShotOtherSideAvatarIv)
        mHelper.queryAll()?.let { mList.addAll(it) }
        mAdapter = WechatScreenShotAdapter(mList, this)
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
                1, 14 ->{
                    LaunchUtil.startWechatCreatePictureAndVideoActivity(this@WechatScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                2 ->{
                    LaunchUtil.startWechatCreateTimeActivity(this, mOtherSideEntity, mEditEntity, 1)
                }
                3, 4 ->{
                    LaunchUtil.startWechatCreateRedPacketActivity(this@WechatScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                5, 6, 15 ->{
                    LaunchUtil.startWechatCreateTransferActivity(this@WechatScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                7 ->{
                    LaunchUtil.startWechatCreateVoiceActivity(this@WechatScreenShotActivity, mOtherSideEntity, mEditEntity, 1)
                }
                8 ->{
                    LaunchUtil.startWechatCreateSystemMessageActivity(this, mOtherSideEntity, mEditEntity, 1, "微信")
                }
                9, 10 ->{
                    LaunchUtil.startWechatCreateVideoActivity(this, mOtherSideEntity, mEditEntity, 1)
                }
                11 ->{
                    LaunchUtil.startWechatCreateShareActivity(this, mOtherSideEntity, mEditEntity, 1)
                }
                12 ->{
                    LaunchUtil.startWechatCreateCardActivity(this, mOtherSideEntity, mEditEntity, 1)
                }
                13 ->{
                    LaunchUtil.startWechatCreateInviteJoinGroupActivity(this, mOtherSideEntity, mEditEntity, 1)
                }
//                14 ->{
//                    LaunchUtil.startWechatCreateEmojiActivity(this, mOtherSideEntity, mEditEntity, 1)
//                }
                16 ->{
                    LaunchUtil.startWechatCreateFileActivity(this, mOtherSideEntity, mEditEntity, 1)
                }
            }
        }

        mWechatScreenShotCreateMenuRecyclerView.setOnItemMoveListener(object : OnItemMoveListener {
            override fun onItemMove(srcHolder: RecyclerView.ViewHolder, targetHolder: RecyclerView.ViewHolder): Boolean {
                if (srcHolder.itemViewType != targetHolder.itemViewType) return false
                val fromPosition = srcHolder.adapterPosition - mWechatScreenShotCreateMenuRecyclerView.headerItemCount
                val toPosition = targetHolder.adapterPosition - mWechatScreenShotCreateMenuRecyclerView.headerItemCount
                Collections.swap(mList, fromPosition, toPosition)
                mAdapter?.notifyItemMoved(fromPosition, toPosition)

                return true
            }

            override fun onItemDismiss(srcHolder: RecyclerView.ViewHolder) {

            }
        })
        mWechatScreenShotCreateMenuRecyclerView.setOnItemStateChangedListener { _, actionState ->
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
                EnsureDialog(this@WechatScreenShotActivity).builder()
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
                            wechatEntity.avatarInt = mMySideEntity.avatarInt
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
                            wechatEntity.avatarInt = mOtherSideEntity.avatarInt
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNeedChangeOrAddData(entity: WechatScreenShotEntity) {
        if ("收钱" == entity.tagStr){
            return
        }
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
}
