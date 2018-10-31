package dasheng.com.capturedevice.ui.fragment

import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseFragment
import dasheng.com.capturedevice.ui.adapter.HomeAdapter
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * 作者： liuyuanbo on 2018/10/23 17:27.
 * 时间： 2018/10/23 17:27
 * 邮箱： 972383753@qq.com
 * 用途：
 */

class HomeFragment : BaseFragment() {
    private var mAdapter: HomeAdapter? = null
    override fun setLayoutResouceId() = R.layout.fragment_home

    override fun initAllViews() {
        setTitle("微商截图神器", 1)
        mAdapter = HomeAdapter()
        mRecyclerView.adapter = mAdapter
    }

    override fun initViewsListener() {
        /*mWechatBtn.setOnClickListener {
            LaunchUtil.launch(activity, WechatSimulatorActivity::class.java)
        }
        mAlipayBtn.setOnClickListener {
            var intent: Intent = Intent(activity, WechatSendRedPacketActivity::class.java)
            startActivity(intent)
        }
        mQQBtn.setOnClickListener {
            var intent: Intent = Intent(activity, WechatSendRedPacketActivity::class.java)
            startActivity(intent)
        }*/
    }
}
