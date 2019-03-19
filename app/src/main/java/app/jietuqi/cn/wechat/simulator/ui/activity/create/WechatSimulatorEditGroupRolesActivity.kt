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
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.simulator.adapter.WechatSimulatorCreateGroupAdapter
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.wechat.widget.groupicon.CombineBitmap
import app.jietuqi.cn.wechat.widget.groupicon.layout.WechatLayoutManager
import app.jietuqi.cn.wechat.widget.groupicon.listener.OnProgressListener
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.activity_wechat_simulator_create_group_chat.*
import java.io.File

/**
 * 作者： liuyuanbo on 2019/2/12 11:21.
 * 时间： 2019/2/12 11:21
 * 邮箱： 972383753@qq.com
 * 用途： 创建群聊的页面
 */
class WechatSimulatorEditGroupRolesActivity : BaseWechatActivity() {
    private val mHelper = RoleLibraryHelper(this)
    private val mList: ArrayList<WechatUserEntity> = arrayListOf()
    private var mAdapter: WechatSimulatorCreateGroupAdapter? = null
    private var mMyEntity: WechatUserEntity = WechatUserEntity()
    /**
     * 0 -- 增加群成员
     * 1 -- 减少群成员
     */
    private var mType = 0
    private lateinit var mEntity: WechatUserEntity
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_create_group_chat
    override fun needLoadingView() = false

    override fun initAllViews() {
        mMyEntity = UserOperateUtil.getWechatSimulatorMySelf()
        setLightStatusBarForM(this, true)
        setStatusBarColor(ColorFinal.WHITE)
    }

    override fun initViewsListener() {
        mAddRoleBtn.setOnClickListener(this)
        mOkBtn.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val allRoleList = mHelper.queryWithoutMe()
        mEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity
        mType = intent.getIntExtra(IntentKey.TYPE, 0)
        if (mType == 0){
            setTopTitle("添加群成员")
            mList.addAll(removeDuplicate(allRoleList, mEntity.groupRoles))
        }else{
            setTopTitle("删除群成员")
            mList.addAll(mEntity.groupRoles)
            for (i in mList.indices){
                mList[i].isChecked = false
            }
        }
        mAdapter = WechatSimulatorCreateGroupAdapter(mList, mMyEntity)
        mGroupRv.adapter = mAdapter
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAddRoleBtn ->{
                LaunchUtil.launch(this, WechatSimulatorCreateAddRoleActivity::class.java, RequestCode.ADD_GROUP_ROLE)
            }
            R.id.mOkBtn ->{
                var entity: WechatUserEntity
                val roleList = arrayListOf<WechatUserEntity>()
                for (i in mList.indices){
                    entity = mList[i]
                    if (entity.isChecked){
                        roleList.add(entity)
                    }
                }
                createMsg()
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

    private fun createMsg(){
        val roleList = arrayListOf<WechatUserEntity>()
        roleList.addAll(mEntity.groupRoles)
        var entity: WechatUserEntity
        for (i in mList.indices){
            entity = mList[i]
            if (entity.isChecked){
                if (mType == 0){
                    roleList.add(entity)
                }else{
                    roleList.remove(entity)
                }
            }
        }
        if(mType == 1){
            var reduceEntity: WechatUserEntity
            var hasRecent = false//是否最近发言人被删掉了
            for (j in roleList.indices){
                reduceEntity = roleList[j]
                if (reduceEntity.isRecentRole){
                    hasRecent = true
                    break
                }
            }
            if (!hasRecent){//如果被删掉了
                roleList[0].isRecentRole = true
            }
        }
        mEntity.eventBusTag = "编辑"
        mEntity.groupRoles = roleList
        mEntity.groupRoleCount = roleList.size
        var listHelper = WechatSimulatorListHelper(this)
        if (mEntity.groupRoles.size < 9){
            val bitmap = arrayOfNulls<Bitmap>(roleList.size)
            for (i in roleList.indices){
                if (i < 9){
                    entity = roleList[i]
                    if (!TextUtils.isEmpty(entity.wechatUserAvatar)){//选择的
                        val file = File(entity.wechatUserAvatar)
                        val uri = Uri.fromFile(file)
                        val header = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        bitmap[i] = header
                    }else{
                        bitmap[i] = BitmapFactory.decodeResource(resources, ResourceHelper.getAppIconId(entity.resourceName))
                    }
                }
            }
            CombineBitmap.init(this)
                    .setLayoutManager(WechatLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                    .setSize(180) // 必选，组合后Bitmap的尺寸，单位dp
                    .setGap(9) // 单个图片之间的距离，单位dp，默认0dp
                    .setGapColor(Color.parseColor("#DDDEE0")) // 单个图片间距的颜色，默认白色
                    .setPlaceholder(R.drawable.head_default) // 单个图片加载失败的默认显示图片
                    .setBitmaps(*bitmap) // 要加载的图片bitmap数组
                    // 设置“子图片”的点击事件，需使用setImageView()，index和图片资源数组的索引对应
                    .setOnSubItemClickListener { }
                    // 加载进度的回调函数，如果不使用setImageView()方法，可在onComplete()完成最终图片的显示
                    .setOnProgressListener(object : OnProgressListener {
                        override fun onStart() {}
                        override fun onComplete(bitmap: Bitmap) {
                            EventBusUtil.post(mEntity)
                            mEntity.groupHeader = bitmap
                            listHelper.update(mEntity)
                            finish()
                        }
                    }).build()
        }else{
            EventBusUtil.post(mEntity)
            listHelper.update(mEntity)
            finish()
        }
    }
    private fun removeDuplicate(oldList: ArrayList<WechatUserEntity>, newList: ArrayList<WechatUserEntity>): ArrayList<WechatUserEntity> {
        var entity: WechatUserEntity
        for (i in newList.indices){
            entity = newList[i]
            if (oldList.contains(entity)){
                oldList.remove(entity)
            }
        }
        return oldList
    }
}