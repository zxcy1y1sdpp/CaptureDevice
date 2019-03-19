package app.jietuqi.cn.ui.activity

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.entity.OverallUserInfoEntity
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.entity.MyClientEntity
import app.jietuqi.cn.ui.entity.OverallApiEntity
import app.jietuqi.cn.ui.widget.MyClientDialog
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import com.xinlan.imageeditlibrary.ToastUtils
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.CallBackProxy
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import com.zhouyou.http.request.PostRequest
import kotlinx.android.synthetic.main.activity_overall_my_client.*
import java.util.*

/**
 * 作者： liuyuanbo on 2019/3/14 17:30.
 * 时间： 2019/3/14 17:30
 * 邮箱： 972383753@qq.com
 * 用途： 我的客户页面
 */
class OverallMyClientActivity : BaseOverallInternetActivity() {
    private lateinit var mEntity: MyClientEntity
    private lateinit var mDialog: MyClientDialog
    private var mList = arrayListOf<OverallUserInfoEntity>()
    override fun setLayoutResourceId() = R.layout.activity_overall_my_client

    override fun needLoadingView() = false

    override fun initAllViews() {
        setStatusBarColor(ColorFinal.OVERALL_BLUE)
        setTopTitle("我的客户", bgColor = R.color.wechatBlue, contentColor = R.color.white, leftIv = R.drawable.back_white)
        getData()
        mDialog = MyClientDialog()
    }

    override fun initViewsListener() {
        mZtUserCountLayout.setOnClickListener(this)
        mJtUserCountLayout.setOnClickListener(this)
        mZtMerchantCountLayout.setOnClickListener(this)
        mJtMerchantCountLayout.setOnClickListener(this)
        mCopyWechatNumberTv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        mList.clear()
        when(v.id){
            R.id.mZtUserCountLayout ->{
                mList.addAll(mEntity.z_user)
                LaunchUtil.startOverallMyClientDetailsActivity(this, mList,  mZtUserCountTv.text.toString())
            }
            R.id.mJtUserCountLayout ->{
                mList.addAll(mEntity.j_user)
                LaunchUtil.startOverallMyClientDetailsActivity(this, mList, mJtUserCountTv.text.toString())
            }
            R.id.mZtMerchantCountLayout ->{
                mList.addAll(mEntity.z_agent)
                LaunchUtil.startOverallMyClientDetailsActivity(this, mList,  mZtMerchantCountTv.text.toString())
            }
            R.id.mJtMerchantCountLayout ->{
                mList.addAll(mEntity.j_agent)
                LaunchUtil.startOverallMyClientDetailsActivity(this, mList, mJtMerchantCountTv.text.toString())
            }
            R.id.mCopyWechatNumberTv ->{
                copyWxNumber()
            }
        }
//        mDialog.setData(mList)
//        if (!mDialog.isAdded){
//            mDialog.show(supportFragmentManager, "client")
//        }
    }
    private fun getData(){
        var request: PostRequest = EasyHttp.post(HttpConfig.AGENT, false).params("way", "myclient").params("users_id", UserOperateUtil.getUserId())
        request.execute(object : CallBackProxy<OverallApiEntity<MyClientEntity>, MyClientEntity>(object : SimpleCallBack<MyClientEntity>() {
            override fun onError(e: ApiException) {
                e.message?.let { showToast(it) }
            }

            override fun onSuccess(t: MyClientEntity) {
                mEntity = t
                mWechatNumberTv.text = StringUtils.insertFront(mEntity.superior.wx_account, "微信号：")
                mZtUserCountTv.text = StringUtils.insertFrontAndBack(mEntity.z_user.size, "直推用户：", "人")
                mJtUserCountTv.text = StringUtils.insertFrontAndBack(mEntity.j_user.size, "间推用户：", "人")
                mZtMerchantCountTv.text = StringUtils.insertFrontAndBack(mEntity.z_agent.size, "直推店主：", "人")
                mJtMerchantCountTv.text = StringUtils.insertFrontAndBack(mEntity.j_agent.size, "直推店主：", "人")
                setDetails(mEntity.z_user, mZtUserCountDetailLayout, mZtUserCountDetailNickNameTv, mZtUserCountDetailVipLevelTv)
                setDetails(mEntity.j_user, mJtUserCountDetailLayout, mJtUserCountDetailNickNameTv, mJtUserCountDetailVipLevelTv)
                setDetails(mEntity.z_agent, mZtMerchantCountDetailLayout, mZtMerchantCountDetailNickNameTv, mZtMerchantCountDetailVipLevelTv)
                setDetails(mEntity.j_agent, mJtMerchantCountDetailLayout, mJtMerchantCountDetailNickNameTv, mJtMerchantCountDetailVipLevelTv)
                /*if (mEntity.z_user.size == 0){
                    mZtUserCountDetailLayout.visibility = View.GONE
                }else{
                    val ztUser = mEntity.z_user[0]
                    mZtUserCountDetailNickNameTv.text = ztUser.nickname
                    when(ztUser.status){
                        1 ->{
                            mZtUserCountDetailVipLevelTv.text = "普通会员"
                        }
                        2 ->{
                            mZtUserCountDetailVipLevelTv.text = "季度会员"
                        }
                        3 ->{
                            mZtUserCountDetailVipLevelTv.text = "年度会员"
                        }
                        5 ->{
                            mZtUserCountDetailVipLevelTv.text = "半年会员"
                        }
                    }
                }
                if (mEntity.j_user.size == 0){
                    mJtUserCountDetailLayout.visibility = View.GONE
                }else{
                    val jtUser = mEntity.j_user[0]
                    mJtUserCountDetailNickNameTv.text = jtUser.nickname
                    when(jtUser.status){
                        1 ->{
                            mJtUserCountDetailVipLevelTv.text = "普通会员"
                        }
                        2 ->{
                            mJtUserCountDetailVipLevelTv.text = "季度会员"
                        }
                        3 ->{
                            mJtUserCountDetailVipLevelTv.text = "年度会员"
                        }
                        5 ->{
                            mJtUserCountDetailVipLevelTv.text = "半年会员"
                        }
                    }
                }
                if (mEntity.z_agent.size == 0){
                    mZtMerchantCountDetailLayout.visibility = View.GONE
                }else{
                    val ztAgent = mEntity.z_agent[0]
                    mZtMerchantCountDetailNickNameTv.text = ztAgent.nickname
                    when(ztAgent.status){
                        1 ->{
                            mZtMerchantCountDetailVipLevelTv.text = "普通会员"
                        }
                        2 ->{
                            mZtMerchantCountDetailVipLevelTv.text = "季度会员"
                        }
                        3 ->{
                            mZtMerchantCountDetailVipLevelTv.text = "年度会员"
                        }
                        5 ->{
                            mZtMerchantCountDetailVipLevelTv.text = "半年会员"
                        }
                    }
                }
                if (mEntity.j_agent.size == 0){
                    mJtMerchantCountDetailLayout.visibility = View.GONE
                }else{
                    val jtAgent = mEntity.j_agent[0]
                    mJtMerchantCountDetailNickNameTv.text = jtAgent.nickname
                    when(jtAgent.status){
                        1 ->{
                            mJtMerchantCountDetailVipLevelTv.text = "普通会员"
                        }
                        2 ->{
                            mJtMerchantCountDetailVipLevelTv.text = "季度会员"
                        }
                        3 ->{
                            mJtMerchantCountDetailVipLevelTv.text = "年度会员"
                        }
                        5 ->{
                            mJtMerchantCountDetailVipLevelTv.text = "半年会员"
                        }
                    }
                }*/
            }
        }) {})
    }

    private fun setDetails(list: ArrayList<OverallUserInfoEntity>, layout: RelativeLayout, nickName: TextView, vipLevel: TextView){
        if (list.size == 0){
            layout.visibility = View.GONE
        }else{
            val entity = list[0]
            nickName.text = entity.nickname
            when(entity.status){
                1 ->{
                    vipLevel.text = "普通会员"
                }
                2 ->{
                    vipLevel.text = "季度会员"
                }
                3 ->{
                    vipLevel.text = "年度会员"
                }
                5 ->{
                    vipLevel.text = "半年会员"
                }
                6 ->{
                    vipLevel.text = "体验会员"
                }
            }
        }
    }

    private fun copyWxNumber(){
        try {
            //获取剪贴板管理器：
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText("Label", mEntity.superior.wx_account)
            // 将ClipData内容放到系统剪贴板里。
            cm.primaryClip = mClipData
            showQQTipDialog("成功复制客服微信号")
            /*val intent = Intent()
            val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
            intent.action = Intent.ACTION_MAIN
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.component = cmp
            startActivity(intent)*/
        }catch (e: ActivityNotFoundException){
            ToastUtils.showShort(this, "您还没有安装微信，请安装后使用")
        }
    }
}
