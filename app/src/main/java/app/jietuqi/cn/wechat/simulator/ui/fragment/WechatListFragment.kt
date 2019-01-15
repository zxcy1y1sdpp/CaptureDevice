package app.jietuqi.cn.wechat.simulator.ui.fragment

import android.support.v7.widget.RecyclerView
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatFragment
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.wechat.simulator.adapter.WechatListFragmentAdapter
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.wechat.simulator.widget.WechatSimulatorDialog
import app.jietuqi.cn.wechat.ui.activity.WechatAddTalkObjectActivity
import kotlinx.android.synthetic.main.fragment_wechat_list.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/10/9 17:41.
 * 时间： 2018/10/9 17:41
 * 邮箱： 972383753@qq.com
 * 用途： 微信聊天列表
 */

class WechatListFragment : BaseWechatFragment() {
    private var mAdapter: WechatListFragmentAdapter? = null
    private var mList: MutableList<WechatUserEntity> = mutableListOf()
    private lateinit var mHelper: WechatSimulatorListHelper
    override fun setLayoutResouceId(): Int {
        return R.layout.fragment_wechat_list
    }

    override fun initAllViews() {
        mHelper = WechatSimulatorListHelper(activity)
        EventBusUtil.register(this)
        mAdapter = WechatListFragmentAdapter(mList)
        chatRecyclerView.adapter = mAdapter
    }

    override fun initViewsListener() {
        chatRecyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(chatRecyclerView) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val position = vh.adapterPosition
                val entity = mList[position]
                LaunchUtil.startWechatSimulatorPreviewActivity(activity, entity)
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {
                val dialog = WechatSimulatorDialog()
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
                LaunchUtil.launch(activity, WechatAddTalkObjectActivity::class.java)
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(entity: WechatUserEntity) {
        mHelper.update(entity)
        val position = mList.indexOf(entity)
        mAdapter?.notifyItemChanged(position)
    }
}
