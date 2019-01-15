package app.jietuqi.cn.ui.qqscreenshot.ui.create

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.qqscreenshot.db.QQScreenShotHelper
import app.jietuqi.cn.ui.qqscreenshot.entity.QQScreenShotEntity
import app.jietuqi.cn.ui.qqscreenshot.widget.EmojiQQManager
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.UserOperateUtil
import kotlinx.android.synthetic.main.activity_qq_create_text.*
import kotlinx.android.synthetic.main.include_choice_role.*

/**
 * 作者： liuyuanbo on 2018/12/4 09:51.
 * 时间： 2018/12/4 09:51
 * 邮箱： 972383753@qq.com
 * 用途： 创建文本的页面
 */
class QQCreateTextActivity: BaseCreateActivity() {
    private lateinit var mHelper: QQScreenShotHelper
    private var mMsgEntity: QQScreenShotEntity = QQScreenShotEntity()
    /**
     * 0 -- 发布新的文本
     * 1 -- 编辑修改文本
     */
    private var mType = 0
    override fun setLayoutResourceId() = R.layout.activity_qq_create_text

    override fun needLoadingView() = false

    override fun initAllViews() {
        setBlackTitle("文本", 1)
        mHelper = QQScreenShotHelper(this)
    }

    override fun initViewsListener() {
        mEmojiBoard.setItemClickListener { code ->
            //emoji点击事件
            if (code == "/DEL") {//点击了删除图标
                mEmojiEt.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
            } else {
                mEmojiEt.text.insert(mEmojiEt.selectionStart, code)
            }
        }
        mEmojiEt.addTextChangedListener(textWatcher)
        mWechatCreateChoiceMySideLayout.setOnClickListener(this)
        mWechatCreateChoiceOtherSideLayout.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mMsgEntity.msgType = 0
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        mOtherSideEntity = intent.getSerializableExtra(IntentKey.OTHER_SIDE) as WechatUserEntity
        GlideUtil.displayHead(this, mOtherSideEntity.getAvatarFile(), mWechatCreateChoiceOtherSideAvatarIv)
        mWechatCreateChoiceOtherSideNickNameTv.text = mOtherSideEntity.wechatUserNickName
        mMySideEntity = UserOperateUtil.getMySelf()
        GlideUtil.displayHead(this, mMySideEntity.getAvatarFile(), mWechatCreateChoiceMySideAvatarIv)
        mWechatCreateChoiceMySideNickNameTv.text = mMySideEntity.wechatUserNickName
        setMsg(mMySideEntity)
        if (mType == 1){
            mMsgEntity = intent.getSerializableExtra(IntentKey.ENTITY) as QQScreenShotEntity
            val cs = EmojiQQManager.parse(mMsgEntity.msg, mEmojiEt.textSize)
            mEmojiEt.setText(cs, TextView.BufferType.SPANNABLE)
            if (mMsgEntity.wechatUserId == mMySideEntity.wechatUserId){
                setChoice(mWechatCreateChoiceMySideChoiceIv, mWechatCreateChoiceOtherSideChoiceIv)
                setMsg(mMySideEntity)
            }else{
                setChoice(mWechatCreateChoiceOtherSideChoiceIv, mWechatCreateChoiceMySideChoiceIv)
                setMsg(mOtherSideEntity)
            }
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatCreateChoiceMySideLayout ->{
                setChoice(mWechatCreateChoiceMySideChoiceIv, mWechatCreateChoiceOtherSideChoiceIv)
                setMsg(mMySideEntity)
                mMsgEntity.isComMsg = false
            }
            R.id.mWechatCreateChoiceOtherSideLayout ->{
                setChoice(mWechatCreateChoiceOtherSideChoiceIv, mWechatCreateChoiceMySideChoiceIv)
                setMsg(mOtherSideEntity)
                mMsgEntity.isComMsg = true
            }
            R.id.overallAllRightWithBgTv ->{
                if(TextUtils.isEmpty(mEmojiEt.text.toString().trim())){
                    showToast("请输入聊天内容")
                    return
                }
                mMsgEntity.msg = mEmojiEt.text.toString()
                if(mType == 0){
                    mHelper.save(mMsgEntity)
                }else{
                    mHelper.update(mMsgEntity)
                }
                finish()
            }
        }
    }
    /**
     * 输入监听事件
     */
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun afterTextChanged(editable: Editable) {
//            sendBtn.setEnabled(!editable.toString().isEmpty())//输入完不为空内容才可以点击
            val start = mEmojiEt.selectionStart
            val end = mEmojiEt.selectionEnd
            mEmojiEt.removeTextChangedListener(this)
            val cs = EmojiQQManager.parse(editable.toString(), mEmojiEt.textSize)
            mEmojiEt.setText(cs, TextView.BufferType.SPANNABLE)
            mEmojiEt.setSelection(start, end)
            mEmojiEt.addTextChangedListener(this)
        }
    }

    private fun setChoice(choiceIv: ImageView, unChoiceIv: ImageView){
        choiceIv.setImageResource(R.drawable.choice)
        unChoiceIv.setImageResource(R.drawable.un_choice)
    }
    private fun setMsg(entity: WechatUserEntity){
        mMsgEntity.avatarInt = entity.resAvatar
        mMsgEntity.avatarStr = entity.wechatUserAvatar
        mMsgEntity.wechatUserId = entity.wechatUserId
    }
}
