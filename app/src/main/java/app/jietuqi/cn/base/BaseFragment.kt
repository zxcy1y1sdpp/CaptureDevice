package app.jietuqi.cn.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import app.jietuqi.cn.R
import app.jietuqi.cn.callback.LoadMoreListener
import app.jietuqi.cn.callback.RefreshListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zhouyou.http.widget.ProgressUtils
import kotlinx.android.synthetic.main.include_base_overall_top.*

/**
 * 作者： liuyuanbo on 2018/10/9 17:43.
 * 时间： 2018/10/9 17:43
 * 邮箱： 972383753@qq.com
 * 用途：
 */

abstract class BaseFragment : Fragment(), View.OnClickListener {
    /**
     * 根view
     */
    private var mRootView: View? = null
    var mPageSize = 1
    var mLimitSize = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /**
         * 解决内存泄漏的重要方法，防止onCreateView方法多次执行
         */
        if (null != mRootView) {
            val parent = mRootView?.parent as ViewGroup
            parent.removeView(mRootView)
        } else {
            mRootView = inflater.inflate(setLayoutResouceId(), container, false)
            //            onLazyLoad();
            //            onLoad();
        }
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setContextS()
        //            initLoadingView();
        initAllViews()
        initViewsListener()
        getArguments(arguments)
        loadFromServer()
    }

    protected fun <T : View> findViewById(id: Int): T? {
        if (mRootView == null) {
            return null
        }

        return mRootView?.findViewById<View>(id) as T
    }

    /**
     * 设置根布局资源id
     */
    protected abstract fun setLayoutResouceId(): Int

    /**
     * 设置根布局资源id
     * @return
     */
    private fun setContextS() {}

    /**
     * 初始化控件的方法
     */
    protected abstract fun initAllViews()

    /**
     * 设置控件的事件 －－ 点击事件之类的
     */
    protected abstract fun initViewsListener()

    /**
     * 接收到的从其他地方传递过来的参数
     * @param arguments
     */
    private fun getArguments(arguments: Bundle?) {}

    open fun loadFromDb() {

    }
    open fun loadFromServer() {
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            visiableForUser()
        }else{
            invisiableForUser()
        }
    }
    override fun onResume() {
        super.onResume()
        loadFromDb()

    }

    /**
     * 对用户来说是可见的状态
     */
    open fun visiableForUser() {

    }
    /**
     * 对用户来说是不可见的状态
     */
    open fun invisiableForUser() {

    }
    override fun onClick(v: View) {
    }
    /**
     * 设置页面的标题
     * @param title
     * @param type
     *        0 -- 只有一个返回键和标题
     *        1 -- 只有标题
     *        2 -- 包含右侧标题
     */
    protected fun setTitle(title: String, type: Int = 0, rightTitle: String = "") {
        val titleTv = findViewById<TextView>(R.id.overAllTitleTv)
        val iv = findViewById<ImageView>(R.id.overAllBackIv)
        if (!TextUtils.isEmpty(rightTitle)){
            val rightTitleTv = findViewById<TextView>(R.id.overAllRightTitleTv)
            rightTitleTv?.setOnClickListener(this)
            rightTitleTv?.visibility = View.VISIBLE
        }
        when(type){
            1 ->{
                overAllBackTv.visibility = View.GONE
                iv?.visibility = View.GONE
            }
        }
        iv?.setOnClickListener(this)
        titleTv?.setOnClickListener(this)
        titleTv?.text = title
    }

    /**
     * 可取消的加载狂
     * @param message : 展示的消息
     * @param canCancel：是否可以取消
     */
    fun showLoadingDialog(message: String = "请稍后...", canCancel: Boolean = true) {
        ProgressUtils.showProgressDialog(message, activity, canCancel)
    }

    fun dismissLoadingDialog() {
        if (ProgressUtils.mProgressDialog != null){
            ProgressUtils.cancleProgressDialog()
        }
    }
    fun showToast(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    fun setRefreshLayout(refreshLayout: RefreshLayout, refreshListener: RefreshListener? = null, loadMoreListener: LoadMoreListener? = null){

        refreshLayout.setOnRefreshListener{
            mPageSize = 1
            loadFromServer()
            refreshListener?.refresh()
        }

        refreshLayout.setOnLoadMoreListener {
            mPageSize += 1
            loadFromServer()
            if (null != loadMoreListener){
                loadMoreListener.loadMore()
            }
        }
    }
}
