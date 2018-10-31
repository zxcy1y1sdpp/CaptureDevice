package dasheng.com.capturedevice;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

/**
 * @作者：liuyuanbo
 * @时间：2018/9/30
 * @邮箱：972383753@qq.com
 * @用途：
 */
@GlideModule
public class GlobalGlideConfig extends AppGlideModule {

	@Override
	public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
		super.applyOptions(context, builder);
//		int memoryCacheSizeBytes = 1024 * 1024 * 20; // 20mb
//		int diskCacheSizeBytes = 1024 * 1024 * 100;  //100 MB
//		builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes))
//				.setDiskCache(new InternalCacheDiskCacheFactory(context, diskCacheSizeBytes));
	}

	@Override
	public boolean isManifestParsingEnabled() {
		return false;
	}

	@Override
	public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
		super.registerComponents(context, glide, registry);
//		registry.append(String.class, InputStream.class,new CustomBaseGlideUrlLoader.Factory());
	}
}

