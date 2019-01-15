package app.jietuqi.cn.ui.wechatscreenshot.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.util.GlideUtil
import com.makeramen.roundedimageview.RoundedImageView

/**
 * 作者： liuyuanbo on 2018/11/29 15:19.
 * 时间： 2018/11/29 15:19
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class RoleLibraryAdapter(val mList: ArrayList<WechatUserEntity>) : RecyclerView.Adapter<RoleLibraryAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_role_library_2, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var entity = mList[position]
        holder.bind(entity)
    }

    override fun getItemCount() = mList.size

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    fun getPositionForSection(section: Int): Int {
        var entity: WechatUserEntity
        for (i in 0 until itemCount) {
            entity = mList[i]
            val sortStr = entity.pinyinNickName
            val firstChar = sortStr.toUpperCase()[0]
            if (firstChar.toInt() == section) {
                return i
            }
        }
        return -1
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

            if (adapterPosition == mList.size - 1){
                noticeTv.visibility = View.VISIBLE
            }else{
                noticeTv.visibility = View.GONE
            }
        }
    }
}
