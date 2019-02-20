package app.jietuqi.cn.base

import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.zhouyou.http.EasyHttp
import io.reactivex.disposables.Disposable

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
        refreshLayout.autoRefresh()
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
}
