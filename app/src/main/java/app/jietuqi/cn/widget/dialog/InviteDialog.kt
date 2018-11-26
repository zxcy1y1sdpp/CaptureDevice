package app.jietuqi.cn.widget.dialog

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import app.jietuqi.cn.R
import cn.sharesdk.framework.Platform
import cn.sharesdk.framework.PlatformActionListener
import cn.sharesdk.framework.ShareSDK
import cn.sharesdk.tencent.qq.QQ
import cn.sharesdk.wechat.friends.Wechat
import cn.sharesdk.wechat.moments.WechatMoments
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
//        async(CommonPool){
//            runOnUiThread {
                when(platform.name){
//                    SinaWeibo.NAME -> Toast.makeText(this@BaseActivityWithShare, "微博分享成功", Toast.LENGTH_LONG).show()
                    Wechat.NAME -> Toast.makeText(activity, "微信分享成功", Toast.LENGTH_LONG).show()
                    WechatMoments.NAME -> Toast.makeText(activity, "朋友圈分享成功", Toast.LENGTH_LONG).show()
                    QQ.NAME -> Toast.makeText(activity, "QQ分享成功", Toast.LENGTH_LONG).show()
                    else -> {}
                }
            }
//        }
//    }

    override fun onError(platform: Platform, i: Int, throwable: Throwable) {
        Toast.makeText(activity, "分享失败", Toast.LENGTH_LONG).show()
//        async(CommonPool){
//            runOnUiThread {
//                Toast.makeText(activity, "分享失败", Toast.LENGTH_LONG).show()
//            }
//        }
    }

    override fun onCancel(platform: Platform, i: Int) {
        Toast.makeText(activity, "分享取消", Toast.LENGTH_LONG).show()
//        async(CommonPool){
//            runOnUiThread {
//                Toast.makeText(this@BaseActivityWithShare, "分享取消", Toast.LENGTH_LONG).show()
//            }
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window.attributes.windowAnimations = R.style.ChangeRoleDialog
        return inflater.inflate(R.layout.dialog_invite, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mShareWechatMomentsTv.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.mShareWechatMomentsTv ->{
                val params = Platform.ShareParams()
                val logo = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
                params.shareType = Platform.SHARE_WEBPAGE
                params.imageData = logo
                params.text = "微商都在用的营销神器！拥有微商截图、加粉加群、一键转发、微商货源等多种功能."
                params.title = "微商必备的营销神器"
                params.url = "http://www.mob.com/"
                val wechat = ShareSDK.getPlatform(Wechat.NAME)
                wechat.platformActionListener = object : PlatformActionListener {
                    override fun onComplete(platform: Platform, i: Int, hashMap: HashMap<String, Any>) {
                        Log.e("shareSDK", "onComplete")
                    }

                    override fun onError(platform: Platform, i: Int, throwable: Throwable) {
                        Log.e("shareSDK", "onError")

                    }

                    override fun onCancel(platform: Platform, i: Int) {
                        Log.e("shareSDK", "onCancel")

                    }
                }
                wechat.share(params)
//                // 分享到朋友圈
//                val paramsPYQ = Platform.ShareParams()
//                paramsPYQ.text = null
//                paramsPYQ.title = null
//                paramsPYQ.titleUrl = null
//                paramsPYQ.imagePath = "http://tupian.qqjay.com/u/2017/1118/1_162252_2.jpg"
//                paramsPYQ.shareType = Platform.SHARE_IMAGE //非常重要:一定要设置分享属性
//                //3、非常重要:获取平台对象
//                val wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME)
//                wechatMoments.platformActionListener = this // 设置分享事件回调
//                // 执行分享
//                wechatMoments.share(paramsPYQ)
            }
        }
    }
}
