package app.jietuqi.cn.ui.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.ProjectMarketEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import com.coorchice.library.SuperTextView
import com.sackcentury.shinebuttonlib.ShineButton

/**
 * 作者： liuyuanbo on 2019/1/25 09:42.
 * 时间： 2019/1/25 09:42
 * 邮箱： 972383753@qq.com
 * 用途：
 *
 * 0 -- 查询分类下的项目列表
 * 1 -- 查询我发布的项目
 * 2 -- 查询我收藏的项目
 */
class OverallProjectClassifyAdapter(val mList: ArrayList<ProjectMarketEntity>, val mType: Int, val mListener: OperateListener) : RecyclerView.Adapter<OverallProjectClassifyAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_project_classify, parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            var entity = mList[adapterPosition]
            when(v?.id){
                R.id.sOverallProjectMarketDeleteTv ->{
                    mListener.delete(entity, adapterPosition)
                }
                R.id.msOverallProjectMarketModifyTv ->{
                    mListener.modify(entity, adapterPosition)
                }
                R.id.msOverallProjectMarketPopularizeTv ->{
                    mListener.popup(entity, adapterPosition)
                }
                R.id.sOverallProjectShowCollectBtn ->{
                    mListener.collect(entity, adapterPosition)
                }
                else ->{
                    LaunchUtil.startOverallProjectShowActivity(itemView.context, entity)
                }
            }
        }
        var pic: ImageView = itemView.findViewById(R.id.sOverallProjectMarketPicIv)
        var title: TextView = itemView.findViewById(R.id.sOverallProjectMarketTitleTv)
        private var classify: TextView = itemView.findViewById(R.id.sOverallProjectMarketClassifyTv)
        var avatar: ImageView = itemView.findViewById(R.id.sOverallProjectMarketAvatarIv)
        var nickName: TextView = itemView.findViewById(R.id.sOverallProjectMarketNickNameTv)
        var statusTv: TextView = itemView.findViewById(R.id.sOverallProjectMarketStatusTv)
        var myLayout: LinearLayout = itemView.findViewById(R.id.sOverallProjectMarketMyLayout)
        var deleteTv: TextView = itemView.findViewById(R.id.sOverallProjectMarketDeleteTv)
        var modifyTv: TextView = itemView.findViewById(R.id.msOverallProjectMarketModifyTv)
        var popupTv: TextView = itemView.findViewById(R.id.msOverallProjectMarketPopularizeTv)
        var collecBtn: ShineButton = itemView.findViewById(R.id.sOverallProjectShowCollectBtn)
        var topTagIv: ImageView = itemView.findViewById(R.id.sOverallProjectMarketTopIv)
        var checkTv: SuperTextView = itemView.findViewById(R.id.sCheckProjectTv)
        var rightView: ImageView = itemView.findViewById(R.id.sOverallProjectMarketRightIv)
        init {
            itemView.setOnClickListener(this)
            deleteTv.setOnClickListener(this)
            modifyTv.setOnClickListener(this)
            popupTv.setOnClickListener(this)
            collecBtn.setOnClickListener(this)
            itemView.setOnClickListener(this)
            if (mType == 0){
                checkTv.visibility = View.VISIBLE
                rightView.visibility = View.GONE
                nickName.visibility = View.VISIBLE
                avatar.visibility = View.VISIBLE
            }else if (mType == 1){//我发布的
                myLayout.visibility = View.VISIBLE
                statusTv.visibility = View.VISIBLE
                checkTv.visibility = View.GONE
                rightView.visibility = View.VISIBLE
                nickName.visibility = View.GONE
                avatar.visibility = View.INVISIBLE
            }else if (mType == 2){
                collecBtn.visibility = View.VISIBLE
                checkTv.visibility = View.GONE
                rightView.visibility = View.VISIBLE
                nickName.visibility = View.VISIBLE
                avatar.visibility = View.VISIBLE
                collecBtn.setChecked(true, false)
            }
        }
        fun bind(entity: ProjectMarketEntity) {
            if (mType == 0){
                if (entity.is_top > 0){
                    topTagIv.visibility = View.VISIBLE
                }else{
                    topTagIv.visibility = View.GONE
                }
            }
            GlideUtil.display(itemView.context, entity.logo, pic)
            title.text = entity.name
            classify.text = entity.industry.name
            GlideUtil.displayHead(itemView.context, entity.users.headimgurl, avatar)
            nickName.text = entity.users.nickname
            if (entity.status == 2){
                statusTv.setTextColor(Color.parseColor("#F85A59"))
                statusTv.text = "未通过"
                popupTv.visibility = View.GONE
            }else if (entity.status == 1){
                statusTv.setTextColor(ContextCompat.getColor(itemView.context, R.color.overallBlue))
                statusTv.text = "已通过"
                popupTv.visibility = View.VISIBLE
            }
        }
    }
    interface OperateListener{
        fun delete(entity: ProjectMarketEntity, position: Int)
        fun modify(entity: ProjectMarketEntity, position: Int)
        fun popup(entity: ProjectMarketEntity, position: Int)
        fun collect(entity: ProjectMarketEntity, position: Int)
    }
}
