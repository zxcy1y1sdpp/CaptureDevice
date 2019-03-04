package app.jietuqi.cn.ui.activity

import android.content.*
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.ProjectMarketEntity
import app.jietuqi.cn.util.*
import com.xinlan.imageeditlibrary.ToastUtils
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.EventBusUtil
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_project_popularize.*

/**
 * 作者： liuyuanbo on 2019/1/29 22:22.
 * 时间： 2019/1/29 22:22
 * 邮箱： 972383753@qq.com
 * 用途： 项目推广
 */
class ProjectPopularizeActivity : BaseOverallInternetActivity() {
    private lateinit var mEntity: ProjectMarketEntity
    override fun setLayoutResourceId() = R.layout.activity_overall_project_popularize

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("项目推广", rightTitle = "我的微币", rightTvColor = R.color.transactionRecord)
    }

    override fun initViewsListener() {
        mProjectPopularizeTopBuyTv.setOnClickListener(this)
        mProjectPopularizeConnectTv.setOnClickListener(this)
        mProjectPopularizeTopBuyTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mEntity = intent.getSerializableExtra(IntentKey.ENTITY) as ProjectMarketEntity
        if (mEntity.is_top > 0){
            mProjectPopularizeTopStatusTv.text = StringUtils.insertFront(TimeUtil.stampToDate(mEntity.top_time.toLong()), "到期时间：")
            mProjectPopularizeTopBuyTv.text = "续费"
        }
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mProjectPopularizeConnectTv ->{
                try {
                    //获取剪贴板管理器：
                    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    // 创建普通字符型ClipData
                    val mClipData = ClipData.newPlainText("Label", OtherUtil.getContent(mProjectPopularizeConnectTv))
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
            R.id.mProjectPopularizeTopBuyTv ->{
                setTop()
            }
            R.id.overAllRightTitleTv -> {
                if (UserOperateUtil.isCurrentLogin(this)){
                    LaunchUtil.launch(this, OverallWbActivity::class.java)
                }
            }
        }
    }
    private fun setTop(){
        EasyHttp.post(HttpConfig.STORE, false)
                .params("way", "timetop")
                .params("id", mEntity.id.toString())
                .params("time", "24")
                .execute(object : SimpleCallBack<String>() {
                    override fun onStart() {
                        showLoadingDialog()
                        super.onStart()
                        showLoadingDialog()
                    }
                    override fun onError(e: ApiException) {
                        showToast(e.message)
                    }
                    override fun onSuccess(t: String) {
                        EventBusUtil.post("置顶成功")
                        showToast("置顶成功")
                        finish()
                    }
                })
    }
}
