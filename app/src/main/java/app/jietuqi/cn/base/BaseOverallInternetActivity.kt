package app.jietuqi.cn.base

import android.os.Bundle
import android.view.View
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.EventBusUtil
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.include_loading.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 作者： liuyuanbo on 2018/11/7 14:38.
 * 时间： 2018/11/7 14:38
 * 邮箱： 972383753@qq.com
 * 用途： 自己页面的基类（非仿微信，支付宝，QQ页面的基类）
 * 包含网络操作
 */

abstract class BaseOverallInternetActivity : BaseOverallActivity() {
    /**
     * 当前页码
     */
    var mPage = 1
    /**
     * 每页展示的数据量
     */
    var mLimit = "30"
    var mDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLoadingView()
    }
    /**
     * 加载网络数据
     */
    open fun loadFromServer() {}

    override fun onResume() {
        super.onResume()
        loadFromServer()
    }

    override fun onDestroy() {
        mDisposable?.let { EasyHttp.cancelSubscription(it) }//取消网络请求
        super.onDestroy()
    }
    fun setRefreshLayout(refreshLayout: RefreshLayout){
        refreshLayout.setOnRefreshListener{
            mPage = 1
            loadFromServer()
            refreshAndLoadMore()
        }
        refreshLayout.setOnLoadMoreListener {
            mPage += 1
            loadFromServer()
            refreshAndLoadMore()
        }
    }

    open fun refreshAndLoadMore(){}
    private fun initLoadingView(){
        if (needLoadingView()){
            EventBusUtil.register(this)
            mMultipleStatusView.showLoading()
            mMultipleStatusView.setOnRetryClickListener(mRetryClickListener)
        }
    }
    private val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        mPage = 1
        mMultipleStatusView.showLoading()
        loadFromServer()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoadingStatus(type: String) {
        when(type){
            "LoadingSuccess" ->{//成功
                if (mMultipleStatusView.isShown){
                    mMultipleStatusView.showContent()
                }
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

    override fun onPause() {
        super.onPause()
        EventBusUtil.unRegister(this)
    }

    override fun finish() {
        if (null != mMultipleStatusView){
            mMultipleStatusView.visibility = View.GONE
        }
        super.finish()
    }
}
