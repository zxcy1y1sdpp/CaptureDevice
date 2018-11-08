package dasheng.com.capturedevice.network;

import java.io.Serializable;

import io.reactivex.annotations.Nullable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * Created by 刘远博 on 2017/1/17.
 */

public class BaseNetWorkEntity extends ResponseBody implements Serializable {

    public String key;

    @Nullable
    @Override
    public MediaType contentType() {
        return null;
    }

    @Override
    public long contentLength() {
        return 0;
    }

    @Override
    public BufferedSource source() {
        return null;
    }
}
