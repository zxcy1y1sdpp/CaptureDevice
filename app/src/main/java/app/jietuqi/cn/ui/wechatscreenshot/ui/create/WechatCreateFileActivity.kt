package app.jietuqi.cn.ui.wechatscreenshot.ui.create

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.wechat.BaseWechatScreenShotCreateActivity
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.ui.wechatscreenshot.adapter.WechatCreateFileAdapter
import app.jietuqi.cn.ui.wechatscreenshot.entity.FileEntity
import app.jietuqi.cn.util.OtherUtil
import kotlinx.android.synthetic.main.activity_wechat_create_file.*

/**
 * 作者： liuyuanbo on 2018/12/5 17:07.
 * 时间： 2018/12/5 17:07
 * 邮箱： 972383753@qq.com
 * 用途： 创建文件
 */
class WechatCreateFileActivity : BaseWechatScreenShotCreateActivity() {
    private var mList = arrayListOf<FileEntity>()
    private lateinit var mAdapter: WechatCreateFileAdapter
    private var mChoiceEntity = FileEntity()
    override fun needLoadingView() = false
    override fun setLayoutResourceId() = R.layout.activity_wechat_create_file
    override fun initAllViews() {
        super.initAllViews()
        mMsgEntity.msgType = 16
        mChoiceEntity.unit = " B"
        setBlackTitle("文件", 1)
        mList.add(FileEntity("R.drawable.fileicon_ppt", ".pptx", false))
        mList.add(FileEntity("R.drawable.fileicon_word", ".docx", false))
        mList.add(FileEntity("R.drawable.fileicon_xls", ".xlsx", false))
        mList.add(FileEntity("R.drawable.fileicon_zip", ".rar", false))
        mList.add(FileEntity("R.drawable.fileicon_txt", ".txt", false))
        mList.add(FileEntity("R.drawable.fileicon_ppt", ".ppt", false))
        mList.add(FileEntity("R.drawable.fileicon_word", ".doc", false))
        mList.add(FileEntity("R.drawable.fileicon_xls", ".xls", false))
        mList.add(FileEntity("R.drawable.fileicon_zip", ".zip", false))
        mList.add(FileEntity("R.drawable.fileicon_pdf", ".pdf", false))
        mAdapter = WechatCreateFileAdapter(mList)
        mWechatFileRv.adapter = mAdapter
    }

    override fun initViewsListener() {
        super.initViewsListener()
        mWechatFileBtV.setOnClickListener(this)
        mWechatFileKtV.setOnClickListener(this)
        mWechatFileMtV.setOnClickListener(this)
        mWechatFileGtV.setOnClickListener(this)
        mWechatFileRv.addOnItemTouchListener(object : OnRecyclerItemClickListener(mWechatFileRv) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val position = vh.adapterPosition
                var entity: FileEntity
                for (i in mList.indices){
                    entity = mList[i]
                    entity.check = false
                }
                entity = mList[position]
                entity.check = true
                mChoiceEntity.suffix = entity.suffix
                mChoiceEntity.icon = entity.icon
                mChoiceEntity.position = position
                mAdapter.notifyDataSetChanged()
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        if (mType == 1){
            mChoiceEntity = mMsgEntity.fileEntity
            mWechatFileSizeEt.setText(mChoiceEntity.size)
            mWechatFileNameEt.setText(mChoiceEntity.title)
            mList[mChoiceEntity.position].check = true
            mAdapter.notifyItemChanged(mChoiceEntity.position)
            choiceUnit(mChoiceEntity.unit)
        }
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.mWechatFileBtV ->{
                mChoiceEntity.unit = " B"
                choiceUnit(mChoiceEntity.unit)
            }
            R.id.mWechatFileKtV ->{
                mChoiceEntity.unit = " KB"
                choiceUnit(mChoiceEntity.unit)
            }
            R.id.mWechatFileMtV ->{
                mChoiceEntity.unit = " MB"
                choiceUnit(mChoiceEntity.unit)
            }
            R.id.mWechatFileGtV ->{
                mChoiceEntity.unit = " G"
                choiceUnit(mChoiceEntity.unit)
            }
            R.id.overallAllRightWithBgTv ->{
                val size = OtherUtil.getContent(mWechatFileSizeEt)
                if (TextUtils.isEmpty(size)){
                    showToast("请输入文件大小")
                    return
                }
                mChoiceEntity.size = size
                val title = OtherUtil.getContent(mWechatFileNameEt)
                if (TextUtils.isEmpty(title)){
                    showToast("请输入文件名称")
                    return
                }
                if (TextUtils.isEmpty(mChoiceEntity.suffix)){
                    showToast("请选择文件类型")
                    return
                }
                mChoiceEntity.title = title
                mMsgEntity.fileEntity = mChoiceEntity
            }
        }
        super.onClick(v)
    }
   private fun choiceUnit(unit: String){
        when(unit){
            " B" ->{
                mWechatFileBtV.setBackgroundResource(R.drawable.report_problem1)
                mWechatFileKtV.setBackgroundResource(R.drawable.report_problem5)
                mWechatFileMtV.setBackgroundResource(R.drawable.report_problem5)
                mWechatFileGtV.setBackgroundResource(R.drawable.report_problem2)

                mWechatFileBtV.setTextColor(ContextCompat.getColor(this, R.color.white))
                mWechatFileKtV.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
                mWechatFileMtV.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
                mWechatFileGtV.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
            }
            " KB" ->{
                mWechatFileKtV.setBackgroundResource(R.drawable.report_problem7)
                mWechatFileBtV.setBackgroundResource(R.drawable.report_problem6)
                mWechatFileMtV.setBackgroundResource(R.drawable.report_problem5)
                mWechatFileGtV.setBackgroundResource(R.drawable.report_problem2)

                mWechatFileKtV.setTextColor(ContextCompat.getColor(this, R.color.white))
                mWechatFileBtV.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
                mWechatFileMtV.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
                mWechatFileGtV.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
            }
            " MB" ->{
                mWechatFileMtV.setBackgroundResource(R.drawable.report_problem7)
                mWechatFileBtV.setBackgroundResource(R.drawable.report_problem6)
                mWechatFileKtV.setBackgroundResource(R.drawable.report_problem5)
                mWechatFileGtV.setBackgroundResource(R.drawable.report_problem2)

                mWechatFileMtV.setTextColor(ContextCompat.getColor(this, R.color.white))
                mWechatFileBtV.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
                mWechatFileKtV.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
                mWechatFileGtV.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
            }
            " G" ->{
                mWechatFileBtV.setBackgroundResource(R.drawable.report_problem6)
                mWechatFileKtV.setBackgroundResource(R.drawable.report_problem5)
                mWechatFileMtV.setBackgroundResource(R.drawable.report_problem5)
                mWechatFileGtV.setBackgroundResource(R.drawable.report_problem4)

                mWechatFileGtV.setTextColor(ContextCompat.getColor(this, R.color.white))
                mWechatFileBtV.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
                mWechatFileKtV.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
                mWechatFileMtV.setTextColor(ContextCompat.getColor(this, R.color.wechatBlue))
            }
        }
    }
}
