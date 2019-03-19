package app.jietuqi.cn.ui.alipayscreenshot;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

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
        /*
         * int compare(Student o1, Student o2) 返回一个基本类型的整型，
         * 返回负数表示：o1 小于o2，
         * 返回0 表示：o1和o2相等，
         * 返回正数表示：o1大于o2。
         */

        int mHeaderViewHeight;
//        EnsureDialog ensureDialog = null;
    }

}
