package app.jietuqi.cn.wechat.simulator.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatFragment
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.OtherUtil
import app.jietuqi.cn.util.TimeUtil
import app.jietuqi.cn.wechat.simulator.adapter.WechatSimulatorContactAdapter
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import kotlinx.android.synthetic.main.fragment_wechat_friends.*
import java.util.*

/**
 * 作者： liuyuanbo on 2018/10/9 17:42.
 * 时间： 2018/10/9 17:42
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 通讯
 */

class WechatFriendsFragment : BaseWechatFragment() {
    override fun needLoading(): Boolean = false

    private lateinit var mHelper: RoleLibraryHelper
    private val mList: ArrayList<WechatUserEntity> = arrayListOf()
    private lateinit var mAdapter: WechatSimulatorContactAdapter
    override fun setLayoutResouceId() = R.layout.fragment_wechat_friends

    override fun initAllViews() {
        mAdapter = WechatSimulatorContactAdapter(mList)
        mWechatFriendsRecyclerView.adapter = mAdapter
        mWechatFriendsLetter.setTextView(mWechatFriendsLetterTv)
        mHelper = RoleLibraryHelper(context)
        var list = mHelper.query()
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
}
