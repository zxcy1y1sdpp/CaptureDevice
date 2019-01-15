package app.jietuqi.cn.wechat.create

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.wechat.db.WechatCreateBillsHelper
import app.jietuqi.cn.wechat.entity.WechatCreateBillsEntity
import app.jietuqi.cn.wechat.ui.adapter.WechatCreateBillListAdapter
import kotlinx.android.synthetic.main.activity_wechat_bills_list.*
import kotlinx.android.synthetic.main.include_base_bottom_add_item.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/12/18 09:36.
 * 时间： 2018/12/18 09:36
 * 邮箱： 972383753@qq.com
 * 用途： 创建微信账单页面
 */
class WechatCreateBillsListActivity : BaseWechatActivity() {
    private var mHelper = WechatCreateBillsHelper(this)
    private var mList = arrayListOf<WechatCreateBillsEntity>()
    private var mAdapter: WechatCreateBillListAdapter? = null
    override fun setLayoutResourceId() = R.layout.activity_wechat_bills_list

    override fun needLoadingView() = false

    override fun initAllViews() {
        registerEventBus()
        mHelper.queryAll()?.let { mList.addAll(it) }

    }

    override fun initViewsListener() {
        wechatAddItemBtn.setOnClickListener(this)
        mWechatCreateMenuRecyclerView.setSwipeItemClickListener { _, position ->
            var entity = mList[position]
            LaunchUtil.startWechatCreateBillActivity(this, entity, 1)
        }
        mAdapter = WechatCreateBillListAdapter(mList)
        mWechatCreateMenuRecyclerView.adapter = mAdapter
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.wechatAddItemBtn ->{
                LaunchUtil.startWechatCreateBillActivity(this, null, 0)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNeedChangeOrAddData(entity: WechatCreateBillsEntity) {
        when {
            entity.tag == 0 -> {
                mList.add(entity)
                mAdapter?.notifyItemInserted(entity.position)
            }
            entity.tag == 1 -> {
                mList[entity.position] = entity
                mAdapter?.notifyItemChanged(entity.position)
            }
            entity.tag == 2 -> {
                mList.remove(entity)
                mAdapter?.notifyItemRemoved(entity.position)
            }
        }
    }
}
