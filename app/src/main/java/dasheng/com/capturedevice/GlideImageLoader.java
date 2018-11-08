package dasheng.com.capturedevice;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import dasheng.com.capturedevice.widget.ninegrid.NineGridView;

/**
 * 作者： liuyuanbo on 2018/11/7 17:27.
 * 时间： 2018/11/7 17:27
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class GlideImageLoader implements NineGridView.ImageLoader {
    @Override
    public void onDisplayImage(Context context, ImageView imageView, String url) {
        GlideApp.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .error(R.mipmap.loading)
                .fallback(R.mipmap.loading)
                .into(imageView);
//        int width = ScreenUtil.INSTANCE.getScreenWidth(context) / 7 * 3;
//        GlideApp.with(context)
//                .load(url)
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .thumbnail(0.1f)
//                .error(R.mipmap.loading)
//                .fallback(R.mipmap.loading)
//                .override(width)
//                .into(imageView);
    }

    @Override
    public Bitmap getCacheImage(String url) {
        return null;
    }
}
