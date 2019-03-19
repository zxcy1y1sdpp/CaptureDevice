package app.jietuqi.cn.ui.activity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallCardEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.dialog.customdialog.EnsureDialog
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_explode.*



/**
 * 作者： liuyuanbo on 2018/11/26 21:54.
 * 时间： 2018/11/26 21:54
 * 邮箱： 972383753@qq.com
 * 用途： 爆粉
 */
class OverallExplodeActivity : BaseOverallActivity() {
    /**
     * 是否发布了个人名片
     * 有名片才可以爆粉
     */
    private var mHasCard = false
    override fun setLayoutResourceId() = R.layout.activity_overall_explode

    override fun needLoadingView() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBarColor(ColorFinal.explore)
        setLightStatusBarForM(this, false)
        getCard()
    }
    override fun initAllViews() {
        setTopTitle("VIP爆粉", leftColor = R.color.white, leftIv = R.drawable.back_white, bgColor = R.color.explore, contentColor = R.color.white)

    }
    override fun initViewsListener() {
        mOverallExplodeView.setOnClickListener(this)
        if (UserOperateUtil.isExploring()){
            mOverallExplodeView.text = "正在爆粉"
            mRadarView.startRippleAnimation()
        }
    }
    /**
     * 获取我的名片
     */
    private fun getCard(){
        val postRequest = EasyHttp.post(HttpConfig.INFORMATION, false)
        postRequest.params("way", "id").params("uid", UserOperateUtil.getUserId())
        postRequest.execute(object : CallBackProxy<OverallApiEntity<OverallCardEntity>, OverallCardEntity>(object : SimpleCallBack<OverallCardEntity>() {
            override fun onSuccess(t: OverallCardEntity?) {
                mHasCard = true
            }
            override fun onError(e: ApiException) {
                mHasCard = false
            }
        }) {})
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallExplodeView ->{
                if (UserOperateUtil.isExploring()){
                    mRadarView.stopRippleAnimation()
                    mRadarView.visibility = View.GONE
                    mOverallExplodeView.text = "开始爆粉"
                    SharedPreferencesUtils.putData(SharedPreferenceKey.EXPLORING, false)
                }else{
                    if (mHasCard){
                        explore()
                    }else{
                        EnsureDialog(this).builder()
                                .setGravity(Gravity.CENTER)//默认居中，可以不设置
                                .setTitle("提示")//可以不设置标题颜色，默认系统颜色
                                .setCancelable(false)
                                .setSubTitle("您还没有发布个人名片!")
                                .setNegativeButton("取消") {}
                                .setPositiveButton("去发布") {
                                    LaunchUtil.startOverallPublishCardActivity(this, 0, OverallCardEntity())
                                }
                                .show()
                    }
                }
            }
        }

    }
    fun explore(){
        EasyHttp.post(HttpConfig.INFORMATION, false)
                .params("way", "top")
                .params("uid", UserOperateUtil.getUserId())
                .execute(object : SimpleCallBack<String>() {
                    override fun onStart() {
                        showLoadingDialog()
                        super.onStart()
                        showLoadingDialog()
                    }
                    override fun onError(e: ApiException) {
                        showToast(e.message)
                        SharedPreferencesUtils.putData(SharedPreferenceKey.EXPLORING, false)
                    }
                    override fun onSuccess(t: String) {
                        showToast("开始爆粉")
                        mOverallExplodeView.text = "正在爆粉"
                        mRadarView.startRippleAnimation()
                        mRadarView.visibility = View.VISIBLE
                        SharedPreferencesUtils.putData(SharedPreferenceKey.EXPLORING, true)
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        getCard()
    }
}
