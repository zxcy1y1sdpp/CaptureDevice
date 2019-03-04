package app.jietuqi.cn.wechat.simulator.ui.activity.create

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.wechat.simulator.adapter.WechatSimulatorEditGroupInfoAdapter
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.widget.dialog.EditDialog
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.activity_wechat_simulator_edit_group_info.*
import kotlinx.android.synthetic.main.include_base_overall_top.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.*
import java.io.File

/**
 * 作者： liuyuanbo on 2019/2/18 14:15.
 * 时间： 2019/2/18 14:15
 * 邮箱： 972383753@qq.com
 * 用途： 群信息编辑
 */
@RuntimePermissions
class WechatSimulatorEditGroupInfoActivity : BaseOverallActivity(), EditDialogChoiceListener, WechatSimulatorEditGroupInfoAdapter.OperateListener {
    override fun reduceRoles() {
        LaunchUtil.startWechatSimulatorEditGroupRolesActivity(this, 1, mEntity)
    }
    override fun addRoles() {
        LaunchUtil.startWechatSimulatorEditGroupRolesActivity(this, 0, mEntity)
    }

    private var mAdapter: WechatSimulatorEditGroupInfoAdapter?= null
    private var mList = arrayListOf<WechatUserEntity>()
    private lateinit var mEntity: WechatUserEntity
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_edit_group_info

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        registerEventBus()
        setTopTitle("群信息编辑", rightTitle = "完成")
        mAdapter = WechatSimulatorEditGroupInfoAdapter(mList, this)
        mEditGroupRolesRv.adapter = mAdapter
    }

    override fun initViewsListener() {
        overAllRightTitleTv.setOnClickListener(this)
        mEditGroupInfoCleanBtn.setOnClickListener(this)
        mEditGroupNameLayout.setOnClickListener(this)
        mEditShowNickNameLayout.setOnClickListener(this)
        mEditGroupCountLayout.setOnClickListener(this)
        mBgLayout.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity
        mEntity.groupRoles?.let { mList.addAll(it) }
        mAdapter?.notifyDataSetChanged()
        OtherUtil.onOrOff(mEntity.groupShowNickName, mSimulatorWechatCreateGroupRedPacketJoinReceiveIv)
        mSimulatorWechatCreateGroupRedPacketJoinReceiveIv.tag = mEntity.groupShowNickName

        if (TextUtils.isEmpty(mEntity.groupName)){
            val groupName = StringBuilder()
            var entity2: WechatUserEntity
            for (i in mEntity.groupRoles.indices){
                entity2 = mEntity.groupRoles[i]
                groupName.append(entity2.wechatUserNickName).append("、")
            }
            groupName.deleteCharAt(groupName.length - 1)
            mEditGroupNameTv.text = groupName.toString()
        }else{
            mEditGroupNameTv.text = mEntity.groupName
        }

        mEditGroupCountTv.text = mEntity.groupRoleCount.toString()
        if (!TextUtils.isEmpty(mEntity.chatBg)){
            GlideUtil.display(this, File(mEntity.chatBg), mBgIv)
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.overAllRightTitleTv ->{
                mEntity.groupShowNickName = mSimulatorWechatCreateGroupRedPacketJoinReceiveIv.tag.toString().toBoolean()
                mEntity.eventBusTag = "编辑"
                WechatSimulatorListHelper(this).update(mEntity)
                EventBusUtil.post(mEntity)
                finish()
            }
            R.id.mEditGroupInfoCleanBtn ->{
                EventBusUtil.post("清除")
                showToast("完成")
            }
            R.id.mEditGroupNameLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(0, "", "群聊名称"))
                dialog.show(supportFragmentManager, "showGroupName")
            }
            R.id.mEditShowNickNameLayout ->{
                mSimulatorWechatCreateGroupRedPacketJoinReceiveIv.tag = !mSimulatorWechatCreateGroupRedPacketJoinReceiveIv.tag.toString().toBoolean()
                OtherUtil.onOrOff(mSimulatorWechatCreateGroupRedPacketJoinReceiveIv.tag.toString().toBoolean(), mSimulatorWechatCreateGroupRedPacketJoinReceiveIv)
            }
            R.id.mEditGroupCountLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(1, "", "群人数", true))
                dialog.show(supportFragmentManager, "realCount")
            }
            R.id.mBgLayout ->{
                openAlbumWithPermissionCheck(false)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.IMAGE_SELECT ->{
                mEntity.chatBg = mFiles[0].absolutePath
                GlideUtil.displayAll(this, mFiles[0], mBgIv)
                var entity: WechatUserEntity
                for (i in mEntity.groupRoles.indices){
                    entity = mEntity.groupRoles[i]
                    entity.chatBg = mFiles[0].absolutePath
                }
            }
        }
    }
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun openAlbum(needCrop: Boolean = false) {
        callAlbum(needCrop = needCrop)
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
    override fun onChoice(entity: EditDialogEntity) {
        when(entity.position){
            0 ->{
                mEditGroupNameTv.text = entity.content
                mEntity.groupName = entity.content
            }
            1 ->{
                mEditGroupCountTv.text = entity.content
                mEntity.groupRoleCount = entity.content.toInt()
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChangeUserInfo(entity: WechatUserEntity) {
        mList.clear()
        mList.addAll(entity.groupRoles)
        mAdapter?.notifyDataSetChanged()
        mEntity.groupRoles.clear()
        mEntity.groupRoles.addAll(entity.groupRoles)
    }
}
