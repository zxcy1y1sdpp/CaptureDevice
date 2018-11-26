package app.jietuqi.cn.callback

/**
 * 作者： liuyuanbo on 2018/11/22 12:17.
 * 时间： 2018/11/22 12:17
 * 邮箱： 972383753@qq.com
 * 用途：
 */
interface RefreshListener{
    open fun refresh()
}
interface LoadMoreListener{
    fun loadMore()
}
