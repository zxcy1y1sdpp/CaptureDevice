package app.jietuqi.cn.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import app.jietuqi.cn.RoleOfLibraryActivity;
import app.jietuqi.cn.alipay.create.AlipayCreateTransferBillActivity;
import app.jietuqi.cn.alipay.entity.AlipayCreateMyEntity;
import app.jietuqi.cn.alipay.entity.AlipayCreateRedPacketEntity;
import app.jietuqi.cn.alipay.entity.AlipayCreateTransferBillEntity;
import app.jietuqi.cn.alipay.entity.AlipayCreateWithdrawDepositBillEntity;
import app.jietuqi.cn.alipay.entity.AlipayPreviewBalanceEntity;
import app.jietuqi.cn.alipay.preview.AlipayPreviewBalanceActivity;
import app.jietuqi.cn.alipay.preview.AlipayPreviewMyActivity;
import app.jietuqi.cn.alipay.preview.AlipayPreviewRedPacketActivity;
import app.jietuqi.cn.alipay.preview.AlipayPreviewTransferBillActivity;
import app.jietuqi.cn.alipay.preview.AlipayPreviewWithdrawDepositBillActivity;
import app.jietuqi.cn.constant.IntentKey;
import app.jietuqi.cn.constant.RequestCode;
import app.jietuqi.cn.entity.WechatVoiceAndVideoEntity;
import app.jietuqi.cn.ui.activity.OverallCdkListActivity;
import app.jietuqi.cn.ui.activity.OverallCleanFansConfirmOrderActivity;
import app.jietuqi.cn.ui.activity.OverallCommunicateDetailsActivity;
import app.jietuqi.cn.ui.activity.OverallEditActivity;
import app.jietuqi.cn.ui.activity.OverallJoinGroupsActivity;
import app.jietuqi.cn.ui.activity.OverallMyPublishActivity;
import app.jietuqi.cn.ui.activity.OverallPersonalCardActivity;
import app.jietuqi.cn.ui.activity.OverallProjectClassifyActivity;
import app.jietuqi.cn.ui.activity.OverallProjectPublishActivity;
import app.jietuqi.cn.ui.activity.OverallProjectPublishDetailsActivity;
import app.jietuqi.cn.ui.activity.OverallProjectShowActivity;
import app.jietuqi.cn.ui.activity.OverallPublishCardActivity;
import app.jietuqi.cn.ui.activity.OverallRegisterActivity;
import app.jietuqi.cn.ui.activity.OverallStickActivity;
import app.jietuqi.cn.ui.activity.OverallWeMediaClassifyActivity;
import app.jietuqi.cn.ui.activity.OverallWeMediaDetailsActivity;
import app.jietuqi.cn.ui.activity.OverallWebViewActivity;
import app.jietuqi.cn.ui.activity.ProjectPopularizeActivity;
import app.jietuqi.cn.ui.alipayscreenshot.entity.AlipayScreenShotEntity;
import app.jietuqi.cn.ui.alipayscreenshot.ui.create.AlipayCreatePictureActivity;
import app.jietuqi.cn.ui.alipayscreenshot.ui.create.AlipayCreateRedPacketActivity;
import app.jietuqi.cn.ui.alipayscreenshot.ui.create.AlipayCreateSystemMessageActivity;
import app.jietuqi.cn.ui.alipayscreenshot.ui.create.AlipayCreateTextActivity;
import app.jietuqi.cn.ui.alipayscreenshot.ui.create.AlipayCreateTransferActivity;
import app.jietuqi.cn.ui.alipayscreenshot.ui.create.AlipayCreateVoiceActivity;
import app.jietuqi.cn.ui.alipayscreenshot.ui.preview.AlipayScreenShotPreviewActivity;
import app.jietuqi.cn.ui.entity.OverallCardEntity;
import app.jietuqi.cn.ui.entity.OverallDynamicEntity;
import app.jietuqi.cn.ui.entity.OverallPublishEntity;
import app.jietuqi.cn.ui.entity.OverallVipCardEntity;
import app.jietuqi.cn.ui.entity.OverallWeMediaClassifyEntity;
import app.jietuqi.cn.ui.entity.ProjectMarketEntity;
import app.jietuqi.cn.ui.entity.WechatUserEntity;
import app.jietuqi.cn.ui.qqscreenshot.entity.QQScreenShotEntity;
import app.jietuqi.cn.ui.qqscreenshot.ui.create.QQCreatePictureActivity;
import app.jietuqi.cn.ui.qqscreenshot.ui.create.QQCreateRedPacketActivity;
import app.jietuqi.cn.ui.qqscreenshot.ui.create.QQCreateSystemMessageActivity;
import app.jietuqi.cn.ui.qqscreenshot.ui.create.QQCreateTextActivity;
import app.jietuqi.cn.ui.qqscreenshot.ui.create.QQCreateTransferActivity;
import app.jietuqi.cn.ui.qqscreenshot.ui.create.QQCreateVoiceActivity;
import app.jietuqi.cn.ui.qqscreenshot.ui.preview.QQScreenShotPreviewActivity;
import app.jietuqi.cn.ui.wechatscreenshot.entity.WechatScreenShotEntity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateCardActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateEditRoleActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateEmojiActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateInviteJoinGroupActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreatePictureAndVideoActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateRedPacketActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateSettingInfoActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateShareActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateSystemMessageActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateTextActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateTimeActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateTransferActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateVideoActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateVoiceActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.preview.WechatScreenShotPreviewActivity;
import app.jietuqi.cn.wechat.create.WechatAddChargeDetailActivity;
import app.jietuqi.cn.wechat.create.WechatChangeActivity;
import app.jietuqi.cn.wechat.create.WechatCreateBillActivity;
import app.jietuqi.cn.wechat.create.WechatLooseChangeActivity;
import app.jietuqi.cn.wechat.create.WechatMyWalletActivity;
import app.jietuqi.cn.wechat.create.WechatTransferDetailActivity;
import app.jietuqi.cn.wechat.entity.WechatBankEntity;
import app.jietuqi.cn.wechat.entity.WechatChargeDetailEntity;
import app.jietuqi.cn.wechat.entity.WechatCreateBillsEntity;
import app.jietuqi.cn.wechat.entity.WechatWithdrawDepositEntity;
import app.jietuqi.cn.wechat.preview.WechatPreviewChangeWithdrawDepositActivity;
import app.jietuqi.cn.wechat.preview.WechatVoiceAndVideoPreviewActivity;
import app.jietuqi.cn.wechat.screenshot.WechatScreenShotChangeActivity;
import app.jietuqi.cn.wechat.screenshot.WechatScreenShotMyWalletActivity;
import app.jietuqi.cn.wechat.screenshot.WechatScreenShotPreviewChangeWithdrawDepositActivity;
import app.jietuqi.cn.wechat.screenshot.WechatScreenShotReceiveRedPacketActivity;
import app.jietuqi.cn.wechat.screenshot.WechatScreenShotSendRedPacketActivity;
import app.jietuqi.cn.wechat.screenshot.WechatScreenShotTransferDetailActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatEditOtherActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatSimulatorGroupRedPacketActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatSimulatorPreviewActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatSimulatorPreviewGroupActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatSimulatorReceiveRedPacketActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatSimulatorRechargeSuccessActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatSimulatorSendRedPacketActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateGroupRedPacketActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateGroupSystemMessageActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreatePictureActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateRedPacketActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateSystemMessageActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateTextActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateTimeActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateTransferActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateVoiceActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorEditGroupInfoActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorEditGroupRolesActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorEditRoleActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorSortActivity;

/**
 * 作者： liuyuanbo on 2018/10/2 20:37.
 * 时间： 2018/10/2 20:37
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class LaunchUtil {
    /*********************************************** 不传递参数的通用跳转方法***********************************************/
    /**
     * 没有任何参数的跳转（startActivity）
     * @param context
     * @param cls
     */
    public static void launch(Context context, Class<?> cls) {
        context.startActivity(new Intent(context, cls));
    }
    /**
     * 没有任何参数的跳转（startActivityForResult）
     * @param context
     * @param cls
     */
    public static void launch(Activity context, Class<?> cls, int requestCode) {
        context.startActivityForResult(new Intent(context, cls), requestCode);
    }

    /**
     * 跳转到预览红包的页面(收红包)
     * @param sender 发送人
     */
    public static void startWechatScreenShotReceiveRedPacketActivity(Context context, WechatUserEntity sender){
        Intent intent = new Intent(context, WechatScreenShotReceiveRedPacketActivity.class);
        intent.putExtra(IntentKey.ENTITY_SENDER, sender);
        context.startActivity(intent);
    }
    /**
     * 微信模拟器跳转到预览红包的页面(收红包)
     * @param sender 发送人
     */
    public static void startWechatSimulatorReceiveRedPacketActivity(Context context, WechatUserEntity sender){
        Intent intent = new Intent(context, WechatSimulatorReceiveRedPacketActivity.class);
        intent.putExtra(IntentKey.ENTITY_SENDER, sender);
        context.startActivity(intent);
    }
    /**
     * 跳转到预览红包的页面(发红包)
     * @param sender 发送人
     * @param receiver 领取人
     */
    public static void startWechatScreenShotSendRedPacketActivity(Context context, WechatUserEntity sender, WechatUserEntity receiver){
        Intent intent = new Intent(context, WechatScreenShotSendRedPacketActivity.class);
        intent.putExtra(IntentKey.ENTITY_SENDER, sender);
        intent.putExtra(IntentKey.ENTITY_RECEIVER, receiver);
        context.startActivity(intent);
    }
    /**
     * 微信模拟器跳转到预览红包的页面(发红包)
     * @param sender 发送人
     * @param receiver 领取人
     */
    public static void startWechatSimulatorSendRedPacketActivity(Context context, WechatUserEntity sender, WechatUserEntity receiver){
        Intent intent = new Intent(context, WechatSimulatorSendRedPacketActivity.class);
        intent.putExtra(IntentKey.ENTITY_SENDER, sender);
        intent.putExtra(IntentKey.ENTITY_RECEIVER, receiver);
        context.startActivity(intent);
    }
    /**
     * 微信模拟器跳转到预览红包的页面(发红包)
     */
    public static void startWechatSimulatorGroupRedPacketActivity(Context context, WechatScreenShotEntity entity){
        Intent intent = new Intent(context, WechatSimulatorGroupRedPacketActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 跳转到微信我的钱包页面
     * @param money 余额
     */
    public static void startWechatMyWalletActivity(Context context, String money){
        Intent intent = new Intent(context, WechatMyWalletActivity.class);
        intent.putExtra(IntentKey.MONEY, money);
        context.startActivity(intent);
    }
    /**
     * 跳转到微信我的钱包页面
     * @param money 余额
     */
    public static void startWechatScreenShotMyWalletActivity(Context context, String money){
        Intent intent = new Intent(context, WechatScreenShotMyWalletActivity.class);
        intent.putExtra(IntentKey.MONEY, money);
        context.startActivity(intent);
    }
    /**
     * 跳转到微信零钱页面
     * @param money 余额
     */
    public static void startWechatChangeActivity(Context context, String money, boolean isSimulator){
        Intent intent = new Intent(context, WechatChangeActivity.class);
        intent.putExtra(IntentKey.MONEY, money);
        intent.putExtra(IntentKey.IS_SIMULATOR, isSimulator);
        context.startActivity(intent);
    }
    /**
     * 跳转到微信零钱页面
     * @param money 余额
     */
    public static void startWechatScreenShotChangeActivity(Context context, String money){
        Intent intent = new Intent(context, WechatScreenShotChangeActivity.class);
        intent.putExtra(IntentKey.MONEY, money);
        context.startActivity(intent);
    }
    /**
     * 跳转到微信我的钱包页面
     * @param entity 数据实体
     */
    public static void startWechatTransferDetailActivity(Context context, WechatScreenShotEntity entity){
        Intent intent = new Intent(context, WechatTransferDetailActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 跳转到微信我的钱包页面
     * @param entity 数据实体
     */
    public static void startWechatScreenShotTransferDetailActivity(Context context, WechatScreenShotEntity entity){
        Intent intent = new Intent(context, WechatScreenShotTransferDetailActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 跳转到预览语音或视频聊天的页面
     * @param entity 数据实体
     */
    public static void startWechatVoiceAndVideoPreviewActivity(Context context, WechatVoiceAndVideoEntity entity){
        Intent intent = new Intent(context, WechatVoiceAndVideoPreviewActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 跳转到微信单聊页面
     * @param type 0 -- 我的钱包
     *             1 -- 微信零钱
     */
    public static void startWechatLooseChangeActivity(Context context, int type){
        Intent intent = new Intent(context, WechatLooseChangeActivity.class);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 跳转到预览 -- 微信 -- 提现页面
     * @param entity
     */
    public static void startWechatPreviewChangeWithdrawDepositActivity(Context context, WechatWithdrawDepositEntity entity){
        Intent intent = new Intent(context, WechatPreviewChangeWithdrawDepositActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 跳转到预览 -- 微信 -- 提现页面
     * @param entity
     */
    public static void startWechatScreenShotPreviewChangeWithdrawDepositActivity(Context context, WechatWithdrawDepositEntity entity){
        Intent intent = new Intent(context, WechatScreenShotPreviewChangeWithdrawDepositActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 跳转到H5页面
     */
    public static void startOverallWebViewActivity(Context context, String url, String title){
        Intent intent = new Intent(context, OverallWebViewActivity.class);
        intent.putExtra(IntentKey.URL, url);
        intent.putExtra(IntentKey.TITLE, title);
        context.startActivity(intent);
    }

    /**
     * 跳转到编辑/添加微信零钱明细的页面
     * @param context
     * @param entity
     */
    public static void startWechatAddChargeDetailActivity(Context context, WechatChargeDetailEntity entity){
        Intent intent = new Intent(context, WechatAddChargeDetailActivity.class);
        if (null != entity){
            intent.putExtra(IntentKey.ENTITY, entity);
        }
        context.startActivity(intent);
    }

    /**
     * 支付宝 -- 预览 -- 红包页面
     * @param entity
     */
    public static void startAlipayPreviewRedPacketActivity(Context context, AlipayCreateRedPacketEntity entity){
        Intent intent = new Intent(context, AlipayPreviewRedPacketActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 支付宝 -- 预览 -- 余额页面
     * @param entity
     */
    public static void startAlipayPreviewBalanceActivity(Context context, AlipayPreviewBalanceEntity entity){
        Intent intent = new Intent(context, AlipayPreviewBalanceActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 支付宝 -- 预览 -- 转账账单页面
     * @param entity
     * @param type
     *        0 -- 转账账单
     *        1 -- 收款账单
     */
    public static void startAlipayPreviewTransferBillActivity(Context context, AlipayCreateTransferBillEntity entity, int type){
        Intent intent = new Intent(context, AlipayPreviewTransferBillActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 支付宝 -- 预览 -- 提现账单页面
     * @param entity
     */
    public static void startAlipayPreviewWithdrawDepositBillActivity(Context context, AlipayCreateWithdrawDepositBillEntity entity){
        Intent intent = new Intent(context, AlipayPreviewWithdrawDepositBillActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 支付宝 -- 预览 -- 转账账单页面/收款账单页面
     * @param type
     *        0 -- 转账账单页面
     *        1 -- 收款账单页面
     */
    public static void startAlipayCreateTransferBillActivity(Context context, int type){
        Intent intent = new Intent(context, AlipayCreateTransferBillActivity.class);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 支付宝 -- 预览 -- 我的 页面/收款账单页面
     */
    public static void startAlipayPreviewMyActivity(Context context, AlipayCreateMyEntity entity){
        Intent intent = new Intent(context, AlipayPreviewMyActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 评论详情页面
     */
    public static void startOverallCommunicateDetailsActivity(Context context, OverallDynamicEntity entity){
        Intent intent = new Intent(context, OverallCommunicateDetailsActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 加粉，加群的页面
     * 0 -- 互粉
     * 1 -- 加群
     */
    public static void startOverallJoinGroupsActivity(Context context, int type){
        Intent intent = new Intent(context, OverallJoinGroupsActivity.class);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 发布名片的页面
     * 0 -- 互粉
     * 1 -- 加群
     */
    public static void startOverallPublishCardActivity(Context context, int type, OverallCardEntity entity){
        Intent intent = new Intent(context, OverallPublishCardActivity.class);
        intent.putExtra(IntentKey.TYPE, type);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 注册/绑定手机
     * 0 -- 注册
     * 1 -- 绑定手机
     * 2 -- 修改密码
     */
    public static void startOverallRegisterActivity(Context context, int type){
        Intent intent = new Intent(context, OverallRegisterActivity.class);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 注册/绑定手机
     * 0 -- 个人名片
     * 1 -- 群名片
     */
    public static void startOverallPersonalCardActivity(Context context, int type, OverallCardEntity entity){
        Intent intent = new Intent(context, OverallPersonalCardActivity.class);
        intent.putExtra(IntentKey.TYPE, type);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 跳转到设置资料的页面
     * @param type
     * 0 -- 微信，1 -- 支付宝，2 -- QQ
     */
    public static void startWechatCreateSettingInfoActivity(Activity context, WechatUserEntity mySide, WechatUserEntity otherSide, int type){
        Intent intent = new Intent(context, WechatCreateSettingInfoActivity.class);
        intent.putExtra(IntentKey.MY_SIDE, mySide);
        intent.putExtra(IntentKey.OTHER_SIDE, otherSide);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 跳转到选择角色页面
     */
    public static void startForResultRoleOfLibraryActivity(Activity activity, int requestCode, boolean needChangeShared){
        Intent intent = new Intent(activity, RoleOfLibraryActivity.class);
        intent.putExtra(IntentKey.REQUEST_CODE, requestCode);
        intent.putExtra(IntentKey.CHANGE_SHAREDPREFERENCE, needChangeShared);
        activity.startActivityForResult(intent, requestCode);
    }
    /**
     * 跳转到修改角色页面
     */
    public static void startForResultWechatCreateEditRoleActivity(Activity activity, WechatUserEntity entity, int requestCode, int type, boolean needDb){
        Intent intent = new Intent(activity, WechatCreateEditRoleActivity.class);
        intent.putExtra(IntentKey.REQUEST_CODE, requestCode);
        intent.putExtra(IntentKey.ENTITY, entity);
        intent.putExtra(IntentKey.TYPE, type);
        intent.putExtra(IntentKey.NEED_OPERATE_DB, needDb);
        activity.startActivityForResult(intent, requestCode);
    }
    /**
     * 发送文本的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatCreateTextActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatCreateTextActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 发送时间的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatCreateTimeActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatCreateTimeActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 发送图片或视频的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatCreatePictureAndVideoActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatCreatePictureAndVideoActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 发送或接受红包的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatCreateRedPacketActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatCreateRedPacketActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建转账的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatCreateTransferActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatCreateTransferActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建语音的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatCreateVoiceActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatCreateVoiceActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建系统提示的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatCreateSystemMessageActivity(Context context, WechatUserEntity other, WechatScreenShotEntity msgEntity, int type, String screenType){
        Intent intent = new Intent(context, WechatCreateSystemMessageActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, other);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.SCREEN_TYPE, screenType);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 微信截图预览
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatScreenShotPreviewActivity(Context context, ArrayList<WechatScreenShotEntity> list){
        Intent intent = new Intent(context, WechatScreenShotPreviewActivity.class);
        intent.putExtra(IntentKey.LIST, list);
        context.startActivity(intent);
    }
    /**
     * 发送文本的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startAlipayCreateTextActivity(Context context, WechatUserEntity entity, AlipayScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, AlipayCreateTextActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 发送图片或视频的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startAlipayCreatePictureActivity(Context context, WechatUserEntity entity, AlipayScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, AlipayCreatePictureActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建语音的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startAlipayCreateVoiceActivity(Context context, WechatUserEntity entity, AlipayScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, AlipayCreateVoiceActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 发送或接受红包的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startAlipayCreateRedPacketActivity(Context context, WechatUserEntity entity, AlipayScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, AlipayCreateRedPacketActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建转账的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startAlipayCreateTransferActivity(Context context, WechatUserEntity entity, AlipayScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, AlipayCreateTransferActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }

    /**
     * 创建系统提示的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startAlipayCreateSystemMessageActivity(Context context, WechatUserEntity other, AlipayScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, AlipayCreateSystemMessageActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, other);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 支付宝截图预览
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startAlipayScreenShotPreviewActivity(Context context, ArrayList<AlipayScreenShotEntity> list){
        Intent intent = new Intent(context, AlipayScreenShotPreviewActivity.class);
        intent.putExtra(IntentKey.LIST, list);
        context.startActivity(intent);
    }
    /**
     * 创微信账单的页面
     * type 0 -- 创建
     * type 1 -- 修改
     */
    public static void startWechatCreateBillActivity(Context context, WechatCreateBillsEntity entity, int type){
        Intent intent = new Intent(context, WechatCreateBillActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 发送文本的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startQQCreateTextActivity(Context context, WechatUserEntity entity, QQScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, QQCreateTextActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }

    /**
     * 发送图片或视频的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startQQCreatePictureActivity(Context context, WechatUserEntity entity, QQScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, QQCreatePictureActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }

    /**
     * 发送或接受红包的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startQQCreateRedPacketActivity(Context context, WechatUserEntity entity, QQScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, QQCreateRedPacketActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建转账的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startQQCreateTransferActivity(Context context, WechatUserEntity entity, QQScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, QQCreateTransferActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建语音的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startQQCreateVoiceActivity(Context context, WechatUserEntity entity, QQScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, QQCreateVoiceActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建系统提示的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startQQCreateSystemMessageActivity(Context context, WechatUserEntity other, QQScreenShotEntity msgEntity, int type, String screenType){
        Intent intent = new Intent(context, QQCreateSystemMessageActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, other);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.SCREEN_TYPE, screenType);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * QQ截图预览
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startQQScreenShotPreviewActivity(Context context, ArrayList<QQScreenShotEntity> list){
        Intent intent = new Intent(context, QQScreenShotPreviewActivity.class);
        intent.putExtra(IntentKey.LIST, list);
        context.startActivity(intent);
    }
    /**
     * 微信模拟器
     * @param otherSide：聊天对象的信息
     */
    public static void startWechatSimulatorPreviewActivity(Context context, WechatUserEntity otherSide){
        Intent intent = new Intent(context, WechatSimulatorPreviewActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, otherSide);
        context.startActivity(intent);
    }
    /**
     * 微信模拟器
     */
    public static void startWechatSimulatorPreviewGroupActivity(Context context, WechatUserEntity otherSide){
        Intent intent = new Intent(context, WechatSimulatorPreviewGroupActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, otherSide);
        context.startActivity(intent);
    }
    /**
     * 说说列表
     */
    public static void startOverallMyPublishActivity(Context context, String userId, String nickName){
        Intent intent = new Intent(context, OverallMyPublishActivity.class);
        intent.putExtra(IntentKey.USER_ID, userId);
        intent.putExtra(IntentKey.NICKNAME, nickName);
        context.startActivity(intent);
    }
    /**
     * 创建语音的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatSimulatorCreateVoiceActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatSimulatorCreateVoiceActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 发送或接受红包的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatSimulatorCreateRedPacketActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatSimulatorCreateRedPacketActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 发送群红包
     * @param entity: 发送人
     * @param roleCount: 群人数
     * @param isComMsg: true -- 对方发送红包
     *                  false -- 我发送红包
     */
    public static void startWechatSimulatorCreateGroupRedPacketActivity(Context context, WechatUserEntity entity, int roleCount, boolean isComMsg){
        Intent intent = new Intent(context, WechatSimulatorCreateGroupRedPacketActivity.class);
        intent.putExtra(IntentKey.ENTITY_SENDER, entity);
        intent.putExtra(IntentKey.COUNT, roleCount);
        intent.putExtra(IntentKey.COMMSG, isComMsg);
        context.startActivity(intent);
    }
    /**
     * 创建转账的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatSimulatorCreateTransferActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatSimulatorCreateTransferActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建系统提示的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatSimulatorCreateSystemMessageActivity(Context context, WechatUserEntity other, WechatScreenShotEntity msgEntity, int type, String screenType){
        Intent intent = new Intent(context, WechatSimulatorCreateSystemMessageActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, other);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.SCREEN_TYPE, screenType);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建群系统提示的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatSimulatorCreateGroupSystemMessageActivity(Context context, int type, WechatUserEntity userEntity, WechatScreenShotEntity msgEntity){
        Intent intent = new Intent(context, WechatSimulatorCreateGroupSystemMessageActivity.class);
        intent.putExtra(IntentKey.ROLE_ENTITY, userEntity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }

    /**
     * 编辑群信息页面
     */
    public static void startWechatSimulatorEditGroupInfoActivity(Context context, WechatUserEntity entity){
        Intent intent = new Intent(context, WechatSimulatorEditGroupInfoActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 创建时间
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatSimulatorCreateTimeActivity(Context context, WechatUserEntity other, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatSimulatorCreateTimeActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, other);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 项目详情
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startOverallProjectPublishActivity(Context context, ProjectMarketEntity entity, int type){
        Intent intent = new Intent(context, OverallProjectPublishActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 项目详情
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startOverallProjectPublishDetailsActivity(Activity activity, int type, ArrayList<OverallPublishEntity> list, String content){
        Intent intent = new Intent(activity, OverallProjectPublishDetailsActivity.class);
        intent.putExtra(IntentKey.TYPE, type);
        intent.putExtra(IntentKey.LIST, list);
        intent.putExtra(IntentKey.CONTENT, content);
        activity.startActivityForResult(intent, RequestCode.CHOICE_PROJECT);
    }
    /**
     * 编辑框
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startEditActivity(Activity activity, String content){
        Intent intent = new Intent(activity, OverallEditActivity.class);
        intent.putExtra(IntentKey.CONTENT, content);
        activity.startActivityForResult(intent, RequestCode.PROJECT_TITLE);
    }
    /**
     * 项目详情
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startOverallProjectShowActivity(Context context, ProjectMarketEntity entity){
        Intent intent = new Intent(context, OverallProjectShowActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 某一分类的项目
     */
    public static void startOverallProjectClassifyActivity(Context context, int type, String usersId, String title, String industryId){
        Intent intent = new Intent(context, OverallProjectClassifyActivity.class);
        intent.putExtra(IntentKey.TITLE, title);
        if (type == 0){
            intent.putExtra(IntentKey.INDUSTRY_ID, industryId);
        }else {
            intent.putExtra(IntentKey.TYPE, type);
            intent.putExtra(IntentKey.USER_ID, usersId);
        }
        context.startActivity(intent);
    }
    /**
     * 充值成功
     */
    public static void startWechatSimulatorRechargeSuccessActivity(Context context, String money, WechatBankEntity entity){
        Intent intent = new Intent(context, WechatSimulatorRechargeSuccessActivity.class);
        intent.putExtra(IntentKey.MONEY, money);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    public static void startWechatEditOtherActivity(Context context, WechatUserEntity otherEntity){
        Intent intent = new Intent(context, WechatEditOtherActivity.class);
        intent.putExtra(IntentKey.ENTITY, otherEntity);
        context.startActivity(intent);
    }

    /**
     * 发送文本的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatSimulatorCreateTextActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatSimulatorCreateTextActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 发送图片或视频的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatSimulatorCreatePictureActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatSimulatorCreatePictureActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }

    /**
     * 项目推广
     */
    public static void startProjectPopularizeActivity(Context context, ProjectMarketEntity entity){
        Intent intent = new Intent(context, ProjectPopularizeActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }

    /**
     * 加粉加群置顶服务
     */
    public static void startOverallStickActivity(Context context, int type){
        Intent intent = new Intent(context, OverallStickActivity.class);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 清理死粉下单页面
     */
    public static void startOverallCleanFansConfirmOrderActivity(Context context, OverallVipCardEntity vipEntity, OverallVipCardEntity notVipEntity){
        Intent intent = new Intent(context, OverallCleanFansConfirmOrderActivity.class);
        intent.putExtra(IntentKey.VIP_Entity, vipEntity);
        intent.putExtra(IntentKey.NOT_VIP_Entity, notVipEntity);
        context.startActivity(intent);
    }
    /**
     * 激活码列表页面
     */
    public static void startOverallCdkListActivity(Context context, String orderId){
        Intent intent = new Intent(context, OverallCdkListActivity.class);
        intent.putExtra(IntentKey.ID, orderId);
        context.startActivity(intent);
    }
    /**
     * 自媒体分类页面
     */
    public static void startOverallWeMediaClassifyActivity(Context context, int type){
        Intent intent = new Intent(context, OverallWeMediaClassifyActivity.class);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 自媒体分类详情页面
     */
    public static void startOverallWeMediaDetailsActivity(Context context, int type, OverallWeMediaClassifyEntity entity){
        Intent intent = new Intent(context, OverallWeMediaDetailsActivity.class);
        intent.putExtra(IntentKey.TYPE, type);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 增加或减少群成员
     */
    public static void startWechatSimulatorEditGroupRolesActivity(Context context, int type, WechatUserEntity entity){
        Intent intent = new Intent(context, WechatSimulatorEditGroupRolesActivity.class);
        intent.putExtra(IntentKey.TYPE, type);
        intent.putExtra(IntentKey.ENTITY, entity);
        context.startActivity(intent);
    }
    /**
     * 跳转到修改角色页面
     */
    public static void startWechatSimulatorEditRoleActivity(Activity activity, WechatUserEntity entity){
        Intent intent = new Intent(activity, WechatSimulatorEditRoleActivity.class);
        intent.putExtra(IntentKey.ENTITY, entity);
        activity.startActivity(intent);
    }
    /**
     * 对话排序
     * @param type 0 -- 单聊
     *             1 -- 群聊
     */
    public static void startWechatSimulatorSortActivity(Activity activity, ArrayList<WechatScreenShotEntity> conversation, WechatUserEntity otherEntity, String groupTableName, int type){
        Intent intent = new Intent(activity, WechatSimulatorSortActivity.class);
        intent.putExtra(IntentKey.LIST, conversation);
        intent.putExtra(IntentKey.TYPE, type);
        intent.putExtra(IntentKey.ENTITY, otherEntity);
        intent.putExtra(IntentKey.GROUP_TABLE_NAME, groupTableName);
        activity.startActivity(intent);
    }
    /**
     * 创建视频和语音聊天的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatCreateVideoActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatCreateVideoActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建转发的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatCreateShareActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatCreateShareActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建个人名片的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatCreateCardActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatCreateCardActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建个人名片的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatCreateInviteJoinGroupActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatCreateInviteJoinGroupActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
    /**
     * 创建表情的页面
     * type 0 -- 发布
     * type 1 -- 修改
     */
    public static void startWechatCreateEmojiActivity(Context context, WechatUserEntity entity, WechatScreenShotEntity msgEntity, int type){
        Intent intent = new Intent(context, WechatCreateEmojiActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
        intent.putExtra(IntentKey.ENTITY, msgEntity);
        intent.putExtra(IntentKey.TYPE, type);
        context.startActivity(intent);
    }
}
