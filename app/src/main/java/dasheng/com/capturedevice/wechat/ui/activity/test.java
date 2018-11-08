package dasheng.com.capturedevice.wechat.ui.activity;

import android.app.Activity;
import android.util.ArrayMap;

import java.util.Random;

import dasheng.com.capturedevice.network.APIResponse;
import dasheng.com.capturedevice.network.BaseObserver;
import dasheng.com.capturedevice.network.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import lyb.retrofittest.newretrofit.AdvertisementBean;

import static dasheng.com.capturedevice.network.AESOperatorUtils.aesOperatorParams;

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
        //订阅者（网络请求回调）
        BaseObserver<AdvertisementBean> observer = new BaseObserver<AdvertisementBean>() {
            @Override
            protected void onSuccees(APIResponse<AdvertisementBean> t) throws Exception {

            }

            @Override
            protected void onFailure(Throwable e, boolean isNetWorkError) throws Exception {

            }
        };
        ArrayMap<String, String> map = new ArrayMap<>();
        RetrofitClient.getInstence().API().PostAPI("advertisement/get", aesOperatorParams(map))
                .subscribeOn(Schedulers.io())//指定网络请求在io后台线程中进行
                .observeOn(AndroidSchedulers.mainThread())//指定observer回调在UI主线程中进行
                .subscribe ((Consumer<? super APIResponse<Object>>) observer);
    }

}
