package app.jietuqi.cn.wechat.simulator.ui.activity.create

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.entity.WechatMsgEditEntity
import app.jietuqi.cn.wechat.simulator.adapter.WechatGroupRolesAdapter
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorHelper
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.activity_simulator_wechat_create_group_system_message.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 作者： liuyuanbo on 2018/12/22 16:24.
 * 时间： 2018/12/22 16:24
 * 邮箱： 972383753@qq.com
 * 用途： 系统提示
 */
class WechatSimulatorCreateGroupSystemMessageActivity : BaseCreateActivity() {
    private lateinit var mHelper: WechatSimulatorHelper
    private lateinit var mAdapter: WechatGroupRolesAdapter
    private var mMsgEntity: WechatScreenShotEntity = WechatScreenShotEntity()
    private lateinit var mOperateUserEntity: WechatUserEntity
    private var mJoinUserNickNames = ""
    private lateinit var mRoleEntity: WechatUserEntity
    /**
     * 是不是“我”在操作
     */
    private var mOpreateIsMySelf = true
    /**
     * 0 -- 创建
     * 1 -- 编辑
     */
    private var mType = 0
    override fun setLayoutResourceId() = R.layout.activity_simulator_wechat_create_group_system_message

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {}

    override fun initViewsListener() {
        mSystemMsg0Tv.setOnClickListener(this)
        mSystemMsg1Tv.setOnClickListener(this)
        mSystemMsg2Tv.setOnClickListener(this)
        mSystemMsg3Tv.setOnClickListener(this)
        mSystemMsg4Tv.setOnClickListener(this)
        mSystemMsg5Tv.setOnClickListener(this)
        mSystemMsg6Tv.setOnClickListener(this)
        mSystemMsg7Tv.setOnClickListener(this)

        mSimulatorSystemMessageGroupRolesRv.addOnItemTouchListener(object : OnRecyclerItemClickListener(mSimulatorSystemMessageGroupRolesRv) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                mOperateUserEntity = mRoleEntity.groupRoles[vh.adapterPosition]
                changeMsg()
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        mOperateUserEntity = UserOperateUtil.getWechatSimulatorMySelf()
        mRoleEntity = intent.getSerializableExtra(IntentKey.ROLE_ENTITY) as WechatUserEntity
        mOpreateIsMySelf = false
        mMsgEntity.msgType = 8
        if (mType == 0){
            setBlackTitle("创建系统提示", 1)
        }else{
            setBlackTitle("编辑系统提示", 1)
            mMsgEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatScreenShotEntity
            mSimulatorSystemMessageEt.setText(mMsgEntity.msg)
            mHelper = WechatSimulatorHelper(this, mRoleEntity.groupTableName)
        }

        mRoleEntity.groupRoles.add(0, mOperateUserEntity)
        mAdapter = WechatGroupRolesAdapter(mRoleEntity.groupRoles)
        mSimulatorSystemMessageGroupRolesRv.adapter = mAdapter
        changeMsg()

    }
    override fun onClick(v: View) {

        when(v.id){
            R.id.mSystemMsg0Tv ->{
                mSimulatorSystemMessageEt.setText(StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "你邀请“", "”加入了群聊 撤销"))
            }
            R.id.mSystemMsg1Tv ->{
                mSimulatorSystemMessageEt.setText(StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "“", "”通过扫描你分享的二维码加入群聊 撤销"))
            }
            R.id.mSystemMsg2Tv ->{
                mSimulatorSystemMessageEt.setText(StringUtils.insertBack(mJoinUserNickNames, "你通过扫描二维码加入群聊，群聊参与人还有："))
            }
            R.id.mSystemMsg3Tv ->{
                mSimulatorSystemMessageEt.setText(StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "你将“", "”移出了群聊"))
            }
            R.id.mSystemMsg4Tv ->{
                mSimulatorSystemMessageEt.setText(StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "你被“", "”移出群聊"))
            }
            R.id.mSystemMsg5Tv ->{
                if (TextUtils.isEmpty(mRoleEntity.groupName)){
                    mSimulatorSystemMessageEt.setText("你修改群名为“群聊”")
                }else{
                    mSimulatorSystemMessageEt.setText(StringUtils.insertFrontAndBack(mOperateUserEntity.groupName, "你修改群名为“", "”"))
                }
            }
            R.id.mSystemMsg6Tv ->{
                if (TextUtils.isEmpty(mOperateUserEntity.groupName)){
                    mSimulatorSystemMessageEt.setText(StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "“", "”修改群名为“群聊”"))
                }else{
                    mSimulatorSystemMessageEt.setText(StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "“", "”修改群名为“群聊”"))
                }
            }
            R.id.mSystemMsg7Tv ->{
                mSimulatorSystemMessageEt.setText(StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "“", "”与群里其他人都不是微信朋友关系，请注意隐私安全"))
            }

            R.id.overallAllRightWithBgTv ->{
                val msg = mSimulatorSystemMessageEt.text.toString().trim()
                if(TextUtils.isEmpty(msg)){
                    showToast("请输入内容")
                    return
                }
                mMsgEntity.msg = msg
                if (mType == 0){
                    EventBusUtil.post(mMsgEntity)
                }else{
                    val entity = WechatMsgEditEntity()
                    entity.editEntity  = mMsgEntity
                    EventBusUtil.post(entity)
                    mHelper.update(mMsgEntity, false)
                }
                finish()
            }
        }
        super.onClick(v)
    }

    private fun changeMsg(){
        mSystemMsg0Tv.text = StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "你邀请“", "”加入了群聊 撤销")
        mSystemMsg1Tv.text = StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "“", "”通过扫描你分享的二维码加入群聊 撤销")
        mSystemMsg3Tv.text = StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "你将“", "”移出了群聊")
        mSystemMsg4Tv.text = StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "你被“", "”移出群聊")
        if (TextUtils.isEmpty(mRoleEntity.groupName)){
            mSystemMsg5Tv.text = "你修改群名为“群聊”"
        }else{
            mSystemMsg5Tv.text = StringUtils.insertFrontAndBack(mOperateUserEntity.groupName, "你修改群名为“", "”")
        }
        if (TextUtils.isEmpty(mOperateUserEntity.groupName)){
            mSystemMsg6Tv.text = StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "“", "”修改群名为“群聊”")
        }else{
            mSystemMsg6Tv.text = StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "“", StringUtils.insertFrontAndBack(mOperateUserEntity.groupName, "“修改群名为“", "”"))
        }
        mSystemMsg7Tv.text = StringUtils.insertFrontAndBack(mOperateUserEntity.wechatUserNickName, "“", "”与群里其他人都不是微信朋友关系，请注意隐私安全")
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
            val groupName = StringBuilder()
            var entity: WechatUserEntity
            for (i in mRoleEntity.groupRoles.indices){
                entity = mRoleEntity.groupRoles[i]
                groupName.append(entity.wechatUserNickName).append("、")
            }
            groupName.deleteCharAt(groupName.length - 1)
            mJoinUserNickNames = groupName.toString()
            runOnUiThread {
                mSystemMsg2Tv.text = StringUtils.insertFront(mJoinUserNickNames, "你通过扫描二维码加入群聊，群聊参与人还有：")
            }
        }
    }
}
