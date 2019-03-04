package app.jietuqi.cn.base

import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.jietuqi.cn.callback.LoadMoreListener
import app.jietuqi.cn.callback.RefreshListener
import app.jietuqi.cn.constant.ColorFinal
import com.jaeger.library.StatusBarUtil
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.xinlan.imageeditlibrary.ToastUtils
import com.zhouyou.http.EventBusUtil
import com.zhouyou.http.widget.ProgressUtils
import kotlinx.android.synthetic.main.include_loading.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.annotations.NotNull

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
    var mLimitSize = 30
    var mQQDialog: QMUITipDialog? = null
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
        initLoadingView()
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

    protected abstract fun needLoading(): Boolean


    private fun initLoadingView(){
        if (needLoading()){
            EventBusUtil.register(this)
            mMultipleStatusView.showLoading()
            mMultipleStatusView.setOnRetryClickListener(mRetryClickListener)
        }
    }

    private val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        loadFromServer()
    }

    protected fun registerEventBus(){
        EventBusUtil.register(this)
    }
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
    protected open fun getArguments(arguments: Bundle?) {}
    open fun loadFromDb() {}

    open fun loadFromServer() {}

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            visiableForUser()
        } else {
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
    open fun visiableForUser() {}

    /**
     * 对用户来说是不可见的状态
     */
    open fun invisiableForUser() {}

    override fun onClick(v: View) {}

    /**
     * 设置状态栏的颜色
     */
    fun setStatusBarColor(color: Int = ColorFinal.wechatTitleBar, alpha: Int = 0) {
        StatusBarUtil.setColor(activity, color, alpha)
    }

    /**
     * Android6.0设置亮色状态栏模式
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun setLightStatusBarForM(@NotNull activity: Activity, dark: Boolean) {
        val window = activity.window
        if (window != null) {
            val decor = window.decorView
            var ui = decor.systemUiVisibility
            ui = if (dark) {
                ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                ui and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decor.systemUiVisibility = ui
        }
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
        if (ProgressUtils.mProgressDialog != null) {
            ProgressUtils.cancleProgressDialog()
        }
    }

    fun showToast(msg: String) {
        ToastUtils.showShort(activity, msg)
    }

    fun setRefreshLayout(refreshLayout: RefreshLayout, refreshListener: RefreshListener? = null, loadMoreListener: LoadMoreListener? = null) {

        refreshLayout.setOnRefreshListener {
            mPageSize = 1
            loadFromServer()
            refreshListener?.refresh()
        }

        refreshLayout.setOnLoadMoreListener {
            mPageSize += 1
            loadFromServer()
            if (null != loadMoreListener) {
                loadMoreListener.loadMore()
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoadingStatus(type: String) {
        when(type){
            "LoadingSuccess" ->{//成功
                mMultipleStatusView.showContent()
            }
            "LoadingError" ->{//失败
                mMultipleStatusView.showError()
            }
            "LoadingEmpty" ->{//空数据
                mMultipleStatusView.showEmpty()
            }
        }
    }

    fun showEmptyView(){
        EventBusUtil.post("LoadingEmpty")
    }
    fun showErrorView(){
        EventBusUtil.post("LoadingError")
    }

    override fun onDestroyView() {
        EventBusUtil.unRegister(this)
        super.onDestroyView()
    }

    fun showQQWaitDialog(msg: String = "请稍后"){
        mQQDialog = QMUITipDialog.Builder(activity)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg)
                .create()
        mQQDialog?.show()
    }

    fun dismissQQDialog() {
        mQQDialog?.dismiss()
    }
}
