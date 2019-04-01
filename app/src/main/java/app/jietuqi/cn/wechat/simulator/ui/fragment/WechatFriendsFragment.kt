package app.jietuqi.cn.wechat.simulator.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatFragment
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.*
import app.jietuqi.cn.wechat.simulator.adapter.WechatSimulatorContactAdapter
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatChatListActivity
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatNewFriendsActivity
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.fragment_wechat_friends.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

/**
 * 作者： liuyuanbo on 2018/10/9 17:42.
 * 时间： 2018/10/9 17:42
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 通讯
 */

class WechatFriendsFragment : BaseWechatFragment(), WechatSimulatorContactAdapter.AlreadyShowListener {
    override fun alreadyShow() {
        var entity: WechatUserEntity
        val list = UserOperateUtil.getWechatNewFriendsList()
        if (null != list && list.size > 0){
            for (i in list.indices){
                entity = list[i]
                entity.alreadyShow = true
            }
            SharedPreferencesUtils.putListData(SharedPreferenceKey.WECHAT_NEW_FRIENDS_LIST, list)
        }
        SharedPreferencesUtils.putData(SharedPreferenceKey.SHOW_TOP_RP, false)
        LaunchUtil.launch(activity, WechatNewFriendsActivity::class.java)
        (activity as WechatChatListActivity).noNewFriendsCount(0)
        setData()
    }

    override fun needLoading(): Boolean = false

    private lateinit var mHelper: RoleLibraryHelper
    private val mList: ArrayList<WechatUserEntity> = arrayListOf()
    private lateinit var mAdapter: WechatSimulatorContactAdapter
    override fun setLayoutResouceId() = R.layout.fragment_wechat_friends

    override fun initAllViews() {
        EventBusUtil.register(this)
        mAdapter = WechatSimulatorContactAdapter(mList, this)
        val listAll = UserOperateUtil.getWechatNewFriendsList()
        val listUnAdd = arrayListOf<WechatUserEntity>()
        var entity: WechatUserEntity
        for (i in listAll.indices){
            entity = listAll[i]
            if (!entity.isChecked){
                listUnAdd.add(entity)
            }
        }
        mAdapter.setUnAddFriends(listUnAdd)
        mWechatFriendsRecyclerView.adapter = mAdapter
        mWechatFriendsLetter.setTextView(mWechatFriendsLetterTv)
        mHelper = RoleLibraryHelper(context)
        setData()
    }

    private fun comparePinyin(){
        val compare = OtherUtil.PinyinComparator()
        return Collections.sort(mList, compare)
    }
    override fun initViewsListener() {
        mWechatFriendsRecyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(mWechatFriendsRecyclerView) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                if (vh.adapterPosition == 0){
                    return
                }
                val position = vh.adapterPosition - 1
                var entity = mList[position]
                var entityInList = WechatSimulatorListHelper(activity).query(entity.wechatUserId)
                if (null == entityInList){//没有该数据，说明没有保存到聊天列表中
                    entity.lastTime = TimeUtil.getCurrentTimeEndMs()
                }else{
                    entity = entityInList
                }
                LaunchUtil.startWechatSimulatorPreviewActivity(activity, entity)
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
        mWechatFriendsLetter.setOnTouchingLetterChangedListener { s ->
            if (s == "↑" || s == "☆"){
                mWechatFriendsRecyclerView.scrollToPosition(0)
            }else{
                val position = getPositionForSection(s[0].toInt())
                if (position != -1) {
                    mWechatFriendsRecyclerView.scrollToPosition(position)
                    //指定第一个可见位置的item
                    var mLayoutManager = mWechatFriendsRecyclerView.layoutManager as LinearLayoutManager
                    mLayoutManager.scrollToPositionWithOffset(position, 0)
                }
            }
        }
    }
    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    private fun getSectionForPosition(position: Int): Int {
        return mList[position].pinyinNickName[0].toInt()
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    private fun getPositionForSection(section: Int): Int {
        var entity: WechatUserEntity
        for (i in 0 until mList.size) {
            entity = mList[i]
            val sortStr = entity.pinyinNickName
            val firstChar = sortStr.toUpperCase()[0]
            if (firstChar.toInt() == section) {
                return i
            }
        }
        return -1
    }

    fun setData(){
        var list = mHelper.query()
        mList.clear()
        mList.addAll(list)
        comparePinyin()
        var entity : WechatUserEntity
        for (i in 0 until mList.size) {
            entity = mList[i]
            val section = getSectionForPosition(i)
            entity.isFirst = i == getPositionForSection(section)
            if (entity.isFirst){
                if (i - 1 < 0){
                    mList[i].isLast = true
                }else{
                    mList[i - 1].isLast = true
                }
            }
        }
        mList[mList.size - 1].isLast = true
        mAdapter.notifyDataSetChanged()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onChoiceGroupHeader(msg: String) {
        if ("已添加到通讯录" == msg || "从通讯录中移除" == msg || "清空列表" == msg || "全部接受" == msg || "添加多个朋友" == msg || "编辑朋友信息" == msg){
            SharedPreferencesUtils.putData(SharedPreferenceKey.SHOW_TOP_RP, true)
            SharedPreferencesUtils.putData(SharedPreferenceKey.SHOW_BOTTOM_RP, true)
            setData()
            val listAll = UserOperateUtil.getWechatNewFriendsList()
            val listUnAdd = arrayListOf<WechatUserEntity>()
            var entity: WechatUserEntity
            var unAddCount = 0
            for (i in listAll.indices){
                entity = listAll[i]
                if (!entity.isChecked){
                    listUnAdd.add(entity)
                }
            }
            for (i in listUnAdd.indices){
                entity = listUnAdd[i]
                if (!entity.alreadyShow){
                    unAddCount++
                }
            }
            mAdapter.setUnAddFriends(listUnAdd)
            (activity as WechatChatListActivity).noNewFriendsCount(unAddCount)
        }else if ("聊天页面改变用户信息" == msg){
            setData()
        }
    }
}
