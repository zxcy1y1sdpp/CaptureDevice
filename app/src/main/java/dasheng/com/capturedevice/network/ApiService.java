package dasheng.com.capturedevice.network;

/**
 * Created by Nathan on 15/5/22.
 */

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * 升级retrofit改动的代码------------------开始
 * @author yuanboliu
 */
public interface ApiService {
    @GET()
    Observable<APIResponse<Object>> GetAPI(@Url String url, @QueryMap Map<String, String> maps);

    /**
     * POST带参数
     * @param url
     * @param maps
     * @return
     */
    @FormUrlEncoded
    @POST()
    Observable<APIResponse<Object>> PostAPI(@Url String url, @FieldMap Map<String, String> maps);
}
/** 升级retrofit改动的代码------------------结束*/