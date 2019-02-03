package app.jietuqi.cn.ui.activity

import android.content.*
import android.net.Uri
import android.view.View
import android.widget.Toast
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.util.OtherUtil
import com.xinlan.imageeditlibrary.ToastUtils
import kotlinx.android.synthetic.main.activity_overall_server_helper.*

/**
 * 作者： liuyuanbo on 2019/1/29 15:11.
 * 时间： 2019/1/29 15:11
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class OverallServerHelpActivity : BaseOverallActivity() {
    override fun setLayoutResourceId() = R.layout.activity_overall_server_helper

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("客服帮助")

    }

    override fun initViewsListener() {
        mQQServerTv.setOnClickListener(this)
        mWxServerTv.setOnClickListener(this)
        mWxPublicServerTv.setOnClickListener(this)
        mQuestion1Layout.setOnClickListener(this)
        mQuestion2Layout.setOnClickListener(this)
        mQuestion3Layout.setOnClickListener(this)
        mQuestion4Layout.setOnClickListener(this)
        mQuestion5Layout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mQQServerTv ->{
                try {
                    val url = "mqqwpa://im/chat?chat_type=wpa&uin=" + mQQServerTv.tag.toString()
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                } catch (e: Exception) {
                    Toast.makeText(this, "尚未安装QQ", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.mWxServerTv ->{
                try {
                    //获取剪贴板管理器：
                    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    // 创建普通字符型ClipData
                    val mClipData = ClipData.newPlainText("Label", OtherUtil.getContent(mWxServerTv))
                    // 将ClipData内容放到系统剪贴板里。
                    cm.primaryClip = mClipData

                    val intent = Intent()
                    val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
                    intent.action = Intent.ACTION_MAIN
                    intent.addCategory(Intent.CATEGORY_LAUNCHER)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.component = cmp
                    startActivity(intent)
                    ToastUtils.showShort(this, "成功复制客服微信号")
                }catch (e: ActivityNotFoundException){
                    ToastUtils.showShort(this, "您还没有安装微信，请安装后使用")
                }
            }
            R.id.mWxPublicServerTv ->{
                try {
                    //获取剪贴板管理器：
                    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    // 创建普通字符型ClipData
                    val mClipData = ClipData.newPlainText("Label", OtherUtil.getContent(mWxPublicServerTv))
                    // 将ClipData内容放到系统剪贴板里。
                    cm.primaryClip = mClipData

                    val intent = Intent()
                    val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
                    intent.action = Intent.ACTION_MAIN
                    intent.addCategory(Intent.CATEGORY_LAUNCHER)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.component = cmp
                    startActivity(intent)

                    ToastUtils.showShort(this, "成功复制微信公众号")
                }catch (e: ActivityNotFoundException){
                    ToastUtils.showShort(this, "您还没有安装微信，请安装后使用")
                }
            }
            R.id.mQuestion1Layout ->{
                if (mAnswer1Tv.visibility == View.VISIBLE){
                    mAnswer1Tv.visibility = View.GONE
                    mAnswer1Iv.setImageResource(R.drawable.question_not_show)
                }else{
                    mAnswer1Tv.visibility = View.VISIBLE
                    mAnswer1Iv.setImageResource(R.drawable.question_show)
                }
            }
            R.id.mQuestion2Layout ->{
                if (mAnswer2Tv.visibility == View.VISIBLE){
                    mAnswer2Tv.visibility = View.GONE
                    mAnswer2Iv.setImageResource(R.drawable.question_not_show)
                }else{
                    mAnswer2Tv.visibility = View.VISIBLE
                    mAnswer2Iv.setImageResource(R.drawable.question_show)
                }
            }
            R.id.mQuestion3Layout ->{
                if (mAnswer3Tv.visibility == View.VISIBLE){
                    mAnswer3Tv.visibility = View.GONE
                    mAnswer3Iv.setImageResource(R.drawable.question_not_show)
                }else{
                    mAnswer3Tv.visibility = View.VISIBLE
                    mAnswer3Iv.setImageResource(R.drawable.question_show)
                }
            }
            R.id.mQuestion4Layout ->{
                if (mAnswer4Tv.visibility == View.VISIBLE){
                    mAnswer4Tv.visibility = View.GONE
                    mAnswer4Iv.setImageResource(R.drawable.question_not_show)
                }else{
                    mAnswer4Tv.visibility = View.VISIBLE
                    mAnswer4Iv.setImageResource(R.drawable.question_show)
                }
            }
            R.id.mQuestion5Layout ->{
                if (mAnswer5Tv.visibility == View.VISIBLE){
                    mAnswer5Tv.visibility = View.GONE
                    mAnswer5Iv.setImageResource(R.drawable.question_not_show)
                }else{
                    mAnswer5Tv.visibility = View.VISIBLE
                    mAnswer5Iv.setImageResource(R.drawable.question_show)
                }
            }
        }
    }
}
