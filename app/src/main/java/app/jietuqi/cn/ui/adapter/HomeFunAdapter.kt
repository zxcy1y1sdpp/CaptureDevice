package app.jietuqi.cn.ui.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import app.jietuqi.cn.BuildConfig
import app.jietuqi.cn.R
import app.jietuqi.cn.ui.activity.*
import app.jietuqi.cn.ui.entity.HomeFunEntity
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatScreenShotActivity
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.util.UserOperateUtil

/**
 * 作者： liuyuanbo on 2019/1/9 11:48.
 * 时间： 2019/1/9 11:48
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class HomeFunAdapter : RecyclerView.Adapter<HomeFunAdapter.Holder>() {
    private var mFunList = arrayListOf<HomeFunEntity>()
    init {
        if (BuildConfig.DEBUG){
            mFunList.add(HomeFunEntity("微商截图", R.drawable.wsjt, true))
            mFunList.add(HomeFunEntity("加粉加群", R.drawable.jfjq, false))
            mFunList.add(HomeFunEntity("项目市场", R.drawable.xmsc, false))
            mFunList.add(HomeFunEntity("视频去水印", R.drawable.spqsy, false))
            mFunList.add(HomeFunEntity("二维码", R.drawable.ewm, false))
            mFunList.add(HomeFunEntity("图片编辑", R.drawable.tpbj, false))
            mFunList.add(HomeFunEntity("快递查询", R.drawable.kdcx, false))

        }else{
            if (UserOperateUtil.needColseByChannel()){
                mFunList.add(HomeFunEntity("微商截图", R.drawable.wsjt, true))
                if (!UserOperateUtil.isVivoChannel() && !UserOperateUtil.isBaiduChannel()
                        && !UserOperateUtil.is360Channel() && !UserOperateUtil.isHuaweiChannel()){
                    mFunList.add(HomeFunEntity("加粉加群", R.drawable.jfjq, false))
                }
                if (!UserOperateUtil.isXiaomiChannel() && !UserOperateUtil.is360Channel()){
                    mFunList.add(HomeFunEntity("视频去水印", R.drawable.spqsy, false))
                }

                mFunList.add(HomeFunEntity("二维码", R.drawable.ewm, false))
                mFunList.add(HomeFunEntity("图片编辑", R.drawable.tpbj, false))

                if (!UserOperateUtil.isXiaomiChannel()){
                    mFunList.add(HomeFunEntity("快递查询", R.drawable.kdcx, false))
                }
            }else{
                mFunList.add(HomeFunEntity("微商截图", R.drawable.wsjt, true))
                mFunList.add(HomeFunEntity("加粉加群", R.drawable.jfjq, false))
                mFunList.add(HomeFunEntity("项目市场", R.drawable.xmsc, false))
                mFunList.add(HomeFunEntity("视频去水印", R.drawable.spqsy, false))
                mFunList.add(HomeFunEntity("二维码", R.drawable.ewm, false))
                mFunList.add(HomeFunEntity("图片编辑", R.drawable.tpbj, false))
                mFunList.add(HomeFunEntity("快递查询", R.drawable.kdcx, false))
            }
        }
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(mFunList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_home_fun, parent, false))
    }

    override fun getItemCount(): Int {
        return mFunList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val funTv: TextView = itemView.findViewById(R.id.sFunTv)
        private val hotTag: ImageView = itemView.findViewById(R.id.sHotTag)
        init {
            funTv.setOnClickListener(this)
        }
        fun bind(entity: HomeFunEntity) {
            funTv.text = entity.funName
            val drawable = ContextCompat.getDrawable(itemView.context, entity.funIcon)
            // / 这一步必须要做,否则不会显示.
            drawable?.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            funTv.setCompoundDrawables(null, drawable, null, null)

            if (entity.needHot){
                hotTag.visibility = View.VISIBLE
            }else{
                hotTag.visibility = View.GONE
            }
        }
        override fun onClick(v: View?) {
            when(funTv.text){
                "微商截图" ->{
                    if (BuildConfig.DEBUG){
                        LaunchUtil.launch(itemView.context, SimulatorActivity::class.java)
                    }else{
                        if (UserOperateUtil.needColseByChannel()){
                            LaunchUtil.launch(itemView.context, WechatScreenShotActivity::class.java)
                        }else{
                            LaunchUtil.launch(itemView.context, SimulatorActivity::class.java)
                        }
                    }
                }
                "加粉加群" ->{
                    if (UserOperateUtil.isCurrentLoginDirectlyLogin(itemView.context)){
                        LaunchUtil.launch(itemView.context, OverallAddFansAndGroupsActivity::class.java)
                    }
                }
                "视频去水印" -> LaunchUtil.launch(itemView.context, OverallRemoveWatermarkActivity::class.java)
                "快递查询" -> LaunchUtil.startOverallWebViewActivity(itemView.context, "https://m.kuaidi100.com/app/?coname=dasheng", "快递查询")
                "图片编辑" -> LaunchUtil.launch(itemView.context, com.xinlan.imageeditlibrary.temporary.EditImageActivity::class.java)
                "二维码" -> LaunchUtil.launch(itemView.context, OverallCreateQRCodeActivity::class.java)
                "项目市场" -> LaunchUtil.launch(itemView.context, OverallProjectMarketActivity::class.java)
            }
        }
    }
}
