package dasheng.com.capturedevice.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import dasheng.com.capturedevice.constant.IntentKey;
import dasheng.com.capturedevice.database.table.WechatUserTable;
import dasheng.com.capturedevice.entity.WechatTransferEntity;
import dasheng.com.capturedevice.entity.WechatVoiceAndVideoEntity;
import dasheng.com.capturedevice.wechat.entity.WechatChargeDetailEntity;
import dasheng.com.capturedevice.wechat.entity.WechatWithdrawDepositEntity;
import dasheng.com.capturedevice.wechat.ui.activity.WechatAddChargeDetailActivity;
import dasheng.com.capturedevice.wechat.ui.activity.WechatChangeActivity;
import dasheng.com.capturedevice.wechat.ui.activity.WechatLooseChangeActivity;
import dasheng.com.capturedevice.wechat.ui.activity.WechatMyWalletActivity;
import dasheng.com.capturedevice.wechat.ui.activity.WechatPreviewChangeWithdrawDepositActivity;
import dasheng.com.capturedevice.wechat.ui.activity.WechatRedPacketPreviewActivity;
import dasheng.com.capturedevice.wechat.ui.activity.WechatSingleChatActivity;
import dasheng.com.capturedevice.wechat.ui.activity.WechatTransferDetailActivity;
import dasheng.com.capturedevice.wechat.ui.activity.WechatVoiceAndVideoActivity;
import dasheng.com.capturedevice.wechat.ui.activity.WechatVoiceAndVideoPreviewActivity;

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
}
