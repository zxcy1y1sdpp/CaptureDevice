package app.jietuqi.cn.callback

import app.jietuqi.cn.ui.entity.OverallDynamicEntity

/**
 * 作者： liuyuanbo on 2018/11/21 15:11.
 * 时间： 2018/11/21 15:11
 * 邮箱： 972383753@qq.com
 * 用途： 点赞/取消点赞的回调
 */
interface LikeListener{
    /**
     * @param overallDynamicEntity: 文章的实体
     * @param comment: 文章里评论的实体
     * @param type: 0 -- 文章评论，1 -- 实体评论
     */
    fun like(overallDynamicEntity: OverallDynamicEntity? = null, comment: OverallDynamicEntity.Comment? = null, type: Int = 0)
}
