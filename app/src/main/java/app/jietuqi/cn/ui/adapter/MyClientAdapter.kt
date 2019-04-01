package app.jietuqi.cn.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.entity.OverallUserInfoEntity
import java.util.*

/**
 * 作者： liuyuanbo on 2018/10/23 14:57.
 * 时间： 2018/10/23 14:57
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class MyClientAdapter(val mList: ArrayList<OverallUserInfoEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE1 = 0
    private val TYPE2 = 1
    /**
     * 0 -- 直推用户
     * 1 -- 间推用户
     * 2 -- 直推商户
     * 3 -- 间推商户
     */
    private var mTitle = ""
    fun setTitle(title: String){
        mTitle = title
    }
    override fun getItemViewType(position: Int): Int {
        return if (position == 0){
            TYPE1
        }else{
            TYPE2
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE1){
            TitleHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_my_client_1, parent, false))
        }else{
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_my_client_2, parent, false))
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE1 -> if (holder is TitleHolder) {
                holder.bind()
            }
            TYPE2 -> if (holder is Holder) {
                holder.bind(mList[position - 1])
            }
        }
    }
    override fun getItemCount() = mList.size + 1

    inner class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val countTv: TextView = itemView.findViewById(R.id.sCountTv)
        fun bind(){
            countTv.text = mTitle
        }
    }
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val nickName: TextView = itemView.findViewById(R.id.sNickNameTv)
        private val vipLevel: TextView = itemView.findViewById(R.id.sVipLevelTv)
        fun bind(entity: OverallUserInfoEntity){
            nickName.text = entity.nickname
            when(entity.status){
                1 ->{
                    vipLevel.text = "普通会员"
                }
                2 ->{
                    vipLevel.text = "季度会员"
                }
                3 ->{
                    vipLevel.text = "年度会员"
                }
                5 ->{
                    vipLevel.text = "半年会员"
                }
                6 ->{
                    vipLevel.text = "体验会员"
                }
            }
        }
    }
}
