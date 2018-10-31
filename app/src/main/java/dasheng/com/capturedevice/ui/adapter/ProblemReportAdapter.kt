package dasheng.com.capturedevice.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.callback.DeleteListener
import dasheng.com.capturedevice.entity.ProblemReportEntity
import dasheng.com.capturedevice.util.GlideUtil

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
//        return mList.size
        return if (mList.size >= 3) 3 else mList.size
    }

    /* override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         return Holder1(LayoutInflater.from(parent.context).inflate(R.layout.item_problem_report, parent, false))
     }

     override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
         (holder as Holder2).bind(mList[position])
     }

     override fun getItemCount(): Int {
         return 3
 //        return if (mList.size < 3) mList.size + 1 else 3
     }*/
/*
    inner class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var deleteIv: ImageView = itemView.findViewById(R.id.deleteIv)
        init {
            deleteIv.visibility = View.GONE
            itemView.findViewById<RelativeLayout>(R.id.selectPicsLayout).setOnClickListener(this)
        }

        override fun onClick(v: View) {
        }

    }*/
    inner class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var picThumb: ImageView = itemView.findViewById(R.id.picThumb2)
        init {
            itemView.findViewById<ImageView>(R.id.deleteIv).setOnClickListener(this)
        }
        fun bind(entity: ProblemReportEntity) {
            GlideUtil.display(itemView.context, entity.pic, picThumb)
        }
        override fun onClick(v: View?) {
            listener.delete(adapterPosition)
        }
    }

}
