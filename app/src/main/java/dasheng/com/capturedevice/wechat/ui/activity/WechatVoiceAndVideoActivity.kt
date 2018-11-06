package dasheng.com.capturedevice.wechat.ui.activity

import android.content.Intent
import android.text.TextUtils
import android.view.View
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseWechatActivity
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.constant.RequestCode
import dasheng.com.capturedevice.database.table.WechatUserTable
import dasheng.com.capturedevice.entity.WechatVoiceAndVideoEntity
import dasheng.com.capturedevice.entity.eventbusentity.EventBusTimeEntity
import dasheng.com.capturedevice.util.*
import kotlinx.android.synthetic.main.activity_wechat_voice_and_video.*
import kotlinx.android.synthetic.main.include_wechat_preview_btn.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/10/20 12:09.
 * 时间： 2018/10/20 12:09
 * 邮箱： 972383753@qq.com
 * 用途： 生成未了语音或视频聊天的页面
 */

class WechatVoiceAndVideoActivity : BaseWechatActivity() {
    private var mEntity: WechatVoiceAndVideoEntity = WechatVoiceAndVideoEntity()
    /**
     * 语音还是视频聊天
     * 0 -- 语音聊天
     * 1 -- 视频聊天
     */
    var mType = 0
    /**
     * 0 -- 语音聊天背景
     * 1 -- 语音聊天对象
     * 2 -- 视频聊天对方背景
     * 3 -- 视频聊天我的背景
     */
    private var mChatBgType = 0
    override fun setLayoutResourceId() = R.layout.activity_wechat_voice_and_video

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        registerEventBus()
    }

    override fun initViewsListener() {
        mWaitingForAnswerTv.setOnClickListener(this)
        mBusyNowTv.setOnClickListener(this)
        mSelectOthersLayout.setOnClickListener(this)
        mTimeLayout.setOnClickListener(this)
        mChatBgLayout.setOnClickListener(this)
        mOtherBgLayout.setOnClickListener(this)
        mMyBgLayout.setOnClickListener(this)
//        mSelectVideoBgLayout.setOnClickListener(this)
        previewBtn.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        if(mType == 0){//语音聊天
            setWechatViewTitle("微信语音聊天", 0)
            mEntity.chatType = 0
        }else{//视频聊天
            setWechatViewTitle("微信视频聊天", 0)
            mChatBgLayout.visibility = View.VISIBLE
            mEntity.chatType = 1
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWaitingForAnswerTv ->{
                mEntity = WechatVoiceAndVideoEntity()
                mEntity.chatType = mType
                OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, false)
                mEntity.type = 0
                OtherUtil.changeWechatTwoBtnBg(this, mWaitingForAnswerTv, mBusyNowTv)
                mTimeLayout.visibility = View.GONE
                if (mType == 0){//语音聊天
                    mChatBgLayout.visibility = View.GONE
                    mOtherBgLayout.visibility = View.GONE
                    mMyBgLayout.visibility = View.GONE
                }else{
                    mChatBgLayout.visibility = View.VISIBLE
                    mOtherBgLayout.visibility = View.GONE
                    mMyBgLayout.visibility = View.GONE
                    mSelectOthersLayout.visibility = View.VISIBLE
                }
            }
            R.id.mBusyNowTv ->{
                mEntity = WechatVoiceAndVideoEntity()
                mEntity.chatType = mType
                OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, false)
                mEntity.type = 1
                OtherUtil.changeWechatTwoBtnBg(this, mBusyNowTv, mWaitingForAnswerTv)
                mTimeLayout.visibility = View.VISIBLE
                if (mType == 0){//语音聊天
                    mChatBgLayout.visibility = View.GONE
                    mOtherBgLayout.visibility = View.GONE
                    mMyBgLayout.visibility = View.GONE
                }else{
                    mChatBgLayout.visibility = View.GONE
                    mOtherBgLayout.visibility = View.VISIBLE
                    mMyBgLayout.visibility = View.VISIBLE
                    mSelectOthersLayout.visibility = View.GONE
                }
            }
            R.id.mSelectOthersLayout ->{
                mChatBgType = 1
                LaunchUtil.launch(this, WechatRoleActivity::class.java, RequestCode.CHANGE_ROLE)
            }
            R.id.mTimeLayout ->{
                initTimePickerView("", 2)
            }
            R.id.mChatBgLayout ->{
                mChatBgType = 0
                callAlbum(1, true)
            }
            R.id.mOtherBgLayout ->{
                mChatBgType = 2
                callAlbum(1, true)
            }
            R.id.mMyBgLayout ->{
                mChatBgType = 3
                callAlbum(1, true)
            }
            R.id.previewBtn ->{
                mEntity.time = mTimeTv.text.toString()
                LaunchUtil.startWechatVoiceAndVideoPreviewActivity(this, mEntity)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.CHANGE_ROLE ->{
                    val entity = data?.getSerializableExtra(IntentKey.ENTITY) as WechatUserTable
                    mEntity.wechatUserNickName = entity.wechatUserNickName
                    mEntity.avatar = entity.avatar
                    mNickNameTv.text = mEntity.wechatUserNickName
                    GlideUtil.display(this, mEntity.avatar, mAvatarIv)

                    if (mType == 1){//视频聊天
                        if (mEntity.type == 0){//待接听
                            if (null != mEntity.wechatBg && !TextUtils.isEmpty(mEntity.wechatUserNickName) && null != mEntity.avatar){
                                OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, true)
                            }else{
                                OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, false)
                            }
                        }
                    }else{
                        if (!TextUtils.isEmpty(mEntity.wechatUserNickName) && null != mEntity.avatar){
                            OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, true)
                        }else{
                            OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, false)
                        }
                    }
                }
                RequestCode.CROP_IMAGE ->{
                    if (mChatBgType == 0){
                        mEntity.wechatBg = mFinalCropFile
                        GlideUtil.display(this, mFinalCropFile, mChatBgIv)
                    }else if (mChatBgType == 2){
                        mEntity.otherFileBg = mFinalCropFile
                        GlideUtil.display(this, mFinalCropFile, mOtherBgIv)
                    }else if (mChatBgType == 3){
                        mEntity.myFileBg = mFinalCropFile
                        GlideUtil.display(this, mFinalCropFile, mMyBgIv)
                    }
                    if (mType == 1){//视频聊天
                        if (mEntity.type == 0){//待接听
                            if (null != mEntity.wechatBg && !TextUtils.isEmpty(mEntity.wechatUserNickName) && null != mEntity.avatar){
                                OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, true)
                            }else{
                                OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, false)
                            }
                        }else{//通话中
                            if (null != mEntity.otherFileBg && mEntity.myFileBg != null){
                                OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, true)
                            }else{
                                OtherUtil.changeWechatPreviewBtnBg(this, previewBtn, false)
                            }
                        }

                    }
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSelecTimeEvent(timeEntity: EventBusTimeEntity) {
        mTimeTv.text = timeEntity.timeOnlyMS
    }

    override fun onDestroy() {
        EventBusUtil.unRegister(this)
        super.onDestroy()
    }
}
