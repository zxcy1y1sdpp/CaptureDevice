package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.support.v7.widget.RecyclerView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseCreateActivity
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.ui.wechatscreenshot.adapter.WechatChoiceEmojiAdapter
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.activity_wechat_choice_emoji.*

/**
 * 作者： liuyuanbo on 2019/3/4 15:43.
 * 时间： 2019/3/4 15:43
 * 邮箱： 972383753@qq.com
 * 用途： 选择表情
 */
class WechatChoiceEmojiActivity : BaseCreateActivity() {
    private var mList = arrayListOf<String>()
    private lateinit var mAdapter: WechatChoiceEmojiAdapter
    override fun setLayoutResourceId() = R.layout.activity_wechat_choice_emoji

    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("选择表情")
        mList.add("R.drawable.wechat_big_gif1")
        mList.add("R.drawable.wechat_big_gif2")
        mList.add("R.drawable.wechat_big_gif3")
        mList.add("R.drawable.wechat_big_gif4")
        mList.add("R.drawable.wechat_big_gif5")
        mList.add("R.drawable.wechat_big_gif6")
        mList.add("R.drawable.wechat_big_gif7")
        mList.add("R.drawable.wechat_big_gif8")
        mList.add("R.drawable.wechat_big_gif9")
        mList.add("R.drawable.wechat_big_gif10")
        mList.add("R.drawable.wechat_big_gif11")
        mList.add("R.drawable.wechat_big_gif12")
        mList.add("R.drawable.wechat_big_gif13")
        mList.add("R.drawable.wechat_big_gif14")
        mList.add("R.drawable.wechat_big_gif15")
        mList.add("R.drawable.wechat_big_gif16")
        mList.add("R.drawable.wechat_big_gif17")
        mList.add("R.drawable.wechat_big_gif18")
        mList.add("R.drawable.wechat_big_gif19")
        mList.add("R.drawable.wechat_big_gif20")
        mList.add("R.drawable.wechat_big_gif21")
        mList.add("R.drawable.wechat_big_gif22")
        mList.add("R.drawable.wechat_big_gif23")
        mList.add("R.drawable.wechat_big_gif24")
        mList.add("R.drawable.wechat_big_gif25")
        mList.add("R.drawable.wechat_big_gif26")
        mList.add("R.drawable.wechat_big_gif27")
        mList.add("R.drawable.wechat_big_gif28")
        mList.add("R.drawable.wechat_big_gif29")
        mList.add("R.drawable.wechat_big_gif30")
        mList.add("R.drawable.wechat_big_gif31")
        mList.add("R.drawable.wechat_big_gif32")
        mList.add("R.drawable.wechat_big_gif33")
        mList.add("R.drawable.wechat_big_gif34")
        mList.add("R.drawable.wechat_big_gif35")
        mList.add("R.drawable.wechat_big_gif36")
        mList.add("R.drawable.wechat_big_gif37")
        mList.add("R.drawable.wechat_big_gif38")
        mList.add("R.drawable.wechat_big_gif39")
        mList.add("R.drawable.wechat_big_gif40")

        mList.add("R.drawable.wechat_dice_1")
        mList.add("R.drawable.wechat_dice_2")
        mList.add("R.drawable.wechat_dice_3")
        mList.add("R.drawable.wechat_dice_4")
        mList.add("R.drawable.wechat_dice_5")
        mList.add("R.drawable.wechat_dice_6")
        mList.add("R.drawable.wechat_guess_1")
        mList.add("R.drawable.wechat_guess_2")
        mList.add("R.drawable.wechat_guess_3")

        mAdapter = WechatChoiceEmojiAdapter(mList)
        mEmojiRv.adapter = mAdapter
    }

    override fun initViewsListener() {
        mEmojiRv.addOnItemTouchListener(object : OnRecyclerItemClickListener(mEmojiRv) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                EventBusUtil.post(mList[vh.adapterPosition])
                finish()
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
    }
}
