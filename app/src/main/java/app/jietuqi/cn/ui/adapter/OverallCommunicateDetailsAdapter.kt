package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.callback.LikeListener
import app.jietuqi.cn.ui.entity.OverallDynamicEntity
import app.jietuqi.cn.util.*
import app.jietuqi.cn.widget.ninegrid.NineGridView
import app.jietuqi.cn.widget.ninegrid.preview.NineGridViewClickAdapter
import com.sackcentury.shinebuttonlib.ShineButton
/**
 * 作者： liuyuanbo on 2018/11/7 17:03.
 * 时间： 2018/11/7 17:03
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class OverallCommunicateDetailsAdapter(val mList: ArrayList<OverallDynamicEntity.Comment>, val mEntity: OverallDynamicEntity, var mLikeListener: LikeListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val MAX_LINE_COUNT = 3//最大显示行数
    private val STATE_UNKNOW = -1//未知状态
    private val STATE_NOT_OVERFLOW = 1//文本行数小于最大可显示行数
    private val STATE_COLLAPSED = 2//折叠状态
    private val STATE_EXPANDED = 3//展开状态

    private val TYPE1 = 0//头部
    private val TYPE2 = 1//内容
    override fun getItemViewType(position: Int): Int {
        return if (position == 0){
            TYPE1
        }else{
            TYPE2
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE1){
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_communicate_details_1, parent, false))
        }else{
            Holder1(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_communicate_details_2, parent, false))
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            TYPE1 -> if (holder is Holder){
                val state = mEntity.showAllStatus
                if (state == STATE_UNKNOW){//未知状态
                    holder.content.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            //这个回掉会调用多次，获取完行数后记得注销监听
                            holder.content.viewTreeObserver.removeOnPreDrawListener(this)
                            //holder.content.getViewTreeObserver().addOnPreDrawListener(null);
                            //如果内容显示的行数大于最大显示行数
                            if (holder.content.lineCount > MAX_LINE_COUNT) {
                                holder.content.maxLines = MAX_LINE_COUNT//设置最大显示行数
                                holder.showAll.visibility = View.VISIBLE//显示“全文”
                                holder.showAll.text = "全文"
                                mEntity.showAllStatus = STATE_COLLAPSED//保存状态
                            } else {
                                holder.showAll.visibility = View.GONE
                                mEntity.showAllStatus = STATE_NOT_OVERFLOW//保存状态
                            }
                            return true
                        }
                    })
                    holder.content.maxLines = Integer.MAX_VALUE//设置文本的最大行数，为整数的最大数值
                }else {
                    //如果之前已经初始化过了，则使用保存的状态。
                    when (state) {
                        STATE_NOT_OVERFLOW ->{
                            holder.showAll.visibility = View.GONE
                        }
                        STATE_COLLAPSED ->{
                            holder.content.maxLines = MAX_LINE_COUNT
                            holder.showAll.visibility = View.VISIBLE
                            holder.showAll.text = "全文"
                        }
                        STATE_EXPANDED ->{
                            holder.content.maxLines = Integer.MAX_VALUE
                            holder.showAll.visibility = View.VISIBLE
                            holder.showAll.text = "收起"
                        }
                    }
                }
                holder.bind()
            }
            TYPE2 -> if (holder is Holder1) {//不添加该判断会导致数据重复
                holder.bind(mList[position - 1])
            }
        }

    }

    override fun getItemCount(): Int {
        return mList.size + 1
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            when(v?.id){
                R.id.overallCommunicateDetails1ShowAllContentTv ->{
                    val status = mEntity.showAllStatus
                    if (status == STATE_COLLAPSED) {
                        content.maxLines = Integer.MAX_VALUE
                        showAll.text = "收起"
                        mEntity.showAllStatus = STATE_EXPANDED
                    } else if (status == STATE_EXPANDED) {
                        content.maxLines = MAX_LINE_COUNT
                        showAll.text = "全文"
                        mEntity.showAllStatus = STATE_COLLAPSED
                    }
                }
                R.id.overallCommunicateDetails1LikeBtn ->{
                    if (UserOperateUtil.isCurrentLoginDirectlyLogin(itemView.context)) {
                        mLikeListener.like(mEntity)
                    }
                }
            }
        }

        var avatar: ImageView = itemView.findViewById(R.id.overallCommunicateDetails1AvatarIv)
        private var nickName: TextView = itemView.findViewById(R.id.overallCommunicateDetails1NickNameTv)
        var content: TextView = itemView.findViewById(R.id.overallCommunicateDetails1ContentTv)
        var showAll: TextView = itemView.findViewById(R.id.overallCommunicateDetails1ShowAllContentTv)//展开/全文按钮
        var pics: NineGridView = itemView.findViewById(R.id.overallCommunicateDetails1NineGridView)
        var createTime: TextView = itemView.findViewById(R.id.overallCommunicateDetails1TimeTv)//创建时间
        var likeCount: TextView = itemView.findViewById(R.id.overallCommunicateDetails1LikeCountTv)//点赞人数
        var pingLunCount: TextView = itemView.findViewById(R.id.overallCommunicateDetails1PingLunTv)//评论人数
        var likeBtn: ShineButton = itemView.findViewById(R.id.overallCommunicateDetails1LikeBtn)//点赞按钮
        var pingLunAllCount: TextView = itemView.findViewById(R.id.overallCommunicateDetails1PingLunAllCountTv)
        var vipTagIv: ImageView = itemView.findViewById(R.id.overallCommunicateVipTagIv)
        init {
            likeBtn.setOnClickListener(this)
            showAll.setOnClickListener(this)
        }
        fun bind() {
            if (mEntity.vip >= 2){
                vipTagIv.visibility = View.VISIBLE
            }
            GlideUtil.displayHead(itemView.context, mEntity.headimgurl, avatar)
            nickName.text = mEntity.nickname
            content.text = mEntity.content
            pics.setAdapter(NineGridViewClickAdapter(itemView.context, mEntity.infoList))
            likeCount.text = mEntity.favour.toString()
            pingLunCount.text = mEntity.comment_number.toString()
            createTime.text = TimeUtil.stampToDateYMDHM(mEntity.create_time)
            pingLunAllCount.text = StringUtils.insertFrontAndBack(mList.size, "全部评论（","）")
            if (mEntity.is_favour == 1){
                likeBtn.setChecked(true, false)
            }else{
                likeBtn.setChecked(false, false)
            }
        }
    }
    inner class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            var entity = mList[adapterPosition - 1]
            when(v?.id){
                R.id.overallCommunicateDetails2LikeBtn ->{
                    if (UserOperateUtil.isCurrentLoginDirectlyLogin(itemView.context)) {
                        entity.position = adapterPosition - 1
                        mLikeListener.like(comment = entity, type = 1)
                    }
                }
            }
        }

        var avatar: ImageView = itemView.findViewById(R.id.overallCommunicateDetails2AvatarIv)
        private var nickName: TextView = itemView.findViewById(R.id.overallCommunicateDetails2NickNameTv)
        var content: TextView = itemView.findViewById(R.id.overallCommunicateDetails2ContentTv)
        var createTime: TextView = itemView.findViewById(R.id.overallCommunicateDetails2CreateTimeTv)//创建时间
        var likeCount: TextView = itemView.findViewById(R.id.overallCommunicateDetails2LikeCountTv)//点赞人数
        var likeBtn: ShineButton = itemView.findViewById(R.id.overallCommunicateDetails2LikeBtn)//点赞按钮
        init {
            likeBtn.setOnClickListener(this)
        }
        fun bind(entity: OverallDynamicEntity.Comment) {
            GlideUtil.displayHead(itemView.context, entity.headimgurl, avatar)
            nickName.text = entity.nickname
            content.text = entity.content
            likeCount.text = entity.favour.toString()
            createTime.text = StringUtils.insertBack(WechatTimeUtil.getStandardDate(entity.create_time * 1000), " · 回复")
            if (entity.is_favour == 1){
                likeBtn.setChecked(true, false)
            }else{
                likeBtn.setChecked(false, false)
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is Holder){
            GlideUtil.clearViews(holder.avatar.context, holder.avatar)
            holder.avatar.setImageBitmap(null)
        }else if (holder is Holder1){
            GlideUtil.clearViews(holder.avatar.context, holder.avatar)
            holder.avatar.setImageBitmap(null)
        }
    }
}
