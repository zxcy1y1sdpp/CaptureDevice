package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import app.jietuqi.cn.GlideApp
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.OverallPublishEntity
import app.jietuqi.cn.ui.entity.ProjectMarketEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.TimeUtil
import app.jietuqi.cn.util.UserOperateUtil
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.makeramen.roundedimageview.RoundedImageView
import com.sackcentury.shinebuttonlib.ShineButton

/**
 * 作者： liuyuanbo on 2019/1/24 18:03.
 * 时间： 2019/1/24 18:03
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class OverallProjectShowAdapter(val mOperatetListener: OperatetListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE1 = 0
    private val TYPE2 = 1
    private lateinit var mEntity: ProjectMarketEntity
    fun setData(entity: ProjectMarketEntity){
        mEntity = entity
    }
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE1
            else -> TYPE2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE1 -> Holder1(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_project_show1, parent, false))
            else -> Holder2(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_project_show2, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE1 -> if (holder is Holder1) {
                holder.bind()
            }
            TYPE2 -> if (holder is Holder2) {
                holder.bind(mEntity.picture[position - 1])
                mEntity.picture
            }
        }
    }

    override fun getItemCount() = mEntity.picture.size + 1

    internal inner class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        override fun onClick(v: View?) {
            when(v?.id){
                R.id.sOverallProjectShowCollectLayout, R.id.sOverallProjectShowCollectBtn ->{
                    if (UserOperateUtil.isCurrentLogin(itemView.context)){
                        mOperatetListener.collect(mEntity)
                    }
                }
                R.id.sOverallProjectShowPhoneLayout ->{
                    mOperatetListener.operate(mEntity, "手机")
                }
                R.id.sOverallProjectShowWXLayout ->{
                    mOperatetListener.operate(mEntity, "微信")
                }
                R.id.sOverallProjectShowQQLayout ->{
                    mOperatetListener.operate(mEntity, "qq")
                }
            }
        }

        private var avatar: RoundedImageView = itemView.findViewById(R.id.sOverallProjectShowAvatarIv)
        private var titleTv: TextView = itemView.findViewById(R.id.sTitleTv)
        private var nickName: TextView = itemView.findViewById(R.id.sOverallProjectShowNickNameTv)
        private var time: TextView = itemView.findViewById(R.id.sOverallProjectShowUpdateTimeTv)
        private var seeTimes: TextView = itemView.findViewById(R.id.sOverallProjectShowSeeTimesTv)
        private var collect: ShineButton = itemView.findViewById(R.id.sOverallProjectShowCollectBtn)
        private var collectTv: TextView = itemView.findViewById(R.id.sOverallProjectShowCollectTv)
        private var collectlayout: RelativeLayout = itemView.findViewById(R.id.sOverallProjectShowCollectLayout)
        private var content: TextView = itemView.findViewById(R.id.sOverallProjectShowContentTv)
        private var classifyTv: TextView = itemView.findViewById(R.id.sClassifyTitleTv)

        private var phone: LinearLayout = itemView.findViewById(R.id.sOverallProjectShowPhoneLayout)
        private var wx: LinearLayout = itemView.findViewById(R.id.sOverallProjectShowWXLayout)
        private var qq: LinearLayout = itemView.findViewById(R.id.sOverallProjectShowQQLayout)
        init {
            collectlayout.setOnClickListener(this)
            collect.setOnClickListener(this)
            phone.setOnClickListener(this)
            wx.setOnClickListener(this)
            qq.setOnClickListener(this)
        }
        fun bind() {
            titleTv.text = mEntity.name
            GlideUtil.displayHead(itemView.context, mEntity.users.headimgurl, avatar)
            nickName.text = mEntity.users.nickname
            time.text = StringUtils.insertBack(TimeUtil.timeWithoutHMS(mEntity.update_time), "更新")
            seeTimes.text = StringUtils.insertFront(mEntity.view, "浏览")
            classifyTv.text = StringUtils.insertFront(mEntity.industry.title, "行业类型：")
            if (1 == mEntity.is_favour){//已收藏
                collect.setChecked(true, true)
                collectTv.text = "已收藏"
            }else{
                collect.setChecked(false, true)
                collectTv.text = "收藏"
            }
            if (TextUtils.isEmpty(mEntity.phone)){
                phone.visibility = View.GONE
            }
            if (TextUtils.isEmpty(mEntity.wx)){
                wx.visibility = View.GONE
            }
            if (TextUtils.isEmpty(mEntity.qq)){
                qq.visibility = View.GONE
            }
            content.text = mEntity.content
        }
    }

    inner class Holder2(itemView: View) : RecyclerView.ViewHolder(itemView){
        var pic: ImageView = itemView.findViewById(R.id.sPicIv)

        fun bind(entity: OverallPublishEntity) {
            GlideApp.with(itemView.context)
                    .load(entity.url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.loading)
                    .fallback(R.mipmap.loading)
                    .into(pic)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)

        when (holder.itemViewType) {
            TYPE2 -> if (holder is Holder2) {//不添加该判断会导致数据重复
                GlideUtil.clearViews(holder.pic.context, holder.pic)
                holder.pic.setImageBitmap(null)
            }
            else -> {
            }
        }
    }

    interface OperatetListener{
        fun collect(entity: ProjectMarketEntity)
        fun operate(entity: ProjectMarketEntity, type: String)
    }
}

