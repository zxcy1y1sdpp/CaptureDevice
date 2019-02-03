package app.jietuqi.cn.ui.activity

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallActivity
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.util.OtherUtil
import kotlinx.android.synthetic.main.activity_edit.*

/**
 * 作者： liuyuanbo on 2019/1/23 19:57.
 * 时间： 2019/1/23 19:57
 * 邮箱： 972383753@qq.com
 * 用途： 只有一个输入框
 */
class OverallEditActivity : BaseOverallActivity() {
    override fun setLayoutResourceId() = R.layout.activity_edit

    override fun needLoadingView() = false

    override fun initAllViews() {
        setTopTitle("项目名称", rightTitle = "完成")
    }

    override fun initViewsListener() {}

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        val content = intent.getStringExtra(IntentKey.CONTENT)
        mEditEt.setText(content)
    }
    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.overAllRightTitleTv ->{
                val content = OtherUtil.getContent(mEditEt)
                if (content.length < 6){
                    showToast("请至少输入6个字符")
                    return
                }
                intent.putExtra(IntentKey.CONTENT, content)
                setResult(2, intent)
                finish()
            }
        }
    }
}
