package app.jietuqi.cn

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import app.jietuqi.cn.base.wechat.BaseWechatWithDbActivity
import app.jietuqi.cn.callback.OnRecyclerItemClickListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.wechatscreenshot.adapter.RoleLibraryAdapter
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.util.OtherUtil.PinyinComparator
import app.jietuqi.cn.util.SharedPreferencesUtils
import kotlinx.android.synthetic.main.activity_roleoflibrary.*
import java.util.*

/**
 * 作者： liuyuanbo on 2018/11/29 12:23.
 * 时间： 2018/11/29 12:23
 * 邮箱： 972383753@qq.com
 * 用途：
 */
class RoleOfLibraryActivity : BaseWechatWithDbActivity() {
    private val mHelper = RoleLibraryHelper(this)
    private val mList: ArrayList<WechatUserEntity> = arrayListOf()
    private lateinit var mAdapter: RoleLibraryAdapter
    private var mRequestCode = -1
    override fun setLayoutResourceId() = R.layout.activity_roleoflibrary

    override fun needLoadingView() = false

    override fun initAllViews() {
        setWechatViewTitle("角色库")
        mAdapter = RoleLibraryAdapter(mList)
        mRoleOfLibraryRecyclerView.adapter = mAdapter
        mRoleLetter.setTextView(mRoleLetterTv)
        var list = mHelper.query()
        mList.addAll(list)
        comparePinyin()
        mAdapter.notifyDataSetChanged()
    }

    private fun comparePinyin(){
        val compare = PinyinComparator()
        return Collections.sort(mList, compare)
    }
    override fun initViewsListener() {
        mRoleOfLibraryRecyclerView.addOnItemTouchListener(object : OnRecyclerItemClickListener(mRoleOfLibraryRecyclerView) {
            override fun onItemClick(vh: RecyclerView.ViewHolder) {
                val entity = mList[vh.adapterPosition]
                intent.putExtra(IntentKey.ENTITY, entity)
                setResult(mRequestCode, intent)
                if (mRequestCode == RequestCode.MY_SIDE){
                    SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.MY_SELF)
                }else{
                    SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.OTHER_SIDE)
                }
                finish()
            }
            override fun onItemLongClick(vh: RecyclerView.ViewHolder) {}
        })
        mRoleLetter.setOnTouchingLetterChangedListener { s ->
            if (s == "↑" || s == "☆"){
                mRoleOfLibraryRecyclerView.scrollToPosition(0)
            }else{
                val position = mAdapter.getPositionForSection(s[0].toInt())
                if (position != -1) {
                    mRoleOfLibraryRecyclerView.scrollToPosition(position)
                    //指定第一个可见位置的item
                    var mLayoutManager = mRoleOfLibraryRecyclerView.layoutManager as LinearLayoutManager
                    mLayoutManager.scrollToPositionWithOffset(position, 0)
                }
            }
        }
    }

    override fun getAttribute(intent: Intent) {
        super.getAttribute(intent)
        mRequestCode = intent.getIntExtra(IntentKey.REQUEST_CODE, -1)
    }
}
