package dasheng.com.capturedevice.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 作者： liuyuanbo on 2018/11/7 11:32.
 * 时间： 2018/11/7 11:32
 * 邮箱： 972383753@qq.com
 * 用途： 构建Retrofit的客服端
 */

public class RetrofitClient {
    private static RetrofitClient mRetrofitClient;
    private static  ApiService mAPIFunction;
    private RetrofitClient(){
        OkHttpClient mOkHttpClient=new OkHttpClient.Builder()
                .connectTimeout(HttpConfig.HTTP_FAIL_TIME, TimeUnit.SECONDS)
                .readTimeout(HttpConfig.HTTP_FAIL_TIME, TimeUnit.SECONDS)
                .writeTimeout(HttpConfig.HTTP_FAIL_TIME, TimeUnit.SECONDS)
                .addInterceptor(InterceptorUtil.HeaderInterceptor())
                //添加日志拦截器
                .addInterceptor(InterceptorUtil.LogInterceptor())
                .build();

        Retrofit mRetrofit=new Retrofit.Builder()
                .baseUrl(HttpConfig.BASE_URL)
                //添加gson转换器
                .addConverterFactory(CustomGsonConverterFactory.create())
                //添加rxjava转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
        mAPIFunction = mRetrofit.create(ApiService.class);

    }

    public static RetrofitClient getInstence(){
        if (mRetrofitClient==null){
            synchronized (RetrofitClient.class) {
                if (mRetrofitClient == null) {
                    mRetrofitClient = new RetrofitClient();
                }
            }

        }
        return mRetrofitClient;
    }
    public ApiService API(){
        return mAPIFunction;
    }
}
