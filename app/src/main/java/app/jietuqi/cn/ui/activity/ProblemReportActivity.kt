package app.jietuqi.cn.ui.activity

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseActivity
import app.jietuqi.cn.callback.DeleteListener
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.entity.ProblemReportEntity
import app.jietuqi.cn.ui.adapter.ProblemReportAdapter
import app.jietuqi.cn.util.OtherUtil
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_report_problem.*

/**
 * 作者： liuyuanbo on 2018/10/25 15:54.
 * 时间： 2018/10/25 15:54
 * 邮箱： 972383753@qq.com
 * 用途： 问题反馈的页面
 */

class ProblemReportActivity : BaseActivity(), DeleteListener {

    /**
     * 反馈的图片
     */
    private var mPicList = arrayListOf<ProblemReportEntity>()
    private var mAdapter: ProblemReportAdapter? = null

    override fun setLayoutResourceId() = R.layout.activity_report_problem

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTitle("问题反馈")
        mAdapter = ProblemReportAdapter(mPicList, this)
        mRecyclerView.adapter = mAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.IMAGE_SELECT ->{
                    mAlbumList = Matisse.obtainResult(data)
                    var entity: ProblemReportEntity
                    for (i in 0 until mAlbumList.size) {
                        entity = ProblemReportEntity()
                        entity.pic = mFiles[i]
                        mPicList.add(entity)
                    }
                    mAdapter?.notifyDataSetChanged()
                    if (mPicList.size >= 3){
                        mSelectPicsIv.visibility = View.GONE
                    }
                    mFiles.clear()
                }
            }
        }
    }
    override fun initViewsListener() {
        mSelectPicsIv.setOnClickListener(this)
        mSuggestionTv.setOnClickListener(this)
        mFunProblemTv.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mSelectPicsIv ->{
                callAlbum(3 - mPicList.size)
            }
            R.id.mSuggestionTv ->{
                OtherUtil.changeReoprtProblemBtnBg(this, mSuggestionTv, mFunProblemTv)
            }
            R.id.mFunProblemTv ->{
                OtherUtil.changeReoprtProblemBtnBg(this, mFunProblemTv, mSuggestionTv)
            }
        }
    }
    override fun delete(position: Int) {
        mPicList.removeAt(position)
        mAdapter?.notifyItemRemoved(position)
        mSelectPicsIv.visibility = View.VISIBLE
    }
}
