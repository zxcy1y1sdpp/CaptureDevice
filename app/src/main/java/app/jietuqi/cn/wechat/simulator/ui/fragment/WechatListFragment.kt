package app.jietuqi.cn.wechat.simulator.ui.fragment

import android.support.v7.widget.RecyclerView
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatFragment
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.constant.GlobalVariable
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.wechat.simulator.WechatSimulatorUnReadEntity
import app.jietuqi.cn.wechat.simulator.adapter.WechatListFragmentAdapter
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorHelper
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateGroupChatActivity
import app.jietuqi.cn.wechat.simulator.widget.WechatSimulatorDialog
import app.jietuqi.cn.wechat.simulator.widget.topright.MenuItem
import app.jietuqi.cn.wechat.simulator.widget.topright.TopRightMenu
import app.jietuqi.cn.wechat.ui.activity.WechatAddTalkObjectActivity
import kotlinx.android.synthetic.main.fragment_wechat_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 作者： liuyuanbo on 2018/10/9 17:41.
 * 时间： 2018/10/9 17:41
 * 邮箱： 972383753@qq.com
 * 用途： 微信聊天列表
 */

class WechatListFragment : BaseWechatFragment(), WechatSimulatorDialog.OperateListener {
    private var mAdapter: WechatListFragmentAdapter? = null
    private var mList: MutableList<WechatUserEntity> = mutableListOf()
    private lateinit var mHelper: WechatSimulatorListHelper
    private lateinit var mTopRightMenu: TopRightMenu
    override fun setLayoutResouceId(): Int {
        return R.layout.fragment_wechat_list
    }

    override fun initAllViews() {
        mHelper = WechatSimulatorListHelper(activity)
        EventBusUtil.register(this)
        mAdapter = WechatListFragmentAdapter(mList)
        chatRecyclerView.adapter = mAdapter
        var list = WechatSimulatorListHelper(activity).queryAll()
        if (null != list){
            GlobalScope.launch { // 在一个公共线程池中创建一个协程
                var entity: WechatUserEntity
                var unReadNumber = 0
                for (i in list.indices) {
                    entity = list[i]
                    unReadNumber += entity.unReadNum.toInt()
                }
                activity?.runOnUiThread{
                    if (unReadNumber <= 0){
                        mWechatSimulatorPreviewNickNameTv.text = "微信"
                    }else{
                        mWechatSimulatorPreviewNickNameTv.text = StringUtils.insertFrontAndBack(unReadNumber, "微信(", ")")
                    }
                }
            }
        }
//        loadWechatBitmap(8)
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun showOnReadNumbers(entity: WechatSimulatorUnReadEntity) {
        if (entity.tag == 0){
            if (entity.unRead <= 0){
                mWechatSimulatorPreviewNickNameTv.text = "微信"
            }else{
                mWechatSimulatorPreviewNickNameTv.text = StringUtils.insertFrontAndBack(entity.unRead, "微信(", ")")
            }
        }
    }
    override fun initViewsListener() {
        chatRecyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(chatRecyclerView) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val position = vh.adapterPosition
                val entity = mList[position]

                if (entity.chatType == 1){
                    GlobalVariable.mGoupHeader = entity.groupHeader
                    entity.groupHeader = null
                    LaunchUtil.startWechatSimulatorPreviewGroupActivity(activity, entity)
                }else{
                    LaunchUtil.startWechatSimulatorPreviewActivity(activity, entity)
                }

                GlobalScope.launch { // 在一个公共线程池中创建一个协程
                    var entity2: WechatUserEntity
                    var unReadNumber = 0
                    for (i in mList.indices) {
                        entity2 = mList[i]
                        unReadNumber += entity2.unReadNum.toInt()
                    }
                    EventBusUtil.post(WechatSimulatorUnReadEntity(0, unReadNumber - entity.unReadNum.toInt()))
                }
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {
                val position = vh.adapterPosition
                val entity = mList[position]
                val dialog = WechatSimulatorDialog()
                dialog.setOperateListener(this@WechatListFragment)
                dialog.setData(entity, mHelper)
                dialog.show(activity?.supportFragmentManager, "chatType")
            }
        })
        mWechatSimulatorPreviewAddIv.setOnClickListener(this)
    }

    override fun loadFromDb() {
        super.loadFromDb()
        var list = mHelper.queryAll()
        if (null != list){
            mList.clear()
            mList.addAll(list)
        }
        mAdapter?.notifyDataSetChanged()
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mWechatSimulatorPreviewAddIv ->{
                mTopRightMenu = TopRightMenu(activity)
                val menuItems = arrayListOf<MenuItem>()
                menuItems.add(MenuItem("添加对话"))
                menuItems.add(MenuItem("清空列表"))
                menuItems.add(MenuItem("添加群聊"))
                mTopRightMenu
                        .setHeight(480)     //默认高度480
                        .dimBackground(true)           //背景变暗，默认为true
                        .needAnimationStyle(true)   //显示动画，默认为true
                        .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                        .addMenuList(menuItems)
                        .setOnMenuItemClickListener { position ->
                            when(position){
                                0 ->{
                                    LaunchUtil.launch(activity, WechatAddTalkObjectActivity::class.java)
                                }
                                1 ->{
                                    mHelper.deleteAll()
                                    mList.clear()
                                    mAdapter?.notifyDataSetChanged()
                                }
                                2 ->{
                                    LaunchUtil.launch(activity, WechatSimulatorCreateGroupChatActivity::class.java)
                                }
                            }
                        }
                        .showAsDropDown(mWechatSimulatorPreviewAddIv, -200, 10)
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(entity: WechatUserEntity) {
        if (0 == mList.size){//没有数据
            mList.add( entity)
            mAdapter?.notifyDataSetChanged()
        }else{
            var position = -1
            mList.forEach {
                if(it.wechatUserId == entity.wechatUserId){
                    position = mList.indexOf(it)
                }
            }
            mHelper.update(entity)
            mList.removeAt(position)
            mList.add(position, entity)
            mAdapter?.notifyItemChanged(position)
        }
    }
    override fun operate(type: String, userEntity: WechatUserEntity) {
        loadFromDb()
        when(type){
            "置顶聊天" ->{ }
            "取消置顶" ->{ }
            "标为未读" ->{
                GlobalScope.launch { // 在一个公共线程池中创建一个协程
                    var entity: WechatUserEntity
                    var unReadNumber = 0
                    for (i in mList.indices) {
                        entity = mList[i]
                        unReadNumber += entity.unReadNum.toInt()
                    }
                    EventBusUtil.post(WechatSimulatorUnReadEntity(0, unReadNumber))
                }
            }
            "标为已读" ->{
                GlobalScope.launch { // 在一个公共线程池中创建一个协程
                    var entity: WechatUserEntity
                    var unReadNumber = 0
                    for (i in mList.indices) {
                        entity = mList[i]
                        unReadNumber += entity.unReadNum.toInt()
                    }
                    EventBusUtil.post(WechatSimulatorUnReadEntity(0, unReadNumber))
                }
            }
            "显示小圆点" ->{ }
            "隐藏小圆点" ->{ }
            "删除" ->{
                WechatSimulatorHelper(activity, userEntity).deleteAll()
            }
        }
        mAdapter?.notifyDataSetChanged()
    }
   /* private val IMG_URL_ARR = arrayOf("http://img.hb.aicdn.com/eca438704a81dd1fa83347cb8ec1a49ec16d2802c846-laesx2_fw658",
            "http://img.hb.aicdn.com/729970b85e6f56b0d029dcc30be04b484e6cf82d18df2-XwtPUZ_fw658",
            "http://img.hb.aicdn.com/85579fa12b182a3abee62bd3fceae0047767857fe6d4-99Wtzp_fw658",
            "http://img.hb.aicdn.com/2814e43d98ed41e8b3393b0ff8f08f98398d1f6e28a9b-xfGDIC_fw658",
            "http://img.hb.aicdn.com/a1f189d4a420ef1927317ebfacc2ae055ff9f212148fb-iEyFWS_fw658",
            "http://img.hb.aicdn.com/69b52afdca0ae780ee44c6f14a371eee68ece4ec8a8ce-4vaO0k_fw658",
            "http://img.hb.aicdn.com/9925b5f679964d769c91ad407e46a4ae9d47be8155e9a-seH7yY_fw658",
            "http://img.hb.aicdn.com/e22ee5730f152c236c69e2242b9d9114852be2bd8629-EKEnFD_fw658",
            "http://img.hb.aicdn.com/73f2fbeb01cd3fcb2b4dccbbb7973aa1a82c420b21079-5yj6fx_fw658")
    private fun loadWechatBitmap(count: Int) {
        CombineBitmap.init(activity)
                .setLayoutManager(WechatLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                .setSize(180) // 必选，组合后Bitmap的尺寸，单位dp
                .setGap(3) // 单个图片之间的距离，单位dp，默认0dp
                .setGapColor(Color.parseColor("#E8E8E8")) // 单个图片间距的颜色，默认白色
                .setPlaceholder(R.drawable.head_default) // 单个图片加载失败的默认显示图片
                .setUrls(*getUrls(count.toString())) // 要加载的图片url数组
//                .setBitmaps() // 要加载的图片bitmap数组
//                .setResourceIds() // 要加载的图片资源id数组
//                .setImageView() // 直接设置要显示图片的ImageView
                // 设置“子图片”的点击事件，需使用setImageView()，index和图片资源数组的索引对应
                .setOnSubItemClickListener { }
                // 加载进度的回调函数，如果不使用setImageView()方法，可在onComplete()完成最终图片的显示
                .setOnProgressListener(object : OnProgressListener {
                    override fun onStart() {}

                    override fun onComplete(bitmap: Bitmap) {}
                })
                .build()
    }

    private fun getUrls(count: String): Array<String> {
        val urls = arrayOf(count)
        System.arraycopy(IMG_URL_ARR, 0, urls, 0, count.toInt())
        return urls
    }*/
}
