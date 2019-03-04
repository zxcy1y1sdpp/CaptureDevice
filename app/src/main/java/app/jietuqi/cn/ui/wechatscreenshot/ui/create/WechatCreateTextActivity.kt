package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.content.Intent
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatScreenShotCreateActivity
import app.jietuqi.cn.ui.wechatscreenshot.widget.EmojiWechatManager
import kotlinx.android.synthetic.main.activity_wechat_create_text.*

/**
 * 作者： liuyuanbo on 2018/12/4 09:51.
 * 时间： 2018/12/4 09:51
 * 邮箱： 972383753@qq.com
 * 用途： 创建文本的页面
 */
class WechatCreateTextActivity: BaseWechatScreenShotCreateActivity() {
    override fun setLayoutResourceId() = R.layout.activity_wechat_create_text

    override fun needLoadingView() = false

    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 0
        setBlackTitle("文本", 1)
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mEmojiBoard.setItemClickListener { code ->
            //emoji点击事件
            if (code == "/DEL") {//点击了删除图标
                mEmojiEt.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
            } else {
                mEmojiEt.text.insert(mEmojiEt.selectionStart, code)
            }
        }
        mEmojiEt.addTextChangedListener(textWatcher)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mType == 1){
            val cs = EmojiWechatManager.parse(mMsgEntity.msg, mEmojiEt.textSize)
            mEmojiEt.setText(cs, TextView.BufferType.SPANNABLE)
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.overallAllRightWithBgTv ->{
                if(TextUtils.isEmpty(mEmojiEt.text.toString().trim())){
                    showToast("请输入聊天内容")
                    return
                }
                mMsgEntity.msg = mEmojiEt.text.toString()
            }
        }
        super.onClick(v)
    }
    /**
     * 输入监听事件
     */
    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun afterTextChanged(editable: Editable) {
            val start = mEmojiEt.selectionStart
            val end = mEmojiEt.selectionEnd
            mEmojiEt.removeTextChangedListener(this)
            val cs = EmojiWechatManager.parse(editable.toString(), mEmojiEt.textSize)
            mEmojiEt.setText(cs, TextView.BufferType.SPANNABLE)
            mEmojiEt.setSelection(start, end)
            mEmojiEt.addTextChangedListener(this)
        }
    }
}
