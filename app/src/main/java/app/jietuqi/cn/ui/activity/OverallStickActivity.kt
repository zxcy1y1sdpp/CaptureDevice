package app.jietuqi.cn.ui.activity

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_stick.*

/**
 * 作者： liuyuanbo on 2019/1/30 11:21.
 * 时间： 2019/1/30 11:21
 * 邮箱： 972383753@qq.com
 * 用途： 置顶服务
 */
class OverallStickActivity : BaseOverallInternetActivity() {
    /**
     * 0 -- 互粉置顶
     * 1 -- 加群置顶
     */
    private var mClassifyType = "user"
    override fun setLayoutResourceId() = R.layout.activity_overall_stick

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("置顶服务", rightTitle = "我的微币", rightTvColor = R.color.transactionRecord)
    }

    override fun initViewsListener() {
        mServer1Tv.setOnClickListener(this)
        mServer2Tv.setOnClickListener(this)
        mServer3Tv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val type = intent.getIntExtra(IntentKey.TYPE, 0)
        mClassifyType = if (type == 0){
            "user"
        }else{
            "group"
        }
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mServer1Tv -> {
                if (UserOperateUtil.isCurrentLogin(this)){
                    setTop("1")
                }
            }
            R.id.mServer2Tv -> {
                if (UserOperateUtil.isCurrentLogin(this)){
                    setTop("3")
                }
            }
            R.id.mServer3Tv -> {
                if (UserOperateUtil.isCurrentLogin(this)){
                    setTop("24")
                }
            }
            R.id.overAllRightTitleTv -> {
                if (UserOperateUtil.isCurrentLogin(this)){
                    LaunchUtil.launch(this, OverallWbActivity::class.java)
                }
            }
        }
    }
    private fun setTop(time: String){
        EasyHttp.post(HttpConfig.INFORMATION)
                .params("way", "timetop")
                .params("uid", UserOperateUtil.getUserId())
                .params("time", time)
                .params("classify", mClassifyType)
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
