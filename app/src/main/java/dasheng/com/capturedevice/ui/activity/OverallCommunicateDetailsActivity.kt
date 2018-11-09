package dasheng.com.capturedevice.ui.activity

import android.content.Intent
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseOverallInternetActivity
import dasheng.com.capturedevice.constant.IntentKey
import dasheng.com.capturedevice.constant.RandomUtil
import dasheng.com.capturedevice.ui.adapter.OverallCommunicateDetailsAdapter
import dasheng.com.capturedevice.ui.entity.OverallCommunicateEntity
import kotlinx.android.synthetic.main.activity_overall_communicate_details.*

/**
 * 作者： liuyuanbo on 2018/11/7 14:37.
 * 时间： 2018/11/7 14:37
 * 邮箱： 972383753@qq.com
 * 用途： 评论详情列表
 */

class OverallCommunicateDetailsActivity : BaseOverallInternetActivity() {
    private lateinit var mAdapter: OverallCommunicateDetailsAdapter
    private var mList: ArrayList<OverallCommunicateEntity> = arrayListOf()
    internal var mEntity:OverallCommunicateEntity = OverallCommunicateEntity()
    override fun setLayoutResourceId() = R.layout.activity_overall_communicate_details

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {

        var entity: OverallCommunicateEntity
        for (i in 0..15) {
            entity = OverallCommunicateEntity()
            entity.id = i
//            entity.avatarInt = RandomUtil.getRandomAvatar()
            entity.avatar = RandomUtil.getRandomNetPics()
            entity.content = RandomUtil.getRandomContents()
            entity.nickName = RandomUtil.getRandomNickName()
            entity.createTime = 1541745870000
            if (i % 2 == 0){
                entity.like = true
                entity.likeCount = (i * 2).toString()
//                entity.pingLunCount = (i * 8).toString()
            }else{
                entity.like = false
                entity.likeCount = (i * 3).toString()
//                entity.pingLunCount = (i * 10).toString()
            }
            mList.add(entity)
        }
    }

    override fun initViewsListener() {
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mEntity = intent.getSerializableExtra(IntentKey.ENTITY) as OverallCommunicateEntity
        mAdapter = OverallCommunicateDetailsAdapter(mList, mEntity)
        mOverallCommunicateDetailsRecyclerView.adapter = mAdapter
    }

}
