package app.jietuqi.cn.wechat.simulator.ui.activity.create

import android.content.Intent
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.entity.eventbusentity.EventBusTimeEntity
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.WechatTimeUtil
import app.jietuqi.cn.wechat.entity.WechatMsgEditEntity
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorHelper
import kotlinx.android.synthetic.main.activity_wechat_create_time.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2019/1/28 11:05.
 * 时间： 2019/1/28 11:05
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatSimulatorCreateTimeActivity : BaseCreateActivity() {
    /**
     * 0 -- 12小时制
     * 1 -- 24小时制
     */
    private var mTimeType = 0
    private lateinit var mHelper: WechatSimulatorHelper
    private var mMsgEntity = WechatScreenShotEntity()
    /**
     * 0 -- 发布新的文本
     * 1 -- 编辑修改文本
     */
    private var mType = 0
    override fun setLayoutResourceId() = R.layout.activity_wechat_create_time

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        registerEventBus()
        setBlackTitle("时间", 1)
        mMsgEntity.msgType = 2
    }

    override fun initViewsListener() {
        m12TypeBtn.setOnClickListener(this)
        m24TypeBtn.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity
        mHelper = WechatSimulatorHelper(this, mOtherSideEntity)
        if (mType == 1){
            mMsgEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatScreenShotEntity
            if ("12" == mMsgEntity.timeType){
                mTimeTv.text = WechatTimeUtil.getNewChat12Time(mMsgEntity.time)
            }else{
                mTimeTv.text = WechatTimeUtil.getNewChat24Time(mMsgEntity.time)
            }
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.m12TypeBtn ->{
                mTimeType = 0
                initTimePickerView(tag = "创建")
            }
            R.id.m24TypeBtn ->{
                mTimeType = 1
                initTimePickerView(tag = "创建")
            }
            R.id.overallAllRightWithBgTv ->{
                if(TextUtils.isEmpty(OtherUtil.getContent(mTimeTv))){
                    showToast("请正确选择时间")
                    return
                }
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
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelectTimeEvent(timeEntity: EventBusTimeEntity) {
        mMsgEntity.time = timeEntity.timeLong
        if (mTimeType == 0){//12小时制
            mMsgEntity.timeType = "12"
            mTimeTv.text = WechatTimeUtil.getNewChat12Time(mMsgEntity.time)
        }else{//24小时制
            mMsgEntity.timeType = "24"
            mTimeTv.text = WechatTimeUtil.getNewChat24Time(mMsgEntity.time)
        }
    }
}
