package dasheng.com.capturedevice.ui.activity

import android.app.PendingIntent.getActivity
import android.content.*
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseOverallActivity
import kotlinx.android.synthetic.main.activity_overall_choice_service.*
import android.content.Intent
import dasheng.com.capturedevice.util.StringUtils


/**
 * 作者： liuyuanbo on 2018/11/6 10:22.
 * 时间： 2018/11/6 10:22
 * 邮箱： 972383753@qq.com
 * 用途： 客服选择
 */

class OverAllChoiceServiceActivity : BaseOverallActivity() {

    /**
     * 不用父类的statusbar颜色就需要重写oncreate（）并且设置颜色
     */
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ColorFinal.alipayBalanceRed)
        var tintManager: SystemBarTintManager  = SystemBarTintManager(this)
             // 启用状态栏渐变
        tintManager.isStatusBarTintEnabled = true
             //设置状态栏颜色与ActionBar颜色相连
             tintManager.setStatusBarTintResource(R.color.gray2)
        Looper.myQueue().addIdleHandler {
            if (true) {
                initStatusBar()
                window.decorView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom -> initStatusBar() }
            }
            false
        }
        initStatusBar()
    }*/
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
