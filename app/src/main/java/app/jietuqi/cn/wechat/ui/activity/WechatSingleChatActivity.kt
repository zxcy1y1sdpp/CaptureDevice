package app.jietuqi.cn.wechat.ui.activity

import android.content.Intent
import android.view.View
import android.widget.Toast
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.database.DatabaseUtils
import app.jietuqi.cn.database.MyOpenHelper
import app.jietuqi.cn.database.table.WechatSingleTalkEntity
import app.jietuqi.cn.database.table.WechatUserTable
import app.jietuqi.cn.entity.UserEntity
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.PreferencesUtils
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.TimeUtil
import app.jietuqi.cn.wechat.ui.adapter.WechatSingleTalkAdapter
import kotlinx.android.synthetic.main.activity_wechat_singlechat.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*


/**
 * 作者： liuyuanbo on 2018/10/10 17:40.
 * 时间： 2018/10/10 17:40
 * 邮箱： 972383753@qq.com
 * 用途： 微信单聊页面
 */

class WechatSingleChatActivity : BaseWechatActivity() {
    /**
     * true -- 对方说话
     * false -- 自己说话
     */
    private var mComMsg = false
    private var mHelper: MyOpenHelper = DatabaseUtils.getHelper()
    private var mList: MutableList<WechatSingleTalkEntity> = mutableListOf()
    private var mAdapter: WechatSingleTalkAdapter? = null
    private var imgList: ArrayList<Int> = arrayListOf()
    private var mEntity: WechatUserTable = WechatUserTable()
    override fun setLayoutResourceId() = R.layout.activity_wechat_singlechat
    var bol = false

    override fun needLoadingView() = false

    override fun initAllViews() {
        EventBusUtil.register(this)
        imgList.add(R.mipmap.role_019)
        imgList.add(R.mipmap.role_020)
        imgList.add(R.mipmap.timg)
        imgList.add(R.mipmap.timg1)
        imgList.add(R.mipmap.timg2)
        imgList.add(R.mipmap.role_002)
    }

    override fun initViewsListener() {
        mSendMsgBtn.setOnClickListener(this)
        mSendImgIv.setOnClickListener(this)
        mSendTimeBtn.setOnClickListener(this)
        mSendRedPackageBtn.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatUserTable
        var myInfo: UserEntity = PreferencesUtils.getMyInfo(this)
        setWechatViewTitle(mEntity.wechatUserNickName, 2)
        val list = mHelper.queryWechtSingleMsgList(mEntity.wechatUserId)
        if (null != list){
            mList.addAll(list)
        }
        mAdapter = WechatSingleTalkAdapter(mList, mEntity, myInfo)
        mRecyclerView.adapter = mAdapter
        mAdapter?.itemCount?.minus(1)?.let { mRecyclerView.scrollToPosition(it) }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (v.id == R.id.mBaseCreateTitleTv){
            if (mComMsg){
                mAdapter?.switchRoles(true)
                Toast.makeText(this, "切换为自己说话", Toast.LENGTH_SHORT).show()
            }else{
                mAdapter?.switchRoles(false)
                Toast.makeText(this, "切换为对方说话", Toast.LENGTH_SHORT).show()
            }
            mComMsg = !mComMsg
        }else{
//            var entity: WechatSingleTalkEntity = WechatSingleTalkEntity()
            var id: Int
            when(v.id){
                R.id.mSendMsgBtn ->{
                    var entity  = WechatSingleTalkEntity(mEntity.wechatUserId, 0, mComMsg, mContentEt.text.toString(), 0)
                    id = mHelper.saveWechatSingleMsg(entity)
                    entity.id = id
                    mList.add(entity)
                    mAdapter?.itemCount?.minus(1)?.let { mAdapter?.notifyItemInserted(it) }
                    mAdapter?.itemCount?.minus(1)?.let { mRecyclerView.smoothScrollToPosition(it) }

                    mEntity.msgType = "0"
                    mEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
                    mEntity.msg = mContentEt.text.toString()
                    mHelper.updateWechatUser(mEntity)

                }
                R.id.mSendImgIv ->{
                    var entity = if (bol){
                        WechatSingleTalkEntity(mEntity.wechatUserId, 1, mComMsg, R.mipmap.timg, 0)
                    }else{
                        WechatSingleTalkEntity(mEntity.wechatUserId, 1, mComMsg, R.mipmap.timg1, 0)
                    }
                    bol = !bol
                    id = mHelper.saveWechatSingleMsg(entity)
                    entity.id = id
                    mList.add(entity)
                    mAdapter?.itemCount?.minus(1)?.let { mAdapter?.notifyItemInserted(it) }
                    mAdapter?.itemCount?.minus(1)?.let { mRecyclerView.smoothScrollToPosition(it) }

                    mEntity.msgType = "1"
                    mEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
                    mEntity.msg = "[图片]"
                    mHelper.updateWechatUser(mEntity)
                }
                R.id.mSendTimeBtn ->{
                    //时间选择器
                    initTimePickerView()
                }
                R.id.mSendRedPackageBtn ->{
                    var entity = WechatSingleTalkEntity(mEntity.wechatUserId, 3, mComMsg, false, 100, mContentEt.text.toString(), 0)
                    id = mHelper.saveWechatSingleMsg(entity)
                    entity.id = id
                    mList.add(entity)
                    mAdapter?.itemCount?.minus(1)?.let { mAdapter?.notifyItemInserted(it) }
                    mAdapter?.itemCount?.minus(1)?.let { mRecyclerView.smoothScrollToPosition(it) }

                    mEntity.msgType = "3"
                    mEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
                    mEntity.msg = StringUtils.insertFront(entity.msg, "[微信红包]")
                    mHelper.updateWechatUser(mEntity)
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(entity: WechatSingleTalkEntity) {
        mHelper.updateWechatSingleMsg(entity)
        mAdapter?.notifyItemChanged(entity.position)
        mAdapter?.itemCount?.minus(1)?.let { mRecyclerView.scrollToPosition(it) }

        mEntity.msgType = "3"
        mEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
        if (entity.receive){//如果红包被领取了
            if (mComMsg){//如果是我领取了对方的红包
                entity.msg = StringUtils.insertFrontAndBack(mEntity.wechatUserNickName, "你领取了", "的红包")//我领取对方的红包
            }else{//对方领取了我的红包
                mEntity.msg = StringUtils.insertBack(entity.msg, "领取了你的红包")//对方领取我的红包
            }
        }
        mHelper.updateWechatUser(mEntity)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelecTimeEvent(timeEntity: EventBusTimeEntity) {
        var entity = WechatSingleTalkEntity(mEntity.wechatUserId, 2, mComMsg, timeEntity.timeLong, timeEntity.timeLong)
        var id = mHelper.saveWechatSingleMsg(entity)
        entity.id = id
        mList.add(entity)
        mAdapter?.itemCount?.minus(1)?.let { mAdapter?.notifyItemInserted(it) }
        mAdapter?.itemCount?.minus(1)?.let { mRecyclerView.smoothScrollToPosition(it) }
    }
    override fun onDestroy() {
        EventBusUtil.unRegister(this)
        super.onDestroy()
    }
}
