package app.jietuqi.cn.ui.activity

import android.content.*
import android.net.Uri
import android.view.View
import android.widget.Toast
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.util.StringUtils
import kotlinx.android.synthetic.main.activity_overall_choice_service.*


/**
 * 作者： liuyuanbo on 2018/11/6 10:22.
 * 时间： 2018/11/6 10:22
 * 邮箱： 972383753@qq.com
 * 用途： 客服选择
 */

class OverAllChoiceServiceActivity : BaseOverallActivity() {
    override fun setLayoutResourceId() = R.layout.activity_overall_choice_service

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {

    }

    override fun initViewsListener() {
        mOverallChoiceServiceWechatBtn.setOnClickListener(this)
        mOverallChoiceServiceQQBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallChoiceServiceWechatBtn ->{
                try {
                  //获取剪贴板管理器：
                    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    // 创建普通字符型ClipData
                    val mClipData = ClipData.newPlainText("Label", getString(R.string.wechatService))
                    // 将ClipData内容放到系统剪贴板里。
                    cm.primaryClip = mClipData

                    val intent = Intent()
                    val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
                    intent.action = Intent.ACTION_MAIN
                    intent.addCategory(Intent.CATEGORY_LAUNCHER)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.component = cmp
                    startActivity(intent)

                    Toast.makeText(this, "成功复制客服微信号", Toast.LENGTH_SHORT).show()
                }catch (e: ActivityNotFoundException){
                    Toast.makeText(this, "您还没有安装微信，请安装后使用", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.mOverallChoiceServiceQQBtn ->{
                try {
                    //第二种方式：可以跳转到添加好友，如果qq号是好友了，直接聊天
                    val url = StringUtils.insertBack("mqqwpa://im/chat?chat_type=wpa&uin=", getString(R.string.qqService))//uin是发送过去的qq号码
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "请检查是否安装QQ客户端", Toast.LENGTH_SHORT).show()
                }

//                //加人
//                val urlQQ = "mqqwpa://im/chat?chat_type=wpa&uin=qqnumber&version=1"
//                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlQQ)))
//
//                //加群
//                val urlQQQ = "mqqwpa://im/chat?chat_type=group&uin=qqnumber&version=1"
//                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlQQ)))
            }
        }
    }

}
