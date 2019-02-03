package app.jietuqi.cn.widget.dialog

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import android.widget.Toast
import app.jietuqi.cn.R
import app.jietuqi.cn.util.StringUtils
import app.jietuqi.cn.util.UserOperateUtil
import app.jietuqi.cn.widget.QRCodeDialog
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.tencent.qzone.QZone
import cn.sharesdk.wechat.friends.Wechat
import cn.sharesdk.wechat.moments.WechatMoments
import com.bm.zlzq.utils.ScreenUtil
import com.xinlan.imageeditlibrary.ToastUtils
import kotlinx.android.synthetic.main.dialog_invite.*
import java.util.*

/**
 * 作者： liuyuanbo on 2018/10/25 13:34.
 * 时间： 2018/10/25 13:34
 * 邮箱： 972383753@qq.com
 * 用途： 邀请的分享弹框
 */

class InviteDialog : DialogFragment(), View.OnClickListener, PlatformActionListener {
    override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
        when(platform.name){
            Wechat.NAME -> Toast.makeText(activity, "微信分享成功", Toast.LENGTH_LONG).show()
            WechatMoments.NAME -> Toast.makeText(activity, "朋友圈分享成功", Toast.LENGTH_LONG).show()
            QQ.NAME -> Toast.makeText(activity, "QQ分享成功", Toast.LENGTH_LONG).show()
            QZone.NAME -> Toast.makeText(activity, "QQ空间分享成功", Toast.LENGTH_LONG).show()
            else -> {}
        }
    }

    override fun onError(platform: Platform, i: Int, throwable: Throwable) {
        when(platform.name){
            Wechat.NAME -> Toast.makeText(activity, "微信分享失败", Toast.LENGTH_LONG).show()
            WechatMoments.NAME -> Toast.makeText(activity, "朋友圈分享失败", Toast.LENGTH_LONG).show()
            QQ.NAME -> Toast.makeText(activity, "QQ分享失败", Toast.LENGTH_LONG).show()
            QZone.NAME -> Toast.makeText(activity, "QQ空间分享失败", Toast.LENGTH_LONG).show()
            else -> {}
        }
    }

    override fun onCancel(platform: Platform, i: Int) {
        when(platform.name){
            Wechat.NAME -> Toast.makeText(activity, "微信分享取消", Toast.LENGTH_LONG).show()
            WechatMoments.NAME -> Toast.makeText(activity, "朋友圈分享取消", Toast.LENGTH_LONG).show()
            QQ.NAME -> Toast.makeText(activity, "QQ分享取消", Toast.LENGTH_LONG).show()
            QZone.NAME -> Toast.makeText(activity, "QQ空间分享取消", Toast.LENGTH_LONG).show()
            else -> {}
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//注意此处
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog?.window?.attributes?.windowAnimations = R.style.ChangeRoleDialog
        return inflater.inflate(R.layout.dialog_invite, container, false)
    }

    override fun onStart() {
        super.onStart()
        var screenWidth = activity?.let { ScreenUtil.getScreenWidth(it) }
        val width = screenWidth?.div(4)?.times(3)
        var window = dialog.window
        var params = window?.attributes
        params?.gravity = Gravity.CENTER_HORIZONTAL
        width?.let { params?.width = it }
        window?.attributes = params
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mShareWechatMomentsTv.setOnClickListener(this)
        mShareWechatFriendsTv.setOnClickListener(this)
        mShareQQFriendsTv.setOnClickListener(this)
        mShareQZoneFriendsTv.setOnClickListener(this)
        mCopyPathTv.setOnClickListener(this)
        mScanTv.setOnClickListener(this)
        mShareNumberBtn.text = StringUtils.insertFrontAndBack(UserOperateUtil.getShareNumber(), "已邀请", "人")
    }
    override fun onClick(v: View?) {
//        dialog.dismiss()
        when(v?.id) {
            R.id.mShareWechatMomentsTv -> {
                val logo = BitmapFactory.decodeResource(resources, R.mipmap.app_icon)
                // 分享到朋友圈
                val params = Platform.ShareParams()
                params.title = resources.getString(R.string.invite_title)
                params.url = "http://www.jietuqi.cn?uid=" + UserOperateUtil.getUserId()
                params.imageData = logo
                params.shareType = Platform.SHARE_WEBPAGE //非常重要:一定要设置分享属性
                //3、非常重要:获取平台对象
                val wechat = ShareSDK.getPlatform(WechatMoments.NAME)
                wechat.platformActionListener = this // 设置分享事件回调
                // 执行分享
                wechat.share(params)
            }
            R.id.mShareWechatFriendsTv -> {
                val params = Platform.ShareParams()
                val logo = BitmapFactory.decodeResource(resources, R.mipmap.app_icon)
                params.shareType = Platform.SHARE_WEBPAGE
                params.imageData = logo
                params.text = resources.getString(R.string.invite_title)
                params.title = "微商必备的营销神器"
                params.url = "http://www.jietuqi.cn?uid=" + UserOperateUtil.getUserId()
                val wechat = ShareSDK.getPlatform(Wechat.NAME)
                wechat.platformActionListener = this
                wechat.share(params)
            }
            R.id.mShareQQFriendsTv -> {
                val params = Platform.ShareParams()
                params.title = "微商必备的营销神器"
                params.shareType = Platform.SHARE_WEBPAGE
                params.text = resources.getString(R.string.invite_title)
                params.titleUrl = "http://www.jietuqi.cn?uid=" + UserOperateUtil.getUserId()
                val wechat = ShareSDK.getPlatform(QQ.NAME)
                wechat.platformActionListener = this
                wechat.share(params)
            }
            R.id.mShareQZoneFriendsTv -> {
                val params = Platform.ShareParams()
                params.shareType = Platform.SHARE_WEBPAGE
                params.text = resources.getString(R.string.invite_title)
                params.title = "微商必备的营销神器"
                params.titleUrl = "http://www.jietuqi.cn?uid=" + UserOperateUtil.getUserId()
                val wechat = ShareSDK.getPlatform(QZone.NAME)
                wechat.platformActionListener = this
                wechat.share(params)
            }
            R.id.mCopyPathTv -> {
                val cm = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                // 创建普通字符型ClipData
                val mClipData = ClipData.newPlainText("Label", "http://www.jietuqi.cn?uid=" + UserOperateUtil.getUserId())
                // 将ClipData内容放到系统剪贴板里。
                cm.primaryClip = mClipData
                ToastUtils.showShort(activity, "链接已复制")
                dismiss()
            }
            R.id.mScanTv -> {
                val width = context?.let { ScreenUtil.getScreenWidth(it) }?.div(3)
                val qr = width?.let { QRCodeEncoder.syncEncodeQRCode("http://www.jietuqi.cn?uid=" + UserOperateUtil.getUserId(), it) }
                var dialog = QRCodeDialog()
                dialog.show(activity?.supportFragmentManager, "invite")
                qr?.let { dialog.setData(it) }
                dismiss()
            }
        }
    }
}
