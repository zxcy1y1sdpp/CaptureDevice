package app.jietuqi.cn

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.multidex.MultiDex
import android.text.TextUtils
import app.jietuqi.cn.constant.RandomUtil
import app.jietuqi.cn.constant.SharedPreferenceKey
import app.jietuqi.cn.database.DatabaseUtils
import app.jietuqi.cn.http.HttpConfig
import app.jietuqi.cn.http.util.MD5
import app.jietuqi.cn.ui.alipayscreenshot.widget.EmojiAlipayManager
import app.jietuqi.cn.ui.entity.WechatUserEntity
import app.jietuqi.cn.ui.qqscreenshot.widget.EmojiQQManager
import app.jietuqi.cn.ui.wechatscreenshot.db.RoleLibraryHelper
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity
import app.jietuqi.cn.ui.wechatscreenshot.widget.EmojiWechatManager
import app.jietuqi.cn.util.*
import app.jietuqi.cn.web.SonicRuntimeImpl
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorHelper
import app.jietuqi.cn.wechat.simulator.db.WechatSimulatorListHelper
import app.jietuqi.cn.wechat.widget.groupicon.CombineBitmap
import app.jietuqi.cn.wechat.widget.groupicon.layout.WechatLayoutManager
import app.jietuqi.cn.wechat.widget.groupicon.listener.OnProgressListener
import app.jietuqi.cn.widget.ninegrid.NineGridView
import com.mob.MobSDK
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.sonic.sdk.SonicConfig
import com.tencent.sonic.sdk.SonicEngine
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.cache.converter.SerializableDiskConverter
import com.zhouyou.http.model.HttpHeaders
import com.zhouyou.http.utils.HttpLog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * 作者： liuyuanbo on 2018/10/9 10:43.
 * 时间： 2018/10/9 10:43
 * 邮箱： 972383753@qq.com
 * 用途： 自定义的Application
 */

class App : Application() {
    init {
//设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white)//全局设置主题颜色
            MRefreshHeader(context)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        /*SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f)
        }*/
    }
    override fun onCreate() {
        super.onCreate()

        initBugly()
        // 通过代码注册你的AppKey和AppSecret
        MobSDK.init(this)
        globeHttpConfiguration()
        DatabaseUtils.initHelper(this)
        NineGridView.setImageLoader(GlideImageLoader())
        SharedPreferencesUtils.getInstance(this, SharedPreferencesUtils.SHARED_NAME)
        initTencentWeb()

        GlobalScope.launch {
            // 在一个公共线程池中创建一个协程
//            delay(5000L)
            EmojiWechatManager.init(this@App)
            EmojiAlipayManager.init(this@App)
            EmojiQQManager.init(this@App)
        }
        ResourceHelper()
        initRoleLibrary()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    private fun initTencentWeb() {
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(SonicRuntimeImpl(this), SonicConfig.Builder().build())
        }
    }
    private fun initRoleLibrary(){
        GlobalScope.launch { // 在一个公共线程池中创建一个协程
//            delay(5000L) // 非阻塞的延迟一秒（默认单位是毫秒）
            var helper = RoleLibraryHelper(this@App)
            val list = helper.query()
            if (null == list || list.size <= 0){
                var entity: WechatUserEntity
                var nickName: String
                for (i in RandomUtil.randomAvatar.indices){
                    nickName = RandomUtil.getOrderNickName(i)
                    entity = WechatUserEntity(RandomUtil.getRandomAvatarName(i),  nickName, OtherUtil.transformPinYin(nickName))
//                    entity = WechatUserEntity(RandomUtil.getOrderAvatar(i), "", nickName, OtherUtil.transformPinYin(nickName))
                    var userId = helper.save(entity)
                    entity.wechatUserId = userId.toString()
                    helper.update(this@App, entity)
                    if (i == 0){//微信截图中的“我”
                        entity.wechatNumber = "WEIXINHAO_"//设置默认的微信号
                        SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.MY_SELF)
                    }
                    if (i == 1){//微信截图中的“对方”
                        SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.OTHER_SIDE)
                    }
                    if (i == 2){//QQ截图中的“我
                        SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.QQ_ME_SELF)
                    }
                    if (i == 3){//QQ截图中的“对方”
                        SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.QQ_OTHER_SIDE)
                    }
                    if (i == 4){//支付宝截图中的“我
                        SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.ALIPAY_ME_SELF)
                    }
                    if (i == 5){//支付宝截图中的“对方”
                        SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.ALIPAY_OTHER_SIDE)
                    }
                    if (i == 6){//微信模拟器中的“我”
                        entity.wechatNumber = "WEIXINHAO_"//设置默认的微信号
                        SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.WECHAT_SIMULATOR_MY_SIDE)
                    }
                    if (i == 7){//微信模拟器中的“对方”,新手引导用的
                        var wechatMySimulator: WechatUserEntity? = UserOperateUtil.getWechatSimulatorMySelf()
                        wechatMySimulator?.let { initWechatSimulatorData1(it, entity) }
//                        wechatMySimulator?.let { initWechatSimulatorData(it, entity, listHelper) }
                    }
                    if (i == 8){//微信模拟器中的“对方”,新手引导用的
                        var wechatMySimulator: WechatUserEntity? = UserOperateUtil.getWechatSimulatorMySelf()
                        wechatMySimulator?.let { initWechatSimulatorData2(it, entity) }
//                        wechatMySimulator?.let { initWechatSimulatorData(it, entity, listHelper) }
                    }
                    if (i == 9){//微信模拟器中的“对方”,新手引导用的
                        var wechatMySimulator: WechatUserEntity? = UserOperateUtil.getWechatSimulatorMySelf()
                        wechatMySimulator?.let { initWechatSimulatorData3(it, entity) }
//                        wechatMySimulator?.let { initWechatSimulatorData(it, entity, listHelper) }
                    }
                    if (i == 10){//微信模拟器中的“对方”,新手引导用的
                        var wechatMySimulator: WechatUserEntity? = UserOperateUtil.getWechatSimulatorMySelf()
                        wechatMySimulator?.let { initWechatSimulatorData4(it, entity) }
//                        wechatMySimulator?.let { initWechatSimulatorData(it, entity, listHelper) }
                    }
                }
            }else{
                var wxMySelf: WechatUserEntity? = UserOperateUtil.getMySelf()
                if (null == wxMySelf) {
                    var entity: WechatUserEntity
                    var nickName: String
                    helper.deleteAll()
                    for (i in RandomUtil.randomAvatar.indices) {
                        nickName = RandomUtil.getOrderNickName(i)
                        entity = WechatUserEntity(RandomUtil.getRandomAvatarName(i),  nickName, OtherUtil.transformPinYin(nickName))
                        var userId = helper.save(entity)
                        entity.wechatUserId = userId.toString()
                        helper.update(this@App, entity)
                        if (i == 0) {//微信截图中的“我”
                            entity.wechatNumber = "WEIXINHAO_"//设置默认的微信号
                            SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.MY_SELF)
                        }
                        if (i == 1) {//微信截图中的“对方”
                            SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.OTHER_SIDE)
                        }
                        if (i == 2) {//QQ截图中的“我
                            SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.QQ_ME_SELF)
                        }
                        if (i == 3) {//QQ截图中的“对方”
                            SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.QQ_OTHER_SIDE)
                        }
                        if (i == 4) {//支付宝截图中的“我
                            SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.ALIPAY_ME_SELF)
                        }
                        if (i == 5) {//支付宝截图中的“对方”
                            SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.ALIPAY_OTHER_SIDE)
                        }
                        if (i == 6) {//微信模拟器中的“我”
                            entity.wechatNumber = "WEIXINHAO_"//设置默认的微信号
                            SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.WECHAT_SIMULATOR_MY_SIDE)
                        }
                        if (i == 7) {//微信模拟器中的“对方”,新手引导用的
                            var wechatMySimulator: WechatUserEntity? = UserOperateUtil.getWechatSimulatorMySelf()
                            wechatMySimulator?.let { initWechatSimulatorData1(it, entity) }
                        }
                        if (i == 8) {//微信模拟器中的“对方”,新手引导用的
                            var wechatMySimulator: WechatUserEntity? = UserOperateUtil.getWechatSimulatorMySelf()
                            wechatMySimulator?.let { initWechatSimulatorData2(it, entity) }
                        }
                        if (i == 9) {//微信模拟器中的“对方”,新手引导用的
                            var wechatMySimulator: WechatUserEntity? = UserOperateUtil.getWechatSimulatorMySelf()
                            wechatMySimulator?.let { initWechatSimulatorData3(it, entity) }
                        }
                        if (i == 10) {//微信模拟器中的“对方”,新手引导用的
                            var wechatMySimulator: WechatUserEntity? = UserOperateUtil.getWechatSimulatorMySelf()
                            wechatMySimulator?.let { initWechatSimulatorData4(it, entity) }
                        }
                    }
                }
            }
        }
    }

    /**
     * 初始化新手指引的数据
     */
    private fun initWechatSimulatorData1(mySide: WechatUserEntity, otherSide: WechatUserEntity){
        val helper = WechatSimulatorHelper(this, otherSide)
        var msgEntity = WechatScreenShotEntity()
        msgEntity.msgType = 2
        msgEntity.time = TimeUtil.getCurrentTimeEndMs()
        msgEntity.needEventBus = false
        helper.save(msgEntity)
        msgEntity = WechatScreenShotEntity()
        msgEntity.msg = "你已添加" + otherSide.wechatUserNickName + "，现在可以开始聊天了"
        msgEntity.msgType = 8
        helper.save(msgEntity)
        msgEntity = WechatScreenShotEntity()
        msgEntity.isComMsg = false
        msgEntity.msgType = 0
        msgEntity.msg = "\uD83D\uDD34APP新手必看\uD83D\uDD34\n" +
                "\n" +
                "\uD83D\uDD39切换角色\uD83D\uDD39：切换角色说话点最上面“昵称”\n" +
                "\n" +
                "\uD83D\uDD39删除消息\uD83D\uDD39：长按消息，点删除\n" +
                "\n" +
                "\uD83D\uDD39清空消息\uD83D\uDD39：点右上角的···按钮，进入新页面，点清空即可\n" +
                "\n" +
                "\uD83D\uDD39更多功能\uD83D\uDD39：点击右下角的“+”按钮。\n" +
                "\n" +
                "\uD83D\uDD39添加时间\uD83D\uDD39：点击右下角的“+”按钮，然后点击\"时间\"。"
        msgEntity.avatarInt = mySide.avatarInt
        msgEntity.avatarStr = mySide.wechatUserAvatar
        msgEntity.resourceName = mySide.resourceName
        msgEntity.wechatUserId = mySide.wechatUserId
        helper.save(msgEntity)

        msgEntity = WechatScreenShotEntity()
        msgEntity.isComMsg = true
        msgEntity.msgType = 0
        msgEntity.msg = "\uD83D\uDD34转账及红包的发和领取方法\uD83D\uDD34\n" +
                "\n" +
                "\uD83D\uDD39向对方转账(或红包)\uD83D\uDD39：点击右下角“+”，之后点击“转账(或红包)”。\n" +
                "\n" +
                "\uD83D\uDD39让对方收钱(或红包)\uD83D\uDD39：你发给对方红包或转账点最上面“昵称”切换到对方说话领取就可以了\n" +
                "\uD83D\uDD39让自己收钱(或红包)\uD83D\uDD39:   别人给你发红包或者转账，不管切换谁说话都是可以直接领取。"
        msgEntity.avatarInt = otherSide.avatarInt
        msgEntity.avatarStr = otherSide.wechatUserAvatar
        msgEntity.resourceName = otherSide.resourceName
        msgEntity.wechatUserId = otherSide.wechatUserId
        helper.save(msgEntity)

        msgEntity = WechatScreenShotEntity()
        msgEntity?.receive = false
        msgEntity.isComMsg = false
        msgEntity.msgType = 5
        msgEntity.money = "200"
        msgEntity.msg = StringUtils.insertFront(otherSide.wechatUserNickName, "转账给")
        msgEntity.transferOutTime = TimeUtil.getCurrentTimeEndMs()
        msgEntity.avatarInt = mySide.avatarInt
        msgEntity.avatarStr = mySide.wechatUserAvatar
        msgEntity.resourceName = mySide.resourceName
        msgEntity.wechatUserId = mySide.wechatUserId
        helper.save(msgEntity)

        msgEntity = WechatScreenShotEntity()
        msgEntity.isComMsg = false
        msgEntity.msgType = 0
        msgEntity.msg = "\uD83D\uDD34操作练习①\uD83D\uDD34\n" +
                "\n" +
                "\uD83D\uDD39让对方收钱\uD83D\uDD39\n" +
                "\n" +
                "▶️操作步骤：\n" +
                "\n" +
                "1️⃣1、点最上面“昵称切换聊天”；\n" +
                "\n" +
                "2️⃣2、点开转账“☆确认收钱☆”"
        msgEntity.avatarInt = mySide.avatarInt
        msgEntity.avatarStr = mySide.wechatUserAvatar
        msgEntity.resourceName = mySide.resourceName
        msgEntity.wechatUserId = mySide.wechatUserId
        helper.save(msgEntity)

        msgEntity = WechatScreenShotEntity()
        msgEntity.isComMsg = true
        msgEntity.msgType = 0
        msgEntity.msg = "\uD83D\uDD34操作练习②\uD83D\uDD34\n" +
                "\n" +
                "\uD83D\uDD39让自己收钱\uD83D\uDD39\n" +
                "\n" +
                "▶️操作步骤：\n" +
                "\n" +
                "1️⃣1、不需要切换聊天直接点\n" +
                "\n" +
                "2️⃣2、点开转账“☆确认收钱☆”"
        msgEntity.avatarInt = otherSide.avatarInt
        msgEntity.avatarStr = otherSide.wechatUserAvatar
        msgEntity.resourceName = otherSide.resourceName
        msgEntity.wechatUserId = otherSide.wechatUserId
        helper.save(msgEntity)
        val lastEntity = helper.queryLastMsg()
        if (null != lastEntity){
            val userEntity = WechatUserEntity()
            userEntity.id = otherSide.listId
            userEntity.wechatUserId = otherSide.wechatUserId
            userEntity.resourceName = otherSide.resourceName
            userEntity.avatarInt = otherSide.avatarInt
            userEntity.wechatUserAvatar = otherSide.wechatUserAvatar
            userEntity.wechatUserNickName = otherSide.wechatUserNickName
            userEntity.avatarFile = otherSide.avatarFile
            userEntity.pinyinNickName = otherSide.pinyinNickName
            userEntity.firstChar = otherSide.firstChar
            userEntity.isFirst = otherSide.isFirst
            userEntity.isLast = otherSide.isLast
            userEntity.meSelf = otherSide.meSelf
            userEntity.top = otherSide.top
            userEntity.unReadNum = "1"
            userEntity.showPoint = false
            userEntity.chatBg = ""

            userEntity.msgType = lastEntity.msgType.toString()
            userEntity.msg = lastEntity.msg
            userEntity.lastTime = lastEntity.lastTime
            userEntity.isComMsg = lastEntity.isComMsg
            userEntity.alreadyRead = lastEntity.alreadyRead
            userEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
            var listHelper = WechatSimulatorListHelper(this)
            listHelper.save(userEntity)
        }
    }
    /**
     * 初始化新手指引的数据
     */
    private fun initWechatSimulatorData2(mySide: WechatUserEntity, otherSide: WechatUserEntity){
        val helper = WechatSimulatorHelper(this, otherSide)
        var msgEntity = WechatScreenShotEntity()
        msgEntity = WechatScreenShotEntity()
        msgEntity.isComMsg = false
        msgEntity.msgType = 0
        msgEntity.msg = "点击到我的页面 - 支付 - 可充值提现"
        msgEntity.avatarInt = mySide.avatarInt
        msgEntity.avatarStr = mySide.wechatUserAvatar
        msgEntity.resourceName = mySide.resourceName
        msgEntity.wechatUserId = mySide.wechatUserId
        helper.save(msgEntity)

        msgEntity = WechatScreenShotEntity()
        msgEntity.isComMsg = false
        msgEntity.msgType = 0
        msgEntity.msg = "点击到我的页面 - 头像一栏 进入个人信息页面 - 可修改头像、昵称、微信号"
        msgEntity.avatarInt = mySide.avatarInt
        msgEntity.avatarStr = mySide.wechatUserAvatar
        msgEntity.resourceName = mySide.resourceName
        msgEntity.wechatUserId = mySide.wechatUserId
        helper.save(msgEntity)

        val lastEntity = helper.queryLastMsg()
        if (null != lastEntity){
            val userEntity = WechatUserEntity()
            userEntity.id = otherSide.listId
            userEntity.wechatUserId = otherSide.wechatUserId
            userEntity.resourceName = otherSide.resourceName
            userEntity.avatarInt = otherSide.avatarInt
            userEntity.wechatUserAvatar = otherSide.wechatUserAvatar
            userEntity.wechatUserNickName = otherSide.wechatUserNickName
            userEntity.avatarFile = otherSide.avatarFile
            userEntity.pinyinNickName = otherSide.pinyinNickName
            userEntity.firstChar = otherSide.firstChar
            userEntity.isFirst = otherSide.isFirst
            userEntity.isLast = otherSide.isLast
            userEntity.meSelf = otherSide.meSelf
            userEntity.top = otherSide.top
            userEntity.unReadNum = "1"
            userEntity.showPoint = false
            userEntity.chatBg = ""

            userEntity.msgType = lastEntity.msgType.toString()
            userEntity.msg = lastEntity.msg
            userEntity.lastTime = lastEntity.lastTime
            userEntity.isComMsg = lastEntity.isComMsg
            userEntity.alreadyRead = lastEntity.alreadyRead
            userEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
            var listHelper = WechatSimulatorListHelper(this)
            listHelper.save(userEntity)
        }
    }
    /**
     * 初始化新手指引的数据
     */
    private fun initWechatSimulatorData3(mySide: WechatUserEntity, otherSide: WechatUserEntity){
        val helper = WechatSimulatorHelper(this, otherSide)
        var msgEntity = WechatScreenShotEntity()
        msgEntity = WechatScreenShotEntity()
        msgEntity.isComMsg = false
        msgEntity.msgType = 0
        msgEntity.msg = "长按我，可执行置顶聊天、设置未读消息数、删除等操作"
        msgEntity.avatarInt = mySide.avatarInt
        msgEntity.avatarStr = mySide.wechatUserAvatar
        msgEntity.resourceName = mySide.resourceName
        msgEntity.wechatUserId = mySide.wechatUserId
        helper.save(msgEntity)
        val lastEntity = helper.queryLastMsg()
        if (null != lastEntity){
            val userEntity = WechatUserEntity()
            userEntity.id = otherSide.listId
            userEntity.wechatUserId = otherSide.wechatUserId
            userEntity.resourceName = otherSide.resourceName
            userEntity.avatarInt = otherSide.avatarInt
            userEntity.wechatUserAvatar = otherSide.wechatUserAvatar
            userEntity.wechatUserNickName = otherSide.wechatUserNickName
            userEntity.avatarFile = otherSide.avatarFile
            userEntity.pinyinNickName = otherSide.pinyinNickName
            userEntity.firstChar = otherSide.firstChar
            userEntity.isFirst = otherSide.isFirst
            userEntity.isLast = otherSide.isLast
            userEntity.meSelf = otherSide.meSelf
            userEntity.top = otherSide.top
            userEntity.unReadNum = "1"
            userEntity.showPoint = false
            userEntity.chatBg = ""

            userEntity.msgType = lastEntity.msgType.toString()
            userEntity.msg = lastEntity.msg
            userEntity.lastTime = lastEntity.lastTime
            userEntity.isComMsg = lastEntity.isComMsg
            userEntity.alreadyRead = lastEntity.alreadyRead
            userEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
            var listHelper = WechatSimulatorListHelper(this)
            listHelper.save(userEntity)
        }
    }
    /**
     * 初始化新手指引的数据
     */
    private fun initWechatSimulatorData4(mySide: WechatUserEntity, otherSide: WechatUserEntity){
        val helper = WechatSimulatorHelper(this, otherSide)
        var msgEntity = WechatScreenShotEntity()
        msgEntity = WechatScreenShotEntity()
        msgEntity.isComMsg = false
        msgEntity.msgType = 0
        msgEntity.msg = "点击右上角" + " ，添加新的对话"
        msgEntity.avatarInt = mySide.avatarInt
        msgEntity.avatarStr = mySide.wechatUserAvatar
        msgEntity.resourceName = mySide.resourceName
        msgEntity.wechatUserId = mySide.wechatUserId
        helper.save(msgEntity)
        val lastEntity = helper.queryLastMsg()
        if (null != lastEntity){
            val userEntity = WechatUserEntity()
            userEntity.id = otherSide.listId
            userEntity.wechatUserId = otherSide.wechatUserId
            userEntity.resourceName = otherSide.resourceName
            userEntity.avatarInt = otherSide.avatarInt
            userEntity.wechatUserAvatar = otherSide.wechatUserAvatar
            userEntity.wechatUserNickName = otherSide.wechatUserNickName
            userEntity.avatarFile = otherSide.avatarFile
            userEntity.pinyinNickName = otherSide.pinyinNickName
            userEntity.firstChar = otherSide.firstChar
            userEntity.isFirst = otherSide.isFirst
            userEntity.isLast = otherSide.isLast
            userEntity.meSelf = otherSide.meSelf
            userEntity.top = otherSide.top
            userEntity.unReadNum = "1"
            userEntity.showPoint = false
            userEntity.chatBg = ""

            userEntity.msgType = lastEntity.msgType.toString()
            userEntity.msg = lastEntity.msg
            userEntity.lastTime = lastEntity.lastTime
            userEntity.isComMsg = lastEntity.isComMsg
            userEntity.alreadyRead = lastEntity.alreadyRead
            userEntity.lastTime = TimeUtil.getCurrentTimeEndMs()
            var listHelper = WechatSimulatorListHelper(this)
            listHelper.save(userEntity)
        }
    }
    private fun initWechatSimulatorData5(mySide: WechatUserEntity, otherSide: WechatUserEntity){
        val helper = WechatSimulatorHelper(this, otherSide)
        var msgEntity = WechatScreenShotEntity()
        msgEntity = WechatScreenShotEntity()
        msgEntity.isComMsg = false
        msgEntity.msgType = 0
        msgEntity.msg = "点击右上角" + " ，添加新的对话"
        msgEntity.avatarInt = mySide.avatarInt
        msgEntity.avatarStr = mySide.wechatUserAvatar
        msgEntity.resourceName = mySide.resourceName
        msgEntity.wechatUserId = mySide.wechatUserId
        helper.save(msgEntity)
        val lastEntity = helper.queryLastMsg()
        if (null != lastEntity){
            val userEntity = WechatUserEntity()
            userEntity.id = otherSide.listId
            userEntity.wechatUserId = otherSide.wechatUserId
            userEntity.resourceName = otherSide.resourceName
            userEntity.avatarInt = otherSide.avatarInt
            userEntity.wechatUserAvatar = otherSide.wechatUserAvatar
            userEntity.wechatUserNickName = otherSide.wechatUserNickName
            userEntity.avatarFile = otherSide.avatarFile
            userEntity.pinyinNickName = otherSide.pinyinNickName
            userEntity.firstChar = otherSide.firstChar
            userEntity.isFirst = otherSide.isFirst
            userEntity.isLast = otherSide.isLast
            userEntity.meSelf = otherSide.meSelf
            userEntity.top = otherSide.top
            userEntity.unReadNum = "1"
            userEntity.showPoint = false
            userEntity.chatBg = ""

            userEntity.msgType = lastEntity.msgType.toString()
            userEntity.msg = lastEntity.msg
            userEntity.lastTime = lastEntity.lastTime
            userEntity.isComMsg = lastEntity.isComMsg
            userEntity.alreadyRead = lastEntity.alreadyRead

            userEntity.chatType = 1
            var listHelper = WechatSimulatorListHelper(this)

            val bitmap = arrayOfNulls<Bitmap>(9)
            bitmap[0] = BitmapFactory.decodeResource(resources,R.mipmap.icon0)
            bitmap[1] = BitmapFactory.decodeResource(resources,R.mipmap.icon1)
            bitmap[2] = BitmapFactory.decodeResource(resources,R.mipmap.icon2)
            bitmap[3] = BitmapFactory.decodeResource(resources,R.mipmap.icon3)
            bitmap[4] = BitmapFactory.decodeResource(resources,R.mipmap.icon4)
            bitmap[5] = BitmapFactory.decodeResource(resources,R.drawable.wechat_icon1)
            bitmap[6] = BitmapFactory.decodeResource(resources,R.drawable.wechat_icon2)
            bitmap[7] = BitmapFactory.decodeResource(resources,R.drawable.wechat_icon3)
            bitmap[8] = BitmapFactory.decodeResource(resources,R.drawable.wechat_icon4)
            CombineBitmap.init(this)
                    .setLayoutManager(WechatLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                    .setSize(180) // 必选，组合后Bitmap的尺寸，单位dp
                    .setGap(3) // 单个图片之间的距离，单位dp，默认0dp
                    .setGapColor(Color.parseColor("#E8E8E8")) // 单个图片间距的颜色，默认白色
                    .setPlaceholder(R.drawable.head_default) // 单个图片加载失败的默认显示图片
//                    .setUrls(*getUrls(9.toString())) // 要加载的图片url数组
                .setBitmaps(*bitmap) // 要加载的图片bitmap数组
//                .setResourceIds() // 要加载的图片资源id数组
//                .setImageView() // 直接设置要显示图片的ImageView
                    // 设置“子图片”的点击事件，需使用setImageView()，index和图片资源数组的索引对应
                    .setOnSubItemClickListener { }
                    // 加载进度的回调函数，如果不使用setImageView()方法，可在onComplete()完成最终图片的显示
                    .setOnProgressListener(object : OnProgressListener {
                        override fun onStart() {}

                        override fun onComplete(bitmap: Bitmap) {
                            userEntity.groupHeader = bitmap
                            listHelper.save(userEntity)
                        }
                    })
                    .build()
        }
    }
    /**
     * 第三个参数为SDK调试模式开关，调试模式的行为特性如下：
     * 输出详细的Bugly SDK的Log；
     * 每一条Crash都会被立即上报
     * 自定义日志将会在Logcat中输出
     * 建议在测试阶段建议设置成true，发布时设置为false。
     */
    private fun initBugly() {
        val context = applicationContext
        // 获取当前包名
        val packageName = context.packageName
        // 获取当前进程名
        val processName = getProcessName(android.os.Process.myPid())
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(context)
        strategy.isUploadProcess = processName == null || processName == packageName
        // 建议在测试阶段建议设置成true，发布时设置为false。
        // 初始化Bugly
        CrashReport.initCrashReport(context, AppConfig.BUGLY_APP_ID, true, strategy)
        /**
         * bugly的标签
         * 区分是线上代码报错还是开发代码报错
         */
        CrashReport.setUserSceneTag(applicationContext, if(BuildConfig.DEBUG) 99740 else 99739)
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }
    private fun globeHttpConfiguration() {
        EasyHttp.init(this)
        //设置请求头
        val headers = HttpHeaders()
        //        headers.put("User-Agent", SystemInfoUtils.getUserAgent(this, AppConstant.APPID));
        //        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Authorization", HttpConfig.APPID + "|" + MD5.string2MD5(HttpConfig.APPID + HttpConfig.APPSECRET))
        headers.put("Accept-Encoding", "identity")
        headers.put("os", "android")
        headers.put("version", "20190130")
        //设置请求参数
        EasyHttp.getInstance()
                .debug("RxEasyHttp", true)
                .setReadTimeOut((60 * 1000).toLong())
                .setWriteTimeOut((60 * 1000).toLong())
                .setConnectTimeout((60 * 1000).toLong())
                .setRetryCount(3)//默认网络不好自动重试3次
                .setRetryDelay(500)//每次延时500ms重试
                .setRetryIncreaseDelay(500)//每次延时叠加500ms
                .setBaseUrl(HttpConfig.BASE_URL)
                .setCacheDiskConverter(SerializableDiskConverter())//默认缓存使用序列化转化
                .setCacheMaxSize((50 * 1024 * 1024).toLong())//设置缓存大小为50M
                .setCacheVersion(1)//缓存版本为1
                .setHostnameVerifier(UnSafeHostnameVerifier(HttpConfig.BASE_URL))//全局访问规则
                .setCertificates()//信任所有证书
                //.addConverterFactory(GsonConverterFactory.create(gson))//本框架没有采用Retrofit的Gson转化，所以不用配置
                .addCommonHeaders(headers)//设置全局公共头
        //                .addCommonParams(params)//设置全局公共参数
        //                .addInterceptor(newfun CustomSignInterceptor());//添加参数签名拦截器
        //.addInterceptor(newfun HeTInterceptor());//处理自己业务的拦截器
    }

    inner class UnSafeHostnameVerifier(private val host: String?) : HostnameVerifier {

        init {
            HttpLog.i("###############　UnSafeHostnameVerifier $host")
        }

        override fun verify(hostname: String, session: SSLSession): Boolean {
            HttpLog.i("############### verify " + hostname + " " + this.host)
            return !(this.host == null || "" == this.host || !this.host.contains(hostname))
        }
    }

    companion object {
        private var mInstant: App? = null

        //static 代码段可以防止内存泄露
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.overallBg, R.color.overallGray3)//全局设置主题颜色
                ClassicsHeader(context)//.setTimeFormat(newfun DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.overallBg, R.color.overallGray3)//全局设置主题颜色
                //指定为经典Footer，默认是 BallPulseFooter
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }

        // 单例模式中获取唯一的MyApplication实例
        val instance: App
            get() {
                if (null == mInstant) {
                    mInstant = App()
                }
                return mInstant as App
            }
    }
}
