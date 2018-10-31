package dasheng.com.capturedevice.wechat.ui.fragment

import android.content.Intent
import android.support.v7.widget.RecyclerView
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseWechatFragment
import dasheng.com.capturedevice.callback.OnRecyclerItemClickListener
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.database.DatabaseUtils
import dasheng.com.capturedevice.database.table.WechatUserTable
import dasheng.com.capturedevice.util.LaunchUtil
import dasheng.com.capturedevice.wechat.ui.adapter.WechatListFragmentAdapter
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
