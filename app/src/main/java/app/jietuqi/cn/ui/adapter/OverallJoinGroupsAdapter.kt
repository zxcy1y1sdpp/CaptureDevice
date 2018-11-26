package app.jietuqi.cn.ui.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.OverallCardEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.widget.ShapeCornerBgView

/**
 * 作者： liuyuanbo on 2018/11/11 21:23.
 * 时间： 2018/11/11 21:23
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class OverallJoinGroupsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mType = 0
//    private var mGrouplist: ArrayList<OverallJoinGroupsEntity> = arrayListOf()
    private var mFanslist: ArrayList<OverallCardEntity> = arrayListOf()
    fun setData(/*groupList: ArrayList<OverallJoinGroupsEntity>, */fansList: ArrayList<OverallCardEntity>, type: Int){
        mType = type
//        mGrouplist = groupList
        mFanslist = fansList

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (mType == 0){
            Holder1(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_add_fans_eacher, parent, false))
        }else{
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_overall_join_groups, parent, false))
        }
    }

    override fun getItemCount(): Int = mFanslist.size

//    override fun getItemCount(): Int = if (mType ==  0){
//        mFanslist.size
//    }else{
//        mGrouplist.size
//    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mType == 0){
            if (holder is Holder1){
                holder.bind(mFanslist[position])
            }
        }else{
            if (holder is Holder){
                holder.bind(mFanslist[position])
            }
        }
    }

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView){

        private var titleBg: ShapeCornerBgView = itemView.findViewById(R.id.sOverallJoinGroupBgView)
        private var top: ImageView = itemView.findViewById(R.id.sOverallJoinGroupTopIv)
        private var title: TextView = itemView.findViewById(R.id.sOverallJoinGroupTitleTv)
        private var content: TextView = itemView.findViewById(R.id.sOverallJoinGroupContentTv)
        fun bind(entity: OverallCardEntity){
            if(adapterPosition == 0){
                titleBg.setBgColor(Color.GREEN)
                title.setTextColor(Color.GREEN)
                top.visibility = View.VISIBLE
            }else if (adapterPosition == 1 || adapterPosition == 2){
                titleBg.setBgColor(Color.RED)
                title.setTextColor(Color.RED)
                top.visibility = View.VISIBLE
                titleBg.setBgColor(R.color.inviteRed)
                if (adapterPosition == 2){
                    titleBg.setBgColor(Color.BLUE)
                    title.setTextColor(Color.BLUE)
                }
            }else{
                titleBg.setBgColor(Color.YELLOW)
                top.visibility = View.GONE
                title.setTextColor(Color.BLACK)
            }
            title.text = entity.wxnickname
            content.text = entity.content
        }
    }
    class Holder1(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private var avatar: ImageView = itemView.findViewById(R.id.sOverallAddFansEachOtherAvatarIv)//头像
        private var nickName: TextView = itemView.findViewById(R.id.sOverallAddFansEachOtherNickNameTv)//昵称
//        private var idTv: TextView = itemView.findViewById(R.id.sOverallAddFansEachOtherIdTv)//id
        private var content: TextView = itemView.findViewById(R.id.sOverallAddFansEachOtherContentTv)
        init {
            itemView.findViewById<ShapeCornerBgView>(R.id.sOverallMakeFriendsBgView).setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v?.id){
                R.id.sOverallMakeFriendsBgView ->{
                    Toast.makeText(itemView.context, "加不加", Toast.LENGTH_SHORT).show()
                }
            }
        }
        fun bind(entity: OverallCardEntity){
            content.text = entity.content
            GlideUtil.display(itemView.context, entity.headimgurl, avatar)
            nickName.text = entity.wxnickname
//            idTv.text = entity.userId
        }
    }
}
