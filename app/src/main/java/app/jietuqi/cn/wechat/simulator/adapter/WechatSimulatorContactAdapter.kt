package app.jietuqi.cn.wechat.simulator.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.StringUtils
import com.makeramen.roundedimageview.RoundedImageView
import com.zhy.android.percent.support.PercentRelativeLayout

/**
 * 作者： liuyuanbo on 2018/11/29 15:19.
 * 时间： 2018/11/29 15:19
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class WechatSimulatorContactAdapter(val mList: ArrayList<WechatUserEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE1 = 0
    private val TYPE2 = 1
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE1
            else -> TYPE2
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return when (viewType) {
            TYPE1 -> Holder1(LayoutInflater.from(parent.context).inflate(R.layout.item_role_library_1, parent, false))
            else -> Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_role_library_2, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE1 -> if (holder is Holder1) {
                holder.bind()
            }
            TYPE2 -> if (holder is Holder) {
                var entity = mList[position - 1]
                holder.bind(entity)
            }

        }
    }

    override fun getItemCount() = mList.size + 1
    inner class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        override fun onClick(v: View?) {
            when(v?.id){
                R.id.sWechatNewFriendsLayout ->{
                }
                R.id.sWechatGroupLayout ->{
                }
                R.id.sWechatTagLayout ->{
                }
                R.id.sWechatThePublicLayout ->{
                }
            }
        }
        init {
            itemView.findViewById<PercentRelativeLayout>(R.id.sWechatNewFriendsLayout).setOnClickListener(this)
            itemView.findViewById<PercentRelativeLayout>(R.id.sWechatGroupLayout).setOnClickListener(this)
            itemView.findViewById<PercentRelativeLayout>(R.id.sWechatTagLayout).setOnClickListener(this)
            itemView.findViewById<PercentRelativeLayout>(R.id.sWechatThePublicLayout).setOnClickListener(this)

        }
        fun bind() {

        }
    }
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var firstLetterTv: TextView = itemView.findViewById(R.id.sRoleLibraryFirstLetterTv)
        private var avatarIv: RoundedImageView = itemView.findViewById(R.id.sRoleLibraryAvatarIv)
        private var nickNameTv: TextView = itemView.findViewById(R.id.sRoleLibraryNickNameTv)
        private var noticeTv: TextView = itemView.findViewById(R.id.sRoleLibraryNoticeTv)
        private var lineView1Tv: TextView = itemView.findViewById(R.id.lineView1)
        private var lineView2Tv: TextView = itemView.findViewById(R.id.lineView2)
        private var lineView3Tv: TextView = itemView.findViewById(R.id.lineView3)

        fun bind(entity: WechatUserEntity) {
            GlideUtil.displayHead(itemView.context, entity.getAvatarFile(), avatarIv)
            firstLetterTv.text = entity.firstChar
            nickNameTv.text = entity.wechatUserNickName
            if (entity.isFirst){
                firstLetterTv.text = entity.firstChar
                firstLetterTv.visibility = View.VISIBLE
                lineView1Tv.visibility = View.VISIBLE
                if (entity.isLast){
                    lineView2Tv.visibility = View.GONE
                    lineView3Tv.visibility = View.VISIBLE
                }else{
                    lineView2Tv.visibility = View.VISIBLE
                    lineView3Tv.visibility = View.GONE
                }
            }else if (!entity.isFirst && !entity.isLast){
                firstLetterTv.visibility = View.GONE
                lineView1Tv.visibility = View.GONE
                lineView2Tv.visibility = View.VISIBLE
                lineView3Tv.visibility = View.GONE
            }else{
                firstLetterTv.visibility = View.GONE
                lineView1Tv.visibility = View.GONE
                lineView2Tv.visibility = View.GONE
                lineView3Tv.visibility = View.VISIBLE
            }
            if (adapterPosition == mList.size){
                noticeTv.text = StringUtils.insertBack(mList.size, "位联系人")
                noticeTv.visibility = View.VISIBLE
            }else{
                noticeTv.visibility = View.GONE
            }
        }
    }
}
