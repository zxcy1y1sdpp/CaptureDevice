package app.jietuqi.cn.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

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
import app.jietuqi.cn.database.table.WechatUserTable;
import app.jietuqi.cn.entity.WechatTransferEntity;
import app.jietuqi.cn.entity.WechatVoiceAndVideoEntity;
import app.jietuqi.cn.ui.activity.OverallCommunicateDetailsActivity;
import app.jietuqi.cn.ui.activity.OverallJoinGroupsActivity;
import app.jietuqi.cn.ui.activity.OverallPersonalCardActivity;
import app.jietuqi.cn.ui.activity.OverallPublishCardActivity;
import app.jietuqi.cn.ui.activity.OverallRegisterActivity;
import app.jietuqi.cn.ui.entity.OverallCardEntity;
import app.jietuqi.cn.ui.entity.OverallDynamicEntity;
import app.jietuqi.cn.wechat.entity.WechatChargeDetailEntity;
import app.jietuqi.cn.wechat.entity.WechatWithdrawDepositEntity;
import app.jietuqi.cn.wechat.ui.activity.WechatAddChargeDetailActivity;
import app.jietuqi.cn.wechat.ui.activity.WechatChangeActivity;
import app.jietuqi.cn.wechat.ui.activity.WechatLooseChangeActivity;
import app.jietuqi.cn.wechat.ui.activity.WechatMyWalletActivity;
import app.jietuqi.cn.wechat.ui.activity.WechatPreviewChangeWithdrawDepositActivity;
import app.jietuqi.cn.wechat.ui.activity.WechatRedPacketPreviewActivity;
import app.jietuqi.cn.wechat.ui.activity.WechatSingleChatActivity;
import app.jietuqi.cn.wechat.ui.activity.WechatTransferDetailActivity;
import app.jietuqi.cn.wechat.ui.activity.WechatVoiceAndVideoActivity;
import app.jietuqi.cn.wechat.ui.activity.WechatVoiceAndVideoPreviewActivity;

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
    public static void startWechatRedPacketPreviewActivity(Context context, WechatUserTable sender, WechatUserTable receiver, String money){
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
    public static void startWechatRedPacketPreviewActivity(Context context, WechatUserTable sender, String money){
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
     * 跳转到微信生成语音或视频聊天页面
     * @param type 0 -- 语音聊天，1 -- 视频聊天
     */
    public static void startWechatVoiceAndVideoActivity(Context context, int type){
        Intent intent = new Intent(context, WechatVoiceAndVideoActivity.class);
        intent.putExtra(IntentKey.TYPE, type);
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
    public static void startWechatSingleChatActivity(Context context, WechatUserTable table){
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
}
