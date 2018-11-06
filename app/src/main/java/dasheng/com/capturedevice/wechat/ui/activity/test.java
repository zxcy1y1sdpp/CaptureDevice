package dasheng.com.capturedevice.wechat.ui.activity;

import android.app.Activity;

import java.util.Random;

/**
 * 作者： liuyuanbo on 2018/10/11 10:37.
 * 时间： 2018/10/11 10:37
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class test extends Activity{
    char[] str = {'0','1','2','3','4','5','6','7','8','9'};
    public test() {
    }

    public void test() {

        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        int count = 0;
        int i; // 生成的随机数
        while (count < 28) {
            int index = (int) (Math.random() * str.length);
            i = str[index];
            pwd.append(i);
            count++;
        }

    }


}
