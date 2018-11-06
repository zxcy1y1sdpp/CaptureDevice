package dasheng.com.capturedevice.ui.adapter

import android.support.design.widget.TabLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.youth.banner.Banner
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.util.LaunchUtil
import dasheng.com.capturedevice.ui.activity.WechatSimulatorActivity

/**
 * 作者： liuyuanbo on 2018/10/24 15:46.
 * 时间： 2018/10/24 15:46
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE1 = 0
    private val TYPE2 = 1
    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE1
            else -> TYPE2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE1 -> Holder1(LayoutInflater.from(parent.context).inflate(R.layout.item_home_1, parent, false))
            else -> Holder2(LayoutInflater.from(parent.context).inflate(R.layout.item_home_2, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            TYPE1 ->{}
            TYPE2 ->{}
        }
    }

    override fun getItemCount(): Int {
        return 2
    }

    internal inner class Holder1(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        override fun onClick(v: View?) {
            when(v?.id){
                R.id.wechatFunLayout ->{
                    LaunchUtil.launch(itemView.context, WechatSimulatorActivity::class.java)
                }
                R.id.alipayFunLayout ->{
                    LaunchUtil.launch(itemView.context, WechatSimulatorActivity::class.java)
                }
            }

        }

        private var banner: Banner = itemView.findViewById(R.id.mBanner)
        private var tabLayout: TabLayout = itemView.findViewById(R.id.mTabLayout)
        init {
            itemView.findViewById<LinearLayout>(R.id.wechatFunLayout).setOnClickListener(this)
            tabLayout.addTab(tabLayout.newTab().setText("最新"))
            tabLayout.addTab(tabLayout.newTab().setText("最热"))
            tabLayout.addTab(tabLayout.newTab().setText("已购买"))

        }
        fun bind(){

        }
    }
    internal inner class Holder2(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(){

        }
    }


}
