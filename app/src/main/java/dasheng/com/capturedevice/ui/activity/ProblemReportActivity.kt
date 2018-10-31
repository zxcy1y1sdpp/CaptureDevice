package dasheng.com.capturedevice.ui.activity

import android.content.Intent
import android.view.View
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseActivity
import dasheng.com.capturedevice.callback.DeleteListener
import dasheng.com.capturedevice.constant.RequestCode
import dasheng.com.capturedevice.entity.ProblemReportEntity
import dasheng.com.capturedevice.ui.adapter.ProblemReportAdapter
import dasheng.com.capturedevice.util.FileUtil
import dasheng.com.capturedevice.util.OtherUtil
import kotlinx.android.synthetic.main.activity_report_problem.*
import java.io.File

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
//        var dialog = BottomDialog()
//        dialog.setItemSelectionListener(this)
//        dialog.show(supportFragmentManager, "Dialog")
        /*val entity = ProblemReportEntity()
        entity.res = R.mipmap.add_video
        entity.type = 0
        entity.position = 0
        mPicList.add(entity)
        mPicList.add(entity)
        mPicList.add(entity)*/
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
                    for (i in 0..mAlbumList.size - 1) {
                        entity = ProblemReportEntity()
                        entity.pic = mFile
                        mPicList.add(entity)
                    }
                    mAdapter?.notifyDataSetChanged()
                    if (mPicList.size >= 3){
                        mSelectPicsIv.visibility = View.GONE
                    }
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
    }
}
