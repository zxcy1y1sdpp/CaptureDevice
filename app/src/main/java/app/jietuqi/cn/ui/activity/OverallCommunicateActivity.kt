package app.jietuqi.cn.ui.activity

import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.ui.adapter.OverallCommunicateAdapter
import app.jietuqi.cn.ui.entity.OverallCommunicateEntity
import app.jietuqi.cn.widget.ninegrid.ImageInfo
import kotlinx.android.synthetic.main.activity_overall_communicate.*

/**
 * 作者： liuyuanbo on 2018/11/7 14:37.
 * 时间： 2018/11/7 14:37
 * 邮箱： 972383753@qq.com
 * 用途： 评论列表
 */

class OverallCommunicateActivity : BaseOverallInternetActivity() {
    private var mAdapter: OverallCommunicateAdapter? = null
    private var mList: ArrayList<OverallCommunicateEntity> = arrayListOf()
    override fun setLayoutResourceId() = R.layout.activity_overall_communicate

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        var entity: OverallCommunicateEntity
        var info: ImageInfo
        for (i in 0..15) {
            entity = OverallCommunicateEntity()
            entity.id = i
            entity.avatarInt = RandomUtil.getRandomAvatar()
            entity.avatar = RandomUtil.getRandomNetPics()
            entity.content = RandomUtil.getRandomContents()
            entity.nickName = RandomUtil.getRandomNickName()
            entity.createTime = 1541745870000
            if (i %2 == 0){
                entity.like = true
                entity.likeCount = (i * 2).toString()
                entity.pingLunCount = (i * 8).toString()
            }else{
                entity.like = false
                entity.likeCount = (i * 3).toString()
                entity.pingLunCount = (i * 10).toString()
            }

            when (i) {
                0 -> {
                    var infoList: ArrayList<ImageInfo> = arrayListOf()
                    info = ImageInfo()
                    val url = RandomUtil.randomNetPics[i]
                    info.bigImageUrl = url
                    info.thumbnailUrl = url
                    info.imageViewHeight = RandomUtil.randomNetPicsHeight[i]
                    info.imageViewWidth = RandomUtil.randomNetPicsWidth[i]
                    info.whPercentage = (info.imageViewHeight / info.imageViewHeight).toFloat()
                    infoList.add(info)
                    entity.pics = infoList
                }
                1 -> {
                    var infoList: ArrayList<ImageInfo> = arrayListOf()
                    for (j in 0..1) {
                        info = ImageInfo()
                        val url = RandomUtil.getRandomNetPics()
                        info.bigImageUrl = url
                        info.thumbnailUrl = url
                        info.imageViewHeight = RandomUtil.randomNetPicsHeight[i]
                        info.imageViewWidth = RandomUtil.randomNetPicsWidth[i]
                        info.whPercentage = (info.imageViewHeight / info.imageViewHeight).toFloat()
                        infoList.add(info)
                        entity.pics = infoList
                    }
                }
                2 -> {
                    var infoList: ArrayList<ImageInfo> = arrayListOf()
                    for (j in 0..2) {
                        info = ImageInfo()
                        val url = RandomUtil.getRandomNetPics()
                        info.bigImageUrl = url
                        info.thumbnailUrl = url
                        info.imageViewHeight = RandomUtil.randomNetPicsHeight[i]
                        info.imageViewWidth = RandomUtil.randomNetPicsWidth[i]
                        info.whPercentage = (info.imageViewHeight / info.imageViewHeight).toFloat()
                        infoList.add(info)
                        entity.pics = infoList
                    }
                }
                3 -> {
                    var infoList: ArrayList<ImageInfo> = arrayListOf()
                    for (j in 0..3) {
                        info = ImageInfo()
                        val url = RandomUtil.getRandomNetPics()
                        info.bigImageUrl = url
                        info.thumbnailUrl = url
                        info.imageViewHeight = RandomUtil.randomNetPicsHeight[i]
                        info.imageViewWidth = RandomUtil.randomNetPicsWidth[i]
                        info.whPercentage = (info.imageViewHeight / info.imageViewHeight).toFloat()
                        infoList.add(info)
                        entity.pics = infoList
                    }
                }
                4 -> {
                    var infoList: ArrayList<ImageInfo> = arrayListOf()
                    for (j in 0..4) {
                        info = ImageInfo()
                        val url = RandomUtil.getRandomNetPics()
                        info.bigImageUrl = url
                        info.thumbnailUrl = url
                        info.imageViewHeight = RandomUtil.randomNetPicsHeight[i]
                        info.imageViewWidth = RandomUtil.randomNetPicsWidth[i]
                        info.whPercentage = (info.imageViewHeight / info.imageViewHeight).toFloat()
                        infoList.add(info)
                        entity.pics = infoList
                    }
                }
                5 -> {
                    var infoList: ArrayList<ImageInfo> = arrayListOf()
                    for (j in 0..5) {
                        info = ImageInfo()
                        val url = RandomUtil.getRandomNetPics()
                        info.bigImageUrl = url
                        info.thumbnailUrl = url
                        info.imageViewHeight = RandomUtil.randomNetPicsHeight[i]
                        info.imageViewWidth = RandomUtil.randomNetPicsWidth[i]
                        info.whPercentage = (info.imageViewHeight / info.imageViewHeight).toFloat()
                        infoList.add(info)
                        entity.pics = infoList
                    }
                }
                6 -> {
                    var infoList: ArrayList<ImageInfo> = arrayListOf()
                    for (j in 0..6) {
                        info = ImageInfo()
                        val url = RandomUtil.getRandomNetPics()
                        info.bigImageUrl = url
                        info.thumbnailUrl = url
                        info.imageViewHeight = RandomUtil.randomNetPicsHeight[i]
                        info.imageViewWidth = RandomUtil.randomNetPicsWidth[i]
                        info.whPercentage = (info.imageViewHeight / info.imageViewHeight).toFloat()
                        infoList.add(info)
                        entity.pics = infoList
                    }
                }
                7 -> {
                    var infoList: ArrayList<ImageInfo> = arrayListOf()
                    for (j in 0..7) {
                        info = ImageInfo()
                        val url = RandomUtil.getRandomNetPics()
                        info.bigImageUrl = url
                        info.thumbnailUrl = url
                        info.imageViewHeight = RandomUtil.randomNetPicsHeight[i]
                        info.imageViewWidth = RandomUtil.randomNetPicsWidth[i]
                        info.whPercentage = (info.imageViewHeight / info.imageViewHeight).toFloat()
                        infoList.add(info)
                        entity.pics = infoList
                    }
                }
                8 -> {
                    var infoList: ArrayList<ImageInfo> = arrayListOf()
                    for (j in 0..8) {
                        info = ImageInfo()
                        val url = RandomUtil.getRandomNetPics()
                        info.bigImageUrl = url
                        info.thumbnailUrl = url
                        info.imageViewHeight = RandomUtil.randomNetPicsHeight[i]
                        info.imageViewWidth = RandomUtil.randomNetPicsWidth[i]
                        info.whPercentage = (info.imageViewHeight / info.imageViewHeight).toFloat()
                        infoList.add(info)
                        entity.pics = infoList
                    }
                }
                else -> {
                    var infoList: ArrayList<ImageInfo> = arrayListOf()
                    info = ImageInfo()
                    val url = RandomUtil.randomNetPics[i]
                    info.bigImageUrl = url
                    info.thumbnailUrl = url
                    info.imageViewHeight = RandomUtil.randomNetPicsHeight[i]
                    info.imageViewWidth = RandomUtil.randomNetPicsWidth[i]
                    info.whPercentage = info.imageViewWidth.toFloat() / info.imageViewHeight
                    infoList.add(info)
                    entity.pics = infoList
                }
            }
            mList.add(entity)
        }
        mAdapter = OverallCommunicateAdapter(mList)
        mOverallCommunicateRecyclerView.adapter = mAdapter
    }

    override fun initViewsListener() {

    }
}
