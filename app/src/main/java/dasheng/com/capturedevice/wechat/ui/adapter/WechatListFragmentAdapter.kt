package dasheng.com.capturedevice.wechat.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.database.table.WechatUserTable
import dasheng.com.capturedevice.util.GlideUtil
import dasheng.com.capturedevice.util.WechatTimeUtil

/**
 * 作者： liuyuanbo on 2018/10/10 10:07.
 * 时间： 2018/10/10 10:07
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class WechatListFragmentAdapter(val mList: MutableList<WechatUserTable>) : RecyclerView.Adapter<WechatListFragmentAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_fragment_wechat_list, parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount() = mList.size
    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val avatar: ImageView = itemView.findViewById(R.id.avatarIv)
        private val nick: TextView = itemView.findViewById(R.id.nickNameTv)
        private val lastMsg: TextView = itemView.findViewById(R.id.lastMsgTv)
        private val time: TextView = itemView.findViewById(R.id.timeTv)
        fun bind(entity: WechatUserTable){
            GlideUtil.display(itemView.context, entity, avatar)
            nick.text = entity.wechatUserNickName
            lastMsg.text = entity.msg
            var timeStr = WechatTimeUtil.getNewChatTime(entity.lastTime)
//            val date = Date(entity.lastTime)
//            //使用日期格式化类完成日期到格式化字符串的输出
//            val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SS")
            time.text = timeStr
        }
    }
}

