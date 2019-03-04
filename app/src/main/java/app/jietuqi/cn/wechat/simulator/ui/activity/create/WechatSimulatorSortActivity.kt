package app.jietuqi.cn.wechat.simulator.ui.activity.create

import android.content.Intent
import android.support.v7.widget.RecyclerView
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.wechat.simulator.adapter.WechatSimulatorSortAdapter
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorHelper
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener
import com.zhouyou.http.EventBusUtil
import kotlinx.android.synthetic.main.activity_wechat_simulator_sort.*
import java.util.*

/**
 * 作者： liuyuanbo on 2019/3/1 10:56.
 * 时间： 2019/3/1 10:56
 * 邮箱： 972383753@qq.com
 * 用途： 对话排序
 */
class WechatSimulatorSortActivity : BaseWechatActivity() {
    private var mList = arrayListOf<WechatScreenShotEntity>()
    private lateinit var mAdapter: WechatSimulatorSortAdapter
    private var mOtherEntity: WechatUserEntity? = null
    private var mHelper: WechatSimulatorHelper? = null
    override fun setLayoutResourceId() = R.layout.activity_wechat_simulator_sort

    override fun needLoadingView() = false

    override fun initAllViews() {
        mSortRv.isLongPressDragEnabled = true // 拖拽排序，默认关闭。
    }

    override fun initViewsListener() {
        mSortRv.setOnItemMoveListener(object : OnItemMoveListener {
            override fun onItemMove(srcHolder: RecyclerView.ViewHolder, targetHolder: RecyclerView.ViewHolder): Boolean {
                if (srcHolder.itemViewType != targetHolder.itemViewType) return false

                // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
                val fromPosition = srcHolder.adapterPosition - mSortRv.headerItemCount
                val toPosition = targetHolder.adapterPosition - mSortRv.headerItemCount

                Collections.swap(mList, fromPosition, toPosition)
                mAdapter.notifyItemMoved(fromPosition, toPosition)
                return true
            }

            override fun onItemDismiss(srcHolder: RecyclerView.ViewHolder) {}
        })
    }

    override fun finish() {
        showQQWaitDialog("请稍后")
        mHelper?.deleteAll()
        mHelper?.saveAll(mList)
        dismissQQDialog()
        EventBusUtil.post("排序")
        super.finish()
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val type = intent.getIntExtra(IntentKey.TYPE, 0)
        if (type == 0){
            setTopTitle("单聊对话排序")
            mOtherEntity = intent.getSerializableExtra(IntentKey.ENTITY) as WechatUserEntity?
            mHelper = WechatSimulatorHelper(this, mOtherEntity)
        }else{
            setTopTitle("群聊对话排序")
            mHelper = WechatSimulatorHelper(this, intent.getStringExtra(IntentKey.GROUP_TABLE_NAME))
        }
        val list: ArrayList<WechatScreenShotEntity> = intent.getSerializableExtra(IntentKey.LIST) as ArrayList<WechatScreenShotEntity>
        mList.addAll(list)
        mAdapter = WechatSimulatorSortAdapter(mList, type)
        mSortRv.adapter = mAdapter
    }
}
