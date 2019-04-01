package app.jietuqi.cn.lansong.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import app.jietuqi.cn.R
import app.jietuqi.cn.lansong.entity.LansongImgEntity
import app.jietuqi.cn.util.GlideUtil
import java.util.*

/**
 * 作者： liuyuanbo on 2019/4/1 10:59.
 * 时间： 2019/4/1 10:59
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class ChoiceImageAdapter(val mList: ArrayList<LansongImgEntity>) : RecyclerView.Adapter<ChoiceImageAdapter.Holder>() {
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChoiceImageAdapter.Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_lansong_choice_item, parent, false))
    }

    override fun getItemCount() = mList.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val picIv: ImageView = itemView.findViewById(R.id.sPicIv)
        fun bind(entity: LansongImgEntity){
            GlideUtil.displayFile(itemView.context, entity.imgFile, picIv)
        }
    }
}

