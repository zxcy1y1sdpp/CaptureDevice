package app.jietuqi.cn.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.view.Gravity;

import java.util.Locale;

import app.jietuqi.cn.ui.activity.OverallLoginActivity;
import app.jietuqi.cn.widget.dialog.customdialog.EnsureDialog;

/**
 * @author lyb
 * created at 17/6/30 下午8:50
 * be used for 项目中一些公用的方法
 * 1:和客服聊天（不发送商品轨迹）
 */

public class AppUtils {
	/**
	 * 版本名
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}
	/**
	 * @return 当前程序的版本名称
	 */
//	public static String getVersionName(Context context) {
//		String version;
//		try {
//			PackageManager pm = context.getPackageManager();
//			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
//			version = packageInfo.versionName;
//		} catch (Exception e) {
//			e.printStackTrace();
//			version = "";
//		}
//		return version;
//	}

	/**
	 * 获取版本号
	 * @param context
	 * @return
	 */
	public static String getVersionCode(Context context) {
		String code = String.valueOf(getPackageInfo(context).versionCode);
		return code;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;

		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pi;
	}

	/**
	 * 跳转登录页面
	 * @param context
	 */
	public static void showLogin(final Context context) {
		new EnsureDialog(context).builder()
				.setGravity(Gravity.CENTER)//默认居中，可以不设置
				.setTitle("您尚未登录！")//可以不设置标题颜色，默认系统颜色
				.setSubTitle("请前去登录")
				.setNegativeButton("取消", v -> {

				})
				.setPositiveButton("登录", v -> {
					Intent intent = new Intent(context, OverallLoginActivity.class);
					intent.putExtra("fromDialog", true);
					context.startActivity(intent);
				}).show();
	}

	/**
	 * 判断字符串第一个字符是否是中文字符
	 * @param str
	 * @return
	 */
	public static boolean isCnorEn(String str) {
		final char c = str.charAt(0);
		return (c >= 0x0391 && c <= 0xFFE5);
	}

	/**
	 * 获取当前手机系统语言。
	 *
	 * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
	 */
	public static String getSystemLanguage() {
		return Locale.getDefault().getLanguage();
	}

	/**
	 * 获取当前系统上的语言列表(Locale列表)
	 *
	 * @return  语言列表
	 */
	public static Locale[] getSystemLanguageList() {
		return Locale.getAvailableLocales();
	}

	/**
	 * 获取当前手机系统版本号
	 *
	 * @return  系统版本号
	 */
	public static String getSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取手机型号
	 *
	 * @return  手机型号
	 */
	public static String getSystemModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取手机厂商
	 *
	 * @return  手机厂商
	 */
	public static String getDeviceBrand() {
		return android.os.Build.BRAND;
	}

	/**
	 * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
	 *
	 * @return  手机IMEI
	 */
	@SuppressLint("MissingPermission")
	public static String getIMEI(Context ctx) {
		TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
		if (tm != null) {
			return tm.getDeviceId();
		}
		return null;
	}
}
