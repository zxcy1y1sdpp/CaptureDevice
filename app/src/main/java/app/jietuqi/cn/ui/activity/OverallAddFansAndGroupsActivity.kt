package app.jietuqi.cn.ui.activity

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.entity.OverallIndustryEntity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_add_fans_and_group.*

/**
 * 作者： liuyuanbo on 2018/11/10 14:47.
 * 时间： 2018/11/10 14:47
 * 邮箱： 972383753@qq.com
 * 用途： 加粉加群页面
 */
class OverallAddFansAndGroupsActivity : BaseOverallActivity() {
    private var mDialog: SweetAlertDialog? = null
    override fun setLayoutResourceId() = R.layout.activity_overall_add_fans_and_group

    override fun needLoadingView() = false

    override fun initAllViews() {
        setTitle("加粉加群", 0)
        getIndustryData()
        getGroupIndustryData()
    }

    override fun initViewsListener() {
        mOverallAddFansAndGroupAddFansBtn.setOnClickListener(this)
        mOverallAddFansAndGroupAddGroupsBtn.setOnClickListener(this)
        mOverallAddFansAndGroupPreciseAddFansLayout.setOnClickListener(this)
        mOverallAddFansAndGroupNumberAddFansLayout.setOnClickListener(this)
        mOverallAddFansAndGroupVipBoomLayout.setOnClickListener(this)
        mOverallAddFansAndGroupClearAddressListLayout.setOnClickListener(this)
        mOverallAddFansAndGroupUseHelpLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallAddFansAndGroupAddFansBtn ->{//加粉
                LaunchUtil.startOverallJoinGroupsActivity(this, 0)
            }
            R.id.mOverallAddFansAndGroupAddGroupsBtn ->{//加群
                LaunchUtil.startOverallJoinGroupsActivity(this, 1)
            }
            R.id.mOverallAddFansAndGroupPreciseAddFansLayout ->{//精准加粉
                LaunchUtil.launch(this, OverallPreciseAddFansActivity::class.java)
            }
            R.id.mOverallAddFansAndGroupNumberAddFansLayout ->{//号段加粉
                LaunchUtil.launch(this, OverallNumberAddFansActivity::class.java)
            }
            R.id.mOverallAddFansAndGroupVipBoomLayout ->{//VIP高级爆粉

            }
            R.id.mOverallAddFansAndGroupClearAddressListLayout ->{//清理通讯录
                mDialog = SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("删除记录")
                        .setContentText("只清除加粉时导入的数据，不会破坏您的原有通讯录")
                        .setConfirmText("下一步")
                        .setCancelText("取消")
                        .setConfirmClickListener {
                            Thread.sleep(2000)
                            mDialog?.setTitleText("删除记录成功！")
                                    ?.setConfirmText("我知道了")
                                    ?.showCancelButton(false)
                                    ?.setConfirmClickListener {
                                        it.dismissWithAnimation()
                                    }
                                    ?.changeAlertType(SweetAlertDialog.SUCCESS_TYPE)
                        }
                        .setCancelClickListener {
                            it.dismissWithAnimation()
                        }
                mDialog?.show()
            }
            R.id.mOverallAddFansAndGroupUseHelpLayout ->{//使用教程

            }
        }
    }

    /**
     * 获取行业类别
     */
    private fun getIndustryData(){
        EasyHttp.post(HttpConfig.INFORMATION)
                .params("way", "industry")
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallIndustryEntity>>, ArrayList<OverallIndustryEntity>>(object : SimpleCallBack<ArrayList<OverallIndustryEntity>>() {
                    override fun onSuccess(t: ArrayList<OverallIndustryEntity>?) {
                        SharedPreferencesUtils.putListData(SharedPreferenceKey.INDUSTRY, t)
                    }
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }
                }) {})
    }
    /**
     * 获取行业类别
     */
    private fun getGroupIndustryData(){
        EasyHttp.post(HttpConfig.INFORMATION)
                .params("way", "heapsort")
                .execute(object : CallBackProxy<OverallApiEntity<ArrayList<OverallIndustryEntity>>, ArrayList<OverallIndustryEntity>>(object : SimpleCallBack<ArrayList<OverallIndustryEntity>>() {
                    override fun onSuccess(t: ArrayList<OverallIndustryEntity>?) {
                        SharedPreferencesUtils.putListData(SharedPreferenceKey.HEAPSORT, t)
                    }
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }
                }) {})
    }
}
