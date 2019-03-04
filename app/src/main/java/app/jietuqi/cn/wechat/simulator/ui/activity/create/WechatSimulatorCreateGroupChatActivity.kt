package app.jietuqi.cn.wechat.simulator.ui.activity.create

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.ResourceHelper
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.ColorFinal
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.TimeUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.simulator.adapter.WechatSimulatorCreateGroupAdapter
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.wechat.widget.groupicon.CombineBitmap
import app.jietuqi.cn.wechat.widget.groupicon.layout.WechatLayoutManager
import app.jietuqi.cn.wechat.widget.groupicon.listener.OnProgressListener
import kotlinx.android.synthetic.main.activity_wechat_simulator_create_group_chat.*
import java.io.File
import java.util.*

/**
 * 作者： liuyuanbo on 2019/2/12 11:21.
 * 时间： 2019/2/12 11:21
 * 邮箱： 972383753@qq.com
 * 用途： 创建群聊的页面
 */
class WechatSimulatorCreateGroupChatActivity : BaseWechatActivity() {
    private val mHelper = RoleLibraryHelper(this)
    private val mList: ArrayList<WechatUserEntity> = arrayListOf()
    private var mAdapter: WechatSimulatorCreateGroupAdapter? = null
    private var mNowTime = 0L
    private var mMyEntity: WechatUserEntity = WechatUserEntity()
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_create_group_chat
    override fun needLoadingView() = false

    override fun initAllViews() {
        mMyEntity = UserOperateUtil.getWechatSimulatorMySelf()
        setLightStatusBarForM(this, true)
        setStatusBarColor(ColorFinal.WHITE)
        setTopTitle("选择群成员")
        var list = mHelper.queryWithoutMe()
        mList.addAll(list)
        mAdapter = WechatSimulatorCreateGroupAdapter(mList, mMyEntity)
        mGroupRv.adapter = mAdapter
    }

    override fun initViewsListener() {
        mAddRoleBtn.setOnClickListener(this)
        mOkBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAddRoleBtn ->{
                LaunchUtil.launch(this, WechatSimulatorCreateAddRoleActivity::class.java, RequestCode.ADD_GROUP_ROLE)
            }
            R.id.mOkBtn ->{
                mNowTime = TimeUtil.getCurrentTimeEndMs()
                var entity: WechatUserEntity
                val roleList = arrayListOf<WechatUserEntity>()
                for (i in mList.indices){
                    entity = mList[i]
                    if (entity.isChecked){
                        roleList.add(entity)
                    }
                }
                if (roleList.size < 2){
                    showToast("最少选择两个人")
                    return
                }
                initWechatSimulatorData5()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.ADD_GROUP_ROLE ->{
                if (null != data){
                    val entity= data.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity
                    mList.add(entity)
                    mAdapter?.notifyItemInserted(mList.size - 1)
                }
            }
        }
    }

    private fun initWechatSimulatorData5(){
        val msgEntity = WechatUserEntity()
        msgEntity.groupTableName = "wechatGroup$mNowTime"//时间戳
        msgEntity.chatType = 1
        msgEntity.lastTime = TimeUtil.getCurrentTimeEndMs()

        val roleList = arrayListOf<WechatUserEntity>()
        val roleWithCreateHeader = arrayListOf<WechatUserEntity>()//参与创建群聊的人
        roleWithCreateHeader.add(mMyEntity)
        var entity: WechatUserEntity
//        val groupName = StringBuilder()
        for (i in mList.indices){
            entity = mList[i]
            if (entity.isChecked){
                if (roleWithCreateHeader.size < 9){
                    roleWithCreateHeader.add(entity)
                }
                roleList.add(entity)
            }
        }
        roleList.shuffle()
        roleList[0].isRecentRole = true
        roleWithCreateHeader.shuffle()
        val bitmap = arrayOfNulls<Bitmap>(roleWithCreateHeader.size)
        for (i in roleWithCreateHeader.indices){
            entity = roleWithCreateHeader[i]
            if (!TextUtils.isEmpty(entity.wechatUserAvatar)){//选择的
                val file = File(entity.wechatUserAvatar)
                val uri = Uri.fromFile(file)
                val header = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                bitmap[i] = header
            }else{
                bitmap[i] = BitmapFactory.decodeResource(resources, ResourceHelper.getAppIconId(entity.resourceName))
            }
        }
        msgEntity.groupRoles = roleList
        msgEntity.groupRoleCount = roleList.size + 1
        var listHelper = WechatSimulatorListHelper(this)
        CombineBitmap.init(this)
                .setLayoutManager(WechatLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                .setSize(180) // 必选，组合后Bitmap的尺寸，单位dp
                .setGap(3) // 单个图片之间的距离，单位dp，默认0dp
                .setGapColor(Color.parseColor("#E8E8E8")) // 单个图片间距的颜色，默认白色
                .setPlaceholder(R.drawable.head_default) // 单个图片加载失败的默认显示图片
                .setBitmaps(*bitmap) // 要加载的图片bitmap数组
                // 设置“子图片”的点击事件，需使用setImageView()，index和图片资源数组的索引对应
                .setOnSubItemClickListener { }
                // 加载进度的回调函数，如果不使用setImageView()方法，可在onComplete()完成最终图片的显示
                .setOnProgressListener(object : OnProgressListener {
                    override fun onStart() {}
                    override fun onComplete(bitmap: Bitmap) {
                        msgEntity.groupHeader = bitmap
                        listHelper.save(msgEntity)
                        finish()
                    }
                }).build()
    }
}
