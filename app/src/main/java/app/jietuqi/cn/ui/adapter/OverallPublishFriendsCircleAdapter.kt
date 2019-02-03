package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import app.jietuqi.cn.R
import app.jietuqi.cn.callback.DeleteListener
import app.jietuqi.cn.ui.entity.OverallPublishEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.widget.VerticalProgressBar
import com.xinlan.imageeditlibrary.ToastUtils

/**
 * 作者： liuyuanbo on 2018/11/20 11:31.
 * 时间： 2018/11/20 11:31
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class OverallPublishFriendsCircleAdapter(val mList: ArrayList<OverallPublishEntity>, private val mMaxCount: Int, val mDeleteListener: DeleteListener) : RecyclerView.Adapter<OverallPublishFriendsCircleAdapter.Holder>() {

    private var mType = 0
    fun setType(type: Int){
        mType = type
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverallPublishFriendsCircleAdapter.Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_publish_friends_circle, parent, false))
    }

    override fun getItemCount() = mList.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val uploadIv: ImageView = itemView.findViewById(R.id.sUploadTv)
        private val progressView: VerticalProgressBar = itemView.findViewById(R.id.sUploadProgress)
        private val deleteIv: ImageView = itemView.findViewById(R.id.sUploadDeleteIv)
        init {
            deleteIv.setOnClickListener(this)
            progressView.setOnClickListener(this)
        }
        override fun onClick(v: View) {
            when(v.id){
                R.id.sUploadDeleteIv ->{
                    mDeleteListener.delete(adapterPosition)
                }
                R.id.sUploadProgress ->{
                    ToastUtils.showShort(itemView.context, "重试")
                }
            }
        }
        fun bind(entity: OverallPublishEntity){
            if (adapterPosition >= mMaxCount) {//图片已选完时，隐藏添加按钮
                uploadIv.visibility = View.GONE
            } else {
                uploadIv.visibility = View.VISIBLE
            }
            when(entity.uploadStatus) {
                1 -> {
                    progressView.visibility = View.VISIBLE
                    progressView.setProgress(entity.progress)
                }
                3 -> {
                    progressView.setText("点击重试")
                }
                2 -> progressView.visibility = View.GONE
                0 -> progressView.visibility = View.GONE
            }
            if (adapterPosition == mList.size - 1){
                GlideUtil.display(itemView.context, entity.lastPic, uploadIv)
                deleteIv.visibility = View.GONE
            }else{
                if (mType == 0){
                    GlideUtil.displayFile(itemView.context, entity.pic, uploadIv)
                }else{
                    if (TextUtils.isEmpty(entity.url)){
                        GlideUtil.displayFile(itemView.context, entity.pic, uploadIv)
                    }else{
                        GlideUtil.display(itemView.context, entity.url, uploadIv)
                    }
                }
                deleteIv.visibility = View.VISIBLE
            }
        }
    }
}
