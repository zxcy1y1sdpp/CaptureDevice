package app.jietuqi.cn.ui.alipayscreenshot;

import android.app.Activity;
import android.content.Intent;

/**
 * 作者： liuyuanbo on 2018/12/10 17:56.
 * 时间： 2018/12/10 17:56
 * 邮箱： 972383753@qq.com
 * 用途：
 */
public class test extends Activity {

    public test(){
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq");
        startActivity(intent);

    }

}
