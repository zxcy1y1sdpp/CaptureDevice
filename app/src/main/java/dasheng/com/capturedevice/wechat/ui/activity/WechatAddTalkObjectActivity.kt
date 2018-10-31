package dasheng.com.capturedevice.wechat.ui.activity

import android.content.Intent
import android.view.View
import dasheng.com.capturedevice.R
import dasheng.com.capturedevice.base.BaseWechatActivity
import dasheng.com.capturedevice.constant.RandomUtil
import dasheng.com.capturedevice.constant.RandomUtil.*
import dasheng.com.capturedevice.constant.RequestCode
import dasheng.com.capturedevice.util.GlideUtil
import dasheng.com.capturedevice.widget.dialog.BottomDialog
import dasheng.com.capturedevice.widget.dialog.BottomDialog.ItemSelection
import kotlinx.android.synthetic.main.activity_wechat_add_talk_object.*
import dasheng.com.capturedevice.database.DatabaseUtils
import dasheng.com.capturedevice.database.table.WechatUserTable
import dasheng.com.capturedevice.util.LaunchUtil


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
    private lateinit var mUserEntity: WechatUserTable
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
                mUserEntity.wechatUserNickName = mNickNameEt.text.toString()
            }
            R.id.mWechatAddObject ->{
                var userid = mMyOpenHelper?.saveWechatMsg(this, mUserEntity)
                mUserEntity.wechatUserId = userid.toString()
                mMyOpenHelper?.createSingleTalkTable(mUserEntity.wechatUserId)
                LaunchUtil.startWechatSingleChatActivity(this, mUserEntity)
                finish()
            }
        }
    }
    override fun firstClick() {
        mRandomType = true
        mUserEntity.resAvatar = RandomUtil.getRandomAvatar()
        mUserEntity.wechatUserAvatar = ""//清除其他的头像存储数据
        GlideUtil.display(this, mUserEntity.resAvatar, mUserAvatarIv)
    }

    override fun secondClick() {
        mRandomType = false
        callAlbum(1, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.CROP_IMAGE ->{
                    mUserEntity.resAvatar = -1
                    mUserEntity.wechatUserAvatar = mFinalPicUri.toString()
//                    var path = FileUtil.getFilePathByUri(this, mFinalPicUri)
//                    val newFile = File(path) //转换为File
                    GlideUtil.display(this, mFinalCropFile, mUserAvatarIv)
                }
            }
        }
    }
}
