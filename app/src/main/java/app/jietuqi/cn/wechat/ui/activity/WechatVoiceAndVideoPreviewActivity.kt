package app.jietuqi.cn.wechat.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.entity.WechatVoiceAndVideoEntity
import app.jietuqi.cn.util.FastBlur
import app.jietuqi.cn.util.GlideUtil
import kotlinx.android.synthetic.main.activity_wechat_voice_and_video_preview.*


/**
 * 作者： liuyuanbo on 2018/10/20 12:09.
 * 时间： 2018/10/20 12:09
 * 邮箱： 972383753@qq.com
 * 用途： 生成微信语音或视频聊天的页面
 */

class WechatVoiceAndVideoPreviewActivity : BaseWechatActivity() {
    private val scaleRatio = 5
    private val blurRadius = 10
    override fun setLayoutResourceId() = R.layout.activity_wechat_voice_and_video_preview

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
    }

    override fun initViewsListener() {
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val entity: WechatVoiceAndVideoEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatVoiceAndVideoEntity
        mChatTimeTv.text = entity.time
        GlideUtil.display(this, entity.avatar, mPreviewHeadIv)
        mPreviewNickNameTv.text = entity.wechatUserNickName
        if (entity.chatType == 0){//语音通话
            var uri: Uri = Uri.fromFile(entity.avatar)
            var bitmap: Bitmap  = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / scaleRatio, bitmap.height / scaleRatio, false)
            val blurBitmap = FastBlur.doBlur(scaledBitmap, blurRadius, true)
            mBlurWechatPreviewBg.setImageBitmap(blurBitmap)
            if (entity.type == 0){//待接听
                mInviteYouVoiceTv.visibility = View.VISIBLE
                mChatTimeTv.visibility = View.GONE
                mLeftIv.setImageResource(R.drawable.wechat_voice_video_up)
                mLeftTv.text = "挂断"
                mWechatVoiceAndVideoCenterLayout.visibility = View.GONE
                mRightIv.setImageResource(R.drawable.wechat_voice_video_answer)
                mRightTv.text = "接听"
            }else{//通话中
                mInviteYouVoiceTv.visibility = View.GONE
                mLeftIv.setImageResource(R.drawable.wechat_voice_video_on)
                mLeftTv.text = "静音"
                mWechatVoiceAndVideoCenterLayout.visibility = View.VISIBLE
                mCenterIv.setImageResource(R.drawable.wechat_voice_video_up)
                mCenterTv.text = "挂断"
                mRightIv.setImageResource(R.drawable.wechat_voice_video_speaker_on)
                mRightTv.text = "免提"
            }
        }else{//视频通话
            if (entity.type == 0){//待接听
                /*mInviteYouVoiceTv.visibility = View.VISIBLE
                mLeftIv.setImageResource(R.drawable.wechat_voice_video_up)
                mLeftTv.text = "挂断"
                mWechatVoiceAndVideoCenterLayout.visibility = View.GONE
                mRightIv.setImageResource(R.drawable.wechat_voice_video_up)
                mRightTv.text = "接听"*/
            }else{//通话中
                /*var uri: Uri = Uri.fromFile(entity.wechatBg)
                var bitmap: Bitmap  = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                val scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.width / scaleRatio, bitmap.height / scaleRatio, false)
                val blurBitmap = FastBlur.doBlur(scaledBitmap, blurRadius, true)
                mBlurWechatPreviewBg.setImageBitmap(blurBitmap)*/
                mCoverView.visibility = View.GONE
                GlideUtil.displayFile(this, entity.otherFileBg, mBlurWechatPreviewBg)
                mOtherHeaderIv.visibility = View.VISIBLE
                GlideUtil.displayFile(this, entity.myFileBg, mOtherHeaderIv)
                mOtherHeaderIv.setImageResource(R.mipmap.icon2)
                mPreviewHeadIv.visibility = View.GONE
                mPreviewNickNameTv.visibility = View.GONE
                mInviteYouVoiceTv.visibility = View.GONE
                mLeftIv.setImageResource(R.drawable.wechat_voice_video_convert)
                mLeftTv.text = "切换到语音通话"
                mWechatVoiceAndVideoCenterLayout.visibility = View.VISIBLE
                mCenterIv.setImageResource(R.drawable.wechat_voice_video_up)
                mCenterTv.text = "挂断"
                mRightIv.setImageResource(R.drawable.wechat_voice_video_camera)
                mRightTv.text = "转换摄像头"
            }
        }
    }
}
