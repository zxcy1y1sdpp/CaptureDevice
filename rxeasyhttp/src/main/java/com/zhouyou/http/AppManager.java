package com.zhouyou.http;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 作者： liuyuanbo on 2018/11/15 14:45.
 * 时间： 2018/11/15 14:45
 * 邮箱： 972383753@qq.com
 * 用途： Activity管理类，用于管理Activity,程序退出
 */
public class AppManager {

    private static CopyOnWriteArrayList<Activity> mActivityStack;
    private static AppManager mAppManager;
    private AppManager() { }
    /**
     * 单一实例
     */
    public static AppManager getInstance() {
        if (mAppManager == null) {
            mAppManager = new AppManager();
        }
        return mAppManager;
    }
    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new CopyOnWriteArrayList<Activity>();
        }
        mActivityStack.add(activity);
    }

    public Activity getTopActivity(){
        return mActivityStack.get(mActivityStack.size() - 1);
    }
    /**
     * 结束指定的Activity
     */
    public void killActivity(Activity activity) {
        try {
            if (activity != null) {
                mActivityStack.remove(activity);
                activity.finish();
                activity = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 结束指定类名的Activity
     */
    public void killActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                killActivity(activity);
            }
        }
    }
    /**
     * 结束所有Activity
     */
    public void killAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }
    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            killAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
