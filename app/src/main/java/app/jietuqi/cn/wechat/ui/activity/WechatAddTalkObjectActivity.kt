package app.jietuqi.cn.wechat.ui.activity

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.constant.RandomUtil.getRandomNickName
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.database.DatabaseUtils
import app.jietuqi.cn.database.table.WechatUserTable
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.util.LaunchUtil
import app.jietuqi.cn.widget.dialog.BottomDialog
import app.jietuqi.cn.widget.dialog.BottomDialog.ItemSelection
import kotlinx.android.synthetic.main.activity_wechat_add_talk_object.*


/**
 * 作者： liuyuanbo on 2018/10/13 21:43.
 * 时间： 2018/10/13 21:43
 * 邮箱： 972383753@qq.com
 * 用途： 添加微信聊天对象的页面
 */

class WechatAddTalkObjectActivity : BaseWechatActivity(), ItemSelection {
    /**
     * 是随机的头像 -- true
     * 相册选择的头像 -- false
     */
    var mRandomType = true
    /**
     * 最后需要保存的对象
     */
    private var mUserEntity: WechatUserTable? = null
    val mMyOpenHelper = DatabaseUtils.getHelper()
    override fun setLayoutResourceId() = R.layout.activity_wechat_add_talk_object

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        mNickNameEt.setText(getRandomNickName())
        var avatar: Int = RandomUtil.getRandomAvatar()
        mUserEntity = WechatUserTable(avatar, mNickNameEt.text.toString())
        mUserAvatarIv.setImageResource(avatar)
    }

    override fun initViewsListener() {
        mSelectAvatarLayout.setOnClickListener(this)
        mRefreshIv.setOnClickListener(this)
        mWechatAddObject.setOnClickListener(this)
//
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mSelectAvatarLayout ->{
                var dialog = BottomDialog()
                dialog.setItemSelectionListener(this)
                dialog.show(supportFragmentManager, "Dialog")
            }
            R.id.mRefreshIv ->{
                mNickNameEt.setText(getRandomNickName())
                mUserEntity?.wechatUserNickName = mNickNameEt.text.toString()
            }
            R.id.mWechatAddObject ->{
                var userid = mMyOpenHelper?.saveWechatMsg(this, mUserEntity)
                mUserEntity?.wechatUserId = userid.toString()
                mMyOpenHelper?.createSingleTalkTable(mUserEntity?.wechatUserId)
                LaunchUtil.startWechatSingleChatActivity(this, mUserEntity)
                finish()
            }
        }
    }
    override fun firstClick() {
        mRandomType = true
        mUserEntity?.resAvatar = RandomUtil.getRandomAvatar()
        mUserEntity?.wechatUserAvatar = ""//清除其他的头像存储数据
        mUserEntity?.resAvatar?.let { GlideUtil.display(this, it, mUserAvatarIv) }

    }

    override fun secondClick() {
        mRandomType = false
        callAlbum(needCrop = true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.CROP_IMAGE ->{
                    mUserEntity?.resAvatar = -1
                    mUserEntity?.wechatUserAvatar = mFinalPicUri.toString()
//                    var path = FileUtil.getFilePathByUri(this, mFinalPicUri)
//                    val newFile = File(path) //转换为File
                    GlideUtil.display(this, mFinalCropFile, mUserAvatarIv)
                }
            }
        }
    }
}
