package app.jietuqi.cn.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import java.util.ArrayList;

import app.jietuqi.cn.constant.SharedPreferenceKey;
import app.jietuqi.cn.entity.OverallUserInfoEntity;
import app.jietuqi.cn.ui.activity.OverallLoginActivity;
import app.jietuqi.cn.ui.entity.ContactEntity;
import app.jietuqi.cn.ui.entity.OverallIndustryEntity;

import static app.jietuqi.cn.constant.SharedPreferenceKey.IS_LOGIN;
import static app.jietuqi.cn.constant.SharedPreferenceKey.USER_AVATAR;
import static app.jietuqi.cn.constant.SharedPreferenceKey.USER_ID;
import static app.jietuqi.cn.constant.SharedPreferenceKey.USER_INFO;
import static app.jietuqi.cn.constant.SharedPreferenceKey.USER_NICKNAME;

/**
 * 作者： liuyuanbo on 2018/11/12 17:03.
 * 时间： 2018/11/12 17:03
 * 邮箱： 972383753@qq.com
 * 用途： app中用到的一些基本的方法
 * 例如： 判断用户是否登录之类的工具类
 */
public class UserOperateUtil {
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
        String userId = SharedPreferencesUtils.getData(USER_ID, "").toString();
        if (TextUtils.isEmpty(userId)){
            return "";
        }else {
            return userId;
        }
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
     * 获取用户头像
     * @return
     */
    public static OverallUserInfoEntity getUserInfo(){
        OverallUserInfoEntity user = SharedPreferencesUtils.getBeanFromSp(USER_INFO);
        return user;
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
        SharedPreferencesUtils.remove(context, SharedPreferenceKey.CONTACT);
    }
}
