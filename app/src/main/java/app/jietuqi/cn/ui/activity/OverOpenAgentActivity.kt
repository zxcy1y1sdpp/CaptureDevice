package app.jietuqi.cn.ui.activity

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Toast
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.util.StringUtils
import com.xinlan.imageeditlibrary.ToastUtils
import kotlinx.android.synthetic.main.activity_overall_open_agent.*


/**
 * 作者： liuyuanbo on 2018/11/6 10:22.
 * 时间： 2018/11/6 10:22
 * 邮箱： 972383753@qq.com
 * 用途： 客服选择
 */

class OverOpenAgentActivity : BaseOverallActivity() {
    override fun setLayoutResourceId() = R.layout.activity_overall_open_agent

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("开通代理")
    }

    override fun initViewsListener() {
        mOverallOpenAgentQQBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallOpenAgentQQBtn ->{
                try {
                    //第二种方式：可以跳转到添加好友，如果qq号是好友了，直接聊天
                    val url = StringUtils.insertBack("mqqwpa://im/chat?chat_type=wpa&uin=", getString(R.string.qqService))//uin是发送过去的qq号码
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

                } catch (e: Exception) {
                    e.printStackTrace()
                    ToastUtils.showShort(this, "请检查是否安装QQ客户端")
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
