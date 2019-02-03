package app.jietuqi.cn.ui.activity

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil
import kotlinx.android.synthetic.main.activity_my_project.*

/**
 * 作者： liuyuanbo on 2019/1/22 23:11.
 * 时间： 2019/1/22 23:11
 * 邮箱： 972383753@qq.com
 * 用途： 我的项目
 */
class OverallProjectMyActivity : BaseOverallInternetActivity() {
    override fun setLayoutResourceId() = R.layout.activity_my_project

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTopTitle("我的项目")
    }

    override fun initViewsListener() {
        mMyProjectLayout.setOnClickListener(this)
        mMyCollectLayout.setOnClickListener(this)
        mProblemLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mMyProjectLayout ->{//我发布的项目
                LaunchUtil.startOverallProjectClassifyActivity(this, 1, UserOperateUtil.getUserId(), "发布的项目", "")
            }
            R.id.mMyCollectLayout ->{//我收藏的项目
                LaunchUtil.startOverallProjectClassifyActivity(this, 2, UserOperateUtil.getUserId(), "收藏的项目", "")
            }
            R.id.mProblemLayout ->{

            }
        }
    }
}
