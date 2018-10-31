package dasheng.com.capturedevice;

import android.app.Application;

import dasheng.com.capturedevice.database.DatabaseUtils;

/**
 * 作者： liuyuanbo on 2018/10/9 10:43.
 * 时间： 2018/10/9 10:43
 * 邮箱： 972383753@qq.com
 * 用途： 自定义的Application
 */

public class App extends Application {
    private static App mInstant;
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseUtils.initHelper(this);

    }
    // 单例模式中获取唯一的MyApplication实例
    public static App getInstance() {
        if (null == mInstant) {
            mInstant = new App();
        }
        return mInstant;
    }
}
