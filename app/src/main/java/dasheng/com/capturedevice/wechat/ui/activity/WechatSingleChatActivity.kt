package dasheng.com.capturedevice.wechat.ui.activity

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.Toast
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseWechatActivity
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.database.DatabaseUtils
import dasheng.com.capturedevice.database.MyOpenHelper
import dasheng.com.capturedevice.database.table.WechatSingleTalkEntity
import dasheng.com.capturedevice.database.table.WechatUserTable
import dasheng.com.capturedevice.entity.UserEntity
import dasheng.com.capturedevice.wechat.ui.adapter.WechatSingleTalkAdapter
import kotlinx.android.synthetic.main.activity_wechat_singlechat.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.SimpleDateFormat
import java.util.*
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.builder.TimePickerBuilder
import dasheng.com.capturedevice.entity.eventbusentity.EventBusTimeEntity
import dasheng.com.capturedevice.util.*


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
    lateinit var mHelper: MyOpenHelper
    private var mList: MutableList<WechatSingleTalkEntity> = mutableListOf()
    private lateinit var mAdapter: WechatSingleTalkAdapter
    private var imgList: ArrayList<Int> = arrayListOf()
    private lateinit var mEntity: WechatUserTable
    override fun setLayoutResourceId() = R.layout.activity_wechat_singlechat

    override fun needLoadingView() = false

    override fun initAllViews() {
        EventBusUtil.register(this)
        mHelper = DatabaseUtils.getHelper()
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
        mRecyclerView.scrollToPosition(mAdapter.itemCount - 1)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        if (v.id == R.id.wechatTitleTv){
            if (mComMsg){
                mAdapter.switchRoles(true)
                Toast.makeText(this, "切换为自己说话", Toast.LENGTH_SHORT).show()
            }else{
                mAdapter.switchRoles(false)
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
                    mAdapter.notifyItemInserted(mAdapter.itemCount - 1)
                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount - 1)

                    mEntity.msgType = "0"
                    mEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
                    mEntity.msg = mContentEt.text.toString()
                    mHelper.updateWechatUser(mEntity)

                }
                R.id.mSendImgIv ->{
                    var entity = WechatSingleTalkEntity(mEntity.wechatUserId, 1, mComMsg, R.mipmap.timg2, 0)
                    id = mHelper.saveWechatSingleMsg(entity)
                    entity.id = id
                    mList.add(entity)
                    mAdapter.notifyItemInserted(mAdapter.itemCount - 1)
                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount - 1)

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
                    mAdapter.notifyItemInserted(mAdapter.itemCount - 1)
                    mRecyclerView.smoothScrollToPosition(mAdapter.itemCount - 1)

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
        mAdapter.notifyItemChanged(entity.position)
        mRecyclerView.scrollToPosition(mAdapter.itemCount - 1)

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
        mAdapter.notifyItemInserted(mAdapter.itemCount - 1)
        mRecyclerView.smoothScrollToPosition(mAdapter.itemCount - 1)
    }
    override fun onDestroy() {
        EventBusUtil.unRegister(this)
        super.onDestroy()
    }
}
