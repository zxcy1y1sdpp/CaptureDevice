package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.callback.LikeListener
import app.jietuqi.cn.ui.entity.OverallDynamicEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.TimeUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.ninegrid.NineGridView
import app.jietuqi.cn.widget.ninegrid.preview.NineGridViewClickAdapter
import com.sackcentury.shinebuttonlib.ShineButton

/**
 * 作者： liuyuanbo on 2018/12/13 14:57.
 * 时间： 2018/12/13 14:57
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class MyPublishAdapter(val mList: ArrayList<OverallDynamicEntity>, val mLikeListener: LikeListener) : RecyclerView.Adapter<MyPublishAdapter.Holder>() {
    private val MAX_LINE_COUNT = 3//最大显示行数
    private val STATE_UNKNOW = -1//未知状态
    private val STATE_NOT_OVERFLOW = 1//文本行数小于最大可显示行数
    private val STATE_COLLAPSED = 2//折叠状态
    private val STATE_EXPANDED = 3//展开状态
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_communicate, parent, false))

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            var entity = mList[adapterPosition]
            when(v?.id){
                R.id.overallCommunicateShowAllContentTv ->{
                    val status = entity.status
                    if (status == STATE_COLLAPSED) {
                        content.maxLines = Integer.MAX_VALUE
                        showAll.text = "收起"
                        entity.status = STATE_EXPANDED
                    } else if (status == STATE_EXPANDED) {
                        content.maxLines = MAX_LINE_COUNT
                        showAll.text = "全文"
                        entity.status = STATE_COLLAPSED
                    }
                }
                R.id.overallCommunicateLikeBtn ->{
                    if (UserOperateUtil.isCurrentLoginDirectlyLogin(itemView.context)){
                        mLikeListener.like(entity)
                    }
                }
                else ->{
                    LaunchUtil.startOverallCommunicateDetailsActivity(itemView.context, entity)
                }
            }
        }

        var avatar: ImageView = itemView.findViewById(R.id.overallCommunicateAvatarIv)
        private var nickName: TextView = itemView.findViewById(R.id.overallCommunicateNickNameTv)
        private var content: TextView = itemView.findViewById(R.id.overallCommunicateContentTv)
        private var showAll: TextView = itemView.findViewById(R.id.overallCommunicateShowAllContentTv)//展开/全文按钮
        private var pics: NineGridView = itemView.findViewById(R.id.overallCommunicateNineGridView)
        private var createTime: TextView = itemView.findViewById(R.id.overallCommunicateTimeTv)//创建时间
        private var likeCount: TextView = itemView.findViewById(R.id.overallCommunicateLikeCountTv)//点赞人数
        private var pingLunCount: TextView = itemView.findViewById(R.id.overallCommunicatePingLunTv)//评论人数
        var likeBtn: ShineButton = itemView.findViewById(R.id.overallCommunicateLikeBtn)//点赞按钮
        private var vipTagIv: ImageView = itemView.findViewById(R.id.overallCommunicateVipTagIv)//会员标志
        init {
            likeBtn.setOnClickListener(this)
            showAll.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }
        fun bind(entity: OverallDynamicEntity) {
            if (entity.vip >= 2){
                vipTagIv.visibility = View.VISIBLE
            }
            GlideUtil.display(itemView.context, entity.headimgurl, avatar)
            nickName.text = entity.nickname
            content.text = entity.content
            pics.setAdapter(NineGridViewClickAdapter(itemView.context, entity.infoList))
            likeCount.text = entity.favour.toString()
            pingLunCount.text = entity.comment_number.toString()
            createTime.text = TimeUtil.stampToDateYMDHM(entity.create_time)
            if (entity.is_favour == 1) {
                likeBtn.setChecked(true, false)
            }else{
                likeBtn.setChecked(false, false)
            }
        }
    }

    override fun onViewRecycled(holder: Holder) {
        super.onViewRecycled(holder)
        GlideUtil.clearViews(holder.avatar.context, holder.avatar)
        holder.avatar.setImageBitmap(null)
    }
}
