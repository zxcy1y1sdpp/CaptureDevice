package dasheng.com.capturedevice.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author yemao
 * @date 2017/4/9
 * @description 拦截器工具类!
 */

public class InterceptorUtil {
	public static String TAG="----";
	//日志拦截器
	public static HttpLoggingInterceptor LogInterceptor(){
		return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
			@Override
			public void log(String message) {
				Log.w(TAG, "log: "+message );
			}
		}).setLevel(HttpLoggingInterceptor.Level.BODY);//设置打印数据的级别
	}

	public static Interceptor HeaderInterceptor(){
		return new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {
				//偷天换日
				Request oldRequest = chain.request();

				//拿到拥有以前的request里的url的那些信息的builder
//				HttpUrl.Builder builder = oldRequest
//						.url()
//						.newBuilder();

				//得到新的url（已经追加好了参数）
				/*HttpUrl newUrl = builder.addQueryParameter("deviceId", "12345")
						.addQueryParameter("token", "i_am_token")
						.addQueryParameter("appVersion", "1.0.0-beta")
						.build();*/

				//利用新的Url，构建新的request，并发送给服务器
//				Request newRequest = oldRequest
//						.newBuilder()
//						.url(newUrl)
//						.build();
//				Request mRequest=chain.request();
//				Log.e("intercept", mRequest.url().url())
//				//在这里你可以做一些想做的事,比如token失效时,重新获取token
//				//或者添加header等等,PS我会在下一篇文章总写拦截token方法
				return chain.proceed(oldRequest);
			}
		};
	}
}

