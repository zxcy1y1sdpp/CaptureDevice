package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.entity.BannerEntity
import app.jietuqi.cn.ui.entity.ProjectMarketEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.widget.ProjectMarketBannerItemType
import com.ms.banner.Banner
import com.ms.banner.BannerConfig
import com.ms.banner.Transformer

/**
 * 作者： liuyuanbo on 2019/1/22 10:47.
 * 时间： 2019/1/22 10:47
 * 邮箱： 972383753@qq.com
 * 用途： 项目市场适配器
 */
class OverallProjectMarketAdapter(val mList: ArrayList<ProjectMarketEntity>, val mBannerList: ArrayList<BannerEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE1 = 0
    private val TYPE2 = 1
    private var mTitleList = arrayListOf<String>()
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE1
            else -> TYPE2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE1 -> Holder1(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_project_market1, parent, false))
            else -> Holder2(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_project_market2, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE1 -> if (holder is Holder1) {
                holder.bind()
            }
            TYPE2 -> if (holder is Holder2) {
                holder.bind(mList[position - 1])
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size + 1
    }

    internal inner class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var banner: Banner = itemView.findViewById(R.id.sBanner)
        private var funRecyclerView: RecyclerView = itemView.findViewById(R.id.sOverallProjectMarketRv)
        init {
            funRecyclerView.adapter = ProjectMarketFunAdapter()
            banner.setOnBannerClickListener {
                var url = mBannerList[it].hrefurl
                if (!TextUtils.isEmpty(url)){
                    LaunchUtil.startOverallWebViewActivity(itemView.context, mBannerList[it].hrefurl, mBannerList[it].title)
                }
            }
            var entity: BannerEntity
            for(i in mBannerList.indices){
                entity = mBannerList[i]
                mTitleList.add(entity.title)
            }
        }
        fun bind() {
            banner.setAutoPlay(true)
                    .setOffscreenPageLimit(mBannerList.size)
                    .setBannerAnimation(Transformer.Scale)
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                    .setBannerTitles(mTitleList)
                    .setPages(mBannerList) { ProjectMarketBannerItemType() }
                    .start()
        }
    }

    inner class Holder2(itemView: View) : RecyclerView.ViewHolder(itemView){

        var pic: ImageView = itemView.findViewById(R.id.sOverallProjectMarketPicIv)
        var title: TextView = itemView.findViewById(R.id.sOverallProjectMarketTitleTv)
        private var classify: TextView = itemView.findViewById(R.id.sOverallProjectMarketClassifyTv)
        var avatar: ImageView = itemView.findViewById(R.id.sOverallProjectMarketAvatarIv)
        var nickName: TextView = itemView.findViewById(R.id.sOverallProjectMarketNickNameTv)

        fun bind(entity: ProjectMarketEntity) {
            GlideUtil.display(itemView.context, entity.logo, pic)
            title.text = entity.name
            classify.text = entity.industry.name
            GlideUtil.displayHead(itemView.context, entity.users.headimgurl, avatar)
            nickName.text = entity.users.nickname
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)

        when (holder.itemViewType) {
            TYPE2 -> if (holder is Holder2) {//不添加该判断会导致数据重复
                GlideUtil.clearViews(holder.avatar.context, holder.avatar)
                holder.avatar.setImageBitmap(null)
            }
            else -> {
            }
        }
    }
}

