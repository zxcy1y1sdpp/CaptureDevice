package dasheng.com.capturedevice.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dasheng.com.capturedevice.R

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
    protected var mRootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        /**
         * 解决内存泄漏的重要方法，防止oncreateview方法多次执行
         */
        if (null != mRootView) {
            val parent = mRootView!!.parent as ViewGroup
            parent?.removeView(mRootView)
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
    }

    protected fun <T : View> findViewById(id: Int): T? {
        if (mRootView == null) {
            return null
        }

        return mRootView!!.findViewById<View>(id) as T
    }

    /**
     * 设置根布局资源id
     */
    protected abstract fun setLayoutResouceId(): Int

    /**
     * 设置根布局资源id
     * @return
     */
    protected fun setContextS() {}

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
    protected fun getArguments(arguments: Bundle?) {}

    open fun loadFromDb() {

    }

    override fun onResume() {
        super.onResume()
        loadFromDb()
    }

    override fun onClick(v: View) {

    }
    /**
     * 设置页面的标题
     * @param title
     * @param type
     *        0 -- 只有一个返回键和标题
     *        1 -- 只有标题
     */
    protected fun setTitle(title: String, type: Int = 0) {
        val titleTv = findViewById<TextView>(R.id.overAllTitleTv)
        val iv = findViewById<ImageView>(R.id.overAllBackIv)
        when(type){
            1 ->{
                iv?.visibility = View.GONE
            }
            2 ->{
            }
        }
        iv?.setOnClickListener(this)
        titleTv?.setOnClickListener(this)
        titleTv?.text = title
    }
}
