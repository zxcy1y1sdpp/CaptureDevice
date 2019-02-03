package app.jietuqi.cn.wechat.simulator.ui.fragment

import android.support.v7.widget.RecyclerView
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatFragment
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.wechat.simulator.WechatSimulatorUnReadEntity
import app.jietuqi.cn.wechat.simulator.adapter.WechatListFragmentAdapter
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorHelper
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
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
                LaunchUtil.startWechatSimulatorPreviewActivity(activity, entity)
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
                menuItems.add(MenuItem(R.mipmap.icon0, "添加对话"))
                menuItems.add(MenuItem(R.mipmap.icon1, "清空列表"))
                mTopRightMenu
                        .setHeight(360)     //默认高度480
                        .showIcon(false)     //显示菜单图标，默认为true
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
                WechatSimulatorHelper(activity, userEntity.wechatUserId).deleteAll()
            }
        }
        mAdapter?.notifyDataSetChanged()
    }
}
