package app.jietuqi.cn.ui.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.OverallCardEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.widget.ShapeCornerBgView

/**
 * 作者： liuyuanbo on 2018/11/11 21:23.
 * 时间： 2018/11/11 21:23
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class OverallJoinGroupsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mType = 0
    private var mFanslist: ArrayList<OverallCardEntity> = arrayListOf()
    fun setData(fansList: ArrayList<OverallCardEntity>, type: Int){
        mType = type
        mFanslist = fansList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (mType == 0){
            Holder1(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_add_fans_eacher, parent, false))
        }else{
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_join_groups, parent, false))
        }
    }

    override fun getItemCount(): Int = mFanslist.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mType == 0){
            if (holder is Holder1){
                holder.bind(mFanslist[position])
            }
        }else{
            if (holder is Holder){
                holder.bind(mFanslist[position])
            }
        }
    }

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView){

        private var titleBg: ShapeCornerBgView = itemView.findViewById(R.id.sOverallJoinGroupBgView)
        private var title: TextView = itemView.findViewById(R.id.sOverallJoinGroupTitleTv)
        private var content: TextView = itemView.findViewById(R.id.sOverallJoinGroupContentTv)
        private var isTopTagIv: ImageView = itemView.findViewById(R.id.sOverallJoinGroupTopIv)//置顶标识
//        private var isVipIv: ImageView = itemView.findViewById(R.id.sOverallJoinGroupVipIv)//置顶标识
        fun bind(entity: OverallCardEntity){
            if (entity.is_top >= 1){
                isTopTagIv.visibility = View.VISIBLE
            }else{
                isTopTagIv.visibility = View.GONE
            }
            when {
                entity.is_top == 0 -> //未置顶
                    title.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                entity.is_top == 1 -> //置顶一小时
                    title.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                entity.is_top == 3 -> //置顶三小时
                    title.setTextColor(ContextCompat.getColor(itemView.context, R.color.overallBlue))
                else -> //置顶24小时
                    title.setTextColor(ContextCompat.getColor(itemView.context, R.color.inviteRed))
            }
            /*if (entity.vip > 1){
                isVipIv.visibility = View.VISIBLE
            }else{
                isVipIv.visibility = View.GONE
            }*/
            var color = Color.parseColor(StringUtils.insertFront(entity.industry.background, "#"))
            titleBg.setBgColor(color)
            titleBg.text = entity.industry.name
            title.text = entity.wxnickname
            content.text = entity.content
        }
    }
    class Holder1(itemView: View): RecyclerView.ViewHolder(itemView){
        private var avatar: ImageView = itemView.findViewById(R.id.sOverallAddFansEachOtherAvatarIv)//头像
        private var nickName: TextView = itemView.findViewById(R.id.sOverallAddFansEachOtherNickNameTv)//昵称
        private var content: TextView = itemView.findViewById(R.id.sOverallAddFansEachOtherContentTv)
        private var isTopTagIv: ImageView = itemView.findViewById(R.id.sOverallJoinGroupTopIv)//置顶标识
        private var isVipIv: ImageView = itemView.findViewById(R.id.sOverallJoinGroupVipIv)//置顶标识
        fun bind(entity: OverallCardEntity){
            content.text = entity.content
            GlideUtil.displayHead(itemView.context, entity.headimgurl, avatar)
            nickName.text = entity.wxnickname
            if (entity.is_top >= 1){
                isTopTagIv.visibility = View.VISIBLE
            }else{
                isTopTagIv.visibility = View.GONE
            }
            when {
                entity.is_top == 0 -> //未置顶
                    nickName.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                entity.is_top == 1 -> //置顶一小时
                    nickName.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                entity.is_top == 3 -> //置顶三小时
                    nickName.setTextColor(ContextCompat.getColor(itemView.context, R.color.overallBlue))
                else -> //置顶24小时
                    nickName.setTextColor(ContextCompat.getColor(itemView.context, R.color.inviteRed))
            }
            if (entity.vip > 1){
                isVipIv.visibility = View.VISIBLE
            }else{
                isVipIv.visibility = View.GONE
            }
        }
    }
}
