package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.OverallWeMediaClassifyEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils

/**
 * 作者： liuyuanbo on 2018/10/25 17:10.
 * 时间： 2018/10/25 17:10
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class OverallWeMediaClassifyAdapter(val mList: ArrayList<OverallWeMediaClassifyEntity>, val mType: Int) : RecyclerView.Adapter<OverallWeMediaClassifyAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_overal_we_meida_classify, parent, false))
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val titleTv = itemView.findViewById<TextView>(R.id.sTitleTv)//标题
        private val classifyTv = itemView.findViewById<TextView>(R.id.sClassifyTv)//所属分类
        private var iconIv: ImageView = itemView.findViewById(R.id.sClassIconIv)//图标
        private val accountTv = itemView.findViewById<TextView>(R.id.sAccountTv)//账号
        private val hitCount = itemView.findViewById<TextView>(R.id.sHitCountTv)//点击量
        private val hitCountTitleTv = itemView.findViewById<TextView>(R.id.sHitCountTitleTv)//标题
        private val certificationStatus = itemView.findViewById<TextView>(R.id.sCertificationStatusTv)//认证状态
        private val fansCount = itemView.findViewById<TextView>(R.id.sFansCountTv)//粉丝数量
        private val fansFavour = itemView.findViewById<TextView>(R.id.sFansFavourTv)//粉丝偏向
        private val price = itemView.findViewById<TextView>(R.id.sPriceTv)//价格

        fun bind(entity: OverallWeMediaClassifyEntity) {
            titleTv.text = entity.title
            classifyTv.text = StringUtils.insertFront(entity.industry.title, "所属类目：")
            if (mType == 0){//抖音
                hitCount.text = entity.hits_title
                hitCountTitleTv.text = "点击量"
                GlideUtil.display(itemView.context, R.drawable.douyin, iconIv)
            }else{//快手
                hitCount.text = "全国"
                hitCountTitleTv.text = "粉丝区域"
                GlideUtil.display(itemView.context, R.drawable.kuaishou, iconIv)
            }
            accountTv.text = entity.name

            fansCount.text = entity.fans_title
            if (entity.approve == 0){
                certificationStatus.text = "未认证"
            }else{
                certificationStatus.text = "已认证"
            }
            when {
                entity.bias == 0 -> fansFavour.text = "男女平衡"
                entity.bias == 1 -> fansFavour.text = "男粉多"
                else -> fansFavour.text = "女粉多"
            }
            price.text = StringUtils.insertFront(entity.price, "¥")
        }
    }

}
