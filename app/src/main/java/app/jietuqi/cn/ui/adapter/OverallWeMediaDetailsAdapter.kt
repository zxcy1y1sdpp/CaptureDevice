package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.jietuqi.cn.GlideApp
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.OverallWeMediaClassifyEntity
import app.jietuqi.cn.ui.entity.ProjectMarketEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.coorchice.library.SuperTextView

/**
 * 作者： liuyuanbo on 2019/1/24 18:03.
 * 时间： 2019/1/24 18:03
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class OverallWeMediaDetailsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE1 = 0
    private val TYPE2 = 1
    private lateinit var mEntity: OverallWeMediaClassifyEntity
    private var mType = 0
    fun setData(entity: OverallWeMediaClassifyEntity, type: Int){
        mEntity = entity
        mType = type
    }
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE1
            else -> TYPE2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE1 -> Holder1(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_we_media_details_1, parent, false))
            else -> Holder2(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_we_media_details_2, parent, false))
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

    internal inner class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView)/*, View.OnClickListener*/{
        private var titleTv: TextView = itemView.findViewById(R.id.sMeMediaTitleTv)
        private var iconIv: ImageView = itemView.findViewById(R.id.sWeMediaIconIv)
        private var priceTv: SuperTextView = itemView.findViewById(R.id.sWeMediaPriceTv)
        private var hitCountTv: TextView = itemView.findViewById(R.id.sWeMediaHitCountTv)
        private var statusTv: TextView = itemView.findViewById(R.id.sWeMediaStatusTv)
        private var fansCountTv: TextView = itemView.findViewById(R.id.sWeMediaFansCountTv)
        private var favourTv: TextView = itemView.findViewById(R.id.sWeMediaFavourTv)
        private var videoCount: TextView = itemView.findViewById(R.id.sWeMediaVideoCountTv)
        private var classifyTv: TextView = itemView.findViewById(R.id.sWeMediaClassifyTv)
        private var contentTv: TextView = itemView.findViewById(R.id.sWeMediaContentTv)

        private var areaLayout: LinearLayout = itemView.findViewById(R.id.sAreaLayout)
        private var hitLayout: LinearLayout = itemView.findViewById(R.id.sWeMediaHitCountLayout)
        fun bind() {
            titleTv.text = mEntity.title
            if (mType == 0){
                areaLayout.visibility = View.GONE
                hitLayout.visibility = View.VISIBLE
                GlideUtil.display(itemView.context, R.drawable.douyin, iconIv)
            }else{

                areaLayout.visibility = View.VISIBLE
                hitLayout.visibility = View.GONE
                GlideUtil.display(itemView.context, R.drawable.kuaishou, iconIv)
            }
            priceTv.text = StringUtils.insertFront(mEntity.price, "¥")
            hitCountTv.text = mEntity.hits_title
            if (mEntity.approve == 0){
                statusTv.text = "未认证"
            }else{
                statusTv.text = "已认证"
            }
            fansCountTv.text = mEntity.fans_title
            when {
                mEntity.bias == 0 -> favourTv.text = "男女平衡"
                mEntity.bias == 1 -> favourTv.text = "男粉多"
                else -> favourTv.text = "女粉多"
            }
            videoCount.text = mEntity.video.toString()
            classifyTv.text = StringUtils.insertFront(mEntity.industry.title, "所属类目：")
            contentTv.text = mEntity.content
        }
    }

    inner class Holder2(itemView: View) : RecyclerView.ViewHolder(itemView){
        var pic: ImageView = itemView.findViewById(R.id.sPicIv)

        fun bind(entity: OverallWeMediaClassifyEntity.PictureEntity) {
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

