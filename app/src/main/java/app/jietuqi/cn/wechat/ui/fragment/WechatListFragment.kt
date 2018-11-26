package app.jietuqi.cn.wechat.ui.fragment

import android.support.v7.widget.RecyclerView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatFragment
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.database.DatabaseUtils
import app.jietuqi.cn.database.table.WechatUserTable
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.wechat.ui.adapter.WechatListFragmentAdapter
import kotlinx.android.synthetic.main.fragment_wechat_list.*

/**
 * 作者： liuyuanbo on 2018/10/9 17:41.
 * 时间： 2018/10/9 17:41
 * 邮箱： 972383753@qq.com
 * 用途： 微信聊天列表
 */

class WechatListFragment : BaseWechatFragment() {
    private var mAdapter: WechatListFragmentAdapter? = null
    private var mList: MutableList<WechatUserTable> = mutableListOf()
    override fun setLayoutResouceId(): Int {
        return R.layout.fragment_wechat_list
    }

    override fun initAllViews() {
        mAdapter = WechatListFragmentAdapter(mList)
        chatRecyclerView.adapter = mAdapter
    }

    override fun initViewsListener() {
        chatRecyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(chatRecyclerView) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val position = vh.adapterPosition
                val entity = mList[position]
                LaunchUtil.startWechatSingleChatActivity(activity, entity)
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {

            }
        })
    }

    override fun loadFromDb() {
        super.loadFromDb()
        var list = DatabaseUtils.getHelper().queryWechtLastMsgList(activity)
        if (null != list){
            mList.clear()
            mList.addAll(list)
        }
        mAdapter?.notifyDataSetChanged()
    }

}
