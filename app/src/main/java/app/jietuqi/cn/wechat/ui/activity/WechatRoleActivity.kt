package app.jietuqi.cn.wechat.ui.activity

import android.content.Intent
import android.view.View
import app.jietuqi.cn.R
import app.jietuqi.cn.base.BaseWechatActivity
import app.jietuqi.cn.callback.EditDialogChoiceListener
import app.jietuqi.cn.constant.IntentKey
import app.jietuqi.cn.constant.RequestCode
import app.jietuqi.cn.database.table.WechatUserTable
import app.jietuqi.cn.entity.EditDialogEntity
import app.jietuqi.cn.util.GlideUtil
import app.jietuqi.cn.widget.dialog.EditDialog
import kotlinx.android.synthetic.main.activity_wechat_role.*

/**
 * 作者： liuyuanbo on 2018/10/18 14:24.
 * 时间： 2018/10/18 14:24
 * 邮箱： 972383753@qq.com
 * 用途： 微信 -- 修改角色
 */

class WechatRoleActivity : BaseWechatActivity(), EditDialogChoiceListener {
    override fun onChoice(entity: EditDialogEntity?) {
        mSenderNameTv.text = entity?.content
        mEntity.wechatUserNickName = entity?.content
    }

    private var mEntity: WechatUserTable = WechatUserTable()
    override fun setLayoutResourceId() = R.layout.activity_wechat_role

    override fun needLoadingView(): Boolean {
        return false
    }

    override fun initAllViews() {
        setWechatViewTitle("修改角色", 1)
    }

    override fun initViewsListener() {
        mAvatarLayout.setOnClickListener(this)
        mNickNameLayout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when(v.id){
            R.id.mAvatarLayout ->{
                callAlbum(1, true)
            }
            R.id.mNickNameLayout ->{
                val dialog = EditDialog()
                dialog.setData(this, EditDialogEntity(-1, getString(R.string.app_name), "角色昵称"))
                dialog.show(supportFragmentManager, "changeRole")
            }
            R.id.sureTv ->{
                var intent: Intent = Intent()
                intent.putExtra(IntentKey.ENTITY, mEntity)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            when(requestCode){
                RequestCode.CROP_IMAGE ->{
                    mEntity.avatar = mFinalCropFile
                    GlideUtil.display(this, mFinalCropFile, mAvatarIv)
                }
            }
        }
    }
}
