package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.OverallIndustryEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.ToastUtils
import app.jietuqi.cn.util.UserOperateUtil


/**
 * 作者： liuyuanbo on 2019/1/9 11:48.
 * 时间： 2019/1/9 11:48
 * 邮箱： 972383753@qq.com
 * 用途： 项目市场分类
 */
class ProjectMarketFunAdapter : RecyclerView.Adapter<ProjectMarketFunAdapter.Holder>() {
    private val mList: ArrayList<OverallIndustryEntity> = arrayListOf()
    init {
        mList.addAll(UserOperateUtil.getProjectClassify())
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_project_market_classify, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val title: TextView = itemView.findViewById(R.id.sFunTitleTv)
        private val pic: ImageView = itemView.findViewById(R.id.sFunPicIv)
        init {
            itemView.setOnClickListener{
                val entity = mList[adapterPosition]
                LaunchUtil.startOverallProjectClassifyActivity(itemView.context, 0, "", entity.title, entity.id.toString())
            }
        }
        fun bind(entity: OverallIndustryEntity) {
            title.text = entity.title
            GlideUtil.display(itemView.context, entity.logo, pic)
        }
        override fun onClick(v: View?) {
            val entity = mList[adapterPosition]
            ToastUtils.showShort(itemView.context,entity.name)
        }
    }
}
