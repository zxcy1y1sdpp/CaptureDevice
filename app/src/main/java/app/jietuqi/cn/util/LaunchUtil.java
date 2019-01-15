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
import app.jietuqi.cn.entity.WechatTransferEntity;
import app.jietuqi.cn.entity.WechatVoiceAndVideoEntity;
import app.jietuqi.cn.ui.activity.OverallCommunicateDetailsActivity;
import app.jietuqi.cn.ui.activity.OverallJoinGroupsActivity;
import app.jietuqi.cn.ui.activity.OverallMyPublishActivity;
import app.jietuqi.cn.ui.activity.OverallPersonalCardActivity;
import app.jietuqi.cn.ui.activity.OverallPublishCardActivity;
import app.jietuqi.cn.ui.activity.OverallRegisterActivity;
import app.jietuqi.cn.ui.activity.OverallWebViewActivity;
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
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateEditRoleActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreatePictureAndVideoActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateRedPacketActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateSettingInfoActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateSystemMessageActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateTextActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateTransferActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.create.WechatCreateVoiceActivity;
import app.jietuqi.cn.ui.wechatscreenshot.ui.preview.WechatScreenShotPreviewActivity;
import app.jietuqi.cn.wechat.create.WechatAddChargeDetailActivity;
import app.jietuqi.cn.wechat.create.WechatChangeActivity;
import app.jietuqi.cn.wechat.create.WechatCreateBillActivity;
import app.jietuqi.cn.wechat.create.WechatLooseChangeActivity;
import app.jietuqi.cn.wechat.create.WechatMyWalletActivity;
import app.jietuqi.cn.wechat.create.WechatSingleChatActivity;
import app.jietuqi.cn.wechat.create.WechatTransferDetailActivity;
import app.jietuqi.cn.wechat.entity.WechatChargeDetailEntity;
import app.jietuqi.cn.wechat.entity.WechatCreateBillsEntity;
import app.jietuqi.cn.wechat.entity.WechatWithdrawDepositEntity;
import app.jietuqi.cn.wechat.preview.WechatPreviewChangeWithdrawDepositActivity;
import app.jietuqi.cn.wechat.preview.WechatRedPacketPreviewActivity;
import app.jietuqi.cn.wechat.preview.WechatVoiceAndVideoPreviewActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.WechatSimulatorPreviewActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateRedPacketActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateSystemMessageActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateTransferActivity;
import app.jietuqi.cn.wechat.simulator.ui.activity.create.WechatSimulatorCreateVoiceActivity;

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
     * 跳转到预览红包的页面(发红包)
     * @param sender 发送人
     * @param receiver 领取人
     */
    public static void startWechatRedPacketPreviewActivity(Context context, WechatUserEntity sender, WechatUserEntity receiver, String money){
        Intent intent = new Intent(context, WechatRedPacketPreviewActivity.class);
        intent.putExtra(IntentKey.TYPE, 1);
        intent.putExtra(IntentKey.ENTITY_SENDER, sender);
        intent.putExtra(IntentKey.ENTITY_RECEIVER, receiver);
        intent.putExtra(IntentKey.MONEY, money);
        context.startActivity(intent);
    }
    /**
     * 跳转到预览红包的页面(收红包)
     * @param sender 发送人
     */
    public static void startWechatRedPacketPreviewActivity(Context context, WechatUserEntity sender, String money){
        Intent intent = new Intent(context, WechatRedPacketPreviewActivity.class);
        intent.putExtra(IntentKey.TYPE, 0);
        intent.putExtra(IntentKey.ENTITY_SENDER, sender);
        intent.putExtra(IntentKey.MONEY, money);
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
     * 跳转到微信零钱页面
     * @param money 余额
     */
    public static void startWechatChangeActivity(Context context, String money){
        Intent intent = new Intent(context, WechatChangeActivity.class);
        intent.putExtra(IntentKey.MONEY, money);
        context.startActivity(intent);
    }
    /**
     * 跳转到微信我的钱包页面
     * @param entity 数据实体
     */
    public static void startWechatTransferDetailActivity(Context context, WechatTransferEntity entity){
        Intent intent = new Intent(context, WechatTransferDetailActivity.class);
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
     * @param table
     */
    public static void startWechatSingleChatActivity(Context context, WechatUserEntity table){
        Intent intent = new Intent(context, WechatSingleChatActivity.class);
        intent.putExtra(IntentKey.ENTITY, table);
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
    public static void startForResultRoleOfLibraryActivity(Activity activity, int requestCode){
        Intent intent = new Intent(activity, RoleOfLibraryActivity.class);
        intent.putExtra(IntentKey.REQUEST_CODE, requestCode);
        activity.startActivityForResult(intent, requestCode);
    }
    /**
     * 跳转到修改角色页面
     */
    public static void startForResultWechatCreateEditRoleActivity(Activity activity, WechatUserEntity entity, int requestCode){
        Intent intent = new Intent(activity, WechatCreateEditRoleActivity.class);
        intent.putExtra(IntentKey.REQUEST_CODE, requestCode);
        intent.putExtra(IntentKey.ENTITY, entity);
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
    public static void startWechatSimulatorCreateVoiceActivity(Context context, WechatUserEntity entity){
        Intent intent = new Intent(context, WechatSimulatorCreateVoiceActivity.class);
        intent.putExtra(IntentKey.OTHER_SIDE, entity);
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
}
