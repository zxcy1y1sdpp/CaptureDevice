package app.jietuqi.cn.wechat.preview

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.ResourceHelper
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
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
    private val blurRadius = 100
    private lateinit var mBitmap: Bitmap
    override fun setLayoutResourceId() = R.layout.activity_wechat_voice_and_video_preview

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setStatusBarColor(ColorFinal.BLACK)
    }

    override fun initViewsListener() {
        mBackIv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val entity: WechatVoiceAndVideoEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatVoiceAndVideoEntity
        mChatTimeTv.text = entity.time
        GlideUtil.displayHead(this, entity.getAvatarFile(), mPreviewHeadIv)
        GlideUtil.displayHead(this, entity.getAvatarFile(), mPreviewHeadIv1)
        mPreviewNickNameTv.text = entity.wechatUserNickName
        mPreviewNickNameTv1.text = entity.wechatUserNickName
        mConvertLayout.visibility = View.GONE
        mVideoLayout.visibility = View.GONE
        if (entity.chatType == 0){//语音通话
            mBitmap = if (null == entity.avatarFile){
                BitmapFactory.decodeResource(resources, ResourceHelper.getAppIconId(entity.resourceName))
            }else{
                var uri: Uri = Uri.fromFile(entity.avatarFile)
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            }
            val scaledBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.width / scaleRatio, mBitmap.height / scaleRatio, false)
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
            mBitmap.recycle()	// 回收bitmap的内存
        }else{//视频通话
            if (entity.type == 0){//待接听
                mBlurWechatPreviewBg.alpha = 0.6f
                GlideUtil.display(this, entity.wechatBg, mBlurWechatPreviewBg)
                mPreviewHeadIv.visibility = View.GONE
                mPreviewNickNameTv.visibility = View.GONE
                mInviteYouVoiceTv.visibility = View.GONE

                mConvertLayout.visibility = View.VISIBLE
                mVideoLayout.visibility = View.VISIBLE
                mLeftIv.setImageResource(R.drawable.wechat_voice_video_up)
                mLeftTv.text = "挂断"
                mWechatVoiceAndVideoCenterLayout.visibility = View.GONE
                mRightIv.setImageResource(R.drawable.wechat_video_answer)
                mRightTv.text = "接听"
            }else{//通话中
                mVideoLayout.visibility = View.GONE
                mCoverView.visibility = View.GONE
                GlideUtil.displayFile(this, entity.otherFileBg, mBlurWechatPreviewBg)
                mOtherHeaderIv.visibility = View.VISIBLE
                GlideUtil.displayFile(this, entity.myFileBg, mOtherHeaderIv)
                mOtherHeaderIv.setImageResource(R.mipmap.icon2)
                mPreviewHeadIv.visibility = View.GONE
                mPreviewNickNameTv.visibility = View.GONE
                mInviteYouVoiceTv.visibility = View.GONE
                mLeftIv.setImageResource(R.drawable.wechat_voice_video_convert)
                mLeftTv.text = "切到语音通话"
                mWechatVoiceAndVideoCenterLayout.visibility = View.VISIBLE
                mCenterIv.setImageResource(R.drawable.wechat_voice_video_up)
                mCenterTv.text = "挂断"
                mRightIv.setImageResource(R.drawable.wechat_voice_video_camera)
                mRightTv.text = "转换摄像头"
            }
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mBackIv ->{
                finish()
            }
        }
    }

}
