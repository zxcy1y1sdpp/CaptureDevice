package dasheng.com.capturedevice.network;

import android.util.ArrayMap;

/**
 * 作者： liuyuanbo on 2018/11/7 11:53.
 * 时间： 2018/11/7 11:53
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class NetWorkParams {
    private static NetWorkParams mNetWorkParams;
    private NetWorkParams(){}
    public static NetWorkParams getInstance(){
        if (mNetWorkParams == null) {
            mNetWorkParams = new NetWorkParams();
        }
        return mNetWorkParams;
    }

    public void getAdvertisemen() {
        ArrayMap<String, String> map = new ArrayMap<>();
//        RetrofitClient.getInstence().API(AdvertisementBean.class).PostAPI(Urls.INSTANCE.getADVERTISMENT_GET(), aesOperatorParams(map), new APICallback(context, listener, 0, AdvertisementBean.class));
    }
}
