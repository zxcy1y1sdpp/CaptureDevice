package app.jietuqi.cn.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.widget.DefaultItemAnimator
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseOverallInternetActivity
import app.jietuqi.cn.callback.LikeListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.ui.adapter.OverallCommunicateDetailsAdapter
import app.jietuqi.cn.ui.entity.OverallDynamicEntity
import app.jietuqi.cn.util.EventBusUtil
import app.jietuqi.cn.util.TimeUtil
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.SoftKeyBoardListener
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import kotlinx.android.synthetic.main.activity_overall_communicate_details.*

/**
 * 作者： liuyuanbo on 2018/11/7 14:37.
 * 时间： 2018/11/7 14:37
 * 邮箱： 972383753@qq.com
 * 用途： 评论详情列表
 */

class OverallCommunicateDetailsActivity : BaseOverallInternetActivity(), LikeListener {

    override fun like(overallDynamicEntity: OverallDynamicEntity?, comment: OverallDynamicEntity.Comment?, type: Int) {
        if (type == 0){//文章点赞
            overallDynamicEntity?.let { likeAndUnLike(it) }
        }else{//评论点赞
            comment?.let { likeAndUnLike(it) }
        }
    }

    private var mAdapter: OverallCommunicateDetailsAdapter? = null
    private var mList: ArrayList<OverallDynamicEntity.Comment> = arrayListOf()
    internal var mEntity:OverallDynamicEntity = OverallDynamicEntity()
    override fun setLayoutResourceId() = R.layout.activity_overall_communicate_details

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setTitle("评论详情", 0)
        (mOverallCommunicateDetailsRecyclerView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
    }

    override fun initViewsListener() {
        mOverallCommunicateDetailsCallPinglunTv.setOnClickListener(this)
        mOverallCommunicateDetailsSendPinglunLayout.setOnClickListener(this)
        SoftKeyBoardListener.setListener(this, object : SoftKeyBoardListener.OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
                Log.e("softkeyboard", "显示")
                mOverallCommunicateDetailsPinglunLayout.visibility = View.GONE
                mOverallCommunicateDetailsSendPinglunLayout.visibility = View.VISIBLE
            }

            override fun keyBoardHide(height: Int) {
                Log.e("softkeyboard", "键盘隐藏")
                mOverallCommunicateDetailsPinglunLayout.visibility = View.VISIBLE
                mOverallCommunicateDetailsSendPinglunLayout.visibility = View.GONE
            }
        })
        mOverallCommunicateDetailsSendPingLunTv.setOnClickListener(this)
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mEntity = intent.getSerializableExtra(IntentKey.ENTITY) as OverallDynamicEntity
        mList.addAll(mEntity.comment)
        mAdapter = OverallCommunicateDetailsAdapter(mList, mEntity, this)
        mOverallCommunicateDetailsRecyclerView.adapter = mAdapter
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mOverallCommunicateDetailsCallPinglunTv ->{
                mOverallCommunicateDetailsPinglunLayout.visibility = View.GONE
                mOverallCommunicateDetailsSendPinglunLayout.visibility = View.VISIBLE
                mOverallCommunicateDetailsPingLunEt.isFocusable = true
                mOverallCommunicateDetailsPingLunEt.isFocusableInTouchMode = true
                mOverallCommunicateDetailsPingLunEt.requestFocus()
                //打开软键盘
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)    //InputMethodManager.SHOW_FORCED
            }
            R.id.mOverallCommunicateDetailsSendPingLunTv -> {
                if (UserOperateUtil.isCurrentLoginDirectlyLogin(this)) {
                    val content = mOverallCommunicateDetailsPingLunEt.text.toString().trim()
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(this@OverallCommunicateDetailsActivity, "评论不能为空", Toast.LENGTH_SHORT).show()
                        return
                    }
                    pingLun(content)
                }
            }
        }
    }
    private fun pingLun(content: String){
        EasyHttp.post(HttpConfig.INFO)
                .params("way", "comment_add")
                .params("uid", UserOperateUtil.getUserId())
                .params("content",content)
                .params("article_id", mEntity.id.toString())
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException) {}
                    override fun onSuccess(t: String) {
                        var comment: OverallDynamicEntity.Comment = OverallDynamicEntity.Comment()
                        comment.content = content
                        comment.headimgurl = UserOperateUtil.getUserAvatar()
                        comment.nickname = UserOperateUtil.getUserNickName()
                        comment.create_time = TimeUtil.getCurrentTimeEndMs()
                        mList.add(0, comment)
                        mAdapter?.notifyItemInserted(1)
                        mOverallCommunicateDetailsPingLunEt.setText("")
                        mEntity.comment_number += 1
                        mEntity.comment.add(0, comment)
                        mAdapter?.notifyItemChanged(0)
                        EventBusUtil.post(mEntity)
                    }
                })
    }
    /**
     * 点赞/取消点赞
     */
    private fun likeAndUnLike(entity: OverallDynamicEntity){
        EasyHttp.post(HttpConfig.INDEX)
                .params("way", "favour")
                .params("uid", UserOperateUtil.getUserId())
                .params("classify", "article")
                .params("info_id", entity.id.toString())
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException) {}
                    override fun onSuccess(t: String) {
                        if (entity.is_favour == 0){
                            entity.is_favour = 1
                            entity.favour += 1
                        }else{
                            entity.is_favour = 0
                            entity.favour -= 1
                        }
                        mAdapter?.notifyItemRangeChanged(0, 1)
                        EventBusUtil.post(entity)
                    }
                })
    }
    /**
     * 点赞/取消点赞
     */
    private fun likeAndUnLike(comment: OverallDynamicEntity.Comment){
        EasyHttp.post(HttpConfig.INDEX)
                .params("way", "favour")
                .params("uid", UserOperateUtil.getUserId())
                .params("classify", "comment")
                .params("info_id", comment.id.toString())
                .execute(object : SimpleCallBack<String>() {
                    override fun onError(e: ApiException) {}
                    override fun onSuccess(t: String) {
                        if (comment.is_favour == 0){
                            comment.is_favour = 1
                            comment.favour += 1
                        }else{
                            comment.is_favour = 0
                            comment.favour -= 1
                        }
                        var position = mList.indexOf(comment)
                        mAdapter?.notifyItemRangeChanged( position + 1, 1)
                        val entity: OverallDynamicEntity.Comment
                        val id = mEntity.comment.indexOf(comment)
                        entity = mEntity.comment[id]
                        entity.is_favour = comment.is_favour
                        entity.favour = comment.favour
                        EventBusUtil.post(mEntity)
                    }
                })
    }
}
