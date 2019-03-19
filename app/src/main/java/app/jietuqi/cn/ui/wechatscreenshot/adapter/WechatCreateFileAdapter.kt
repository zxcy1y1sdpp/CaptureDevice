package app.jietuqi.cn.ui.wechatscreenshot.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ResourceHelper
import app.jietuqi.cn.ui.wechatscreenshot.entity.FileEntity
import app.jietuqi.cn.util.GlideUtil

/**
 * 作者： liuyuanbo on 2019/3/5 17:42.
 * 时间： 2019/3/5 17:42
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatCreateFileAdapter(val mList: ArrayList<FileEntity>) : RecyclerView.Adapter<WechatCreateFileAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_wechat_create_file, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var entity = mList[position]
        holder.bind(entity)
    }

    override fun getItemCount() = mList.size

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var fileNameTv: TextView = itemView.findViewById(R.id.sWechatFileNameTv)
        private var fileIconTv: ImageView = itemView.findViewById(R.id.sWechatFileIconIv)

        fun bind(entity: FileEntity) {
            GlideUtil.displayHead(itemView.context, ResourceHelper.getAppIconId(entity.icon), fileIconTv)
            fileNameTv.text = entity.suffix
            if (entity.check){
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.pressColor))
            }else{
                itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
            }
        }
    }
}

