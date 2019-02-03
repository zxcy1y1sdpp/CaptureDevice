package app.jietuqi.cn.ui.activity

import android.content.*
import android.graphics.Color
import android.net.Uri
import android.widget.Toast
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.OverallProjectShowAdapter
import app.jietuqi.cn.ui.entity.ProjectMarketEntity
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.sweetalert.SweetAlertDialog
import com.xinlan.imageeditlibrary.ToastUtils
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_project_show.*



/**
 * 作者： liuyuanbo on 2019/1/23 21:57.
 * 时间： 2019/1/23 21:57
 * 邮箱： 972383753@qq.com
 * 用途： 产品展示
 */
class OverallProjectShowActivity : BaseOverallInternetActivity(), OverallProjectShowAdapter.OperatetListener {
    override fun operate(entity: ProjectMarketEntity, type: String) {
        when(type){
            "手机" ->{
                SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setCanTouchOutSideCancle(false)
                        .canCancle(false)
                        .setTitleText(entity.phone)
                        .setContentText("涉及金钱交易请务必谨慎，本平台概不负责")
                        .setContentColor(Color.RED)
                        .setConfirmText("拨打")
                        .setCancelText("取消")
                        .setConfirmClickListener { sweetAlertDialog ->
                            val intent = Intent(Intent.ACTION_DIAL)
                            val data = Uri.parse("tel:"+entity.phone)
                            intent.data = data
                            this@OverallProjectShowActivity.startActivity(intent)
                            sweetAlertDialog.dismissWithAnimation()
                        }.setCancelClickListener {
                            it.dismissWithAnimation()
                        }.show()
            }
            "微信" ->{
                try {
                    //获取剪贴板管理器：
                    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    // 创建普通字符型ClipData
                    val mClipData = ClipData.newPlainText("Label", entity.wx)
                    // 将ClipData内容放到系统剪贴板里。
                    cm.primaryClip = mClipData
                    SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setCanTouchOutSideCancle(false)
                            .canCancle(false)
                            .setTitleText("微信号已复制，是否前往微信添加朋友")
                            .setContentText("涉及金钱交易请务必谨慎，本平台概不负责")
                            .setContentColor(Color.RED)
                            .setConfirmText("前往")
                            .setCancelText("取消")
                            .setConfirmClickListener { sweetAlertDialog ->
                                val intent = Intent()
                                val cmp = ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI")
                                intent.action = Intent.ACTION_MAIN
                                intent.addCategory(Intent.CATEGORY_LAUNCHER)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intent.component = cmp
                                startActivity(intent)
                                sweetAlertDialog.dismissWithAnimation()
                            }.setCancelClickListener {
                                it.dismissWithAnimation()
                            }.show()
                }catch (e: ActivityNotFoundException){
                    com.xinlan.imageeditlibrary.ToastUtils.showShort(this, "您还没有安装微信，请安装后使用")
                }
            }
            "qq" ->{
                try {
                    SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setCanTouchOutSideCancle(false)
                            .canCancle(false)
                            .setTitleText("是否前往QQ进行沟通")
                            .setContentText("涉及金钱交易请务必谨慎，本平台概不负责")
                            .setContentColor(Color.RED)
                            .setConfirmText("前往")
                            .setCancelText("取消")
                            .setConfirmClickListener { sweetAlertDialog ->
                                try {
                                    val url = "mqqwpa://im/chat?chat_type=wpa&uin=" + entity.qq
                                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                                } catch (e: Exception) {
                                    Toast.makeText(this, "尚未安装QQ", Toast.LENGTH_SHORT).show()
                                }
                                sweetAlertDialog.dismissWithAnimation()
                            }.setCancelClickListener {
                                it.dismissWithAnimation()
                            }.show()


                } catch (e: Exception) {
                    e.printStackTrace()
                    ToastUtils.showShort(this, "请检查是否安装QQ客户端")
                }
            }
        }
    }

    override fun collect(entity: ProjectMarketEntity) {
        likeAndUnLike(entity)
    }

    private lateinit var mEntity: ProjectMarketEntity
    private lateinit var mAdapter: OverallProjectShowAdapter
    override fun setLayoutResourceId() = R.layout.activity_overall_project_show
    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("产品展示")
    }

    override fun initViewsListener() {}

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mEntity = intent.getSerializableExtra(IntentKey.ENTITY) as ProjectMarketEntity
        mEntity.view += 1//增加一个浏览量
        mAdapter = OverallProjectShowAdapter(this)
        mAdapter.setData(mEntity)
        mOverallProjectShowRv.adapter = mAdapter
    }
    /**
     * 点赞/取消点赞
     */
    private fun likeAndUnLike(entity: ProjectMarketEntity){
        EasyHttp.post(HttpConfig.STORE)
                .params("way", "favour")
                .params("uid", UserOperateUtil.getUserId())
                .params("info_id", entity.id.toString())
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException) {
                        e.message?.let { showToast(it) }
                    }

                    override fun onSuccess(t: String) {
                        if (entity.is_favour == 0){
                            entity.is_favour = 1
                        }else{
                            entity.is_favour = 0
                        }
                        mAdapter?.notifyItemChanged(0)
                    }
                })
    }
}
