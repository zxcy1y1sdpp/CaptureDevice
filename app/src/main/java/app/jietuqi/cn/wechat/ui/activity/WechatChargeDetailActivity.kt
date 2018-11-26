package app.jietuqi.cn.wechat.ui.activity

import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatWithDbActivity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.wechat.db.WechatChargeHelper
import app.jietuqi.cn.wechat.entity.WechatChargeDetailEntity
import app.jietuqi.cn.wechat.ui.adapter.WechatChargeDetailAdapter
import com.yanzhenjie.recyclerview.swipe.*
import kotlinx.android.synthetic.main.activity_wechat_charge_detail.*
import kotlinx.android.synthetic.main.include_base_bottom_add_item.*
import java.util.*


/**
 * 作者： liuyuanbo on 2018/10/30 16:22.
 * 时间： 2018/10/30 16:22
 * 邮箱： 972383753@qq.com
 * 用途： 准备添加微信零钱明细的页面
 */

class WechatChargeDetailActivity : BaseWechatWithDbActivity() {
    val mHelper: WechatChargeHelper = WechatChargeHelper(this)
    private var mAdapter: WechatChargeDetailAdapter? = null
    private var mList: ArrayList<WechatChargeDetailEntity> = arrayListOf()
    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private val swipeMenuCreator = object : SwipeMenuCreator {
        override fun onCreateMenu(swipeLeftMenu: SwipeMenu, swipeRightMenu: SwipeMenu, viewType: Int) {
            val width = resources.getDimensionPixelSize(R.dimen.wechatMoneyTextSize)

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            val height = ViewGroup.LayoutParams.MATCH_PARENT

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            run {
                val closeItem = SwipeMenuItem(this@WechatChargeDetailActivity)
                        .setBackground(R.color.red_btn_bg_color)
                        .setText("删除")
                        .setTextColor(ContextCompat.getColor(this@WechatChargeDetailActivity, R.color.white))
//                        .setTextSize(R.dimen.wechatNormalTextSize)
                        .setWidth(width)
                        .setHeight(height)
                swipeRightMenu.addMenuItem(closeItem) // 添加菜单到左侧。
            }
        }
    }
    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private val mMenuItemClickListener = SwipeMenuItemClickListener { menuBridge ->
        menuBridge.closeMenu()

        val direction = menuBridge.direction // 左侧还是右侧菜单。
        val adapterPosition = menuBridge.adapterPosition // RecyclerView的Item的position。
//        val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。

        if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
            mHelper.delete(mList[adapterPosition])
            mList.removeAt(adapterPosition)
            mAdapter?.notifyItemRemoved(adapterPosition)
        }
    }

    override fun setLayoutResourceId() = R.layout.activity_wechat_charge_detail

    override fun needLoadingView() = false

    override fun initAllViews() {
        setWechatViewTitle("零钱明细", 0)
        mAdapter = WechatChargeDetailAdapter(mList)
    }

    override fun initViewsListener() {
        wechatAddItemBtn.setOnClickListener(this)
        previewBtn.setOnClickListener(this)
        // 设置菜单创建器。
        mMenuRecyclerView.setSwipeMenuCreator(swipeMenuCreator)
        mMenuRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener)
        mMenuRecyclerView.setSwipeItemClickListener { itemView, position ->
            LaunchUtil.startWechatAddChargeDetailActivity(this@WechatChargeDetailActivity, mList[position])
        }
        mMenuRecyclerView.adapter = mAdapter
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.wechatAddItemBtn ->{
                LaunchUtil.startWechatAddChargeDetailActivity(this, null)
            }
            R.id.previewBtn ->{
                LaunchUtil.launch(this, WechatPreviewChargeDetailActivity::class.java)
            }
        }
    }

    override fun loadFromDb() {
        super.loadFromDb()
        val list = mHelper.query()
        if (null != list && list.isNotEmpty()){
            if (mList.size != 0){
                mList.clear()
            }
            mList.addAll(list)
        }
        mAdapter?.notifyDataSetChanged()
    }
}
