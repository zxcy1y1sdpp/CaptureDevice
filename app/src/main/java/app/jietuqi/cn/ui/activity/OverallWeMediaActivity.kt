package app.jietuqi.cn.ui.activity

import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.util.LaunchUtil
import kotlinx.android.synthetic.main.activity_overall_we_media.*

/**
 * 作者： liuyuanbo on 2019/2/22 09:53.
 * 时间： 2019/2/22 09:53
 * 邮箱： 972383753@qq.com
 * 用途： 自媒体服务
 */
class OverallWeMediaActivity : BaseOverallInternetActivity() {
    override fun setLayoutResourceId() = R.layout.activity_overall_we_media

    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("自媒体服务")
    }

    override fun initViewsListener() {
        mOverallDouyinIv.setOnClickListener(this)
        mOverallKuaiShowIv.setOnClickListener(this)
        mOverallWeMediaIv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallDouyinIv ->{
                LaunchUtil.startOverallWeMediaClassifyActivity(this, 0)
            }
            R.id.mOverallKuaiShowIv ->{
                LaunchUtil.startOverallWeMediaClassifyActivity(this, 1)
            }
            R.id.mOverallWeMediaIv ->{
                LaunchUtil.startOverallWebViewActivity(this, "http://jietuqi.cn/index/index/news/id/3", "自媒体的价值")
            }
        }
    }
}
