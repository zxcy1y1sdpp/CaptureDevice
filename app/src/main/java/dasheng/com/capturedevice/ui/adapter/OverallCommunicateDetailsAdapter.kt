package dasheng.com.capturedevice.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import com.sackcentury.shinebuttonlib.ShineButton
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.ui.entity.OverallCommunicateEntity
import dasheng.com.capturedevice.util.GlideUtil
import dasheng.com.capturedevice.util.LaunchUtil
import dasheng.com.capturedevice.util.StringUtils
import dasheng.com.capturedevice.util.WechatTimeUtil
import dasheng.com.capturedevice.widget.ninegrid.NineGridView
import dasheng.com.capturedevice.widget.ninegrid.preview.NineGridViewClickAdapter
/**
 * 作者： liuyuanbo on 2018/11/7 17:03.
 * 时间： 2018/11/7 17:03
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class OverallCommunicateDetailsAdapter(val mList: ArrayList<OverallCommunicateEntity>, val mEntity: OverallCommunicateEntity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
                val state = mEntity.status
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
                                mEntity.status = STATE_COLLAPSED//保存状态
                            } else {
                                holder.showAll.visibility = View.GONE
                                mEntity.status = STATE_NOT_OVERFLOW//保存状态
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
                    entity.like = !entity.like
                    notifyItemChanged(adapterPosition)
                }
                else ->{
                    LaunchUtil.startOverallCommunicateDetailsActivity(itemView.context, entity)
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
        init {
            likeBtn.setOnClickListener(this)
            showAll.setOnClickListener(this)
            itemView.setOnClickListener(this)
        }
        fun bind() {
            GlideUtil.display(itemView.context, mEntity.avatar, avatar)
            nickName.text = mEntity.nickName
            content.text = mEntity.content
            pics.setAdapter(NineGridViewClickAdapter(itemView.context, mEntity.pics))
            likeCount.text = mEntity.likeCount
            pingLunCount.text = mEntity.pingLunCount
            createTime.text = WechatTimeUtil.getStandardDate(mEntity.createTime)
            if (mEntity.like){
                likeBtn.setChecked(true, true)
            }else{
                likeBtn.setChecked(false, true)
            }
        }
    }
    inner class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            var entity = mList[adapterPosition]
            when(v?.id){
                R.id.overallCommunicateLikeBtn ->{
                    entity.like = !entity.like
                    notifyItemChanged(adapterPosition)
                }
//                else ->{
//                    LaunchUtil.startOverallCommunicateDetailsActivity(itemView.context, entity)
//                }
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
//            itemView.setOnClickListener(this)
        }
        fun bind(entity: OverallCommunicateEntity) {
            GlideUtil.display(itemView.context, entity.avatar, avatar)
            nickName.text = entity.nickName
            content.text = entity.content
            likeCount.text = entity.likeCount
            createTime.text = StringUtils.insertBack(WechatTimeUtil.getStandardDate(entity.createTime), " · 回复")
            if (entity.like){
                likeBtn.setChecked(true, true)
            }else{
                likeBtn.setChecked(false, true)
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
