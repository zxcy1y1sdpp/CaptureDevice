package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import app.jietuqi.cn.R
import app.jietuqi.cn.callback.DeleteListener
import app.jietuqi.cn.entity.ProblemReportEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.widget.VerticalProgressBar

/**
 * 作者： liuyuanbo on 2018/10/25 17:10.
 * 时间： 2018/10/25 17:10
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class ProblemReportAdapter(val mList: ArrayList<ProblemReportEntity>, val listener: DeleteListener) : RecyclerView.Adapter<ProblemReportAdapter.Holder1>() {
    override fun onBindViewHolder(holder: Holder1, position: Int) {
        holder.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder1 {
        return Holder1(LayoutInflater.from(parent.context).inflate(R.layout.item_problem_report, parent, false))
    }

    override fun getItemCount(): Int {
        return if (mList.size >= 3) 3 else mList.size
    }

    inner class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var picThumb: ImageView = itemView.findViewById(R.id.picThumb2)
        private val progressView: VerticalProgressBar = itemView.findViewById(R.id.sUploadProgress)
        init {
            itemView.findViewById<ImageView>(R.id.deleteIv).setOnClickListener(this)
        }
        fun bind(entity: ProblemReportEntity) {
            GlideUtil.display(itemView.context, entity.pic, picThumb)
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
        }
        override fun onClick(v: View?) {
            listener.delete(adapterPosition)
        }
    }

}
