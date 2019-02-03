package app.jietuqi.cn.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.request.PostRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import app.jietuqi.cn.R;
import app.jietuqi.cn.constant.SharedPreferenceKey;
import app.jietuqi.cn.entity.OverallUserInfoEntity;
import app.jietuqi.cn.http.HttpConfig;
import app.jietuqi.cn.http.util.SystemInfoUtils;
import app.jietuqi.cn.ui.activity.OverallLoginActivity;
import app.jietuqi.cn.ui.entity.ContactEntity;
import app.jietuqi.cn.ui.entity.OverallIndustryEntity;
import app.jietuqi.cn.ui.entity.WechatUserEntity;
import app.jietuqi.cn.ui.wechatscreenshot.entity.ChangeSingleTaklBgEntity;
import app.jietuqi.cn.wechat.entity.WechatBankEntity;

import static app.jietuqi.cn.constant.SharedPreferenceKey.ALIPAY_CHAT_BG;
import static app.jietuqi.cn.constant.SharedPreferenceKey.ALIPAY_ME_SELF;
import static app.jietuqi.cn.constant.SharedPreferenceKey.ALIPAY_OTHER_SIDE;
import static app.jietuqi.cn.constant.SharedPreferenceKey.CHANNEL_NAME;
import static app.jietuqi.cn.constant.SharedPreferenceKey.CHANNEL_STATUS;
import static app.jietuqi.cn.constant.SharedPreferenceKey.CHANNEL_VERSION;
import static app.jietuqi.cn.constant.SharedPreferenceKey.DATE;
import static app.jietuqi.cn.constant.SharedPreferenceKey.EXPLORING;
import static app.jietuqi.cn.constant.SharedPreferenceKey.IS_LOGIN;
import static app.jietuqi.cn.constant.SharedPreferenceKey.MY_SELF;
import static app.jietuqi.cn.constant.SharedPreferenceKey.OTHER_SIDE;
import static app.jietuqi.cn.constant.SharedPreferenceKey.QQ_CHAT_BG;
import static app.jietuqi.cn.constant.SharedPreferenceKey.QQ_ME_SELF;
import static app.jietuqi.cn.constant.SharedPreferenceKey.QQ_OTHER_SIDE;
import static app.jietuqi.cn.constant.SharedPreferenceKey.QQ_OTHER_STATUS;
import static app.jietuqi.cn.constant.SharedPreferenceKey.QQ_UN_READ_NUMBER;
import static app.jietuqi.cn.constant.SharedPreferenceKey.SCREENSHOT_AGREEMENT;
import static app.jietuqi.cn.constant.SharedPreferenceKey.SINGLE_TALK_BG;
import static app.jietuqi.cn.constant.SharedPreferenceKey.TIMES;
import static app.jietuqi.cn.constant.SharedPreferenceKey.UNREAD_FRIEND_CIRCLE;
import static app.jietuqi.cn.constant.SharedPreferenceKey.USER_AVATAR;
import static app.jietuqi.cn.constant.SharedPreferenceKey.USER_INFO;
import static app.jietuqi.cn.constant.SharedPreferenceKey.USER_IS_VIP;
import static app.jietuqi.cn.constant.SharedPreferenceKey.USER_NICKNAME;
import static app.jietuqi.cn.constant.SharedPreferenceKey.USER_SHARE_NUMBER;
import static app.jietuqi.cn.constant.SharedPreferenceKey.UUIDKEY;
import static app.jietuqi.cn.constant.SharedPreferenceKey.WB;
import static app.jietuqi.cn.constant.SharedPreferenceKey.WECHAT_COVER;
import static app.jietuqi.cn.constant.SharedPreferenceKey.WECHAT_SIMULATOR_MY_SIDE;
import static app.jietuqi.cn.constant.SharedPreferenceKey.WECHAT_SIMULATOR_OTHER_SIDE;

/**
 * 作者： liuyuanbo on 2018/11/12 17:03.
 * 时间： 2018/11/12 17:03
 * 邮箱： 972383753@qq.com
 * 用途： app中用到的一些基本的方法
 * 例如： 判断用户是否登录之类的工具类
 */
public class UserOperateUtil {
    /**
     * 存储的渠道的信息
     * 当前的版本
     * @return
     */
    public static String getChannelVersion(){
        String number = SharedPreferencesUtils.getData(CHANNEL_VERSION, "-1").toString();
        return number;
    }
    /**
     * 存储的渠道的信息
     * 是否可以打开隐藏的功能
     * 0 -- 不可用
     * 1 -- 可用
     * @return
     */
    public static int getChannelStatus(){
        int status = (int) SharedPreferencesUtils.getData(CHANNEL_STATUS, -1);
        return status;
    }

    /**
     * 是否需要根据渠道进行关闭
     * @return
     */
    public static boolean needColseByChannel(){
        int status = getChannelStatus();
        if (0 == status){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 需要显示登录框
     * @param context
     * @return
     */
    public static boolean isCurrentLogin(Context context){
        if (!Boolean.parseBoolean(SharedPreferencesUtils.getData(IS_LOGIN, false).toString())){
            AppUtils.showLogin(context);
            return false;
        }else {
            return true;
        }
    }
    /**
     * 不需要显示登录框，直接跳转登录页面
     * @param context
     * @return
     */
    public static boolean isCurrentLoginDirectlyLogin(Context context){
        boolean login = (boolean) SharedPreferencesUtils.getData(IS_LOGIN, false);
        if (!login){
            context.startActivity(new Intent(context, OverallLoginActivity.class));
            return false;
        }else {
            return true;
        }
    }

    /**
     * 不需要显示登录框
     * @return
     */
    public static boolean isCurrentLoginNoDialog(){
        boolean login = Boolean.parseBoolean(SharedPreferencesUtils.getData(IS_LOGIN, false).toString());
        return login;
    }
    /**
     * 获取用户id
     * @return
     */
    public static String getUserId(){
        OverallUserInfoEntity entity = UserOperateUtil.getUserInfo();
        if (null != entity){
            return String.valueOf(entity.id);
        }else {
            return "";
        }
    }
    /**
     * 获取聊天页面的我的身份的实体
     * @return
     */
    public static WechatUserEntity getMySelf(){
        WechatUserEntity mySelf = SharedPreferencesUtils.getBeanFromSp(MY_SELF);
        return mySelf;
    }
    /**
     * 获取聊天页面的对方的身份的实体
     * @return
     */
    public static WechatUserEntity getOtherSide(){
        WechatUserEntity otherSide = SharedPreferencesUtils.getBeanFromSp(OTHER_SIDE);
        return otherSide;
    }
    /**
     * 获取QQ聊天页面的我的身份的实体
     * @return
     */
    public static WechatUserEntity getQQMySelf(){
        WechatUserEntity mySelf = SharedPreferencesUtils.getBeanFromSp(QQ_ME_SELF);
        return mySelf;
    }
    /**
     * 获取QQ聊天页面的对方的身份的实体
     * @return
     */
    public static WechatUserEntity getQQOtherSide(){
        WechatUserEntity otherSide = SharedPreferencesUtils.getBeanFromSp(QQ_OTHER_SIDE);
        return otherSide;
    }
    /**
     * 获取支付宝聊天页面的对方的身份的实体
     * @return
     */
    public static WechatUserEntity getAlipayOtherSide(){
        WechatUserEntity otherSide = SharedPreferencesUtils.getBeanFromSp(ALIPAY_OTHER_SIDE);
        return otherSide;
    }
    /**
     * 获取支付宝聊天页面的我的身份的实体
     * @return
     */
    public static WechatUserEntity getAlipayMySelf(){
        WechatUserEntity mySelf = SharedPreferencesUtils.getBeanFromSp(ALIPAY_ME_SELF);
        return mySelf;
    }
    /**
     * 获取微信模拟器聊天页面的对方的身份的实体
     * @return
     */
    public static WechatUserEntity getWechatSimulatorOtherSide(){
        WechatUserEntity otherSide = SharedPreferencesUtils.getBeanFromSp(WECHAT_SIMULATOR_OTHER_SIDE);
        return otherSide;
    }
    /**
     * 获取微信模拟器页面的我的身份的实体
     * @return
     */
    public static WechatUserEntity getWechatSimulatorMySelf(){
        WechatUserEntity mySelf = SharedPreferencesUtils.getBeanFromSp(WECHAT_SIMULATOR_MY_SIDE);
        return mySelf;
    }
    /**
     * 获取用户头像
     * @return
     */
    public static String getUserAvatar(){
        String avatar = SharedPreferencesUtils.getData(USER_AVATAR, "").toString();
        return avatar;
    }
    /**
     * 获取用户头像
     * @return
     */
    public static String getUserNickName(){
        String nickName = SharedPreferencesUtils.getData(USER_NICKNAME, "微商截图器").toString();
        return nickName;
    }
    /**
     * 获取用户邀请了多少好友
     * @return
     */
    public static String getShareNumber(){
        String number = SharedPreferencesUtils.getData(USER_SHARE_NUMBER, -1).toString();
        return number;
    }
    /**
     * 是否正在爆粉
     * @return
     */
    public static boolean isExploring(){
        boolean number = (boolean) SharedPreferencesUtils.getData(EXPLORING, false);
        return number;
    }
    /**
     * 获取用户完整的信息
     * @return
     */
    public static OverallUserInfoEntity getUserInfo(){
        OverallUserInfoEntity user = SharedPreferencesUtils.getBeanFromSp(USER_INFO);
        return user;
    }
    /**
     * 获取用户是否是Vip
     * @return
     */
    public static boolean isVip(){
        boolean isVip = (boolean) SharedPreferencesUtils.getData(USER_IS_VIP, false);
        return isVip;
    }

    /**
     * 是否同意截图的特别声明
     * @return
     */
    public static boolean screenShotAgreememt(){
        boolean agree = (boolean) SharedPreferencesUtils.getData(SCREENSHOT_AGREEMENT, false);
        return agree;
    }
    /**
     * 是否需要显示微信模拟器的覆盖层
     * @return
     */
    public static boolean hideWechatCover(){
        boolean hide = (boolean) SharedPreferencesUtils.getData(WECHAT_COVER, false);
        return hide;
    }

    /**
     * 保存用户信息
     * @param entity
     */
    public static void saveUser(OverallUserInfoEntity entity){
        SharedPreferencesUtils.saveBean2Sp(entity, SharedPreferenceKey.USER_INFO);
    }

    public static ArrayList<OverallIndustryEntity> getIndustrys(){
        ArrayList<OverallIndustryEntity> list = (ArrayList<OverallIndustryEntity>) SharedPreferencesUtils.getListData(SharedPreferenceKey.INDUSTRY, OverallIndustryEntity.class);
        return list;
    }
    public static ArrayList<OverallIndustryEntity> getGroupType(){
        ArrayList<OverallIndustryEntity> list = (ArrayList<OverallIndustryEntity>) SharedPreferencesUtils.getListData(SharedPreferenceKey.HEAPSORT, OverallIndustryEntity.class);
        return list;
    }

    /**
     * 手机联系人
     * @return
     */
    public static ArrayList<ContactEntity> getContactList(){
        ArrayList<ContactEntity> list = (ArrayList<ContactEntity>) SharedPreferencesUtils.getListData(SharedPreferenceKey.CONTACT, ContactEntity.class);
        return list;
    }
    public static void deleteContactCache(Context context){
        SharedPreferencesUtils.remove(SharedPreferenceKey.CONTACT);
    }

    public static PostRequest getChannelParams(Context context){
        PostRequest request = EasyHttp.post(HttpConfig.INDEX)
                .params("way", "init")
                .params("place", SystemInfoUtils.getChannel(context))
                .params("appversion", AppUtils.getVersionName(context))
                .params("appname", context.getString(R.string.app_name))
                .params("deviceid", UserOperateUtil.getUUID())
//                .params("deviceid", SystemInfoUtils.getIMEI(context))
                .params("devicemodel", SystemInfoUtils.getModelName())
                .params("code", String.valueOf(SystemInfoUtils.getAppVersionCode(context)))
                .params("systemtype", "android")
                .params("uid", UserOperateUtil.getUserId());
        return request;
    }

    /**
     * 获取记录的时间
     * @return
     */
    public static Date getTime(){
        Date date = SharedPreferencesUtils.getBeanFromSp(DATE);
        return date;
    }

    /**
     * 获取号段加粉的次数
     * 普通会员每天可以导入三次
     * VIP每天5次
     * @return
     */
    public static int getHaoduanTimes(){
        int times = (int) SharedPreferencesUtils.getData(TIMES, 0);
        return times;
    }
    /**
     * 获取微币数量
     * @return
     */
    public static int getMyWb(){
        int wb = (int) SharedPreferencesUtils.getData(WB, 0);
        return wb;
    }

    /**
     * 获取手机唯一标示
     * @return
     */
    public static String getUUID(){
        String str = SharedPreferencesUtils.getData(UUIDKEY, "-1").toString();
        if (str.equals("-1")){//没有获取到唯一标示,就去保存一个
            UUID uuid = UUID.randomUUID();
            String uniqueId = uuid.toString();
            SharedPreferencesUtils.putData(UUIDKEY, uniqueId);
            Log.e("UUID -- 生成的", uniqueId);
            return uniqueId;
        }
        Log.e("UUID--获取到了", str);
        return str;
    }

    /**
     * 获取QQ的未读消息数量
     * @return
     */
    public static String getQQUnReadNumber(){
        String number = SharedPreferencesUtils.getData(QQ_UN_READ_NUMBER, "20").toString();
        return number;
    }
    /**
     * 获取QQ对方的在线状态
     * @return
     */
    public static String getQQOtherStatus(){
        String number = SharedPreferencesUtils.getData(QQ_OTHER_STATUS, "手机在线 - WIFI").toString();
        return number;
    }
    /**
     * 获取微信是否需要聊天背景
     * @return
     */
    public static ChangeSingleTaklBgEntity getSingleTalkBg(){
        ChangeSingleTaklBgEntity entity = SharedPreferencesUtils.getBeanFromSp(SINGLE_TALK_BG);
        if (null == entity){
            return new ChangeSingleTaklBgEntity(false, "");
        }
        return entity;
    }
    /**
     * 获取支付宝是否需要聊天背景
     * @return
     */
    public static ChangeSingleTaklBgEntity getAlipayChatBg(){
        ChangeSingleTaklBgEntity entity = SharedPreferencesUtils.getBeanFromSp(ALIPAY_CHAT_BG);
        if (null == entity){
            return new ChangeSingleTaklBgEntity(false, "");
        }
        return entity;
    }

    /**
     * 获取微信模拟器的聊天背景
     * @return
     */
    public static ChangeSingleTaklBgEntity getWechatSimulatorBg(){
        ChangeSingleTaklBgEntity entity = SharedPreferencesUtils.getBeanFromSp(ALIPAY_CHAT_BG);
        if (null == entity){
            return new ChangeSingleTaklBgEntity(false, "");
        }
        return entity;
    }
    /**
     * 获取微信是否需要聊天背景
     * @return
     */
    public static ChangeSingleTaklBgEntity getQQSingleTalkBg(){
        ChangeSingleTaklBgEntity entity = SharedPreferencesUtils.getBeanFromSp(QQ_CHAT_BG);
        if (null == entity){
            return new ChangeSingleTaklBgEntity(false, "");
        }
        return entity;
    }

    /**
     * 是否有未读的朋友圈提示
     * @return
     */
    public static boolean hasUnReadFriendCircle(){
        boolean has = (boolean) SharedPreferencesUtils.getData(UNREAD_FRIEND_CIRCLE, false);
        return has;
    }

    /**
     * 获取项目市场中的分类
     * @return
     */
    public static ArrayList<OverallIndustryEntity> getProjectClassify(){
        ArrayList<OverallIndustryEntity> list = (ArrayList<OverallIndustryEntity>) SharedPreferencesUtils.getListData(SharedPreferenceKey.PROJECT_CLASSIFY, OverallIndustryEntity.class);
        return list;
    }
    /**
     * 获取微信模拟器中的银行卡信息
     * @return
     */
    public static List<WechatBankEntity> getWechatSimulatorBank(){
        List<WechatBankEntity> list = SharedPreferencesUtils.getListData(SharedPreferenceKey.WECHAT_SIMULATOR_BANK_LIST, WechatBankEntity.class);
        return list;
    }



    /**
     * 是不是_360的渠道包
     * @return
     */
    public static boolean is360Channel(){
        String channelName = SharedPreferencesUtils.getData(CHANNEL_NAME, "").toString();
        if ("_360".equals(channelName)){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 是不是Huawei的渠道包
     * @return
     */
    public static boolean isHuaweiChannel(){
        String channelName = SharedPreferencesUtils.getData(CHANNEL_NAME, "").toString();
        if ("huawei".equals(channelName)){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 是不是xiaomi的渠道包
     * @return
     */
    public static boolean isXiaomiChannel(){
        String channelName = SharedPreferencesUtils.getData(CHANNEL_NAME, "").toString();
        if ("xiaomi".equals(channelName)){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 是不是Vivo的渠道包
     * @return
     */
    public static boolean isVivoChannel(){
        String channelName = SharedPreferencesUtils.getData(CHANNEL_NAME, "").toString();
        if ("vivo".equals(channelName)){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 是不是OPPO的渠道包
     * @return
     */
    public static boolean isOppoChannel(){
        String channelName = SharedPreferencesUtils.getData(CHANNEL_NAME, "").toString();
        if ("oppo".equals(channelName)){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 是不是tencent的渠道包
     * @return
     */
    public static boolean isTencentChannel(){
        String channelName = SharedPreferencesUtils.getData(CHANNEL_NAME, "").toString();
        if ("tencent".equals(channelName)){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 是不是百度的渠道包
     * @return
     */
    public static boolean isBaiduChannel(){
        String channelName = SharedPreferencesUtils.getData(CHANNEL_NAME, "").toString();
        if ("baidu".equals(channelName)){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 是不是Wandoujia的渠道包
     * @return
     */
    public static boolean isWandoujiaChannel(){
        String channelName = SharedPreferencesUtils.getData(CHANNEL_NAME, "").toString();
        if ("wandoujia".equals(channelName)){
            return true;
        }else {
            return false;
        }
    }
}
