package app.jietuqi.cn.wechat.simulator.ui.activity

import android.content.Intent
import android.view.Gravity
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatSimulatorActivity
import app.jietuqi.cn.constant.*
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.SharedPreferencesUtils
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.wechat.simulator.adapter.WechatNewFriendsAdapter
import app.jietuqi.cn.wechat.simulator.widget.topright.MenuItem
import app.jietuqi.cn.wechat.simulator.widget.topright.TopRightMenu
import app.jietuqi.cn.widget.dialog.customdialog.EnsureDialog
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.activity_wechat_new_friends.*

/**
 * 作者： liuyuanbo on 2019/3/26 10:10.
 * 时间： 2019/3/26 10:10
 * 邮箱： 972383753@qq.com
 * 用途： 微信模拟器 -- 新的朋友
 */
class WechatNewFriendsActivity : BaseWechatSimulatorActivity(), WechatNewFriendsAdapter.EditListener {
    private var mHelper: RoleLibraryHelper? = null
    private var mList = arrayListOf<WechatUserEntity>()
    private lateinit var mAdapter: WechatNewFriendsAdapter
    override fun setLayoutResourceId() = R.layout.activity_wechat_new_friends

    override fun needLoadingView() = false

    override fun initAllViews() {
        mHelper = RoleLibraryHelper(this)
        setStatusBarColor(ColorFinal.NEW_WECHAT_TITLEBAR_DARK)
        setLightStatusBarForM(this, true)
        val list = UserOperateUtil.getWechatNewFriendsList()
        mList.addAll(list)
        mAdapter = WechatNewFriendsAdapter(mList, this)
        mNewFriendsRv.adapter = mAdapter
    }
    override fun initViewsListener() {
        mAddNewFriendsTv.setOnClickListener(this)
        mWechatSimulatorPreviewBackITv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAddNewFriendsTv ->{
                val menuItems = arrayListOf<MenuItem>()
                menuItems.add(MenuItem("添加1个"))
                menuItems.add(MenuItem("添加20个"))
                menuItems.add(MenuItem("全部接受"))
                menuItems.add(MenuItem("清空列表"))
                TopRightMenu(this).dimBackground(true)           //背景变暗，默认为true
                        .needAnimationStyle(true)   //显示动画，默认为true
                        .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                        .addMenuList(menuItems)
                        .setOnMenuItemClickListener { position ->
                            when(position){
                                0 ->{
                                    LaunchUtil.startWechatNewFriendsEditActivity(this, null, 0)
                                }
                                1 ->{
                                    var entity: WechatUserEntity
                                    var msg: String
                                    for (i in 0..19) {
                                        entity = WechatUserEntity(RandomUtil.getRandomNewFriendAvatar(), RandomUtil.getRandomNewFriendNickName())
                                        msg = RandomUtil.getRandomNewFriendMsg()
                                        entity.msg = msg
                                        if ("我是" == msg){
                                            entity.msg = StringUtils.insertFront(entity.wechatUserNickName, msg)
                                        }
                                        mList.add(entity)
                                    }
                                    SharedPreferencesUtils.putListData(SharedPreferenceKey.WECHAT_NEW_FRIENDS_LIST, mList)
                                    mAdapter.notifyDataSetChanged()
                                    EventBusUtil.post("添加多个朋友")
                                }
                                2 ->{
                                    if(mList.size > 0){
                                        var entity: WechatUserEntity
                                        for (i in mList.indices){
                                            entity = mList[i]
                                            entity.isChecked = true
                                        }
                                        mAdapter.notifyDataSetChanged()
                                        SharedPreferencesUtils.putListData(SharedPreferenceKey.WECHAT_NEW_FRIENDS_LIST, mList)
                                        EventBusUtil.post("全部接受")
                                    }
                                }
                                3 ->{
                                    EnsureDialog(this).builder()
                                            .setGravity(Gravity.CENTER)//默认居中，可以不设置
                                            .setTitle("提示！")//可以不设置标题颜色，默认系统颜色
                                            .setSubTitle("确定要清空列表吗？")
                                            .setNegativeButton("取消") {}
                                            .setPositiveButton("清空") {
                                                mList.clear()
                                                mAdapter.notifyDataSetChanged()
                                                SharedPreferencesUtils.remove(SharedPreferenceKey.WECHAT_NEW_FRIENDS_LIST)
                                                EventBusUtil.post("清空列表")
                                            }.show()

                                }
                            }
                        }.showAsDropDown(mAddNewFriendsTv, -100, 20)
            }
            R.id.mWechatSimulatorPreviewBackITv ->{
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RequestCode.CHANGE_ROLE ->{
                if (null != data){
                    val entity: WechatUserEntity = data.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity
                    val position = data.getIntExtra(IntentKey.POSITION, 0)
                    when {
                        "新朋友 -- 删除" == entity.eventBusTag -> {
                            mList.removeAt(position)
                            mAdapter.notifyItemRemoved(position)

                        }
                        "新朋友 -- 修改" == entity.eventBusTag -> {
                            mList.removeAt(position)
                            mList.add(position, entity)
                            mAdapter.notifyDataSetChanged()
                        }
                        "新朋友 -- 添加" == entity.eventBusTag -> {
                            mList.add(0, entity)
                            mAdapter.notifyDataSetChanged()

                        }
                    }
                    SharedPreferencesUtils.putListData(SharedPreferenceKey.WECHAT_NEW_FRIENDS_LIST, mList)
                    EventBusUtil.post("编辑朋友信息")
                }
            }
        }
    }

    override fun edit(entity: WechatUserEntity?, position: Int) {
        entity?.eventBusTag = "新朋友 -- 修改"
        LaunchUtil.startWechatNewFriendsEditActivity(this, entity, position)
    }
    override fun accept(entity: WechatUserEntity?, position: Int) {
        entity?.isChecked = true
        mAdapter.notifyDataSetChanged()
        val id = mHelper?.save(entity)//向数据库里添加新的人
        entity?.id = id
        SharedPreferencesUtils.putListData(SharedPreferenceKey.WECHAT_NEW_FRIENDS_LIST, mList)
        showQQTipDialog("已添加到通讯录")
        EventBusUtil.post("已添加到通讯录")

    }
}
