package dasheng.com.capturedevice.wechat.ui.activity

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bm.zlzq.utils.ScreenUtil
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.util.LaunchUtil
import dasheng.com.capturedevice.wechat.db.WechatChargeHelper
import dasheng.com.capturedevice.wechat.entity.WechatChargeDetailEntity
import dasheng.com.capturedevice.wechat.ui.adapter.WechatChargeDetailAdapter
import kotlinx.android.synthetic.main.activity_wechat_charge_detail.*
import kotlinx.android.synthetic.main.include_base_bottom_add_item.*
import java.util.ArrayList
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener
import dasheng.com.capturedevice.base.wechat.BaseWechatWithDbActivity
import dasheng.com.capturedevice.callback.OnRecyclerItemClickListener


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
     * 菜单创建器。
     */
    private val mSwipeMenuCreator = SwipeMenuCreator { swipeLeftMenu, swipeRightMenu, viewType ->
        val width = ScreenUtil.getScreenWidth(this) / 6

        // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        val deleteItem = SwipeMenuItem(this)
//                .setBackgroundDrawable(R.drawable.selector_red)
//                .setImage(R.mipmap.ic_action_delete)
                .setBackgroundColor(ContextCompat.getColor(this, R.color.inviteRed))
                .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                .setTextColor(Color.WHITE)
                .setWidth(width)
                .setHeight(height)
        swipeRightMenu.addMenuItem(deleteItem)// 添加一个按钮到右侧侧菜单。
    }
    private val mMenuItemClickListener: SwipeMenuItemClickListener = SwipeMenuItemClickListener { menuBridge ->
        // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
        menuBridge.closeMenu()// 关闭被点击的菜单。
        val direction = menuBridge.direction // 左侧还是右侧菜单。
        val adapterPosition = menuBridge.adapterPosition // RecyclerView的Item的position。
        if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION){
            mList.removeAt(adapterPosition)
            mAdapter?.notifyItemRemoved(adapterPosition)
        }
//        val menuPosition = menuBridge.position // 菜单在RecyclerView的Item中的Position。
    }

    override fun setLayoutResourceId() = R.layout.activity_wechat_charge_detail

    override fun needLoadingView() = false

    override fun initAllViews() {
        setWechatViewTitle("零钱明细", 0)
        mAdapter = WechatChargeDetailAdapter(mList)
    }

    override fun initViewsListener() {
        wechatAddItemBtn.setOnClickListener(this)
        wechatPreviewBtn.setOnClickListener(this)
        mMenuRecyclerView.isItemViewSwipeEnabled = true // 策划删除，默认关闭。
        // 设置菜单创建器。
        mMenuRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator)
        mMenuRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener)
        mMenuRecyclerView.adapter = mAdapter

        mMenuRecyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(mMenuRecyclerView) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val position = vh.adapterPosition
                LaunchUtil.startWechatAddChargeDetailActivity(this@WechatChargeDetailActivity, mList[position])
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.wechatAddItemBtn ->{
                LaunchUtil.startWechatAddChargeDetailActivity(this, null)
            }
            R.id.wechatPreviewBtn ->{}
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
