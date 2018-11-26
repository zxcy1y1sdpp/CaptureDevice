package app.jietuqi.cn.wechat.ui.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Random;

import app.jietuqi.cn.ui.entity.OverallIndustryEntity;
import app.jietuqi.cn.util.UserOperateUtil;

/**
 * 作者： liuyuanbo on 2018/10/11 10:37.
 * 时间： 2018/10/11 10:37
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class test extends Activity{
    char[] str = {'0','1','2','3','4','5','6','7','8','9'};
    private static final int asldkfj = 1;
    public test() {
    }

    public void test() {
        ArrayList<OverallIndustryEntity> list = UserOperateUtil.getIndustrys();
        for (int i = 0; i < 10; i++) {
            
        }
        for (OverallIndustryEntity entity : UserOperateUtil.getIndustrys()) {

        }
        StringBuffer pwd = new StringBuffer();
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
